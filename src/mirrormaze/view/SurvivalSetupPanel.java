package mirrormaze.view;

import java.awt.*;
import java.util.function.BiConsumer;

import javax.swing.*;

import mirrormaze.mode.*;

public class SurvivalSetupPanel extends JPanel {
    
    public SurvivalSetupPanel(BiConsumer<ModeConfig, Integer> onStart, Runnable onBack) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(new JLabel("Start Dimension:"), gbc);
        gbc.gridx = 1;
        JSpinner dimSpinner = new JSpinner(new SpinnerNumberModel(8, 4, 100, 2));
        add(dimSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Lives:"), gbc);
        gbc.gridx = 1;
        JSpinner livesSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        add(livesSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Steps:"), gbc);
        gbc.gridx = 1;
        JSpinner stepSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 2));
        add(stepSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        JButton startBtn = new JButton("Start");

        startBtn.addActionListener(e -> {
            int dim = (int) dimSpinner.getValue();
            int life = (int) livesSpinner.getValue();
            int step = (int) stepSpinner.getValue();
            ModeConfig cfg = new SurvivalModeConfig(dim, life, step);
            onStart.accept(cfg, dim);
        });
        add(startBtn, gbc);

        gbc.gridx = 0;

        JButton backBtn = new JButton("Back");

        backBtn.addActionListener(e -> onBack.run());
        add(backBtn, gbc);
    }
}
