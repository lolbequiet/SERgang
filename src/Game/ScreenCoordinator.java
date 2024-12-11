package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Screens.CreditsScreen;
import Screens.InventoryScreen;
import Screens.LevelScreen;
import Screens.MenuScreen;
import Screens.PlayLevelScreen;
import Screens.ShopScreen;
import Screens.NewWorldScreen;
import Screens.OverWorldScreen;
import Players.Cat;
import Level.Player;
import Maps.TestMap;

/*
 * Based on the current game state, this class determines which Screen should be shown
 * There can only be one "currentScreen", although screens can have "nested" screens
 */
public class ScreenCoordinator extends Screen {
    // Currently shown Screen
    protected Screen currentScreen = new DefaultScreen();

    // Keep track of gameState so ScreenCoordinator knows which Screen to show
    protected GameState gameState;
    protected Screen previousScreen = null;
    protected GameState persistedGameState;
    protected GameState previousGameState;

    // Player instance

    public GameState getGameState() {
        return gameState;
    }

    // Other Screens can set the gameState of this class to force it to change the currentScreen
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setGameStatePersist(GameState gameState) {
        previousScreen = currentScreen;
        persistedGameState = this.gameState;
        this.gameState = gameState;

    }

    public void BackToPersist() {
        this.gameState = persistedGameState;
    }

    @Override
    public void initialize() {
        // Start game off with Menu Screen
        gameState = GameState.MENU;
    }

    private void savePlayerData(Player player) {

        SharedPlayerData data = SharedPlayerData.getInstance();
        if (player != null) {
            data.setHealth(player.getHealth());
            data.setExperience(player.getExperience());
            data.setStamina(player.getStamina());
            data.setCoins(player.getCoins()); // Save coins
            data.setInventory(player.getInventory());
            data.setHasSword(((Cat) player).hasSword()); // Save sword status
        }
        
    }

    private void restorePlayerData(Player player) {
        SharedPlayerData data = SharedPlayerData.getInstance();
        player = new Cat(0,0);


        if (data != null) {
            player.setHealth(data.getHealth());
            player.setExperience(data.getExperience());
            player.setStamina(data.getStamina());
            player.getInventory().clear();
            player.getInventory().addAll(data.getInventory());
            player.addCoins(data.getCoins()); // Restore coins
            if (data.hasSword()) {
                ((Cat) player).pickUpSword(); // Restore sword
            }
        }
    }

    @Override
    public void update() {
        do {
            // If previousGameState does not equal gameState, it means there was a change in gameState
            // This triggers ScreenCoordinator to bring up a new Screen based on what the gameState is
            if (previousGameState != gameState) {

                if (previousScreen != null && persistedGameState == gameState) {
                    currentScreen = previousScreen;
                    currentScreen.restorePlayerData();

                    previousScreen = null;
                } else {
                    switch (gameState) {
                        case MENU:
                            currentScreen = new MenuScreen(this);
                            break;
                        case LEVEL:
                            currentScreen = new PlayLevelScreen(this);
                            break;
                        case LEVEL_SELECT:
                            currentScreen = new LevelScreen(this);
                            break;
                        case CREDITS:
                            currentScreen = new CreditsScreen(this);
                            break;
                        case NEWWORLD:
                            currentScreen = new NewWorldScreen(this);
                            break;
                        case OVERWORLD:
                            currentScreen = new OverWorldScreen(this);
                            break;
                        case SHOP:
                            currentScreen = new ShopScreen(this, (TestMap)PlayLevelScreen.getMap()); // Pass the player and map if required
                            break;
                    }

                    currentScreen.initialize();
                }
            }
            previousGameState = gameState;

            // Call the update method for the currentScreen
            currentScreen.update();
        } while (previousGameState != gameState);
    }

    public void SwapScreen(Screen screenToSwap) {
        previousScreen = screenToSwap;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Call the draw method for the currentScreen
        currentScreen.draw(graphicsHandler);
    }

    @Override
    public void restorePlayerData() {
        // TODO Auto-generated method stub
    }
}
