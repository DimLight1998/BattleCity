package common.item.tile;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public class Plant extends Tile {
    public Plant(int orderX, int orderY) {
        super(orderX, orderY);
    }

    @Override
    public boolean isTankThrough() {
        return true;
    }

    @Override
    public boolean isBulletThrough() {
        return true;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isSuperDamageable() {
        return false;
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
        return 3;
    }

    @Override
    public Image getImage() {
        return new ImageIcon(getClass().getResource("/res/pic/plant.png")).getImage();
    }
}
