package common.item.tank;

/**
 * Created on 2017/05/03.
 */
public class PlayerTank extends Tank {
    int owner;

    public PlayerTank(int locationX, int locationY,int owner) {
        super(locationX, locationY);
        health = 1;
        this.owner = owner;
        facingStatus = kDirectionUp;
    }

    @Override
    public int getTypeID() {
        return 0;
    }

    void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    String getImagePrefix() {
        if(owner == 1) {
            return "P1";
        } else {
            return "P2";
        }
    }

    @Override
    public void deactivate() {
        this.activated = false;
        this.setLocationX(-100);
        this.setLocationY(-100);
        this.setVelocityStatus(kNotMoving);
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
        return 0;
    }
}
