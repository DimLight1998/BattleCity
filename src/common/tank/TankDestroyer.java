package common.tank;

/**
 * Created on 2017/04/30.
 */
public class TankDestroyer extends Tank {
    TankDestroyer(int locationX, int locationY) {
        super(locationX, locationY);
        health = 3;
    }

    @Override
    int getMoveVelocity() {
        return 2;
    }

    @Override
    int getShootingVelocity() {
        return 3;
    }

    @Override
    int getScore() {
        return 300;
    }
}
