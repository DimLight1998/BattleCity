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
    public int getMoveVelocity() {
        return 2;
    }

    @Override
    public int getShootingVelocity() {
        return 2;
    }

    @Override
    public int getScore() {
        return 100;
    }
}
