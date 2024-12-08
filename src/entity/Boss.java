package entity;

import Enemy.PiercingBullet;
import Enemy.PiercingBulletPool;
import Sound_Operator.SoundManager;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.GameSettings;
import screen.GameScreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.Timer;

import static screen.GameScreen.checkCollision;

public abstract class Boss extends Entity {
    private final int SEPARATION_LINE_HEIGHT_DOWN = 635;
    protected int maxHp;
    protected int currentHp;
    protected boolean enraged;
    protected boolean dragonBreatheRunning;
    protected GameSettings gameSettings;
    protected SoundManager soundManager;
    private Set<Fire> fireSet;
    private Cooldown fireCooldown;
    Fire fire;
    protected int bossX;
    protected int bossY;
    protected int width;
    protected int height;
    protected int fireCount;
    protected int fireCount_hit;
    protected int fireCount_hit_prev;
    protected double angle1;
    protected double angle2;
    protected int level;
    private boolean raining;
    protected int attackSpeed;

    SoundManager sm; // = SoundManager.getInstance();

    protected Boss(int bossX, int bossY, int width, int height, int level, int maxHp, GameSettings gameSettings) {
        super(bossX, bossY, width, height, Color.RED);
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.enraged = false;
        this.dragonBreatheRunning = false;
        this.gameSettings = gameSettings;
        this.bossX = bossX;
        this.bossY = bossY;
        this.width = width;
        this.height = height;
        this.level = level;
        this.raining = false;
        this.fireCount = 0;
        this.fireCount_hit = 0;
        this.fireCount_hit_prev = -1;
        this.attackSpeed = gameSettings.getShootingFrequency();

        this.fireSet = new HashSet<>();
        this.fireCooldown = Core.getCooldown(100); // Minimum 0.5s
//        this.soundManager = sm;
    }

    public void lifeSteal() {
        int healAmount;
        if (level == 4) {
            healAmount = maxHp / 10;
        } else {
            healAmount = maxHp / 5;
        }

        currentHp = Math.min(currentHp + healAmount, maxHp);
    }

    public void enragedMode() {
        GameSettings.setShootingFrequency(attackSpeed / 2);
        enraged = true;
    }

    public void calmDown() {
        GameSettings.setShootingFrequency(attackSpeed);
        enraged = false;
    }

    public boolean isEnraged() {
        return currentHp <= maxHp * 0.3;
    }

    public boolean enrage() {
        return enraged;
    }

    public void rain_of_fire() {
        if (!GameScreen.isLevelFinished() && !raining) {
            raining = true;

            ActionListener actionListener = e -> {
                if (raining) {
                    attack();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            };

            Timer timer = new Timer(10000, actionListener);
            timer.setRepeats(true);
            timer.start();
        }
    }

    private void attack() {
        Random random = new Random();

        int widthSegment = 600 / 6;
        int excludeXStart = random.nextInt(6) * widthSegment;

        for (int x = 0; x < 600; x += 20) {
            if (x < excludeXStart || x >= excludeXStart + widthSegment) {
                fire = new Fire(x, 50);
                this.fireSet.add(fire);
                fireCount++;
            }
        }

        if (this.fireCooldown.checkFinished()) {
            this.fireCooldown.reset();
        }
    }

    public void drawFire() {
        for (Fire fire : this.fireSet) {
            DrawManager.drawEntity(fire, fire.getPositionX(), fire.getPositionY());
        }
    }

    public void update() {
        Set<Fire> removefireSet = new HashSet<>();

        for (Fire fire : this.fireSet) {
            fire.update();
            if (fire.getPositionY() >= SEPARATION_LINE_HEIGHT_DOWN) {
                removefireSet.add(fire);
            }
        }
        this.fireSet.removeAll(removefireSet);
    }

    public boolean collision(Ship ship) {
        for (Fire fire : this.fireSet) {
            if (checkCollision(ship, fire)) {
                fireCount_hit = fireCount;
                break;
            }
        }
        boolean answer = (fireCount_hit != fireCount_hit_prev && fireCount_hit != 0);
        fireCount_hit_prev = fireCount_hit;

        return answer;
    }

    public void takeDamage(int damage) {
        currentHp -= damage;
    }

    public boolean isRaining() {
        return raining;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isBossDead() {
        return currentHp <= 0;
    }

    public void damageSound() {
//        sm.playES("boss_damage");
    }

    /**
     * 보스 사망 사운드
     */
    public final void destroy() {
        sm = SoundManager.getInstance();
        // 보스 사망 시 사운드는 일반 적 사망 사운드와 차별화가 필요할 거 같아 넣었습니다.
//		if (this.spriteType == DrawManager.SpriteType.middleBoss) {
//			sm.playES("middleBoss_die");
//		} else {
//			sm.playES("finalBoss_die");
//		}
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }
}