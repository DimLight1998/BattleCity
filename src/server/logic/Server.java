package server.logic;

import common.item.bullet.Bullet;
import common.item.tank.Tank;
import common.item.tile.*;
import common.logic.Emitter;
import common.logic.MapLoader;
import server.gui.Panel_Setup;
import server.gui.Panel_Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

import static common.item.tile.Tile.*;

/**
 * Created on 2017/05/01.
 */
public class Server implements ActionListener{
    private Tile[][] tiles;
    private ArrayList<Tank> tanks;
    private ArrayList<Bullet> bullets;
    Tank hero_1;
    Tank hero_2;

    private int serverPortNumber;
    private Emitter emitter_1;
    private Emitter emitter_2;
    private InetAddress address_1;
    private InetAddress address_2;
    private int portNumber_1;
    private int portNumber_2;
    private Receiver receiver;
    private Panel_Setup panel_setup;
    Panel_Status panel_status;
    private File mapFile;

    boolean isPortSet = false;
    boolean isPlayerReady_1 = false;
    boolean isPlayerReady_2 = false;
    boolean isGameOver = false;

    public Server() {
        tiles = new Tile[30][30];
        tanks = new ArrayList<>();
        bullets = new ArrayList<>();

        panel_setup = new Panel_Setup(this);
//        panel_status = new Panel_Status();
    }

    void handleInfo(String info, InetAddress address) {
        // TODO

        // player registers
        if(info.startsWith("reg")) {
            if(!isPlayerReady_1) {
                isPlayerReady_1 = true;
                address_1 = address;
                portNumber_1 = Integer.parseInt(info.substring(3));
            } else if(!isPlayerReady_2) {
                isPlayerReady_2 = true;
                address_2 =address;
                portNumber_2 = Integer.parseInt(info.substring(3));
            }
        }

        // player selects map
        if(info.startsWith("map")) {
            mapFile = new File("src/res/map/"+info.substring(3)+".txt");
        }

        // player asks if others ready
        if(info.startsWith("ask") && info.endsWith("1")) {
            if(isPlayerReady_2) {
                emitter_1.emit("true");
            } else {
                emitter_1.emit("false");
            }
        }

        if (info.startsWith("ask") && info.endsWith("2")) {
            if(isPlayerReady_1) {
                emitter_2.emit("true");
            } else {
                emitter_2.emit("false");
            }
        }

        // player presses key
        // e.g. prsu1
        if(info.startsWith("prs")) {
            if(info.endsWith("1")) {
                switch (info.charAt(3)) {
                    // TODO
                }
            } else if(info.endsWith("2")) {
                switch (info.charAt(3)) {
                    // TODO
                }
            }
        }

        // player release key
        // e.g. rls1
        if(info.startsWith("rls")) {
            if(info.endsWith("1")) {
                // TODO
            } else if(info.endsWith("2")) {
                // TODO
            }
        }

        // player fire
        if(info.startsWith("fir")) {
            if(info.endsWith("1")) {
                // TODO
            } else if(info.endsWith("2")) {
                // TODO
            }
        }
    }

    public void start() throws InterruptedException, IOException {
        // TODO
        panel_setup.display();

        while (!isPortSet) {
            Thread.sleep(100);
        }

        panel_setup.dispose();
        receiver = new Receiver(serverPortNumber,this);

        receiver.start();

        while(!isPlayerReady_2) {
            Thread.sleep(100);
        }

        emitter_1 = new Emitter(address_1,portNumber_1);
        emitter_2 = new Emitter(address_2,portNumber_2);

        emitter_1.emit("dis1");
        emitter_2.emit("dis2");
//        panel_status.show();

        // TODO remove this
        System.out.println("ready");

        // TODO complete map selection
        MapLoader.loadMap(mapFile,tiles);

        while(!isGameOver) {
            Thread.sleep(100);// TODO 这是必要的吗
            updateStatus();
            checkGameOver();
            emitInfo();
        }

        JOptionPane.showConfirmDialog(panel_status,"Game over");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start")) {
            serverPortNumber = panel_setup.getPortNumber();
            isPortSet = true;

            // TODO delete
            System.out.println("Running on port "+ serverPortNumber);
        }
    }



    void updateStatus() {

    }

    void checkGameOver() {

    }

    void emitInfo() {

    }

    static final int MAX_MAP_SIZE_X = 30;
    static final int MAX_MAP_SIZE_Y = 30;
}
