package mirrormaze.mode;

import java.util.List;

import javax.swing.*;
import mirrormaze.controller.GameController;

public class StandardMode implements GameMode {
    private final GameController controller;
    private boolean endless;

    private int solvedCount = 0;
    private int hitCount = 0;

    public StandardMode(GameController controller, boolean endless) {
        this.controller = controller;
        this.endless = endless;
    }

    @Override
    public void onHit() {
        hitCount++;
    }

    @Override
    public void onFinish() {
        solvedCount++;
        if (endless) {
            controller.generateGame();
        } else {
            SwingUtilities.invokeLater(() -> {
                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Success!",
                        "Level Completed",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, new String[] { "Again", "Exit" },
                        "Again");

                if (choice == JOptionPane.YES_OPTION) {
                    controller.generateGame();
                } else {
                    onExit();
                }
            });
        }
    }

    @Override
    public void onExit() {
        controller.showMenu();
    }

    @Override
    public List<String> getOverlayText() {
        return List.of(
            "Solved: " + solvedCount,
            "Hits: " + hitCount
        );
    }
}
