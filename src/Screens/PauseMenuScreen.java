package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import java.awt.Color;
import java.awt.Font;

public class PauseMenuScreen extends Screen {
    private ScreenCoordinator screenCoordinator;

    public PauseMenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Any initialization if necessary
    }

    @Override
    public void update() {
        // Handle inputs for resuming the game or navigating the pause menu options
        // Assuming you will replace this with actual key detection logic or button press
        // if (/* condition to resume */) {
        //     screenCoordinator.setGameState(GameState.PLAYING); // Switch back to the game
        // }

        // if (/* condition to quit or go back to the menu */) {
        //     screenCoordinator.setGameState(GameState.MENU);
        // }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, new Color(0, 0, 0, 150)); // Semi-transparent background
        graphicsHandler.drawString("PAUSED", 350, 300, new Font("Arial", Font.BOLD, 36), Color.WHITE);
        graphicsHandler.drawString("Press P to Resume", 320, 350, new Font("Arial", Font.PLAIN, 24), Color.WHITE);
    }
}
