package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.FlagManager;
import Level.Map;
import Level.MapTile;
import Level.Player;
import Maps.TestMap2;
import Maps.TitleScreenMap;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Direction;
import Utils.Point;
import Game.GameState;
import java.awt.Color;
import java.awt.Font;

public class NewWorldScreen extends Screen {
    protected Player player;
    private ScreenCoordinator screenCoordinator;
    protected FlagManager flagManager;
    protected KeyLocker keyLocker = new KeyLocker();
    protected NewWorldScreenState NewWorldScreenState;
    protected Map background;
    protected Map map;
    protected MapTile portal;
    protected boolean isInventoryShowing;
    protected InventoryScreen inventoryScreen;
    //protected WinScreen winScreen;

    private final int screenWidth = 800;
    private final int screenHeight = 600;
    private final int healthBarWidth = 200;
    private final int healthBarHeight = 20;
    private final int expBarHeight = 14; 

    protected SpriteFont topText;


    private enum NewWorldScreenState {
        RUNNING, LEVEL_COMPLETED, GAME_OVER
    }
    
    
    public NewWorldScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.inventoryScreen = new InventoryScreen(screenCoordinator);
        this.isInventoryShowing = false;
        initialize();
    }

    // Initializes the Map screen
    @Override
    public void initialize() {
        flagManager = new FlagManager();
        map = new TestMap2();
        background = new TitleScreenMap();

        // screenCoordinator.OverridePersistState(GameState.NEWWORLD, this);

        player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);
        player.setFacingDirection(Direction.RIGHT);

        topText = new SpriteFont("Level 2", 0, 0, "Arial", 0, Color.WHITE);
        topText.setFontStyle(Font.BOLD);

        portal = map.getMapTile(1, 23);

        NewWorldScreenState = NewWorldScreenState.RUNNING;

        map.setPlayer(player);
        map.getTextbox().setInteractKey(player.getInteractKey());

        // winScreen = new WinScreen(this);
        // inventoryScreen.setPlayer((Cat) player);

        keyLocker.lockKey(Key.M); // Lock the 'M' key initially
        keyLocker.lockKey(Key.ENTER);



        // for position for the portal position x= 1 aqnd y = 23

    }

    @Override
    public void update() {
        
        player.update();
        map.update(player);

        Point portalLoc = portal.getLocation();
        Point playerLoc = player.getLocation();
        
        double distance = Math.sqrt(Math.pow(portalLoc.x - playerLoc.x, 2) + Math.pow(portalLoc.y - playerLoc.y, 2));
        if (distance < 75) {
            if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
                screenCoordinator.setGameState(GameState.LEVEL);
                
                keyLocker.lockKey(Key.ENTER);
            } else if (Keyboard.isKeyUp(Key.ENTER)) {
                keyLocker.unlockKey(Key.ENTER);
            }
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
    }

    

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
       
        // map.draw(graphicsHandler);
        // player.draw(graphicsHandler);
        // topText.draw(graphicsHandler);
        // drawHUD(graphicsHandler);
            switch (NewWorldScreenState) {
                case RUNNING:
                    map.draw(player, graphicsHandler);
                    drawHUD(graphicsHandler);
    
                    if (isInventoryShowing) {
                        inventoryScreen.draw(graphicsHandler);
                    }
                    break;
    
                // case LEVEL_COMPLETED:
                //     winScreen.draw(graphicsHandler);
                //     break;
    
                case GAME_OVER:
                    graphicsHandler.drawString(
                        "Game Over",
                        screenWidth / 2 - 50,
                        screenHeight / 2,
                        new Font("Montserrat", Font.BOLD, 24),
                        Color.RED
                    );
                    break;
                default:
                    break;
            }
        }

    

    private void drawHUD(GraphicsHandler graphicsHandler) {
        int currentHealthWidth = (int) ((player.getHealth() / (double) player.getMaxHealth()) * healthBarWidth);
        int currentExpWidth = (int) ((player.getExperience() / (double) player.getExpToLevelUp()) * healthBarWidth);

        // Draw health bar
        graphicsHandler.drawString("HEALTH", 20, 15, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 20, currentHealthWidth, healthBarHeight, Color.RED);
        graphicsHandler.drawRectangle(20, 20, healthBarWidth, healthBarHeight, Color.BLACK);

        // Draw stamina bar
        graphicsHandler.drawString("STAMINA", 20, 45, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 50, player.getStamina(), 14, Color.ORANGE);
        graphicsHandler.drawRectangle(20, 50, healthBarWidth, 14, Color.BLACK);

        // Draw EXP bar
        graphicsHandler.drawString("EXP", 20, 75, new Font("Montserrat", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 80, currentExpWidth, expBarHeight, Color.BLUE);
        graphicsHandler.drawRectangle(20, 80, healthBarWidth, expBarHeight, Color.BLACK);

        // Draw player level
        graphicsHandler.drawString(
            "LEVEL: " + player.getLevel(),
            20, 110, new Font("Montserrat", Font.BOLD, 18), Color.YELLOW
        );

        // Draw currency display
        graphicsHandler.drawString(
            "Coins: " + player.getCoins(),
            screenWidth - 120, 20, new Font("Montserrat", Font.BOLD, 18), Color.YELLOW
        );

        // Active Quest Section
        // graphicsHandler.drawString("ACTIVE QUEST:", screenWidth - 180, 60, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);

        // if (!flagManager.isFlagSet("hasTalkedToWalrus")) {
        //     graphicsHandler.drawString("Talk To Seb", screenWidth - 170, 90, new Font("Montserrat", Font.BOLD, 18), Color.WHITE);
        // }

        // Inventory Button
        int buttonWidth = 60;
        int buttonHeight = 30;
        graphicsHandler.drawFilledRectangle(
            10,
            screenHeight / 2 - buttonHeight / 2,
            buttonWidth,
            buttonHeight,
            Color.GREEN
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



}
