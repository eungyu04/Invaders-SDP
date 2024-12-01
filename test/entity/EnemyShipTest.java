package entity;

import Sound_Operator.SoundManager;
import entity.EnemyShip;
import engine.DrawManager.SpriteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EnemyShipTest {

    private EnemyShip enemyShip;
    private SoundManager soundManagerMock;

    @BeforeEach
    public void setUp() {
        soundManagerMock = mock(SoundManager.class);
    }

    @Test
    public void testDestroy_EnemyShipSpecial() {
        enemyShip = new EnemyShip(0, 0, SpriteType.EnemyShipSpecial);
        enemyShip.setSoundManager(soundManagerMock);

        enemyShip.destroy();

        assertTrue(enemyShip.isDestroyed(), "EnemyShipSpecial should be destroyed.");
        assertEquals(SpriteType.Explosion, enemyShip.getSpriteType(), "SpriteType should be set to Explosion after destruction.");
    }

    @Test
    public void testDestroy_StoryModeEnemyShipD1() {
        enemyShip = new EnemyShip(0, 0, SpriteType.EnemyShipD1);
        enemyShip.setSoundManager(soundManagerMock);

        enemyShip.destroy();

        assertTrue(enemyShip.isDestroyed(), "StoryModeEnemyShipD1 should be destroyed.");
        assertEquals(SpriteType.ExplosionD3, enemyShip.getSpriteType(), "SpriteType should be set to Boss after destruction.");
    }

    @Test
    public void testDestroy_BasicEnemyShip() {
        enemyShip = new EnemyShip(0, 0, SpriteType.EnemyShipA1);
        enemyShip.setSoundManager(soundManagerMock);

        enemyShip.destroy();

        assertTrue(enemyShip.isDestroyed(), "BasicEnemyShip should be destroyed.");
        assertEquals(SpriteType.Explosion, enemyShip.getSpriteType(), "SpriteType should be set to Explosion after destruction.");
    }
}
