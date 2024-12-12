package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel;
    protected SpriteFont modifiedby, _jj, _fahim, _mike, _seb;
    protected SpriteFont returnInstructionsLabel;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }
    

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);

        //original
        creditsLabel = new SpriteFont("Credits", 15, 7, "Times New Roman", 30, Color.white);
        createdByLabel = new SpriteFont("Original by Alex Thimineur", 130, 121, "Times New Roman", 20, Color.white);
        createdByLabel.setOutlineColor(Color.BLUE);
        createdByLabel.setOutlineThickness(5);

        //modified/reworked
        modifiedby = new SpriteFont("Modified/Changes by:", 490, 121,"Times New Roman", 20, Color.white);
        modifiedby.setOutlineColor(Color.red.darker());
        modifiedby.setOutlineThickness(5);

        _jj = new SpriteFont("Jacorian Adom (JJ)", 490, 170, "Times New Roman",20, Color.white);
        _jj.setOutlineColor(Color.red.darker());
        _jj.setOutlineThickness(5);

        _mike = new SpriteFont("Michael Alvarado (Mike)", 490, 220, "Times New Roman",20, Color.white);
        _mike.setOutlineColor(Color.red.darker());
        _mike.setOutlineThickness(5);

        _seb = new SpriteFont("Sebastian Salazar (Seb)", 490, 270, "Times New Roman",20, Color.white);
        _seb.setOutlineColor(Color.red.darker());
        _seb.setOutlineThickness(5);

        _fahim = new SpriteFont("Fahim Kalange", 490, 320, "Times New Roman",20, Color.white);
        _fahim.setOutlineColor(Color.red.darker());
        _fahim.setOutlineThickness(5);


        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 180, 532, "Times New Roman", 30, Color.white);
        returnInstructionsLabel.setOutlineColor(Color.WHITE);
        returnInstructionsLabel.setOutlineThickness(2);
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        creditsLabel.draw(graphicsHandler);
        createdByLabel.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);

        //group credits
        modifiedby.draw(graphicsHandler);
        _jj.draw(graphicsHandler);
        _mike.draw(graphicsHandler);
        _seb.draw(graphicsHandler);
        _fahim.draw(graphicsHandler);
    }
}
