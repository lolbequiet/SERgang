package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.TestMap;
import Players.Cat;
import Utils.Direction;

import java.awt.Color;
import java.awt.Font;

public class PlayLevelScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    protected boolean isInventoryShowing;
    protected InventoryScreen inventoryScreen;

    private final int screenWidth = 800;
    private final int screenHeight = 600;
    private final int healthBarWidth = 200;
    private final int healthBarHeight = 20;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.inventoryScreen = new InventoryScreen(screenCoordinator);
        this.isInventoryShowing = false;
        initialize();
    }

    public void initialize() {
        flagManager = new FlagManager();

        // Flags for tracking progress and sword pickup
        flagManager.addFlag("pickedUpSword");
        flagManager.addFlag("hasLostBall");
        flagManager.addFlag("hasTalkedToWalrus");
        flagManager.addFlag("hasFoundBall");

        map = new TestMap();
        map.setFlagManager(flagManager);

        player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);
        player.setFacingDirection(Direction.LEFT);

        playLevelScreenState = PlayLevelScreenState.RUNNING;

        map.setPlayer(player);
        map.getTextbox().setInteractKey(player.getInteractKey());

        winScreen = new WinScreen(this);

        inventoryScreen.setPlayer((Cat) player);

        keyLocker.lockKey(Key.M); // Lock the 'M' key initially
    }

    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);

                // Check if the player picked up the sword and apply the change
                if (map.getFlagManager().isFlagSet("pickedUpSword")) {
                    ((Cat) player).pickUpSword();  // Equip sword upon pickup
                    map.getFlagManager().unsetFlag("pickedUpSword"); // Clear flag after use
                }

                // Game over logic
                if (player.getHealth() <= 0) {
                    playLevelScreenState = PlayLevelScreenState.GAME_OVER;
                }

                // Level completion logic
                if (map.getFlagManager().isFlagSet("hasFoundBall")) {
                    playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
                }
                break;

            case LEVEL_COMPLETED:
                winScreen.update();
                break;

            case GAME_OVER:
                goBackToMenu();
                break;
        }

        // Toggle Inventory Screen
        if (Keyboard.isKeyDown(Key.I) && !keyLocker.isKeyLocked(Key.I)) {
            isInventoryShowing = !isInventoryShowing;
            keyLocker.lockKey(Key.I);
        } else if (Keyboard.isKeyUp(Key.I)) {
            keyLocker.unlockKey(Key.I);
        }

        // Navigate to Level Select
        if (Keyboard.isKeyDown(Key.M) && !keyLocker.isKeyLocked(Key.M)) {
            screenCoordinator.setGameStatePersist(GameState.LEVEL_SELECT);
            keyLocker.lockKey(Key.M);
        } else if (Keyboard.isKeyUp(Key.M)) {
            keyLocker.unlockKey(Key.M);
        }

        // Reset Level when ESC is pressed
        if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            resetLevel();
            keyLocker.lockKey(Key.ESC);
        } else if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                drawHUD(graphicsHandler);

                if (isInventoryShowing) {
                    inventoryScreen.draw(graphicsHandler);
                }
                break;

            case LEVEL_COMPLETED:
                winScreen.draw(graphicsHandler);
                break;

            case GAME_OVER:
                graphicsHandler.drawString(
                    "Game Over",
                    screenWidth / 2 - 50,
                    screenHeight / 2,
                    new Font("Arial", Font.BOLD, 24),
                    Color.RED
                );
                break;
        }
    }

    private void drawHUD(GraphicsHandler graphicsHandler) {
        int currentHealthWidth = (int) ((player.getHealth() / (double) player.getMaxHealth()) * healthBarWidth);

        // Draw health bar
        graphicsHandler.drawString("HEALTH", 20, 15, new Font("Arial", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 20, currentHealthWidth, healthBarHeight, Color.RED);
        graphicsHandler.drawRectangle(20, 20, healthBarWidth, healthBarHeight, Color.BLACK);

        // Draw stamina bar
        graphicsHandler.drawString("STAMINA", 20, 45, new Font("Arial", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 50, player.getStamina(), 14, Color.ORANGE);
        graphicsHandler.drawRectangle(20, 50, healthBarWidth, 14, Color.BLACK);

        // Active Quest Section
        graphicsHandler.drawString("ACTIVE QUEST:", screenWidth - 180, 30, new Font("Arial", Font.BOLD, 18), Color.WHITE);

        if (!flagManager.isFlagSet("hasTalkedToWalrus")) {
            graphicsHandler.drawString("Talk To Seb", screenWidth - 170, 60, new Font("Arial", Font.BOLD, 18), Color.WHITE);
        }

        // Inventory Button
        int buttonWidth = 60;
        int buttonHeight = 30;
        graphicsHandler.drawFilledRectangle(
            10,
            screenHeight / 2 - buttonHeight / 2,
            buttonWidth,
            buttonHeight,
            Color.GRAY
        );
        graphicsHandler.drawRectangle(
            10,
            screenHeight / 2 - buttonHeight / 2,
            buttonWidth,
            buttonHeight,
            Color.BLACK
        );
        graphicsHandler.drawString("Inventory", 12, screenHeight / 2, new Font("Arial", Font.PLAIN, 12), Color.WHITE);
    }

    public void resetLevel() {
        System.out.println("Resetting the level...");
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, GAME_OVER
    }
}
