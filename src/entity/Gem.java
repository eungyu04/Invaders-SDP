package entity;

import engine.DrawManager;

import java.awt.*;

public class Gem extends Entity {
    public Gem() {
        super(0,0, 5 * 2, 5 * 2, Color.CYAN);

        this.spriteType = DrawManager.SpriteType.Gem;
    }
}