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
    int getMoveVelocity() {
        return 2;
    }

    @Override
    int getShootingVelocity() {
        return 2;
    }

    @Override
    int getScore() {
        return 400;
    }
}
