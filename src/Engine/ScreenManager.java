package Engine;

import Game.ScreenCoordinator;
import GameObject.Rectangle;

public class ScreenManager {
    private static ScreenManager instance;
    private Screen currentScreen;
    private static Rectangle screenBounds = new Rectangle(0, 0, 0, 0);
    private static ScreenCoordinator screenCoordinator;

    public void SetScreenCoordinator(ScreenCoordinator sc) {
        screenCoordinator = sc;
    }
    
    public static ScreenCoordinator getScreenCoordinator() {
        return screenCoordinator;
    }

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(Rectangle screenBounds) {
        ScreenManager.screenBounds = screenBounds;
        setCurrentScreen(new DefaultScreen());
    }

    public void setCurrentScreen(Screen screen) {
        screen.initialize();
        this.currentScreen = screen;
    }

    public void update() {
        currentScreen.update();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        currentScreen.draw(graphicsHandler);
    }

    public static int getScreenWidth() {
        return screenBounds.getWidth();
    }

    public static int getScreenHeight() {
        return screenBounds.getHeight();
    }

    public static Rectangle getScreenBounds() {
        return screenBounds;
    }
}
