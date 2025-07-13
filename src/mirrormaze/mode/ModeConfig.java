package mirrormaze.mode;

import mirrormaze.controller.GameController;

public interface ModeConfig {
    GameMode createMode(GameController controller);

    default boolean cheatsAllowed() {
        return false;
    }
}
