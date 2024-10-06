package inventory_develop;

import engine.DrawManager;
import entity.EnemyShip;
import entity.Entity;
import java.awt.Color;
import java.util.List;
import java.util.logging.Logger;

public class Bomb extends Entity {

    private int BombSpeed;
    // Boob를 먹을 때 true로 전환할 예정
    private static boolean IsBomb = false;
    // Boob를 먹을 때 true로 전환할 예정

    private static boolean CanShoot = true;

    public Bomb(final int positionX, final int positionY, final int speed) {
         super(positionX, positionY, 9 * 2, 11 * 2, Color.BLACK);

        this.BombSpeed = speed;
    }

    public static boolean setBomb(){
        return IsBomb;
    }

    public static void destroyByBomb(List<List<EnemyShip>> enemyShips, EnemyShip destroyedShip, Logger logger) {
        for (List<EnemyShip> column : enemyShips)
            for (int i = 0; i < column.size(); i++) {
                if (column.get(i).equals(destroyedShip)) {
                    column.get(i).destroy();

                    int columnIndex = enemyShips.indexOf(column);

                    // 좌측 함선 파괴
                    if (columnIndex > 0) {
                        List<EnemyShip> leftColumn = enemyShips.get(columnIndex - 1);
                        if (i < leftColumn.size()) {
                            leftColumn.get(i).destroy();
                            logger.info("Destroyed left ship at (" + (columnIndex - 1) + "," + i + ")");
                        }
                    }

                    // 우측 함선 파괴
                    if (columnIndex < enemyShips.size() - 1) {
                        List<EnemyShip> rightColumn = enemyShips.get(columnIndex + 1);
                        if (i < rightColumn.size()) {
                            rightColumn.get(i).destroy();
                            logger.info("Destroyed right ship at (" + (columnIndex + 1) + "," + i + ")");
                        }
                    }

                    // 상단 함선 파괴
                    if (i > 0) {
                        List<EnemyShip> currentColumn = enemyShips.get(columnIndex);
                        currentColumn.get(i - 1).destroy();
                        logger.info("Destroyed top ship at (" + columnIndex + "," + (i - 1) + ")");
                    }

                    // 하단 우주선 파괴
                    List<EnemyShip> currentColumn = enemyShips.get(columnIndex);
                    if (i < currentColumn.size() - 1) {
                        currentColumn.get(i + 1).destroy();
                        logger.info("Destroyed bottom ship at (" + columnIndex + "," + (i + 1) + ")");
                    }
                }
            }
        removeBombItem();
    }

    public static void removeBombItem() {
        IsBomb = false;
    }

    public static boolean getIsBomb() {
        return IsBomb;
    }

    public static void setIsbomb(boolean isbomb) {
        IsBomb = isbomb;
    }

    public static boolean getCanShoot() {
        return CanShoot;
    }

    public static void setCanShoot(boolean canshoot) {
        CanShoot = canshoot;
    }

    public final void setSpeed(final int BoobSpeed) {this.BombSpeed = BoobSpeed;}

    public final int getSpeed() {return this.BombSpeed;}

}