package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Game.SharedPlayerData;
import Level.FlagManager;
import Level.Map;
import Level.MapTile;
import Level.Player;
import Maps.TestMap3;
import Maps.TitleScreenMap;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Direction;
import Utils.Point;
import java.awt.Color;
import java.awt.Font;

public class OverWorldScreen extends Screen {
    protected Player player;
    private ScreenCoordinator screenCoordinator;
    protected FlagManager flagManager;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Map background;
    protected Map map;
    protected MapTile nextPortal;
    protected MapTile backPortal;
    protected boolean isInventoryShowing;
    protected InventoryScreen inventoryScreen;
    protected boolean showRectangle; // Toggle flag for rectangle
    protected OverWorldScreenState OverWorldScreenState;

    private final int screenWidth = 800;
    private final int screenHeight = 600;
    private final int healthBarWidth = 200;
    private final int healthBarHeight = 20;
    private final int expBarHeight = 14;

    protected SpriteFont topText;

    private enum OverWorldScreenState {
        RUNNING, LEVEL_COMPLETED, GAME_OVER
    }

    public OverWorldScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.inventoryScreen = new InventoryScreen(screenCoordinator);
        this.isInventoryShowing = false;
        this.showRectangle = false; // Rectangle is hidden initially
        initialize();
    }

    @Override
    public void initialize() {
        flagManager = new FlagManager();
        map = new TestMap3();
        background = new TitleScreenMap();

        // Restore player data
        restorePlayerData();

        topText = new SpriteFont("Level 3", 0, 0, "Arial", 0, Color.WHITE);
        topText.setFontStyle(Font.BOLD);

        nextPortal = map.getMapTile(1, 23);
        backPortal = map.getMapTile(
            Math.round(map.getPlayerStartPosition().x / map.getTileset().getScaledSpriteWidth()),
            Math.round(map.getPlayerStartPosition().y / map.getTileset().getScaledSpriteHeight())
        );

        OverWorldScreenState = OverWorldScreenState.RUNNING;

        map.setPlayer(player);
        map.getTextbox().setInteractKey(player.getInteractKey());

        keyLocker.lockKey(Key.M);
        keyLocker.lockKey(Key.ENTER);
    }

    @Override
    public void update() {
        player.update();
        map.update(player);

        // Save player data to SharedPlayerData
        savePlayerData();

        Point portalLocN = nextPortal.getLocation();
        Point portalLocB = backPortal.getLocation();
        Point playerLoc = player.getLocation();

        double distanceN = Math.sqrt(Math.pow(portalLocN.x - playerLoc.x, 2) + Math.pow(portalLocN.y - playerLoc.y, 2));
        if (distanceN < 75) {
            if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
                savePlayerData();
                screenCoordinator.setGameState(GameState.NEWWORLD);
                keyLocker.lockKey(Key.ENTER);
            } else if (Keyboard.isKeyUp(Key.ENTER)) {
                keyLocker.unlockKey(Key.ENTER);
            }
        }

        double distanceB = Math.sqrt(Math.pow(portalLocB.x - playerLoc.x, 2) + Math.pow(portalLocB.y - playerLoc.y, 2));
        if (distanceB < 75) {
            if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
                savePlayerData();
                screenCoordinator.setGameState(GameState.LEVEL);
                keyLocker.lockKey(Key.ENTER);
            } else if (Keyboard.isKeyUp(Key.ENTER)) {
                keyLocker.unlockKey(Key.ENTER);
            }
        }
        if (Keyboard.isKeyDown(Key.Q) && !keyLocker.isKeyLocked(Key.Q)) {
            showRectangle = !showRectangle; 
            keyLocker.lockKey(Key.Q);
        } else if (Keyboard.isKeyUp(Key.Q)) {
            keyLocker.unlockKey(Key.Q);
        }
        if (Keyboard.isKeyDown(Key.I) && !keyLocker.isKeyLocked(Key.I)) {
            isInventoryShowing = !isInventoryShowing;
            keyLocker.lockKey(Key.I);
        } else if (Keyboard.isKeyUp(Key.I)) {
            keyLocker.unlockKey(Key.I);
        }
    }



    public void draw(GraphicsHandler graphicsHandler) {
        switch (OverWorldScreenState) {
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
                    graphicsHandler.drawString("Press Q to open/close Quests", 1200, 620, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
                    
                    // Active Quests

                    if (!flagManager.isFlagSet("hasTalkedToWalrus")) {
                        graphicsHandler.drawString("Escape Evil Clone", 1210, 430, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
                    } else {
                        graphicsHandler.drawString("Escape Evil Clone", 1210, 430, new Font("Montserrat", Font.PLAIN, 18), Color.GRAY);
                    }


                    }
                
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


        //graphicsHandler.drawString("ACTIVE QUESTS:", screenWidth - 180, 60, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);


        //if (!flagManager.isFlagSet("hasTalkedToWalrus")) {
        //    graphicsHandler.drawString("Talk To Seb", screenWidth - 167, 125, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        //}


        //if (!flagManager.isFlagSet("WalrusMobDefeated")) {
        //    graphicsHandler.drawString("Defeat 5 Mobs", screenWidth - 170, 90, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        //}


        //if (!flagManager.isFlagSet("ReturnBackToEarth")) {
        //    graphicsHandler.drawString("Return Back to Earth", screenWidth - 200, 150, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        //}


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


    private void savePlayerData() {
        SharedPlayerData data = SharedPlayerData.getInstance();
        if (player != null) {
            data.setHealth(player.getHealth());
            data.setExperience(player.getExperience());
            data.setStamina(player.getStamina());
            data.setInventory(player.getInventory());
            data.setCoins(player.getCoins()); // Save coins
            data.setHasSword(((Cat) player).hasSword()); // Save sword status
            
        }
    }

    public void restorePlayerData() {
        SharedPlayerData data = SharedPlayerData.getInstance();
        player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);

        if (data != null) {
            player.setHealth(data.getHealth());
            player.setExperience(data.getExperience());
            player.setStamina(data.getStamina());
            player.getInventory().clear();
            player.getInventory().addAll(data.getInventory());
            player.addCoins(data.getCoins()); // Restore coins
            if (data.hasSword()) {
                ((Cat) player).pickUpSword(); // Restore sword
            }
            if (Cat.isBossModeSelected()) {
                ((Cat) player).switchToBossSprite();
            } else {
                ((Cat) player).switchToCatSprite();
            }
        }
    }
}
