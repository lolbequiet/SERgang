package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Level.TileType;
import Utils.Point;
import java.util.List;

public class Sword extends EnhancedMapTile {
    private boolean isPickedUp = false;

    public Sword(Point location) {
        // Load the sword sprite (24x24), set tile type to passable once picked up
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Sword.png"), 24, 24), TileType.PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);

        // Check if player is close enough to interact and presses E (interact key)
        if (player.isCloseTo(this.getIntersectRectangle(), getWidth() * 1.5) && player.isInteracting() && !isPickedUp) {
            isPickedUp = true; // Mark the sword as picked up
            this.setMapEntityStatus(MapEntityStatus.REMOVED); // Remove the sword from the map
            player.addToInventory("Sword"); // Add the sword to the player's inventory
            System.out.println("Picked up the Sword!");
        }
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
                .withScale(3) // Scale the sprite by 3x (24px -> 72px)
                .build();
        return new GameObject(x, y, frame);
    }
}
