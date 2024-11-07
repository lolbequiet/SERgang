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
import Utils.Point;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

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
    protected ShopScreen ShopScreen;
    protected MapTile portal;

    private final int screenWidth = 800;
    private final int screenHeight = 600;
    private final int healthBarWidth = 200;
    private final int healthBarHeight = 20;
    private final int expBarHeight = 14;  // EXP bar height

    private long lastExpGainTime;  // Track time for periodic EXP gain

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.inventoryScreen = new InventoryScreen(screenCoordinator);
        this.ShopScreen = new ShopScreen(this, player);
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
        flagManager.addFlag("WalrusMobDefeated", false);

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

        keyLocker.lockKey(Key.M);  // Lock the 'M' key initially
        keyLocker.lockKey(Key.ENTER);

        portal = map.getMapTile(1, 23);

        // Initialize time tracking
        lastExpGainTime = System.currentTimeMillis();
    }

    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);

                handlePortalInteraction();
                handleSwordPickup();
                handleNPCExp();
                handlePeriodicExpGain();  // Adds EXP every 10 seconds

                if (player.getHealth() <= 0) {
                    playLevelScreenState = PlayLevelScreenState.GAME_OVER;
                }

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

        handleKeyToggles();
    }

    private void handlePortalInteraction() {
        Point portalLoc = portal.getLocation();
        Point playerLoc = player.getLocation();
        double distance = Math.sqrt(Math.pow(portalLoc.x - playerLoc.x, 2) + Math.pow(portalLoc.y - playerLoc.y, 2));

        if (distance < 75) {
            if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
                screenCoordinator.setGameStatePersist(GameState.NEWWORLD);
                keyLocker.lockKey(Key.ENTER);
            } else if (Keyboard.isKeyUp(Key.ENTER)) {
                keyLocker.unlockKey(Key.ENTER);
            }
        }
    }

    private void handleSwordPickup() {
        if (map.getFlagManager().isFlagSet("pickedUpSword")) {
            ((Cat) player).pickUpSword();
            map.getFlagManager().unsetFlag("pickedUpSword");
        }
    }

    private void handleNPCExp() {
        List<NPC> enemies = map.getEnemies();
        for (NPC npc : enemies) {
            if (!npc.isExpGranted() && !npc.isActive()) {
                npc.setExpGranted(true);  // Prevent repeated EXP grants
                player.gainExp(npc.getExpReward());
                System.out.println("Player gained EXP: " + npc.getExpReward());
            }
        }
    }
    

    private void handlePeriodicExpGain() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastExpGainTime >= 10000) {  // Gain 10 EXP every 10 seconds
            player.gainExp(10);
            System.out.println("Periodic EXP gain: 10. Current EXP: " + player.getExperience());
            lastExpGainTime = currentTime;
        }
    }

    private void handleKeyToggles() {
        if (Keyboard.isKeyDown(Key.I) && !keyLocker.isKeyLocked(Key.I)) {
            isInventoryShowing = !isInventoryShowing;
            keyLocker.lockKey(Key.I);
        } else if (Keyboard.isKeyUp(Key.I)) {
            keyLocker.unlockKey(Key.I);
        }

        if (Keyboard.isKeyDown(Key.M) && !keyLocker.isKeyLocked(Key.M)) {
            screenCoordinator.setGameStatePersist(GameState.LEVEL_SELECT);
            keyLocker.lockKey(Key.M);
        } else if (Keyboard.isKeyUp(Key.M)) {
            keyLocker.unlockKey(Key.M);
        }

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

            case SHOP:
                ShopScreen.draw(graphicsHandler);
                break;

            case GAME_OVER:
                graphicsHandler.drawString(
                        "Game Over",
                        screenWidth / 2 - 50,
                        screenHeight / 2,
                        new Font("Montserrat", Font.BOLD, 24),
                        Color.RED
                );
                break;
        }
    }

    private void drawHUD(GraphicsHandler graphicsHandler) {
        int currentHealthWidth = (int) ((player.getHealth() / (double) player.getMaxHealth()) * healthBarWidth);
        int currentExpWidth = (int) ((player.getExperience() / (double) player.getExpToLevelUp()) * healthBarWidth);

        // Health Bar
        graphicsHandler.drawString("HEALTH", 20, 15, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 20, currentHealthWidth, healthBarHeight, Color.RED);
        graphicsHandler.drawRectangle(20, 20, healthBarWidth, healthBarHeight, Color.BLACK);

        // Stamina Bar
        graphicsHandler.drawString("STAMINA", 20, 45, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 50, player.getStamina(), 14, Color.ORANGE);
        graphicsHandler.drawRectangle(20, 50, healthBarWidth, 14, Color.BLACK);

        // EXP Bar
        graphicsHandler.drawString("EXP", 20, 75, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 80, currentExpWidth, expBarHeight, Color.BLUE);
        graphicsHandler.drawRectangle(20, 80, healthBarWidth, expBarHeight, Color.BLACK);

        // Player Level
        graphicsHandler.drawString(
                "LEVEL: " + player.getLevel(),
                20, 110, new Font("Montserrat", Font.BOLD, 18), Color.YELLOW
        );

        // Coins
        graphicsHandler.drawString(
                "Coins: " + player.getCoins(),
                screenWidth - 120, 20, new Font("Montserrat", Font.BOLD, 18), Color.YELLOW
        );

        // Active Quests
        graphicsHandler.drawString("ACTIVE QUESTS:", screenWidth - 180, 60, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);

        if (!flagManager.isFlagSet("hasTalkedToWalrus")) {
            graphicsHandler.drawString("Talk To Seb", screenWidth - 167, 125, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        }

        if (!flagManager.isFlagSet("WalrusMobDefeated")) {
            graphicsHandler.drawString("Defeat 5 Mobs", screenWidth - 170, 90, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        }

        // Inventory Button
        int buttonWidth = 60;
        int buttonHeight = 30;
        graphicsHandler.drawFilledRectangle(
                10,
                screenHeight / 2 - buttonHeight / 2,
                buttonWidth,
                buttonHeight,
                Color.RED
        );
        graphicsHandler.drawRectangle(
                10,
                screenHeight / 2 - buttonHeight / 2,
                buttonWidth,
                buttonHeight,
                Color.BLACK
        );
        graphicsHandler.drawString("Inventory", 12, screenHeight / 2, new Font("Montserrat", Font.PLAIN, 12), Color.WHITE);
    }

    public void resetLevel() {
        System.out.println("Resetting the level...");
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, GAME_OVER, SHOP
    }
}
