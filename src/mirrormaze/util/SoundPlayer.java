package controls_demo;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundPlayer {
    private Clip hitClip;
    private Clip successClip;
    private Clip themeClip;
    private Clip moveClip;
    private Clip deathClip;

    private String wavPath = "../resources/";

    public SoundPlayer() {
        hitClip = loadClip(wavPath + "mirrormaze_hit.wav");
        successClip = loadClip(wavPath + "mirrormaze_success.wav");
        themeClip = loadClip(wavPath + "mirrormaze_theme.wav");
        moveClip = loadClip(wavPath + "mirrormaze_move.wav");
        deathClip = loadClip(wavPath + "mirrormaze_death.wav");
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

    public void playMove() {
        if (moveClip == null) return;
        if (moveClip.isRunning()) moveClip.stop();
        moveClip.setFramePosition(0);
        moveClip.start();
    }

    public void playDeath() {
        if (deathClip == null) return;
        if (deathClip.isRunning()) deathClip.stop();
        deathClip.setFramePosition(0);
        deathClip.start();
    }

    public void setSFXVolume(float volume) {
        if (moveClip == null) return;
        float dB = (float)(20.0 * Math.log10(Math.max(volume, 0.0001)));

        FloatControl moveGain = (FloatControl) moveClip.getControl(FloatControl.Type.MASTER_GAIN);
        moveGain.setValue(dB);

        FloatControl hitGain = (FloatControl) hitClip.getControl(FloatControl.Type.MASTER_GAIN);
        hitGain.setValue(dB);

        FloatControl successGain = (FloatControl) successClip.getControl(FloatControl.Type.MASTER_GAIN);
        successGain.setValue(dB);

        FloatControl deathGain = (FloatControl) deathClip.getControl(FloatControl.Type.MASTER_GAIN);
        deathGain.setValue(dB);
    }

    public void playSuccess() {
        if (successClip == null) return;
        if (successClip.isRunning()) successClip.stop();
        successClip.setFramePosition(0);
        successClip.start();
    }
}
