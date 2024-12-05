package engine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import CtrlS.CurrencyManager;
import CtrlS.RoundState;
import CtrlS.ReceiptScreen;
import CtrlS.UpgradeManager;
import Sound_Operator.SoundManager;
import clove.Statistics;
import entity.EnemyShip;
import inventory_develop.StoryModeTrait;
import level_design.Background;
import clove.AchievementManager;
import screen.*;

import javax.imageio.ImageIO;


/**
 * Implements core game logic.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class Core {

	/** Width of current screen. */
	private static final int WIDTH = 630;
	/** Height of current screen. */
	private static final int HEIGHT = 720;
	/** Max fps of current screen. */
	private static final int FPS = 60;

	/** Max lives. */
	public static final int MAX_LIVES = 3; // TEAM CLOVER: Fixed MAX_LIVES from private to public for usage in achievement
	/** Levels between extra life. */
	private static final int EXTRA_LIFE_FRECUENCY = 3;
	/** Total number of levels. */
	public static final int NUM_LEVELS = 7; // TEAM CLOVER : Fixed NUM_LEVELS from privated to public for usage in achievement
	
	/** Difficulty settings for level 1. */
	private static final GameSettings SETTINGS_LEVEL_1 =
			new GameSettings(5, 4, 60, 200, 1);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2 =
			new GameSettings(5, 5, 50, 2500, 1);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3 =
			new GameSettings(1, 1, -8, 500, 1);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4 =
			new GameSettings(6, 6, 30, 1500, 2);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5 =
			new GameSettings(7, 6, 20, 1000, 2);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_LEVEL_6 =
			new GameSettings(7, 7, 10, 1000, 3);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_7 =
			new GameSettings(8,  7, 2, 500, 1);

	// Story 모드
	/** Difficulty settings for Story mode level 1. */
	private static final GameSettings SETTINGS_LEVEL_S1 =
			new GameSettings(20, 1500, 600, 1500);
	/** Difficulty settings for Story mode level 2. */
	private static final GameSettings SETTINGS_LEVEL_S2 =
			new GameSettings(15, 1500, 575, 1500);
	/** Difficulty settings for Story mode level 3. */
	private static final GameSettings SETTINGS_LEVEL_S3 =
			new GameSettings(15, 1500, 550, 1500);
	/** Difficulty settings for Story mode level 4. */
	private static final GameSettings SETTINGS_LEVEL_S4 =
			new GameSettings(1, 0, 0, 700); // Middle Boss Level
	/** Difficulty settings for Story mode level 5. */
	private static final GameSettings SETTINGS_LEVEL_S5 =
			new GameSettings(15, 1500, 500, 1000);
	/** Difficulty settings for Story mode level 6. */
	private static final GameSettings SETTINGS_LEVEL_S6 =
			new GameSettings(15, 1500, 4750, 1000);
	/** Difficulty settings for Story mode level 7. */
	private static final GameSettings SETTINGS_LEVEL_S7 =
			new GameSettings(15, 1500, 450, 500);
	/** Difficulty settings for Story mode level 8. */
	private static final GameSettings SETTINGS_LEVEL_S8 =
			new GameSettings(1, 0, 0, 500); // Final Boss Level


	/** Frame to draw the screen on. */
	private static Frame frame;
	/** Screen currently shown. */
	private static Screen currentScreen;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings;
	private static List<GameSettings> storyModeSettings;
	/** Application logger. */
	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
	/** Logger handler for printing to disk. */
	private static Handler fileHandler;
	/** Logger handler for printing to console. */
	private static ConsoleHandler consoleHandler;
	// Sound Operator
	private static SoundManager sm;
    private static AchievementManager achievementManager; // Team CLOVER

	/**
	 * Test implementation.
	 * 
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) {
		try {
			LOGGER.setUseParentHandlers(false);

			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new MinimalFormatter());

			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new MinimalFormatter());
			// Sound Operator
			sm = SoundManager.getInstance();
			EnemyShip.setSoundManager(sm);

			LOGGER.addHandler(fileHandler);
			LOGGER.addHandler(consoleHandler);
			LOGGER.setLevel(Level.ALL);

			// TEAM CLOVER : Added log to check if function is working
			System.out.println("Initializing AchievementManager...");
			achievementManager = new AchievementManager(DrawManager.getInstance());
			System.out.println("AchievementManager initialized!");

			// CtrlS: Make instance of Upgrade Manager
			Core.getUpgradeManager();

			//Clove. Reset Player Statistics After the Game Starts
			Statistics statistics = new Statistics();
			statistics.resetStatistics();
			LOGGER.info("Reset Player Statistics");

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();

		/** ### TEAM INTERNATIONAL ###*/
		/** Initialize singleton instance of a background*/
		Background.getInstance().initialize(frame);

		gameSettings = new ArrayList<GameSettings>();
		gameSettings.add(SETTINGS_LEVEL_1);
		gameSettings.add(SETTINGS_LEVEL_2);
		gameSettings.add(SETTINGS_LEVEL_3);
		gameSettings.add(SETTINGS_LEVEL_4);
		gameSettings.add(SETTINGS_LEVEL_5);
		gameSettings.add(SETTINGS_LEVEL_6);
		gameSettings.add(SETTINGS_LEVEL_7);

		// Story 모드 전용 gameSettings 리스트
		storyModeSettings = new ArrayList<>();
		storyModeSettings.add(SETTINGS_LEVEL_S1);
		storyModeSettings.add(SETTINGS_LEVEL_S2);
		storyModeSettings.add(SETTINGS_LEVEL_S3);
		storyModeSettings.add(SETTINGS_LEVEL_S4);
		storyModeSettings.add(SETTINGS_LEVEL_S5);
		storyModeSettings.add(SETTINGS_LEVEL_S6);
		storyModeSettings.add(SETTINGS_LEVEL_S7);
		storyModeSettings.add(SETTINGS_LEVEL_S8);
		
		GameState gameState;
		RoundState roundState;

		int returnCode = 1;
		do {
			// Add playtime parameter - Soomin Lee / TeamHUD
			// Add hitCount parameter - Ctrl S
			// Add coinItemsCollected parameter - Ctrl S
			gameState = new GameState(returnCode, 1, 0
					, MAX_LIVES, 0,0, 0, 0, 0, 0, 0, 0);
			switch (returnCode) {
			case 1:
				// Main menu.
                currentScreen = new TitleScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " title screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing title screen.");
				break;
			case 2: // normal mode
				// Game & score.
				LOGGER.info("Starting inGameBGM");
				// Sound Operator
				sm.playES("start_button_ES");
				sm.playBGM("inGame_bgm");

				try {
					getUpgradeManager().resetUpgrades();		// storyMode에서 업그레이드되었던 부분 초기화
					getUpgradeManager().setShipShoot360(true);	// 360도 발사만 가능하게
				} catch (IOException e) {throw new RuntimeException(e);}

				do {
					// One extra live every few levels.
					boolean bonusLife = gameState.getLevel()
							% EXTRA_LIFE_FRECUENCY == 0
							&& gameState.getLivesRemaining() < MAX_LIVES;

					GameState prevState = gameState;
					currentScreen = new GameScreen(gameState,
							gameSettings.get(gameState.getLevel() - 1),
							bonusLife, width, height, FPS,2);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " game screen at " + FPS + " fps.");
					frame.setScreen(currentScreen);
					LOGGER.info("Closing game screen.");


					achievementManager.updateAchievements(currentScreen); // TEAM CLOVER : Achievement

					Statistics statistics = new Statistics(); //Clove

					gameState = ((GameScreen) currentScreen).getGameState();

					roundState = new RoundState(prevState, gameState);

					// Add playtime parameter - Soomin Lee / TeamHUD
					gameState = new GameState(returnCode,
							gameState.getLevel() + 1,
							gameState.getScore(),
							gameState.getLivesRemaining(),
							gameState.getLivesTwoRemaining(),
							gameState.getBulletsShot(),
							gameState.getShipsDestroyed(),
							gameState.getTime(),
							gameState.getCoin() + roundState.getRoundCoin(),
							gameState.getGem(),
							gameState.getHitCount(),
							gameState.getCoinItemsCollected());
          			LOGGER.info("Round Coin: " + roundState.getRoundCoin());
					LOGGER.info("Round Hit Rate: " + roundState.getRoundHitRate());
					LOGGER.info("Round Time: " + roundState.getRoundTime());

					try { //Clove
						statistics.addTotalPlayTime(roundState.getRoundTime());
						LOGGER.info("RoundTime Saving");
					} catch (IOException e){
						LOGGER.info("Failed to Save RoundTime");
					}

					// Show receiptScreen
					// If it is not the last round and the game is not over
					// Ctrl-S
					if (gameState.getLevel() <= 7 && gameState.getLivesRemaining() > 0) {
						LOGGER.info("loading receiptScreen");
						currentScreen = new ReceiptScreen(width, height, FPS, roundState, gameState);

						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
								+ " receipt screen at " + FPS + " fps.");
						frame.setScreen(currentScreen);
						LOGGER.info("Closing receiptScreen.");
					}

                    if (achievementManager != null) { // TEAM CLOVER : Added code
                        achievementManager.updateAchievements(currentScreen);
                    }

				} while (gameState.getLivesRemaining() > 0
						&& gameState.getLevel() <= NUM_LEVELS);

				LOGGER.info("Stop InGameBGM");
				// Sound Operator
				sm.stopAllBGM();

				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " score screen at " + FPS + " fps, with a score of "
						+ gameState.getScore() + ", "
						+ gameState.getLivesRemaining() + " lives remaining, "
						+ gameState.getBulletsShot() + " bullets shot and "
						+ gameState.getShipsDestroyed() + " ships destroyed.");
				currentScreen = new ScoreScreen(width, height, FPS, gameState, returnCode);
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing score screen.");
				break;

			case 3:
				// Infinity modes.

				break;

			case 4:
				// Story modes.
				LOGGER.info("Starting Story mode cutscenes");
				DrawManager drawManager = DrawManager.getInstance();
//				SoundManager soundManager = SoundManager.getInstance();
//				soundManager.playBGM("Select_cutscenes1-8_bgm");

				BufferedImage[] firstSetImages = new BufferedImage[5];
				for (int i = 0; i < 5; i++) {
					InputStream imageStream = Background.getStoryModeBackgroundImageStream(i + 1);
					try {
						firstSetImages[i] = ImageIO.read(imageStream);
					} catch (IOException e) {
						throw new RuntimeException("Failed to load cutscene image " + (i + 1), e);
					}
				}
				BufferedImage[] secondSetImages = new BufferedImage[3];
				for (int i = 5; i < 8; i++) {
					InputStream imageStream = Background.getStoryModeBackgroundImageStream(i + 1);
					try {
						secondSetImages[i - 5] = ImageIO.read(imageStream);
					} catch (IOException e) {
						throw new RuntimeException("Failed to load cutscene image " + (i + 1), e);
					}
				}
				BufferedImage[] thirdSetImages = new BufferedImage[2];
				for (int i = 8; i < 10; i++) {
					InputStream imageStream = Background.getStoryModeBackgroundImageStream(i + 1);
					try {
						thirdSetImages[i - 8] = ImageIO.read(imageStream);
					} catch (IOException e) {
						throw new RuntimeException("Failed to load cutscene image " + (i + 1), e);
					}
				}
				SoundManager soundManager = SoundManager.getInstance();
				soundManager.playBGM("First_cutscene_bgm");

				// First cutscene
				for (BufferedImage cutsceneImage : firstSetImages) {
					DrawManager.getInstance().initDrawing(currentScreen);
					DrawManager.backBufferGraphics.drawImage(cutsceneImage, 0, 0, frame.getWidth(), frame.getHeight(), null);
					DrawManager.getInstance().completeDrawing(currentScreen);

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						LOGGER.warning("Cutscene interrupted");
					}
				}
				soundManager.stopAllBGM();


				LOGGER.info("Starting Story mode game");
				// Sound Operator - 배경음악 시작
				//SoundManager soundManager = SoundManager.getInstance();
				soundManager.stopAllBGM();

				playStoryModeBGM(gameState.getLevel());
				//+음악추가
				long roundStartTime = System.currentTimeMillis();
				
				// StoryModeTrait생성 - player의 특성이 기본값으로 변경됨(StatusConfig.properties)
				StoryModeTrait storyModeTrait = new StoryModeTrait();	

				do {
					boolean bonusLife = gameState.getLevel()
							% EXTRA_LIFE_FRECUENCY == 0
							&& gameState.getLivesRemaining() < MAX_LIVES;

					GameState prevState = gameState;
					// GameScreen을 사용하여 단일 플레이어 모드 실행
					currentScreen = new GameScreen(gameState,
							storyModeSettings.get(gameState.getLevel() - 1),
							bonusLife, width, height, FPS, 4);
					LOGGER.info("Starting story mode stage " + gameState.getLevel() + " with " + WIDTH + "x" + HEIGHT + " at " + FPS + " fps.");
					frame.setScreen(currentScreen);
					LOGGER.info("Closing game screen.");


					achievementManager.updateAchievements(currentScreen);
					Statistics statistics = new Statistics();

					gameState = ((GameScreen) currentScreen).getGameState();

					roundState = new RoundState(prevState, gameState);

//					 Show TraitScreen
 					if (gameState.getLevel() <= 7 && gameState.getLivesRemaining() > 0) {
 						soundManager.stopAllBGM();
 						soundManager.playBGM("Select_characteristics_bgm");
 						String[] traits = storyModeTrait.getRandomTraits(gameState.getLevel());
 						LOGGER.info("loading traitScreen");
 						currentScreen = new TraitScreen(width, height, FPS, gameState, storyModeTrait, traits);
 						frame.setScreen(currentScreen);
 						LOGGER.info("Closing traitScreen.");

 						soundManager.stopAllBGM();
 						playStoryModeBGM(gameState.getLevel());
 					}

					// Add playtime parameter
					gameState = new GameState(returnCode,
							gameState.getLevel() + 1,
							gameState.getScore(),
							gameState.getLivesRemaining(),
							gameState.getLivesTwoRemaining(),
							gameState.getBulletsShot(),
							gameState.getShipsDestroyed(),
							gameState.getTime(),
							gameState.getCoin() + roundState.getRoundCoin(),
							gameState.getGem(),
							gameState.getHitCount(),
							gameState.getCoinItemsCollected());
					LOGGER.info("Round Coin: " + roundState.getRoundCoin());
					LOGGER.info("Round Hit Rate: " + roundState.getRoundHitRate());
					LOGGER.info("Round Time: " + roundState.getRoundTime());

					// Secend cutscene()
					if (gameState.getLevel() == 5 && gameState.getLivesRemaining() > 0) {
						LOGGER.info("Displaying cutscenes 6-8 for rounds after 4");
						soundManager.stopAllBGM();
						soundManager.playBGM("Second_cutscene_bgm");
						for (BufferedImage cutsceneImage : secondSetImages) {
							DrawManager.getInstance().initDrawing(currentScreen);
							DrawManager.backBufferGraphics.drawImage(cutsceneImage, 0, 0, frame.getWidth(), frame.getHeight(), null);
							DrawManager.getInstance().completeDrawing(currentScreen);

							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								LOGGER.warning("Cutscene interrupted");
							}
						}
					}
					soundManager.stopAllBGM();
					playStoryModeBGM(gameState.getLevel());

					// Third cutscene()
					if (gameState.getLivesRemaining() > 0 && gameState.getLevel() == 9) {
						LOGGER.info("Displaying cutscenes 9-10 for completing all rounds");
						soundManager.stopAllBGM();
						soundManager.playBGM("Third_cutscene_bgm");
						for (BufferedImage cutsceneImage : thirdSetImages) {
							DrawManager.getInstance().initDrawing(currentScreen);
							DrawManager.backBufferGraphics.drawImage(cutsceneImage, 0, 0, frame.getWidth(), frame.getHeight(), null);
							DrawManager.getInstance().completeDrawing(currentScreen);

							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								LOGGER.warning("Cutscene interrupted");
							}
						}
					}
					soundManager.stopAllBGM();
					playStoryModeBGM(gameState.getLevel());

					try {
						statistics.addTotalPlayTime(roundState.getRoundTime());
						LOGGER.info("RoundTime Saving");
					} catch (IOException e) {
						LOGGER.info("Failed to Save RoundTime");
					}
					if (gameState.getLivesRemaining() <= 0 || gameState.getLevel() > 8)
						break;
          			soundManager.stopAllBGM(); // 이전 BGM 중지
					playStoryModeBGM(gameState.getLevel());
					// Show receiptScreen
					// If it is not the last round and the game is not over
					// 스토리모드에는 필요없는 화면인 것 같아서 제거
//					if (gameState.getLevel() <= 8 && gameState.getLivesRemaining() > 0) {
//						LOGGER.info("loading receiptScreen");
//						currentScreen = new ReceiptScreen(width, height, FPS, roundState, gameState);
//
//						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT + " receipt screen at " + FPS + " fps.");
//						frame.setScreen(currentScreen);
//						LOGGER.info("Closing receiptScreen.");
//					}
//					if (achievementManager != null) {
//						achievementManager.updateAchievements(currentScreen);
//					}

				} while (gameState.getLivesRemaining() > 0
						&& gameState.getLevel() <= 8);


					LOGGER.info("Stop InGameBGM");
					// Sound Operator - 배경음악 종료
					if (gameState.getLivesRemaining() <= 0){
						soundManager.stopAllBGM();
						soundManager.playBGM("game_over_bgm");
						LOGGER.info("Game Over - Playing game_over_bgm");}
					else{
						soundManager.stopAllBGM();
						soundManager.playBGM("endingcredits_bgm");
						LOGGER.info("Game Complete - Playing endingcredits_bgm");}
					//+음악추가

					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT + " score screen at " + FPS + " fps, with a score of "
							+ gameState.getScore() + ", "
							+ gameState.getLivesRemaining() + " lives remaining, "
							+ gameState.getBulletsShot() + " bullets shot and "
							+ gameState.getShipsDestroyed() + " ships destroyed.");
					// StoryScoreScreen and EndingCredit(if Boss Clear)
					currentScreen = new StoryScoreScreen(width, height, FPS, gameState);
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing score screen.");
				break;

			case 5:
				// High scores.
				currentScreen = new HighScoreScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " high score screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing high score screen.");
				break;
//			case 4:
//				LOGGER.info("Starting inGameBGM");
//				// Sound Operator
//				sm.playES("start_button_ES");
//				sm.playBGM("inGame_bgm");
//
//				do {
//					if (gameSettings == null || gameSettings.isEmpty()) {
//						gameSettings = new ArrayList<>();
//						gameSettings.add(SETTINGS_LEVEL_1);
//						gameSettings.add(SETTINGS_LEVEL_2);
//						gameSettings.add(SETTINGS_LEVEL_3);
//						gameSettings.add(SETTINGS_LEVEL_4);
//						gameSettings.add(SETTINGS_LEVEL_5);
//						gameSettings.add(SETTINGS_LEVEL_6);
//						gameSettings.add(SETTINGS_LEVEL_7);
//					}
//
//					GameSettings currentGameSettings = gameSettings.get(gameState.getLevel() - 1);
//
//					int fps = FPS;
//					boolean bonusLife = gameState.getLevel() % EXTRA_LIFE_FRECUENCY == 0 &&
//							(gameState.getLivesRemaining() < MAX_LIVES || gameState.getLivesTwoRemaining() < MAX_LIVES);
//
//					GameState prevState = gameState;
//
//					// TwoPlayerMode의 생성자를 호출할 때 필요한 매개변수를 모두 전달
//					currentScreen = new TwoPlayerMode(gameState, currentGameSettings, bonusLife, width, height, fps);
//
//					Statistics statistics = new Statistics(); //Clove
//
//
//					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
//							+ " game screen at " + FPS + " fps.");
//					frame.setScreen(currentScreen);
//					LOGGER.info("Closing game screen.");
//
//
//					achievementManager.updateAchievements(currentScreen); // TEAM CLOVER : Achievement
//
//					gameState = ((TwoPlayerMode) currentScreen).getGameState();
//
//					roundState = new RoundState(prevState, gameState);
//
//					// Add playtime parameter - Soomin Lee / TeamHUD
//					gameState = new GameState(gameState.getLevel() + 1,
//							gameState.getScore(),
//							gameState.getLivesRemaining(),
//							gameState.getLivesTwoRemaining(),
//							gameState.getBulletsShot(),
//							gameState.getShipsDestroyed(),
//							gameState.getTime(),
//							gameState.getCoin() + roundState.getRoundCoin(),
//							gameState.getGem(),
//							gameState.getHitCount(),
//							gameState.getCoinItemsCollected());
//					LOGGER.info("Round Coin: " + roundState.getRoundCoin());
//					LOGGER.info("Round Hit Rate: " + roundState.getRoundHitRate());
//					LOGGER.info("Round Time: " + roundState.getRoundTime());
//
//					try { //Clove
//						statistics.addTotalPlayTime(roundState.getRoundTime());
//						LOGGER.info("RoundTime Saving");
//					} catch (IOException e){
//						LOGGER.info("Failed to Save RoundTime");
//					}
//
//					// Show receiptScreen
//					// If it is not the last round and the game is not over
//					// Ctrl-S
//					if (gameState.getLevel() <= 7 && gameState.getLivesRemaining() > 0) {
//						LOGGER.info("loading receiptScreen");
//						currentScreen = new ReceiptScreen(width, height, FPS, roundState, gameState);
//
//						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
//								+ " receipt screen at " + FPS + " fps.");
//						frame.setScreen(currentScreen);
//						LOGGER.info("Closing receiptScreen.");
//					}
//
//					if (achievementManager != null) { // TEAM CLOVER : Added code
//						achievementManager.updateAchievements(currentScreen);
//					}
//
//				} while ((gameState.getLivesRemaining() > 0 || gameState.getLivesTwoRemaining() > 0) && gameState.getLevel() <= NUM_LEVELS);
//
//				LOGGER.info("Stop InGameBGM");
//				// Sound Operator
//				sm.stopAllBGM();
//
//				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
//						+ " score screen at " + FPS + " fps, with a score of "
//						+ gameState.getScore() + ", "
//						+ gameState.getLivesRemaining() + " lives remaining, "
//						+ gameState.getBulletsShot() + " bullets shot and "
//						+ gameState.getShipsDestroyed() + " ships destroyed.");
//				currentScreen = new ScoreScreen(width, height, FPS, gameState);
//				returnCode = frame.setScreen(currentScreen);
//				LOGGER.info("Closing score screen.");
//				break;
			case 6:
				// Recent Records.
				currentScreen = new RecordScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " recent record screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing recent record screen.");
				break;
			default:
				break;
			}

		} while (returnCode != 0);

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Constructor, not called.
	 */
	private Core() {

	}

	/**
	 * Controls access to the logger.
	 * 
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Controls access to the drawing manager.
	 * 
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager.
	 * 
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls access to the file manager.
	 * 
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
			final int variance) {
		return new Cooldown(milliseconds, variance);
	}

	/**
	 * Controls access to the currency manager.
	 *
	 * @return Application currency manager.
	 */
	// Team-Ctrl-S(Currency)
	public static CurrencyManager getCurrencyManager() {
		return CurrencyManager.getInstance();
	}

	/**
	 * Controls access to the currency manager.
	 *
	 * @return Application currency manager.
	 */
	// Team-Ctrl-S(Currency)
	public static UpgradeManager getUpgradeManager() {
		return UpgradeManager.getInstance();
	}
	private static void playStoryModeBGM(int level) {
		SoundManager soundManager = SoundManager.getInstance();

		switch (level) {
			case 1:
			case 2:
			case 3:
			case 5:
			case 6:
			case 7:
				soundManager.playBGM("storyMode_bgm1-3,5-7");
				break;
			case 4:
				soundManager.playBGM("storyMode_bgm_4");
				break;
			case 8:
				soundManager.playBGM("storyMode_bgm_8");
				break;
			default:
				soundManager.stopAllBGM();
				break;
		}
	}
}
