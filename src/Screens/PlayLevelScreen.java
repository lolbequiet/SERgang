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
    protected MapTile portal;


    private final int screenWidth = 800;
    private final int screenHeight = 600;
    private final int healthBarWidth = 200;
    private final int healthBarHeight = 20;
    private final int expBarHeight = 14;


    private long lastExpGainTime;
    private long allMobsDefeatedTime; // Track when all mobs are defeated
    private boolean allMobsDefeated;  // Flag to check if mobs have been defeated


    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.inventoryScreen = new InventoryScreen(screenCoordinator);
        this.isInventoryShowing = false;
        initialize();
    }


    public void initialize() {
        flagManager = new FlagManager();


        // Flags for tracking progress
        flagManager.addFlag("pickedUpSword");
        flagManager.addFlag("hasLostBall");
        flagManager.addFlag("hasTalkedToWalrus");
        flagManager.addFlag("hasFoundBall");
        flagManager.addFlag("WalrusMobDefeated", false);
        flagManager.addFlag("ReturnBackToEarth", false); // New quest flag


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


        keyLocker.lockKey(Key.M);
        keyLocker.lockKey(Key.ENTER);


        portal = map.getMapTile(1, 23);


        lastExpGainTime = System.currentTimeMillis();
        allMobsDefeated = false;  // Initially, mobs are not defeated
    }


    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);


                handlePortalInteraction();
                handleSwordPickup();
                handleNPCExp();
                handlePeriodicExpGain();
                checkAllMobsDefeated();


                if (allMobsDefeated && System.currentTimeMillis() - allMobsDefeatedTime >= 5000) {
                    playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
                }


                // }


                Point portalLoc = portal.getLocation();
                Point playerLoc = player.getLocation();
               
                double distance = Math.sqrt(Math.pow(portalLoc.x - playerLoc.x, 2) + Math.pow(portalLoc.y - playerLoc.y, 2));
                if (distance < 75) {
                    if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
                       
                        // player.moveRight(100);
                        screenCoordinator.setGameStatePersist(GameState.OVERWORLD);
                       
                        keyLocker.lockKey(Key.ENTER);
                    } else if (Keyboard.isKeyUp(Key.ENTER)) {
                        keyLocker.unlockKey(Key.ENTER);
                    }
                }


                // Check if the player picked up the sword and apply the change
                if (map.getFlagManager().isFlagSet("pickedUpSword")) {
                    ((Cat) player).pickUpSword();  // Equip sword upon pickup
                    map.getFlagManager().unsetFlag("pickedUpSword");  // Clear flag
                }


                // Check for defeated NPCs and grant EXP
                List<NPC> enemies = map.getEnemies();
                for (NPC npc : enemies) {
                    if (!npc.isActive() && npc.hasCombatLogic()) {
                        npc.setActive(false);  // Deactivate NPC to avoid repeated EXP gain
                        player.gainExp(npc.getExpReward());  // Grant EXP
                    }
                }


                // Handle player death
                if (player.getHealth() <= 0) {
                    playLevelScreenState = PlayLevelScreenState.GAME_OVER;
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
                flagManager.setFlag("ReturnBackToEarth", true); // Mark quest as completed
                System.out.println("Quest 'Return Back to Earth' completed!");
                screenCoordinator.setGameStatePersist(GameState.NEWWORLD); // Transition to second world
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
                npc.setExpGranted(true);
                player.gainExp(npc.getExpReward());
                System.out.println("Player gained EXP: " + npc.getExpReward());
            }
        }
    }


    private void checkAllMobsDefeated() {
        List<NPC> enemies = map.getEnemies();
        boolean allDefeated = true;


        for (NPC npc : enemies) {
            if (npc.isActive()) {
                allDefeated = false;
                break;
            }
        }


        if (allDefeated && !allMobsDefeated) {
            allMobsDefeated = true;  // Set the flag
            allMobsDefeatedTime = System.currentTimeMillis();  // Start the countdown
            System.out.println("All mobs defeated! Win screen will appear in 5 seconds.");
        }
    }


    private void handlePeriodicExpGain() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastExpGainTime >= 10000) { // Gain 10 EXP every 10 seconds
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


        graphicsHandler.drawString("HEALTH", 20, 15, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 20, currentHealthWidth, healthBarHeight, Color.RED);
        graphicsHandler.drawRectangle(20, 20, healthBarWidth, healthBarHeight, Color.BLACK);


        graphicsHandler.drawString("STAMINA", 20, 45, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 50, player.getStamina(), 14, Color.ORANGE);
        graphicsHandler.drawRectangle(20, 50, healthBarWidth, 14, Color.BLACK);


        graphicsHandler.drawString("EXP", 20, 75, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 80, currentExpWidth, expBarHeight, Color.BLUE);
        graphicsHandler.drawRectangle(20, 80, healthBarWidth, expBarHeight, Color.BLACK);


        graphicsHandler.drawString(
                "LEVEL: " + player.getLevel(),
                20, 110, new Font("Montserrat", Font.BOLD, 18), Color.YELLOW
        );


        graphicsHandler.drawString(
                "Coins: " + player.getCoins(),
                screenWidth - 120, 20, new Font("Montserrat", Font.BOLD, 18),
                Color.YELLOW
        );


        graphicsHandler.drawString("ACTIVE QUESTS:", screenWidth - 180, 60, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);


        if (!flagManager.isFlagSet("hasTalkedToWalrus")) {
            graphicsHandler.drawString("Talk To Seb", screenWidth - 167, 125, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        }


        if (!flagManager.isFlagSet("WalrusMobDefeated")) {
            graphicsHandler.drawString("Defeat 5 Mobs", screenWidth - 170, 90, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        }


        if (!flagManager.isFlagSet("ReturnBackToEarth")) {
            graphicsHandler.drawString("Return Back to Earth", screenWidth - 200, 150, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        }


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



