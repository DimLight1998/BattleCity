package client.gui;

import client.logic.Client;
import common.item.bullet.Bullet;
import common.item.tank.Tank;
import common.item.tile.Tile;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.net.URISyntaxException;
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
        Tank              hero_2,
        AtomicInteger     player_1_score,
        AtomicInteger     player_2_score) {
        panel_game =
            new Panel_Game(tiles, tanks, bullets, hero_1, hero_2, player_1_score, player_2_score);

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


    public void playFireSound() {
        (new SoundPlayer("fire.wav")).start();
    }

    class SoundPlayer extends Thread {
        String fileName = "";


        SoundPlayer(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void run() {
            super.run();

            new JFXPanel();
            Media media = null;
            try {
                media =
                    new Media(getClass().getResource("/res/sound/" + fileName).toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            assert      media != null;
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.setAutoPlay(true);

            mediaPlayer.play();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
