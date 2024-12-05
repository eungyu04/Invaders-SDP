package entity;

import engine.DrawManager;
import engine.GameSettings;

public class FinalBoss extends Boss {

    public FinalBoss(int bossX, int bossY, int width, int height, int level, GameSettings gameSettings) {
        super(bossX, bossY, width, height, level, 120, gameSettings);
        this.spriteType = DrawManager.SpriteType.finalBoss;
    }

    public FinalBoss(int level, GameSettings gameSettings) {
        this(225, 100, 150, 120, level, gameSettings);
    }
}