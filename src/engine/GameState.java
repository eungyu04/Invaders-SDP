package engine;

import clove.ScoreManager;

/**
 * Implements an object that stores the state of the game between levels.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameState {

	/** Current game level. */
	private int level;
	/** Current score. */
	public int score; // TEAM CLOVER : Changed score from private to public for usage in achievement
	/** Lives currently remaining. */
	public int livesRemaining; // TEAM CLOVER : Changed livesRemaining from private to public for usage in achievement
	/** Bullets shot until now. */
	private int bulletsShot;
	/** Ships destroyed until now. */
	public int shipsDestroyed; // TEAM CLOVER : Changed shipsDestroyed from private to public for usage in achievement
	// Soomin Lee / TeamHUD
	/** Total time to play. */
	private int playTime;
	/** Current currency **/
	// Team-Ctrl-S(Currency)
	private int currency;
	/** Current gem **/
	// Team-Ctrl-S(Currency)
	private int gem;
	// Clove
	public ScoreManager scoreManager;

	/**
	 * Constructor.
	 *
	 * @param level
	 *            Current game level.
	 * @param score
	 *            Current score.
	 * @param livesRemaining
	 *            Lives currently remaining.
	 * @param bulletsShot
	 *            Bullets shot until now.
	 * @param shipsDestroyed
	 *            Ships destroyed until now.
	 *
	 * Soomin Lee / TeamHUD
	 * @param playTime
	 * 	          Total time to play.
	 * @param currency
	 * 			  Current currency. // Team-Ctrl-S(Currency)
	 * @param gem
	 * 			  Current gem. // Team-Ctrl-S(Currency)
	 */
	public GameState(final int level, final int score,
					 final int livesRemaining, final int bulletsShot,
					 final int shipsDestroyed, final int playTime, final int currency, final int gem) {
		this.level = level;
		this.score = score;
		this.livesRemaining = livesRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
		this.playTime = playTime;
		this.currency = currency; // Team-Ctrl-S(Currency)
		this.gem = gem; // Team-Ctrl-S(Currency)
		this.gem = gem; // Team-Ctrl-S(Currency)
		this.scoreManager = new ScoreManager(level, score);
	}

	// TEAM CLOVER : Added two static below
	public int shipsDestroyed() {
		return shipsDestroyed;
	}

	/**
	 * Team Clove Create Constructor for using in "engine.Score"
	 *
	 * Constructor for Save file
	 *
	 * @param bulletsShot
	 *            Bullets shot until now.
	 * @param shipsDestroyed
	 *            Ships destroyed until now.
	 * @param level
	 *            Current game level.
	 */
	public GameState(int bulletsShot, int shipsDestroyed, int level) { //Team Clove
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
		this.level = level;
	}

	public GameState(int livesRemaining) { // TEAM CLOVER
		this.livesRemaining = livesRemaining;
	}

	/**
	 * @return the level
	 */
	public final int getLevel() {
		return level;
	}

	/**
	 * @return the score
	 */
	public final int getScore() {
		return score;
	}

	/**
	 * @return the livesRemaining
	 */
	public final int getLivesRemaining() {
		return livesRemaining;
	}

	/**
	 * @return the bulletsShot
	 */
	public final int getBulletsShot() {
		return bulletsShot;
	}

	/**
	 * @return the shipsDestroyed
	 */
	public final int getShipsDestroyed() {
		return shipsDestroyed;
	}

	/**
	 * Soomin Lee / TeamHUD
	 * @return the playTime
	 */
	public final int getTime() { return playTime; }

	/**
	 * @return the currency
	 */
	// Team-Ctrl-S(Currency)
	public final int getCurrency() {
		return currency;
	}

	/**
	 * @return the gem
	 */
	// Team-Ctrl-S(Currency)
	public final int getGem() {
		return gem;
	}
}
