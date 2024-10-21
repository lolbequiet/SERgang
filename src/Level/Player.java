package Level;

import java.awt.Color;
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

public abstract class Player extends GameObject {
    protected float walkSpeed = 2.3f;
    protected float originalWalkSpeed = walkSpeed;
    protected float sprintSpeed = walkSpeed * 4f;
    protected int interactionRange = 1;
    protected int health = 100; // Added health field
    protected int stamina = 200;
    protected float walkCooldown = 0;
    protected float standCooldown = 0;
    protected Direction currentWalkingXDirection;
    protected Direction currentWalkingYDirection;
    protected Direction lastWalkingXDirection;
    protected Direction lastWalkingYDirection;
    protected float moveAmountX, moveAmountY;
    protected float lastAmountMovedX, lastAmountMovedY;
    protected PlayerState playerState;
    protected PlayerState previousPlayerState;
    protected Direction facingDirection;
    protected Direction lastMovementDirection;
    protected boolean isLocked = false; // Locking state

    // Key handling
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key MOVE_LEFT_KEY = Key.LEFT;
    protected Key MOVE_RIGHT_KEY = Key.RIGHT;
    protected Key MOVE_UP_KEY = Key.UP;
    protected Key MOVE_DOWN_KEY = Key.DOWN;
    protected Key INTERACT_KEY = Key.SPACE;
    protected Key REMOVE_KEY = Key.E;
    protected Key MAP_KEY = Key.M;
    protected Key SPRINT_KEY = Key.SHIFT;

    public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName) {
        super(spriteSheet, x, y, startingAnimationName);
        facingDirection = Direction.RIGHT;
        playerState = PlayerState.STANDING;
        previousPlayerState = playerState;
        this.affectedByTriggers = true;
    }

    public int getStamina() {
        return stamina;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
        System.out.println("Player Health: " + health);
    }

    protected void die() {
    System.out.println("Player has died.");
    ScreenManager.getInstance().setCurrentScreen(new DeathScreen(this)); // Pass the current player instance
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
            getBounds().getHeight() + (interactionRange * 2)
        );
    }

    public void update() {
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
        handlePlayerAnimation();
        updateLockedKeys();
        super.update();
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

    public abstract boolean isInteracting();
}
