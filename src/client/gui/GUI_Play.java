package client.gui;

import client.logic.Client;
import common.item.bullet.Bullet;
import common.item.tank.Tank;
import common.item.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2017/04/30.
 */
public class GUI_Play extends JPanel {
    private Panel_Game panel_game;
    private JFrame     mainFrame;


    public GUI_Play(Client client,
                    Tile[][] tiles,
                    ArrayList<Tank>   tanks,
                    ArrayList<Bullet> bullets,
                    Tank              hero_1,
                    Tank              hero_2, AtomicInteger player_1_score, AtomicInteger player_2_score) {
        panel_game = new Panel_Game(tiles, tanks, bullets, hero_1, hero_2,player_1_score,player_2_score);

        this.setLayout(new GridLayout(1, 1));
        this.add(panel_game);


        mainFrame = new JFrame("Battle City");

        mainFrame.setContentPane(this);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setSize(750, 509 /*509 is best size*/);
        mainFrame.setLocationRelativeTo(null);

        mainFrame.addKeyListener(client);
    }

    public void display() {
        mainFrame.setVisible(true);
        //        panel_game.repaint();
    }

    public void dispose() {
        mainFrame.dispose();
    }

    public void repaintPanelGame() {
        panel_game.repaint();
    }
}
