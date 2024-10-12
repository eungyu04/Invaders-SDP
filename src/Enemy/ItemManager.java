package Enemy;

import engine.Core;
import entity.EnemyShip;
import entity.Ship;
import inventory_develop.Bomb;
import inventory_develop.FeverTimeItem;
import screen.GameScreen;
import engine.DrawManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

//import inventory_develop.Bomb;
import inventory_develop.ItemBarrierAndHeart;
import inventory_develop.NumberOfBullet;

import CtrlS.CurrencyManager;


public class ItemManager {

    public Set<Item> items;
    private int screenHeight;
    private DrawManager drawManager;
    private GameScreen gameScreen;
    protected Logger logger = Core.getLogger();
    private Set<Item> recyclableItems = new HashSet<>();
    private ItemBarrierAndHeart Item2;
    private NumberOfBullet numberOfBullet;
    private Ship ship;
    private PlayerGrowth growth;
    private FeverTimeItem feverTimeItem;
    private CurrencyManager currencyManager;

    public ItemManager(int screenHeight, DrawManager drawManager, GameScreen gameScreen) {
        this.items = new HashSet<>();
        this.screenHeight = screenHeight;
        this.drawManager = drawManager;
        this.gameScreen = gameScreen;
        this.ship = gameScreen.getShip();       // Team Inventory
        this.growth = ship.getPlayerGrowth();
        this.Item2 = gameScreen.getItem();
        this.feverTimeItem = gameScreen.getFeverTimeItem();
        this.numberOfBullet = new NumberOfBullet();
    }

    public void cleanItems() {
        Set<Item> recyclable = new HashSet<>();
        for (Item item : this.items) {
            item.update();
            if (item.getPositionY() > screenHeight) {
                recyclable.add(item);
            }
        }
        this.items.removeAll(recyclable);
        ItemPool.recycle(recyclable);
    }

    public void initialize() {
        this.items = new HashSet<>();
    }

    public void drawItems() {
        for (Item item : this.items) {
            drawManager.drawEntity(item, item.getPositionX(), item.getPositionY());
        }
    }

    public void dropItem(EnemyShip enemyShip, double probability, int enemyship_type) {
        if(Math.random() < probability) {
            Item item = ItemPool.getItem(enemyShip.getPositionX(), enemyShip.getPositionY(), 3, enemyship_type);
            this.items.add(item);
        }
    }

    // team Inventory
    public void OperateItem(Item item) {
        if(item!= null) {

            DrawManager.SpriteType whatItem = item.getSpriteType();

            switch (whatItem) {     // Operates according to the SpriteType of the item.
                case ItemBomb:
                    Bomb.setIsbomb(true);
                    Bomb.setCanShoot(true);
                    break;
                case ItemBarrier:
                    Item2.activatebarrier();
                    break;
                case ItemHeart:
                    Item2.activeheart(gameScreen, ship, growth);
                    break;
                case ItemFeverTime: // 피버타임 아이템일 경우
                    feverTimeItem.activate();
                    break;
                case ItemPierce:
                    numberOfBullet.pierceup();
                    ship.increaseBulletSpeed();
                    break;
                case ItemCoin:
                    try {
                        Core.getCurrencyManager().addCurrency(10);
                        logger.info("You get coin (10$)");
                    } catch (IOException e) {
                        logger.warning("Couldn't load currency!");
                    }
            }

            addItemRecycle(item);
        }
    }

    public void addItemRecycle(Item item) {
        recyclableItems.add(item);
        String itemLog = item.getSpriteType().toString().toLowerCase().substring(4);
        if (!itemLog.equals("coin")) {
            this.logger.info("get " + itemLog + " item");   // Change log for each item
        }
    }

    public void removeAllReItems(){
        this.items.removeAll(recyclableItems);
        ItemPool.recycle(recyclableItems);
    }

}
