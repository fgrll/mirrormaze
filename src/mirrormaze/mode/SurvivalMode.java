package mirrormaze.mode;

import javax.swing.JOptionPane;

import mirrormaze.controller.GameController;
import java.util.List;

public class SurvivalMode implements GameMode {
    private final GameController controller; 
    private int dimension;
    private int lives;
    private final int step;
    private int roundCount = 1;

    public SurvivalMode(
        GameController controller,
        int startDimension,
        int startLives,
        int step
    ) {
        this.controller = controller;
        this.dimension = startDimension;
        this.lives = startLives;
        this.step = step;
    }

    @Override
    public void onHit() {
        lives--;

        if (lives == 0) {
            JOptionPane.showMessageDialog(null, "Game Over!\nScore: " + String.valueOf(roundCount - 1), "Ouch!", JOptionPane.INFORMATION_MESSAGE);
            controller.showMenu();
        }
    }

    @Override
    public void onExit() {
        controller.showMenu();
    }

    @Override
    public List<String> getOverlayText() {
        return List.of(
            "Round: " + roundCount,
            "Lives: " + lives
        );
    }

    @Override
    public void onFinish() {
        roundCount++;
        dimension += step;
        controller.nextRound(dimension);
    }
}
