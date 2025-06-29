package menu_demo;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel{
    public MenuPanel(Runnable onPlay) {
        setLayout(new GridBagLayout());
        JButton playBtn = new JButton("Play");
        playBtn.addActionListener(e -> onPlay.run());
        add(playBtn);
    }
}
