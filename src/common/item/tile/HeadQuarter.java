package common.item.tile;

import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public abstract class HeadQuarter extends Tile {
    public HeadQuarter(int orderX, int orderY) {
        super(orderX, orderY);
    }

    @Override
    public boolean isTankThrough() {
        return true;
    }

    @Override
    public boolean isBulletThrough() {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean isSuperDamageable() {
        return true;
    }

    @Override
    public boolean isSlippery() {
        return false;
    }

    @Override
    public boolean isDecisive() {
        return true;
    }

    @Override
    public int getPaintLayer() {
        return 0;
    }
}
