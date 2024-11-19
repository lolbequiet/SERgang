package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.*;
import Maps.*;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Direction;
import java.awt.Color;
import Engine.KeyLocker;


public class ShopScreen extends Screen{

    protected PlayLevelScreen playLevelScreen;
    protected ScreenCoordinator screenCoordinator;
    protected FlagManager flagManager;
    protected Sprite background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Player player;
    protected ShopMap maps;
    protected Map map;
    protected NewShopScreenState NewShopScreenState;

    public enum NewShopScreenState {
        RUNNING, LEVEL_COMPLETED, GAME_OVER, SHOP
    }

    public ShopScreen(PlayLevelScreen playLevelScreen, Player player){
        this.playLevelScreen = playLevelScreen;
        this.player = player;

    }
   
    
    public ShopScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        initialize();
    }


    @Override
    public void initialize() {
        flagManager = new FlagManager();
        map = new TestMap();
        map.setFlagManager(flagManager);

        player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);

        map.setPlayer(player);
        map.getTextbox().setInteractKey(player.getInteractKey());

        
        NewShopScreenState = NewShopScreenState.SHOP;
    }

    @Override
    public void update() {
        player.update();
        map.update(player);

        case RUNNING:

        case LEVEL_COMPLETED:

        case SHOP:

        case GAME_OVER:


    } 
    

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        map.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, new Color(10, 10, 10, 150));
        
        
    }

    


    
}

