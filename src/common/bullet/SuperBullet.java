package common.bullet;

import common.tank.Tank;

/**
 * Created on 2017/04/30.
 */
public class SuperBullet extends Bullet {
    SuperBullet(Tank tank) {
        super(tank);
        isSuper = true;
    }
}
