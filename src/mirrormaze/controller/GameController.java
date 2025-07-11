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

    public void selectStandardMode() {
        this.mode = new StandardMode(this);
        showSetup();
    }

    public void startGame(int cols, int rows) {
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

        currentGridPanel = new GridPanel(model, sounds, this::showSetup, this::generateGame, this::handleMove);
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
