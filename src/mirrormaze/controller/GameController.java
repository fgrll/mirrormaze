package mirrormaze.controller;

import java.awt.*;
import java.awt.Dialog.ModalityType;

import javax.swing.*;

import mirrormaze.generator.DFSMazeGenerator;
import mirrormaze.generator.MazeGenerator;
import mirrormaze.generator.MirrorMazeGenerator;
import mirrormaze.mode.GameMode;
import mirrormaze.mode.ModeConfig;
import mirrormaze.model.Direction;
import mirrormaze.model.GameModel;
import mirrormaze.util.SettingsManager;
import mirrormaze.util.SoundPlayer;
import mirrormaze.view.GridPanel;
import mirrormaze.view.SettingsPanel;

public class GameController {
    private GameModel model;
    private final CardLayout cardLayout;
    private final JPanel cards;
    private SoundPlayer sounds;
    private GridPanel currentGridPanel;

    private MazeGenerator generator;
    private int halfWidth, height;

    private GameMode mode;

    private final SettingsManager settings;

    public GameController(CardLayout cardLayout, JPanel cards, SoundPlayer sounds, SettingsManager settings) {
        this.sounds = sounds;
        this.cardLayout = cardLayout;
        this.cards = cards;
        this.settings = settings;

        settings.addPropertyChangeListener(evt -> {
            if (currentGridPanel != null) {
                float f = (float) evt.getNewValue();
                currentGridPanel.setUiScale(f);
            }
        });
    }

    private void showSettingsDialogInGame() {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(cards);
        JDialog dlg = new JDialog(
                owner,
                "SETTINGS", false);

        SettingsPanel panel = new SettingsPanel(settings, sounds, dlg::dispose);

        dlg.getContentPane().add(panel);
        dlg.pack();
        dlg.setLocationRelativeTo(dlg.getOwner());
        dlg.setVisible(true);
    }

    public void selectStandardMode() {
        if (currentGridPanel != null) {
            cards.remove(currentGridPanel);
            currentGridPanel.cleanup();
            currentGridPanel = null;
        }
        cardLayout.show(cards, "SETUP_STANDARD");
    }

    public void selectSurvivalMode() {
        if (currentGridPanel != null) {
            cards.remove(currentGridPanel);
            currentGridPanel.cleanup();
            currentGridPanel = null;
        }
        cardLayout.show(cards, "SETUP_SURVIVAL");
    }

    public void startGame(int dim, ModeConfig config) {
        this.height = dim;
        this.halfWidth = dim / 2;

        MazeGenerator baseGenerator = new DFSMazeGenerator();
        this.generator = new MirrorMazeGenerator(baseGenerator);

        this.mode = config.createMode(this);

        generateGame();
    }

    public void generateGame() {
        boolean[][] walls = generator.generate(halfWidth, height);

        this.model = new GameModel(walls);

        if (currentGridPanel != null) {
            cards.remove(currentGridPanel);
            currentGridPanel.cleanup();
        }

        currentGridPanel = new GridPanel(
                model,
                sounds,
                mode::onExit,
                this::generateGame,
                this::handleMove,
                mode,
                settings.getUIScale(),
                this::showSettingsDialogInGame,
                this::showHelpDialogInGame);
        cards.add(currentGridPanel, "GAME");
        cardLayout.show(cards, "GAME");
        // SwingUtilities.getWindowAncestor(cards).pack();
        SwingUtilities.getWindowAncestor(cards);
        currentGridPanel.requestFocusInWindow();
    }

    private void showHelpDialogInGame() {
        Window owner = SwingUtilities.getWindowAncestor(cards);
        JDialog dlg = new JDialog(owner, "Controls", ModalityType.MODELESS);

        java.util.List<String> controls = java.util.List.of(
                "You can move with arrow keys. Scroll to scale and drag the maze to control view offset.",
                "",
                "S - Open settings",
                "G - Generate new maze",
                "Arrow Keys - Move",
                "R - Reset position",
                "Esc - Exit to menu",
                "V - Reset scaling and offset",
                "M - Show mirrored maze half");

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        content.setBackground(new Color(30, 30, 30));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setOpaque(false);
        area.setForeground(Color.WHITE);
        area.setFont(area.getFont().deriveFont(Font.PLAIN, 14f));
        controls.forEach(line -> {
            area.append(line);
            area.append("\n");
        });

        content.add(area, BorderLayout.CENTER);
        dlg.getContentPane().add(content);

        dlg.pack();
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true);
    }

    public void showMenu() {
        cardLayout.show(cards, "MENU");
    }

    public void showSettings() {
        cardLayout.show(cards, "SETTINGS");
    }

    public void showSelectionPanel() {
        cardLayout.show(cards, "MODE");
    }

    private void handleMove(Direction dir, boolean moved) {
        if (!moved) {
            mode.onHit();
        } else {
            sounds.playMove();
            if (model.isFinished()) {
                sounds.playSuccess();
                mode.onFinish();
            }
        }
    }
}
