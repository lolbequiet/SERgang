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

        if (Keyboard.isKeyDown(Key.I) && !keyLocker.isKeyLocked(Key.I)) {
            isInventoryShowing = !isInventoryShowing;
            keyLocker.lockKey(Key.I);
        } else if (Keyboard.isKeyUp(Key.I)) {
            keyLocker.unlockKey(Key.I);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        switch (OverWorldScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                drawHUD(graphicsHandler);

                if (isInventoryShowing) {
                    inventoryScreen.draw(graphicsHandler);
                }
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
            screenWidth - 120, 20, new Font("Montserrat", Font.BOLD, 18), Color.YELLOW
        );
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

    private void restorePlayerData() {
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
        }
    }
}
