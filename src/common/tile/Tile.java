package common.tile;

/**
 * Created on 2017/04/30.
 */
abstract public class Tile {
    private int positionX;
    private int positionY;

    public Tile(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    abstract boolean isTankThrough();

    abstract boolean isBulletThrough();

    abstract boolean isDamageable();

    abstract boolean isSuperDamageable();

    abstract boolean isSlippery();

    abstract boolean isDecisive();

    abstract int getPaintLayer();
}
