package entity;

import entity.EnemyShip;
import engine.DrawManager.SpriteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyShipTest {

    private EnemyShip enemyShip;

    @BeforeEach
    public void setUp() {}

    @Test
    public void testDestroy_EnemyShipSpecial() {

        enemyShip = new EnemyShip(0, 0, SpriteType.EnemyShipSpecial);

        enemyShip.destroy();

        assertTrue(enemyShip.isDestroyed(), "EnemyShipSpecial should be destroyed.");
        assertEquals(SpriteType.Explosion, enemyShip.getSpriteType(), "SpriteType should be set to Explosion after destruction.");
    }

    @Test
    public void testDestroy_StoryModeEnemyShipD1() {

        enemyShip = new EnemyShip(0, 0, SpriteType.EnemyShipD1);

        enemyShip.destroy();

        assertTrue(enemyShip.isDestroyed(), "StoryModeEnemyShipD1 should be destroyed.");
        assertEquals(SpriteType.Boss, enemyShip.getSpriteType(), "SpriteType should be set to Boss after destruction.");
    }

    @Test
    public void testDestroy_BasicEnemyShip() {
        enemyShip = new EnemyShip(0, 0, SpriteType.EnemyShipA1);

        enemyShip.destroy();

        assertTrue(enemyShip.isDestroyed(), "BasicEnemyShip should be destroyed.");
        assertEquals(SpriteType.Explosion, enemyShip.getSpriteType(), "SpriteType should be set to Explosion after destruction.");
    }
}
