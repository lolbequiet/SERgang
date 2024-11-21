package Screens;



import java.awt.Graphics2D;
import java.awt.FontMetrics;
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
    protected boolean showRectangle; // Toggle flag for rectangle
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
        this.showRectangle = false; // Rectangle is hidden initially
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
                screenCoordinator.setGameStatePersist(GameState.OVERWORLD); // Transition to second world
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

        //Triangle for Quests
        if (Keyboard.isKeyDown(Key.Q) && !keyLocker.isKeyLocked(Key.Q)) {
            showRectangle = !showRectangle; 
            keyLocker.lockKey(Key.Q);
        } else if (Keyboard.isKeyUp(Key.Q)) {
            keyLocker.unlockKey(Key.Q);
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

                if (showRectangle) {
                    // Draw a red rectangle in the middle of the screen
                    int rectWidth = 200;
                    int rectHeight = 100;
                    int x = (screenWidth - rectWidth) / 2;
                    int y = (screenHeight - rectHeight) / 2;
                    graphicsHandler.drawString("Active Quests", 1200, 390, new Font("Arial", Font.BOLD, 20), Color.WHITE);
                    graphicsHandler.drawFilledRectangle(1200, 400, 250, 200, new Color(0, 0, 0, 150));
                    graphicsHandler.drawRectangle(1200, 400, 252, 202, Color.WHITE);
                    graphicsHandler.drawString("Press Q to open/close Quests", 1200, 630, new Font("Arial", Font.PLAIN, 14), Color.WHITE);

                    // Active Quests
                    if (!flagManager.isFlagSet("hasTalkedToWalrus")) {
                        graphicsHandler.drawString("Talk To Seb", 1210, 430, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
                    } else {
                        graphicsHandler.drawString("Talk To Seb", 1210, 430, new Font("Montserrat", Font.PLAIN, 18), Color.GRAY);
                    }
                
                    if (!flagManager.isFlagSet("WalrusMobDefeated")) {
                        graphicsHandler.drawString("Defeat 5 Mobs", 1210, 460, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
                    } else {
                        graphicsHandler.drawString("Defeat 5 Mobs", 1210, 460, new Font("Montserrat", Font.PLAIN, 18), Color.GRAY);
                    }
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


        graphicsHandler.drawString("HEALTH", 70, 45, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(70, 50, currentHealthWidth, healthBarHeight, Color.RED);
        graphicsHandler.drawRectangle(70, 50, healthBarWidth, healthBarHeight, Color.BLACK);


        graphicsHandler.drawString("STAMINA", 70, 85, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(70, 90, player.getStamina(), 14, Color.ORANGE);
        graphicsHandler.drawRectangle(70, 90, healthBarWidth, 14, Color.BLACK);


        graphicsHandler.drawString("EXP", 70, 125, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(70, 130, currentExpWidth, expBarHeight, Color.BLUE);
        graphicsHandler.drawRectangle(70, 130, healthBarWidth, expBarHeight, Color.BLACK);


        // Posiitoned as close as possible to the cetner of screen to show player the level
        graphicsHandler.drawString(
                "LEVEL: " + player.getLevel(),
                720, 45, new Font("Montserrat", Font.BOLD, 18), Color.YELLOW
        );


        graphicsHandler.drawString(
                "Coins: " + player.getCoins(), 
                screenWidth + 530, 45, new Font("Montserrat", Font.BOLD, 18),
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
        graphicsHandler.drawFilledRectangle(40, 300, buttonWidth, buttonHeight, Color.RED);


        graphicsHandler.drawRectangle(40, 300, buttonWidth, buttonHeight, Color.BLACK);

        graphicsHandler.drawString("Inventory", 43, 320, new Font("Montserrat", Font.PLAIN, 12), Color.WHITE);
        
        //Quest Button
        Font questFont = new Font("Montserrat", Font.BOLD, 12);

        graphicsHandler.drawFilledRectangle(1400, 300, buttonWidth, buttonHeight, Color.BLUE); // Draw rectangle
        graphicsHandler.drawRectangle(1400, 300, buttonWidth, buttonHeight, Color.BLACK); // Draw border
        
        // Approximate center: Adjust offsets as necessary
        int textX = 1393 + buttonWidth / 4; 
        int textY = 300 + buttonHeight / 2 + 4; 
        graphicsHandler.drawString("Quest", textX, textY, questFont, Color.WHITE); // Draw text
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



