package mirrormaze.view;

import javax.swing.*;

import mirrormaze.mode.GameMode;
import mirrormaze.model.Direction;
import mirrormaze.model.GameModel;
import mirrormaze.pathfinder.PathFinder;
import mirrormaze.util.SoundPlayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.function.BiConsumer;
import java.util.List;

/**
 * GP renders the maze and controls all in-game interactions
 * 
 * @see javax.swing.JPanel
 */
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
    private boolean showMirrored = false;
    private double scale = 1.0;
    private double offsetX = 0, offsetY = 0;
    private Point dragStart;
    private final GameMode mode;
    private float uiScale;
    private Runnable onOpenSettings;
    private Runnable onShowHelp;
    private boolean showPath = false;
    private List<Point> pathPoints = null;

    /**
     * Constructor of GridPanel class
     * 
     * @param model GameModel holds maze data and player status
     * @param sounds Central SoundPlayer instance
     * @param onEscape Runnable which is triggered by ESC
     * @param onGenerate Runnable to generate a new maze (G key)
     * @param onMove Callback that informs the controller about movements
     * @param mode Current game mode instance
     * @param uiScale GUI Scaling factor (in-game)
     * @param onOpenSettings Runnable triggered by S key to opens settings
     * @param onShowHelp Runnable triggered by H key to show help dialog
     * @param cheatsEnabled Bool that decides whether M key and P key are mapped
     */
    public GridPanel(
            GameModel model,
            SoundPlayer sounds,
            Runnable onEscape,
            Runnable onGenerate,
            BiConsumer<Direction, Boolean> onMove,
            GameMode mode,
            float uiScale,
            Runnable onOpenSettings,
            Runnable onShowHelp,
            boolean cheatsEnabled) {

        this.model = model;
        this.sounds = sounds;
        this.onEscape = onEscape;
        this.onGenerate = onGenerate;
        this.onMove = onMove;
        this.mode = mode;
        this.uiScale = uiScale;
        this.onOpenSettings = onOpenSettings;
        this.onShowHelp = onShowHelp;

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                fitToWindow();
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        setupKeyBindings();

        if (cheatsEnabled) {
            setupCheats();
        }

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = e.getPoint();
                offsetX += (dragStart.x - p.x) / scale;
                offsetY += (dragStart.y - p.y) / scale;
                dragStart = p;
                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double mx = e.getX() / scale + offsetX;
                double my = e.getY() / scale + offsetY;

                double factor = Math.pow(1.1, -e.getPreciseWheelRotation());
                scale *= factor;

                offsetX = mx - e.getX() / scale;
                offsetY = my - e.getY() / scale;

                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);

        SwingUtilities.invokeLater(this::fitToWindow);
    }

    /**
     * Calculates visual scaling and offset for GUI zoom/panning
     */
    private void fitToWindow() {
        int cols = model.getCols();
        int rows = model.getRows();
        double mazeW = cols * cellSize;
        double mazeH = rows * cellSize;
        double viewW = getWidth();
        double viewH = getHeight();

        if (viewW <= 0 || viewH <= 0)
            return;

        scale = Math.min(viewW / mazeW, viewH / mazeH);

        offsetX = mazeW / 2.0 - (viewW / 2.0) / scale;
        offsetY = mazeH / 2.0 - (viewH / 2.0) / scale;

        repaint();
    }

    /**
     * Setter for UI scaling value
     * 
     * @param uiScale
     */
    public void setUiScale(float uiScale) {
        this.uiScale = uiScale;
        fitToWindow();
    }

    /**
     * Draws maze, player and overlay
     */
    @Override
    protected void paintComponent(Graphics g0) {

        //=========================
        // Maze
        //=========================

        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();

        g.scale(scale, scale);
        g.translate(-offsetX, -offsetY);

        int cols = model.getCols();
        int rows = model.getRows();
        int halfCols = cols / 2;
        int spacing = 5;

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                int px = x * cellSize, py = y * cellSize;

                if (!showMirrored && (x > halfCols) && (x < cols - 1) && (y > 0) && (y < rows - 1)) {
                    Shape oldClip = g.getClip();
                    g.setClip(px, py, cellSize, cellSize);

                    g.setColor(Color.WHITE);
                    if (x == flashX && y == flashY) {
                        g.setColor(Color.RED);
                    }
                    g.fillRect(px, py, cellSize, cellSize);

                    if (!(x == flashX && y == flashY)) {
                        g.setColor(Color.BLACK);
                        for (int d = -cellSize; d < cellSize; d += spacing) {
                            g.drawLine(px + d, py, px + d + cellSize, py + cellSize);
                        }
                    }

                    g.setClip(oldClip);
                    g.setColor(Color.BLACK);
                    g.drawRect(px, py, cellSize, cellSize);
                    continue;
                }

                if (model.isWall(x, y)) {
                    if (x == flashX && y == flashY) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                    }
                    g.fillRect(px, py, cellSize, cellSize);
                } else if ((x == 0 && y == 1) || (x == halfCols && y == rows - 2)) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(px, py, cellSize, cellSize);
                } else if ((x == cols - 1) && (y == 1)) {
                    g.setColor(Color.GREEN);
                    g.fillRect(px, py, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(px, py, cellSize, cellSize);
                }

                g.setColor(Color.BLACK);
                g.drawRect(px, py, cellSize, cellSize);
            }
        }

        //=========================
        // Pathfinding cheat
        //=========================

        if (showPath && pathPoints != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setStroke(new BasicStroke((float) (2.0 / scale)));
            g2.setColor(new Color(0, 0, 255, 255)); 

            for (int i = 0; i < pathPoints.size() - 1; i++) {
                Point p1 = pathPoints.get(i);
                Point p2 = pathPoints.get(i + 1);
                int x1 = p1.x * cellSize + cellSize / 2;
                int y1 = p1.y * cellSize + cellSize / 2;
                int x2 = p2.x * cellSize + cellSize / 2;
                int y2 = p2.y * cellSize + cellSize / 2;
                g2.drawLine(x1, y1, x2, y2);
            }
            g2.dispose();
        }

        //=========================
        // Player
        //=========================

        int cx = model.getPlayerX() * cellSize;
        int cy = model.getPlayerY() * cellSize;
        Shape arrow = createArrowShape(cellSize);
        AffineTransform at = new AffineTransform();
        at.translate(cx + cellSize / 2, cy + cellSize / 2);
        double angle = 0;
        switch (lastDir) {
            case NORTH:
                angle = 0;
                break;
            case EAST:
                angle = Math.PI / 2;
                break;
            case SOUTH:
                angle = Math.PI;
                break;
            case WEST:
                angle = -Math.PI / 2;
                break;
        }
        at.rotate(angle);
        g.setColor(Color.RED);
        g.fill(at.createTransformedShape(arrow));
        g.dispose();

        //=========================
        // Status GUI (top right)
        //=========================

        Graphics2D o = (Graphics2D) g0.create();
        List<String> lines = mode.getOverlayText();

        if (!lines.isEmpty()) {
            Font base = o.getFont();
            float size = base.getSize2D() * uiScale;
            Font overlayFont = base.deriveFont(Font.BOLD, size);
            o.setFont(overlayFont);

            FontMetrics fm = o.getFontMetrics();
            int widthText = lines.stream()
                    .mapToInt(fm::stringWidth)
                    .max().orElse(0);
            int lineHeight = fm.getHeight();

            int padding = (int) (6 * uiScale);
            int margin = (int) (10 * uiScale);
            int arc = (int) (8 * uiScale);

            int boxW = widthText + 2 * padding;
            int boxH = lines.size() * lineHeight + 2 * padding;
            int x0 = getWidth() - boxW - margin;
            int y0 = margin;

            o.setColor(new Color(0, 0, 0, 150));
            o.fillRoundRect(x0, y0, boxW, boxH, arc, arc);

            o.setColor(Color.WHITE);
            int tx = x0 + padding;
            int ty = y0 + padding + fm.getAscent();
            for (String line : lines) {
                o.drawString(line, tx, ty);
                ty += lineHeight;
            }
        }
        o.dispose();

        //=========================
        // Hint (bottom left)
        //=========================

        Graphics2D o1 = (Graphics2D) g0.create();
        String hint = "Press H to show help";
        Font base1 = o1.getFont();
        float size1 = base1.getSize2D() * uiScale;
        Font overlayFont1 = base1.deriveFont(Font.BOLD, size1);
        o1.setFont(overlayFont1);

        int padding = (int) (6 * uiScale);
        int margin = (int) (10 * uiScale);
        int arc = (int) (8 * uiScale);

        FontMetrics fm1 = o1.getFontMetrics();

        int widthText1 = fm1.stringWidth(hint);
        int lineHeight1 = fm1.getHeight();

        int x1 = margin;
        int y1 = getHeight() - margin - lineHeight1;

        o1.setColor(new Color(0, 0, 0, 120));
        o1.fillRoundRect(x1 - padding, y1 - padding, widthText1 + 2 * padding, lineHeight1 + 2 * padding, arc, arc);

        o1.setColor(Color.WHITE);
        o1.drawString(hint, x1, y1 + fm1.getAscent());
        o1.dispose();
    }

    /**
     * Helper function for creating the arrowhead (player model)
     * 
     * @param size Edge length for bounding box
     * @return Shape arrowhead
     */
    private Shape createArrowShape(int size) {
        int h = size / 2;
        Polygon p = new Polygon();
        p.addPoint(0, -h / 2);
        p.addPoint(-h / 2, h / 2);
        p.addPoint(h / 2, h / 2);
        return p;
    }

    /**
     * Makes a wall at coordinates (x,y) briefly light up upon collision
     * 
     * @param x x coordinate of wall
     * @param y y coordinate of wall
     */
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

    /**
     * Defines control and registers all key bindings
     */
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
        im.put(KeyStroke.getKeyStroke("V"), "viewReset");
        im.put(KeyStroke.getKeyStroke("S"), "openSettings");
        im.put(KeyStroke.getKeyStroke("H"), "openHelp");

        am.put("up", new MoveAction(Direction.NORTH));
        am.put("down", new MoveAction(Direction.SOUTH));
        am.put("left", new MoveAction(Direction.WEST));
        am.put("right", new MoveAction(Direction.EAST));
        am.put("reset", new ResetAction());
        am.put("exit", new ExitAction());
        am.put("generate", new GenerateAction());
        am.put("viewReset", new ViewRestAction());
        am.put("openSettings", new OpenSettingsAction());
        am.put("openHelp", new OpenHelpAction());
    }

    private void setupCheats() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke("M"), "toggleShow");
        im.put(KeyStroke.getKeyStroke("P"), "togglePath");

        am.put("toggleShow", new ShowMirroredAction());
        am.put("togglePath", new ShowPathAction());


    }

    //=========================
    // Actions
    //=========================

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

    private class ShowMirroredAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            showMirrored = !showMirrored;
            repaint();
        }
    }

    private class ViewRestAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            fitToWindow();
        }
    }

    private class OpenSettingsAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            onOpenSettings.run();
        }
    }

    private class OpenHelpAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            onShowHelp.run();
        }
    }

    private class ShowPathAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            showPath = !showPath;

            if (showPath) {
                PathFinder.Result res = PathFinder.bfs(model.getWalls(), model.getPlayerX(), model.getPlayerY(),
                        model.getExitX(), model.getExitY());
                pathPoints = (res != null) ? res.path : null;
            } else {
                pathPoints = null;
            }
            repaint();
        }
    }
}
