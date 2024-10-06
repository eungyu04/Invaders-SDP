package inventory_develop;

import entity.Bullet;
import entity.BulletPool;
import entity.Entity;

import java.util.HashSet;
import java.util.Set;

/**
 * TwoBulletPool extends BulletPool to manage firing two bullets at once.
 */
public class NumberOfBullet{

    /** Offset to ensure bullets don't overlap when fired together. */
    private static final int OFFSET_X_TWOBULLETS = 15;
    private static final int OFFSET_X_THREEBULLETS = 12;

    /** Bullet levels */
    private static int BulletLevel = 2;

    private BulletPool bulletPool;

    /**
     * Constructor
     */
    public NumberOfBullet() {
    }

    /**
     *
     * @return
     */
    public Set<Bullet> AddBullet(int positionX, int positionY, int speed) {
        Set<Bullet> bullets = new HashSet<>();

        switch (BulletLevel) {
            case 1:
                bullets.add(bulletPool.getBullet(positionX, positionY, speed));
                break;
            case 2:
                bullets.add(bulletPool.getBullet(positionX - OFFSET_X_TWOBULLETS, positionY, speed));
                bullets.add(bulletPool.getBullet(positionX + OFFSET_X_TWOBULLETS, positionY, speed));
                break;
            case 3:
                bullets.add(bulletPool.getBullet(positionX + OFFSET_X_TWOBULLETS, positionY, speed));
                bullets.add(bulletPool.getBullet(positionX, positionY, speed));
                bullets.add(bulletPool.getBullet(positionX - OFFSET_X_TWOBULLETS, positionY, speed));
                break;
        }
        return bullets;

    }



    /**
     * Returns two bullets from the pool if available, otherwise creates new ones.
     * Ensures the bullets are placed at different positions.
     *
     * @param positionX Requested position of the bullet in the X axis.
     * @param positionY Requested position of the bullet in the Y axis.
     * @param speed     Requested speed of the bullet, positive or negative depending
     *                  on direction - positive is down.
     * @return An array containing two bullets.
     */
    public Bullet[] getTwoBullets(int positionX, int positionY, int speed) {
        Bullet[] bullets = new Bullet[2];

        bullets[0] = bulletPool.getBullet(positionX - OFFSET_X_TWOBULLETS, positionY, speed);
        bullets[1] = bulletPool.getBullet(positionX + OFFSET_X_TWOBULLETS, positionY, speed);

        return bullets;
    }

    /**
     * Returns three bullets from the pool if available, otherwise creates new ones.
     * Ensures the bullets are placed at different positions.
     *
     * @param positionX Requested position of the bullet in the X axis.
     * @param positionY Requested position of the bullet in the Y axis.
     * @param speed     Requested speed of the bullet, positive or negative depending
     *                  on direction - positive is down.
     * @return An array containing three bullets.
     */
    public Bullet[] getThreeBullets(int positionX, int positionY, int speed) {
        Bullet[] bullets = new Bullet[3];

        bullets[0] = bulletPool.getBullet(positionX - OFFSET_X_THREEBULLETS, positionY, speed);
        bullets[1] = bulletPool.getBullet(positionX, positionY, speed);
        bullets[2] = bulletPool.getBullet(positionX + OFFSET_X_THREEBULLETS, positionY, speed);

        return bullets;
    }

}