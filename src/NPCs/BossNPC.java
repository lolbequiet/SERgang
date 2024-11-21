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

/**
 * A class representing the boss NPC with collision damage and a cooldown.
 */
public class BossNPC extends NPC {
    private int health;
    private int maxHealth;
    private int attackDamage;
    private static final float AGGRO_RADIUS = 200f;
    private boolean enraged = false;
    private long lastAttackTime; // Tracks the last time damage was dealt
    private static final long ATTACK_COOLDOWN = 1000; // Cooldown in milliseconds (1 second)

    public BossNPC(Point location) {
        super(1, location.x, location.y, loadBossSprite(), "WALK_LEFT");
        this.health = 200; // Boss has higher health
        this.maxHealth = 200;
        this.attackDamage = 15; // Damage per hit
        this.lastAttackTime = 0; // Initialize the attack timer
    }

    private static SpriteSheet loadBossSprite() {
        BufferedImage spriteImage = null;
        try {
            spriteImage = ImageIO.read(new File("resources/boss.png"));
            spriteImage = applyTransparency(spriteImage, new Color(255, 0, 255)); // Magenta to transparent
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SpriteSheet(spriteImage, 24, 24); // Ensure sprites are 24x24
    }

    private static BufferedImage applyTransparency(BufferedImage image, Color transparentColor) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                if (pixel == transparentColor.getRGB()) {
                    newImage.setRGB(x, y, 0x00000000); // Fully transparent pixel
                } else {
                    newImage.setRGB(x, y, pixel);
                }
            }
        }

        g2d.dispose();
        return newImage;
    }

    @Override
    public void update(Player player) {
        super.update(player);

        float playerX = player.getX();
        float playerY = player.getY();

        // Move towards player when within aggro radius
        if (distanceToPlayer(playerX, playerY) <= AGGRO_RADIUS) {
            moveTowardsPlayer(playerX, playerY);
        }

        // Deal damage to the player on collision with a cooldown
        if (getBounds().intersects(player.getBounds())) {
            dealDamageToPlayer(player);
        }

        if (!enraged && health <= maxHealth / 2) {
            enraged = true;
            System.out.println("Boss is now enraged!");
        }
    }

    private float distanceToPlayer(float playerX, float playerY) {
        return (float) Math.sqrt(Math.pow(this.getX() - playerX, 2) + Math.pow(this.getY() - playerY, 2));
    }

    private void moveTowardsPlayer(float playerX, float playerY) {
        if (playerX < this.getX()) {
            this.moveX(-0.5f); // Slower movement for the boss
        } else {
            this.moveX(0.5f);
        }

        if (playerY < this.getY()) {
            this.moveY(-0.5f);
        } else {
            this.moveY(0.5f);
        }
    }

    private void dealDamageToPlayer(Player player) {
        long currentTime = System.currentTimeMillis();

        // Only deal damage if cooldown has passed
        if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            System.out.println("Boss damaged the player for " + attackDamage + " HP!");
            player.takeDamage(attackDamage);
            lastAttackTime = currentTime; // Update the last attack time
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (health > 0) {
            health -= damage;
            System.out.println("Boss takes " + damage + " damage! Remaining health: " + health);
            if (health <= 0) {
                die();
            }
        }
    }

    @Override
    public void die() {
        System.out.println("Boss defeated!");
        setMapEntityStatus(MapEntityStatus.REMOVED);
        setActive(false);

        // Handle flag updates if necessary
        if (map.getFlagManager() != null) {
            map.getFlagManager().setFlag("BossDefeated", true);
        }
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        int screenX = Math.round(getX() - map.getCamera().getX());
        int screenY = Math.round(getY() - map.getCamera().getY());

        // Draw health bar above boss
        int healthBarWidth = 100;
        int healthBarHeight = 10;
        int currentHealthWidth = (int) ((health / (float) maxHealth) * healthBarWidth);

        // Background of health bar
        graphicsHandler.drawFilledRectangle(
            screenX - healthBarWidth / 2, screenY - 20, healthBarWidth, healthBarHeight, Color.GRAY
        );

        // Current health amount
        graphicsHandler.drawFilledRectangle(
            screenX - healthBarWidth / 2, screenY - 20, currentHealthWidth, healthBarHeight, Color.RED
        );

        // Health bar border
        graphicsHandler.drawRectangle(
            screenX - healthBarWidth / 2, screenY - 20, healthBarWidth, healthBarHeight, Color.BLACK
        );
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<>() {{
            put("WALK_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });

            put("WALK_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .build()
            });

            put("ATTACK_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(1, 0), 30)
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });

            put("ATTACK_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(1, 0), 30)
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .build()
            });
        }};
    }
}
