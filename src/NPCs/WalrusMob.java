package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WalrusMob extends NPC {
    private int health;
    private int attackDamage;
    private long lastAttackTime;
    private long attackCooldown = 2000; // Attack every 2 seconds
    private static final float AGGRO_RADIUS = 150f; // Adjust the following radius

    public WalrusMob(Point location) {
        super(1, location.x, location.y, loadWalrusSprite(), "WALK_LEFT");
        this.health = 50;  // Example health
        this.attackDamage = 10;  // Example damage
    }

    private static SpriteSheet loadWalrusSprite() {
        BufferedImage spriteImage = null;
        try {
            spriteImage = ImageIO.read(new File("resources/WalrusMob.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SpriteSheet(spriteImage, 24, 24); // Adjust dimensions as needed
    }

    @Override
    public void update(Player player) {
        super.update(player);
        float playerX = player.getX();
        float playerY = player.getY();

        // Move towards the player if within aggro radius
        if (distanceToPlayer(playerX, playerY) <= AGGRO_RADIUS) {
            moveTowardsPlayer(playerX, playerY);
        }

        // Check for collision and handle attack
        if (getBounds().intersects(player.getBounds())) {
            attack(player);
        }
    }

    private float distanceToPlayer(float playerX, float playerY) {
        return (float) Math.sqrt(Math.pow(this.getX() - playerX, 2) + Math.pow(this.getY() - playerY, 2));
    }

    private void moveTowardsPlayer(float playerX, float playerY) {
        if (playerX < this.getX()) {
            this.moveX(-1.5f); // Adjust speed as needed
        } else {
            this.moveX(1.5f);
        }

        if (playerY < this.getY()) {
            this.moveY(-1.5f);
        } else {
            this.moveY(1.5f);
        }
    }

    @Override
    public void attack(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= attackCooldown) {
            player.takeDamage(attackDamage);
            lastAttackTime = currentTime;
        }
    }

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        System.out.println("WalrusMob health: " + this.health);  // Verify health reduction
        if (this.health <= 0) {
            die();
        }
    }

    @Override
    public void die() {
        System.out.println("WalrusMob defeated!");  // Verify mob death
        setActive(false);
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<>() {{
            put("WALK_LEFT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
            put("WALK_RIGHT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .build()
            });
        }};
    }
}
