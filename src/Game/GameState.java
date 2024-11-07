package Game;

/*
 * This is used by the ScreenCoordinator class to determine which "state" the game is currently in
 */
public enum GameState {
    MENU, 
    PLAYING,
    PAUSED,
    LEVEL, 
    LEVEL_SELECT,
    GAME_OVER,
    CREDITS,
    INVENTORY,
    SHOP
}
