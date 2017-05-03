package client.logic;

import client.gui.Panel_Game;
import client.gui.Panel_Login;
import common.logic.Emitter;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created on 2017/04/30.
 */
public class Client implements ActionListener,KeyListener {
    InetAddress IPAddress;
    int portNumber;
    int localPortNumber;
    Panel_Login panel_login;
    Panel_Game panel_game;
    Emitter emitter;
    Receiver receiver;

    boolean isReady = false;
    int playerNumber;

    public Client() {
        panel_login = new Panel_Login(this);
    }

    public void start() throws InterruptedException, IOException {
        panel_login.display();

        while(!isReady) {
            Thread.sleep(100);
        }

        // TODO remove
        panel_login.dispose();
        System.out.println("haha");

        while(true) {
            Thread.sleep(100);
        }

    }

    void handleInfo(String info) {
        if(info.startsWith("dis")) {
            playerNumber =Character.getNumericValue(info.charAt(3));

            // TODO remove
            System.out.println("i am player "+playerNumber);
        }
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
                receiver = new Receiver(this);
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
        // TODO delete this
        if(e.getKeyChar() == 'e') {
            System.out.println("is typed.");
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
        System.out.println("released");
    }
}
