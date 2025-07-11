package mirrormaze.mode;

import javax.swing.*;
import mirrormaze.controller.GameController;

public class StandardMode implements GameMode {
    private final GameController controller;

    public StandardMode(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void onHit() {}

    @Override
    public void onFinish() {
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showOptionDialog(
                null,
                "Success!",
                "Level Completed",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Again", "Exit"},
                "Again");

                if (choice == JOptionPane.YES_OPTION) {
                    controller.generateGame();
                } else {
                    onExit();
                }
        });
    }

    @Override
    public void onExit() {
        controller.showMenu();
    }
}
