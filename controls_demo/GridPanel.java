package controls_demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GridPanel extends JPanel {
    private final GameModel model;
    private final int cellSize = 20;

    public GridPanel(GameModel model) {
        this.model = model;
        setPreferredSize(new Dimension(model.getCols()*cellSize, model.getRows()*cellSize));
        setFocusable(true);
        requestFocusInWindow();
        setupKeyBindings();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < model.getCols(); x++) {
            for (int y = 0; y < model.getRows(); y++) {
                if (model.isWall(x, y)) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                }
            }
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
