package mirrormaze.util;

import java.nio.file.*;
import java.io.*;
import java.util.Properties;

public class SettingsManager {
    private static final Path PREF_FILE = Paths.get(System.getProperty("user.home"), ".mirror-maze", "settings.json");

    private final Properties props = new Properties();

    private static final float DEFAULT_THEME_VOLUME = 0.4f;
    private static final float DEFAULT_SFX_VOLUME = 1.0f;
    private static final float DEFAULT_UI_SCALING = 1.5f;

    public SettingsManager() {
        load();
    }

    private void load() {
        try {
            Files.createDirectories(PREF_FILE.getParent());
            if (Files.exists(PREF_FILE)) {
                try (InputStream in = Files.newInputStream(PREF_FILE)) {
                    props.load(in);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (OutputStream out = Files.newOutputStream(PREF_FILE)) {
            props.store(out, "Mirror Maze Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float getThemeVolume() {
        return Float.parseFloat(
            props.getProperty("themeVolume", String.valueOf(DEFAULT_THEME_VOLUME))
        );
    }

    public float getSFXVolume() {
        return Float.parseFloat(
            props.getProperty("sfxVolume", String.valueOf(DEFAULT_SFX_VOLUME))
        );
    }

    public float getUIScale() {
        return Float.parseFloat(
            props.getProperty("UI_SCALING", String.valueOf(DEFAULT_UI_SCALING))
        );
    }

    public void setThemeVolume(float v) {
        props.setProperty("themeVolume", String.valueOf(v));
    }

    public void setSFXVolume(float v) {
        props.setProperty("sfxVolume", String.valueOf(v));
    }

    public void setUIScaling(float f) {
        props.setProperty("UI_SCALING", String.valueOf(f));
    }
}
