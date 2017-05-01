package common.item.tank;

/**
 * Created on 2017/04/30.
 */
public abstract class Tank {
    int health;
    int locationX;
    int locationY;
    int velocityStatus;

    boolean isSuper;
    boolean isMovable;

    abstract int getMoveVelocity();

    abstract int getShootingVelocity();

    abstract int getScore();


    Tank(int locationX, int locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.velocityStatus = kNotMoving;
        this.isSuper = false;
        this.isMovable = true;
    }


    public int getLocationX() {
        return locationX;
    }


    public int getLocationY() {
        return locationY;
    }


    public int getVelocityStatus() {
        return velocityStatus;
    }


    public void setHealth(int health) {
        this.health = health;
    }


    public void setVelocityStatus(int velocityStatus) {
        this.velocityStatus = velocityStatus;
    }


    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }


    public void setMovable(boolean movable) {
        isMovable = movable;
    }


    void updateLocation() {
        if (isMovable) {
            switch (velocityStatus) {
                // TODO check whether it is valid and reset MoveVelocity
                case kNotMoving:
                    break;
                case kMovingLeft:
                    locationX -= getMoveVelocity();
                    break;
                case kMovingRight:
                    locationX += getMoveVelocity();
                    break;
                case kMovingUp:
                    locationY -= getMoveVelocity();
                    break;
                case kMovingDown:
                    locationY += getMoveVelocity();
                    break;
                default:
                    break;
            }
        }
    }


    void increaseHealth() {
        if (health > 0) {
            health++;
        }
    }


    void decreaseHealth() {
        if (health > 0) {
            health--;
            if (health == 0) {
                // TODO remove this from map and respawn
            }
        }
    }


    void shoot() {
        // TODO complete this after bullet
    }


    static final int kNotMoving = 0;
    static final int kMovingLeft = 1;
    static final int kMovingRight = 2;
    static final int kMovingUp = 3;
    static final int kMovingDown = 4;
}
