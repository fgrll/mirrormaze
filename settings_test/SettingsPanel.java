package settings_test;

import javax.swing.*;
import controls_demo.SoundPlayer;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel(SettingsManager settings, SoundPlayer sounds, Runnable onBack) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        add(new JLabel("Theme Volume"), gbc);
        gbc.gridx = 1;
        JSlider themeSlider = new JSlider(0, 100, Math.round(settings.getThemeVolume() * 100));
        themeSlider.addChangeListener(e -> {
            float v = themeSlider.getValue() / 100f;
            settings.setThemeVolume(v);
            sounds.setThemeVolume(v);
        });
        add(themeSlider, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("SFX Volume"), gbc);
        gbc.gridx = 1;
        JSlider sfxSlider = new JSlider(0, 100, Math.round(settings.getSFXVolume() * 100));
        sfxSlider.addChangeListener(e -> {
            float v = sfxSlider.getValue() / 100f;
            settings.setSFXVolume(v);
            sounds.setSFXVolume(v);
        });
        add(sfxSlider, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            settings.save();
            onBack.run();
        });
        add(back, gbc);
        
    }
}
