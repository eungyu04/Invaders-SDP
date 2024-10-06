package inventory_develop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ShipStatus {

    private int Speed_increase;
    private int SHOOTING_INTERVAL_increase;
    private int BULLET_SPEED_increase;


    Properties properties = new Properties();


    public ShipStatus() {}

    public void loadStatus(){
        try (InputStream inputStream = ShipStatus.class.getClassLoader().getResourceAsStream("StatusConfig.properties")) {
            if (inputStream == null) {
                System.out.println("FileNotFound");
                return;
            }

            properties.load(inputStream);

            Speed_increase = Integer.parseInt(properties.getProperty("Speed.increase"));
            SHOOTING_INTERVAL_increase = Integer.parseInt(properties.getProperty("SHOOTING_INTERVAL.increase"));
            BULLET_SPEED_increase = Integer.parseInt(properties.getProperty("BULLET_SPEED.increase"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final int getSpeedIn(){
        return Speed_increase;
    }
    public final int getSuootingInIn(){
        return SHOOTING_INTERVAL_increase;
    }
    public final int getBulletSpeedIn(){
        return BULLET_SPEED_increase;
    }
}
