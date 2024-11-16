package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont title;
    protected SpriteFont playGame;
    protected SpriteFont options;
    protected SpriteFont credits;
    protected Map background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Title centered at the top-middle
        title = new SpriteFont("DREAM QUEST", 180, 80, "Montserrat", 48, Color.white);
        title.setOutlineColor(Color.black);
        title.setOutlineThickness(4);

        // Centered buttons near the bottom
        playGame = new SpriteFont("PLAY GAME", 250, 280, "Montserrat", 30, Color.white);
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);

        options = new SpriteFont("OPTIONS", 250, 350, "Montserrat", 30, Color.white);
        options.setOutlineColor(Color.black);
        options.setOutlineThickness(3);

        credits = new SpriteFont("CREDITS", 250, 420, "Montserrat", 30, Color.white);
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);

        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);

        // if down or up is pressed, change menu item "hovered" over
        if (Keyboard.isKeyDown(Key.DOWN) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered++;
        } else if (Keyboard.isKeyDown(Key.UP) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // Loop through menu options
        if (currentMenuItemHovered > 2) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 2;
        }

        // Highlight selected menu item
        if (currentMenuItemHovered == 0) {
            playGame.setColor(Color.black);
            options.setColor(Color.white);
            credits.setColor(Color.white);
            pointerLocationX = 220;
            pointerLocationY = 290;
        } else if (currentMenuItemHovered == 1) {
            playGame.setColor(Color.white);
            options.setColor(Color.black);
            credits.setColor(Color.white);
            pointerLocationX = 220;
            pointerLocationY = 360;
        } else if (currentMenuItemHovered == 2) {
            playGame.setColor(Color.white);
            options.setColor(Color.white);
            credits.setColor(Color.black);
            pointerLocationX = 220;
            pointerLocationY = 430;
        }

        // Select menu item on SPACE key press
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {
                screenCoordinator.setGameState(GameState.LEVEL);
            } else if (menuItemSelected == 1) {
                // Add logic for options here
                System.out.println("Options menu (not yet implemented)");
            } else if (menuItemSelected == 2) {
                screenCoordinator.setGameState(GameState.CREDITS);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        title.draw(graphicsHandler);
        playGame.draw(graphicsHandler);
        options.draw(graphicsHandler);
        credits.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, Color.white, Color.black, 2);
    }
}
