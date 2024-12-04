package inventory_develop;

import engine.Core;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class StoryModeTrait {
    private static Logger logger;

    // basic - normal - rare - epic
    private final Map<String, Integer> trait_BulletDamage
            = Map.of("basic", 1, "common", 2, "rare", 3,"epic",5);
    private final Map<String, Integer> trait_BulletCount
            = Map.of("basic", 1, "common", 2, "rare", 3,"epic", 5);
    private final Map<String, Integer> trait_BulletSpeed
            = Map.of("basic", 5, "common", 7, "rare",  10,"epic", 15);
    private final Map<String, Double> trait_ShipSpeed
            = Map.of("basic", 3.5, "common", 4.0, "rare", 5.0,"epic",6.0);
    private final Map<String, Boolean> trait_ShipShoot360
            = Map.of("common", false, "rare", true);

    private Map<String, String> traits = new HashMap<>();

    // Construct
    public StoryModeTrait() {
        logger = Core.getLogger();
        // 특성 초기화
        traits.put("BulletDamage", "basic");
        traits.put("BulletCount", "basic");
        traits.put("BulletSpeed", "basic");
        traits.put("ShipSpeed", "basic");
        traits.put("ShipShoot360", "common");   // 360도 발사는 rare에만 있기 때문에 초기 값을 common으로 지정

        update();
    }

    // 랜덤으로 세 특성을 뽑는 부분
    public String[] getRandomTraits(int level) {
        HashSet<String> randomTraits = new HashSet<>(); // 랜덤으로 나온 3개를 반환할 HashSet
        List<String> roulette = new ArrayList<>();      // rarity에 따른 확률로 뽑기위해 필요한 ArrayList

        switch(level) {
            // 1~3 -> 노말4, 레어3, 에픽0
            case 1:
            case 2:
            case 3:
                for (String type : traits.keySet()) {
                    String rarity = traits.get(type);

                    if (rarity.equals("basic")) {
                        for (int i = 0; i < 4; i++) {   // 4번 추가
                            roulette.add(type);
                        }
                    } else if (rarity.equals("common")) {
                        for (int i = 0; i < 3; i++) {   // 3번 추가
                            roulette.add(type);
                        }
                    }
                }
                break;

            // 5~7 -> 노말1, 레어3, 에픽3
            case 4:
            case 5:
            case 6:
            case 7:
                for (String type : traits.keySet()) {
                    String rarity = traits.get(type);
                    if (rarity.equals("basic")) {
                        for (int i = 0; i < 1; i++) {   // 1번 추가
                            roulette.add(type);
                        }
                    } else if (rarity.equals("common")) {   // 3번 추가
                        for (int i = 0; i < 3; i++) {
                            roulette.add(type);
                        }
                    } else if (rarity.equals("rare") && !type.equals("ShipShoot360")) { // 3번 추가
                        for (int i = 0; i < 3; i++) {
                            roulette.add(type);
                        }
                    }
                }
                break;
        }

        Collections.shuffle(roulette);

        for (int i = 0; i < 3 && !roulette.isEmpty(); i++) {
            String chosenTrait;
            do {
                chosenTrait = roulette.get(new Random().nextInt(roulette.size()));
            } while (randomTraits.contains(chosenTrait)); // 중복 방지
            randomTraits.add(chosenTrait);
        }

        return randomTraits.toArray(new String[0]);
    }

    // 선택된 특성을 traits에 적용
    public void upgradeSelectedTraits(String type) {
        String rarity = traits.get(type);
        String beforerarity = rarity;

        rarity = setNextRarity(rarity);

        traits.put(type, rarity);

        logger.info("User's " + type + " changed from " + beforerarity + " to " + rarity);
        update();
    }

    public String setNextRarity(String rarity) {
        if (rarity.equals("basic")) return "common";
        else if (rarity.equals("common")) return "rare";
        else if (rarity.equals("rare")) return "epic";
        return rarity;
    }

    // update() - 다른 update()들과 다르게 필요 시에만 작동하게 적용
    public void update() {
        int newBulletDamage = trait_BulletDamage.get(traits.get("BulletDamage"));
        int newBulletCount = trait_BulletCount.get(traits.get("BulletCount"));
        int newBulletSpeed = trait_BulletSpeed.get(traits.get("BulletSpeed"));
        double newShipSpeed = trait_ShipSpeed.get(traits.get("ShipSpeed"));
        boolean newShipShoot360 = trait_ShipShoot360.get(traits.get("ShipShoot360"));

        try {
            Core.getUpgradeManager().setBulletDamage(newBulletDamage);  // NumberOfBullet
            Core.getUpgradeManager().setBulletNum(newBulletCount);      // NumberOfBullet
            Core.getUpgradeManager().setBulletSpeed(newBulletSpeed);    // Ship -> 이것도 NumberOfBullet으로 옮기고 싶지만 총알 속도가 올라가는 아이템이 있으니 나중에
            Core.getUpgradeManager().setMovementSpeed(newShipSpeed);    // Ship
            Core.getUpgradeManager().setShipShoot360(newShipShoot360);    //360도발사 Ship or GameScreen
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Map<String, String> getTraits() {
        return traits;
    }
}
