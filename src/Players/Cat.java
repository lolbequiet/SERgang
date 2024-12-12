package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;

import java.util.HashMap;

public class Cat extends Player {

    private boolean eKeyPressed = false; // Prevents multiple toggles with one key press
    private boolean isAttacking = false; // Track if the attack animation is playing

    public Cat(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Cat.png"), 24, 24), x, y, "STAND_RIGHT");
        walkSpeed = 2.3f;
    }

    // Tracks if the sword is equipped
private boolean isSwordEquipped = false;

// Method to toggle sword equip/de-equip
public void toggleSword() {
    if (hasItem("Sword")) { // Check if the player owns the sword
        isSwordEquipped = !isSwordEquipped; // Toggle the state
        System.out.println("Sword " + (isSwordEquipped ? "equipped!" : "de-equipped!"));
    } else {
        System.out.println("You don't own a sword!");
    }
}

// Method to check if the sword is equipped
public boolean isSwordEquipped() {
    return isSwordEquipped;
}

// Method to equip the sword
public void equipSword() {
    if (!isSwordEquipped) {
        isSwordEquipped = true;
        System.out.println("Sword equipped.");
    } else {
        System.out.println("Sword is already equipped.");
    }
}

// Method to de-equip the sword
public void deEquipSword() {
    if (isSwordEquipped) {
        isSwordEquipped = false;
        System.out.println("Sword de-equipped.");
    } else {
        System.out.println("Sword is already de-equipped.");
    }
}

private int normalDamage = 10; // Default damage without sword
private int swordDamage = 25; // Damage with the sword equipped

@Override
public int getDamage() {
    // Return increased damage if the sword is equipped
    return isSwordEquipped ? swordDamage : normalDamage;
}


    

    @Override
    public void update() {
        super.update();

        // Handle sword equip/de-equip with E key
        if (Keyboard.isKeyDown(Key.E) && !eKeyPressed) {
            eKeyPressed = true; // Lock the key press
            toggleSword(); // Toggle sword equip or de-equip
        }

        if (Keyboard.isKeyUp(Key.E)) {
            eKeyPressed = false; // Unlock the key press when released
        }

        // Handle attack animation with SPACE key
        if (Keyboard.isKeyDown(Key.SPACE) && !isAttacking) {
            isAttacking = true;
            if (getCurrentAnimationName().contains("LEFT")) {
                this.setCurrentAnimationName("ATTACK_LEFT"); // Attack facing left
            } else {
                this.setCurrentAnimationName("ATTACK_RIGHT"); // Attack facing right
            }
        }

        // Reset to idle or walking animation after attack animation finishes
        if (isAttacking && !this.getCurrentAnimationName().startsWith("ATTACK")) {
            isAttacking = false;
            resetToIdleOrWalk(); // Reset animation to idle or walk
        }
    }

    private void resetToIdleOrWalk() {
        if (Keyboard.isKeyDown(Key.RIGHT)) {
            this.setCurrentAnimationName("WALK_RIGHT");
        } else if (Keyboard.isKeyDown(Key.LEFT)) {
            this.setCurrentAnimationName("WALK_LEFT");
        } else {
            this.setCurrentAnimationName("STAND_RIGHT");
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public boolean isInteracting() {
        return Keyboard.isKeyDown(Key.E);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<>() {{
            put("STAND_RIGHT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build()
            });

            put("STAND_LEFT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build()
            });

            put("WALK_RIGHT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build()
            });

            put("WALK_LEFT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build()
            });

            put("ATTACK_RIGHT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(2, 0), 30)
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(2, 1), 30)
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(2, 2), 30)
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(2, 3), 30)
                    .withScale(3)
                    .withBounds(6, 12, 12, 7)
                    .build()
            });

            put("ATTACK_LEFT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(2, 0), 30)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(2, 1), 30)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(2, 2), 30)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(2, 3), 30)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 12, 12, 7)
                    .build()
            });
        }};
    }

    @Override
    protected void setPosition(int i, float y) {
        throw new UnsupportedOperationException("Unimplemented method 'setPosition'");
    }
}