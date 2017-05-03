package common.item.tank;

/**
 * Created on 2017/05/03.
 */
public class PlayerTank extends Tank {
    public PlayerTank(int locationX, int locationY) {
        super(locationX, locationY);
        health = 1;
    }

    @Override
    int getMoveVelocity() {
        return 1;
    }

    @Override
    int getShootingVelocity() {
        return 1;
    }

    @Override
    int getScore() {
        return 0;
    }
}
