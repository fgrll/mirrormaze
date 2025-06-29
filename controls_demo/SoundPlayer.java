package controls_demo;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundPlayer {
    private Clip hitClip;
    private Clip successClip;
    private Clip themeClip;

    public SoundPlayer() {
        hitClip = loadClip("../resources/mirrormaze_hit.wav");
        successClip = loadClip("../resources/mirrormaze_success.wav");
        themeClip = loadClip("../resources/mirrormaze_theme.wav");
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

    public void playThemeLoop() {
        if (themeClip == null) return;
        if (themeClip.isRunning()) return;
        themeClip.setFramePosition(0);
        themeClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setThemeVolume(float volume) {
        if (themeClip == null) return;
        FloatControl gain = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float)(20.0 * Math.log10(Math.max(volume, 0.0001)));
        gain.setValue(dB);
    }

    public void stopTheme() {
        if (themeClip != null && themeClip.isRunning()) {
            themeClip.stop();
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
