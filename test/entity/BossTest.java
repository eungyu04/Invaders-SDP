package entity;

import Sound_Operator.SoundManager;
import engine.DrawManager;
import engine.GameSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BossTest {
    private GameSettings mockGameSettings;
    private SoundManager mockSoundManager;
    private Boss middleBoss;
    private Boss finalBoss;

    @BeforeEach
    void setup() {
        mockGameSettings = mock(GameSettings.class);
        when(mockGameSettings.getEnemyFrequency()).thenReturn(100);

        mockSoundManager = mock(SoundManager.class);

        middleBoss = new MiddleBoss(4, mockGameSettings);
        middleBoss.setSoundManager(mockSoundManager);
        finalBoss = new FinalBoss(8, mockGameSettings);
        finalBoss.setSoundManager(mockSoundManager);
    }

    @Test
    void testBossInitialization_level4() {
        assertEquals(30, middleBoss.getMaxHp());
        assertEquals(30, middleBoss.getCurrentHp());
        assertEquals(DrawManager.SpriteType.middleBoss, middleBoss.getSpriteType());
    }

    @Test
    void testBossInitialization_Level8() {
        assertEquals(120, finalBoss.getMaxHp());
        assertEquals(120, finalBoss.getCurrentHp());
        assertEquals(DrawManager.SpriteType.finalBoss, finalBoss.getSpriteType());
    }

    @Test
    void testTakeDamage() {
        middleBoss.takeDamage(10);
        assertEquals(20, middleBoss.getCurrentHp());

        middleBoss.takeDamage(25);
        assertTrue(middleBoss.isBossDead());

        finalBoss.takeDamage(10);
        assertEquals(110, finalBoss.getCurrentHp());

        finalBoss.takeDamage(115);
        assertTrue(finalBoss.isBossDead());
    }

    @Test
    void testLifeSteal() {
        middleBoss.takeDamage(20);
        middleBoss.lifeSteal();

        assertEquals(10 + 3, middleBoss.getCurrentHp());

        finalBoss.takeDamage(70);
        finalBoss.lifeSteal();

        assertEquals(50 + 24, finalBoss.getCurrentHp());
    }

    @Test
    void testEnragedMode() {
        middleBoss.enragedMode();
        middleBoss.takeDamage(20);
        assertFalse(middleBoss.isEnraged());

        middleBoss.takeDamage(1);
        assertTrue(middleBoss.isEnraged());

        finalBoss.enragedMode();
        finalBoss.takeDamage(80);
        assertFalse(finalBoss.isEnraged());

        finalBoss.takeDamage(5);
        assertTrue(finalBoss.isEnraged());
    }

    @Test
    void testCalmDown() {
        middleBoss.enragedMode();
        middleBoss.calmDown();

        assertFalse(middleBoss.isEnraged());

        finalBoss.enragedMode();
        finalBoss.calmDown();

        assertFalse(finalBoss.isEnraged());
    }
}
