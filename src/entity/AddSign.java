package entity;

import engine.DrawManager;

import java.awt.*;

public class AddSign extends Entity{
    public AddSign() {

        super(0,0, 5 * 2, 5 * 2, Color.WHITE);

        this.spriteType = DrawManager.SpriteType.AddSign;
    }


}
