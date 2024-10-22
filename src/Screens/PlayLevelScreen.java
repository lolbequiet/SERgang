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
import Engine.*;

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

    private final int screenWidth = 800;  // Hardcoded screen width
    private final int screenHeight = 600; // Hardcoded screen height

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.inventoryScreen = new InventoryScreen(screenCoordinator);
        this.isInventoryShowing = false;
        this.inventoryScreen.initialize();
    }   

    public void initialize() {
        flagManager = new FlagManager();
        // ----
        flagManager.addFlag("hasLostBall", false);
        //flag for talked to walrus
        flagManager.addFlag("hasTalkedToWalrus", false);

        

        // ----
        flagManager.addFlag("hasTalkedToDinosaur", false);
        flagManager.addFlag("hasFoundBall", false);
       

        map = new TestMap();
        map.setFlagManager(flagManager);

        player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.setFacingDirection(Direction.LEFT);

        map.setPlayer(player);
        map.getTextbox().setInteractKey(player.getInteractKey());
        map.preloadScripts();
        winScreen = new WinScreen(this);

        keyLocker.lockKey(Key.M);
    }

    private boolean isInLevelSelect = false;

    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);

                break;
                
                case LEVEL_COMPLETED:
                winScreen.update();
                break;
            }
            
            if (Keyboard.isKeyDown(Key.I) && !keyLocker.isKeyLocked(Key.I)) {
                isInventoryShowing = !isInventoryShowing;
                inventoryScreen.initialize();

                keyLocker.lockKey(Key.I);
            } else if (Keyboard.isKeyUp(Key.I)){
                keyLocker.unlockKey(Key.I);
            }
        //

        if (Keyboard.isKeyDown(Key.M) && !keyLocker.isKeyLocked(Key.M)) {
            screenCoordinator.setGameStatePersist(GameState.LEVEL_SELECT);
			keyLocker.lockKey(Key.M);
		}

		if (Keyboard.isKeyUp(Key.M)) {
			keyLocker.unlockKey(Key.M);
		}
    
        if (map.getFlagManager().isFlagSet("hasFoundBall")) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
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
        }
    }

    private void drawHUD(GraphicsHandler graphicsHandler) {
        int barWidth = 200;
        int barHeight = 20;
        int staminaBarHeight = 14;

        // Draw "HEALTH" label above the health bar
        graphicsHandler.drawString("HEALTH", 20, 15, new Font("Arial", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 20, player.getHealth() * 2, barHeight, Color.RED);
        graphicsHandler.drawRectangle(20, 20, barWidth, barHeight, Color.BLACK);

        // Draw "STAMINA" label above the stamina bar
        graphicsHandler.drawString("STAMINA", 20, 45, new Font("Arial", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 50, player.getStamina(), staminaBarHeight, Color.ORANGE);
        graphicsHandler.drawRectangle(20, 50, barWidth, staminaBarHeight, Color.BLACK);

        // Text holder for Quests that are Active (shown below it)
        graphicsHandler.drawString("ACTIVE QUEST:", screenWidth - 180, 30, new Font("Arial", Font.BOLD, 18), Color.WHITE);

        int buttonWidth = 60;
        int buttonHeight = 30;
    // checks if quest is false when true will evaporate
        if (!flagManager.isFlagSet("hasTalkedToWalrus")) {
            graphicsHandler.drawString("Talk To Seb", screenWidth - 170, 60, new Font("Arial", Font.BOLD, 18), Color.WHITE);
    }
    
        // Draw a single Inventory button on the left side of the screen
        graphicsHandler.drawFilledRectangle(10, screenHeight / 2 - buttonHeight / 2, buttonWidth, buttonHeight, Color.GRAY);
        graphicsHandler.drawRectangle(10, screenHeight / 2 - buttonHeight / 2, buttonWidth, buttonHeight, Color.BLACK);
        graphicsHandler.drawString("Inventory", 12, screenHeight / 2, new Font("Arial", Font.PLAIN, 12), Color.WHITE);
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED
    }
}