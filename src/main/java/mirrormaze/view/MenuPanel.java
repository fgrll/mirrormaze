package menu_demo;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel{
    public MenuPanel(Runnable onPlay, Runnable onSettings) {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        JButton playBtn = new JButton("Play");
        playBtn.addActionListener(e -> onPlay.run());
        add(playBtn, gbc);

        gbc.gridy++;
        JButton settingsBtn = new JButton("Settings");
        settingsBtn.addActionListener(e -> onSettings.run());
        add(settingsBtn, gbc);
    }
}
