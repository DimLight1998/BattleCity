package common.item.tank;

/**
 * Created on 2017/04/30.
 */
public class TankDestroyer extends Tank {
    TankDestroyer(int locationX, int locationY) {
        super(locationX, locationY);
        health = 3;
    }

    @Override
    String getImagePrefix() {
        return "TD";
    }

    @Override
    public int getMoveVelocity() {
        return 2;
    }

    @Override
    public int getShootingVelocity() {
        return 3;
    }

    @Override
    public int getScore() {
        return 300;
    }
}
