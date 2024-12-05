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
    private BufferedImage potionSprite;
    private BufferedImage sandwichSprite;

    private boolean isSwordLoaded = false;
    private boolean swordEquipped = false; // Track if the sword is equipped

    public InventoryScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        swordSprite = loadImage("resources/Sword.png");    // Load sword sprite for slot 1
        potionSprite = loadImage("resources/Potion.png");  // Load potion sprite for slot 2
        sandwichSprite = loadImage("resources/Sandwich.png"); // Load sandwich sprite for slot 3
    }

    /**
     * Load an image from the given path. If it fails, create a placeholder image.
     */
    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return createPlaceholderImage(); // Placeholder on failure
        }
    }

    /**
     * Create a placeholder image for items.
     */
    private BufferedImage createPlaceholderImage() {
        BufferedImage placeholder = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 32, 32);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Item", 5, 20);
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
            swordEquipped = !swordEquipped;
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Draw inventory background
        graphicsHandler.drawFilledRectangle(50, 100, 220, 400, new Color(0, 0, 0, 150));
        graphicsHandler.drawRectangle(49, 100, 222, 402, Color.WHITE);

        // Draw inventory title
        graphicsHandler.drawString("Inventory", 110, 90, new Font("Arial", Font.BOLD, 20), Color.WHITE);

        // Draw each slot
        drawSlot(graphicsHandler, 0, 120, "Weapon Slot", swordSprite);    // Slot 1
        drawSlot(graphicsHandler, 1, 240, "Healing Slot", potionSprite); // Slot 2
        drawSlot(graphicsHandler, 2, 360, "Healing Slot 2", sandwichSprite); // Slot 3

        // Instructions at the bottom
        graphicsHandler.drawString("Press I to open/close inventory", 55, 520, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
    }

    /**
     * Draws a single inventory slot with its content and background.
     */
    private void drawSlot(GraphicsHandler graphicsHandler, int slot, int y, String label, BufferedImage sprite) {
        // Determine the background color for the slot
        Color backgroundColor = (swordEquipped && slot == 0)
            ? new Color(0, 255, 0, 150) // Green for equipped sword
            : new Color(0, 0, 0, 100);  // Default black

        // Draw the slot background
        graphicsHandler.drawFilledRectangle(60, y, 200, 100, backgroundColor);

        // Draw the slot border
        graphicsHandler.drawRectangle(60, y, 200, 100, Color.WHITE);

        // Draw the item sprite if it exists
        if (sprite != null) {
            graphicsHandler.drawImage(sprite, 120, y + 25, 50, 50); // Centered within the slot
        } else {
            graphicsHandler.drawString("Empty", 120, y + 55, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
        }

        // Draw the slot label below the slot
        graphicsHandler.drawString(label, 75, y + 95, new Font("Arial", Font.BOLD, 14), Color.WHITE);
    }
}
