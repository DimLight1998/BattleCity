package client.gui;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import common.item.bullet.Bullet;
import common.item.tank.Tank;
import common.item.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2017/04/30.
 */
public class Panel_Game extends JPanel {
    Tile[][] tiles;
    ArrayList<Tank>   tanks;
    ArrayList<Bullet> bullets;
    Tank              hero_1;
    Tank              hero_2;
    AtomicInteger player_1_score;
    AtomicInteger player_2_score;

    Panel_Game(Tile[][] tiles,
               ArrayList<Tank> tanks,
               ArrayList<Bullet> bullets,
               Tank hero_1,
               Tank hero_2, AtomicInteger player_1_score, AtomicInteger player_2_score) {
        this.tiles   = tiles;
        this.tanks   = tanks;
        this.bullets = bullets;
        this.hero_1  = hero_1;
        this.hero_2  = hero_2;
        this.player_1_score = player_1_score;
        this.player_2_score = player_2_score;
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);

        // paint layer -1
        graphics.drawImage(new ImageIcon(getClass().getResource("/res/pic/plain_tile.png")).getImage(),0,0,480,480,this);


        // paint layer 0 (tile except plant)
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if(tiles[i][j].getPaintLayer()==0){
                    graphics.drawImage(tiles[i][j].getImage(),tiles[i][j].getPositionX(),tiles[i][j].getPositionY(),this);
                }
            }
        }

        // paint layer 1 (tank)
        synchronized (tanks) {
            Iterator<Tank> tankIterator = tanks.iterator();
            while (tankIterator.hasNext()) {
                Tank next = tankIterator.next();
                graphics.drawImage(next.getImage(), next.getLocationX(), next.getLocationY(), this);
            }
        }

        // paint layer 1 (player)
        if(hero_1.isActivated()) {
            graphics.drawImage(hero_1.getImage(), hero_1.getLocationX(), hero_1.getLocationY(), this);
        }

        if(hero_2.isActivated()) {
            graphics.drawImage(hero_2.getImage(), hero_2.getLocationX(), hero_2.getLocationY(), this);
        }

        // paint layer 2 (bullet)
        synchronized (bullets) {
            for (Bullet bullet : bullets) {
                graphics.drawImage(bullet.getImage(), bullet.getLocationX() - 2, bullet.getLocationY() - 2, this);
            }
        }

        // paint layer 3 (plant)
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if(tiles[i][j].getPaintLayer()==3){
                    graphics.drawImage(tiles[i][j].getImage(),tiles[i][j].getPositionX(),tiles[i][j].getPositionY(),this);
                }
            }
        }

        // paint layer 4 (tool)

        // paint layer info
        ((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(new Font("TimesRoman",Font.BOLD,20));
        graphics.drawString("Battle City",530,50);
        graphics.drawString(""+player_1_score,530,200);
        graphics.drawString(""+player_2_score,530,350);

        graphics.setFont(new Font("TimesRoman",Font.PLAIN,20));
        graphics.drawString("Player 1 Score",530,150);
        graphics.drawString("Player 2 Score",530,300);
    }
}
