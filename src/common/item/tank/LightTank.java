package common.item.tank;

/**
 * Created on 2017/04/30.
 */
public class LightTank extends Tank {
    LightTank(int locationX, int locationY) {
        super(locationX, locationY);
        health = 1;
    }

    @Override
    String getImagePrefix() {
        return "LT";
    }

    @Override
    int getMoveVelocity() {
        return 2;
    }

    @Override
    int getShootingVelocity() {
        return 2;
    }

    @Override
    int getScore() {
        return 100;
    }
}
