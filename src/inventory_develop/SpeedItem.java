package inventory_develop;

import java.awt.Color;
import engine.Core;
import engine.Cooldown;
import engine.DrawManager.SpriteType;
import entity.EnemyShip;
import entity.Entity;

/**
 * Implements an item that changes the speed of enemy ships.
 * It can either slow down or speed up the enemy ship based on the type.
 */
public class SpeedItem extends Entity {

    /**
     * Duration of the speed effect in milliseconds.
     */
    private static final int SPEED_EFFECT_DURATION = 5000; // 5 second

    /**
     * Speed change percentage (e.g., 50% slower or faster).
     */
    private double speedMultiplier;
    /**
     * Cooldown to manage the duration of the speed effect.
     */
    private Cooldown speedEffectCooldown;
    /**
     * Type of the item: true for speed up, false for slow down.
     */
    private boolean isSpeedUp;

    /**
     *
     */
    public SpeedItem( int positionX,  int positionY, boolean isSpeedUp) {
        super(positionX, positionY, 12, 12, isSpeedUp ? Color.ORANGE : Color.CYAN); // Items that slow down (orange) and items that speed up (sky blue)
        this.spriteType = isSpeedUp ? SpriteType.ItemSpeedUp : SpriteType.ItemSpeedSlow;
        this.isSpeedUp = isSpeedUp;
        this.speedMultiplier = isSpeedUp ? 3.0 : 0.15; // If true, set the speed 3.0 times faster, if false, set it 0.15 times slower.
        this.speedEffectCooldown = Core.getCooldown(SPEED_EFFECT_DURATION);
    }

    /**
     * Activates the speed effect on the enemy ship.
     *
     * @param enemyShip The enemy ship to change the speed.
     */
    public void applySpeedEffect(EnemyShip enemyShip) {
        double newSpeedMultiplier = enemyShip.getSpeedMultiplier() * speedMultiplier;
        enemyShip.setSpeedMultiplier(newSpeedMultiplier); // New speed multiplier applied

        speedEffectCooldown.reset(); // Cooldown Reset
    }

    /**
     * Updates the item's state and checks if the speed effect should end.
     *
     * @param enemyShip The enemy ship to check and remove the effect if expired.
     */
    public void update(EnemyShip enemyShip) {
        if (speedEffectCooldown != null && speedEffectCooldown.checkFinished()) {
            enemyShip.setSpeedMultiplier(1.0); // Restore to original speed
            speedEffectCooldown = null; // Cooldown off
        }
    }



}