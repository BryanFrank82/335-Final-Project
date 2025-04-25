package proj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private View view;

    public Controller(View view) {
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
