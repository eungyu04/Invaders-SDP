package CtrlS;

import engine.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UpgradeManagerTest {
    // UpgradeManager의 Properties에 대한 getter, setter 체크 테스트

    @Mock
    private FileManager fileManagerMock;

    @InjectMocks
    private UpgradeManager upgradeManager;

    private Properties initialProperties;

    @BeforeEach
    void setUp() throws IOException {
        // Mock FileManager
        fileManagerMock = mock(FileManager.class);

        // Set up initial properties
        initialProperties = new Properties();
        initialProperties.setProperty("bullet_damage", "1");
        initialProperties.setProperty("bullet_num", "1");
        initialProperties.setProperty("bullet_speed", "10");
        initialProperties.setProperty("bullet_interval", "800");
        initialProperties.setProperty("movement_speed", "3.5");
        initialProperties.setProperty("ship_shoot360", "false");

        // Mock FileManager behavior
        when(fileManagerMock.loadUpgradeStatus()).thenReturn(initialProperties);

        // Inject the mock into UpgradeManager
        upgradeManager = UpgradeManager.getInstance();      // 일단 UpgradeManager 인스턴스(생성)
        UpgradeManager.setFileManager(fileManagerMock);     // fileManagerMock을 set 해놓고
        upgradeManager = UpgradeManager.getInstance();      // 다시 UpgradeManager 인스턴스(변경?)
    }

    @Test
    void testGetBulletDamage() throws IOException {
        int bulletDamage = upgradeManager.getBulletDamage();
        assertEquals(1, bulletDamage, "Bullet damage should be initialized to 1");
    }

    @Test
    void testSetBulletDamage() throws IOException {
        // Act
        upgradeManager.setBulletDamage(5);

        // Assert
        verify(fileManagerMock, times(1)).saveUpgradeStatus(any(Properties.class)); // saveUpgradeStatus 호출 확인
        assertEquals("5", initialProperties.getProperty("bullet_damage"));
    }


    @Test
    void testGetBulletCount() throws IOException {
        int bulletCount = upgradeManager.getBulletNum();
        assertEquals(1, bulletCount, "Bullet count should be initialized to 1");
    }

    @Test
    void testSetBulletCount() throws IOException {
        upgradeManager.setBulletNum(3);

        verify(fileManagerMock, times(1)).saveUpgradeStatus(any(Properties.class));
        assertEquals("3", initialProperties.getProperty("bullet_num"), "Bullet count should be updated to 3");
    }

    @Test
    void testGetBulletSpeed() throws IOException {
        int bulletSpeed = upgradeManager.getBulletSpeed();
        assertEquals(10, bulletSpeed, "Bullet speed should be initialized to 10");
    }

    @Test
    void testSetBulletSpeed() throws IOException {
        upgradeManager.setBulletSpeed(15);

        verify(fileManagerMock, times(1)).saveUpgradeStatus(any(Properties.class));
        assertEquals("15", initialProperties.getProperty("bullet_speed"), "Bullet speed should be updated to 15");
    }

    @Test
    void testGetBulletInterval() throws IOException {
        int bulletInterval = upgradeManager.getBulletInterval();
        assertEquals(800, bulletInterval, "Bullet interval should be initialized to 800");
    }

    @Test
    void testSetBulletInterval() throws IOException {
        upgradeManager.setBulletInterval(700);

        verify(fileManagerMock, times(1)).saveUpgradeStatus(any(Properties.class));
        assertEquals("700", initialProperties.getProperty("bullet_interval"), "Bullet interval should be updated to ");
    }

    @Test
    void testGetMovementSpeed() throws IOException {
        double movementSpeed = upgradeManager.getMovementSpeed();
        assertEquals(3.5, movementSpeed, "Movement speed should be initialized to 3.5");
    }

    @Test
    void testSetMovementSpeed() throws IOException {
        upgradeManager.setMovementSpeed(5.0);

        verify(fileManagerMock, times(1)).saveUpgradeStatus(any(Properties.class));
        assertEquals("5.0", initialProperties.getProperty("movement_speed"), "Movement speed should be updated to 5.0");
    }

    @Test
    void testGetShipShoot360() throws IOException {
        boolean shipShoot360 = upgradeManager.getShipShoot360();
        assertEquals(false, shipShoot360, "shipShoot360 should be initialized to false");
    }

    @Test
    void testSetShipShoot360() throws IOException {
        upgradeManager.setShipShoot360(true);

        verify(fileManagerMock, times(1)).saveUpgradeStatus(any(Properties.class));
        assertEquals("true", initialProperties.getProperty("ship_shoot360"), "shipShoot360 should be updated to true");
    }
}
