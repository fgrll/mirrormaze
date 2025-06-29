package controls_demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GridPanel extends JPanel {
    private final GameModel model;
    private final int cellSize = 20;
    private final SoundPlayer sounds = new SoundPlayer();

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

        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");

        am.put("up", new MoveAction(Direction.NORTH));
        am.put("down", new MoveAction(Direction.SOUTH));
        am.put("left", new MoveAction(Direction.WEST));
        am.put("right", new MoveAction(Direction.EAST));
    }

    private class MoveAction extends AbstractAction {
        private final Direction dir;

        public MoveAction(Direction dir) {
            this.dir = dir;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean moved = model.tryMove(dir);
            if (!moved) {
                sounds.playHit();
            }
            repaint();
        }
    }
}
