package Enemy;

import java.awt.Color;
import entity.EnemyShip;
import engine.DrawManager.SpriteType;

public class HpEnemyShip {

    private static double magentaProbability = 0.1; //set basic probability

    /**
     * set the probability of being a MAGENTA color.
     *
     * @param probability the probability to a value between 0 and 1.
     */

    public static void setItemEnemyProbability(double probability) {
        if (probability >= 0.0 && probability <= 1.0) {
            magentaProbability = probability;
        } else {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        }
    }

    /**
     * returns the probability of being the current MAGENTA color.
     *
     * @return current probability.
     */
    public static double getItemEnemyProbability() {
        return magentaProbability;
    }

    /**
     * Determine the color of the ship according to hp
     * @param hp
     * 			The ship's hp
     * @return if hp is 2, return yellow
     * 		   if hp is 3, return orange
     * 		   if hp is 1, return white
     */
    public static Color determineColor(SpriteType spriteType) {
        switch (spriteType) {
            case MID_BOSS1:
            case MID_BOSS2:
                return new Color(128, 0, 128); // 중간 보스는 보라색
            case FINAL_BOSS1:
            case FINAL_BOSS2:
                return Color.RED; // 최종 보스는 빨간색
            default:
                return Color.WHITE; // 기본 적은 흰색
        }
    }

    /**
     * When the EnemyShip is hit and its hp reaches 0, destroy the ship
     * @param enemyShip
     *          The ship that was hit
     */
    public static void hit(EnemyShip enemyShip) {
        int hp = enemyShip.getHp();
        hp -= 1;
        enemyShip.setHp(hp);
        if (hp <= 0) {
            enemyShip.destroy();
        }
        enemyShip.setColor(determineColor(enemyShip.getType()));
    }


}
