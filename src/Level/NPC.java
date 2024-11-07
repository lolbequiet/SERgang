package Level;

import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Utils.Direction;
import java.util.HashMap;
import java.awt.Color;

public class NPC extends MapEntity {
    protected int id;
    protected boolean isLocked = false;
    protected boolean isActive = true;  // Track if the NPC is active
    protected boolean hasCombatLogic = false;  // Combat-enabled NPCs
    protected int health = -1;  // Default: non-combat NPCs have no health
    protected int maxHealth = -1;
    protected int expReward = 25;  // EXP reward on defeat

    public NPC(int id, float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, spriteSheet, startingAnimation);
        this.id = id;
    }

    public NPC(int id, float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        this.id = id;
    }

    public NPC(int id, float x, float y, Frame[] frames) {
        super(x, y, frames);
        this.id = id;
    }

    public NPC(int id, float x, float y, Frame frame) {
        super(x, y, frame);
        this.id = id;
    }

    public NPC(int id, float x, float y) {
        super(x, y);
        this.id = id;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setHasCombatLogic(boolean hasCombatLogic) {
        this.hasCombatLogic = hasCombatLogic;
    }

    public boolean hasCombatLogic() {
        return hasCombatLogic;
    }

    public void setHealth(int health) {
        this.health = health;
        this.maxHealth = health;  // Initialize max health
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        if (health > 0) {
            health -= damage;
            if (health <= 0) {
                die();  // Handle NPC death
            }
        }
    }

    protected void die() {
        setActive(false);  // Deactivate NPC upon death
    
        // Grant EXP to the player if the player exists on the map.
        if (map.getPlayer() != null) {
            Player player = map.getPlayer();
            player.gainExp(expReward);  // Grant the EXP reward
            System.out.println("NPC died: " + this.getId() + ", granting EXP: " + expReward);
        }
    }
    

    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    // Make the NPC face the player
    public void facePlayer(Player player) {
        float centerPoint = getBounds().getX() + (getBounds().getWidth() / 2);
        float playerCenterPoint = player.getBounds().getX() + (player.getBounds().getWidth() / 2);

        if (centerPoint < playerCenterPoint) {
            this.currentAnimationName = "STAND_RIGHT";
        } else {
            this.currentAnimationName = "STAND_LEFT";
        }
    }

    public void stand(Direction direction) {
        if (direction == Direction.RIGHT) {
            this.currentAnimationName = "STAND_RIGHT";
        } else if (direction == Direction.LEFT) {
            this.currentAnimationName = "STAND_LEFT";
        }
    }

    public void walk(Direction direction, float speed) {
        switch (direction) {
            case RIGHT:
                this.currentAnimationName = "WALK_RIGHT";
                moveX(speed);
                break;
            case LEFT:
                this.currentAnimationName = "WALK_LEFT";
                moveX(-speed);
                break;
            case UP:
                moveY(-speed);
                break;
            case DOWN:
                moveY(speed);
                break;
            default:
                break;
        }
    }

    @Override
    public void update(Player player) {
        if (!isActive || isLocked) {
            return;  // Skip update if NPC is inactive or locked
        }

        performAction(player);  // Custom actions for specific NPCs

        if (hasCombatLogic && getBounds().intersects(player.getBounds())) {
            attack(player);  // Trigger attack if player is within bounds
        }

        super.update();
    }

    protected void attack(Player player) {
        // Override this method in subclasses for combat behavior
    }

    protected void performAction(Player player) {
        // Placeholder for custom NPC behavior
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        // Draw health bar if NPC has health
        if (hasCombatLogic && health > 0) {
            drawHealthBar(graphicsHandler);
        }
    }

    private void drawHealthBar(GraphicsHandler graphicsHandler) {
        int barWidth = 50;
        int healthBarWidth = (int) (((float) health / maxHealth) * barWidth);

        graphicsHandler.drawFilledRectangle(getX(), getY() - 10, healthBarWidth, 5, Color.RED);
        graphicsHandler.drawRectangle(getX(), getY() - 10, barWidth, 5, Color.BLACK);
    }

    public void lock() {
        isLocked = true;
    }

    public void unlock() {
        isLocked = false;
    }
}
