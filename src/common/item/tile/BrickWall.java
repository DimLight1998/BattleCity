package common.item.tile;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public class BrickWall extends Tile {
    public BrickWall(int orderX, int orderY) {
        super(orderX, orderY);
    }

    @Override
    public boolean isTankThrough() {
        return false;
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
        return false;
    }

    @Override
    public int getPaintLayer() {
        return 0;
    }

    @Override
    public Image getImage() {
        return new ImageIcon(this.getClass().getResource("/res/pic/brick_wall.png")).getImage();
    }
}
