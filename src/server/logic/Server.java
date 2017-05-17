package server.logic;

import common.item.bullet.Bullet;
import common.item.bullet.SuperBullet;
import common.item.tank.*;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

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

    private int tankRemain;
    private int tankActivated;

    public Server() {
        tiles = new Tile[30][30];
        tanks = new ArrayList<>();// todo
        bullets = new ArrayList<>();

        // TODO for test
        hero_1 = new PlayerTank(160,448);
        hero_2 = new PlayerTank(288,448);

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
                    forceSynchronizePlayer();
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
                    forceSynchronizePlayer();
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
                    bullets.add(new SuperBullet(hero_1));
                    hero_1.resetFireDelay();
                    broadcast("isb_1");
                }
            } else if(info.endsWith("2")) {
                if(hero_2.isAbleToFire()) {
                    bullets.add(new SuperBullet(hero_2));
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

        AIInitialize();


        // game main
        int counter = 0;

        while(!isGameOver) {
            Thread.sleep(20);// TODO 这是必要的吗
            updateStatus();
            AIUpdate();
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

        return new Pair<>(row,column);
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

        Pair<Integer,Integer> order_1 = new Pair<>(0,0);
        Pair<Integer,Integer> order_2 = new Pair<>(0,0);

        switch(direction) {
            case kDirectionLeft:
            {
                order_1 = getOrderFromLocation(tankLocationX-tank.getMoveVelocity(),tankLocationY);
                order_2 = getOrderFromLocation(tankLocationX-tank.getMoveVelocity(),tankLocationY+16);
                break;
            }
            case kDirectionRight:
            {
                order_1= getOrderFromLocation(tankLocationX+32+tank.getMoveVelocity(),tankLocationY);
                order_2= getOrderFromLocation(tankLocationX+32+tank.getMoveVelocity(),tankLocationY+16);
                break;
            }
            case kDirectionUp:
            {
                order_1= getOrderFromLocation(tankLocationX,tankLocationY-tank.getMoveVelocity());
                order_2= getOrderFromLocation(tankLocationX+16,tankLocationY-tank.getMoveVelocity());
                break;
            }
            case kDirectionDown:
            {
                order_1= getOrderFromLocation(tankLocationX,tankLocationY+32+tank.getMoveVelocity());
                order_2= getOrderFromLocation(tankLocationX+16,tankLocationY+32+tank.getMoveVelocity());
                break;
            }
        }

        if(!isInBoarder(order_1) || !isInBoarder(order_2)) {
            return true;
        }

        // TODO add tank-tank check
        if(hero_1!=tank) {
            if(isTileBlockedByTank(order_1,hero_1) && isTileBlockedByTank(order_2,hero_1)) {
                return true;
            }
        }

        if(hero_2!=tank) {
            if(isTileBlockedByTank(order_1,hero_2) && isTileBlockedByTank(order_2,hero_2)) {
                return true;
            }
        }

        for(Tank tankIter:tanks) {
            if(tank!=tankIter) {
                if(isTileBlockedByTank(order_1,tankIter) && isTileBlockedByTank(order_2,tankIter)) {
                    return true;
                }
            }
        }


        System.out.printf("moving%d %d %d %d %d\n",direction,order_1.getKey(),order_1.getValue(),order_2.getKey(), order_2.getValue());
        if(tiles[order_1.getKey()][order_1.getValue()].isTankThrough() && tiles[order_2.getKey()][order_2.getValue()].isTankThrough()) {
            System.out.println("false returned because not blocked.");
            return false;
        }



        System.out.println("true returned because blocked.");
        return true;
    }


    private boolean isTileBlockedByTank(Pair<Integer,Integer> order,Tank tank) {
        int row = order.getKey();
        int column = order.getValue();
        int locationX = tank.getLocationX();
        int locationY = tank.getLocationY();
        Pair<Integer,Integer> blockLU = getOrderFromLocation(locationX,locationY);

        if((locationX % 16 == 0)&&(locationY % 16 == 0)) {
            if((row >= blockLU.getKey())&&(row <= blockLU.getKey()+1)&&(column >= blockLU.getValue())&&(column <= blockLU.getValue()+1)) {
                return true;
            }
        }

        if((locationX % 16 == 0)&&(locationY % 16 !=0)) {
            if((row >= blockLU.getKey())&&(row <= blockLU.getKey()+2)&&(column >= blockLU.getValue())&&(column <= blockLU.getValue()+1)) {
                return true;
            }
        }

        if((locationX % 16 != 0)&&(locationY % 16 ==0)) {
            if((row >= blockLU.getKey())&&(row <= blockLU.getKey()+1)&&(column >= blockLU.getValue())&&(column <= blockLU.getValue()+2)) {
                return true;
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
        long startTime = System.currentTimeMillis();

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

        bulletSimplify();

        Iterator<Bullet> bulletIterator;

        // check bullet-tank
        synchronized (bullets) {
            bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();

                if (isBulletHittingTank(bullet, hero_1)) {
                    // todo
                }

                if (isBulletHittingTank(bullet, hero_2)) {
                    // todo
                }

                if (bullet.isSuper()) {
                    for (Tank tank : tanks) {
                        if (isBulletHittingTank(bullet, tank)) {
                            tank.decreaseHealth();
                            bulletIterator.remove();
                        }
                    }
                }
            }

            bulletIterator = bullets.iterator();

            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();

                Pair<Integer, Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> hittingInfoPack = getHittingWallInfoPack(bullet);

                if (hittingInfoPack.getKey() == kBulletOutOfBoard) {
                    bulletIterator.remove();
                    continue;
                }

                if (hittingInfoPack.getKey() == kBulletHitWall) {
                    bulletIterator.remove();
                    destroyTile(hittingInfoPack.getValue().getKey());
                    destroyTile(hittingInfoPack.getValue().getValue());
                    continue;
                }

                bullet.updateLocation();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Updated, "+(endTime-startTime)+" ms");
    }


    private void bulletSimplify() {
        boolean bulletsRemoved = false;
        ArrayList<Integer> simplifyIndexes = new ArrayList<>();

        for(int i = 0;i<bullets.size();i++) {
            for(int j = i+1;j<bullets.size();j++) {
                if(isBulletsReducible(bullets.get(i),bullets.get(j)) || isBulletsReducible(bullets.get(j),bullets.get(i))) {
                    if((!simplifyIndexes.contains(i))&&(!simplifyIndexes.contains(j))) {
                        simplifyIndexes.add(i);
                        simplifyIndexes.add(j);
                        bulletsRemoved = true;
                    }
                }
            }
        }

        simplifyIndexes.sort(Collections.reverseOrder());
        for(int i : simplifyIndexes) {
            bullets.remove(i);
        }

        if(bulletsRemoved) {
            System.out.println("Bullets removed.");
            forceSynchronizeBullet();
        }
    }


    private static boolean isBulletsReducible(Bullet bullet_1,Bullet bullet_2) {
        int delta_x = bullet_1.getLocationX()-bullet_2.getLocationX();
        int delta_y = bullet_1.getLocationY()-bullet_2.getLocationY();

        if((bullet_1.getLocationY() == bullet_2.getLocationY()) && (delta_x>=-2) &&(delta_x <=2)
                && (bullet_1.getVelocityStatus()==kMovingRight)&&(bullet_2.getVelocityStatus()==kMovingLeft)) {
            return true;
        }

        if((bullet_1.getLocationX() == bullet_2.getLocationX()) && (delta_y>=-2) &&(delta_y <=2)
                && (bullet_1.getVelocityStatus()==kMovingDown)&&(bullet_2.getVelocityStatus()==kMovingUp)) {
            return true;
        }

        return false;
    }


    private boolean isBulletHittingTank(Bullet bullet,Tank tank) {
        int tankLocationX = tank.getLocationX();
        int tankLocationY = tank.getLocationY();

        int bulletLocationX = bullet.getLocationX();
        int bulletLocationY = bullet.getLocationY();


        if((tankLocationX%16 == 0) && (tankLocationY % 16 == 0)) {
            int tankCentLocationX = tankLocationX + 16;
            int tankCentLocationY = tankLocationY + 16;

            switch (bullet.getVelocityStatus()) {
                case kMovingLeft:
                case kMovingRight:
                    if((bulletLocationY == tankCentLocationY) && (Math.abs(bulletLocationX-tankCentLocationX)<=2)) {
                        return true;
                    }
                    break;
                case kMovingUp:
                case kMovingDown:
                    if((bulletLocationX == tankCentLocationX) && (Math.abs(bulletLocationY-tankCentLocationY)<=2)) {
                        return true;
                    }
                    break;
            }
        } else if(tankLocationX%16 != 0) {
            int tankLocationLeftBound = tankLocationX / 16;
            tankLocationLeftBound *= 16;

            int tankLineLocationX_1 = tankLocationLeftBound + 16;
            int tankLineLocationX_2 = tankLocationLeftBound + 32;
            int tankCentLocationX = tankLocationX + 16;
            int tankCentLocationY = tankLocationY + 16;

            switch (bullet.getVelocityStatus()) {
                case kMovingLeft:
                case kMovingRight:
                    if((bulletLocationY == tankCentLocationY) && (Math.abs(bulletLocationX-tankCentLocationX)<=2)) {
                        return true;
                    }
                    break;
                case kMovingUp:
                case kMovingDown:
                    if((bulletLocationX == tankLineLocationX_1) || (bulletLocationX == tankLineLocationX_2)) {
                        if (Math.abs(bulletLocationY - tankCentLocationY)<=2) {
                            return true;
                        }
                    }
                    break;
            }
        } else if(tankLocationY % 16 != 0) {
            int tankLocationUpBound = tankLocationY / 16;
            tankLocationUpBound *= 16;

            int tankLineLocationY_1 = tankLocationUpBound + 16;
            int tankLineLocationY_2 = tankLocationUpBound + 32;
            int tankCentLocationX = tankLocationX + 16;
            int tankCentLocationY = tankLocationY + 16;

            switch (bullet.getVelocityStatus()) {
                case kMovingLeft:
                case kMovingRight:
                    if((bulletLocationY == tankLineLocationY_1) || (bulletLocationY == tankLineLocationY_2)) {
                        if(Math.abs(bulletLocationX - tankCentLocationX) <= 2) {
                            return true;
                        }
                    }
                    break;
                case kMovingUp:
                case kMovingDown:
                    if((bulletLocationX == tankCentLocationX) && (Math.abs(bulletLocationY - tankCentLocationY) <= 2)) {
                        return true;
                    }
                    break;
            }
        }

        return false;
    }


    private void checkGameOver() {

    }


    private void emitInfo() {

    }


    private void forceSynchronize() {
        forceSynchronizePlayer();
        forceSynchronizeTank();
        forceSynchronizeBullet();
    }


    private void forceSynchronizePlayer() {
        // heroes sync
        String hero_1_string = hero_1.toString();
        String hero_2_string = hero_2.toString();

        broadcast("synch1"+hero_1_string+"%");
        broadcast("synch2"+hero_2_string+"%");
    }


    private void forceSynchronizeTank() {
        // tanks sync
        StringBuilder tanks_string = new StringBuilder("synct_"+tanks.size());

        for(Tank tank: tanks) {
            tanks_string.append("_").append(tank.toString());
        }

        broadcast(tanks_string.toString()+"%");
    }


    private void forceSynchronizeBullet() {
        // bullets sync
        StringBuilder bullets_string = new StringBuilder("syncb_" + bullets.size());

        for(Bullet bullet : bullets) {
            bullets_string.append("_").append(bullet.toString());
        }

        broadcast(bullets_string.toString()+"%");

    }


    private void broadcast(String info) {
        emitter_1.emit(info);
        emitter_2.emit(info);
    }


    private boolean isInBoarder(Pair<Integer,Integer> pair) {
        return (pair.getKey() >= 0 && pair.getKey() < MAX_MAP_SIZE && pair.getValue() >= 0 && pair.getValue() < MAX_MAP_SIZE);
    }


    private void AIInitialize() {
        int[] initSpawnLocationX = {0*16,9*16,19*16,28*16};

        for(int i = 0;i<kInitEnemyNumber;i++) {
            int tankID = ThreadLocalRandom.current().nextInt(1, 5);
            switch (tankID) {
                case kHeavyTankID:
                    tanks.add(new HeavyTank(initSpawnLocationX[i],0));
                    break;
                case kLightTankID:
                    tanks.add(new LightTank(initSpawnLocationX[i],0));
                    break;
                case kArmoredTankID:
                    tanks.add(new ArmoredTank(initSpawnLocationX[i],0));
                    break;
                case kTankDestroyerID:
                    tanks.add(new TankDestroyer(initSpawnLocationX[i],0));
                    break;
            }
        }

        for(Tank tank:tanks) {
            tank.activate();
            tank.resetFireDelay();
        }

        tankActivated = kInitEnemyNumber;
        tankRemain = kEnemyTankNumber;
    }


    private void AIUpdate() {
        boolean needSynchronization = false;

        Iterator<Tank> tankIterator = tanks.iterator();
        while(tankIterator.hasNext()) {
            if(!tankIterator.next().isActivated()) {
                tankIterator.remove();
                needSynchronization = true;
            }
        }


        for(Tank tank:tanks) {
            tank.updateFireDelay();
            tank.tryFire(bullets);

            if(!isBlockedInDirection(tank,tank.getFacingStatus())) {
                tank.updateLocation();
                int rand = ThreadLocalRandom.current().nextInt(0, 50);
                if(rand == 0) {
                    tank.setVelocityStatus(ThreadLocalRandom.current().nextInt(0, 5));
                }
            } else {
                tank.setVelocityStatus(ThreadLocalRandom.current().nextInt(0, 5));
            }

            if(!isInSameDirection(tank.getFacingStatus(),tank.getVelocityStatus())) {
                correctTankLocation(tank,tank.getFacingStatus());
                needSynchronization = true;
            }

            if(tank.getVelocityStatus() != kNotMoving) {
                tank.setFacingStatus(tank.getVelocityStatus());
            }
        }

        if(needSynchronization) {
            forceSynchronizeTank();
        }
    }


    private static final int MAX_MAP_SIZE = 30;
    private static final int kTankPositionCorrectionUnit = 16;

    private static final int kBulletOutOfBoard = 0;
    private static final int kBulletHitWall = 1;
    private static final int kBulletNotHitWall = 2;

    private static final int kEnemyTankNumber = 20;
    private static final int kInitEnemyNumber = 4;
}
