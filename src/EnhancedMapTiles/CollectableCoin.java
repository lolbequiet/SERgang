package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Level.PlayerState;
import Level.TileType;
import Scripts.TestMap.CoinsCollectedScript;
import Utils.Direction;
import Utils.Point;

import Level.EnhancedMapTile;

public class CollectableCoin extends EnhancedMapTile {

    private int amount;

    public CollectableCoin(Point location, int amount) {

        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Coin.png"), 16, 16), TileType.NOT_PASSABLE);

        this.amount = amount;

    }

    @Override
    public void update(Player player){
        super.update(player);

        if (player.isCloseTo(this.getIntersectRectangle(), getWidth() * 1.5) && player.isInteracting()){

            collectCoin(player);
            
            }
        }

        private void collectCoin(Player player){
            player.addCoins(amount);

            this.setMapEntityStatus(MapEntityStatus.REMOVED);

            System.out.println("test for actual collecting of the coin");


        }

       

        @Override
        protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
            Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
                .withScale(3)
                .build();
        return new GameObject(x, y, frame);
    
    }
    
}
