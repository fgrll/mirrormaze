package mirrormaze.controller;

import java.awt.*;
import javax.swing.*;

import mirrormaze.generator.DFSMazeGenerator;
import mirrormaze.generator.MazeGenerator;
import mirrormaze.generator.MirrorMazeGenerator;
import mirrormaze.mode.GameMode;
import mirrormaze.mode.ModeConfig;
import mirrormaze.model.Direction;
import mirrormaze.model.GameModel;
import mirrormaze.util.SoundPlayer;
import mirrormaze.view.GridPanel;

public class GameController {
    private GameModel model;
    private final CardLayout cardLayout;
    private final JPanel cards;
    private SoundPlayer sounds;
    private GridPanel currentGridPanel;

    
    private MazeGenerator generator;
    private int halfWidth, height;

    private GameMode mode;

    public GameController(CardLayout cardLayout, JPanel cards, SoundPlayer sounds) {
        this.sounds = sounds;
        this.cardLayout = cardLayout;
        this.cards = cards;
    }

    public void selectStandardMode() {
        if (currentGridPanel != null) {
            cards.remove(currentGridPanel);
            currentGridPanel.cleanup();
            currentGridPanel = null;
        }
        cardLayout.show(cards, "SETUP_STANDARD");
    }

    public void startGame(int dim, ModeConfig config) {
        this.height = dim;
        this.halfWidth = dim  / 2;

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

        currentGridPanel = new GridPanel(model, sounds, mode::onExit, this::generateGame, this::handleMove);
        cards.add(currentGridPanel, "GAME");
        cardLayout.show(cards, "GAME");
        SwingUtilities.getWindowAncestor(cards).pack();
        currentGridPanel.requestFocusInWindow();
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
