package proj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("ROLL".equals(command)) {
            view.rollDice();
        } else if ("SCORE".equals(command)) {
            view.scoreDice();
        } else if ("NEW_GAME".equals(command)) {
            view.startNewGame();
        }
    }
}