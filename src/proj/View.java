package proj;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.*;

public class View extends JFrame {
    private Model model;
    private Controller controller;

    private JButton rollButton, scoreButton, newGameButton;
    private JToggleButton[] diceButtons;
    private JLabel[] computerDiceLabels;
    private JLabel turnLabel;

    private Cup cup = new Cup();
    private JPanel playerDicePanel, computerDicePanel;
    private JPanel allScoreboardsPanel;

    private ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private Player currentPlayer;

    private Map<Player, Scoreboard> playerScoreboards = new HashMap<>();
    private Map<Player, Map<String, JLabel>> playerScoreboardLabels = new HashMap<>();

    private Score scoreCalculator = new Score();

    public View() {
        PlayerLibrary library = new PlayerLibrary();

        // Setup players
        String humanInput = JOptionPane.showInputDialog(this, "Enter number of human players:");
        if (humanInput == null) System.exit(0);
        int numHumans = Integer.parseInt(humanInput);

        String aiInput = JOptionPane.showInputDialog(this, "Enter number of computer players:");
        if (aiInput == null) System.exit(0);
        int numAIs = Integer.parseInt(aiInput);

        for (int i = 1; i <= numHumans; i++) {
            Player p = new Player("Human " + i, Player.PlayerType.HUMAN);
            players.add(p);
            library.addPlayer(p);
        }

        for (int i = 1; i <= numAIs; i++) {
            String[] difficulties = { "Easy", "Hard" };
            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "Choose difficulty for Computer " + i + ":",
                    "Difficulty Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    difficulties,
                    difficulties[0]
            );
            if (choice == null) System.exit(0);

            Player p;
            if (choice.equals("Easy")) {
                p = new ComputerEasy();
            } else {
                p = new ComputerHard();
            }

            // ✅ After creating, immediately reset their cup
            if (p instanceof ComputerEasy) {
                ((ComputerEasy) p).resetCup();
            } else if (p instanceof ComputerHard) {
                ((ComputerHard) p).resetCup();
            }

            players.add(p);
            library.addPlayer(p);
        }

        currentPlayer = players.get(0);
        model = new Model();
        controller = new Controller(model, this);

        setupUI();
        turnLabel.setText("Current Turn: " + currentPlayer.getName());
    }

    private void setupUI() {
        setTitle("Yahtzee Game");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        turnLabel = new JLabel("Current Turn: ", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(turnLabel, BorderLayout.NORTH);

        playerDicePanel = new JPanel(new GridLayout(1, 5));
        diceButtons = new JToggleButton[5];
        for (int i = 0; i < 5; i++) {
            diceButtons[i] = new JToggleButton("?");
            diceButtons[i].setFont(new Font("Arial", Font.BOLD, 36));
            playerDicePanel.add(diceButtons[i]);
        }

        computerDicePanel = new JPanel(new GridLayout(1, 5));
        computerDiceLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            computerDiceLabels[i] = new JLabel("?", SwingConstants.CENTER);
            computerDiceLabels[i].setFont(new Font("Arial", Font.BOLD, 36));
            computerDicePanel.add(computerDiceLabels[i]);
        }

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(playerDicePanel);
        centerPanel.add(computerDicePanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        rollButton = new JButton("Roll");
        rollButton.setActionCommand("ROLL");
        rollButton.addActionListener(controller);
        scoreButton = new JButton("Score");
        scoreButton.setActionCommand("SCORE");
        scoreButton.addActionListener(controller);
        newGameButton = new JButton("New Game");
        newGameButton.setActionCommand("NEW_GAME");
        newGameButton.addActionListener(controller);
        buttonPanel.add(rollButton);
        buttonPanel.add(scoreButton);
        buttonPanel.add(newGameButton);
        add(buttonPanel, BorderLayout.SOUTH);

        allScoreboardsPanel = new JPanel();
        allScoreboardsPanel.setLayout(new GridLayout(players.size(), 1));

        for (Player p : players) {
            Scoreboard sb = new Scoreboard();
            playerScoreboards.put(p, sb);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(14, 1));
            panel.setBorder(BorderFactory.createTitledBorder(p.getName()));

            Map<String, JLabel> labels = new HashMap<>();
            List<String> categories = Arrays.asList(
                "Ones", "Twos", "Threes", "Fours", "Fives", "Sixes",
                "Three of a Kind", "Four of a Kind", "Full House",
                "Small Straight", "Large Straight", "Yahtzee", "Chance"
            );
            for (String cat : categories) {
                JLabel label = new JLabel(cat + ": -");
                label.setFont(new Font("Arial", Font.PLAIN, 16));
                panel.add(label);
                labels.put(cat, label);
            }
            JLabel total = new JLabel("Total Score: 0");
            total.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(total);
            labels.put("Total", total);

            playerScoreboardLabels.put(p, labels);

            allScoreboardsPanel.add(panel);
        }

        JScrollPane scrollPane = new JScrollPane(allScoreboardsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.EAST);
    }

    public void rollDice() {
        if (!(currentPlayer.getType() == Player.PlayerType.HUMAN)) {
            JOptionPane.showMessageDialog(this, "It is not your turn to roll!");
            return;
        }
        if (!currentPlayer.ifcanroll()) {
            JOptionPane.showMessageDialog(this, "You cannot roll more than 3 times. Please score.");
            return;
        }
        ArrayList<Dice> inDice = cup.getInDice();
        for (int i = 0; i < inDice.size(); i++) {
            if (!diceButtons[i].isSelected()) {
                inDice.get(i).roll();
            }
            diceButtons[i].setText(String.valueOf(inDice.get(i).getCurrentValue().ordinal() + 1));
        }
        currentPlayer.incrementRollCount();
        if (!currentPlayer.ifcanroll()) {
            rollButton.setEnabled(false);
        }
    }

    public void scoreDice() {
        if (!(currentPlayer.getType() == Player.PlayerType.HUMAN)) {
            JOptionPane.showMessageDialog(this, "It is not your turn to score!");
            return;
        }

        ArrayList<Dice> currentDice = cup.getInDice();
        Map<String, Integer> possibleScores = scoreCalculator.evaluate(currentDice);
        ArrayList<String> availableCategories = playerScoreboards.get(currentPlayer).getRemainingCategories();
        Map<String, Integer> filteredScores = new LinkedHashMap<>();

        List<String> order = Arrays.asList(
            "Ones", "Twos", "Threes", "Fours", "Fives", "Sixes",
            "Three of a Kind", "Four of a Kind", "Full House",
            "Small Straight", "Large Straight", "Yahtzee", "Chance"
        );

        for (String cat : order) {
            if (availableCategories.contains(cat) && possibleScores.get(cat) != null && possibleScores.get(cat) > 0) {
                filteredScores.put(cat, possibleScores.get(cat));
            }
        }

        String chosenCategory = null;
        int chosenScore = 0;

        if (filteredScores.isEmpty()) {
            String[] options = availableCategories.toArray(new String[0]);
            chosenCategory = (String) JOptionPane.showInputDialog(
                this,
                "No valid scoring categories!\nPick a category to cross out (score 0):",
                "Forced Category Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );
            chosenScore = 0;
        } else {
            String[] options = filteredScores.keySet().toArray(new String[0]);
            chosenCategory = (String) JOptionPane.showInputDialog(
                this,
                "Choose a category to score:",
                "Select Score",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );
            chosenScore = filteredScores.get(chosenCategory);
        }

        if (chosenCategory == null) return;

        Scoreboard currentBoard = playerScoreboards.get(currentPlayer);
        currentBoard.setScore(chosenCategory, chosenScore);

        updatePlayerScoreboardDisplay(currentPlayer);

        JOptionPane.showMessageDialog(this, "Scored " + chosenScore + " in " + chosenCategory + "!");

        rollButton.setEnabled(true);
        resetDiceButtons();
        cup = new Cup(); // reset

        nextTurn();
    }

    private void updatePlayerScoreboardDisplay(Player p) {
        Scoreboard sb = playerScoreboards.get(p);
        Map<String, JLabel> labels = playerScoreboardLabels.get(p);

        for (String cat : labels.keySet()) {
            if (cat.equals("Total")) {
                labels.get(cat).setText("Total Score: " + sb.getTotalScore());
            } else {
                Integer val = sb.getScores().get(cat);
                labels.get(cat).setText(cat + ": " + (val == null ? "-" : val));
            }
        }
    }

    private void nextTurn() {
        // Step 1: Move to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);

        // Step 2: Update Turn Label
        turnLabel.setText("Current Turn: " + currentPlayer.getName());

        // Step 3: Check if Human or Computer
        if (currentPlayer.getType() == Player.PlayerType.HUMAN) {
            // Human needs to manually roll and score
            JOptionPane.showMessageDialog(this, currentPlayer.getName() + "'s turn!");
            rollButton.setEnabled(true);
            scoreButton.setEnabled(true);
            resetDiceButtons();
            cup = new Cup(); // Reset dice cup for human
        } else {
            // Computer auto-plays after short delay
            rollButton.setEnabled(false);
            scoreButton.setEnabled(false);

            // Show "Computer is thinking..." popup first
            JOptionPane.showMessageDialog(this, "🤖 Wait... Computer is thinking...");

            // Start a short timer before computer plays
            Timer timer = new Timer(1000, e -> {
                if (currentPlayer instanceof ComputerEasy) {
                    ((ComputerEasy) currentPlayer).roll(playerScoreboards.get(currentPlayer));
                } else if (currentPlayer instanceof ComputerHard) {
                    ((ComputerHard) currentPlayer).roll(playerScoreboards.get(currentPlayer));
                }

                // After computer finishes playing, move to next player
                nextTurn();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }


    private void playComputerTurn() {
        // Show quick popup
        JOptionPane.showMessageDialog(this, "🤖 Wait... Computer is thinking...");

        // Start a 1 second timer before the computer actually plays
        Timer timer = new Timer(1000, e -> {
            if (currentPlayer instanceof ComputerEasy) {
                ((ComputerEasy) currentPlayer).roll(playerScoreboards.get(currentPlayer));
            } else if (currentPlayer instanceof ComputerHard) {
                ((ComputerHard) currentPlayer).roll(playerScoreboards.get(currentPlayer));
            }

            // After computer finishes playing, move to next player
            nextTurn();
        });
        timer.setRepeats(false); // only fire once
        timer.start();
    }



    private void resetDiceButtons() {
        for (JToggleButton btn : diceButtons) {
            btn.setSelected(false);
            btn.setText("?");
        }
        cup = new Cup();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View gui = new View();
            gui.setVisible(true);
        });
    }
    
    public void startNewGame() {
        // === Step 0: Update Win/Loss Records if there was a previous game
        if (!players.isEmpty()) {
            Player winner = null;
            int maxScore = Integer.MIN_VALUE;
            for (Player p : players) {
                Scoreboard sb = playerScoreboards.get(p);
                if (sb.getTotalScore() > maxScore) {
                    maxScore = sb.getTotalScore();
                    winner = p;
                }
            }

            if (winner != null) {
                model.recordWin(winner);
                for (Player p : players) {
                    if (p != winner) {
                        model.recordLoss(p);
                    }
                }
            }

            // === Show Win/Loss Popup after the game
            StringBuilder stats = new StringBuilder("🏆 Game Over! Current Stats:\n");
            for (Player p : model.getAllPlayers()) {
                int wins = model.getWins(p);
                int losses = model.getLosses(p);
                stats.append(p.getName()).append(": ").append(wins).append(" Wins, ").append(losses).append(" Losses\n");
            }
            JOptionPane.showMessageDialog(this, stats.toString());
        }

        // === Step 1: Reset all game data
        players.clear();
        playerScoreboards.clear();
        playerScoreboardLabels.clear();
        allScoreboardsPanel.removeAll();

        // === Step 2: Ask for new players
        String humanInput = JOptionPane.showInputDialog(this, "Enter number of human players:");
        if (humanInput == null) System.exit(0);
        int numHumans = Integer.parseInt(humanInput);

        String aiInput = JOptionPane.showInputDialog(this, "Enter number of computer players:");
        if (aiInput == null) System.exit(0);
        int numAIs = Integer.parseInt(aiInput);

        for (int i = 1; i <= numHumans; i++) {
            Player p = new Player("Human " + i, Player.PlayerType.HUMAN);
            players.add(p);
            model.addPlayer(p); // ✅ New: update Model
        }

        for (int i = 1; i <= numAIs; i++) {
            String[] difficulties = { "Easy", "Hard" };
            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "Choose difficulty for Computer " + i + ":",
                    "Difficulty Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    difficulties,
                    difficulties[0]
            );
            if (choice == null) System.exit(0);

            Player p;
            if (choice.equals("Easy")) {
                p = new ComputerEasy();
            } else {
                p = new ComputerHard();
            }
            
            // ✅ After creating, immediately reset their cup!
            if (p instanceof ComputerEasy) {
                ((ComputerEasy) p).resetCup();
            } else if (p instanceof ComputerHard) {
                ((ComputerHard) p).resetCup();
            }

            players.add(p);
            model.addPlayer(p);
        }



        // === Step 3: Create new scoreboards
        allScoreboardsPanel.setLayout(new GridLayout(players.size(), 1));
        for (Player p : players) {
            Scoreboard sb = new Scoreboard();
            playerScoreboards.put(p, sb);

            JPanel panel = new JPanel(new GridLayout(14, 1));
            panel.setBorder(BorderFactory.createTitledBorder(p.getName()));

            Map<String, JLabel> labels = new HashMap<>();
            List<String> categories = Arrays.asList(
                "Ones", "Twos", "Threes", "Fours", "Fives", "Sixes",
                "Three of a Kind", "Four of a Kind", "Full House",
                "Small Straight", "Large Straight", "Yahtzee", "Chance"
            );
            for (String cat : categories) {
                JLabel label = new JLabel(cat + ": -");
                label.setFont(new Font("Arial", Font.PLAIN, 16));
                panel.add(label);
                labels.put(cat, label);
            }
            JLabel total = new JLabel("Total Score: 0");
            total.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(total);
            labels.put("Total", total);

            playerScoreboardLabels.put(p, labels);

            allScoreboardsPanel.add(panel);
        }

        allScoreboardsPanel.revalidate();
        allScoreboardsPanel.repaint();

        // === Step 4: Reset turn and dice
        currentPlayerIndex = 0;
        currentPlayer = players.get(0);
        cup = new Cup();
        resetDiceButtons();

        rollButton.setEnabled(true);
        scoreButton.setEnabled(true);

        turnLabel.setText("Current Turn: " + currentPlayer.getName());
        
    }


}