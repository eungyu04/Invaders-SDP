package entity;

import java.awt.Color;
import java.util.Set;

import Enemy.PiercingBullet;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import Enemy.PiercingBulletPool;
// Import PlayerGrowth class
import Enemy.PlayerGrowth;
// Import ShipStatu class
import inventory_develop.ShipStatus;
/**
 * Implements a ship, to be controlled by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Ship extends Entity {

	private static final ShipStatus shipstatus = new ShipStatus(2,750,-6);
	/** Time between shots. */
	private int SHOOTING_INTERVAL = shipstatus.getSHOOTING_INTERVAL();
	/** Speed of the bullets shot by the ship. */
	private int BULLET_SPEED = shipstatus.getBULLET_SPEED();
	/** Movement of the ship for each unit of time. */
	private int SPEED = shipstatus.getSpeed();
	
	/** Minimum time between shots. */
	private Cooldown shootingCooldown;
	/** Time spent inactive between hits. */
	private Cooldown destructionCooldown;
	/** PlayerGrowth 인스턴스 / PlayerGrowth instance */
	private PlayerGrowth growth;

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	//Edit by Enemy
	public Ship(final int positionX, final int positionY) {
		super(positionX, positionY, 13 * 2, 8 * 2, Color.GREEN);

		this.spriteType = SpriteType.Ship;

		// Create PlayerGrowth object and set initial stats
		this.growth = new PlayerGrowth();  // PlayerGrowth 객체를 먼저 초기화

		//  Now use the initialized growth object
		this.shootingCooldown = Core.getCooldown(growth.getShootingDelay());

		this.destructionCooldown = Core.getCooldown(1000);
	}
	/* Status_update */
	public void SetStatus(final int Speed_Level, final int SHOOTING_INTERVAL_Level,
								final int BULLET_SPEED_Level) {
		shipstatus.SetStatus(Speed_Level,SHOOTING_INTERVAL_Level, BULLET_SPEED_Level);
		this.SHOOTING_INTERVAL = shipstatus.getSHOOTING_INTERVAL();
		this.BULLET_SPEED = shipstatus.getBULLET_SPEED();
		this.SPEED = shipstatus.getSpeed();
	}



	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 */
	public final void moveRight() {
		this.positionX += growth.getMoveSpeed(); //  Use PlayerGrowth for movement speed
	} //Edit by Enemy


	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public final void moveLeft() {
		this.positionX -= growth.getMoveSpeed(); // Use PlayerGrowth for movement speed
	} //Edit by Enemy

	/**
	 * Shoots a bullet upwards.
	 * 
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 *
	 * You can set Number of enemies the bullet can pierce at here.
	 */
	//Edit by Enemy
	public final boolean shoot(final Set<PiercingBullet> bullets) {
		// Do not reset cooldown every time
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset(); // Reset cooldown after shooting

			// Add a piercing bullet fired by the player's ship.
			bullets.add(PiercingBulletPool.getPiercingBullet(
					positionX + this.width / 2,
					positionY,
					growth.getBulletSpeed(), // Use PlayerGrowth for bullet speed
					2 // Number of enemies the bullet can pierce
			));
			return true;
		}
		return false;
	}




	/**
	 * Updates status of the ship.
	 */
	public final void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = SpriteType.Ship;
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public final void destroy() {
		this.destructionCooldown.reset();
	}

	/**
	 * Checks if the ship is destroyed.
	 * 
	 * @return True if the ship is currently destroyed.
	 */
	public final boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
	}
	/**
	 * 스탯을 증가시키는 메서드들 (PlayerGrowth 클래스 사용)
	 * Methods to increase stats (using PlayerGrowth)
	 */

	// Increases health
	//Edit by Enemy
	public void increaseHealth(int increment) {
		growth.increaseHealth(increment);
	}

	//  Increases movement speed
	//Edit by Enemy
	public void increaseMoveSpeed(int increment) {
		growth.increaseMoveSpeed(increment);
	}

	// Increases bullet speed
	//Edit by Enemy
	public void increaseBulletSpeed(int increment) {
		growth.increaseBulletSpeed(increment);
	}

	//  Decreases shooting delay
	//Edit by Enemy
	public void decreaseShootingDelay(int decrement) {
		growth.decreaseShootingDelay(decrement);
		this.shootingCooldown = Core.getCooldown(growth.getShootingDelay()); // Apply new shooting delay
	}

	/**
	 * Getter for the ship's speed.
	 * 
	 * @return Speed of the ship.
	 */
	public final int getSpeed() {
		return SPEED;
	}
}
