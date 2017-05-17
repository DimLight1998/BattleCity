package common.item.bullet;

import common.item.tank.Tank;

/**
 * Created on 2017/04/30.
 */
public class SuperBullet extends Bullet {
    public SuperBullet(Tank tank) {
        super(tank);
        isSuper = true;
    }
}
