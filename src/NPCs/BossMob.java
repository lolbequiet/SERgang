package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.MapEntityStatus;
import Level.NPC;
import Level.Player;
import Utils.Direction;
import Utils.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BossMob extends NPC {
    private int health;
    private int maxHealth;
    private int attackDamage;
    private long lastAttackTime;
    private long attackCooldown = 2000; // Boss-specific cooldown
    private static final float AGGRO_RADIUS = 300f; // Boss's aggro range
    private boolean isAttacking = false;

    public BossMob(Point location) {
        super(1, location.x, location.y, loadBossSprite(), "STAND_RIGHT");
        this.health = 200; // Higher health for the boss
        this.maxHealth = 200;
        this.attackDamage = 20; // More damage per attack
        this.setHasCombatLogic(true); // Enables combat logic
        this.setIsUncollidable(true); // Allows the player to pass through the boss
    }

    private static SpriteSheet loadBossSprite() {
        BufferedImage spriteImage = null;
        try {
            spriteImage = ImageIO.read(new File("resources/boss.png")); // Ensure this file exists
            spriteImage = applyTransparency(spriteImage, new Color(255, 0, 255)); // Magenta to transparent
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SpriteSheet(spriteImage, 24, 24); // Adjust sprite size if necessary
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

        // If the player is within the aggro radius, move towards them
        if (distanceToPlayer(playerX, playerY) <= AGGRO_RADIUS) {
            isAttacking = true;
            moveTowardsPlayer(playerX, playerY);
        } else {
            isAttacking = false;
            stand(playerX < this.getX() ? "STAND_LEFT" : "STAND_RIGHT");
        }

        // Attack the player on collision
        if (getBounds().intersects(player.getBounds())) {
            triggerAttackAnimation(player);
            attack(player);
        }
    }

    private float distanceToPlayer(float playerX, float playerY) {
        return (float) Math.sqrt(Math.pow(this.getX() - playerX, 2) + Math.pow(this.getY() - playerY, 2));
    }

    private void moveTowardsPlayer(float playerX, float playerY) {
        if (playerX < this.getX()) {
            this.walk(Direction.LEFT, 0.5f); // Use Direction.LEFT
        } else {
            this.walk(Direction.RIGHT, 0.5f); // Use Direction.RIGHT
        }

        if (playerY < this.getY()) {
            this.moveY(-0.5f);
        } else {
            this.moveY(0.5f);
        }
    }

    private void stand(String direction) {
        this.currentAnimationName = direction;
    }

    @Override
    public void attack(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= attackCooldown) {
            System.out.println("Boss damages the player for " + attackDamage + " HP!");
            player.takeDamage(attackDamage);
            lastAttackTime = currentTime;
        }
    }

    private void triggerAttackAnimation(Player player) {
        if (player.getX() < this.getX()) {
            this.setCurrentAnimationName("ATTACK_LEFT");
        } else {
            this.setCurrentAnimationName("ATTACK_RIGHT");
        }
    }

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        System.out.println("Boss health: " + this.health);
        if (this.health <= 0) {
            die();
        }
    }

    @Override
    public void die() {
        System.out.println("Boss defeated!");
        setMapEntityStatus(MapEntityStatus.REMOVED);
        setActive(false);

        // Check if boss defeat flag needs to be set
        if (map.getFlagManager() != null) {
            map.getFlagManager().setFlag("BossDefeated", true);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        int screenX = Math.round(getX() - map.getCamera().getX());
        int screenY = Math.round(getY() - map.getCamera().getY());

        int healthBarWidth = 100;
        int healthBarHeight = 10;
        int currentHealthWidth = (int) ((health / (float) maxHealth) * healthBarWidth);

        // Draw background of health bar
        graphicsHandler.drawFilledRectangle(
            screenX - healthBarWidth / 2, screenY - 20, healthBarWidth, healthBarHeight, Color.GRAY
        );

        // Draw current health amount
        graphicsHandler.drawFilledRectangle(
            screenX - healthBarWidth / 2, screenY - 20, currentHealthWidth, healthBarHeight, Color.RED
        );

        // Draw health bar border
        graphicsHandler.drawRectangle(
            screenX - healthBarWidth / 2, screenY - 20, healthBarWidth, healthBarHeight, Color.BLACK
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
            put("STAND_LEFT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(1, 0))
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
            put("STAND_RIGHT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(1, 0))
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .build()
            });
            put("ATTACK_LEFT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(2, 0), 30)
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
            put("ATTACK_RIGHT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(2, 0), 30)
                    .withScale(3)
                    .withBounds(7, 13, 18, 18)
                    .build()
            });
        }};
    }
}
