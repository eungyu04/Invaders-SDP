package engine;

/**
 * Implements an object that stores a single game's difficulty settings.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameSettings {

	/** Width of the level's enemy formation. */
	private int formationWidth;
	/** Height of the level's enemy formation. */
	private int formationHeight;
	/** Speed of the enemies, function of the remaining number. */
	private int baseSpeed;
	/** Frequency of enemy shootings, +/- 30%. */
	private static int shootingFrequency;
	/** Frequency of enemy. */
	public int enemyFrequency;
	/** Level Design team modification
	 * Number of enemy ships waves during the level **/
	private int wavesNumber;

	private String gametype;
	private int level;

	/**
	 * Constructor.
	 * 
	 * @param formationWidth
	 *            Width of the level's enemy formation.
	 * @param formationHeight
	 *            Height of the level's enemy formation.
	 * @param baseSpeed
	 *            Speed of the enemies.
	 * @param shootingFrequency
	 *            Frequency of enemy shootings, +/- 30%.
	 * @param wavesNumber
	 * 				Number of waves in the level (Added by the Level Design team)
	 */
	public GameSettings(final int formationWidth, final int formationHeight,
			final int baseSpeed, final int shootingFrequency, int wavesNumber) {
		this.formationWidth = formationWidth;
		this.formationHeight = formationHeight;
		this.baseSpeed = baseSpeed;
		this.shootingFrequency = shootingFrequency;

		/** Added by the Level Design team **/
		this.wavesNumber = wavesNumber;
		this.gametype = "Normal";
	}

	public GameSettings(final int formationWidth, final int enemyFrequency,
						final int baseSpeed, final int shootingFrequency) {
		this.formationWidth = 1;
		this.formationHeight = formationWidth;
		this.enemyFrequency = enemyFrequency;
		this.baseSpeed = baseSpeed;
		this.shootingFrequency = shootingFrequency;

		/** Added by the Level Design team **/

		this.gametype = "Story";
	}

	/**
	 * @return the formationWidth
	 */
	public final int getFormationWidth() {
		return formationWidth;
	}

	/**
	 * @return the formationHeight
	 */
	public final int getFormationHeight() {
		return formationHeight;
	}

	/**
	 * @return the baseSpeed
	 */
	public final int getBaseSpeed() {
		return baseSpeed;
	}

	/**
	 * @return the shootingFrequency
	 */
	public final int getShootingFrequency() {
		return shootingFrequency;
	}

	public static void setShootingFrequency(int shooting_frequency) {
		shootingFrequency = shooting_frequency;
	}

	/**
	 * Added by the Level Design team
	 * @return the wavesNumber
	 */
	public final int getWavesNumber() {
		return wavesNumber;
	}

	public int getEnemyFrequency(){
		return enemyFrequency;
	}

	public String getGameType(){
		return gametype;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
}
