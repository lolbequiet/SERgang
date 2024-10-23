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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WalrusMob extends NPC {
    private int health;
    private int maxHealth; // Track maximum health for the health bar
    private int attackDamage;
    private long lastAttackTime;
    private long attackCooldown = 2000; // Attack every 2 seconds
    private static final float AGGRO_RADIUS = 150f; // Aggro radius for following the player

    public WalrusMob(Point location) {
        super(1, location.x, location.y, loadWalrusSprite(), "WALK_LEFT");
        this.health = 50;  // Initial health
        this.maxHealth = 50; // Set max health to the same initial value
        this.attackDamage = 10;  // Damage dealt to the player on attack
    }

    private static SpriteSheet loadWalrusSprite() {
        BufferedImage spriteImage = null;
        try {
            spriteImage = ImageIO.read(new File("resources/WalrusMob.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SpriteSheet(spriteImage, 24, 24); // Adjust dimensions if necessary
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
        System.out.println("WalrusMob health: " + this.health); // Verify health reduction
        if (this.health <= 0) {
            die();
        }
    }

    @Override
    public void die() {
        System.out.println("WalrusMob defeated!");
        setMapEntityStatus(MapEntityStatus.REMOVED);
        setActive(false);
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        // Adjust the health bar position according to the camera offset
        int screenX = Math.round(getX() - map.getCamera().getX());
        int screenY = Math.round(getY() - map.getCamera().getY());

        // Health bar dimensions
        int healthBarWidth = 50;
        int healthBarHeight = 5;
        int currentHealthWidth = (int) ((health / (float) maxHealth) * healthBarWidth);

        // Draw the health bar background (gray)
        graphicsHandler.drawFilledRectangle(
            screenX, screenY - 10, healthBarWidth, healthBarHeight, Color.GRAY
        );

        // Draw the current health (red)
        graphicsHandler.drawFilledRectangle(
            screenX, screenY - 10, currentHealthWidth, healthBarHeight, Color.RED
        );

        // Draw the health bar outline (black)
        graphicsHandler.drawRectangle(
            screenX, screenY - 10, healthBarWidth, healthBarHeight, Color.BLACK
        );
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
