package controls_demo;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    private Clip hitClip;

    public SoundPlayer() {
        try {
            URL url = getClass().getResource("../resources/mirrormaze_hit.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            hitClip = AudioSystem.getClip();
            hitClip.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playHit() {
        if (hitClip == null)
            return;
        if (hitClip.isRunning())
            hitClip.stop();
        hitClip.setFramePosition(0);
        hitClip.start();
    }
}
