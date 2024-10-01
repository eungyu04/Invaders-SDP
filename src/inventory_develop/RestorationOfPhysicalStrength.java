package inventory_develop;

import screen.GameScreen;

public class RestorationOfPhysicalStrength {

    private boolean active;

    private static final int MAX_LIVES = 3;

    public RestorationOfPhysicalStrength() {
        this.active = false;
    }

    public void activate() {
        active = true;
    }


    public void deactivate() {
        active = false;
    }


    public boolean isActive() {
        return active;
    }

    // 생명 증가
    public void requestIncreaseLives(GameScreen gameScreen) {
        if (active && gameScreen.getLives() < MAX_LIVES) {
            gameScreen.setLives(gameScreen.getLives() + 1);
            deactivate();
        }
    }
}