package mirrormaze.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;

public class SetupPanel extends JPanel {
    private final Runnable onBack;
    public SetupPanel(BiConsumer<Integer, Integer> onStart, Runnable onBack) {
        this.onBack = onBack;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Width:"), gbc);
        gbc.gridx = 1;
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 100, 1));
        add(widthSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Height:"));
        gbc.gridx = 1;
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(8, 5, 100, 1));
        add(heightSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton startBtn = new JButton("Start");
        startBtn.addActionListener(e -> {
            int w = (int) widthSpinner.getValue();
            int h = (int) heightSpinner.getValue();
            // onStart.accept(w*2+1, h*2+1);
            onStart.accept(w, h);
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
