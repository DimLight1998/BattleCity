package common.item.tile;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public class MetalWall extends Tile {
    public MetalWall(int orderX, int orderY) {
        super(orderX, orderY);
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
    public int getPaintLayer() {
        return 0;
    }

    @Override
    public Image getImage() {
        return new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\metal_wall.png").getImage();
    }
}
