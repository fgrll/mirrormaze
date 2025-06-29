package controls_demo;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundPlayer {
    private Clip hitClip;
    private Clip successClip;

    public SoundPlayer() {
        hitClip = loadClip("../resources/mirrormaze_hit.wav");
        successClip = loadClip("../resources/mirrormaze_success.wav");
    }

    private Clip loadClip(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public void playSuccess() {
        if (successClip == null) return;
        if (successClip.isRunning()) successClip.stop();
        successClip.setFramePosition(0);
        successClip.start();
    }
}
