package common.item.tile;

/**
 * Created on 2017/04/30.
 */
public class MetalWall extends Tile {
    public MetalWall(int positionX, int positionY) {
        super(positionX, positionY);
    }

    @Override
    boolean isTankThrough() {
        return false;
    }

    @Override
    boolean isBulletThrough() {
        return false;
    }

    @Override
    boolean isDamageable() {
        return false;
    }

    @Override
    boolean isSuperDamageable() {
        return true;
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
