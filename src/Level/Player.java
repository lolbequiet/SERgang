package Level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.ScreenManager;
import GameObject.GameObject;
import GameObject.IntersectableRectangle;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Screens.DeathScreen;
import Utils.Direction;
import Utils.Point;
import java.awt.Font;
import java.io.File;
import java.io.IOException;



public abstract class Player extends GameObject {
    // values that affect player movement
    // these should be set in a subclass
    protected float walkSpeed = 2.3f;
    protected float originalWalkSpeed = walkSpeed;
    protected float sprintSpeed = walkSpeed * 2.3f;
    protected int interactionRange = 1;

    protected int health = 100; // Player's current health
    protected int maxHealth = 100; // Player's maximum health
    protected int stamina = 200;

    protected float walkCooldown = 0;
    protected float standCooldown = 0;

    protected Direction currentWalkingXDirection;
    protected Direction currentWalkingYDirection;
    protected Direction lastWalkingXDirection;
    protected Direction lastWalkingYDirection;

        // values used to handle player movement

    protected float moveAmountX, moveAmountY;
    protected float lastAmountMovedX, lastAmountMovedY;

        // values used to keep track of player's current state

    protected PlayerState playerState;
    protected PlayerState previousPlayerState;
    protected Direction facingDirection;
    protected Direction lastMovementDirection;
    protected boolean isLocked = false; // Locking state

    // define keys
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key MOVE_LEFT_KEY = Key.LEFT;
    protected Key MOVE_RIGHT_KEY = Key.RIGHT;
    protected Key MOVE_UP_KEY = Key.UP;
    protected Key MOVE_DOWN_KEY = Key.DOWN;
    protected Key INTERACT_KEY = Key.SPACE;
    protected Key REMOVE_KEY = Key.E;
    protected Key MAP_KEY = Key.M;
    protected Key SPRINT_KEY = Key.SHIFT;

    // New Inventory List to store items
    protected List<String> inventory = new ArrayList<>();

    private int coins; 
    // Audio 
    private Clip walkingClip; // Clip to manage the walking sound
        private boolean isWalking = false; // Tracks if the player is currently walking

    public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName) {
        super(spriteSheet, x, y, startingAnimationName);
        facingDirection = Direction.RIGHT;
        playerState = PlayerState.STANDING;
        previousPlayerState = playerState;
        this.affectedByTriggers = true;
    
        initializeWalkingSound(); // Initialize the walking sound
    }

    

// Add an item to the player's inventory
public void addToInventory(String item) {
    if (inventory == null) {
        inventory = new ArrayList<>(); // Ensure inventory is initialized
    }
    inventory.add(item);
    System.out.println("Added " + item + " to inventory.");
}

// Get the current inventory
public List<String> getInventory() {
    if (inventory == null) {
        inventory = new ArrayList<>(); // Ensure inventory is initialized
    }
    return inventory;
}

// Check if a specific item is in the inventory
public boolean hasItem(String item) {
    if (inventory == null) {
        inventory = new ArrayList<>(); // Ensure inventory is initialized
    }
    return inventory.contains(item);
}

    // Getter for current health
    public int getHealth() {
        return health;
    }

    // Getter for maximum health
    public int getMaxHealth() {
        return maxHealth;
    }

    // Getter for stamina
    public int getStamina() {
        return stamina;
    }

    // Add this method inside Player.java
public int getDamage() {
    return 10;  // Default player damage
}


    // Damage the player and update health
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage); // Ensure health doesn't drop below 0
        System.out.println("Player Health: " + health);
        if (health <= 0) {
            die(); // Handle player death
        }
    }

    // Heal the player
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount); // Ensure health doesn't exceed max health
    }

    // Handle player death
    protected void die() {
        System.out.println("Player has died.");
        ScreenManager.getInstance().setCurrentScreen(new DeathScreen(this)); // Switch to DeathScreen
    }

    public void lock() {
        isLocked = true;
        System.out.println("Player locked.");
        playerState = PlayerState.STANDING; // Prevent movement when locked
    }

    public void unlock() {
        isLocked = false;
        System.out.println("Player unlocked.");
    }

    public boolean isLocked() {
        return isLocked;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction direction) {
        this.facingDirection = direction;
    }

    public Key getInteractKey() {
        return INTERACT_KEY;
    }

    public Rectangle getInteractionRange() {
        return new Rectangle(
                getBounds().getX1() - interactionRange,
                getBounds().getY1() - interactionRange,
                getBounds().getWidth() + (interactionRange * 2),
                getBounds().getHeight() + (interactionRange * 2));
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    double nextAttackTime = -1;
    public void update() {
        // Ensure the player isn't locked
        if (!isLocked) {
            moveAmountX = 0;
            moveAmountY = 0;
    
            do {
                previousPlayerState = playerState;
                handlePlayerState();
            } while (previousPlayerState != playerState);
    
            lastAmountMovedY = super.moveYHandleCollision(moveAmountY);
            lastAmountMovedX = super.moveXHandleCollision(moveAmountX);
        }
    
        // Manage walking sound
        manageWalkingSound();
    
        // Handle animations and key updates
        regenerateHealth();
        
        handlePlayerAnimation();

        updateLockedKeys();

        super.update();
    
    
        // Handle player attack logic
        if (Keyboard.isKeyDown(Key.SPACE) && System.currentTimeMillis() > nextAttackTime) {
            for (NPC enemy : map.getEnemies()) {
                Point location = enemy.getLocation();
    
                // Calculate the distance between the player and the enemy
                double distance = Math.sqrt(Math.pow(getX() - location.x, 2) + Math.pow(getY() - location.y, 2));
                System.out.println(distance);
                
                // Attack if within range
                if (distance < 100) {
                    enemy.takeDamage(getDamage());
                }
            }
    
            System.out.println("attacking");
            nextAttackTime = System.currentTimeMillis() + 1500;  // Set attack cooldown
        }
    
        // Regenerate health over time
        regenerateHealth();
    
        // Handle animations and key updates
        handlePlayerAnimation();
        updateLockedKeys();
        super.update();
    }

    public Direction getCurrentWalkingXDirection() {
        return currentWalkingXDirection;
    }

    public Direction getCurrentWalkingYDirection() {
        return currentWalkingYDirection;
    }

    public Direction getLastWalkingXDirection() {
        return lastWalkingXDirection;
    }

    public Direction getLastWalkingYDirection() {
        return lastWalkingYDirection;
    }

    protected void handlePlayerState() {
        switch (playerState) {
            case STANDING -> playerStanding();
            case WALKING -> playerWalking();
            case INTERACT -> isInteracting();
        }
    }

    protected void playerStanding() {
        standCooldown += 1;
        staminaIncrement();

        if (!keyLocker.isKeyLocked(INTERACT_KEY) && Keyboard.isKeyDown(INTERACT_KEY)) {
            keyLocker.lockKey(INTERACT_KEY);
            map.entityInteract(this);
        }

        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY) ||
                Keyboard.isKeyDown(MOVE_UP_KEY) || Keyboard.isKeyDown(MOVE_DOWN_KEY)) {
            playerState = PlayerState.WALKING;
        }
    }

private void initializeWalkingSound() {
    try {
        File soundFile = new File("Resources/Audio/cc_walk.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        walkingClip = AudioSystem.getClip();
        walkingClip.open(audioStream);
        walkingClip.loop(Clip.LOOP_CONTINUOUSLY); // Prepare for looping
        walkingClip.stop(); // Start with sound stopped
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        System.err.println("Error initializing walking sound: " + e.getMessage());
    }
}

private void manageWalkingSound() {
    boolean isCurrentlyWalking = isMovingKeyPressed();

    if (isCurrentlyWalking && !isWalking) {

        // Restart the clip if it has finished or is stopped
        if (!walkingClip.isRunning()) {
            walkingClip.setFramePosition(0); 
            walkingClip.start(); // Play the sound
        }
        isWalking = true;
    } else if (!isCurrentlyWalking && isWalking) {
        walkingClip.stop(); // Stop the sound
        isWalking = false;
    }
}

private boolean isMovingKeyPressed() {
    return Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY) ||
           Keyboard.isKeyDown(MOVE_UP_KEY) || Keyboard.isKeyDown(MOVE_DOWN_KEY);
}
    protected void staminaDecrement() {
        if (walkCooldown >= 1.5f && stamina > 0) {
            stamina -= 1;
            walkCooldown = 0;
        }
    }

    protected void staminaIncrement() {
        if (standCooldown >= 5 && stamina < 200) {
            stamina += 1;
            standCooldown = 0;
        }
    }

    protected void playerWalking() {
        if (walkSpeed == sprintSpeed) {
            walkCooldown += 1;
            staminaDecrement();
        } else {
            standCooldown += 0.25f;
            staminaIncrement();
        }

        if (!keyLocker.isKeyLocked(INTERACT_KEY) && Keyboard.isKeyDown(INTERACT_KEY)) {
            keyLocker.lockKey(INTERACT_KEY);
            map.entityInteract(this);
        }

        if (Keyboard.isKeyDown(SPRINT_KEY) && stamina > 0) {
            walkSpeed = sprintSpeed;
        } else {
            walkSpeed = originalWalkSpeed;
        }

        handleMovement();
    }

    private void handleMovement() {
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY)) {
            moveAmountX -= walkSpeed;
            facingDirection = Direction.LEFT;
            currentWalkingXDirection = Direction.LEFT;
        } else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY)) {
            moveAmountX += walkSpeed;
            facingDirection = Direction.RIGHT;
            currentWalkingXDirection = Direction.RIGHT;
        } else {
            currentWalkingXDirection = Direction.NONE;
        }

        if (Keyboard.isKeyDown(MOVE_UP_KEY)) {
            moveAmountY -= walkSpeed;
            currentWalkingYDirection = Direction.UP;
        } else if (Keyboard.isKeyDown(MOVE_DOWN_KEY)) {
            moveAmountY += walkSpeed;
            currentWalkingYDirection = Direction.DOWN;
        } else {
            currentWalkingYDirection = Direction.NONE;
        }

        updateLastDirections();
    }


    private void updateLastDirections() {
        if (currentWalkingXDirection != Direction.NONE) {
            lastWalkingXDirection = currentWalkingXDirection;
        }
        if (currentWalkingYDirection != Direction.NONE) {
            lastWalkingYDirection = currentWalkingYDirection;
        }
    }

    protected void updateLockedKeys() {
        if (Keyboard.isKeyUp(INTERACT_KEY) && !isLocked) {
            keyLocker.unlockKey(INTERACT_KEY);
        }
    }

    protected void handlePlayerAnimation() {
        if (playerState == PlayerState.STANDING) {
            currentAnimationName = (facingDirection == Direction.RIGHT) ? "STAND_RIGHT" : "STAND_LEFT";
        } else if (playerState == PlayerState.WALKING) {
            currentAnimationName = (facingDirection == Direction.RIGHT) ? "WALK_RIGHT" : "WALK_LEFT";
        }
    }

    public void attack(NPC npc) {
        if (npc != null) {
            npc.takeDamage(20); // Player deals 20 damage
            System.out.println("Attacked NPC, dealt 20 damage");
        }
    }
    

    public String getInventorySlot(int slot) {
        if (slot >= 0 && slot < inventory.size()) {
            return inventory.get(slot);
        }
        return null;  // If slot is empty or out of range
    }

    public void drawInventory(GraphicsHandler graphicsHandler) {
        for (int i = 0; i < inventory.size(); i++) {
            String item = inventory.get(i);
            graphicsHandler.drawString(item, 60, 100 + (i * 50), new Font("Arial", Font.PLAIN, 14), Color.WHITE);
        }
    }

    public boolean isInventoryFull() {
        return inventory.size() >= 3;
    }
     

    public void addCoins(int amount){
        coins += amount;
        System.out.println("test");
    }

    public int getCoins() {
        return coins;
    }
    
    public void resetCoins() {
        coins = 0;
    }

    private int experience = 0;  // Current EXP
private int expToLevelUp = 100;  // EXP required for next level
private int level = 1;  // Player's current level

// Gain EXP when defeating enemies
// Inside Player.java
// Gain EXP when defeating enemies
public void gainExp(int amount) {
    experience += amount;
    System.out.println("Gained " + amount + " EXP. Current EXP: " + experience + "/" + expToLevelUp);

    if (experience >= expToLevelUp) {
        levelUp();
    }
}

// Handle leveling up
private void levelUp() {
    level++;
    experience = 0;  // Reset EXP for the new level
    expToLevelUp += 50;  // Increase EXP required for next level
    System.out.println("Leveled up! Current level: " + level);
}



// Getter for the player's current experience points
public int getExperience() {
    return experience;
}

// Getter for the EXP required to level up
public int getExpToLevelUp() {
    return expToLevelUp;
}

// Getter for the player's current level
public int getLevel() {
    return level;
}

// Add these fields to track health regeneration
private long lastHealTime = 0;  // Track the last heal time
private long healInterval = 3000;  // Heal every 3 seconds
private int healAmount = 5;  // Heal 5 health points every interval

// Method to regenerate health slowly over time
private void regenerateHealth() {
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastHealTime >= healInterval && health < maxHealth) {
        heal(healAmount);  // Heal the player by the specified amount
        System.out.println("Regenerated " + healAmount + " health. Current health: " + health + "/" + maxHealth);
        lastHealTime = currentTime;  // Update the last heal time
    }
}





    

    public abstract boolean isInteracting();

    protected abstract void setPosition(int i, float y);
}
