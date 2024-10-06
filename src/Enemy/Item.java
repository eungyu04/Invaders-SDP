package Enemy;

import java.awt.Color;

import engine.DrawManager.SpriteType;
import entity.Entity;

public class Item extends Entity {
    private int speed;
    public Item(final int positionX, final int positionY, final int speed, final int type) {
        super(positionX, positionY, 3 * 2, 5 * 2, Color.yellow);
        this.speed = speed;
        this.spriteType = SpriteType.Item;
    }
    public final void update() {
        this.positionY += this.speed;
    }
    public final void setSpeed(final int speed) {
        this.speed = speed;
    }
    public final int getSpeed() {
        return this.speed;
    }
}
