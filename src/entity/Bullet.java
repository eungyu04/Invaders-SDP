package entity;

import java.awt.Color;

import engine.DrawManager;
import engine.DrawManager.SpriteType;
import inventory_develop.Bomb;

/**
 * Implements a bullet that moves vertically up or down.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Bullet extends Entity {

	/**
	 * Speed of the bulletX
	 */
	private int speedX;
	/**
	 * Speed of the bulletY, positive or negative depending on direction -
	 * positive is down.
	 */
	private int speedY;
	// Ctrl S
	/**
	 * Check if there is count on the bullet.
	 * if hit occur then checkCount will be false
	 */
	protected boolean checkCount;
	// Ctrl S
	/**
	 * give unique id for certain shot of bullets
	 */
	protected int fire_id;

	protected int BulletType;
	protected double angle;

	/**
	 * Constructor, establishes the bullet's properties.
	 * 
	 * @param positionX
	 *            Initial position of the bullet in the X axis.
	 * @param positionY
	 *            Initial position of the bullet in the Y axis.
	 * @param speedX
	 *			  Speed of the bullet in the X axis
	 * @param speedY
	 *            Speed of the bullet in the Y axis, positive or negative depending on
	 *            direction - positive is down.
	 */
	public Bullet(final int positionX, final int positionY, final int speedX, final int speedY, final int bulletType, final double angle) {
		super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);
		// CtrlS
		this.checkCount = true;
		// CtrlS
		this.speedX = speedX;
		this.speedY = speedY;
		this.BulletType = bulletType;
		this.angle = angle;
		setSprite();
	}


	/**
	 * Sets correct sprite for the bullet, based on speed.
	 */
	public final void setSprite() {

		if (BulletType == 0) {
			if(Bomb.getIsBomb() && Bomb.getCanShoot())
				this.spriteType = SpriteType.ItemBomb;
			else
				this.spriteType = SpriteType.Bullet;
		}
		else
			this.spriteType = SpriteType.EnemyBullet;
	}

	/**
	 * Updates the bullet's position.
	 */
	public final void update() {
		this.positionX += this.speedX;
		this.positionY += this.speedY;
	}

	/**
	 * Setter of the speed of the bullet.
	 *
	 * @param speedX
	 * 	 *        New speed of the bullet in the X axis.
	 * @param speedY
	 *            New speed of the bullet in the Y axis.
	 */
	public final void setSpeed(final int speedX, final int speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}

	/**
	 * Getter for the speedX of the bullet.
	 *
	 * @return Speed of the bullet in the X axis.
	 */
	public final int getSpeedX() {
		return this.speedX;
	}

	/**
	 * Getter for the speedY of the bullet.
	 * 
	 * @return Speed of the bullet in the Y axis.
	 */
	public final int getSpeedY() {
		return this.speedY;
	}

	/**
	 * Getter for the checkCount of the bullet.
	 *
	 * @return checkCount of the bullet.
	 */
	public final boolean isCheckCount() { return this.checkCount; }

	/**
	 * Setter for the checkCount of the bullet.
	 *
	 *  @param checkCount
	 * 	          New checkCount of the bullet.
	 */
	public final void setCheckCount(final boolean checkCount) { this.checkCount = checkCount; }

	/**
	 * Getter for the fire_id of the bullet
	 *
	 * @return Get fire_id of the bullet.
	 */
	public final int getFire_id() { return this.fire_id; }
	/**
	 * Setter for the fire_id of the bullet.
	 *
	 *  @param id
	 * 	          New fire_id of the bullet.
	 */
	public final void setFire_id(final int id) { this.fire_id = id; }

	public void setBulletType(int bulletType) { BulletType = bulletType; }
	public int getBulletType() { return BulletType; }

	public void setAngle(double angle) {this.angle = angle;}
	public double getAngle() {return angle;}

}
