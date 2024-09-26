package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.TestMap;
import Players.Cat;
import Utils.Direction;
import java.awt.Color;
import java.awt.Font;

public class PlayLevelScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;

    private final int screenWidth = 800;  // Hardcoded screen width
    private final int screenHeight = 600; // Hardcoded screen height

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        flagManager = new FlagManager();
        flagManager.addFlag("hasLostBall", false);
        flagManager.addFlag("hasTalkedToWalrus", false);
        flagManager.addFlag("hasTalkedToDinosaur", false);
        flagManager.addFlag("hasFoundBall", false);

        map = new TestMap();
        map.setFlagManager(flagManager);

        player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.setFacingDirection(Direction.LEFT);

        map.setPlayer(player);
        map.getTextbox().setInteractKey(player.getInteractKey());
        map.preloadScripts();
        winScreen = new WinScreen(this);
    }

    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);
                break;

            case LEVEL_COMPLETED:
                winScreen.update();
                break;
        }

        if (map.getFlagManager().isFlagSet("hasFoundBall")) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                drawHUD(graphicsHandler);
                break;

            case LEVEL_COMPLETED:
                winScreen.draw(graphicsHandler);
                break;
        }
    }

    private void drawHUD(GraphicsHandler graphicsHandler) {
        int barWidth = 200;
        int barHeight = 20;
        int staminaBarHeight = 14;

        // Draw "HEALTH" label above the health bar
        graphicsHandler.drawString("HEALTH", 20, 15, new Font("Arial", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 20, barWidth, barHeight, Color.RED);
        graphicsHandler.drawRectangle(20, 20, barWidth, barHeight, Color.BLACK);

        // Draw "STAMINA" label above the stamina bar
        graphicsHandler.drawString("STAMINA", 20, 45, new Font("Arial", Font.BOLD, 14), Color.WHITE);
        graphicsHandler.drawFilledRectangle(20, 50, barWidth, staminaBarHeight, Color.ORANGE);
        graphicsHandler.drawRectangle(20, 50, barWidth, staminaBarHeight, Color.BLACK);

        graphicsHandler.drawString("ACTIVE QUEST:", screenWidth - 180, 30, new Font("Arial", Font.BOLD, 18), Color.WHITE);

        int buttonWidth = 60;
        int buttonHeight = 30;

        // Draw a single Inventory button on the left side of the screen
        graphicsHandler.drawFilledRectangle(10, screenHeight / 2 - buttonHeight / 2, buttonWidth, buttonHeight, Color.GRAY);
        graphicsHandler.drawRectangle(10, screenHeight / 2 - buttonHeight / 2, buttonWidth, buttonHeight, Color.BLACK);
        graphicsHandler.drawString("Inventory", 12, screenHeight / 2, new Font("Arial", Font.PLAIN, 12), Color.WHITE);
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED
    }
}
