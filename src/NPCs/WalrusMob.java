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

// flag made "WalrusMobDefeated" ~ implied


public class WalrusMob extends NPC {
    private int health;
    private int maxHealth;
    private int attackDamage;
    private long lastAttackTime;
    private long attackCooldown = 2000;
    private static final float AGGRO_RADIUS = 150f;

    public WalrusMob(Point location) {
        super(1, location.x, location.y, loadWalrusSprite(), "WALK_LEFT");
        this.health = 50;
        this.maxHealth = 50;
        this.attackDamage = 10;
    }

    private static SpriteSheet loadWalrusSprite() {
        BufferedImage spriteImage = null;
        try {
            spriteImage = ImageIO.read(new File("resources/WalrusMob.png"));
            spriteImage = applyTransparency(spriteImage, new Color(255, 0, 255)); // Magenta to transparent
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SpriteSheet(spriteImage, 24, 24);
    }

    // Apply transparency to a specific color (magenta)
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

        if (distanceToPlayer(playerX, playerY) <= AGGRO_RADIUS) {
            moveTowardsPlayer(playerX, playerY);
        }

        if (getBounds().intersects(player.getBounds())) {
            attack(player);
        }
    }

    private float distanceToPlayer(float playerX, float playerY) {
        return (float) Math.sqrt(Math.pow(this.getX() - playerX, 2) + Math.pow(this.getY() - playerY, 2));
    }

    private void moveTowardsPlayer(float playerX, float playerY) {
        if (playerX < this.getX()) {
            this.moveX(-1.5f);
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
        System.out.println("WalrusMob health: " + this.health);
        if (this.health <= 0) {
            die();
        }
    }


    @Override
    public void die() {
        System.out.println("WalrusMob defeated!");
        setMapEntityStatus(MapEntityStatus.REMOVED);
        setActive(false);

        // Check if all mobs are defeated
        boolean allMobsDefeated = map.getEnemies().stream().noneMatch(NPC::isActive);
        if (allMobsDefeated && map.getFlagManager() != null) {
            map.getFlagManager().setFlag("WalrusMobDefeated", true);
            System.out.println("All mobs defeated!");
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

        int healthBarWidth = 50;
        int healthBarHeight = 5;
        int currentHealthWidth = (int) ((health / (float) maxHealth) * healthBarWidth);

        graphicsHandler.drawFilledRectangle(
            screenX, screenY - 10, healthBarWidth, healthBarHeight, Color.GRAY
        );

        graphicsHandler.drawFilledRectangle(
            screenX, screenY - 10, currentHealthWidth, healthBarHeight, Color.RED
        );

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
