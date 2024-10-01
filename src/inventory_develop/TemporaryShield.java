package inventory_develop;


public class TemporaryShield {

    private static final int SHIELD_DURATION = 3000; // 방패 지속 시간 3초
    private boolean active;
    private long activationTime;

    public TemporaryShield() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
        this.activationTime = System.currentTimeMillis();
    }


    public void update() {
        if (this.active) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.activationTime >= SHIELD_DURATION) {
                deactivate();
            }
        }
    }


    public boolean isActive() {
        return active;
    }
    public void deactivate() {
        this.active = false;
    }

}
