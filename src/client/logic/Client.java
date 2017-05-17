package client.logic;

import client.gui.GUI_Play;
import client.gui.Panel_Login;
import client.gui.Panel_Save;
import common.item.bullet.Bullet;
import common.item.tank.PlayerTank;
import common.item.tank.Tank;
import common.item.tile.*;
import common.logic.Emitter;
import common.logic.InfoHandler;
import common.logic.MapLoader;
import common.logic.Receiver;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
import java.util.concurrent.atomic.AtomicInteger;

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
    private boolean isWin = false;
    private boolean isGameStart = false;
    private int playerNumber;

    private AtomicInteger player_1_score = new AtomicInteger(0);
    private AtomicInteger player_2_score = new AtomicInteger(0);


    public Client() throws FileNotFoundException {
        // TODO for test
        tiles = new Tile[30][30];
        tanks = new ArrayList<>();
        bullets = new ArrayList<>();
        hero_1 = new PlayerTank(160,448,1);
        hero_2 = new PlayerTank(288,448,2);
        MapLoader.loadMap(new File("D:\\File\\Program\\Projects\\BattleCity\\src\\res\\map\\test.txt"),tiles);
        // TODO for test


        panel_login = new Panel_Login(this);
        gui_play = new GUI_Play(this,tiles,tanks,bullets,hero_1,hero_2,player_1_score,player_2_score);

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

        while(!isGameStart) {
            Thread.sleep(100);
        }

        new JFXPanel();
        String backgroundMusicPath = "D:\\File\\Program\\Projects\\BattleCity\\src\\res\\sound\\background.mp3";
        Media media = new Media(new File(backgroundMusicPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.3);
        mediaPlayer.setAutoPlay(true);

        mediaPlayer.play();

        while(!isGameOver) {
            updateStatus();
            paintGame();
            Thread.sleep(20);
        }

        gui_play.dispose();

        String kHistoryFilePath = "history.txt";
        Panel_Save panel_save = new Panel_Save(this,new File(kHistoryFilePath));

        panel_save.display();
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
                isGameStart = true;
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
            hero_1 = new PlayerTank(160,448,1);
            hero_2 = new PlayerTank(288,448,2);
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

        if(info.startsWith("kill")) {
            switch (info.charAt(5)) {
                case '1':
                    hero_1.deactivate();
                    break;
                case '2':
                    hero_2.deactivate();
                    break;
            }
        }

        if(info.startsWith("stop")) {
            emitter.disable();
        }

        if(info.startsWith("sync") && info.endsWith("%")) {
            String infoUsed = info.substring(0,info.length()-1);

            if(infoUsed.charAt(4) == 'h') {
                if(infoUsed.charAt(5) == '1') {
                    hero_1.loadFromString(infoUsed.substring(6));
                } else if(infoUsed.charAt(5) == '2') {
                    hero_2.loadFromString(infoUsed.substring(6));
                }
            }

            if(infoUsed.charAt(4) == 't') {
                String[] slices = infoUsed.split("_");
                int numTank = Integer.valueOf(slices[1]);

                synchronized (tanks) {
                    tanks.clear();
                    for (int i = 0; i < numTank; i++) {
                        tanks.add(tankFactory(slices[i + 2]));
                    }
                }
            }

            if(infoUsed.charAt(4) == 'b') {
               String[] slices = infoUsed.split("_");
               int numBullet = Integer.valueOf(slices[1]);

               synchronized (bullets) {
                   bullets.clear();
                   for (int i = 0; i < numBullet; i++) {
                       bullets.add(new Bullet(slices[i + 2]));
                   }
               }
            }
        }

        if(info.startsWith("sco")) {
            switch (info.charAt(3)) {
                case '1':
                    player_1_score.set(Integer.valueOf(info.substring(5)));
                    break;
                case '2':
                    player_2_score.set(Integer.valueOf(info.substring(5)));
                    break;
            }
        }

        if(info.startsWith("gmo")) {
            isGameOver = true;
            if(info.charAt(3)=='w') {
                System.out.println("win");
                isWin = true;
            } else {
                System.out.println("lose");
                isWin = false;
            }
        }
    }


    void updateStatus() {

        hero_1.updateLocation();
        hero_1.updateFireDelay();

        hero_2.updateLocation();
        hero_2.updateFireDelay();

        synchronized (bullets) {
            for (Bullet bullet : bullets) {
                bullet.updateLocation();
            }
        }

        synchronized (tanks) {
            for (Tank tank : tanks) {
                tank.updateLocation();
            }
        }

    }


    void paintGame() {
        gui_play.repaintPanelGame();
    }


    public boolean getIsWin() {
        return isWin;
    }


    public int getScore() {
        switch ( playerNumber) {
            case 1:
                return player_1_score.get();
            case 2:
                return player_2_score.get();
        }
        return 0;
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

}