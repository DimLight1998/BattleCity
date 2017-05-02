package server.logic;

import common.item.bullet.Bullet;
import common.item.tank.Tank;
import common.item.tile.*;
import common.logic.Emitter;
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
    Tile[][] tiles;
    ArrayList<Tank> tanks;
    ArrayList<Bullet> bullets;

    int serverPortNumber;
    Emitter emitter_1;
    Emitter emitter_2;
    InetAddress address_1;
    InetAddress address_2;
    int portNumber_1;
    int portNumber_2;
    Receiver receiver;
    Panel_Setup panel_setup;
    Panel_Status panel_status;

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

    void handleInfo(String info, InetAddress address, int port) {
        // TODO

        // player registers
        if(info.equals("reg")) {
            if(!isPlayerReady_1) {
                isPlayerReady_1 = true;
                address_1 = address;
                portNumber_1 = port;
            } else if(!isPlayerReady_2) {
                isPlayerReady_2 = true;
                address_2 =address;
                portNumber_2 = port;
            }
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

        // player moves
        // e.g. movu1
        if(info.startsWith("mov")) {
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

        // TODO complete map selection
        loadMap(new File("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\map\\test.txt"));

        receiver.start();

        while(!isPlayerReady_2) {
            Thread.sleep(100);
        }

        emitter_1 = new Emitter(address_1,portNumber_1);
        emitter_2 = new Emitter(address_2,portNumber_2);
//        panel_status.show();

        while(!isGameOver) {
            // TODO complete
            // check game over
            // emit info
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

    void loadMap(File mapFile) throws FileNotFoundException {
        Scanner mapScanner = new Scanner(mapFile);

        int rowCounter = 0;
        int columnCounter = 0;

        while(mapScanner.hasNextInt()) {
            int read = mapScanner.nextInt();

            switch (read) {
                case PLAIN_TILE:
                    tiles[rowCounter][columnCounter] = new PlainTile(columnCounter,rowCounter);
                    break;
                case BRICK_WALL:
                    tiles[rowCounter][columnCounter] = new BrickWall(columnCounter,rowCounter);
                    break;
                case METAL_WALL:
                    tiles[rowCounter][columnCounter] = new MetalWall(columnCounter,rowCounter);
                    break;
                case METAL_TILE:
                    tiles[rowCounter][columnCounter] = new MetalTile(columnCounter,rowCounter);
                    break;
                case PLANT:
                    tiles[rowCounter][columnCounter] = new Plant(columnCounter,rowCounter);
                    break;
                case WATER:
                    tiles[rowCounter][columnCounter] = new Water(columnCounter,rowCounter);
                    break;
                case HEAD_QUARTER_LU:
                case HEAD_QUARTER_RU:
                case HEAD_QUARTER_LD:
                case HEAD_QUARTER_RD:
                    tiles[rowCounter][columnCounter] = new HeadQuarter(columnCounter,rowCounter);
                    break;
                default:
                    break;
            }

            columnCounter++;
            if(columnCounter == MAX_MAP_SIZE_X + 1) {
                columnCounter = 0;
                rowCounter++;
            }
        }
    }

    static final int MAX_MAP_SIZE_X = 30;
    static final int MAX_MAP_SIZE_Y = 30;
}
