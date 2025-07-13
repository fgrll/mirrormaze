package mirrormaze.view;

import javax.swing.*;

import mirrormaze.mode.StandardModeConfig;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;

public class StandardSetupPanel extends JPanel {
    private final Runnable onBack;
    public StandardSetupPanel(BiConsumer<Integer, StandardModeConfig> onStart, Runnable onBack) {
        this.onBack = onBack;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(new JLabel("Dimension:"), gbc);
        gbc.gridx = 1;
        JSpinner dimensionSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 100, 1));
        add(dimensionSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JCheckBox endlessCB = new JCheckBox("Endless Mode");
        add(endlessCB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JCheckBox cheatsCB = new JCheckBox("Allow Cheats?");
        add(cheatsCB, gbc);


        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton startBtn = new JButton("Start");
        startBtn.addActionListener(e -> {
            int dim = (int) dimensionSpinner.getValue();
            boolean endless = endlessCB.isSelected();
            boolean allowCheats = cheatsCB.isSelected();
            onStart.accept(dim, new StandardModeConfig(endless, allowCheats));
        });
        add(startBtn, gbc);

        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");
        am.put("back", new ExitAction());
    }

    private class ExitAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            onBack.run();
        }
    }
}
