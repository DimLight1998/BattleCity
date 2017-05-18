package common.item.tile;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/05/03.
 */
public class HeadQuarterLD extends HeadQuarter {
    public HeadQuarterLD(int orderX, int orderY) {
        super(orderX, orderY);
    }

    @Override
    public Image getImage() {
        return new ImageIcon(getClass().getResource("/res/pic/HQ_LD.png")).getImage();
    }
}
