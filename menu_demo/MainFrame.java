package menu_demo;

import javax.swing.*;

import controls_demo.SoundPlayer;

import java.awt.*;

public class MainFrame extends JFrame {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private final GameController controller;

    public MainFrame(SoundPlayer sounds) {
        super("Mirror Maze");
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        controller = new GameController(cardLayout, cards, sounds);

        initCards();

        setContentPane(cards);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void initCards() {
        MenuPanel menu = new MenuPanel(controller::showSetup);
        cards.add(menu, "MENU");


        SetupPanel setup = new SetupPanel(controller::startGame, controller::showMenu);
        cards.add(setup, "SETUP");
        
        cardLayout.show(cards, "MENU");
    }
    public static void main(String[] args) {
        SoundPlayer sounds = new SoundPlayer();
        sounds.setThemeVolume(0.2f);
        sounds.playThemeLoop();
        SwingUtilities.invokeLater(() -> {
            new MainFrame(sounds).setVisible(true);
        });
    }
}
