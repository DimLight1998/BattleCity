package server.logic;

import common.item.bullet.Bullet;
import common.item.tank.PlayerTank;
import common.item.tank.Tank;
import common.item.tile.*;
import common.logic.Emitter;
import common.logic.MapLoader;
import javafx.util.Pair;
import server.gui.Panel_Setup;
import server.gui.Panel_Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

import static common.item.tank.Tank.*;
import static common.item.tile.Tile.*;
import static javax.xml.bind.JAXB.marshal;

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

        // TODO for test
        hero_1 = new PlayerTank(0,0);
        hero_2 = new PlayerTank(0,0);

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
        // e.g. prsw1
        if(info.startsWith("prs")) {
            if(info.endsWith("1")) {
                // todo check if illegal
                if(!isInSameDirection(getDirectionFromChar(info.charAt(3)),hero_1.getFacingStatus())) {
                    correctTankLocation(hero_1,hero_1.getFacingStatus());
                    forceSynchronize();
                }

                if(!isBlockedInDirection(hero_1,getDirectionFromChar(info.charAt(3)))) {
                    emitter_1.emit("updp1"+info.charAt(3));
                    emitter_2.emit("updp1"+info.charAt(3));
                    hero_1.setVelocityStatus(getDirectionFromChar(info.charAt(3)));
                    hero_1.setFacingStatus(getDirectionFromChar(info.charAt(3)));
                } else {
                    emitter_1.emit("updp10");
                    emitter_2.emit("updp10");
                    hero_1.setVelocityStatus(kNotMoving);
                }

            } else if(info.endsWith("2")) {
                // todo check if illegal
                if(!isInSameDirection(getDirectionFromChar(info.charAt(3)),hero_2.getFacingStatus())) {
                    correctTankLocation(hero_2,hero_2.getFacingStatus());
                    forceSynchronize();
                }

                if(!isBlockedInDirection(hero_2,getDirectionFromChar(info.charAt(3)))) {
                    emitter_1.emit("updp2" + info.charAt(3));
                    emitter_2.emit("updp2" + info.charAt(3));
                    hero_2.setVelocityStatus(getDirectionFromChar(info.charAt(3)));
                    hero_2.setFacingStatus(getDirectionFromChar(info.charAt(3)));
                } else {
                    emitter_1.emit("updp20");
                    emitter_2.emit("updp20");
                    hero_2.setVelocityStatus(kNotMoving);
                }
            }
        }

        // player release key
        // e.g. rls1
        if(info.startsWith("rls")) {
            if(info.endsWith("1")) {
                emitter_1.emit("updp10");
                emitter_2.emit("updp10");
                hero_1.setVelocityStatus(kNotMoving);
            } else if(info.endsWith("2")) {
                emitter_1.emit("updp20");
                emitter_2.emit("updp20");
                hero_2.setVelocityStatus(kNotMoving);

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
        // todo for test
        mapFile = new File("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\map\\gene.txt");
        MapLoader.loadMap(mapFile,tiles);


        int counter = 0;

        while(!isGameOver) {
            Thread.sleep(20);// TODO 这是必要的吗
            updateStatus();
            checkGameOver();
            emitInfo();

            counter ++;
            if(counter == 5) {
                forceSynchronize();
                counter = 0;
            }
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

    private static Pair<Integer,Integer> getOrderFromLocation(int locationX, int locationY) {
        Integer column = locationX / 16;
        Integer row = locationY / 16;

        return new Pair<Integer,Integer>(row,column);
    }

    private static int getDirectionFromChar(char c) {
        switch (c) {
            case 'a':
                return kDirectionLeft;
            case 's':
                return kDirectionDown;
            case 'd':
                return kDirectionRight;
            case 'w':
                return kDirectionUp;
        }

        return 0;
    }

    private boolean isBlockedInDirection(Tank tank, int direction) {
        // todo for test
        return false;

        /*
        int tankLocationX = tank.getLocationX();
        int tankLocationY = tank.getLocationY();

        switch(direction) {
            case kDirectionLeft:
            {
                Pair<Integer,Integer> order= getOrderFromLocation(tankLocationX-1,tankLocationY);

                if(!tiles[order.getKey()][order.getValue()].isTankThrough()) {
                    return true;
                }

                return false;
                // TODO add tank-tank check
                // Todo add border check
            }
            case kDirectionRight:
            {
                Pair<Integer,Integer> order= getOrderFromLocation(tankLocationX+1+32,tankLocationY);

                if(!tiles[order.getKey()][order.getValue()].isTankThrough()) {
                    return true;
                }

                return false;
                // TODO add tank-tank check
            }
            case kDirectionUp:
            {
                Pair<Integer,Integer> order= getOrderFromLocation(tankLocationX,tankLocationY-1);

                if(!tiles[order.getKey()][order.getValue()].isTankThrough()) {
                    return true;
                }

                return false;
                // TODO add tank-tank check
            }
            case kDirectionDown:
            {
                Pair<Integer,Integer> order= getOrderFromLocation(tankLocationX,tankLocationY+1+32);

                if(!tiles[order.getKey()][order.getValue()].isTankThrough()) {
                    return true;
                }

                return false;
                // TODO add tank-tank check
            }
        }

        return false;
        */
    }

    private void correctTankLocation(Tank tank, int direction) {
        switch (direction) {
            case kDirectionLeft:
            case kDirectionRight:
            {
                int locationX = tank.getLocationX();
                int upper = (locationX/kTankPositionCorrectionUnit+1) *kTankPositionCorrectionUnit;
                int lower = (locationX/kTankPositionCorrectionUnit)*kTankPositionCorrectionUnit;

                if((locationX-lower)>=(upper-locationX)) {
                    tank.setLocationX(upper);
                } else {
                    tank.setLocationX(lower);
                }
                break;
            }
            case kDirectionUp:
            case kDirectionDown:
            {
                int locationY = tank.getLocationY();
                int upper = (locationY/kTankPositionCorrectionUnit+1) * kTankPositionCorrectionUnit;
                int lower = (locationY/kTankPositionCorrectionUnit)*kTankPositionCorrectionUnit;

                if((locationY-lower) >= (upper-locationY)) {
                    tank.setLocationY(upper);
                } else {
                    tank.setLocationY(lower);
                }
                break;
            }
        }
    }

    private boolean isInSameDirection(int direction_1,int direction_2) {
        if(direction_1==direction_2) {
            return true;
        }

        if(direction_1 == kDirectionLeft && direction_2 == kDirectionRight) {
            return true;
        }

        if(direction_1 == kDirectionRight && direction_2 == kDirectionLeft) {
            return true;
        }

        if(direction_1 == kDirectionUp && direction_2 == kDirectionDown) {
            return true;
        }

        if(direction_1 == kDirectionDown && direction_2 == kDirectionUp) {
            return true;
        }

        return false;
    }

    private void updateStatus() {
        hero_1.updateLocation();
        hero_2.updateLocation();
    }

    private void checkGameOver() {

    }

    private void emitInfo() {

    }

    private void forceSynchronize() {
        // heroes sync
        String hero_1_string = hero_1.toString();
        String hero_2_string = hero_2.toString();

        emitter_1.emit("synch1"+hero_1_string);
        emitter_1.emit("synch2"+hero_2_string);
        emitter_2.emit("synch1"+hero_1_string);
        emitter_2.emit("synch2"+hero_2_string);

        // tanks sync
        // todo

        // bullets sync
        // todo
    }


    static final int MAX_MAP_SIZE_X = 30;
    static final int MAX_MAP_SIZE_Y = 30;

    static final int kTankPositionCorrectionUnit = 8;
}
