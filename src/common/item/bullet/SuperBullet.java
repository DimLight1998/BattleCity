package common.item.bullet;

import common.item.tank.Tank;

/**
 * Created on 2017/04/30.
 */
public class SuperBullet extends Bullet {
    int belong;

    public SuperBullet(Tank tank,int belong) {
        super(tank);
        isSuper = true;
        this.belong = belong;
    }


    @Override
    public int getBelong() {
        return belong;
    }
}
