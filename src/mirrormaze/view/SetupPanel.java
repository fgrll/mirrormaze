package mirrormaze.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.IntConsumer;

public class SetupPanel extends JPanel {
    private final Runnable onBack;
    public SetupPanel(IntConsumer onStart, Runnable onBack) {
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
        gbc.gridwidth = 2;
        JButton startBtn = new JButton("Start");
        startBtn.addActionListener(e -> {
            int dim = (int) dimensionSpinner.getValue();
            // onStart.accept(w*2+1, h*2+1);
            onStart.accept(dim);
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
