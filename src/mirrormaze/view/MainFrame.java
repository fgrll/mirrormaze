package mirrormaze.view;

// Hallos Piara!

import javax.swing.*;

import mirrormaze.controller.GameController;
import mirrormaze.util.SettingsManager;
import mirrormaze.util.SoundPlayer;

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
        // pack();
        setLocationRelativeTo(null);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        
        setVisible(true);
    }

    private void initCards() {
        MenuPanel menu = new MenuPanel(controller::showSelectionPanel, controller::showSettings);
        cards.add(menu, "MENU");

        SettingsPanel settingsPanel = new SettingsPanel(settings, sounds, controller::showMenu);
        cards.add(settingsPanel, "SETTINGS");

        StandardSetupPanel setup = new StandardSetupPanel(
                (dim, cfg) -> controller.startGame(dim, cfg),
                controller::showMenu);
        cards.add(setup, "SETUP_STANDARD");

        ModeSelectionPanel modes = new ModeSelectionPanel(controller::selectStandardMode);
        cards.add(modes, "MODE");

        cardLayout.show(cards, "MENU");
    }

    public static void main(String[] args) {
        SettingsManager settings = new SettingsManager();

        SoundPlayer sounds = new SoundPlayer();

        sounds.setThemeVolume(settings.getThemeVolume());
        sounds.setSFXVolume(settings.getSFXVolume());

        sounds.playThemeLoop();
        SwingUtilities.invokeLater(() -> {
            new MainFrame(sounds, settings);
        });
    }
}
