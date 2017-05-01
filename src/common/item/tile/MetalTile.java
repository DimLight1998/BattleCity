package common.item.tile;

/**
 * Created on 2017/04/30.
 */
public class MetalTile extends Tile {
    public MetalTile(int positionX, int positionY) {
        super(positionX, positionY);
    }

    @Override
    boolean isTankThrough() {
        return true;
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
        return true;
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
