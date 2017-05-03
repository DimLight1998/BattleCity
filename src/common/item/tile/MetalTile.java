package common.item.tile;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public class MetalTile extends Tile {
    public MetalTile(int orderX, int orderY) {
        super(orderX, orderY);
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
    public int getPaintLayer() {
        return 0;
    }

    @Override
    public Image getImage() {
        return new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\metal_tile.png").getImage();
    }
}
