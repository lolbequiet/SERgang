package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Level.Player;
import Maps.TestMap;

public class TestMapScreen extends Screen {
    private final TestMap testMap;
    private final Player player;

    // Constructor that accepts a player object
    public TestMapScreen(Player player) {
        this.player = player;
        this.testMap = new TestMap();  // Initialize the TestMap here
    }

    @Override
    public void initialize() {
        System.out.println("Initializing TestMapScreen with Player.");
    }

    @Override
    public void update() {
        testMap.update(player);  // Ensure update method handles the player
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        testMap.draw(graphicsHandler);  // Delegate drawing to TestMap
    }
}
