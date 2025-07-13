package mirrormaze.mode;

import mirrormaze.controller.GameController;

public class StandardModeConfig implements ModeConfig {
    private final boolean endless;
    private final boolean allowCheats;

    public StandardModeConfig(boolean endless, boolean allowCheats) {
        this.endless = endless;
        this.allowCheats = allowCheats;
    }

    public boolean isEndless() {
        return endless;
    }

    @Override
    public GameMode createMode(GameController controller) {
        return new StandardMode(controller, endless);
    }

    public boolean isCheatsAllowed() {
        return allowCheats;
    }
}
