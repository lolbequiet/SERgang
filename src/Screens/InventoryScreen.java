package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Players.Cat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InventoryScreen extends Screen {
    private KeyLocker keyLocker = new KeyLocker();
    private ScreenCoordinator screenCoordinator;
    private Cat player;
    private BufferedImage swordSprite;
    private boolean isSwordLoaded = false;
    private boolean hasSword = false; // Track if the sword is picked up
    private boolean swordEquipped = false; // Track if the sword is equipped

    public InventoryScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        swordSprite = loadImage("resources/Sword.png"); // Attempt to load the sword sprite
    }

    /**
     * Load an image from the given path. If it fails, create a placeholder image.
     */
    private BufferedImage loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            isSwordLoaded = true;
            return image;
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return createPlaceholderImage(); // Placeholder on failure
        }
    }

    /**
     * Create a placeholder image for the sword.
     */
    private BufferedImage createPlaceholderImage() {
        BufferedImage placeholder = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 32, 32);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Sword", 5, 20);
        g2d.dispose();
        return placeholder;
    }

    /**
     * Set the player reference for inventory access.
     */
    public void setPlayer(Cat player) {
        this.player = player;
    }

    /**
     * Call this method when the player picks up the sword.
     */
    public void pickUpSword() {
        hasSword = true;
        System.out.println("Sword added to inventory.");
    }

    @Override
    public void initialize() {
        screenCoordinator.setGameState(GameState.PLAYING);
        keyLocker.lockKey(Key.I); // Lock the inventory key initially
    }

    @Override
    public void update() {
        // Equip or de-equip the sword when E is pressed
        if (Keyboard.isKeyDown(Key.E) && !keyLocker.isKeyLocked(Key.E)) {
            toggleSword(); // Toggle sword state
            keyLocker.lockKey(Key.E);
        }
        if (Keyboard.isKeyUp(Key.E)) {
            keyLocker.unlockKey(Key.E);
        }
    }

    /**
     * Toggles the sword between equipped and de-equipped states.
     */
    private void toggleSword() {
        if (hasSword) {
            swordEquipped = !swordEquipped;

            if (swordEquipped) {
                player.equipSword();
                System.out.println("Sword equipped.");
            } else {
                player.deEquipSword();
                System.out.println("Sword de-equipped.");
            }
        } else {
            System.out.println("You don't have the sword.");
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Draw inventory background
        graphicsHandler.drawFilledRectangle(50, 50, 180, 300, new Color(0, 0, 0, 150));
        graphicsHandler.drawRectangle(49, 49, 182, 302, Color.WHITE);

        // Draw inventory title
        graphicsHandler.drawString("Inventory", 60, 40, new Font("Arial", Font.BOLD, 20), Color.WHITE);

        // Draw each slot with its content
        drawSlot(graphicsHandler, 0, 50); // Slot 1: Sword
        drawSlot(graphicsHandler, 1, 150); // Slot 2
        drawSlot(graphicsHandler, 2, 250); // Slot 3

        // Instructions at the bottom
        graphicsHandler.drawString("Press I to open/close inventory", 55, 370, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
    }

    /**
     * Draws a single inventory slot with its content and background.
     */
    private void drawSlot(GraphicsHandler graphicsHandler, int slot, int y) {
        // Background color: green if equipped, otherwise black
        Color backgroundColor = (swordEquipped && slot == 0) ? new Color(0, 255, 0, 100) : new Color(0, 0, 0, 100);
        graphicsHandler.drawFilledRectangle(50, y, 180, 100, backgroundColor);

        // Draw slot border
        graphicsHandler.drawRectangle(50, y, 180, 100, Color.WHITE);

        // Draw slot number
        graphicsHandler.drawString(String.valueOf(slot + 1), 60, y + 20, new Font("Arial", Font.PLAIN, 14), Color.WHITE);

        // Draw item content
        if (slot == 0 && hasSword) {
            graphicsHandler.drawImage(swordSprite, 100, y + 30, 32, 32); // Draw sword sprite
        } else {
            graphicsHandler.drawString("Empty", 100, y + 50, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
        }
    }
}
