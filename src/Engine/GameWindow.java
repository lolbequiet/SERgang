package Engine;

import javax.swing.*;
import java.awt.*;


/*
 * The JFrame that holds the GamePanel
 * Just does some setup and exposes the gamePanel's screenManager to allow an external class to setup their own content and attach it to this engine.
 */
public class GameWindow {
    private JFrame gameWindow;
    private GamePanel gamePanel;

    public GameWindow() {
        gameWindow = new JFrame("Game");
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the game window to fullscreen
        gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameWindow.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        gamePanel = new GamePanel();
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.setPreferredSize(new Dimension(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT));

        // Add game panel to a container panel to center it
        JPanel container = new JPanel();

        container.setLayout(null); 

        container.setBackground(Color.BLACK); // Black background to fill the extra space

        int gamePanelX = (screenWidth - Config.GAME_WINDOW_WIDTH) / 2;

        int gamePanelY = (screenHeight - Config.GAME_WINDOW_HEIGHT) / 2;

        gamePanel.setBounds(gamePanelX, gamePanelY, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT);

        container.add(gamePanel);

        gameWindow.setContentPane(container);
        gameWindow.setVisible(true);

        gamePanel.setupGame();
    }

    // triggers the game loop to start as defined in the GamePanel class
    public void startGame() {
        gamePanel.startGame();
    }

    public ScreenManager getScreenManager() {
        return gamePanel.getScreenManager();
    }
}