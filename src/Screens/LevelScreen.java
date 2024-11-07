package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.Color;
import java.awt.Font;

public class LevelScreen extends Screen {
    private ScreenCoordinator screenCoordinator;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Map background;
    

    protected SpriteFont topText;

    public LevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    //Initializes the Map screen
    @Override
    public void initialize() {
        screenCoordinator.setGameState(GameState.LEVEL_SELECT);
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        topText = new SpriteFont("MAP", 50, 50, "Arial", 30, new Color(49, 207, 240));
        topText.setOutlineColor(Color.black);
        topText.setOutlineThickness(3);

        keyLocker.lockKey(Key.M);
    }

    @Override
    public void update() {
        if (Keyboard.isKeyDown(Key.M) && !keyLocker.isKeyLocked(Key.M)) {
            // screenCoordinator.setGameState(GameState.LEVEL);
            screenCoordinator.BackToPersist(); // sets it back to previous save
			keyLocker.lockKey(Key.M);
		}

		if (Keyboard.isKeyUp(Key.M)) {
			keyLocker.unlockKey(Key.M);
		}
       
        }
        
    

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        topText.draw(graphicsHandler);
    }
}
