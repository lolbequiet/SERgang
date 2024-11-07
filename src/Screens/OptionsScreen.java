package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Engine.Key;
import Engine.Keyboard;
import Engine.KeyLocker;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import java.awt.*;

public class OptionsScreen extends Screen {
    private ScreenCoordinator screenCoordinator;
    private SpriteFont optionsMessage;
    private KeyLocker keyLocker = new KeyLocker();

    public OptionsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Message displayed on the options screen
        optionsMessage = new SpriteFont("Customizable options coming soon!", 100, 250, "Arial", 30, Color.white);
        optionsMessage.setOutlineColor(Color.black);
        optionsMessage.setOutlineThickness(3);
        keyLocker.lockKey(Key.SPACE); // Lock key initially
    }

    public void update() {
        // Return to the menu when space is pressed
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        optionsMessage.draw(graphicsHandler);
    }
}
