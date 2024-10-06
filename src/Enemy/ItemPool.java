package Enemy;

import java.util.HashSet;
import java.util.Set;

public final class ItemPool {
    private static Set<Item> pool = new HashSet<Item>();
    private ItemPool() {
    }
    public static Item getBullet(final int positionX,
                                 final int positionY, final int speed, final int type) {
        Item item;
        if (!pool.isEmpty()) {
            item = pool.iterator().next();
            pool.remove(item);
            item.setPositionX(positionX - item.getWidth() / 2);
            item.setPositionY(positionY);
            item.setSpeed(speed);
        } else {
            item = new Item(positionX, positionY, speed, type);
            item.setPositionX(positionX - item.getWidth() / 2);
        }
        return item;
    }
    public static void recycle(final Set<Item> item) {
        pool.addAll(item);
    }
}