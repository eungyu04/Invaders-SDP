package Enemy;

import engine.Core;
import java.io.IOException;

public class PlayerGrowth {

    //Player's base stats
    private int health;          //Health
    private static double moveSpeed = 1.5;       //Movement speed
    private static int bulletSpeed = 15;     // Bullet speedY
    private static int shootingDelay = 750;   // Shooting delay
    private static boolean canShoot360;     // 360도로 쏠 수 있는지 없는지

    //Constructor to set initial values
    public PlayerGrowth() {//  Base shooting delay is 750ms

        // CtrlS: set player growth based on upgrade_status.properties
        try {
            moveSpeed = Core.getUpgradeManager().getMovementSpeed();
            bulletSpeed = Core.getUpgradeManager().getBulletSpeed();
            shootingDelay = Core.getUpgradeManager().getAttackSpeed();
            canShoot360 = Core.getUpgradeManager().getShipShoot360();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Increases health
    public void increaseHealth(int increment) {
        this.health += increment;
    }

    //Increases movement speed
    public void increaseMoveSpeed(double increment) {
        this.moveSpeed += increment;
    }

    // Increases bullet speed (makes bullets faster)
    public void increaseBulletSpeed(int increment) {
        this.bulletSpeed += increment; // Increase by subtracting (since negative speed)
    }

    // Decreases shooting delay (makes shooting faster)
    public void decreaseShootingDelay(int decrement) {
        this.shootingDelay -= decrement; //  Decrease delay for faster shooting
        if (this.shootingDelay < 100) {
            this.shootingDelay = 100; // Minimum shooting delay is 100ms
        }
    }

    // reset bullet speed
    //Edit by inventory
    public void ResetBulletSpeed() {
        bulletSpeed = 5;
    }

    // Returns current health
    public int getHealth() {
        return this.health;
    }

    //  Returns current movement speed
    public double getMoveSpeed() {
        return this.moveSpeed;
    }

    // Returns current bullet speed
    public int getBulletSpeed() {
        return this.bulletSpeed;
    }

    //  Returns current shooting delay
    public int getShootingDelay() {
        return this.shootingDelay;
    }

    public boolean getCanShoot360() {
        return this.canShoot360;
    }

    // Prints player stats (for debugging)
    public void printStats() {
        System.out.println("Health: " + this.health);
        System.out.println("MoveSpeed: " + this.moveSpeed);
        System.out.println("BulletSpeed: " + this.bulletSpeed);
        System.out.println("ShootingDelay: " + this.shootingDelay + "ms");
    }
}
