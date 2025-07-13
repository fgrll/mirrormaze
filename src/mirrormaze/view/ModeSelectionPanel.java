package mirrormaze.view;

import java.awt.*;
import javax.swing.*;


public class ModeSelectionPanel extends JPanel {
    public ModeSelectionPanel(Runnable onStandardChosen, Runnable onSurvivalChosen) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        JButton standard = new JButton("Standard");
        add(standard, gbc);
        gbc.gridy += 2;
        JButton survival = new JButton("Survival");
        add(survival, gbc);
        
        standard.addActionListener(e -> onStandardChosen.run());
        survival.addActionListener(e -> onSurvivalChosen.run());
        
    }
    
}
