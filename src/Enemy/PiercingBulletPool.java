package Enemy;

import entity.Bullet;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements a pool of recyclable bullets.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class PiercingBulletPool {

    /** Set of already created PiercingBullets available for reuse. */
    private static Set<PiercingBullet> pool = new HashSet<>();

    /**
     * Constructor, not called.
     */
    private PiercingBulletPool() {

    }


    /**
     * Retrieves a piercing bullet from the pool if one is available, or creates a new one if the pool is empty.
     *
     * @param positionX The requested X position for the bullet.
     * @param positionY The requested Y position for the bullet.
     * @param speedX The speedX of the bullet.
     * @param speedY The speedY of the bullet, positive or negative depending on direction.
     * @param piercingCount The number of enemies the bullet can pierce.
     * @return A PiercingBullet object, either from the pool or newly created.
     */
    public static PiercingBullet getPiercingBullet(final int positionX,
                                                   final int positionY, final int speedX, final int speedY,
                                                   final int piercingCount, final int bulletType, final double angle, final int damage) {
        PiercingBullet bullet;
        if (!pool.isEmpty()) {
            bullet = pool.iterator().next();
            pool.remove(bullet); // Remove the bullet from the pool for use
            bullet.setPositionX(positionX - bullet.getWidth() / 2);
            bullet.setPositionY(positionY);
            bullet.setSpeed(speedX, speedY);
            bullet.setPiercingCount(piercingCount);  // Reset piercing count when recycling
            bullet.setBulletType(bulletType);
            bullet.setAngle(angle);
            bullet.setDamage(damage);
            bullet.setSprite(); // Prevents destroyed bullets from being reused incorrectly
            bullet.setPreviousEnemy(null);
        } else {
            bullet = new PiercingBullet(positionX, positionY, speedX, speedY, piercingCount, bulletType, angle, damage);
            bullet.setPositionX(positionX - bullet.getWidth() / 2);
        }
        return bullet;
    }
    /**
     * Adds one or more bullets to the list of available ones.
     *
     * @param bullets
     *            Bullets to recycle.
     */
    public static void recycle(final Set<PiercingBullet> bullets) {
        pool.addAll(bullets);
    }
}
