package client.gui;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import common.item.bullet.Bullet;
import common.item.tank.Tank;
import common.item.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created on 2017/04/30.
 */
public class Panel_Game extends JPanel {
    // clang-format off
    Image image_armoredTankLeft    = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\AM_L.png").getImage();
    Image image_armoredTankRight   = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\AM_R.png").getImage();
    Image image_armoredTankUp      = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\AM_U.png").getImage();
    Image image_armoredTankDown    = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\AM_D.png").getImage();
    Image image_lightTankLeft      = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\LT_L.png").getImage();
    Image image_lightTankRight     = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\LT_R.png").getImage();
    Image image_lightTankUp        = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\LT_U.png").getImage();
    Image image_lightTankDown      = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\LT_D.png").getImage();
    Image image_tankDestroyerLeft  = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\TD_L.png").getImage();
    Image image_tankDestroyerRight = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\TD_R.png").getImage();
    Image image_tankDestroyerUp    = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\TD_U.png").getImage();
    Image image_tankDestroyerDown  = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\TD_D.png").getImage();
    Image image_heavyTankLeft      = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\HT_L.png").getImage();
    Image image_heavyTankRight     = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\HT_R.png").getImage();
    Image image_heavyTankUp        = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\HT_U.png").getImage();
    Image image_heavyTankDown      = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\HT_D.png").getImage();
    Image image_player_1_TankLeft  = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\P1_L.png").getImage();
    Image image_player_1_TankRight = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\P1_R.png").getImage();
    Image image_player_1_TankUp    = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\P1_U.png").getImage();
    Image image_player_1_TankDown  = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\P1_D.png").getImage();
    Image image_player_2_TankLeft  = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\P2_L.png").getImage();
    Image image_player_2_TankRight = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\P2_R.png").getImage();
    Image image_player_2_TankUp    = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\P2_U.png").getImage();
    Image image_player_2_TankDown  = new ImageIcon("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\pic\\P2_D.png").getImage();
    // clang-format on

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

        // paint layer 2 (bullet)

        // paint layer 3 (plant)
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if(tiles[i][j].getPaintLayer()==3){
                    graphics.drawImage(tiles[i][j].getImage(),tiles[i][j].getPositionX(),tiles[i][j].getPositionY(),this);
                }
            }
        }

        // paint layer 4 (tool)
        System.out.println("Painted");
    }
}
