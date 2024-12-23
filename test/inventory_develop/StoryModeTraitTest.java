package inventory_develop;

import CtrlS.UpgradeManager;
import engine.Core;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class StoryModeTraitTest {
    // StoryModeTrait 테스트 -> UpgradeManager에 대한 테스트는 새로운 클래스에 작성
    StoryModeTrait storyModeTrait;

    @BeforeEach
    void setup() {
        storyModeTrait = new StoryModeTrait();
    }

    @Test
    @DisplayName("생성자 테스트")
    void constructTest() {
        assertEquals("basic", storyModeTrait.getTraits().get("BulletDamage"));
        assertEquals("basic", storyModeTrait.getTraits().get("BulletCount"));
        assertEquals("basic", storyModeTrait.getTraits().get("BulletSpeed"));
        assertEquals("basic", storyModeTrait.getTraits().get("ShipSpeed"));
        assertEquals("common", storyModeTrait.getTraits().get("ShipShoot360"));
    }

    @Test
    @DisplayName("getRandomTraits 테스트")
    void getRandomTraitsTest() {
        // 레벨 3
        String[] traitsLevel3 = storyModeTrait.getRandomTraits(1);
        for (String type : traitsLevel3) {
            // key값(type)이 제대로 되었는지 확인
            assertTrue(storyModeTrait.getTraits().containsKey(type),
                    "뽑힌 trait가 getTraits()의 키에 포함되어야 합니다.");

            // value값(rarity)이 제대로 되었는지 확인
            String rarity = storyModeTrait.getTraits().get(type);
            assertTrue(rarity.equals("basic") || rarity.equals("common"),
                    "레벨 1에서는 basic 또는 common 만 나와야 함.");
        }

        // 레벨 5
        String[] traitsLevel5 = storyModeTrait.getRandomTraits(5);
        for (String type : traitsLevel5) {

            // key값(type)이 제대로 되었는지 확인
            assertTrue(storyModeTrait.getTraits().containsKey(type),
                    "뽑힌 trait가 getTraits()의 키에 포함되어야 합니다.");

            // value값(rarity)이 제대로 되었는지 확인
            String rarity = storyModeTrait.getTraits().get(type);
            assertTrue(rarity.equals("basic") || rarity.equals("common") || rarity.equals("rare"),
                    "레벨 5에서는 basic, common, rare 만 나와야 합니다.");
        }


        // 개수 확인
        assertEquals(3, traitsLevel3.length, "결과 배열의 길이는 요청한 3이어야 합니다.");
        assertEquals(3, traitsLevel5.length, "결과 배열의 길이는 요청한 3이어야 합니다.");

        // 중복 확인
        long uniqueCount = Arrays.stream(traitsLevel3).distinct().count();
        assertEquals(3, uniqueCount, "결과 배열에 중복된 값이 없어야 합니다.");
        uniqueCount = Arrays.stream(traitsLevel5).distinct().count();
        assertEquals(3, uniqueCount, "결과 배열에 중복된 값이 없어야 합니다.");

        // 출력해봄
        for (int i = 0; i < 3; i ++) {
            System.out.print(traitsLevel3[i] + " : ");
            System.out.println(storyModeTrait.getTraits().get(traitsLevel3[i]));
        }
    }

    @Test
    @DisplayName("trait 적용 테스트1")
    void selectedTraitsTest1() {
        // BulletDamage
        assertEquals("basic", storyModeTrait.getTraits().get("BulletDamage"));
        storyModeTrait.upgradeSelectedTraits("BulletDamage");
        assertEquals("common", storyModeTrait.getTraits().get("BulletDamage"));
        storyModeTrait.upgradeSelectedTraits("BulletDamage");
        assertEquals("rare", storyModeTrait.getTraits().get("BulletDamage"));
        storyModeTrait.upgradeSelectedTraits("BulletDamage");
        assertEquals("epic", storyModeTrait.getTraits().get("BulletDamage"));

        // ShipShoot360
        assertEquals("common", storyModeTrait.getTraits().get("ShipShoot360"));
        storyModeTrait.upgradeSelectedTraits("ShipShoot360");
        assertEquals("rare", storyModeTrait.getTraits().get("ShipShoot360"));
    }

    @Test
    @DisplayName("trait 적용 테스트2")
    void selectedTraitsTest2() {
        assertDoesNotThrow(() -> { storyModeTrait.upgradeSelectedTraits("BulletSpeed");});
        assertDoesNotThrow(() -> { storyModeTrait.upgradeSelectedTraits("BulletSpeed");});
        assertDoesNotThrow(() -> { storyModeTrait.upgradeSelectedTraits("BulletSpeed");});
        assertDoesNotThrow(() -> { storyModeTrait.upgradeSelectedTraits("BulletSpeed");});  //없음
        assertDoesNotThrow(() -> { storyModeTrait.upgradeSelectedTraits("ShipShoot360");});
        assertThrows(NullPointerException.class, () -> storyModeTrait.upgradeSelectedTraits("ShipShoot360")); //없음
    }
}
