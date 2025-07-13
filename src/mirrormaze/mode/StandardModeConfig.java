package mirrormaze.mode;

import mirrormaze.controller.GameController;

public class StandardModeConfig implements ModeConfig {
    private final boolean endless;

    public StandardModeConfig(boolean endless) {
        this.endless = endless;
    }

    public boolean isEndless() {
        return endless;
    }

    @Override
    public GameMode createMode(GameController controller) {
        return new StandardMode(controller, endless);
    }
}
