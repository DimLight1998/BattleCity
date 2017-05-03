package common.item.tank;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public abstract class Tank {
    int health;
    int locationX;
    int locationY;
    int velocityStatus;
    int facingStatus;

    boolean isSuper;
    boolean isMovable;

    abstract int getMoveVelocity();

    abstract int getShootingVelocity();

    abstract int getScore();


    Tank(int locationX, int locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.velocityStatus = kNotMoving;
        this.facingStatus = kDirectionLeft;
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

    public int getFacingStatus() {
        return facingStatus;
    }

    public void setFacingStatus(int facingStatus) {
        this.facingStatus = facingStatus;
    }

    public void updateLocation() {
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

    public Image getImage() {
        // TODO remove magic
        String imagePath = "D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\";

        switch (facingStatus) {
            case kDirectionLeft:
                return new ImageIcon(imagePath+getImagePrefix()+"_L.png").getImage();
            case kDirectionRight:
                return new ImageIcon(imagePath+getImagePrefix()+"_R.png").getImage();
            case kDirectionUp:
                return new ImageIcon(imagePath+getImagePrefix()+"_U.png").getImage();
            case kDirectionDown:
                return new ImageIcon(imagePath+getImagePrefix()+"_D.png").getImage();
            default:
                return new ImageIcon().getImage();
        }
    }

    abstract String getImagePrefix();


    public static final int kNotMoving = 0;
    public static final int kMovingLeft = 1;
    public static final int kMovingRight = 2;
    public static final int kMovingUp = 3;
    public static final int kMovingDown = 4;

    public static final int kDirectionLeft = 1;
    public static final int kDirectionRight = 2;
    public static final int kDirectionUp = 3;
    public static final int kDirectionDown = 4;
}
