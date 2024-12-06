package entity;

import Enemy.PiercingBullet;
import Sound_Operator.SoundManager;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.GameSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EnemyShipFormationTest {
    EnemyShipFormation enemyShipFormation;
    SoundManager mockSoundManager;

    @BeforeEach
    void SetUp() {
        mockSoundManager = mock(SoundManager.class);

        GameSettings gameSettings = new GameSettings(20, 1500, 600, 1500);

        enemyShipFormation = new EnemyShipFormation(gameSettings);
        enemyShipFormation.setSoundManager(mockSoundManager);
    }

    @Test
    void testShoot() {
        Set<PiercingBullet> bullets = new HashSet<>();

        enemyShipFormation.shoot(bullets);

        assertFalse(bullets.isEmpty(), "Bullets should be added after shooting.");
        for (PiercingBullet bullet : bullets) {
            assertNotNull(bullet, "Bullet can't null");
        }

        for (Cooldown cooldown : enemyShipFormation.getShootingCooldown()) {
            assertFalse(cooldown.checkFinished(), "Cooldowns should be reset after shooting.");
        }
    }

    @Test
    void testShootByEnemyType() {
        Set<PiercingBullet> bullets = new HashSet<>();

        // Setup: Add different enemy types to shooters
        EnemyShip enemyTypeD = new EnemyShip(100, 100, DrawManager.SpriteType.EnemyShipD1, 1, 0, 0);
        EnemyShip enemyTypeE = new EnemyShip(200, 100, DrawManager.SpriteType.EnemyShipE1, 1, 0, 0);
        EnemyShip enemyTypeF = new EnemyShip(300, 100, DrawManager.SpriteType.EnemyShipF1, 1, 0, 0);

        // enemyShipFormation의 Shooters와 ShootingCooldown 초기화
        enemyShipFormation.getShooters().clear();
        enemyShipFormation.getShootingCooldown().clear();
        
        // 각각의 EnemyShip과 Cooldown 임의로 추가하기
        enemyShipFormation.getShooters().add(enemyTypeD);
        enemyShipFormation.getShootingCooldown().add(Core.getVariableCooldown(10, 10));
        enemyShipFormation.getShooters().add(enemyTypeE);
        enemyShipFormation.getShootingCooldown().add(Core.getVariableCooldown(10, 10));
        enemyShipFormation.getShooters().add(enemyTypeF);
        enemyShipFormation.getShootingCooldown().add(Core.getVariableCooldown(10, 10));
        
        enemyShipFormation.shoot(bullets);

        // 총알 갯수
        assertEquals(9, bullets.size(), "Bullets should be created based on enemy types (3 + 5 + 1).");

        // 속력에 따른 총알 개수 Count (속도 값이 int라서 그에 맞추어 속력은 보정)
        // type 1) 속도 보통, 총알 3개
        long typeDBullets = bullets.stream()
                .filter(b -> (Math.sqrt(Math.pow(b.getSpeedX(), 2) + Math.pow(b.getSpeedY(), 2)) >= 3
                        && ( Math.sqrt(Math.pow(b.getSpeedX(), 2) + Math.pow(b.getSpeedY(), 2)) <= 3.2 ))
                        && b.getBulletType() == 2)
                .count();
        assertEquals(3, typeDBullets, "Type D enemy should shoot 3 bullets with normal speed.");

        // type 2) 속도 느림, 총알 5개
        long typeEBullets = bullets.stream()
                .filter(b -> (Math.sqrt(Math.pow(b.getSpeedX(), 2) + Math.pow(b.getSpeedY(), 2)) >= 1.7
                        && ( Math.sqrt(Math.pow(b.getSpeedX(), 2) + Math.pow(b.getSpeedY(), 2)) <= 2.3 ))
                        && b.getBulletType() == 2)
                .count();
        assertEquals(5, typeEBullets, "Type E enemy should shoot 5 bullets with slow speed.");

        // type 3) 속도 빠름, 총알 1개
        long typeFBullets = bullets.stream()
                .filter(b -> (Math.sqrt(Math.pow(b.getSpeedX(), 2) + Math.pow(b.getSpeedY(), 2)) >= 5
                        && ( Math.sqrt(Math.pow(b.getSpeedX(), 2) + Math.pow(b.getSpeedY(), 2)) <= 5.5 ))
                        && b.getBulletType() == 3)
                .count();
        assertEquals(1, typeFBullets, "Type F enemy should shoot 1 bullet with fast speed and aimed at player.");
    }

}
