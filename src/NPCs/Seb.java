

    package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;
import java.awt.*;

import java.util.HashMap;

// This class is for the Seb NPC
public class Seb extends NPC {

    public Seb(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("walrus.png"), 24, 24), "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(7, 13, 11, 10)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build()
            });
            put("STAND_RIGHT", new Frame[] {
                   new FrameBuilder(spriteSheet.getSprite(0, 0))
                           .withScale(3)
                           .withBounds(7, 13, 11, 7)
                           .build()
           });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        //boolean for if the player is near, setting it up with method for interaction.

        boolean isPlayerNear = forInteraction();

        //if statement so IF player is near, then this would draw a "press e to interact" statement (spacebar for now)
        //only for seb of course, if this works will be implemented to other NPC's

        if (isPlayerNear) {
        graphicsHandler.drawString("Press 'SpaceBar' to interact", (int) getX() - 150,(int) getY() - 30, new Font("Arial", Font.PLAIN, 24), Color.WHITE);
        }
    }


    //detects if player is near the NPC for interaction
    private boolean forInteraction() {

        return true;

    }
}

    

