package mirrormaze.view;

import javax.swing.*;

import mirrormaze.controller.GameController;
import mirrormaze.util.SettingsManager;
import mirrormaze.util.SoundPlayer;

import java.awt.*;

/**
 * MainFrame manages all GUI panels using a CardLayout
 * 
 * @see https://www.geeksforgeeks.org/java/java-jframe/
 */
public class MainFrame extends JFrame {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private final GameController controller;
    private final SettingsManager settings;
    private final SoundPlayer sounds;

    /**
     * Creates main window and initializes all cards and GameController
     * 
     * @param sounds central SoundManager instance
     * @param settings central SettingsManager instance
     * 
     * @see mirrormaze.util.SoundPlayer
     * @see mirrormaze.util.SettingsManager
     */
    public MainFrame(SoundPlayer sounds, SettingsManager settings) {
        super("Mirror Maze");
        this.sounds = sounds;
        this.settings = settings;
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        controller = new GameController(cardLayout, cards, sounds, settings);

        initCards();

        setContentPane(cards);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // pack();
        setLocationRelativeTo(null);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        
        setVisible(true);
    }


    /**
     * Initializes panels and adds them to cards.
     * 
     * @see javax.swing.JPanel
     * @see https://javabeginners.de/Layout/GridBagLayout.php
     */
    private void initCards() {
        MenuPanel menu = new MenuPanel(controller::showSelectionPanel, controller::showSettings);
        cards.add(menu, "MENU");

        SettingsPanel settingsPanel = new SettingsPanel(settings, sounds, controller::showMenu);
        cards.add(settingsPanel, "SETTINGS");

        StandardSetupPanel setup = new StandardSetupPanel(
                (dim, cfg) -> controller.startGame(dim, cfg),
                controller::showMenu);
        cards.add(setup, "SETUP_STANDARD");

        SurvivalSetupPanel survivalSetup = new SurvivalSetupPanel((cfg, dim) -> controller.startGame(dim, cfg), controller::showSelectionPanel);
        cards.add(survivalSetup, "SETUP_SURVIVAL");

        ModeSelectionPanel modes = new ModeSelectionPanel(controller::selectStandardMode, controller::selectSurvivalMode);
        cards.add(modes, "MODE");

        cardLayout.show(cards, "MENU");
    }

    /**
     * Entrypoint. Instantiates SettingsManager and SoundPlayer, passes them to the MainFrame() constructor and sets the volume.
     * 
     * @param args none
     * 
     * @see mirrormaze.util.SoundPlayer
     * @see mirrormaze.util.SettingsManager
     */
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
