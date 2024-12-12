package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Screens.DeathScreen;
import Level.Camera;
import Utils.Direction;
import Level.Map;
import Utils.Point;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import Level.Player;

import java.util.HashMap;

public class Cat extends Player {
    private boolean swordPickedUp = false;     
    private boolean swordEquipped = false;     
    private final int BASE_DAMAGE = 10;        
    private final int SWORD_DAMAGE_BOOST = 10; 
    private boolean eKeyPressed = false;       
    private boolean isAttacking = false;       

    // Keys to switch sprites
    private boolean zeroKeyPressed = false;    
    private boolean nineKeyPressed = false;    

    public Cat(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Cat.png"), 24, 24), x, y, "STAND_RIGHT");
        walkSpeed = 2.3f;
        // Load initial animations
        this.animations = loadAnimations(this.spriteSheet);
        setCurrentAnimationName("STAND_RIGHT");
    }

    // Equip the sword to increase damage
    public void equipSword() {
        if (swordPickedUp && !swordEquipped) {
            swordEquipped = true;
            System.out.println("Sword equipped! Damage boosted.");
        } else if (!swordPickedUp) {
            System.out.println("You don't have the sword to equip.");
        }
    }

    // De-equip the sword to reset damage
    public void deEquipSword() {
        if (swordEquipped) {
            swordEquipped = false;
            System.out.println("Sword de-equipped. Damage reset.");
        }
    }

    // Pick up the sword and equip it immediately
    public void pickUpSword() {
        if (!swordPickedUp) {  // Ensure it runs only once
            swordPickedUp = true;
            System.out.println("You picked up the sword!");
            equipSword();
        }
    }

    // Override getDamage to reflect equipped sword damage boost
    @Override
    public int getDamage() {
        return swordEquipped ? BASE_DAMAGE + SWORD_DAMAGE_BOOST : BASE_DAMAGE;
    }

    @Override
    public void update() {
        super.update();

        // Handle sword equip/de-equip with E key
        if (swordPickedUp && Keyboard.isKeyDown(Key.E) && !eKeyPressed) {
            eKeyPressed = true;
            if (swordEquipped) {
                deEquipSword();
            } else {
                equipSword();
            }
        }

        if (Keyboard.isKeyUp(Key.E)) {
            eKeyPressed = false;  // Reset flag on key release
        }

        // Handle attack animation with SPACE key
        if (Keyboard.isKeyDown(Key.SPACE) && !isAttacking) {
            isAttacking = true;
            if (getCurrentAnimationName().contains("LEFT")) {
                setCurrentAnimationName("ATTACK_LEFT"); // Attack facing left
            } else {
                setCurrentAnimationName("ATTACK_RIGHT"); // Attack facing right
            }
        }

        // Reset to idle or walking animation after attack animation finishes
        if (isAttacking && !getCurrentAnimationName().startsWith("ATTACK")) {
            isAttacking = false;
            resetToIdleOrWalk();
        }

        // Press 0 to switch to boss.png
        if (Keyboard.isKeyDown(Key.ZERO) && !zeroKeyPressed) {
            zeroKeyPressed = true;
            switchToBossSprite();
        }
        if (Keyboard.isKeyUp(Key.ZERO)) {
            zeroKeyPressed = false;
        }

        // Press 9 to switch back to Cat.png
        if (Keyboard.isKeyDown(Key.NINE) && !nineKeyPressed) {
            nineKeyPressed = true;
            switchToCatSprite();
        }
        if (Keyboard.isKeyUp(Key.NINE)) {
            nineKeyPressed = false;
        }
    }

    private void resetToIdleOrWalk() {
        if (Keyboard.isKeyDown(Key.RIGHT)) {
            setCurrentAnimationName("WALK_RIGHT");
        } else if (Keyboard.isKeyDown(Key.LEFT)) {
            setCurrentAnimationName("WALK_LEFT");
        } else {
            setCurrentAnimationName("STAND_RIGHT");
        }
    }

    public void switchToBossSprite() {
        this.spriteSheet = new SpriteSheet(ImageLoader.load("boss.png"), 24, 24);
        this.animations = loadAnimations(this.spriteSheet);
        setCurrentAnimationName("STAND_RIGHT");
    }

    public void switchToCatSprite() {
        this.spriteSheet = new SpriteSheet(ImageLoader.load("Cat.png"), 24, 24);
        this.animations = loadAnimations(this.spriteSheet);
        setCurrentAnimationName("STAND_RIGHT");
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