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
    private static final int SPEED_EFFECT_DURATION = 5000; // 5초

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
        super(positionX, positionY, 12, 12, isSpeedUp ? Color.ORANGE : Color.CYAN); // 느려지는 아이템(오렌지)과 빨라지는 아이템(하늘)
        this.spriteType = isSpeedUp ? SpriteType.ItemSpeedUp : SpriteType.ItemSpeedSlow;
        this.isSpeedUp = isSpeedUp;
        this.speedMultiplier = isSpeedUp ? 3.0 : 0.15; // true이면 속도를 3.0배 빠르게, false이면 0.15배 느리게 설정
        this.speedEffectCooldown = Core.getCooldown(SPEED_EFFECT_DURATION);
    }

    /**
     * Activates the speed effect on the enemy ship.
     *
     * @param enemyShip The enemy ship to change the speed.
     */
    public void applySpeedEffect(EnemyShip enemyShip) {
        double newSpeedMultiplier = enemyShip.getSpeedMultiplier() * speedMultiplier;
        enemyShip.setSpeedMultiplier(newSpeedMultiplier); // 새로운 속도 배수 적용

        speedEffectCooldown.reset(); // 쿨다운 리셋
    }

    /**
     * Updates the item's state and checks if the speed effect should end.
     *
     * @param enemyShip The enemy ship to check and remove the effect if expired.
     */
    public void update(EnemyShip enemyShip) {
        if (speedEffectCooldown != null && speedEffectCooldown.checkFinished()) {
            // 효과가 종료되면 원래 속도로 복원
            enemyShip.setSpeedMultiplier(1.0); // 원래 속도로 복구
            speedEffectCooldown = null; // 쿨다운 해제
        }
    }



}