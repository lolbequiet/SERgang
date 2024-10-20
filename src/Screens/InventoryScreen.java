package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import java.awt.Color;
import java.awt.Font;

public class InventoryScreen extends Screen {

    protected KeyLocker keyLocker = new KeyLocker();
    private ScreenCoordinator screenCoordinator;

    public InventoryScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize(){
        screenCoordinator.setGameState(GameState.PLAYING);
        keyLocker.lockKey(Key.I);
    }

    public void update(){

        if (Keyboard.isKeyDown(Key.I) && !keyLocker.isKeyLocked(Key.I)) {
            screenCoordinator.setGameState(GameState.PLAYING);
			keyLocker.lockKey(Key.I);
		}

		if (Keyboard.isKeyUp(Key.I)) {
			keyLocker.unlockKey(Key.I);
		}

        // if (Keyboard.isKeyUp(Key.I)) {
        //     keyLocker.unlockKey(Key.I);
        // }

        // // for closing inventory
        // if (Keyboard.isKeyDown(Key.I) && !keyLocker.isKeyLocked(Key.I)) {
        //     keyLocker.lockKey(Key.I);
        //     screenCoordinator.setGameState(GameState.PLAYING);
        // }
    }


    @Override
    public void draw(GraphicsHandler graphicsHandler) {

        //similar to pause menu screen for now

        graphicsHandler.drawFilledRectangle(50, 50, 180, 300, new Color(0, 0, 0, 150));

        graphicsHandler.drawRectangle(50, 50, 180, 100, Color.WHITE);

        graphicsHandler.drawRectangle(50, 150, 180, 100, Color.WHITE);

        graphicsHandler.drawRectangle(49, 49, 182, 302, Color.WHITE);


        graphicsHandler.drawString("Inventory", 60, 40, new Font("Arial", Font.BOLD, 20), Color.WHITE);

        //code for the potential items in the hotbar, these will be placeholders for now

        graphicsHandler.drawString("1", 80, 100, new Font("Arial", Font.PLAIN, 12), Color.WHITE);

        graphicsHandler.drawString("2", 80, 200, new Font("Arial", Font.PLAIN, 12), Color.WHITE);

        graphicsHandler.drawString("3", 80, 300, new Font("Arial", Font.PLAIN, 12), Color.WHITE);


        graphicsHandler.drawString("Press I to open/close inventory", 55, 370, new Font("Arial", Font.PLAIN, 14), Color.WHITE);
    }


}
