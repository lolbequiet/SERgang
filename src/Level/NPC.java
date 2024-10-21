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
    protected boolean hasCombatLogic = false;  // Can be enabled for combat NPCs
    protected int health = -1;  // Default: non-combat NPCs don't have health
    protected int maxHealth = -1;

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
                die();
            }
        }
    }

    protected void die() {
        setActive(false);  // Deactivate or remove NPC from the game
    }

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
                // Maintain the current walking animation
                break;
        }
    }

    @Override
    public void update(Player player) {
        if (!isActive || isLocked) {
            return;
        }

        performAction(player);

        // If this NPC has combat logic, check for collision with the player
        if (hasCombatLogic && getBounds().intersects(player.getBounds())) {
            attack(player);
        }

        super.update();
    }

    protected void attack(Player player) {
        // Placeholder: This method can be overridden in subclasses like CombatNPC
    }

    protected void performAction(Player player) {
        // To be implemented by specific NPCs if needed
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        // If NPC has health, draw a health bar above it
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
