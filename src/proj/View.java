package proj;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.*;

public class View extends JFrame {
    private Model model;
    private Controller controller;
    private Player currentPlayer;

    private JButton rollButton, scoreButton, newGameButton;
    private JToggleButton[] diceButtons;
    private JLabel[] computerDiceLabels;

    private Cup cup = new Cup();
    private JPanel playerDicePanel, computerDicePanel;
    private JPanel scoreboardPanel, computerScoreboardPanel;
    private Map<String, JLabel> categoryLabels, computerCategoryLabels;

    private Scoreboard scoreboard = new Scoreboard();
    private Scoreboard computerScoreboard = new Scoreboard();
    private Score scoreCalculator = new Score();
    
    private final ImageIcon die1Icon;
    private final ImageIcon die2Icon;
    private final ImageIcon die3Icon;
    private final ImageIcon die4Icon;
    private final ImageIcon die5Icon;
    private final ImageIcon die6Icon;

    private Computer computerPlayer; 

    public View() {
    	int iconWidth = 85;  // or whatever fits your UI
    	int iconHeight = 85;

    	die1Icon = loadScaledIcon("/images/dice1.png", iconWidth, iconHeight);
    	die2Icon = loadScaledIcon("/images/dice2.png", iconWidth, iconHeight);
    	die3Icon = loadScaledIcon("/images/dice3.png", iconWidth, iconHeight);
    	die4Icon = loadScaledIcon("/images/dice4.png", iconWidth, iconHeight);
    	die5Icon = loadScaledIcon("/images/dice5.png", iconWidth, iconHeight);
    	die6Icon = loadScaledIcon("/images/dice6.png", iconWidth, iconHeight);
    	
        PlayerLibrary library = new PlayerLibrary();
        currentPlayer = new Player("You", Player.PlayerType.HUMAN);
        library.addPlayer(currentPlayer);
        model = new Model(library);
        controller = new Controller(model, this);

        // Ask player for computer difficulty
        String[] difficulties = { "Easy", "Hard" };
        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Choose computer difficulty:",
                "Difficulty Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                difficulties,
                difficulties[0]
        );
        
        if (choice == null) {
            JOptionPane.showMessageDialog(this, "You must select a difficulty to start the game. Exiting...");
            System.exit(0);
        }
        
        if (choice.equals("Easy")) {
            computerPlayer = new ComputerEasy();
        } else {
            computerPlayer = new ComputerHard();
        }

        setupUI();
    }
    
    private ImageIcon loadScaledIcon(String path, int width, int height) {
        ImageIcon original = new ImageIcon(getClass().getResource(path));
        Image scaledImage = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void setupUI() {
        setTitle("Yahtzee Game");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        scoreboardPanel = new JPanel(new GridLayout(14, 1));
        categoryLabels = new HashMap<>();
        computerScoreboardPanel = new JPanel(new GridLayout(14, 1));
        computerCategoryLabels = new HashMap<>();

        java.util.List<String> categories = Arrays.asList(
            "Ones", "Twos", "Threes", "Fours", "Fives", "Sixes",
            "Three of a Kind", "Four of a Kind", "Full House",
            "Small Straight", "Large Straight", "Yahtzee", "Chance"
        );

        for (String cat : categories) {
            JLabel pl = new JLabel(cat + ": -");
            pl.setFont(new Font("Arial", Font.PLAIN, 16));
            scoreboardPanel.add(pl);
            categoryLabels.put(cat, pl);

            JLabel cl = new JLabel(cat + ": -");
            cl.setFont(new Font("Arial", Font.PLAIN, 16));
            computerScoreboardPanel.add(cl);
            computerCategoryLabels.put(cat, cl);
        }

        JLabel total1 = new JLabel("Total Score: 0");
        total1.setFont(new Font("Arial", Font.BOLD, 18));
        scoreboardPanel.add(total1);
        categoryLabels.put("Total", total1);

        JLabel total2 = new JLabel("Total Score: 0");
        total2.setFont(new Font("Arial", Font.BOLD, 18));
        computerScoreboardPanel.add(total2);
        computerCategoryLabels.put("Total", total2);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.add(scoreboardPanel);
        rightPanel.add(computerScoreboardPanel);
        add(rightPanel, BorderLayout.EAST);
    }
    
    private void setDieIcon(JComponent comp, int faceValue) {
    	 ImageIcon icon = switch (faceValue) {
         case 1 -> die1Icon;
         case 2 -> die2Icon;
         case 3 -> die3Icon;
         case 4 -> die4Icon;
         case 5 -> die5Icon;
         case 6 -> die6Icon;
         default -> null;
     };

     if (comp instanceof JLabel label) {
         label.setIcon(icon);
     } else if (comp instanceof AbstractButton button) {
         button.setIcon(icon);
     }
    }

    public void rollDice() {
        if (!currentPlayer.ifcanroll()) {
            JOptionPane.showMessageDialog(this, "You cannot roll more than 3 times. Please score.");
            return;
        }
        ArrayList<Dice> inDice = cup.getInDice();
        for (int i = 0; i < inDice.size(); i++) {
            if (!diceButtons[i].isSelected()) {
                inDice.get(i).roll();
            }
            int face = inDice.get(i).getCurrentValue().ordinal() + 1;
            setDieIcon(diceButtons[i], face);
            diceButtons[i].setText(null);

        }
        currentPlayer.incrementRollCount();
        if (!currentPlayer.ifcanroll()) {
            rollButton.setEnabled(false);
        }
    }

    public void scoreDice() {
        ArrayList<Dice> currentDice = cup.getInDice();
        Map<String, Integer> possibleScores = scoreCalculator.evaluate(currentDice);
        ArrayList<String> availableCategories = scoreboard.getRemainingCategories();
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
            // 🚨 No good categories => pick any available and score 0
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
            // 😎 Normal: pick a good scoring category
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

        scoreboard.setScore(chosenCategory, chosenScore);
        updateScoreboardDisplay();
        JOptionPane.showMessageDialog(this, "Scored " + chosenScore + " in " + chosenCategory + "!");

        rollButton.setEnabled(true);
        currentPlayer.resetRollCount();
        resetDiceButtons();

        if (scoreboard.getRemainingCategories().isEmpty()) {
            JOptionPane.showMessageDialog(this, "🎉 Game Over! Final Score: " + scoreboard.getTotalScore());
            rollButton.setEnabled(false);
            scoreButton.setEnabled(false);
            return;
        }

        // === Computer Turn ===
        computerPlayer.roll(computerScoreboard);
        ArrayList<Dice> cd = new ArrayList<>();
        if (computerPlayer instanceof ComputerEasy) {
            cd.addAll(((ComputerEasy)computerPlayer).getCurrentDice());
        } else if (computerPlayer instanceof ComputerHard) {
            cd.addAll(((ComputerHard)computerPlayer).getCurrentDice());
        }
        for (int i = 0; i < cd.size(); i++) {
        	int face = cd.get(i).getCurrentValue().ordinal() + 1;
        	setDieIcon(computerDiceLabels[i], face);
        	computerDiceLabels[i].setText(null);

        }
        updateComputerScoreboardDisplay();
    }


    private void updateScoreboardDisplay() {
        for (String cat : categoryLabels.keySet()) {
            if (cat.equals("Total")) {
                categoryLabels.get(cat).setText("Total Score: " + scoreboard.getTotalScore());
            } else {
                Integer val = scoreboard.getScores().get(cat);
                categoryLabels.get(cat).setText(cat + ": " + (val == null ? "-" : val));
            }
        }
    }

    private void updateComputerScoreboardDisplay() {
        for (String cat : computerCategoryLabels.keySet()) {
            if (cat.equals("Total")) {
                computerCategoryLabels.get(cat).setText("Total Score: " + computerScoreboard.getTotalScore());
            } else {
                Integer val = computerScoreboard.getScores().get(cat);
                computerCategoryLabels.get(cat).setText(cat + ": " + (val == null ? "-" : val));
            }
        }
    }

    private void resetDiceButtons() {
        for (JToggleButton btn : diceButtons) {
            btn.setSelected(false);
            btn.setIcon(null);
            btn.setText("?");
        }
        cup = new Cup();
    }

    public void startNewGame() {
        currentPlayer.resetRollCount();
        cup = new Cup();
        scoreboard = new Scoreboard();
        computerScoreboard = new Scoreboard();
        updateScoreboardDisplay();
        updateComputerScoreboardDisplay();
        resetDiceButtons();
        rollButton.setEnabled(true);
        scoreButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View gui = new View();
            gui.setVisible(true);
        });
    }
}
