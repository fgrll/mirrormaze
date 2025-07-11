package mirrormaze.view;

import java.awt.GridBagLayout;

import javax.swing.*;


public class ModeSelectionPanel extends JPanel {
    public ModeSelectionPanel(Runnable onStandardChosen) {
        setLayout(new GridBagLayout());
        JButton standard = new JButton("Standard");
        standard.addActionListener(e -> onStandardChosen.run());
        add(standard);
    }
    
}
