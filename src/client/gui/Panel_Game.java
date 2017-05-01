package client.gui;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public class Panel_Game extends JPanel {
    Image image_brickWall = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\brick_wall.png").getImage();
    Image image_metalTile = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\metal_tile.png").getImage();
    Image image_metalWall = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\metal_wall.png").getImage();
    Image image_phoenix = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\phoenix.png").getImage();
    Image image_plant = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\plant.png").getImage();
    Image image_water = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\water.png").getImage();


    public void paint(Graphics graphics) {
        super.paint(graphics);

        for (int i = 0; i < 30; i++) {
            graphics.drawImage(i%2==0?image_brickWall:image_metalWall,16*i,0,this);
            graphics.drawImage(i%2==0?image_metalWall:image_water,0,16*i,this);
        }
    }
}
