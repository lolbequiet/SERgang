package Screens;

import Engine.Screen;
import Engine.ScreenManager;
import Engine.GraphicsHandler;
import Game.ScreenCoordinator;
import Level.Player; // Import Player class
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import Engine.Keyboard;

public class DeathScreen extends Screen {
    private final Player player;

    // Constructor to receive the player object
    public DeathScreen(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() {
        System.out.println("Game Over! Press Enter to restart or Escape to go back to the menu.");
    }

    @Override
    public void update() {
        // Restart the game
        if (Keyboard.isKeyDown(KeyEvent.VK_ENTER)) {
            ScreenManager.getInstance().setCurrentScreen(new PlayLevelScreen(new ScreenCoordinator())); // Create a new instance of the level
        } 
        
        // Return to the main menu
        else if (Keyboard.isKeyDown(KeyEvent.VK_ESCAPE)) {
            ScreenManager.getInstance().setCurrentScreen(new MenuScreen(new ScreenCoordinator())); // Go back to the menu
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, Color.BLACK);
        graphicsHandler.drawString("You Have Died", 300, 250, new Font("Arial", Font.BOLD, 36), Color.RED);
        graphicsHandler.drawString("Press Enter to Restart or Escape to go to the Main Menu.", 200, 320, new Font("Arial", Font.PLAIN, 24), Color.WHITE);
    }
}
