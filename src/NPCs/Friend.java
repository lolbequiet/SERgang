package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.MapEntityStatus;
import Level.NPC;
import Level.Player;
import Utils.Point;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class Friend extends NPC {
    private static final float FOLLOW_SPEED = 1.5f;
    private static final float SAFE_DISTANCE = 75f;  // Safe distance from the player

    public Friend(Point location) {
        super(1, location.x, location.y, loadFriendAnimations(), "STAND_RIGHT");
    }

    private static HashMap<String, Frame[]> loadFriendAnimations() {
        SpriteSheet spriteSheet = loadFriendSprite();
        HashMap<String, Frame[]> animations = new HashMap<>();

        animations.put("STAND_RIGHT", new Frame[]{
            new FrameBuilder(spriteSheet.getSprite(0, 0))
                .withScale(3)
                .withBounds(7, 13, 18, 18)
                .build()
        });
        animations.put("STAND_LEFT", new Frame[]{
            new FrameBuilder(spriteSheet.getSprite(0, 0))
                .withScale(3)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(7, 13, 18, 18)
                .build()
        });
        animations.put("WALK_RIGHT", new Frame[]{
            new FrameBuilder(spriteSheet.getSprite(0, 0), 200)
                .withScale(3)
                .withBounds(7, 13, 18, 18)
                .build()
        });
        animations.put("WALK_LEFT", new Frame[]{
            new FrameBuilder(spriteSheet.getSprite(0, 0), 200)
                .withScale(3)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(7, 13, 18, 18)
                .build()
        });

        return animations;
    }

    private static SpriteSheet loadFriendSprite() {
        BufferedImage spriteImage = null;
        try {
            spriteImage = ImageIO.read(new File("resources/temp_pal.png"));
            spriteImage = applyTransparency(spriteImage, new Color(255, 0, 255)); // Magenta to transparent
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SpriteSheet(spriteImage, 24, 24);
    }

    @Override
    public void update(Player player) {
        super.update(player);

        float playerX = player.getX();
        float playerY = player.getY();

        float distanceToPlayer = distanceToPlayer(playerX, playerY);

        // follow the player 
        if (distanceToPlayer > SAFE_DISTANCE) {
            moveTowardsPlayer(playerX, playerY);
        }
    }

    private float distanceToPlayer(float playerX, float playerY) {
        return (float) Math.sqrt(Math.pow(this.getX() - playerX, 2) + Math.pow(this.getY() - playerY, 2));
    }

    private void moveTowardsPlayer(float playerX, float playerY) {
        if (playerX < this.getX()) {
            this.moveX(-FOLLOW_SPEED);
        } else if (playerX > this.getX()) {
            this.moveX(FOLLOW_SPEED);
        }

        if (playerY < this.getY()) {
            this.moveY(-FOLLOW_SPEED);
        } else if (playerY > this.getY()) {
            this.moveY(FOLLOW_SPEED);
        }
    }

    //make magenta transparent
    private static BufferedImage applyTransparency(BufferedImage image, Color transparentColor) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                if (pixel == transparentColor.getRGB()) {
                    newImage.setRGB(x, y, 0x00000000); 
                } else {
                    newImage.setRGB(x, y, pixel);
                }
            }
        }

        g2d.dispose();
        return newImage;
    }
}