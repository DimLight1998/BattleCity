package common.item.tile;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/05/03.
 */
public class HeadQuarterRU extends HeadQuarter {
    public HeadQuarterRU(int orderX, int orderY) {
        super(orderX, orderY);
    }

    @Override
    public Image getImage() {
        return new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\HQ_RU.png").getImage();
    }
}
