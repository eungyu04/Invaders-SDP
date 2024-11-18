package inventory_develop;

import Enemy.PiercingBullet;
import Enemy.PiercingBulletPool;
import engine.Core;

import java.io.IOException;
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
    private static int bulletLevel = 1;
    /** PiercingBullet levles */
    private static int piercingbulletLevel = 1;
    private final int PierceMax = 3;

    /**
     * Constructor
     */
    public NumberOfBullet() {
        try {
            bulletLevel = Core.getUpgradeManager().getBulletNum();
            if (bulletLevel > 3){
                bulletLevel = 3;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return
     */
    public Set<PiercingBullet> addBullet(int positionX, int positionY, int speedX, int speedY, boolean canShootBomb, int bulletType, double angle) {
        Set<PiercingBullet> bullets = new HashSet<>();

        if (canShootBomb) {
            bullets.add(PiercingBulletPool.getPiercingBullet(positionX, positionY, speedX, speedY, 1, bulletType, angle));
            return bullets;
        }

        switch (bulletLevel) {
            case 1:
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX, positionY, speedX, speedY, piercingbulletLevel, bulletType, angle));
                break;
            case 2:
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX - OFFSET_X_TWOBULLETS + 5, positionY, speedX, speedY, piercingbulletLevel, bulletType, angle));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX + OFFSET_X_TWOBULLETS - 5, positionY, speedX, speedY, piercingbulletLevel, bulletType, angle));
                break;
            case 3:
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX + OFFSET_X_THREEBULLETS, positionY, speedX, speedY, piercingbulletLevel, bulletType, angle));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX, positionY, speedX, speedY, piercingbulletLevel, bulletType, angle));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX - OFFSET_X_THREEBULLETS, positionY, speedX, speedY, piercingbulletLevel, bulletType, angle));
                break;
        }

        return bullets;

    }

    public void pierceup() {
        if (piercingbulletLevel < PierceMax){
            piercingbulletLevel += 1;
        }
    }

    public void ResetPierceLevel(){
        piercingbulletLevel = 1;
    }
}