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
    private int equippedSlot = -1;  // Track the currently equipped slot (-1 means nothing equipped)
    private boolean isSwordLoaded = false; // Track if sword sprite is loaded successfully

    public InventoryScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        swordSprite = loadImage("resources/Sword.png"); // Try to load the sword sprite
    }

    private BufferedImage loadImage(String path) {
        try {
            File imageFile = new File(path);
            if (!imageFile.exists()) {
                System.out.println("Image not found at: " + imageFile.getAbsolutePath());
                return createPlaceholderImage(); // Use placeholder if not found
            }
            BufferedImage image = ImageIO.read(imageFile);
            isSwordLoaded = true;
            return image;
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return createPlaceholderImage(); // Use placeholder on failure
        }
    }

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

    public void setPlayer(Cat player) {
        this.player = player;
    }

    @Override
    public void initialize() {
        screenCoordinator.setGameState(GameState.PLAYING);
        keyLocker.lockKey(Key.I);
    }

    @Override
    public void update() {
        // Equip or de-equip based on slot keys (1, 2, 3)
        if (Keyboard.isKeyDown(Key.ONE) && !keyLocker.isKeyLocked(Key.ONE)) {
            toggleSlot(0);  // Toggle slot 1
            keyLocker.lockKey(Key.ONE);
        }
        if (Keyboard.isKeyDown(Key.TWO) && !keyLocker.isKeyLocked(Key.TWO)) {
            toggleSlot(1);  // Toggle slot 2
            keyLocker.lockKey(Key.TWO);
        }
        if (Keyboard.isKeyDown(Key.THREE) && !keyLocker.isKeyLocked(Key.THREE)) {
            toggleSlot(2);  // Toggle slot 3
            keyLocker.lockKey(Key.THREE);
        }

        if (Keyboard.isKeyUp(Key.ONE)) keyLocker.unlockKey(Key.ONE);
        if (Keyboard.isKeyUp(Key.TWO)) keyLocker.unlockKey(Key.TWO);
        if (Keyboard.isKeyUp(Key.THREE)) keyLocker.unlockKey(Key.THREE);
    }

    private void toggleSlot(int slot) {
        if (equippedSlot == slot) {
            // If the slot is already equipped, de-equip it
            equippedSlot = -1;
            player.deEquipSword();  // Reset player damage
            System.out.println("Slot " + (slot + 1) + " de-equipped.");
        } else {
            // Equip the selected slot
            equippedSlot = slot;
            if (slot == 0) player.equipSword();  // Equip sword if it's slot 1
            System.out.println("Slot " + (slot + 1) + " equipped.");
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(50, 50, 180, 300, new Color(0, 0, 0, 150));
        graphicsHandler.drawRectangle(49, 49, 182, 302, Color.WHITE);
        graphicsHandler.drawString("Inventory", 60, 40, new Font("Arial", Font.BOLD, 20), Color.WHITE);

        // Draw Slot 1 (Sword slot)
        drawSlot(graphicsHandler, 0, 50, "Sword");

        // Draw Slot 2
        drawSlot(graphicsHandler, 1, 150, "Empty");

        // Draw Slot 3
        drawSlot(graphicsHandler, 2, 250, "Empty");

        graphicsHandler.drawString("Press I to open/close inventory", 55, 370, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
    }

    private void drawSlot(GraphicsHandler graphicsHandler, int slot, int y, String item) {
        Color borderColor = (slot == equippedSlot) ? Color.GREEN : Color.WHITE;
        graphicsHandler.drawRectangle(50, y, 180, 100, borderColor);
        graphicsHandler.drawString(String.valueOf(slot + 1), 80, y + 50, new Font("Arial", Font.PLAIN, 12), Color.WHITE);

        if (slot == 0 && isSwordLoaded) {
            graphicsHandler.drawImage(swordSprite, 100, y + 20, 32, 32);
        } else {
            graphicsHandler.drawString(item, 100, y + 40, new Font("Arial", Font.PLAIN, 12), Color.WHITE);
        }
    }
}
