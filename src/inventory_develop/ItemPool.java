package inventory_develop;

import java.util.HashSet;
import java.util.Set;

public class ItemPool {

    /** Set of already created Items. */
    private static Set<ItemOnetime> pool = new HashSet<ItemOnetime>();

    /**
     * Constructor, not called.
     */
    private ItemPool() {

    }

    /**
     * Returns Item from the pool if one is available, a new one if there
     * isn't.
     *
     * @param positionX
     *            Requested position of the Item in the X axis.
     * @param positionY
     *            Requested position of the Item in the Y axis.
     * @param speed
     *            Requested speed of the Item, positive or negative depending
     *            on direction - positive is down.
     * @return Requested Item.
     */
    public static ItemOnetime getItem(final int positionX,
                                   final int positionY, final int speed) {
        ItemOnetime Item;
        if (!pool.isEmpty()) {
            Item = pool.iterator().next();
            pool.remove(Item);
            Item.setPositionX(positionX - Item.getWidth() / 2);
            Item.setPositionY(positionY);
            Item.setSpeed(speed);
            Item.randomItem();
        } else {
            Item = new ItemOnetime(positionX, positionY, speed);
            Item.setPositionX(positionX - Item.getWidth() / 2);
        }
        return Item;
    }

    /**
     * Adds one or more ItemOnetimes to the list of available ones.
     *
     * @param ItemOnetime
     *            ItemOnetimes to recycle.
     */
    public static void recycle(final Set<ItemOnetime> ItemOnetime) {
        pool.addAll(ItemOnetime);
    }
}
