package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.*;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Direction;
import java.awt.Color;
import Maps.*;
import java.awt.KeyboardFocusManager;

import Engine.KeyLocker;
import Engine.Keyboard;

public class ShopScreen extends Screen {

    protected FlagManager flagManager;
    protected ShopMap background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Player player;
    protected ScreenCoordinator screenCoordinator;
    protected SpriteFont healthPotion, mace, baconeggNcheese;
    protected int currentShopItem = 0;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected TestMap TestMap;




    public enum NewShopScreenState {
        RUNNING, LEVEL_COMPLETED, GAME_OVER, SHOP
    }

    protected SpriteFont topText;

    protected SpriteFont testText;

    public ShopScreen(ScreenCoordinator screenCoordinator, Player player, TestMap TestMap) {
        this.screenCoordinator = screenCoordinator;
        this.player = player;
        this.TestMap = TestMap;

        initialize();
    }

    @Override
    public void initialize() {
        // screenCoordinator.setGameState(GameState.SHOP);
        background = new ShopMap();
        background.setAdjustCamera(false);
        topText = new SpriteFont("MIKE'S DELI", 50, 50, "Arial", 30, new Color(49, 207, 240));
        topText.setOutlineColor(Color.black);
        topText.setOutlineThickness(3);

        healthPotion = new SpriteFont("Potion - 5 doubloons", 30, 150, "Arial",24, new Color (49, 207, 240));
        healthPotion.setOutlineColor(Color.black);
        healthPotion.setOutlineThickness(2);
       // testText = new SpriteFont("just a test for designing i guess", 50, 100, "Monsterrat", 30, new Color(0,255,0));
        mace = new SpriteFont("Mace - 10 doubloons", 30, 200, "Arial",24, new Color (49, 207, 240));
        mace.setOutlineColor(Color.black);
        mace.setOutlineThickness(2);

        baconeggNcheese = new SpriteFont("baconeggNcheese - 15 doubloons", 30, 250, "Arial",24, new Color (49, 207, 240));
        baconeggNcheese.setOutlineColor(Color.black);
        baconeggNcheese.setOutlineThickness(2);

        keyPressTimer = 0;
        keyLocker.lockKey(Key.B);
    }

    @Override
    public void update() {


        // if down or up is pressed, change menu item "hovered" over
        if (Keyboard.isKeyDown(Key.DOWN) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentShopItem++;
        } else if (Keyboard.isKeyDown(Key.UP) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentShopItem--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // Loop through menu options
        if (currentShopItem > 2) {
            currentShopItem = 0;
        } else if (currentShopItem < 0) {
            currentShopItem = 2;
        }

        if (currentShopItem == 0) {
            healthPotion.setColor(Color.yellow);
            mace.setColor(Color.white);
            baconeggNcheese.setColor(Color.white);
            pointerLocationX = 70;
            pointerLocationY = 50;
        } else if (currentShopItem == 1) {
            healthPotion.setColor(Color.white);
            mace.setColor(Color.yellow);
            baconeggNcheese.setColor(Color.white);
            pointerLocationX = 70;
            pointerLocationY = 150;
        } else if (currentShopItem == 2) {
            healthPotion.setColor(Color.white);
            mace.setColor(Color.white);
            baconeggNcheese.setColor(Color.yellow);
            pointerLocationX = 70;
            pointerLocationY = 200;
        }

        if (Keyboard.isKeyDown(Key.ENTER)) {
            
            if (currentShopItem == 0) {
                if (TestMap.cashinOut(5)) {
                    System.out.println("shoutout my mom fr");
                } else {
                    System.out.println("BROKE BOI BOI BOI");
                }
            } else if (currentShopItem == 1) {
                if (TestMap.cashinOut(10)) {
                    System.out.println("shoutout my mom fr");
                } else {
                    System.out.println("BROKE BOI BOI BOI");
                }
            } else if (currentShopItem == 2)  {
                if (TestMap.cashinOut(15)) {
                    System.out.println("shoutout my mom fr");
                } else {
                    System.out.println("BROKE BOI BOI BOI");
                }

            }
            
        }


        

        if (Keyboard.isKeyDown(Key.B) && !keyLocker.isKeyLocked(Key.B)) {
            screenCoordinator.BackToPersist();
            keyLocker.lockKey(Key.B);
        }

        if (Keyboard.isKeyUp(Key.B)) {
            keyLocker.unlockKey(Key.B);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, new Color(10, 10, 10, 150));
        background.draw(graphicsHandler);
        topText.draw(graphicsHandler);
        healthPotion.draw(graphicsHandler);
        mace.draw(graphicsHandler);
        baconeggNcheese.draw(graphicsHandler);
      //testText.draw(graphicsHandler);
    }
}
