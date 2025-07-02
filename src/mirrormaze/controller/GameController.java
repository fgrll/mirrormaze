package mirrormaze.controller;

import java.awt.*;
import javax.swing.*;

import MazeGenerator.DFSMazeGenerator;
import MazeGenerator.MazeGenerator;
import controls_demo.GameModel;
import controls_demo.GridPanel;
import controls_demo.SoundPlayer;

public class GameController {
    private final CardLayout cardLayout;
    private final JPanel cards;
    private SoundPlayer sounds;
    private GridPanel currentGridPanel;

    private int currentCols, currentRows;
    private MazeGenerator generator;

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
        this.currentCols = cols;
        this.currentRows = rows;
        this.generator = new DFSMazeGenerator(); // debug

        generateGame();
    }

    public void generateGame() {
        boolean[][] walls = generator.generate(currentCols, currentRows);
        GameModel model = new GameModel(currentCols, currentRows, walls);

        if (currentGridPanel != null) {
            cards.remove(currentGridPanel);
            currentGridPanel.cleanup();
        }

        currentGridPanel = new GridPanel(model, sounds, this::showSetup, this::generateGame);
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
}
