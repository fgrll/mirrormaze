package controls_demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GridPanel extends JPanel {
    private final GameModel model;
    private final int cellSize = 40;

    public GridPanel(GameModel model) {
        this.model = model;
        int width = model.getCols() * cellSize;
        int height = model.getRows() * cellSize;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocusInWindow();
        setupKeyBindings();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);

        for (int x = 0; x <= model.getCols(); x++) {
            g.drawLine(x * cellSize, 0, x * cellSize, model.getRows() * cellSize);
        }
        for (int y = 0; y <= model.getRows(); y++) {
            g.drawLine(0, y * cellSize, model.getCols() * cellSize, y * cellSize);
        }

        // demo
        g.setColor(Color.RED);
        int px = model.getPlayerX() * cellSize;
        int py = model.getPlayerY() * cellSize;
        g.fillOval(px + 4, py + 4, cellSize - 8, cellSize - 8);
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");

        am.put("moveUp", new MoveAction(() -> model.moveNorth()));
        am.put("moveDown", new MoveAction(() -> model.moveSouth()));
        am.put("moveLeft", new MoveAction(() -> model.moveWest()));
        am.put("moveRight", new MoveAction(() -> model.moveEast()));
    }

    private class MoveAction extends AbstractAction {
        private final Runnable move;

        public MoveAction(Runnable move) {
            this.move = move;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            move.run();
            repaint();
        }
    }
}
