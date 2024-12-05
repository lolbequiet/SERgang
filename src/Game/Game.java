package Game;

import Engine.GameWindow;
import Engine.ScreenManager;
import Game.AudioManager;

/**
 * The entry point for the game.
 * This class initializes the GameWindow and ScreenManager, attaching the ScreenCoordinator to manage screens.
 * Background audio is started, and the game is launched.
 */
public class Game {

    public static void main(String[] args) {
        // Start the game
        new Game();

        // Play background music in a loop
        AudioManager.playLoop("Resources/Audio/background_lowered_more.wav");
    }

    public Game() {
        System.out.println("Game is starting...");

        // Initialize the game window
        GameWindow gameWindow = new GameWindow();

        // Retrieve the ScreenManager instance
        ScreenManager screenManager = gameWindow.getScreenManager();

        // Initialize and attach ScreenCoordinator
        ScreenCoordinator screenCoordinator = new ScreenCoordinator();
        screenManager.SetScreenCoordinator(screenCoordinator);

        // Set the starting screen
        screenManager.setCurrentScreen(screenCoordinator);

        // Start the game
        gameWindow.startGame();
    }
}
