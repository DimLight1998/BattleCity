package common.item.tank;

import common.item.bullet.Bullet;
import common.item.tile.Tile;
import javafx.util.Pair;
import server.logic.Server;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created on 2017/04/30.
 */
public abstract class Tank {
    int health;
    int locationX;
    int locationY;
    int velocityStatus;
    int facingStatus;
    int fireDelay;
    int fireDelayBase;
    boolean activated;// used by AI
    boolean isSuper;
    boolean isMovable;


    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }


    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }


    public void activate() {
        activated = true;
    }


    public void deactivate() {
        activated = false;
    }


    public boolean isActivated() {
        return activated;
    }


    public abstract int getMoveVelocity();


    public abstract int getShootingVelocity();


    public abstract int getScore();


    Tank(int locationX, int locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.velocityStatus = kNotMoving;
        this.facingStatus = kDirectionDown;
        this.isSuper = false;
        this.isMovable = true;
        this.activated = true;

        this.fireDelayBase = kFireDelayLevel_p;
    }


    public static Tank tankFactory(String constructionInfo) {
        String[] slices = constructionInfo.split("(\\.)|(\\{)|(})");
        // todo debug
        Tank tank = null;

        int locationX = Integer.parseInt(slices[2]);
        int locationY = Integer.parseInt(slices[3]);

        switch (Integer.valueOf(slices[8])) {
            case kHeavyTankID:
                tank = new HeavyTank(locationX,locationY);
                break;
            case kLightTankID:
                tank = new LightTank(locationX,locationY);
                break;
            case kArmoredTankID:
                tank = new ArmoredTank(locationX,locationY);
                break;
            case kTankDestroyerID:
                tank = new TankDestroyer(locationX,locationY);
                break;
        }

        assert tank != null;
        tank.health = Integer.parseInt(slices[1]);
        tank.velocityStatus = Integer.parseInt(slices[4]);
        tank.facingStatus = Integer.parseInt(slices[5]);
        tank.isSuper = (slices[6].equals("1"));
        tank.isMovable = (slices[7].equals("1"));

        return tank;
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


    public void increaseHealth() {
        if (health > 0) {
            health++;
        }
    }


    public void decreaseHealth() {
        if (health > 0) {
            health--;
            if (health == 0) {
                activated = false;
            }
        }
    }


    public void tryFire(ArrayList<Bullet> bullets) {
        if (isAbleToFire()) {
            bullets.add(new Bullet(this));
            resetFireDelay();
        }
    }


    public void updateFireDelay() {
        if(fireDelay>0) {
            fireDelay--;
        }
    }


    public void resetFireDelay() {
        fireDelay = fireDelayBase;
    }


    public boolean isAbleToFire() {
        // todo
        return (fireDelay == 0);
    }


    public Image getImage() {
        // TODO remove magic
        String imagePath = "D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\"+health+"_";

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
    
    
    abstract public int getTypeID();


    abstract String getImagePrefix();


    public String toString() {
        return String.format("{%d.%d.%d.%d.%d.%d.%d.%d}",health,locationX,locationY,velocityStatus,facingStatus,isSuper?1:0,isMovable?1:0,getTypeID());
    }


    public void loadFromString(String info) {
        String[] slices = info.split("(\\.)|(\\{)|(})");
        // todo debug

        health = Integer.parseInt(slices[1]);
        locationX = Integer.parseInt(slices[2]);
        locationY = Integer.parseInt(slices[3]);
        velocityStatus = Integer.parseInt(slices[4]);
        facingStatus = Integer.parseInt(slices[5]);
        isSuper = (slices[6].equals("1"));
        isMovable = (slices[7].equals("1"));
    }


    public static final int kNotMoving = 0;
    public static final int kMovingLeft = 1;
    public static final int kMovingRight = 2;
    public static final int kMovingUp = 3;
    public static final int kMovingDown = 4;

    public static final int kDirectionLeft = 1;
    public static final int kDirectionRight = 2;
    public static final int kDirectionUp = 3;
    public static final int kDirectionDown = 4;

    public static final int kFireDelayLevel_1 = 70;
    public static final int kFireDelayLevel_2 = 50;
    public static final int kFireDelayLevel_3 = 30;
    public static final int kFireDelayLevel_p = 40;

    public static final int kHeavyTankID = 1;
    public static final int kLightTankID = 2;
    public static final int kArmoredTankID = 3;
    public static final int kTankDestroyerID = 4;
}
