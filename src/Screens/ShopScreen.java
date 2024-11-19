package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.*;
import Maps.ShopMap;
import Maps.TitleScreenMap;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Direction;
import java.awt.Color;
import java.awt.KeyboardFocusManager;

import Engine.KeyLocker;
import Engine.Keyboard;
import Level.ScriptState;

public class ShopScreen extends Screen {

    protected FlagManager flagManager;

    protected ShopMap background;

    protected KeyLocker keyLocker = new KeyLocker();

    protected Player player;
    protected ScreenCoordinator screenCoordinator;

    protected ShopMap map;

    protected SpriteFont topText;

    public ShopScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        initialize();
    }

    @Override
    public void initialize() {
        // screenCoordinator.setGameState(GameState.SHOP);
        background = new ShopMap();
        background.setAdjustCamera(false);
        topText = new SpriteFont("MIKE'S SHOP", 50, 50, "Arial", 30, new Color(49, 207, 240));
        topText.setOutlineColor(Color.black);
        topText.setOutlineThickness(3);

        keyLocker.lockKey(Key.B);
    }

    @Override
    public void update() {
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
        graphicsHandler.drawFilledRectangle(0, 0, 1500, 800, new Color(10, 10, 10, 150));
        background.draw(graphicsHandler);
        topText.draw(graphicsHandler);
    }
}
