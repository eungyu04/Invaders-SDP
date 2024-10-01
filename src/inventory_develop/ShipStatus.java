package inventory_develop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ShipStatus {

    protected int Speed;
    protected int SHOOTING_INTERVAL;
    protected int BULLET_SPEED;
    protected int Speed_increase;
    protected int SHOOTING_INTERVAL_increase;
    protected int BULLET_SPEED_increase;


    Properties properties = new Properties();


    public ShipStatus(final int Speed, final int SHOOTING_INTERVAL,
                      final int BULLET_SPEED) {
        this.Speed = Speed;
        this.SHOOTING_INTERVAL = SHOOTING_INTERVAL;
        this.BULLET_SPEED = BULLET_SPEED;
    }

    public void SetStatus(final int Speed_Level, final int SHOOTING_INTERVAL_Level,
                          final int BULLET_SPEED_Level) {
        this.Speed += Speed_increase * Speed_Level - Speed;
        this.SHOOTING_INTERVAL += SHOOTING_INTERVAL_increase * SHOOTING_INTERVAL_Level - SHOOTING_INTERVAL;
        this.BULLET_SPEED += BULLET_SPEED_increase * BULLET_SPEED_Level - BULLET_SPEED;
    }


    public void loadStatus(){
        try (InputStream inputStream = ShipStatus.class.getClassLoader().getResourceAsStream("Status_increase.properties")) {
            if (inputStream == null) {
                System.out.println("FileNotFound");
                return;
            }

            properties.load(inputStream);

            int statusValue = Integer.parseInt(properties.getProperty("Speed"));
            Speed = statusValue;
            statusValue = Integer.parseInt(properties.getProperty("Speed.increase"));
            Speed_increase = statusValue;

            statusValue = Integer.parseInt(properties.getProperty("SHOOTING_INTERVAL"));
            SHOOTING_INTERVAL = statusValue;
            statusValue = Integer.parseInt(properties.getProperty("SHOOTING_INTERVAL.increase"));
            SHOOTING_INTERVAL_increase= statusValue;

            statusValue = Integer.parseInt(properties.getProperty("BULLET_SPEED"));
            BULLET_SPEED = statusValue;
            statusValue = Integer.parseInt(properties.getProperty("BULLET_SPEED.increase"));
            BULLET_SPEED_increase = statusValue;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public final int getSpeed(){
        return Speed;
    }
    public final int getSHOOTING_INTERVAL(){
        return SHOOTING_INTERVAL;
    }
    public final int getBULLET_SPEED(){
        return BULLET_SPEED;
    }
}
