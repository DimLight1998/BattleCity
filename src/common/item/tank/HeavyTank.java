package common.item.tank;

/**
 * Created on 2017/04/30.
 */
public class HeavyTank extends Tank {
    public HeavyTank(int locationX, int locationY) {
        super(locationX, locationY);
        health = 4;
        fireDelayBase = kFireDelayLevel_1;
    }

    @Override
    public int getTypeID() {
        return kHeavyTankID;
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
        return 1;
    }

    @Override
    public int getScore() {
        return 400;
    }
}
