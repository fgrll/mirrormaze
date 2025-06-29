package menu_demo;

import java.awt.*;
import javax.swing.*;

import MazeGenerator.DFSMazeGenerator;
import MazeGenerator.MazeGenerator;
import controls_demo.GameModel;
import controls_demo.GridPanel;

public class GameController {
    private final CardLayout cardLayout;
    private final JPanel cards;

    public GameController(CardLayout cardLayout, JPanel cards) {
        this.cardLayout = cardLayout;
        this.cards = cards;
    }

    public void showSetup() {
        cardLayout.show(cards, "SETUP");
    }

    public void startGame(int cols, int rows) {
        MazeGenerator gen = new DFSMazeGenerator();
        boolean[][] walls = gen.generate(cols, rows);
        GameModel model = new GameModel(cols, rows, walls);
        GridPanel gamePanel = new GridPanel(model);
        cards.add(gamePanel, "GAME");
        cardLayout.show(cards, "GAME");
        gamePanel.requestFocusInWindow();
    }
}
