package entity;

import engine.DrawManager;
import engine.GameSettings;

public class MiddleBoss extends Boss {

    public MiddleBoss(int bossX, int bossY, int width, int height, int level, GameSettings gameSettings) {
        super(bossX, bossY, width, height, level, 30, gameSettings);
        this.spriteType = DrawManager.SpriteType.middleBoss;
    }

    public MiddleBoss(int level, GameSettings gameSettings) {
        this(240, 100, 64 * 2, 100, level, gameSettings);
    }
}