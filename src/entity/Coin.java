package entity;

import engine.DrawManager;

import java.awt.*;

public class Coin extends Entity {
    public Coin() {
        super(0,0, 5 * 2, 5 * 2, Color.YELLOW);

        this.spriteType = DrawManager.SpriteType.Coin;
    }
}
