package common.item.tile;


import java.awt.*;

/**
 * Created on 2017/04/30.
 */
abstract public class Tile {
    private int positionX;
    private int positionY;

    public Tile(int orderX, int orderY) {
        this.positionX = orderX * TILE_RES_SIZE;
        this.positionY = orderY * TILE_RES_SIZE;
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

    public abstract int getPaintLayer();

    public abstract Image getImage();

    public static final int PLAIN_TILE = 0;
    public static final int BRICK_WALL = 1;
    public static final int METAL_WALL = 2;
    public static final int METAL_TILE = 3;
    public static final int PLANT = 4;
    public static final int WATER = 5;
    public static final int HEAD_QUARTER_LU = 6;
    public static final int HEAD_QUARTER_RU = 7;
    public static final int HEAD_QUARTER_LD = 8;
    public static final int HEAD_QUARTER_RD = 9;

    static final int TILE_RES_SIZE = 16;
}
