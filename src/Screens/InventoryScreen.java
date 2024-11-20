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
    private boolean swordEquipped = false; // Track if the sword is equipped

    public InventoryScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        swordSprite = loadImage("resources/Sword.png"); // Load sword sprite for slot 1
        swordEquipped = false;  // Sword starts unequipped by default
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
        if (player != null) {
            if (swordEquipped) {
                player.deEquipSword();
                System.out.println("Sword de-equipped.");
            } else {
                player.equipSword();
                System.out.println("Sword equipped.");
            }
        }
    }
    

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Draw inventory background
        graphicsHandler.drawFilledRectangle(50, 400, 180, 300, new Color(0, 0, 0, 150));
        graphicsHandler.drawRectangle(49, 400, 182, 302, Color.WHITE);

        // Draw inventory title
        graphicsHandler.drawString("Inventory", 50, 390, new Font("Arial", Font.BOLD, 20), Color.WHITE);

        // Draw each slot with its content
        drawSlot(graphicsHandler, 0, 400); // Slot 1: Sword
        drawSlot(graphicsHandler, 1, 500); // Slot 2
        drawSlot(graphicsHandler, 2, 600); // Slot 3

        // Instructions at the bottom
        graphicsHandler.drawString("Press I to open/close inventory", 50, 730, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
    }

// add boolean or flag that changes the slot the sword is in to green when equiping the sword
// have a indicator for when sword is equiped not just the terminal!

//code
//code
//code
//code
//code
//code


    /**
     * Draws a single inventory slot with its content and background.
     */
    private void drawSlot(GraphicsHandler graphicsHandler, int slot, int y) {
        // Check if the sword is equipped and this is slot 1
        Color backgroundColor = (swordEquipped && slot == 0)
            ? new Color(0, 255, 0, 150)  // Transparent green if equipped
            : new Color(0, 0, 0, 100);   // Default background
    
        // Draw the slot background
        graphicsHandler.drawFilledRectangle(50, y, 180, 100, backgroundColor);
    
        // Draw the slot border
        graphicsHandler.drawRectangle(50, y, 180, 100, Color.WHITE);
    
        // Draw the slot number
        graphicsHandler.drawString(String.valueOf(slot + 1), 60, y + 20, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
    
        // Draw the sword in slot 1
        if (slot == 0) {
            graphicsHandler.drawImage(swordSprite, 100, y + 30, 32, 32);  // Sword sprite
        } else {
            graphicsHandler.drawString("Empty", 100, y + 50, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
        }
    }
    
    
    
}
