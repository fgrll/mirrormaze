package mirrormaze.view;

import java.awt.GridBagLayout;

import javax.swing.*;


public class ModeSelectionPanel extends JPanel {
    public ModeSelectionPanel(Runnable onStandardChosen, Runnable onSurvivalChosen) {
        setLayout(new GridBagLayout());
        JButton standard = new JButton("Standard");
        JButton survival = new JButton("Survival");
        standard.addActionListener(e -> onStandardChosen.run());
        survival.addActionListener(e -> onSurvivalChosen.run());
        add(standard);
        add(survival);
    }
    
}
