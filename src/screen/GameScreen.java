package screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

import java.io.IOException;

import CtrlS.RoundState;
import clove.AchievementConditions;
import clove.Statistics;
import Enemy.*;
import HUDTeam.DrawAchievementHud;
import HUDTeam.DrawManagerImpl;
import engine.*;
import entity.*;
// shield and heart recovery
import inventory_develop.*;
// Sound Operator
import Sound_Operator.SoundManager;
import clove.ScoreManager;    // CLOVE
import twoplayermode.TwoPlayerMode;

import javax.swing.*;


/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen {

	/** Milliseconds until the screen accepts user input. */
	private static final int INPUT_DELAY = 6000;
	/** Bonus score for each life remaining at the end of the level. */
	private static final int LIFE_SCORE = 100;
	/** Minimum time between bonus ship's appearances. */
	private static final int BONUS_SHIP_INTERVAL = 20000;
	/** Maximum variance in the time between bonus ship's appearances. */
	private static final int BONUS_SHIP_VARIANCE = 10000;
	/** Time until bonus ship explosion disappears. */
	private static final int BONUS_SHIP_EXPLOSION = 500;
	/** Time until middle boss explosion disappears. */
	private static final int MIDDLE_BOSS_EXPLOSION = 500;
	/** Time until final boss explosion disappears. */
	private static final int FINAL_BOSS_EXPLOSION = 500;
	/** Time from finishing the level to screen change. */
	private static final int SCREEN_CHANGE_INTERVAL = 1500;
	/** Height of the interface separation line. */
	private static final int SEPARATION_LINE_HEIGHT = 40;
	private static final int SEPARATION_LINE_HEIGHT_DOWN = 635;

	/** Current game difficulty settings. */
	private GameSettings gameSettings;
	/** Current difficulty level number. */
	private int level;
	/** Formation of enemy ships. */
	private EnemyShipFormation enemyShipFormation;
	/** Player's ship. */
	private Ship ship;
	public Ship player2;
	/** Bonus enemy ship that appears sometimes. */
	private EnemyShip enemyShipSpecial;
	/** middle boss */
	private Boss midBoss;
	/** final boss */
	private Boss finBoss;
	/** Minimum time between bonus ship appearances. */
	private Cooldown enemyShipSpecialCooldown;
	/** Time until bonus ship explosion disappears. */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/** Time until middle boss explosion disappears. */
	private Cooldown middleBossExplosionCooldown;
	/** Time until final boss explosion disappears. */
	private Cooldown finalBossExplosionCooldown;
	/** Time from finishing the level to screen change. */
	private Cooldown screenFinishedCooldown;
	/** Set of all bullets fired by on screen ships. */
	public Set<PiercingBullet> bullets; //by Enemy team
	/** Add an itemManager Instance */
	public static ItemManager itemManager; //by Enemy team
	/** Shield item */
	private ItemBarrierAndHeart item;   // team Inventory
	private FeverTimeItem feverTimeItem;
	/** Speed item */
	private SpeedItem speedItem;
	/** Current score. */
	private int score;
	/** Player lives left. */
	private int lives;
	/** player2 lives left.*/
	public int livesTwo;
	/** Total bullets shot by the player. */
	private int bulletsShot;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed;
	/** Moment the game starts. */
	private long gameStartTime;
	/** Checks if the level is finished. */
	private static boolean levelFinished;
	/** Checks if a bonus life is received. */
	private boolean bonusLife;
	/**
	 * Added by the Level Design team
	 *
	 * Counts the number of waves destroyed
	 * **/
	private int waveCounter;

	/** ### TEAM INTERNATIONAL ### */
	/** Booleans for horizontal background movement */
	private boolean backgroundMoveLeft = false;
	private boolean backgroundMoveRight = false;



	// --- OBSTACLES
	public Set<Obstacle> obstacles; // Store obstacles
	private Cooldown obstacleSpawnCooldown; //control obstacle spawn speed
	/** Shield item */


	// Soomin Lee / TeamHUD
	/** Moment the user starts to play */
	private long playStartTime;
	/** Total time to play */
	private int playTime = 0;
	/** Play time on previous levels */
	private int playTimePre = 0;

	// Sound Operator
	private static SoundManager sm;

	// Team-Ctrl-S(Currency)
	/** Total coin **/
	private int coin;
	/** Total gem **/
	private int gem;
	/** Total hitCount **/      //CtrlS
	private int hitCount;
	/** Unique id for shot of bullets **/ //CtrlS
	private int fire_id;
	/** Unique id for shot of previous bullets **/
	private int fire_id_pre;
	/** Set of fire_id **/
	private Set<Integer> processedFireBullet;

	/** Score calculation. */
	private ScoreManager scoreManager;    //clove
	/** Check start-time*/
	private long startTime;    //clove
	/** Check end-time*/
	private long endTime;    //clove

	private Statistics statistics; //Team Clove
	private AchievementConditions achievementConditions;
	private int fastKill;
	private int feverSpecialScore;
	/** CtrlS: Count the number of coin collected in game */
	private int coinItemsCollected;

	//스토리라운드
	private static final long ROUND_CLEAR_DURATION =(7000+15000); // 15초 (5 + 00 + 2)
	private long roundStartTime;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param gameState
	 *            Current game state.
	 * @param gameSettings
	 *            Current game settings.
	 * @param bonusLife
	 *            Checks if a bonus life is awarded this level.
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public GameScreen(final GameState gameState,
					  final GameSettings gameSettings, final boolean bonusLife,
					  final int width, final int height, final int fps, int returnCode) {
		super(width, height, fps);

		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife;
		this.level = gameState.getLevel();
		this.score = gameState.getScore();
		this.lives = gameState.getLivesRemaining();
		if (this.bonusLife)
			this.lives++;
		this.livesTwo = gameState.getLivesTwoRemaining();
		if (this.bonusLife)
			this.livesTwo++;
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
		this.item = new ItemBarrierAndHeart();   // team Inventory
		this.feverTimeItem = new FeverTimeItem(); // team Inventory
		this.speedItem = new SpeedItem();   // team Inventory
		this.coin = gameState.getCoin(); // Team-Ctrl-S(Currency)
		this.gem = gameState.getGem(); // Team-Ctrl-S(Currency)
		this.hitCount = gameState.getHitCount(); //CtrlS
		this.fire_id = 0; //CtrlS - fire_id means the id of bullet that shoot already. It starts from 0.
		this.processedFireBullet = new HashSet<>(); //CtrlS - initialized the processedFireBullet
		/**
		 * Added by the Level Design team
		 *
		 * Sets the wave counter
		 * **/
		this.waveCounter = 1;

		// Soomin Lee / TeamHUD
		this.playTime = gameState.getTime();
		this.scoreManager = gameState.scoreManager; //Team Clove
		this.statistics = new Statistics(); //Team Clove
		this.achievementConditions = new AchievementConditions();
		this.coinItemsCollected = gameState.getCoinItemsCollected(); // CtrlS
		this.returnCode = returnCode;

		levelFinished = false;
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public void initialize() {
		super.initialize();
		/** initialize background **/
		drawManager.loadBackground(this.level, this.returnCode);

		this.gameSettings.setLevel(level);
		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.setSoundManager(sm);
		enemyShipFormation.setScoreManager(this.scoreManager);//add by team Enemy
		enemyShipFormation.attach(this);
		this.ship = new Ship(this.width / 2 - 12, this.height - 30, Color.RED); // add by team HUD
		this.midBoss = enemyShipFormation.getBoss();
		this.finBoss = enemyShipFormation.getBoss();

		/** initialize itemManager */
		this.itemManager = new ItemManager(this.height, drawManager, this); //by Enemy team
		this.itemManager.initialize(); //by Enemy team
		enemyShipFormation.setItemManager(this.itemManager);//add by team Enemy
		this.player2=null;

		Set<EnemyShip> enemyShipSet = new HashSet<>();
		for (EnemyShip enemyShip : this.enemyShipFormation) {
			enemyShipSet.add(enemyShip);
		}
		this.itemManager.setEnemyShips(enemyShipSet);

		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(BONUS_SHIP_EXPLOSION);
		this.middleBossExplosionCooldown = Core.getCooldown(MIDDLE_BOSS_EXPLOSION);
		this.finalBossExplosionCooldown = Core.getCooldown(FINAL_BOSS_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<PiercingBullet>(); // Edited by Enemy

		this.startTime = System.currentTimeMillis();    //clove

		// Special input delay / countdown.
		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();

		// Soomin Lee / TeamHUD
		this.playStartTime = gameStartTime + INPUT_DELAY;
		this.playTimePre = playTime;


		//    // --- OBSTACLES - Initialize obstacles
		this.obstacles = new HashSet<>();
		this.obstacleSpawnCooldown = Core.getCooldown(Math.max(2000 - (level * 200), 500)); // Minimum 0.5s

	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score += LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.scoreManager.getAccumulatedScore());

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected void update() {
		super.update();

		if (this.inputDelay.checkFinished() && !levelFinished) {
			// --- OBSTACLES
			if (this.obstacleSpawnCooldown.checkFinished()) {
				// Adjust spawn amount based on the level
				int spawnAmount = Math.min(level, 3); // Spawn up to 3 obstacles at higher levels
				for (int i = 0; i < spawnAmount; i++) {
					int randomX = new Random().nextInt(this.width - 30);
					obstacles.add(new Obstacle(randomX, 50)); // Start each at the top of the screen
				}
				this.obstacleSpawnCooldown.reset();
			}

			// --- OBSTACLES
			Set<Obstacle> obstaclesToRemove = new HashSet<>();
			for (Obstacle obstacle : this.obstacles) {
				obstacle.update(this.level); // Make obstacles move or perform actions
				if (obstacle.shouldBeRemoved()) {
					obstaclesToRemove.add(obstacle);  // Mark obstacle for removal after explosion
				}
			}
			this.obstacles.removeAll(obstaclesToRemove);

			if (!this.ship.isDestroyed()) {

				boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D);
				boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A);
				boolean moveUp = inputManager.isKeyDown(KeyEvent.VK_UP)
						|| inputManager.isKeyDown(KeyEvent.VK_W);
				boolean moveDown = inputManager.isKeyDown(KeyEvent.VK_DOWN)
						|| inputManager.isKeyDown(KeyEvent.VK_S);

				boolean isRightBorder = this.ship.getPositionX()
						+ this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship.getPositionX()
						- this.ship.getSpeed() < 1;
				boolean isUpBorder = this.ship.getPositionY()
						- this.ship.getSpeed() < SEPARATION_LINE_HEIGHT + 1;
				boolean isDownBorder = this.ship.getPositionY()
						+ this.ship.getHeight() + this.ship.getSpeed() > SEPARATION_LINE_HEIGHT_DOWN - 1;

				if (moveRight && !isRightBorder) {
					this.ship.moveRight();
					this.backgroundMoveRight = true;
				}
				if (moveLeft && !isLeftBorder) {
					this.ship.moveLeft();
					this.backgroundMoveLeft = true;
				}
				if (moveUp && !isUpBorder) {
					this.ship.moveUp();
				}
				if (moveDown && !isDownBorder) {
					this.ship.moveDown();
				}

				// 마우스 추적(화면 밖으로 나가도 추적하고 싶으면 사용 - 거의 사용X 일듯 나중에 지우기)
//				Point mousePosition = MouseInfo.getPointerInfo().getLocation();
//				SwingUtilities.convertPointFromScreen(mousePosition, drawManager.getFrame());

				// 함선과의 상대거리 계산
				double deltaX = inputManager.getMouseX() - (this.ship.getPositionX() + this.ship.getWidth() * 3 / 4);
				double deltaY = inputManager.getMouseY() - (this.ship.getPositionY() + this.ship.getHeight() * 2);

				// 각도 계산 (라디안 값)
				double angle = Math.atan2(deltaY, deltaX);
				this.ship.setAngle(angle);

				if (inputManager.isKeyDown(KeyEvent.VK_SPACE) || inputManager.isMouseButtonDown(MouseEvent.BUTTON1))
					if (this.ship.getCanShoot360() && this.ship.shoot360(this.bullets)) {	// 무한모드일 때 작동(임시로 일반모드일 때 작동하게 설정)
						this.bulletsShot++;
						this.fire_id++;
						this.logger.info("Bullet's fire_id is " + fire_id);
					} else if (this.ship.shoot(this.bullets)) {
						this.bulletsShot++;
						this.fire_id++;
						this.logger.info("Bullet's fire_id is " + fire_id);
					}
			}

			if (this.enemyShipSpecial != null) {
				if (!this.enemyShipSpecial.isDestroyed())
					this.enemyShipSpecial.move(2, 0);
				else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
					this.enemyShipSpecial = null;
			}

			if (this.enemyShipSpecial == null
					&& this.enemyShipSpecialCooldown.checkFinished()) {
				this.enemyShipSpecial = new EnemyShip();
				this.enemyShipSpecialCooldown.reset();
				//Sound Operator
				sm = SoundManager.getInstance();
				sm.playES("UFO_come_up");
				this.logger.info("A special ship appears");
			}

			if (this.enemyShipSpecial != null
					&& this.enemyShipSpecial.getPositionX() > this.width) {
				this.enemyShipSpecial = null;
				this.logger.info("The special ship has escaped");
			}


			if (returnCode == 4 && level == 4 && levelFinished) {
				this.midBoss = null;
				levelFinished = false;
			}


			// Boss's fires
			if (midBoss != null)
				midBoss.update();
			if (finBoss != null)
				finBoss.update();

			this.item.updateBarrierAndShip(this.ship);   // team Inventory
			this.speedItem.update();         // team Inventory
			this.feverTimeItem.update();

			this.enemyShipFormation.setShipPosition(this.ship.getPositionX(), this.ship.getPositionY());
			this.enemyShipFormation.update();
			this.enemyShipFormation.shoot(this.bullets);
		}
		//manageCollisions();
		manageCollisions_add_item(); //by Enemy team
		cleanBullets();
		cleanObstacles();
		this.itemManager.cleanItems(); //by Enemy team

		if (player2 != null) {
			// Player 2 movement and shooting
			boolean moveRight2 = inputManager.isKeyDown(KeyEvent.VK_C);
			boolean moveLeft2 = inputManager.isKeyDown(KeyEvent.VK_Z);

			if (moveRight2 && player2.getPositionX() + player2.getWidth() < width) {
				player2.moveRight();
			}
			if (moveLeft2 && player2.getPositionX() > 0) {
				player2.moveLeft();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_X)) {
				player2.shoot(bullets);
			}

			// Player 2 bullet collision handling
			TwoPlayerMode.handleBulletCollisionsForPlayer2(this.bullets, player2);

			// 장애물과 아이템 상호작용 추가
			TwoPlayerMode.handleObstacleCollisionsForPlayer2(this.obstacles, player2);
			TwoPlayerMode.handleItemCollisionsForPlayer2(player2);
		}
		draw();

		/**
		 * Added by the Level Design team and edit by team Enemy
		 * Changed the conditions for the game to end  by team Enemy
		 *
		 * Counts and checks if the number of waves destroyed match the intended number of waves for this level
		 * Spawn another wave
		 **/
		if (getRemainingEnemies() == 0 && waveCounter < this.gameSettings.getWavesNumber()) {

			waveCounter++;
			this.initialize();

		}

		/**
		* Wave counter condition added by the Level Design team*
		* Changed the conditions for the game to end  by team Enemy
		*
		* Checks if the intended number of waves for this level was destroyed
		* **/
		if ((getRemainingEnemies() == 0
				&& !levelFinished
				&& waveCounter == this.gameSettings.getWavesNumber()
				&& this.returnCode != 4)
				|| (this.lives == 0)
				|| (level != 4 && level != 8 && isRoundCleared())
		) {
			levelFinished = true;
			//this.screenFinishedCooldown.reset(); It works now -- With love, Level Design Team
		}

		if (levelFinished && this.screenFinishedCooldown.checkFinished()) {
			//this.logger.info("Final Playtime: " + playTime + " seconds");    //clove
			achievementConditions.checkNoDeathAchievements(lives);
			achievementConditions.score(score);
			try { //Team Clove
				statistics.comHighestLevel(level);
				statistics.addBulletShot(bulletsShot);
				statistics.addShipsDestroyed(shipsDestroyed);

				achievementConditions.onKill();
				achievementConditions.onStage();
				achievementConditions.trials();
				achievementConditions.killStreak();
				achievementConditions.fastKill(fastKill);
				achievementConditions.score(score);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			this.isRunning = false;
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	public void draw() {
		drawManager.initDrawing(this);

		/** ### TEAM INTERNATIONAL ### */
		drawManager.drawBackground(backgroundMoveRight, backgroundMoveLeft, returnCode, level);
		this.backgroundMoveRight = false;
		this.backgroundMoveLeft = false;

		DrawManagerImpl.drawRect(0, 0, this.width, SEPARATION_LINE_HEIGHT, Color.BLACK);
		DrawManagerImpl.drawRect(0, this.height - 70, this.width, 70, Color.BLACK); // by Saeum Jung - TeamHUD

		if (this.ship.getCanShoot360()) {    // 임시
			drawManager.drawRotateEntity(this.ship, this.ship.getPositionX(),
					this.ship.getPositionY(), this.ship.getAngle());
		} else {
			drawManager.drawEntity(this.ship, this.ship.getPositionX(),
					this.ship.getPositionY());
		}

		if (player2 != null) {
			drawManager.drawEntity(player2, player2.getPositionX(), player2.getPositionY());
		}
		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

		if ( level != 4 && level != 8) {
			enemyShipFormation.draw();
		} else {
			enemyShipFormation.draw_Story_Boss();
			if (midBoss != null)
				midBoss.drawFire();
			if (finBoss != null)
				finBoss.drawFire();
		}

		DrawManagerImpl.drawSpeed(this, ship.getSpeed()); // Ko jesung / HUD team
		DrawManagerImpl.drawSeparatorLine(this,  this.height - 65); // Ko jesung / HUD team


		for (PiercingBullet bullet : this.bullets)
			if (this.ship.getCanShoot360() || bullet.getBulletType() != 0)
				drawManager.drawRotateEntity(bullet, bullet.getPositionX(),
								bullet.getPositionY(), bullet.getAngle());
			else
				DrawManager.drawEntity(bullet, bullet.getPositionX(),
						bullet.getPositionY());

		itemManager.drawItems(); //by Enemy team

		// --- OBSTACLES - Draw Obstacles
		if (!levelFinished) {
			for (Obstacle obstacle : this.obstacles) {
				DrawManager.drawEntity(obstacle, obstacle.getPositionX(), obstacle.getPositionY());
			}
		}


		// Interface.
//		drawManager.drawScore(this, this.scoreManager.getAccumulatedScore());    //clove -> edit by jesung ko - TeamHUD(to adjust score)
//		drawManager.drawScore(this, this.score); // by jesung ko - TeamHUD
		DrawManagerImpl.drawScore2(this,this.score); // by jesung ko - TeamHUD
		drawManager.drawLives(this, this.lives);
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);
		DrawManagerImpl.drawRemainingEnemies(this, getRemainingEnemies()); // by HUD team SeungYun
		DrawManagerImpl.drawLevel(this, this.level);
		DrawManagerImpl.drawBulletSpeed(this, ship.getBulletSpeedY());
		// Call the method in DrawManagerImpl - Lee Hyun Woo TeamHud
		DrawManagerImpl.drawTime(this, this.playTime);
		// Call the method in DrawManagerImpl - Soomin Lee / TeamHUD
		drawManager.drawItem(this); // HUD team - Jo Minseo
		double percent = (System.currentTimeMillis() - playStartTime) > 0 ?
			 (double) ((System.currentTimeMillis() - playStartTime) * 100) / (1000 * 15) : 0;
		if(returnCode == 4 & !(level == 4 || level == 8))
			DrawManagerImpl.drawPercentage(this, percent);	// story모드 time에 따른 percent 적용 필요

		if(player2 != null){
			DrawManagerImpl.drawBulletSpeed2P(this, player2.getBulletSpeedY());
			DrawManagerImpl.drawSpeed2P(this, player2.getSpeed());
			DrawManagerImpl.drawLives2P(this, ((TwoPlayerMode) this).getLivesTwo());
			if (((TwoPlayerMode) this).getLivesTwo() == 0) {
				player2 = null;
			}
		} // by HUD team HyunWoo

		// Countdown to game start.
		if (!this.inputDelay.checkFinished()) {
			int countdown = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
					- this.gameStartTime)) / 1000);

			/**
			 * Wave counter condition added by the Level Design team
			 *
			 * Display the wave number instead of the level number
			 * **/
			if (waveCounter != 1) {
				drawManager.drawWave(this, waveCounter, countdown);
			} else {
				drawManager.drawCountDown(this, this.level, countdown,
						this.bonusLife);
			}

			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ 12);
		}

		// Soomin Lee / TeamHUD
		if (this.inputDelay.checkFinished()) {
			playTime = (int) ((System.currentTimeMillis() - playStartTime) / 1000) + playTimePre;
		}

		super.drawPost();
		drawManager.completeDrawing(this);
	}

	/**
	 * Cleans bullets that go off screen.
	 */
	private void cleanBullets() {
		Set<PiercingBullet> recyclable = new HashSet<PiercingBullet>(); // Edited by Enemy
		for (PiercingBullet bullet : this.bullets) { // Edited by Enemy
			bullet.update();
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() + bullet.getHeight() > SEPARATION_LINE_HEIGHT_DOWN) // ko jesung / HUD team
			{
				//Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
				bullet.setCheckCount(true);
				recyclable.add(bullet);
			}
		}
		this.bullets.removeAll(recyclable);
		PiercingBulletPool.recycle(recyclable); // Edited by Enemy
	}

	/**
	 * Clean obstacles that go off screen.
	 */
	private void cleanObstacles() { //added by Level Design Team
		Set<Obstacle> removableObstacles = new HashSet<>();
		for (Obstacle obstacle : this.obstacles) {
			obstacle.update(this.level);
			if ( obstacle.getPositionY() < SEPARATION_LINE_HEIGHT ||
					obstacle.getPositionY() + obstacle.getHeight() > SEPARATION_LINE_HEIGHT_DOWN - 10) {
				removableObstacles.add(obstacle);
			}
		}
		this.obstacles.removeAll(removableObstacles);
	}

	/**
	 * Manages collisions between bullets and ships. -original code
	 */
//	private void manageCollisions() {
//		Set<Bullet> recyclable = new HashSet<Bullet>();
//		for (Bullet bullet : this.bullets)
//			if (bullet.getSpeedY() > 0) {
//				if (checkCollision(bullet, this.ship) && !this.levelFinished) {
//					recyclable.add(bullet);
//					if (!this.ship.isDestroyed() && !this.item.isBarrierActive()) {   // team Inventory
//						this.ship.destroy();
//						this.lives--;
//						this.logger.info("Hit on player ship, " + this.lives
//								+ " lives remaining.");
//					}
//				}
//			} else {
//				for (EnemyShip enemyShip : this.enemyShipFormation)
//					if (!enemyShip.isDestroyed()
//							&& checkCollision(bullet, enemyShip)) {
//						this.scoreManager.addScore(enemyShip.getPointValue());    //clove
//						this.shipsDestroyed++;
//						// CtrlS - increase the hitCount for 1 kill
//						this.hitCount++;
//						this.enemyShipFormation.destroy(enemyShip);
//						recyclable.add(bullet);
//					}
//				if (this.enemyShipSpecial != null
//						&& !this.enemyShipSpecial.isDestroyed()
//						&& checkCollision(bullet, this.enemyShipSpecial)) {
//					this.scoreManager.addScore(this.enemyShipSpecial.getPointValue());    //clove
//					this.shipsDestroyed++;
//					// CtrlS - increase the hitCount for 1 kill
//					this.hitCount++;
//					this.enemyShipSpecial.destroy();
//					this.enemyShipSpecialExplosionCooldown.reset();
//					recyclable.add(bullet);
//				}
//			}
//		this.bullets.removeAll(recyclable);
//		BulletPool.recycle(recyclable);
//	}


	/**
	 * Manages collisions between bullets and ships. -Edited code for Drop Item
	 * Manages collisions between bullets and ships. -Edited code for Piercing Bullet
	 */
	private void adjustShipPositionAfterCollision(Ship ship, EnemyShip enemyShip) {
		int adjustmentDistance = 10; // 충돌 후 벌어질 거리


		if (ship.getPositionX() < enemyShip.getPositionX()) {
			ship.setPositionX(ship.getPositionX() - adjustmentDistance);
		} else {
			ship.setPositionX(ship.getPositionX() + adjustmentDistance);
		}

		if (ship.getPositionY() < enemyShip.getPositionY()) {
			ship.setPositionY(ship.getPositionY() - adjustmentDistance);
		} else {
			ship.setPositionY(ship.getPositionY() + adjustmentDistance);
		}

		System.out.println("Ship position adjusted to avoid repeated collisions.");
	}

	public void manageCollisions_add_item() {
		for (EnemyShip enemyShip : this.enemyShipFormation) {
			if (!enemyShip.isDestroyed() && checkCollision(this.ship, enemyShip)) {
				if (this.ship.isInvincible()) {
					System.out.println("Collision ignored due to invincibility.");
				} else if (this.item.isBarrierActive()) {
					this.ship.activateFrozen();
					adjustShipPositionAfterCollision(this.ship, enemyShip);
					System.out.println("Collision blocked by shield.");

				} else {
					System.out.println("Collision detected. Player lives decreased.");
					if (this.ship.isInvincible()) {
						System.out.println("Collision ignored due to invincibility.");
					} else {
						this.lives--;
						System.out.println("Collision detected. Player lives decreased.");
					}
					this.ship.activateInvincibility();
					this.ship.destroy();
					this.logger.info("Player ship collided with enemy. Lives remaining: " + this.lives);

					if (this.lives <= 0) {
						sm = SoundManager.getInstance();
						sm.playShipDieSounds();
						System.out.println("Player is out of lives.");
					}
				}
			}
		}
		Set<PiercingBullet> recyclable = new HashSet<PiercingBullet>();

		for (PiercingBullet bullet : this.bullets)
			if (bullet.getBulletType() != 0) { // 적 총알
				if (checkCollision(bullet, this.ship) && !levelFinished) {
					recyclable.add(bullet);

					if (!this.ship.isDestroyed() && !this.item.isBarrierActive()) {   // team Inventory
						this.ship.destroy();
						this.lives--;
						if (returnCode == 4) {
							if (level == 4 && midBoss != null) { midBoss.lifeSteal(); }
							else if (level == 8 && finBoss != null) { finBoss.lifeSteal(); }
						}
						this.logger.info("Hit on player ship, " + this.lives
								+ " lives remaining.");

						// Sound Operator
						if (this.lives == 0){
							sm = SoundManager.getInstance();
							sm.playShipDieSounds();
						}
					}
				}
			} else { // 아군 총알
				// CtrlS - set fire_id of bullet.
				bullet.setFire_id(fire_id);
				if (returnCode != 4 || (level != 4 && level != 8) ) {
					for (EnemyShip enemyShip : this.enemyShipFormation) {
						if (!enemyShip.isDestroyed()
								&& checkCollision(bullet, enemyShip)) {
							int[] CntAndPnt = this.enemyShipFormation._destroy(bullet, enemyShip, false);    // team Inventory
							this.shipsDestroyed += CntAndPnt[0];
							int feverScore = CntAndPnt[0]; //TEAM CLOVE //Edited by team Enemy

							if (enemyShip.getHp() <= 0) {
								//inventory_f fever time is activated, the score is doubled.
								if (feverTimeItem.isActive()) {
									feverScore = feverScore * 10;
								}
								this.shipsDestroyed++;
							}

							this.scoreManager.addScore(feverScore); //clove
							this.score += CntAndPnt[1];

							// CtrlS - If collision occur then check the bullet can process
							if (!processedFireBullet.contains(bullet.getFire_id())) {
								// CtrlS - increase hitCount if the bullet can count
								if (bullet.isCheckCount()) {
									hitCount++;
									bullet.setCheckCount(false);
									this.logger.info("Hit count!");
									processedFireBullet.add(bullet.getFire_id()); // mark this bullet_id is processed.
								}
							}

							bullet.onCollision(enemyShip); // Handle bullet collision with enemy ship

							// Check PiercingBullet piercing count and add to recyclable if necessary
							if (bullet.getPiercingCount() <= 0) {
								//Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
								bullet.setCheckCount(true);
								recyclable.add(bullet);
							}
						}
						// Added by team Enemy.
						// Enemy killed by Explosive enemy gives points too
						if (enemyShip.isChainExploded()) {
							if (enemyShip.getColor() == Color.MAGENTA) {
								this.itemManager.dropItem(enemyShip, 1, 1);
							}
							this.score += enemyShip.getPointValue();
							this.shipsDestroyed++;
							enemyShip.setChainExploded(false); // resets enemy's chain explosion state.
						}
					}
				} else if (level == 4) { // 스토리 모드 중간 보스 레벨
					if (this.midBoss != null
							&& !midBoss.isBossDead()
							&& checkCollision(bullet, this.midBoss)) {

						if (fire_id >= fire_id_pre + 1) {
							midBoss.takeDamage(1); // 일단 아군에 의한 데미지는 1로 두었습니다.
							fire_id_pre = fire_id;
							midBoss.damageSound();
							recyclable.add(bullet);
						}

						if (midBoss.isEnraged() && !midBoss.enrage()) {
							midBoss.enragedMode();
						}

						if (!midBoss.isEnraged() && midBoss.enrage()){
							midBoss.calmDown();
						}

						feverSpecialScore = EnemyShip.getPointValue();

						if (feverTimeItem.isActive()) {
							feverSpecialScore *= 10;
						}

						if (!processedFireBullet.contains(bullet.getFire_id())) {
							if (bullet.isCheckCount()) {
								hitCount++;
								bullet.setCheckCount(false);
								this.logger.info("Hit count!");
							}
						}

						bullet.onCollision(this.midBoss);
						if (bullet.getPiercingCount() <= 0) {
							bullet.setCheckCount(true);
							recyclable.add(bullet);
						}
					}
				} else { // 스토리 모드 최종 보스 레벨
					if (this.finBoss != null
							&& !finBoss.isBossDead()
							&& checkCollision(bullet, this.finBoss)) {

						if (fire_id_pre != fire_id) {
							finBoss.takeDamage(1); // 일단 아군에 의한 데미지는 1로 두었습니다.
							fire_id_pre = fire_id;
							finBoss.damageSound();
							recyclable.add(bullet);
						}

						if (finBoss.isEnraged()) {
							finBoss.enragedMode();
						}

						if (!finBoss.isEnraged()) {
							finBoss.calmDown();
						}

						feverSpecialScore = EnemyShip.getPointValue();

						if (feverTimeItem.isActive()) {
							feverSpecialScore *= 10;
						}

						if (!processedFireBullet.contains(bullet.getFire_id())) {
							if (bullet.isCheckCount()) {
								hitCount++;
								bullet.setCheckCount(false);
								this.logger.info("Hit count!");
							}
						}

						bullet.onCollision(this.finBoss);
						if (bullet.getPiercingCount() <= 0) {
							bullet.setCheckCount(true);
							recyclable.add(bullet);
						}
					}
				}

				if (this.enemyShipSpecial != null
						&& !this.enemyShipSpecial.isDestroyed()
						&& checkCollision(bullet, this.enemyShipSpecial)) {

					int feverSpecialScore = enemyShipSpecial.getPointValue();
          			// inventory - Score bonus when acquiring fever items
					if (feverTimeItem.isActive()) { feverSpecialScore *= 10; } //TEAM CLOVE //Team inventory

					// CtrlS - If collision occur then check the bullet can process
					if (!processedFireBullet.contains(bullet.getFire_id())) {
						// CtrlS - If collision occur then increase hitCount and checkCount
						if (bullet.isCheckCount()) {
							hitCount++;
							bullet.setCheckCount(false);
							this.logger.info("Hit count!");
						}
					}
					this.scoreManager.addScore(feverSpecialScore); //clove
					this.shipsDestroyed++;
					this.enemyShipSpecial.destroy();
					this.enemyShipSpecialExplosionCooldown.reset();

					bullet.onCollision(this.enemyShipSpecial); // Handle bullet collision with special enemy

					// Check PiercingBullet piercing count for special enemy and add to recyclable if necessary
					if (bullet.getPiercingCount() <= 0) {
						//Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
						bullet.setCheckCount(true);
						recyclable.add(bullet);
					}

					//// Drop item to 100%
					itemManager.dropItem(enemyShipSpecial,1,2);
				}

				for (Obstacle obstacle : this.obstacles) {
					if (!obstacle.isDestroyed() && checkCollision(bullet, obstacle)) {
						obstacle.destroy();  // Destroy obstacle
						recyclable.add(bullet);  // Remove bullet

						// Sound Operator
						sm = SoundManager.getInstance();
						sm.playES("obstacle_explosion");
					}
				}
			}

		for (Obstacle obstacle : this.obstacles) {
			if (!obstacle.isDestroyed() && checkCollision(this.ship, obstacle)) {
				//Obstacles ignored when barrier activated_team inventory
				if (!this.item.isBarrierActive()) {
					this.lives--;
					if (!this.ship.isDestroyed()) {
						this.ship.destroy();  // Optionally, destroy the ship or apply other effects.
					}
					obstacle.destroy();  // Destroy obstacle
					this.logger.info("Ship hit an obstacle, " + this.lives + " lives remaining.");
				} else {
					obstacle.destroy();  // Destroy obstacle
					this.logger.info("Shield blocked the hit from an obstacle, " + this.lives + " lives remaining.");
				}

				break;  // Stop further collisions if the ship is destroyed.
			}
		}
		if (returnCode == 4 && this.level == 4) {
			if(midBoss.collision(this.ship)) {
				if (!this.item.isBarrierActive()) {
					this.lives--;
					if (!this.ship.isDestroyed()) {
						this.ship.destroy();
					}
				}
			}
		} else if (returnCode == 4 && this.level == 8) {
			if(finBoss.collision(this.ship)) {
				if (!this.item.isBarrierActive()) {
					this.lives--;
					if (!this.ship.isDestroyed()) {
						this.ship.destroy();
					}
				}
			}
		}

		this.bullets.removeAll(recyclable);
		PiercingBulletPool.recycle(recyclable);

		//Check item and ship collision
		for(Item item : itemManager.items){
			if (checkCollision(item, ship)) {
				itemManager.OperateItem(item);
				// CtrlS: Count coin
				if (item.getSpriteType() == DrawManager.SpriteType.ItemCoin) coinItemsCollected++;
				Core.getLogger().info("coin: " + coinItemsCollected);
			}
		}
		itemManager.removeAllReItems();

		if (midBoss != null && midBoss.getCurrentHp() <= 0) {
			this.scoreManager.addScore(feverSpecialScore);
			this.shipsDestroyed++;
			this.midBoss.destroy(); // 사망 사운드
			this.midBoss = null;
			this.middleBossExplosionCooldown.reset();
			levelFinished = true;
		}

		if (finBoss != null && finBoss.getCurrentHp() <= 0) {
			this.scoreManager.addScore(feverSpecialScore);
			this.shipsDestroyed++;
			this.finBoss.destroy(); // 사망 사운드
			this.finBoss = null;
			this.finalBossExplosionCooldown.reset();
			levelFinished = true;
		}
	}


	/**
	 * Checks if two entities are colliding.
	 *
	 * @param a
	 *            First entity, the bullet.
	 * @param b
	 *            Second entity, the ship.
	 * @return Result of the collision test.
	 */
	public static boolean checkCollision(final Entity a, final Entity b) {
		// Calculate center point of the entities in both axis.
		int centerAX = a.getPositionX() + a.getWidth() / 2;
		int centerAY = a.getPositionY() + a.getHeight() / 2;
		int centerBX = b.getPositionX() + b.getWidth() / 2;
		int centerBY = b.getPositionY() + b.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
		int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

	public int getBulletsShot(){    //clove
		return this.bulletsShot;    //clove
	}                               //clove

	/**
	 * Add playtime parameter - Soomin Lee / TeamHUD
	 * Returns a GameState object representing the status of the game.
	 *
	 * @return Current game state.
	 */
	public final GameState getGameState() {
		return new GameState(returnCode, this.level, this.scoreManager.getAccumulatedScore(), this.lives,this.livesTwo,
				this.bulletsShot, this.shipsDestroyed, this.playTime, this.coin, this.gem, this.hitCount, this.coinItemsCollected); // Team-Ctrl-S(Currency)
	}
	public int getLives() {
		return lives;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}

	public Ship getShip() {
		return ship;
	}   // Team Inventory(Item)

	public ItemBarrierAndHeart getItem() {
		return item;
	}   // Team Inventory(Item)

	public FeverTimeItem getFeverTimeItem() {
		return feverTimeItem;
	} // Team Inventory(Item)
	/**
	 * Check remaining enemies
	 *
	 * @return remaining enemies count.
	 *
	 */
	private int getRemainingEnemies() {
		int remainingEnemies = 0;
		for (EnemyShip enemyShip : this.enemyShipFormation) {
			if (!enemyShip.isDestroyed()) {
				remainingEnemies++;
			}
		}
		return remainingEnemies;
	} // by HUD team SeungYun


	public SpeedItem getSpeedItem() {
		return this.speedItem;
	}

	public boolean isRoundCleared() {
		return returnCode == 4 && ( playTime - playTimePre >= 15);
	}

	public static boolean isLevelFinished() {
		return levelFinished;
	}
}
