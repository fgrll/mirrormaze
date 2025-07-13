package mirrormaze.mode;

import mirrormaze.controller.GameController;

public class SurvivalModeConfig implements ModeConfig {
    private final int startDimension;
    private final int startLives;
    private final int step;
    private final boolean allowCheats;

    public SurvivalModeConfig(int startDimension, int startLives, int step, boolean allowCheats) {
        this.startDimension = startDimension;
        this.startLives = startLives;
        this.step = step;
        this.allowCheats = allowCheats;
    }

    public int getStartDimension() { return startDimension; }
    public int getStartLives() { return startLives; }
    public int getStep() { return step; }

    @Override
    public GameMode createMode(GameController controller) {
        return new SurvivalMode(
            controller,
            startDimension,
            startLives,
            step
        );
    }

    @Override
    public boolean cheatsAllowed() {
        return allowCheats;
    }
}
