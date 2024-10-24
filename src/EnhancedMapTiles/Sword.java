package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Players.Cat;
import Utils.Point;
import java.util.List;

import Level.TileType;

public class Sword extends EnhancedMapTile {
    private boolean isPickedUp = false;

    public Sword(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Sword.png"), 24, 24), TileType.PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);

        if (player instanceof Cat catPlayer &&
            catPlayer.isInteracting() &&
            !isPickedUp &&
            player.isCloseTo(this.getIntersectRectangle(), getWidth() * 1.5)) {

            isPickedUp = true;
            this.setMapEntityStatus(MapEntityStatus.REMOVED);
            catPlayer.pickUpSword();  // Update player's inventory
            System.out.println("Picked up the Sword!");
        }
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
                .withScale(3)
                .build();
        return new GameObject(x, y, frame);
    }
}
