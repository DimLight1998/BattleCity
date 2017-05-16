package client.logic;

import client.gui.GUI_Play;
import client.gui.Panel_Login;
import common.item.bullet.Bullet;
import common.item.tank.PlayerTank;
import common.item.tank.Tank;
import common.item.tile.*;
import common.logic.Emitter;
import common.logic.InfoHandler;
import common.logic.MapLoader;
import common.logic.Receiver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static common.item.tank.Tank.*;


/**
 * Created on 2017/04/30.
 */
public class Client implements ActionListener,KeyListener,InfoHandler{
    private InetAddress IPAddress;
    private int portNumber;
    private Panel_Login panel_login;
    private GUI_Play gui_play;
    private Emitter emitter;
    private Receiver receiver;

    Tile[][] tiles;
    ArrayList<Tank> tanks;
    ArrayList<Bullet> bullets;
    Tank hero_1;
    Tank hero_2;

    private boolean isReady = false;
    private boolean isGameOver = false;
    private int playerNumber;


    public Client() throws FileNotFoundException {
        // TODO for test
        tiles = new Tile[30][30];
        tanks = new ArrayList<>();
        bullets = new ArrayList<>();
        hero_1 = new PlayerTank(PLAYER_1_INIT_LOC_X,PLAYER_1_INIT_LOC_Y);
        hero_2 = new PlayerTank(PLAYER_2_INIT_LOC_X,PLAYER_2_INIT_LOC_Y);
        MapLoader.loadMap(new File("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\map\\test.txt"),tiles);
        // TODO for test


        panel_login = new Panel_Login(this);
        gui_play = new GUI_Play(this,tiles,tanks,bullets,hero_1,hero_2);

    }


    // Main function
    public void start() throws InterruptedException, IOException {
        panel_login.display();

        while(!isReady) {
            Thread.sleep(100);
        }

        panel_login.dispose();
        // TODO remove
        System.out.println("Connected to the server");

        gui_play.display();

        while(!isGameOver) {
            updateStatus();
            paintGame();
            Thread.sleep(20);
        }
    }


    public void handleInfo(String info, InetAddress inetAddress){
        if(info.startsWith("dis")) {
            playerNumber =Character.getNumericValue(info.charAt(3));

            // TODO remove
            System.out.println("This client plays as player "+playerNumber);

            int newPort = Integer.parseInt(info.substring(4));
            emitter.closeSocket();
            try {
                emitter = new Emitter(IPAddress,newPort);
                System.out.println("Emitter is locked on port"+newPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(info.startsWith("isb")) {
            if(info.charAt(3) == '_') {
                if(info.endsWith("1")) {
                    bullets.add(new Bullet(hero_1));
                } else if(info.endsWith("2")) {
                    bullets.add(new Bullet(hero_2));
                }
            } else {
                // todo bullets from AI
            }
        }

        if(info.startsWith("init")) {
            tiles = new Tile[30][30];
            tanks = new ArrayList<>();
            bullets = new ArrayList<>();
            hero_1 = new PlayerTank(PLAYER_1_INIT_LOC_X,PLAYER_1_INIT_LOC_Y);
            hero_2 = new PlayerTank(PLAYER_2_INIT_LOC_X,PLAYER_2_INIT_LOC_Y);
        }

        if(info.startsWith("updp")) {
            Tank updated = (info.charAt(4)=='1'?hero_1:hero_2);
            switch (info.charAt(5)) {
                case 'w':
                    updated.setVelocityStatus(kMovingUp);
                    updated.setFacingStatus(kDirectionUp);
                    break;
                case 'a':
                    updated.setVelocityStatus(kMovingLeft);
                    updated.setFacingStatus(kDirectionLeft);
                    break;
                case 's':
                    updated.setVelocityStatus(kMovingDown);
                    updated.setFacingStatus(kDirectionDown);
                    break;
                case 'd':
                    updated.setVelocityStatus(kMovingRight);
                    updated.setFacingStatus(kDirectionRight);
                    break;
                case '0':
                    updated.setVelocityStatus(kNotMoving);
                    break;
            }
        }

        if(info.startsWith("det")) {
            String[] slices = info.split("_");
            int row = Integer.parseInt(slices[1]);
            int column = Integer.parseInt(slices[2]);
            tiles[row][column] = new PlainTile(row,column);
        }

        if(info.startsWith("sync")) {
            if(info.charAt(4) == 'h') {
                if(info.charAt(5) == '1') {
                    hero_1.loadFromString(info.substring(6));
                } else if(info.charAt(5) == '2') {
                    hero_2.loadFromString(info.substring(6));
                }
            }

            // todo tank sync

            if(info.charAt(4) == 'b') {
               String[] slices = info.split("_");
               int numBullet = Integer.valueOf(slices[1]);

               bullets = new ArrayList<>(numBullet);
               for(int i = 0;i<numBullet;i++) {
                   bullets.add(new Bullet(slices[i+2]));
               }
            }
        }

        if(info.startsWith("gmo")) {
            if(info.charAt(3)=='w') {
                // todo remove
                System.out.println("win");
            } else {
                System.out.println("lose");
            }
        }
    }

    void updateStatus() {
        // todo for test
        tiles[5][5] = new Water(5,5);

        hero_1.updateLocation();
        hero_1.updateFireDelay();

        hero_2.updateLocation();
        hero_2.updateFireDelay();

        for(Bullet bullet : bullets) {
            bullet.updateLocation();
        }

    }


    void paintGame() {
        gui_play.repaintPanelGame();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start")) {
            try {
                IPAddress = panel_login.getAddress();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
            portNumber = panel_login.getPort();

            try {
                emitter = new Emitter(IPAddress,portNumber);
                receiver = new Receiver(0,this);
                receiver.start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            emitter.emit("reg"+receiver.getLocalPort());
            isReady = true;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if("wasd".indexOf(e.getKeyChar()) >= 0) {
            emitter.emit("prs" + Character.toLowerCase(e.getKeyChar()) + playerNumber);
        } else if(e.getKeyChar() == ' ') {
            emitter.emit("fir"+playerNumber);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        // TODO delete this
        if(e.getKeyChar() == 'e') {
            System.out.println("press.");
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        emitter.emit("rls"+playerNumber);
    }


    static final int PLAYER_1_INIT_LOC_X = 0;
    static final int PLAYER_1_INIT_LOC_Y = 0;
    static final int PLAYER_2_INIT_LOC_X = 0;
    static final int PLAYER_2_INIT_LOC_Y = 0;

    static final int ENEMY_SPAN_1_LOC_X = 0;
    static final int ENEMY_SPAN_1_LOC_Y = 0;
    static final int ENEMY_SPAN_2_LOC_X = 0;
    static final int ENEMY_SPAN_2_LOC_Y = 0;
    static final int ENEMY_SPAN_3_LOC_X = 0;
    static final int ENEMY_SPAN_3_LOC_Y = 0;
}
