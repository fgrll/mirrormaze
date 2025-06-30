package menu_demo;

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
        if (currentGridPanel != null) {
            cards.remove(currentGridPanel);
            currentGridPanel.cleanup();
        }

        MazeGenerator gen = new DFSMazeGenerator();
        boolean[][] walls = gen.generate(cols, rows);
        GameModel model = new GameModel(cols, rows, walls);
        currentGridPanel = new GridPanel(model, sounds, this::showSetup);
        cards.add(currentGridPanel, "GAME");
        currentGridPanel.requestFocusInWindow();
    }

    public void showMenu() {
        cardLayout.show(cards, "MENU");
    }

    public void showSettings() {
        cardLayout.show(cards, "SETTINGS");
    }
}
