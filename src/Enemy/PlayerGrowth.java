package Enemy;

public class PlayerGrowth {

    //Player's base stats
    private int health;          //Health
    private int moveSpeed;       //Movement speed
    private int bulletSpeed;     // Bullet speed
    private int shootingDelay;   // Shooting delay

    //Constructor to set initial values
    public PlayerGrowth() {
        //  Base stat values
        this.health = 3;          //  Base health is 3
        this.moveSpeed = 2;       // Base movement speed is 2
        this.bulletSpeed = -8;    //  Base bullet speed is -6
        this.shootingDelay = 750; //  Base shooting delay is 750ms
    }

    // Increases health
    public void increaseHealth(int increment) {
        this.health += increment;
    }

    //Increases movement speed
    public void increaseMoveSpeed(int increment) {
        this.moveSpeed += increment;
    }

    // Increases bullet speed (makes bullets faster)
    public void increaseBulletSpeed(int increment) {
        this.bulletSpeed -= increment; // Increase by subtracting (since negative speed)
    }

    // Decreases shooting delay (makes shooting faster)
    public void decreaseShootingDelay(int decrement) {
        this.shootingDelay -= decrement; //  Decrease delay for faster shooting
        if (this.shootingDelay < 100) {
            this.shootingDelay = 100; // Minimum shooting delay is 100ms
        }
    }

    // Returns current health
    public int getHealth() {
        return this.health;
    }

    //  Returns current movement speed
    public int getMoveSpeed() {
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

    // Prints player stats (for debugging)
    public void printStats() {
        System.out.println("Health: " + this.health);
        System.out.println("MoveSpeed: " + this.moveSpeed);
        System.out.println("BulletSpeed: " + this.bulletSpeed);
        System.out.println("ShootingDelay: " + this.shootingDelay + "ms");
    }
}
