package common.item.tank;

import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public class ArmoredTank extends Tank {
    ArmoredTank(int locationX, int locationY) {
        super(locationX, locationY);
        health = 2;
    }

    @Override
    String getImagePrefix() {
        return "AT";
    }

    @Override
    public int getMoveVelocity() {
        return 3;
    }

    @Override
    public int getShootingVelocity() {
        return 2;
    }

    @Override
    public int getScore() {
        return 200;
    }
}
