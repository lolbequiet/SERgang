package Screens;

import Engine.Screen;
import Engine.ScreenManager;
import Engine.GraphicsHandler;
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
        System.out.println("Game Over! Press Enter to restart.");
    }

    @Override
    public void update() {
        if (Keyboard.isKeyDown(KeyEvent.VK_ENTER)) {
            player.reset();
            ScreenManager.getInstance().setCurrentScreen(new TestMapScreen(player));
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, Color.BLACK);
        graphicsHandler.drawString("You Have Died", 300, 250, new Font("Arial", Font.BOLD, 36), Color.RED);
        graphicsHandler.drawString("Please Reconfigure The Game.", 270, 320, new Font("Arial", Font.PLAIN, 24), Color.WHITE);
    }
}

