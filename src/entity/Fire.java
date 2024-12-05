package entity;

import java.awt.Color;
import engine.DrawManager.SpriteType;

public class Fire extends Entity {
    public Fire(final int positionX, final int positionY) {
        super(positionX, positionY, 12 * 2, 12 * 2, Color.RED);
        this.spriteType = SpriteType.fire;
    }

    public final void update() {
        int speed = 1;
        this.positionY += speed;
    }
}