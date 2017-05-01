package common.item.tile;

/**
 * Created on 2017/04/30.
 */
public class Water extends Tile {
    public Water(int positionX, int positionY) {
        super(positionX, positionY);
    }

    @Override
    boolean isTankThrough() {
        return false;
    }

    @Override
    boolean isBulletThrough() {
        return true;
    }

    @Override
    boolean isDamageable() {
        return false;
    }

    @Override
    boolean isSuperDamageable() {
        return false;
    }

    @Override
    boolean isSlippery() {
        return false;
    }

    @Override
    boolean isDecisive() {
        return false;
    }

    @Override
    int getPaintLayer() {
        return 0;
    }
}
