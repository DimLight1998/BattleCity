package common.item.tank;

/**
 * Created on 2017/04/30.
 */
public class HeavyTank extends Tank {
    HeavyTank(int locationX, int locationY) {
        super(locationX, locationY);
        health = 4;
    }

    @Override
    String getImagePrefix() {
        return "HT";
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
        return 400;
    }
}
