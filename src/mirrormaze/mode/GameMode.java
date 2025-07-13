package mirrormaze.mode;

import java.util.List;

public interface GameMode {
    
    void onHit();
    
    void onFinish();

    void onExit();

    default List<String> getOverlayText() {
        return List.of();
    }
}
