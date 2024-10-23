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
    private boolean swordPickedUp = false;  // Track if the sword has been picked up
    private boolean swordEquipped = false;  // Track if the sword is currently equipped
    private final int BASE_DAMAGE = 5;      // Default damage
    private final int SWORD_DAMAGE_BOOST = 10; // Damage boost when the sword is equipped

    public Cat(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Cat.png"), 24, 24), x, y, "STAND_RIGHT");
        walkSpeed = 2.3f;
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

    // Automatically equip the sword upon pickup
    public void pickUpSword() {
        swordPickedUp = true;
        equipSword();  // Equip the sword immediately after picking it up
        System.out.println("You picked up the sword!");
    }

    // Get the player's current damage based on whether the sword is equipped
    public int getDamage() {
        return swordEquipped ? BASE_DAMAGE + SWORD_DAMAGE_BOOST : BASE_DAMAGE;
    }

    @Override
    public void update() {
        super.update();

        // Toggle sword equip/de-equip with 'E' key
        if (Keyboard.isKeyDown(Key.E)) {
            if (swordEquipped) {
                deEquipSword();  // De-equip the sword if currently equipped
            } else {
                equipSword();  // Equip the sword if not currently equipped
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public boolean isInteracting() {
        return Keyboard.isKeyDown(Key.E);  // Check if the player is interacting (E key)
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
        }};
    }
}
