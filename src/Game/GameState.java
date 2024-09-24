package Game;

/*
 * This is used by the ScreenCoordinator class to determine which "state" the game is currently in
 */
public enum GameState {
    MENU, 
    PLAYING,
    PAUSED,
    LEVEL, 
    GAME_OVER,
    CREDITS
}
