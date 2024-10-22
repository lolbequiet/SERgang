package Level;

import GameObject.SpriteSheet;


public class CombatNPC extends NPC {
    private int attackDamage = 10;
    private long lastAttackTime = 0;

    public CombatNPC(int id, float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(id, x, y, spriteSheet, startingAnimation);
        setHasCombatLogic(true);
        setHealth(100);  // Example health
    }

    @Override
    protected void attack(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime > 1000) {  // 1 second cooldown
            player.takeDamage(attackDamage);
            lastAttackTime = currentTime;
        }
    }
}
