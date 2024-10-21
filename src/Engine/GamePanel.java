package Engine;

import GameObject.Rectangle;
import Screens.LevelScreen;
import SpriteFont.SpriteFont;
import Utils.Colors;

import javax.swing.*;
import java.awt.*;

/*
 * This is where the game loop process and render back buffer is set up.
 */
public class GamePanel extends JPanel {
    // Loads Screens on to the JPanel; each screen has its own update and draw methods
    private ScreenManager screenManager;

    // Used to draw graphics to the panel
    private GraphicsHandler graphicsHandler;

    private boolean isGamePaused = false;
    private SpriteFont pauseLabel;
    private KeyLocker keyLocker = new KeyLocker();
    private final Key pauseKey = Key.P;
    private Thread gameLoopProcess;

    private boolean isInventoryShowing = false;
    private final Key inventoryKey = Key.I;

    private Key showFPSKey = Key.G;
    private SpriteFont fpsDisplayLabel;
    private boolean showFPS = false;
    private int currentFPS;
    private boolean doPaint;

    // The JPanel and various important class instances are set up here
    public GamePanel() {
        super();
        this.setDoubleBuffered(true);

        // Attaches Keyboard class's keyListener to this JPanel
        this.addKeyListener(Keyboard.getKeyListener());

        graphicsHandler = new GraphicsHandler();

        // Get the singleton instance of ScreenManager
        screenManager = ScreenManager.getInstance();

        pauseLabel = new SpriteFont("PAUSE", 365, 280, "Arial", 24, Color.white);
        pauseLabel.setOutlineColor(Color.black);
        pauseLabel.setOutlineThickness(2.0f);

        fpsDisplayLabel = new SpriteFont("FPS", 4, 3, "Arial", 12, Color.black);

        currentFPS = Config.TARGET_FPS;

        // This game loop code will run in a separate thread from the rest of the program
        // It will continually update the game's logic and repaint the game's graphics
        GameLoop gameLoop = new GameLoop(this);
        gameLoopProcess = new Thread(gameLoop.getGameLoopProcess());
    }

    // This is called later after instantiation and will initialize ScreenManager
    public void setupGame() {
        setBackground(Colors.CORNFLOWER_BLUE);
        screenManager.initialize(new Rectangle(getX(), getY(), getWidth(), getHeight()));
    }

    // This starts the game loop thread
    public void startGame() {
        gameLoopProcess.start();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public void setCurrentFPS(int currentFPS) {
        this.currentFPS = currentFPS;
    }

    public void setDoPaint(boolean doPaint) {
        this.doPaint = doPaint;
    }

    public void update() {
        updatePauseState();
        updateShowFPSState();
        updateInventoryState();

        if (!isGamePaused) {
            screenManager.update();
        }
    }

    private void updatePauseState() {
        if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
            isGamePaused = !isGamePaused;
            keyLocker.lockKey(pauseKey);
        }

        if (Keyboard.isKeyUp(pauseKey)) {
            keyLocker.unlockKey(pauseKey);
        }
    }

    private void updateInventoryState() {
        // if (Keyboard.isKeyDown(inventoryKey) && !keyLocker.isKeyLocked(inventoryKey)) {
        //     isInventoryShowing = !isInventoryShowing;
        //     keyLocker.lockKey(inventoryKey);
        // }

        // if (Keyboard.isKeyUp(inventoryKey)) {
        //     keyLocker.unlockKey(inventoryKey);
        // }
    }

    private void updateShowFPSState() {
        if (Keyboard.isKeyDown(showFPSKey) && !keyLocker.isKeyLocked(showFPSKey)) {
            showFPS = !showFPS;
            keyLocker.lockKey(showFPSKey);
        }

        if (Keyboard.isKeyUp(showFPSKey)) {
            keyLocker.unlockKey(showFPSKey);
        }

        fpsDisplayLabel.setText("FPS: " + currentFPS);
    }

    public void draw() {
        // Draw current game state
        screenManager.draw(graphicsHandler);

        // If the game is paused, draw pause graphics over Screen graphics
        if (isGamePaused) {
            pauseLabel.draw(graphicsHandler);
            graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
        }

        if (showFPS) {
            fpsDisplayLabel.draw(graphicsHandler);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (doPaint) {
            // Every repaint call will schedule this method to be called
            // When called, it will set up the graphics handler and then call this class's draw method
            graphicsHandler.setGraphics((Graphics2D) g);
            draw();
        }
    }
}
