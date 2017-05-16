package server.logic;

import common.item.bullet.Bullet;
import common.item.tank.PlayerTank;
import common.item.tank.Tank;
import common.item.tile.*;
import common.logic.*;
import javafx.util.Pair;
import server.gui.Panel_Setup;
import server.gui.Panel_Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Iterator;

import static common.item.tank.Tank.*;

/**
 * Created on 2017/05/01.
 */
public class Server implements ActionListener, InfoHandler{
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
    private MultipleReceiver multipleReceiver;
    private Receiver receiver_1;
    private Receiver receiver_2;
    private Panel_Setup panel_setup;
    Panel_Status panel_status;
    private File mapFile;

    boolean isPortSet = false;
    boolean isPlayerReady_1 = false;
    boolean isPlayerReady_2 = false;
    boolean isGameOver = false;

    public Server() {
        tiles = new Tile[30][30];
        tanks = new ArrayList<>(10);
        bullets = new ArrayList<>(20);

        // TODO for test
        hero_1 = new PlayerTank(0,0);
        hero_2 = new PlayerTank(0,0);

        panel_setup = new Panel_Setup(this);
//        panel_status = new Panel_Status();

    }

    public void handleInfo(String info, InetAddress address) {
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

                hero_1.setFacingStatus(getDirectionFromChar(info.charAt(3)));
                if(!isBlockedInDirection(hero_1,getDirectionFromChar(info.charAt(3)))) {
                    emitter_1.emit("updp1"+info.charAt(3));
                    emitter_2.emit("updp1"+info.charAt(3));
                    hero_1.setVelocityStatus(getDirectionFromChar(info.charAt(3)));
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

                hero_2.setFacingStatus(getDirectionFromChar(info.charAt(3)));
                if(!isBlockedInDirection(hero_2,getDirectionFromChar(info.charAt(3)))) {
                    emitter_1.emit("updp2" + info.charAt(3));
                    emitter_2.emit("updp2" + info.charAt(3));
                    hero_2.setVelocityStatus(getDirectionFromChar(info.charAt(3)));
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
                if(hero_1.isAbleToFire()) {
                    bullets.add(new Bullet(hero_1));
                    hero_1.resetFireDelay();
                    broadcast("isb_1");
                }
            } else if(info.endsWith("2")) {
                if(hero_2.isAbleToFire()) {
                    bullets.add(new Bullet(hero_2));
                    hero_2.resetFireDelay();
                    broadcast("isb_2");
                }
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
        receiver_1 = new Receiver(0,this);
        receiver_2 = new Receiver(0,this);

        receiver_1.start();
        receiver_2.start();

        multipleReceiver = new MultipleReceiver(serverPortNumber,this);
        multipleReceiver.start();


        while(!isPlayerReady_2) {
            Thread.sleep(100);
        }

        emitter_1 = new Emitter(address_1,portNumber_1);
        emitter_2 = new Emitter(address_2,portNumber_2);

        emitter_1.emit("dis1"+receiver_1.getLocalPort());
        emitter_2.emit("dis2"+receiver_2.getLocalPort());
//        panel_status.show();

        // TODO remove this
        System.out.println("ready");

        // TODO complete map selection
        // todo for test
        mapFile = new File("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\map\\test.txt");
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

    public static Pair<Integer,Integer> getOrderFromLocation(int locationX, int locationY) {
        Integer column = locationX / 16;
        if(locationX < 0) {
            column = -1;
        }

        Integer row = locationY / 16;
        if(locationY < 0) {
            row = -1;
        }

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
        int tankLocationX = tank.getLocationX();
        int tankLocationY = tank.getLocationY();

        switch(direction) {
            case kDirectionLeft:
            {
                Pair<Integer,Integer> order_1 = getOrderFromLocation(tankLocationX-tank.getMoveVelocity(),tankLocationY);
                Pair<Integer,Integer> order_2 = getOrderFromLocation(tankLocationX-tank.getMoveVelocity(),tankLocationY+16);

                if(!isInBoarder(order_1) || !isInBoarder(order_2)) {
                    return true;
                }

                System.out.printf("moving%d %d %d %d %d\n",direction,order_1.getKey(),order_1.getValue(),order_2.getKey(), order_2.getValue());
                if(tiles[order_1.getKey()][order_1.getValue()].isTankThrough() && tiles[order_2.getKey()][order_2.getValue()].isTankThrough()) {
                    System.out.println("false returned because not blocked.");
                    return false;
                }

                // TODO add tank-tank check

                System.out.println("true returned because blocked.");
                return true;
            }
            case kDirectionRight:
            {
                Pair<Integer,Integer> order_1= getOrderFromLocation(tankLocationX+32+tank.getMoveVelocity(),tankLocationY);
                Pair<Integer,Integer> order_2= getOrderFromLocation(tankLocationX+32+tank.getMoveVelocity(),tankLocationY+16);

                if(!isInBoarder(order_1) || !isInBoarder(order_2)) {
                    return true;
                }

                System.out.printf("moving%d %d %d %d %d\n",direction,order_1.getKey(),order_1.getValue(),order_2.getKey(), order_2.getValue());
                if(tiles[order_1.getKey()][order_1.getValue()].isTankThrough() && tiles[order_2.getKey()][order_2.getValue()].isTankThrough()) {
                    System.out.println("false returned because not blocked.");
                    return false;
                }

                System.out.println("true returned because blocked.");
                return true;
                // TODO add tank-tank check
            }
            case kDirectionUp:
            {
                Pair<Integer,Integer> order_1= getOrderFromLocation(tankLocationX,tankLocationY-tank.getMoveVelocity());
                Pair<Integer,Integer> order_2= getOrderFromLocation(tankLocationX+16,tankLocationY-tank.getMoveVelocity());

                if(!isInBoarder(order_1) || !isInBoarder(order_2)) {
                    return true;
                }

                System.out.printf("moving%d %d %d %d %d\n",direction,order_1.getKey(),order_1.getValue(),order_2.getKey(), order_2.getValue());
                if(tiles[order_1.getKey()][order_1.getValue()].isTankThrough() && tiles[order_2.getKey()][order_2.getValue()].isTankThrough()) {
                    System.out.println("false returned because not blocked.");
                    return false;
                }

                System.out.println("true returned because blocked.");
                return true;
                // TODO add tank-tank check
            }
            case kDirectionDown:
            {
                Pair<Integer,Integer> order_1= getOrderFromLocation(tankLocationX,tankLocationY+32+tank.getMoveVelocity());
                Pair<Integer,Integer> order_2= getOrderFromLocation(tankLocationX+16,tankLocationY+32+tank.getMoveVelocity());

                if(!isInBoarder(order_1) || !isInBoarder(order_2)) {
                    return true;
                }

                System.out.printf("moving%d %d %d %d %d\n",direction,order_1.getKey(),order_1.getValue(),order_2.getKey(), order_2.getValue());
                if(tiles[order_1.getKey()][order_1.getValue()].isTankThrough() && tiles[order_2.getKey()][order_2.getValue()].isTankThrough()) {
                    System.out.println("false returned because not blocked.");
                    return false;
                }

                System.out.println("true returned because blocked.");
                return true;
                // TODO add tank-tank check
            }
        }

        return false;

    }

    private Pair<Integer,Pair<Pair<Integer,Integer>,Pair<Integer,Integer>>> getHittingWallInfoPack(Bullet bullet) {
        int bulletLocationX = bullet.getLocationX();
        int bulletLocationY = bullet.getLocationY();
        int bulletDirection = bullet.getVelocityStatus();

        Pair<Integer,Integer> order_1 = new Pair<>(0,0);
        Pair<Integer,Integer> order_2 = new Pair<>(0,0);


        switch (bulletDirection) {
            case kMovingLeft:
            {
             order_1 = getOrderFromLocation(bulletLocationX-bullet.getBulletVelocity(),bulletLocationY);
              order_2 = getOrderFromLocation(bulletLocationX-bullet.getBulletVelocity(),bulletLocationY-16);
              break;
            }
            case kMovingRight:
            {
               order_1 = getOrderFromLocation(bulletLocationX+bullet.getBulletVelocity(),bulletLocationY);
              order_2 = getOrderFromLocation(bulletLocationX+bullet.getBulletVelocity(),bulletLocationY-16);
                break;
            }
            case kMovingUp:
            {
                order_1 = getOrderFromLocation(bulletLocationX-16,bulletLocationY-bullet.getBulletVelocity());
                order_2 = getOrderFromLocation(bulletLocationX,bulletLocationY-bullet.getBulletVelocity());
                break;
            }
            case kMovingDown:
            {
                 order_1 = getOrderFromLocation(bulletLocationX-16,bulletLocationY+bullet.getBulletVelocity());
                order_2 = getOrderFromLocation(bulletLocationX,bulletLocationY+bullet.getBulletVelocity());
                break;
            }
        }


        if(!isInBoarder(order_1) || !isInBoarder(order_2)) {
            return new Pair<>(kBulletOutOfBoard,new Pair<>(order_1,order_2));
        }

        if(tiles[order_1.getKey()][order_1.getValue()].isBulletThrough() && tiles[order_2.getKey()][order_2.getValue()].isBulletThrough()) {
            return new Pair<>(kBulletNotHitWall,new Pair<>(order_1,order_2));
        }

        // todo add tank check
        return new Pair<>(kBulletHitWall,new Pair<>(order_1,order_2));
    }

    private void destroyTile(Pair<Integer,Integer> pairRowColumn) {
        if (tiles[pairRowColumn.getKey()][pairRowColumn.getValue()].isDamageable()) {
            tiles[pairRowColumn.getKey()][pairRowColumn.getValue()] = new PlainTile(pairRowColumn.getKey(), pairRowColumn.getValue());
            broadcast(String.format("det_%d_%d",pairRowColumn.getKey(),pairRowColumn.getValue()));
        }
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
        if(!isBlockedInDirection(hero_1,hero_1.getVelocityStatus())) {
            hero_1.updateLocation();
        } else {
            broadcast("updp10");
        }

        if(!isBlockedInDirection(hero_2,hero_2.getVelocityStatus())) {
            hero_2.updateLocation();
        } else {
            broadcast("updp20");
        }

        hero_1.updateFireDelay();
        hero_2.updateFireDelay();

        // todo add collision check

        Iterator<Bullet> bulletIterator = bullets.iterator();

        while(bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();

            Pair<Integer,Pair<Pair<Integer,Integer>,Pair<Integer,Integer>>> hittingInfoPack = getHittingWallInfoPack(bullet);

            if(hittingInfoPack.getKey()==kBulletOutOfBoard) {
                bulletIterator.remove();
                continue;
            }

            if(hittingInfoPack.getKey()==kBulletHitWall) {
                bulletIterator.remove();
                destroyTile(hittingInfoPack.getValue().getKey());
                destroyTile(hittingInfoPack.getValue().getValue());
                continue;
            }

            bullet.updateLocation();
        }
    }

    private void checkGameOver() {

    }

    private void emitInfo() {

    }

    private void forceSynchronize() {
        // heroes sync
        String hero_1_string = hero_1.toString();
        String hero_2_string = hero_2.toString();

        broadcast("synch1"+hero_1_string+"%");
        broadcast("synch2"+hero_2_string+"%");

        // tanks sync
        // todo

        // bullets sync
        StringBuilder bullets_string = new StringBuilder("syncb_" + bullets.size());

        for(Bullet bullet : bullets) {
            bullets_string.append("_").append(bullet.toString());
        }

        broadcast(bullets_string.toString()+"%");

        // tiles sync
    }


    private void broadcast(String info) {
        emitter_1.emit(info);
        emitter_2.emit(info);
    }


    private boolean isInBoarder(Pair<Integer,Integer> pair) {
        return (pair.getKey() >= 0 && pair.getKey() < MAX_MAP_SIZE && pair.getValue() >= 0 && pair.getValue() < MAX_MAP_SIZE);
    }


    private static final int MAX_MAP_SIZE = 30;

    private static final int kTankPositionCorrectionUnit = 16;

    private static final int kBulletOutOfBoard = 0;
    private static final int kBulletHitWall = 1;
    private static final int kBulletNotHitWall = 2;

}
