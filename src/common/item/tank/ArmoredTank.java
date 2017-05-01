package common.item.tank;

/**
 * Created on 2017/04/30.
 */
public class ArmoredTank extends Tank {
    ArmoredTank(int locationX, int locationY) {
        super(locationX, locationY);
        health = 2;
    }

    @Override
    int getMoveVelocity() {
        return 3;
    }

    @Override
    int getShootingVelocity() {
        return 2;
    }

    @Override
    int getScore() {
        return 200;
    }
}
