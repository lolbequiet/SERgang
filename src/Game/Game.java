package Game;

import Engine.GameWindow;
import Engine.ScreenManager;
import Game.AudioManager;
import Maps.*;
import Screens.ShopScreen;

/*
 * The game starts here
 * This class just starts up a GameWindow and attaches the ScreenCoordinator to the ScreenManager instance in the GameWindow
 * From this point on the ScreenCoordinator class will dictate what the game does
 */

public class Game {

    public static void main(String[] args) {
      
        Game game = new Game();

 
        AudioManager.playLoop("Resources/Audio/background_lowered_more.wav");
    }

    public Game() {
        System.out.println("Game is starting...");

        GameWindow gameWindow = new GameWindow();
        ScreenManager screenManager = gameWindow.getScreenManager();

        //TestMap testMap = new TestMap();

        //ScreenCoordinator screenCoordinator = new ScreenCoordinator();

        //ShopScreen shopScreen = new ShopScreen(screenCoordinator, testMap);

        screenManager.SetScreenCoordinator(new ScreenCoordinator());
        screenManager.setCurrentScreen(ScreenManager.getScreenCoordinator());
        gameWindow.startGame();
    }
}
