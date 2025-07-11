package mirrormaze.controller;

import java.awt.*;
import javax.swing.*;

import mirrormaze.generator.DFSMazeGenerator;
import mirrormaze.generator.MazeGenerator;
import mirrormaze.mode.GameMode;
import mirrormaze.mode.StandardMode;
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

    private int currentCols, currentRows;
    private MazeGenerator generator;

    private GameMode mode;

    public GameController(CardLayout cardLayout, JPanel cards, SoundPlayer sounds) {
        this.sounds = sounds;
        this.cardLayout = cardLayout;
        this.cards = cards;
    }

    public void showSetup() {
        if (currentGridPanel != null) {
            cards.remove(currentGridPanel);
            currentGridPanel.cleanup();
            currentGridPanel = null;
        }
        cardLayout.show(cards, "SETUP");
    }

    public void startGame(int cols, int rows) {
        this.mode = new StandardMode(this);
        this.currentCols = cols;
        this.currentRows = rows;
        this.generator = new DFSMazeGenerator(); // debug

        generateGame();
    }

    public void generateGame() {
        boolean[][] walls = generator.generate(currentCols, currentRows);
        this.model = new GameModel(currentCols, currentRows, walls);

        if (currentGridPanel != null) {
            cards.remove(currentGridPanel);
            currentGridPanel.cleanup();
        }

        currentGridPanel = new GridPanel(model, sounds, this::showSetup, this::generateGame, dirMoved -> handleMove(dirMoved));
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

    private void handleMove(Direction dir) {
        boolean moved = model.tryMove(dir);

        if (!moved) {
            mode.onHit();
        } else if (model.isFinished()) {
            sounds.playSuccess();
            mode.onFinish();
        } else {
            sounds.playMove();
        }
    }
}
