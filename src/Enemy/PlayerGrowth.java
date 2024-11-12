package Enemy;

import engine.Core;
import java.io.IOException;

public class PlayerGrowth {

    //Player's base stats
    private int health;          //Health
    private static double moveSpeed = 1.5;       //Movement speed
    private static int bulletSpeedX = 0;      // Bullet speedX
    private static int bulletSpeedY = -4;     // Bullet speedY
    private static int shootingDelay = 750;   // Shooting delay

    //Constructor to set initial values
    public PlayerGrowth() {//  Base shooting delay is 750ms

        // CtrlS: set player growth based on upgrade_status.properties
        try {
            moveSpeed = Core.getUpgradeManager().getMovementSpeed();
            shootingDelay = Core.getUpgradeManager().getAttackSpeed();
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
    public void increaseBulletSpeedX(int increment) {
        this.bulletSpeedX -= increment; // Increase by subtracting (since negative speed)
    }
    public void increaseBulletSpeedY(int increment) {
        this.bulletSpeedY -= increment; // Increase by subtracting (since negative speed)
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
    public void ResetBulletSpeed(){
        bulletSpeedX = 0;
        bulletSpeedY = -4;
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
    public int getBulletSpeedX() {
        return this.bulletSpeedX;
    }
    public int getBulletSpeedY() {
        return this.bulletSpeedY;
    }

    //  Returns current shooting delay
    public int getShootingDelay() {
        return this.shootingDelay;
    }

    // Prints player stats (for debugging)
    public void printStats() {
        System.out.println("Health: " + this.health);
        System.out.println("MoveSpeed: " + this.moveSpeed);
        System.out.println("BulletSpeedX: " + this.bulletSpeedX);
        System.out.println("BulletSpeedY: " + this.bulletSpeedY);
        System.out.println("ShootingDelay: " + this.shootingDelay + "ms");
    }
}
