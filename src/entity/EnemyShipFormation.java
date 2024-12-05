package entity;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.Timer;

import Enemy.*;
import HUDTeam.DrawManagerImpl;
import Sound_Operator.SoundManager;
import clove.ScoreManager;
import engine.*;
import inventory_develop.Bomb;
import inventory_develop.SpeedItem;
import screen.Screen;
import engine.DrawManager.SpriteType;

import static java.lang.Math.*;
import Enemy.PiercingBulletPool;
//Sound_Operator


/**
 * Groups enemy ships into a formation that moves together.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class EnemyShipFormation implements Iterable<EnemyShip> {
	private boolean isCircle;
	// Sound Operator
	private static SoundManager sm;
	/** Number of iteration of movement */
	private int iteration = 0;

	/** Initial position in the x-axis. */
	private static final int INIT_POS_X = 60;
	/** Initial position in the y-axis. */
	private static final int INIT_POS_Y = 100;
	/** Boss position in the x-axis. */
	private static int bossX;
	/** Boss position in the y-axis. */
	private static int bossY;
	/** Distance between ships. */
	private static final int SEPARATION_DISTANCE = 60;
	private static final int SEPARATION_DISTANCE_CIRCLE = 70;
	/** Radius of circle */
	private int RADIUS=0;
	private int MINIRADIUS= 0;
	private double angle;


    /** Proportion of C-type ships. */
	private static final double PROPORTION_C = 0.2;
	/** Proportion of B-type ships. */
	private static final double PROPORTION_B = 0.4;
	/** Lateral speed of the formation. */
	private static final int X_SPEED = 8;
	/** Downwards speed of the formation. */
	private static final int Y_SPEED = 4;
	/** SpeedX of the bullets shot by the members. */
	private static final int BULLET_SPEED = 4;
	/** Proportion of differences between shooting times. */
	private static final double SHOOTING_VARIANCE = .2;
	/** Margin on the sides of the screen. */
	private static final int SIDE_MARGIN = 20;
	/** Margin on the bottom of the screen. */
	private static final int BOTTOM_MARGIN = 80;
	/** Distance to go down each pass. */
	private static final int DESCENT_DISTANCE = 20;
	/** Minimum speed allowed. */
	private static final int MINIMUM_SPEED = 10;

	/** DrawManager instance. */
	private DrawManager drawManager;
	/** Application logger. */
	private Logger logger;
	/** Screen to draw ships on. */
	private Screen screen;

	/** List of enemy ships forming the formation. */
	private List<List<EnemyShip>> enemyShips;
	/** List of boss forming the formation. */
	private List<List<MiddleBoss>> middleBossList;
	/** List of boss forming the formation. */
	private List<List<FinalBoss>> finalBossList;
	/** Minimum time between shots. */
	private List<SpeedItem> activeSpeedItems;
	private List<Cooldown> shootingCooldown;
	private Cooldown enemyCooldown;
	/** Number of ships in the formation - horizontally. */
	private int nShipsWide;
	/** Number of ships in the formation - vertically. */
	private int nShipsHigh;
	/** Time between shots. */
	private int shootingInterval;
	/** Variance in the time between shots. */
	private int shootingVariance;
	/** Initial ship speed. */
	private int baseSpeed;
	/** Speed of the ships. */
	private int movementSpeed;
	/** Current direction the formation is moving on. */
	private Direction currentDirection;
	/** Direction the formation was moving previously. */
	private Direction previousDirection;
	/** Interval between movements, in frames. */
	private int movementInterval;
	/** Total width of the formation. */
	private int width;
	/** Total height of the formation. */
	private int height;
	/** Position in the x-axis of the upper left corner of the formation. */
	private int positionX;
	/** Position in the y-axis of the upper left corner of the formation. */
	private int positionY;
	/** Position in the x-axis of player ship. */
	private int shipPositionX;
	/** Position in the y-axis of player ship. */
	private int shipPositionY;
	/** Width of one ship. */
	private int shipWidth;
	/** Height of one ship. */
	private int shipHeight;
	/** List of ships that are able to shoot. */
	private List<EnemyShip> shooters;
	private List<MiddleBoss> middle_boss_shooting;
	private List<FinalBoss> final_boss_shooting;
	/** Number of not destroyed ships. */
	private int shipCount;
	/** level of the mode */
	private int level;
	/** middle Boss */
	private MiddleBoss middleBoss;
	/** final Boss */
	private FinalBoss finalBoss;

	private int enemyFrequency;
	private String gametype;

	private ScoreManager scoreManager; //add by team Enemy
	private ItemManager itemManager; //add by team Enemy

	private int index_x;
	private int index_y;

	/** Directions the formation can move. */
	private enum Direction {
		/** Movement to the right side of the screen. */
		RIGHT,
		/** Movement to the left side of the screen. */
		LEFT,
		/** Movement to the bottom of the screen. */
		DOWN
	};

	//add by team Enemy
	//Setting Up Score Manager and ItemManager
	public void setScoreManager (ScoreManager scoreManager){
		this.scoreManager = scoreManager;
	}
	public void setItemManager (ItemManager itemManager){//add by team Enemy
		this.itemManager = itemManager;
	}

	/**
	 * Constructor, sets the initial conditions.
	 *
	 * @param gameSettings
	 *            Current game settings.
	 */
	public EnemyShipFormation(final GameSettings gameSettings) {
		this.drawManager = Core.getDrawManager();
		this.logger = Core.getLogger();
		this.enemyShips = new ArrayList<>();
		this.middleBossList = new ArrayList<>();
		this.finalBossList = new ArrayList<>();
		this.activeSpeedItems = new ArrayList<>();
		this.currentDirection = Direction.RIGHT;
		this.movementInterval = 0;
		this.nShipsWide = gameSettings.getFormationWidth();
		this.nShipsHigh = gameSettings.getFormationHeight();
		this.shootingInterval = gameSettings.getShootingFrequency();
		this.shootingVariance = (int) (gameSettings.getShootingFrequency()
				* SHOOTING_VARIANCE);
		this.baseSpeed = gameSettings.getBaseSpeed();
		this.enemyFrequency = gameSettings.getEnemyFrequency();
		this.gametype = gameSettings.getGameType();
		this.level = gameSettings.getLevel();

		if (!(gametype.equals("Story") || gametype.equals("Normal"))){
			Core.getLogger().warning("game type is wrong type");
		}
		this.movementSpeed = this.baseSpeed;
		this.positionX = INIT_POS_X;
		this.positionY = INIT_POS_Y;
		this.shooters = new ArrayList<>();
		this.middle_boss_shooting = new ArrayList<>();
		this.final_boss_shooting = new ArrayList<>();
		this.shootingCooldown = new ArrayList<>();
		this.shipCount = 0;
		index_x = 0;
		index_y = 0;
		Random rand= new Random();
		int n = rand.nextInt(2);
		if(n%2==1 && (Objects.equals(gametype, "Normal"))){ isCircle=true;
			this.logger.info("circle"+ 2);
		}
		else isCircle=false;

		if(isCircle){
			RADIUS=gameSettings.getFormationHeight() * 6;
			MINIRADIUS= gameSettings.getFormationHeight() * 2;}

		this.logger.info("Initializing " + nShipsWide + "x" + nShipsHigh
				+ " ship formation in (" + positionX + "," + positionY + ")");

		// Each sub-list is a column on the formation.
		for (int i = 0; i < this.nShipsWide; i++){
			this.enemyShips.add(new ArrayList<>());
		}

		if (Objects.equals(gametype, "Normal")) {
			setEnemyShips();
		}
		else if (Objects.equals(gametype, "Story")){
			if (this.level == 4 || this.level == 8) {
				set_Story_Boss(this.level, gameSettings);
			} else {
				set_Story_Enemy();
			}
		}

		if (this.level != 4 && this.level != 8) {
			this.shipWidth = this.enemyShips.get(0).get(0).getWidth();
			this.shipHeight = this.enemyShips.get(0).get(0).getHeight();

			this.width = (this.nShipsWide - 1) * SEPARATION_DISTANCE
					+ this.shipWidth;
			this.height = (this.nShipsHigh - 1) * SEPARATION_DISTANCE
					+ this.shipHeight;
		}

		Bullet.setGameType(gametype);
		Bullet.setLevel(level);
	}

	private void setEnemyShips(){
		SpriteType spriteType = null;

		int hp = 1;

		for (List<EnemyShip> column : this.enemyShips) {
			int x;
			int y;
			for (int i = 0; i < this.nShipsHigh; i++) {
				double angle = 2 * PI * i / this.nShipsHigh;

				if (i / (float) this.nShipsHigh < PROPORTION_C)
					if (shipCount == (nShipsHigh * 1)+1 ||shipCount == (nShipsHigh*3)+1) //Edited by Enemy
						spriteType = SpriteType.ExplosiveEnemyShip1;
					else if (i / (float) this.nShipsHigh < PROPORTION_C)
						spriteType = SpriteType.EnemyShipC1;
					else if (i / (float) this.nShipsHigh < PROPORTION_B + PROPORTION_C)
						spriteType = SpriteType.EnemyShipB1;
					else
						spriteType = SpriteType.EnemyShipA1;
				if(isCircle){
					x = (int) round(RADIUS * cos(angle) + positionX + ( SEPARATION_DISTANCE_CIRCLE* this.enemyShips.indexOf(column)));
					y = (int) (RADIUS * sin(angle)) + positionY;}
				else{
					x = positionX + (SEPARATION_DISTANCE * this.enemyShips.indexOf(column));
					y = positionY+ i*SEPARATION_DISTANCE;
				}

				if(shipCount == nShipsHigh * (nShipsWide / 2))
					hp = 2; // Edited by Enemy, It just an example to insert EnemyShip that hp is 2.

				column.add(new EnemyShip(x, y, spriteType, hp, this.enemyShips.indexOf(column), i));// Edited by Enemy
				this.shipCount++;
				this.shooters.add(column.get(column.size() - 1));
				this.shootingCooldown.add(Core.getVariableCooldown(shootingInterval + 2000,
						shootingVariance));
				
				hp = 1;// Edited by Enemy
			}
		}
	}

	private void set_Story_Enemy(){
		SpriteType spriteType;

		int hp = 1;

		if ((int) (random() * 100) < 33) {
			spriteType = SpriteType.EnemyShipF1;
			hp = 3;
		}
		else if ((int) (random() * 100) < 66) {
			spriteType = SpriteType.EnemyShipE1;
			hp = 2;
		}
		else
			spriteType = SpriteType.EnemyShipD1;

		for (List<EnemyShip> column : this.enemyShips) {
			if (enemyShips.indexOf(column) == index_y){
				column.add(new EnemyShip((int)(random()*610) + 10, positionY, spriteType, hp, index_y, index_x));// Edited by Enemy
				this.shipCount++;
				this.shooters.add(column.get(column.size() - 1));

				// 몹 종류에 따른 공격속도(interval) 설정, 이 부분은 나중에 다른 값들이랑 같이 따로 클래스를 생성할 수도 있습니다.
				if (spriteType == SpriteType.EnemyShipF1){
					this.shootingCooldown.add(Core.getVariableCooldown(shootingInterval + 2000,
							shootingVariance));
				}
				else if (spriteType == SpriteType.EnemyShipE1){
					this.shootingCooldown.add(Core.getVariableCooldown(shootingInterval + 2000,
							shootingVariance));
				}
				else {
					this.shootingCooldown.add(Core.getVariableCooldown(shootingInterval + 2000,
							shootingVariance));
				}
			}
		}
		if (index_x < this.nShipsHigh) index_x++;
		else {
			index_y++;
			index_x = 0;
		}
	}

	private void set_Story_Boss(int level, GameSettings gameSettings) {
		bossY = INIT_POS_Y;
		this.shipCount = 1;

		if (level == 4) {
			middleBoss = new MiddleBoss(level, gameSettings);
		    List<MiddleBoss> middleBossColumn = new ArrayList<>();
			middleBossColumn.add(middleBoss);
			this.middleBossList.add(middleBossColumn);
    		this.middle_boss_shooting.add(middleBoss);
		} else {
			List<FinalBoss> finalBossColumn = new ArrayList<>();
			finalBoss = new FinalBoss(level, gameSettings);
			finalBossColumn.add(finalBoss);
			this.finalBossList.add(finalBossColumn);
			this.final_boss_shooting.add(finalBoss);
		}

		this.shootingCooldown.add(Core.getVariableCooldown(shootingInterval, shootingVariance));
	}

	/**
	 * Associates the formation to a given screen.
	 *
	 * @param newScreen
	 *            Screen to attach.
	 */
	public final void attach(final Screen newScreen) {
		screen = newScreen;
	}

	/**
	 * Draws every individual component of the formation.
	 */
	public final void draw() {
		for (List<EnemyShip> column : this.enemyShips)
			for (EnemyShip enemyShip : column) {
				drawManager.drawEntity(enemyShip, enemyShip.getPositionX(),
						enemyShip.getPositionY());
				DrawManagerImpl.drawEnemyHp(screen, enemyShip);
			}
	}

	/**
	 * Draws Boss.
	 */
	public final void draw_Story_Boss() {
		if (level == 4){
			MiddleBoss boss = middleBossList.get(0).get(0);
			boss.setSpriteType(SpriteType.middleBoss);
			drawManager.drawEntity(boss, boss.getPositionX(), boss.getPositionY());
		} else {
			FinalBoss boss = finalBossList.get(0).get(0);
			boss.setSpriteType(SpriteType.finalBoss);
			drawManager.drawEntity(boss, boss.getPositionX(), boss.getPositionY());
		}
	}

	/**
	 * Updates the position of the ships.
	 */
	public final void update() {

		if (Objects.equals(gametype, "Story")){
			if(this.enemyCooldown == null) {
				this.enemyCooldown = Core.getCooldown(enemyFrequency);
				this.enemyCooldown.reset();
			}
			if (this.enemyCooldown.checkFinished()) {
				if ( !(this.level == 4 || this.level == 8) ) {
					this.enemyCooldown.reset();
					set_Story_Enemy();
				}
			}
		}

		cleanUp();

		int movementX = 0;
		int movementY = 0;
		double remainingProportion = (double) this.shipCount
				/ (this.nShipsHigh * this.nShipsWide);
		this.movementSpeed = (int) (pow(remainingProportion, 2)
				* this.baseSpeed);
		this.movementSpeed += MINIMUM_SPEED;

		movementInterval++;
		if (movementInterval >= this.movementSpeed) {
			movementInterval = 0;

			int circleFormationPadding = 0;

			if (isCircle) {
				circleFormationPadding = 45;
			}

			boolean isAtBottom = positionY
					+ this.height + RADIUS > screen.getHeight() - BOTTOM_MARGIN;
			boolean isAtRightSide = positionX
					+ this.width + RADIUS >= screen.getWidth() - SIDE_MARGIN;
			boolean isAtLeftSide = positionX - RADIUS - circleFormationPadding <= SIDE_MARGIN;
			boolean isAtHorizontalAltitude = positionY % DESCENT_DISTANCE == 0;

			if (currentDirection == Direction.DOWN) {
				if (isAtHorizontalAltitude)
					if (previousDirection == Direction.RIGHT) {
						currentDirection = Direction.LEFT;
						this.logger.info("Formation now moving left 1");
					} else {
						currentDirection = Direction.RIGHT;
						this.logger.info("Formation now moving right 2");
					}
			} else if (currentDirection == Direction.LEFT) {
				if (isAtLeftSide)
					if (!isAtBottom) {
						previousDirection = currentDirection;
						currentDirection = Direction.DOWN;
						this.logger.info("Formation now moving down 3");
					} else {
						currentDirection = Direction.RIGHT;
						this.logger.info("Formation now moving right 4");
					}
			} else {
				if (isAtRightSide)
					if (!isAtBottom) {
						previousDirection = currentDirection;
						currentDirection = Direction.DOWN;
						this.logger.info("Formation now moving down 5");
					} else {
						currentDirection = Direction.LEFT;
						this.logger.info("Formation now moving left 6");
					}
			}

			if (currentDirection == Direction.RIGHT)
				movementX = X_SPEED;
			else if (currentDirection == Direction.LEFT)
				movementX = -X_SPEED;
			else
				movementY = Y_SPEED;


			positionX += movementX;
			positionY += movementY;

			// Cleans explosions.
			List<EnemyShip> destroyed;
			for (List<EnemyShip> column : this.enemyShips) {
				destroyed = new ArrayList<EnemyShip>();
				for (EnemyShip ship : column) {
					if (ship != null && ship.isDestroyed()) {
						destroyed.add(ship);
						this.logger.info("Removed enemy "
								+ column.indexOf(ship) + " from column "
								+ this.enemyShips.indexOf(column));
					}
				}
				for (EnemyShip destroy : destroyed){
					shootingCooldown.remove(shooters.indexOf(destroy));
					shooters.remove(destroy);
				}
				column.removeAll(destroyed);
			}
			double angle = (PI/this.nShipsHigh);
			int temp;
			iteration++;
			for (List<EnemyShip> column : this.enemyShips){
				temp=0;
				for (EnemyShip enemyShip : column) {
					double currentAngle = angle * (temp+iteration);
					int distanceX = movementX + (int) (MINIRADIUS * cos(currentAngle));
					int distanceY = movementY + (int) (MINIRADIUS * sin(currentAngle));

					if (distanceX + enemyShip.positionX > screen.getWidth() - SIDE_MARGIN || distanceX + enemyShip.positionX < SIDE_MARGIN) {
						distanceX = 0;

					} else if (distanceY + enemyShip.positionY > screen.getHeight() - BOTTOM_MARGIN) {
						distanceY = 0;
					}

					if (!(gametype.equals("Story") && (level == 4 || level == 8))) {
						enemyShip.move(
								distanceX,
								distanceY
						);
					}
					enemyShip.update();
					temp++;
				}
			}
		}
	}

	/**
	 * Cleans empty columns, adjusts the width and height of the formation.
	 */
	private void cleanUp() {
		Set<Integer> emptyColumns = new HashSet<Integer>();
		int maxColumn = 0;
		int minPositionY = Integer.MAX_VALUE;
		for (List<EnemyShip> column : this.enemyShips) {
			if (!column.isEmpty()) {
				// Height of this column
				int columnSize = column.get(column.size() - 1).positionY
						- this.positionY + this.shipHeight;
				maxColumn = max(maxColumn, columnSize);
				minPositionY = min(minPositionY, column.get(0)
						.getPositionY());
			} else {
				// Empty column, we remove it.
				emptyColumns.add(this.enemyShips.indexOf(column));
			}
		}
		for (int index : emptyColumns) {
			this.enemyShips.remove(index);
			logger.info("Removed column " + index);
		}

		int leftMostPoint = 0;
		int rightMostPoint = 0;

		for (List<EnemyShip> column : this.enemyShips) {
			if (!column.isEmpty()) {
				if (leftMostPoint == 0)
					leftMostPoint = column.get(0).getPositionX();
				rightMostPoint = column.get(0).getPositionX();
			}
		}

		this.width = rightMostPoint - leftMostPoint + this.shipWidth;
		this.height = maxColumn;

		this.positionX = leftMostPoint;
		this.positionY = minPositionY;
	}

	/**
	 * Shoots a bullet downwards.
	 *
	 * @param bullets
	 *            Bullets set to add the bullet being shot.
	 */
	public final void shoot(final Set<PiercingBullet> bullets) { // Edited by Enemy
		// For now, only ships in the bottom row are able to shoot.
		for (Cooldown shoot : this.shootingCooldown) {
			if (!gametype.equals("Story") || !(level == 4 || level == 8)) {
				if (!shooters.isEmpty() && shoot.checkFinished()) {
					EnemyShip shooter = this.shooters.get(shootingCooldown.indexOf(shoot));
					shoot.reset();

					// 몹 종류에 따른 공격방식 설정, 이 부분은 나중에 다른 값들이랑 같이 따로 클래스를 생성할 수도 있습니다.
					double speed;
					int bulletNum;	// 총알 갯수
					int bulletType;	// 총알 타입(bullet의 spriteType설정을 위해)

					// type 1) 속도 보통, 총알 3개
					if (shooter.getSpriteType() == SpriteType.EnemyShipD1 || shooter.getSpriteType() == SpriteType.EnemyShipD2){  //임시 기믹 변경 예정
						angle = 1.5708;
						speed = BULLET_SPEED;
						bulletNum = 3;
						bulletType = 2;
					}
					// type 2) 속도 느림, 총알 5개
					else if (shooter.getSpriteType() == SpriteType.EnemyShipE1 || shooter.getSpriteType() == SpriteType.EnemyShipE2){
						angle = 1.5708;
						speed = (double) (BULLET_SPEED * 2) / 3;
						bulletNum = 5;
						bulletType = 2;
					}
					// type 3) 속도 빠름, 총알 1개
					else if (shooter.getSpriteType() == SpriteType.EnemyShipF1 || shooter.getSpriteType() == SpriteType.EnemyShipF2){
						angle = Math.atan2(shipPositionY - shooter.getPositionY(), shipPositionX - shooter.getPositionX());
						speed = (double) (BULLET_SPEED * 3) / 2;
						bulletNum = 1;
						bulletType = 3;
					}
					else {
						angle = 1.5708;
						speed = BULLET_SPEED;
						bulletNum = 1;
						bulletType = 1;
					}

					shootByType(bullets, shooter, speed, bulletNum, bulletType);
				}
			} else if (( !middle_boss_shooting.isEmpty() || !final_boss_shooting.isEmpty() ) && shoot.checkFinished()) {
				shoot.reset();

				int middleBoss_left_hand_x = 260;
				int middleBoss_left_hand_y = 170;
				int middleBoss_right_hand_x = 345;
				int middleBoss_right_hand_y = 170;

				int finalBoss_left_hand_x = 240;
				int finalBoss_left_hand_y = 190;
				int finalBoss_right_hand_x = 365;
				int finalBoss_right_hand_y = 190;

				double angle1 = Math.atan2(shipPositionY - middleBoss_left_hand_y,
						shipPositionX - middleBoss_left_hand_x);
                double angle2 = Math.atan2(shipPositionY - middleBoss_right_hand_y,
						shipPositionX - middleBoss_right_hand_x);
				double angle3 = Math.atan2(shipPositionY - finalBoss_left_hand_y,
						shipPositionX - finalBoss_left_hand_x);
				double angle4 = Math.atan2(shipPositionY - finalBoss_right_hand_y,
						shipPositionX - finalBoss_right_hand_x);


				if (this.level == 4) {
					sm.playES("middleBoss_Shot"); // 보스 총소리 차별화가 필요할 거 같네요

					Boss boss = middleBoss;

					bullets.add(PiercingBulletPool.getPiercingBullet(
							middleBoss_left_hand_x, middleBoss_left_hand_y,
							(int) (BULLET_SPEED * Math.cos(angle1)),
							(int) (BULLET_SPEED * Math.sin(angle1)),
							0, 4,
                            angle1, 1));

					bullets.add(PiercingBulletPool.getPiercingBullet(
							middleBoss_right_hand_x, middleBoss_right_hand_y,
							(int) (BULLET_SPEED * Math.cos(angle2)),
							(int) (BULLET_SPEED * Math.sin(angle2)),
							0, 4,
                            angle2, 1));
					if (!boss.isRaining()) {
						boss.rain_of_fire();
					}
				} else {	// this.level == 8
				sm.playES("finalBoss_Shot");

					Boss boss = finalBoss;

					bullets.add(PiercingBulletPool.getPiercingBullet(
							finalBoss_left_hand_x, finalBoss_left_hand_y,
							(int) (BULLET_SPEED * Math.cos(angle3)),
							(int) (BULLET_SPEED * Math.sin(angle3)),
							0, 4,
                            angle3, 1));

					bullets.add(PiercingBulletPool.getPiercingBullet(
							finalBoss_right_hand_x, finalBoss_right_hand_y,
							(int) (BULLET_SPEED * Math.cos(angle4)),
							(int) (BULLET_SPEED * Math.sin(angle4)),
							0, 4,
                            angle4, 1));

					if (!boss.isRaining()) {
						boss.rain_of_fire();
					}
				}
			}
		}
	}

	private void shootByType(final Set<PiercingBullet> bullets, EnemyShip shooter, double speed, int bulletNum, int bulletType) {
		sm.playES("Enemy_Gun_Shot_1_ES");

		for (int i = 0; i < bulletNum; i++) {
			double adjustedAngle = angle - (Math.toRadians(25) * (bulletNum / 2 - i)); // bullet의 개수에 따라 대칭적으로 각도 조절

			bullets.add(PiercingBulletPool.getPiercingBullet(
					shooter.getPositionX() + shooter.width / 2,
					shooter.getPositionY(),
					(int) (speed * Math.cos(adjustedAngle)),
					(int) (speed * Math.sin(adjustedAngle)),
					0,
					bulletType,
					adjustedAngle,
					1)); // Edited by Enemy
		}
	}

	/**
	 * Destroys a ship.
	 *
	 * @param destroyedShip
	 *            Ship to be destroyed.
	 */
	public final void destroy(final EnemyShip destroyedShip) {
			if (Bomb.getIsBomb()) {		// team Inventory
				Bomb.destroyByBomb(enemyShips, destroyedShip, this.itemManager , this.logger);
			} else {
				for (List<EnemyShip> column : this.enemyShips)
					for (int i = 0; i < column.size(); i++)
						if (column.get(i).equals(destroyedShip)) {
							column.get(i).destroy();
							this.logger.info("Destroyed ship in ("
									+ this.enemyShips.indexOf(column) + "," + i + ")");
						}
			}

		this.shipCount--;
	}

	/**
	 * Returns an iterator over the ships in the formation.
	 *
	 * @return Iterator over the enemy ships.
	 */
	@Override
	public final Iterator<EnemyShip> iterator() {
		Set<EnemyShip> enemyShipsList = new HashSet<>();
		for (List<EnemyShip> column : this.enemyShips) {
			for (EnemyShip enemyShip : column) {
				enemyShipsList.add(enemyShip);
			}
		}
		return enemyShipsList.iterator();
	}

	/**
	 * Checks if there are any ships remaining.
	 *
	 * @return True when all ships have been destroyed.
	 */
	public final boolean isEmpty() {
		return this.shipCount <= 0;
	}

	/**
	 * When EnemyShip is hit, its HP decrease by 1, and if the HP reaches 0, the ship is destroyed.
	 *
	 * @param bullet
	 *            Player's bullet
	 * @param destroyedShip
	 *            Ship to be hit
	 * @param isChainExploded
	 *            True if enemy ship is chain exploded
	 */
	public final int[] _destroy(final Bullet bullet, final EnemyShip destroyedShip, boolean isChainExploded) {// Edited by Enemy team
		int count = 0;   // number of destroyed enemy
		int point = 0;  // point of destroyed enemy

		// Checks if this ship is 'chainExploded' due to recursive call
		if (isChainExploded
				&& !destroyedShip.spriteType.equals(SpriteType.ExplosiveEnemyShip1)
				&& !destroyedShip.spriteType.equals(SpriteType.ExplosiveEnemyShip2)){
			destroyedShip.chainExplode();
		}

		if (bullet.getSpriteType() == SpriteType.ItemBomb && isCircle) {	// Bomb Item type1
			int[] score = Bomb.destroyByBomb_isCircle(enemyShips, destroyedShip, this.itemManager, this.logger);
			count = score[0];
			point = score[1];
		} else if (bullet.getSpriteType() == SpriteType.ItemBomb) {		// Bomb Item type2
			int[] score = Bomb.destroyByBomb(enemyShips, destroyedShip, this.itemManager, this.logger);
			count = score[0];
			point = score[1];
		} else {
			for (List<EnemyShip> column : this.enemyShips) // Add by team Enemy
				for (int i = 0; i < column.size(); i++) {
					if (column.get(i).equals(destroyedShip)) {
						switch (destroyedShip.spriteType){
							case ExplosiveEnemyShip1:
							case ExplosiveEnemyShip2:
//								HpEnemyShip.hit(destroyedShip, bullet);

								//Sound_Operator
								if (destroyedShip.isDestroyed()) {

									sm.playES("enemy_explosion");
								}
								point += destroyedShip.getPointValue();
								int[] point_mob =  explosive(destroyedShip.getX(), destroyedShip.getY(),
										this.enemyShips.indexOf(column),i,this.enemyShips); // Edited by team Enemy
								point += point_mob[0];
								count += point_mob[1]+1;
								if(isChainExploded){
									this.scoreManager.addScore(point-destroyedShip.getPointValue());
								}
								this.logger.info("Destroyed ExplosiveEnemyship in ("
										+ this.enemyShips.indexOf(column) + "," + i + ")");

								break;
							default:
//								HpEnemyShip.hit(destroyedShip, bullet);

								if(destroyedShip.getHp() > 0 ){
									this.logger.info("Enemy ship lost 1 HP in ("
											+ this.enemyShips.indexOf(column) + "," + i + ")");
								}else{
									this.logger.info("Destroyed ship in ("
											+ this.enemyShips.indexOf(column) + "," + i + ")");
									point = column.get(i).getPointValue();
									count += 1;
								}
								break;
						}
						if (column.get(i).getColor().equals(Color.MAGENTA)) { //add by team enemy
							if (!Objects.equals(gametype, "Story"))
								this.itemManager.dropItem(destroyedShip, 1, 1);
						}



					}
				}
		}

		this.shipCount -= count;

		int[] returnValue = {count, point};
		return returnValue;
	}

	/**
	 * A function that explosive up, down, left, and right when an explosive EnemyShip dies
	 *
	 * @param x
	 *            explosive EnemyShip's Initial x-coordinates
	 * @param y
	 *            explosive EnemyShip's Initial y-coordinates
	 * @param index_x
	 *            explosive EnemyShip's x-coordinates in EnemyShips
	 * @param index_y
	 * 			  explosive EnemyShip's y-coordinates in EnemyShips
	 * @param enemyShips
	 * 			  the current arrangement of the enemy
	 */
	public int[] explosive(final int x, final int y, final int index_x, final int index_y, List<List<EnemyShip>> enemyShips){


		Queue<EnemyShip> targetShipQ = new LinkedList<>();
		Timer timer = new Timer(500, null);
		int range = 2;
		int i = 1;
		int point = 0;
		int mob = 0;

		Bullet bullet = new Bullet(0,0, 0,-1, 1, 0, 1);

		do{

			if(index_x+i >= 0 && enemyShips.size() > index_x+i && enemyShips.get(index_x+i).size() > y){ // right
				EnemyShip targetShip = enemyShips.get(index_x+i).get(y);
				if (!targetShip.isDestroyed()) {
					if (targetShip.getX() == x + i && targetShip.getY() == y) {
						targetShipQ.add(targetShip);
						point += targetShip.getPointValue();
						mob += 1;

					}
				}
			}

			if( index_x-i >= 0 && enemyShips.size() > index_x-i && enemyShips.get(index_x-i).size() > y) { // left
				EnemyShip targetShip = enemyShips.get(index_x - i).get(y);
				if(!targetShip.isDestroyed()) {
					if (targetShip.getX() == x - i && targetShip.getY() == y) {
						targetShipQ.add(targetShip);
						point += targetShip.getPointValue();
						mob += 1;

					}
				}
			}

			if(index_y-i >= 0){//up
				EnemyShip targetShip = enemyShips.get(index_x).get(index_y-i);
				if (!targetShip.isDestroyed()) {
					if (targetShip.getX() == x && targetShip.getY() == y - i) {
						targetShipQ.add(targetShip);
						point += targetShip.getPointValue();
						mob += 1;
					}
				}
			}

			if(enemyShips.get(index_x).size() > index_y+i){//down
				EnemyShip targetShip = enemyShips.get(index_x).get(index_y+i);
				if(!targetShip.isDestroyed()) {
					if (targetShip.getX() == x && targetShip.getY() == y + i) {
						targetShipQ.add(targetShip);
						point += targetShip.getPointValue();
						mob += 1;


					}
				}
			}

			targetShipQ.add(new EnemyShip());

			i++;
		}
		while(i <= range);

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {



				while(true){
					if (!targetShipQ.isEmpty()) {
						EnemyShip targetShip = targetShipQ.poll();
						if(targetShip.getX() != -2){
							_destroy(bullet,targetShip,true);
						} else{
							break;
						}

					} else{
						break;
					}
				}

				if (targetShipQ.isEmpty())
					((Timer) e.getSource()).stop();

			}
		};

		timer.addActionListener(listener);
		timer.start();

		return new int[]{point, mob};
	}
	public final void BecomeCircle(boolean isCircle){
		this.isCircle = isCircle;
	}
	public void setShipPosition(int shipPositionX, int shipPositionY){
		this.shipPositionX = shipPositionX;
		this.shipPositionY = shipPositionY;
	}

	public Boss getBoss() {
		if (this.middleBoss != null)
			return middleBoss;
		else if (this.finalBoss != null)
			return finalBoss;
		return null;
	}
	// for test
	public void setSoundManager(SoundManager soundManager) {
		sm = soundManager;
		if (sm == null) {
			sm = SoundManager.getInstance();
		}
	}
	public List<Cooldown> getShootingCooldown() {
		return shootingCooldown;
	}
	public List<EnemyShip> getShooters() {
		return shooters;
	}
}