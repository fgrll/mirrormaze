package mirrormaze.view;

import javax.swing.*;

import mirrormaze.model.Direction;
import mirrormaze.model.GameModel;
import mirrormaze.util.SoundPlayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.util.function.BiConsumer;

public class GridPanel extends JPanel {
    private final GameModel model;
    private final int cellSize = 20;
    
    private final SoundPlayer sounds;

    private Direction lastDir = Direction.EAST;

    private int flashX = -1, flashY = -1;
    private final int flashDuration = 200;
    private Timer flashTimer;

    private final Runnable onEscape;

    private final Runnable onGenerate;

    private BiConsumer<Direction, Boolean> onMove;

    public GridPanel(GameModel model, SoundPlayer sounds, Runnable onEscape, Runnable onGenerate, BiConsumer<Direction, Boolean> onMove) {
        this.model = model;
        this.sounds = sounds;
        this.onEscape = onEscape;
        this.onGenerate = onGenerate;
        this.onMove = onMove;
        setPreferredSize(new Dimension(model.getCols()*cellSize, model.getRows()*cellSize));
        setFocusable(true);
        requestFocusInWindow();
        setupKeyBindings();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();

        for (int x = 0; x < model.getCols(); x++) {
            for (int y = 0; y < model.getRows(); y++) {
                int px = x*cellSize, py = y*cellSize;
                if (model.isWall(x, y)) {
                    if (x == flashX && y == flashY) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                    }
                    g.fillRect(px, py, cellSize, cellSize);
                } else if ((x == 0 && y == 1) || (x == model.getCols() /2 && y == model.getRows() - 2)) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(px, py, cellSize, cellSize);
                } else if ((x == model.getCols() - 1) && (y == 1)) {
                    g.setColor(Color.GREEN);
                    g.fillRect(px, py, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(px, py, cellSize, cellSize);
                }
            }
        }

        int cx = model.getPlayerX()*cellSize;
        int cy = model.getPlayerY()*cellSize;
        Shape arrow = createArrowShape(cellSize);
        AffineTransform at = new AffineTransform();
        at.translate(cx + cellSize/2, cy + cellSize/2);
        double angle = 0;
        switch (lastDir) {
            case NORTH: angle = 0; break;
            case EAST: angle = Math.PI/2; break;
            case SOUTH: angle = Math.PI; break;
            case WEST: angle = -Math.PI/2; break;
        }
        at.rotate(angle);
        g.setColor(Color.RED);
        g.fill(at.createTransformedShape(arrow));
        g.dispose();
    }

    private Shape createArrowShape(int size) {
        int h = size/2;
        Polygon p = new Polygon();
        p.addPoint(0, -h/2);
        p.addPoint(-h/2, h/2);
        p.addPoint(h/2, h/2);
        return p;
    }

    private void flashWall(int x, int y) {
        flashX = x;
        flashY = y;
        if (flashTimer != null && flashTimer.isRunning()) {
            flashTimer.restart();
        } else {
            flashTimer = new Timer(flashDuration, (ActionEvent e) -> {
                flashX = flashY = -1;
                repaint();
                flashTimer.stop();
            });
            flashTimer.setRepeats(false);
            flashTimer.start();
        }
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        im.put(KeyStroke.getKeyStroke("R"), "reset");
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
        im.put(KeyStroke.getKeyStroke("G"), "generate");

        am.put("up", new MoveAction(Direction.NORTH));
        am.put("down", new MoveAction(Direction.SOUTH));
        am.put("left", new MoveAction(Direction.WEST));
        am.put("right", new MoveAction(Direction.EAST));
        am.put("reset", new ResetAction());
        am.put("exit", new ExitAction());
        am.put("generate", new GenerateAction());
    }

    private class MoveAction extends AbstractAction {
        private final Direction dir;

        public MoveAction(Direction dir) {
            this.dir = dir;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            lastDir = dir;

            int nx = model.getPlayerX() + dir.dx;
            int ny = model.getPlayerY() + dir.dy;
            boolean moved = model.tryMove(dir);

            if (!moved) {
                sounds.playHit();
                if (nx >= 0 && nx < model.getCols() && ny >= 0 && ny < model.getRows()) {
                    flashWall(nx, ny);
                }
            } 

            onMove.accept(dir, moved);
            repaint();
        }
    }

    private class ResetAction extends AbstractAction {
        public ResetAction() {
            model.resetPosition();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            lastDir = Direction.EAST;
            sounds.playDeath();
            repaint();
            model.resetPosition();
        }
    }

    private class ExitAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            onEscape.run();
        }
    }

    private class GenerateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            onGenerate.run();
        }
    }

    public void cleanup() {
        if (flashTimer != null && flashTimer.isRunning()) {
            flashTimer.stop();
        }
    }
}
