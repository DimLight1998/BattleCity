package client.gui;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import common.item.bullet.Bullet;
import common.item.tank.Tank;
import common.item.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created on 2017/04/30.
 */
public class Panel_Game extends JPanel {
    Tile[][] tiles;
    ArrayList<Tank>   tanks;
    ArrayList<Bullet> bullets;
    Tank              hero_1;
    Tank              hero_2;

    Panel_Game(Tile[][] tiles,
        ArrayList<Tank>   tanks,
        ArrayList<Bullet> bullets,
        Tank              hero_1,
        Tank              hero_2) {
        this.tiles   = tiles;
        this.tanks   = tanks;
        this.bullets = bullets;
        this.hero_1  = hero_1;
        this.hero_2  = hero_2;
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);

        // paint layer -1
        graphics.drawImage(new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\plain_tile.png").getImage(),0,0,480,480,this);

        // paint layer 0 (tile except plant)
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if(tiles[i][j].getPaintLayer()==0){
                    graphics.drawImage(tiles[i][j].getImage(),tiles[i][j].getPositionX(),tiles[i][j].getPositionY(),this);
                }
            }
        }

        // paint layer 1 (tank)
        Iterator<Tank> tankIterator = tanks.iterator();
        while(tankIterator.hasNext()) {
            Tank next = tankIterator.next();
            graphics.drawImage(next.getImage(),next.getLocationX(),next.getLocationY(),this);
        }

        // paint layer 1 (player)
        graphics.drawImage(hero_1.getImage(),hero_1.getLocationX(),hero_1.getLocationY(),this);
        graphics.drawImage(hero_2.getImage(),hero_2.getLocationX(),hero_2.getLocationY(),this);

        // paint layer 2 (bullet)
//        Iterator<Bullet> bulletIterator = bullets.iterator();
//        while(bulletIterator.hasNext()) {
//            Bullet next = bulletIterator.next();
//            graphics.drawImage(next.getImage(),next.getLocationX(),next.getLocationY(),this);
//        }

        // paint layer 3 (plant)
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if(tiles[i][j].getPaintLayer()==3){
                    graphics.drawImage(tiles[i][j].getImage(),tiles[i][j].getPositionX(),tiles[i][j].getPositionY(),this);
                }
            }
        }

        // paint layer 4 (tool)
    }
}
