package menu_demo;

import javax.swing.*;

import controls_demo.SoundPlayer;
import settings_test.SettingsManager;
import settings_test.SettingsPanel;

import java.awt.*;

public class MainFrame extends JFrame {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private final GameController controller;
    private final SettingsManager settings;
    private final SoundPlayer sounds;

    public MainFrame(SoundPlayer sounds, SettingsManager settings) {
        super("Mirror Maze");
        this.sounds = sounds;
        this.settings = settings;
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
        MenuPanel menu = new MenuPanel(controller::showSetup, controller::showSettings);
        cards.add(menu, "MENU");

        SettingsPanel settingsPanel = new SettingsPanel(settings, sounds, controller::showMenu);
        cards.add(settingsPanel, "SETTINGS");


        SetupPanel setup = new SetupPanel(this::startGame, controller::showMenu);
        cards.add(setup, "SETUP");
        
        cardLayout.show(cards, "MENU");
    }

    private void startGame(int cols, int rows) {
        controller.startGame(cols, rows);
        pack();
        cardLayout.show(cards, "GAME");
    }
    public static void main(String[] args) {
        SettingsManager settings = new SettingsManager();

        SoundPlayer sounds = new SoundPlayer();

        sounds.setThemeVolume(settings.getThemeVolume());
        sounds.setSFXVolume(settings.getSFXVolume());

        sounds.playThemeLoop();
        SwingUtilities.invokeLater(() -> {
            new MainFrame(sounds, settings).setVisible(true);
        });
    }
}
