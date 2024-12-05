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
    private static final int OFFSET_X_FIVEBULLETS = 8;

    /** Bulet damage */
    private static int bulletDamage = 1;
    /** Bullet levels */
    private static int bulletCount = 1;
    /** PiercingBullet levels */
    private static int piercingBulletLevel = 1;
    private final int PierceMax = 3;

    /**
     * Constructor
     */
    public NumberOfBullet() {
        try {
            bulletDamage = Core.getUpgradeManager().getBulletDamage();
            bulletCount = Core.getUpgradeManager().getBulletNum();
            if (bulletCount > 5){
                bulletCount = 5;
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
            bullets.add(PiercingBulletPool.getPiercingBullet(positionX, positionY, speedX, speedY, 1, bulletType, angle, bulletDamage));
            return bullets;
        }

        switch (bulletCount) {
            case 1:
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                break;
            case 2:
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX - OFFSET_X_TWOBULLETS + 5, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX + OFFSET_X_TWOBULLETS - 5, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                break;
            case 3:
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX + OFFSET_X_THREEBULLETS, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX - OFFSET_X_THREEBULLETS, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                break;
            case 5:
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX - OFFSET_X_FIVEBULLETS * 2, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX - OFFSET_X_FIVEBULLETS, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX + OFFSET_X_FIVEBULLETS, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                bullets.add(PiercingBulletPool.getPiercingBullet(positionX + OFFSET_X_FIVEBULLETS * 2, positionY, speedX, speedY, piercingBulletLevel, bulletType, angle, bulletDamage));
                break;
        }

        return bullets;

    }

    public void pierceUp() {
        if (piercingBulletLevel < PierceMax){
            piercingBulletLevel += 1;
        }
    }

    public void ResetPierceLevel(){
        piercingBulletLevel = 1;
    }
}