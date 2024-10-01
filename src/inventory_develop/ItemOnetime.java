package inventory_develop;

import java.awt.*;
import engine.DrawManager.SpriteType;
import entity.Entity;

public class ItemOnetime extends Entity {

    private int speed;

    /**
     * Public constructor.
     */
    public ItemOnetime(final int positionX, final int positionY, final int speed) {
        super(positionX, positionY, 5 * 5, 5 * 5, Color.magenta);

        this.speed = speed;
    }

    /**
     * 아이템 랜덤 & setSprite & setColor
     * Entity에 setColor추가
     */
    public final void randomItem() {
        double rdItem = Math.random();

        if (rdItem < 0.25) { // 25%
            this.spriteType = SpriteType.ItemAttackSpeed;
            this.setColor(Color.yellow);
        } else if (rdItem < 0.5) { // 25%
            this.spriteType = SpriteType.ItemBomb;
            this.setColor(Color.black);
        } else if (rdItem < 0.75) { // 25%
            this.spriteType = SpriteType.ItemBarrier;
            this.setColor(Color.blue);
        } else { // 25%
            this.spriteType = SpriteType.ItemRecovery;
            this.setColor(Color.green);
        }
    }

    /**
     * gamescreen에서 CleanItem메소드를 만들어서 사용
     * 아이템의 위치를 지정 (일단은 일자로 떨어지게끔만)
     */
    public final void update() {
        this.positionY += this.speed;
    }

    /**
     * 일단 Item Speed의 게터와 세터 쓸 일이 있을진 모르겠지만
     */
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
