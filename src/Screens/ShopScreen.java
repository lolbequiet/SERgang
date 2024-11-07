package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.*;
import Maps.ShopMap;
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

    protected ShopMap map;

    public ShopScreen(PlayLevelScreen playLevelScreen, Player player){
        this.playLevelScreen = playLevelScreen;
        this.player = player;
        initialize();

    }
   
    
    @Override
    public void initialize() {
       
    }

    @Override
    public void update() {
        
    } 
    

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, new Color(10, 10, 10, 150));
        
        
    }

    


    
}

