package common.item.bullet;

import common.item.tank.*;

import javax.swing.*;
import java.awt.*;

import static common.item.tank.Tank.*;

/**
 * Created on 2017/04/30.
 */
public class Bullet {
    int locationX;
    int locationY;
    int velocityStatus;
    boolean isSuper;


    public Bullet(Tank tank) {
        switch (tank.getFacingStatus()) {
            case kDirectionLeft:
                locationX = tank.getLocationX();
                locationY = tank.getLocationY()+16;
                break;
            case kDirectionRight:
                locationX = tank.getLocationX()+32;
                locationY = tank.getLocationY()+16;
                break;
            case kDirectionUp:
                locationX = tank.getLocationX()+16;
                locationY = tank.getLocationY();
                break;
            case kDirectionDown:
                locationX = tank.getLocationX()+16;
                locationY = tank.getLocationY()+32;
                break;
        }

        velocityStatus = tank.getFacingStatus();
        isSuper = false;
    }


    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getVelocityStatus() {
        return velocityStatus;
    }

    public void setVelocityStatus(int velocityStatus) {
        this.velocityStatus = velocityStatus;
    }

    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }

    public void updateLocation() {
        switch (velocityStatus) {
            // TODO check whether it is valid and reset MoveVelocity
            case kNotMoving:
                break;
            case kMovingLeft:
                locationX -= kBulletVelocity;
                break;
            case kMovingRight:
                locationX += kBulletVelocity;
                break;
            case kMovingUp:
                locationY -= kBulletVelocity;
                break;
            case kMovingDown:
                locationY += kBulletVelocity;
                break;
            default:
                break;
        }

    }


    public Image getImage() {
        // todo remove magic
        return new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\bullet.png").getImage();
    }

    private static final int kNotMoving = 0;
    private static final int kMovingLeft = 1;
    private static final int kMovingRight = 2;
    private static final int kMovingUp = 3;
    private static final int kMovingDown = 4;

    private static final int kBulletVelocity = 3;
}
