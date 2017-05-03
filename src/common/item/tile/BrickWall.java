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
    boolean isTankThrough() {
        return false;
    }

    @Override
    boolean isBulletThrough() {
        return false;
    }

    @Override
    boolean isDamageable() {
        return true;
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
        return new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\brick_wall.png").getImage();
    }
}
