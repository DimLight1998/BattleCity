package client.logic;

import client.gui.Panel_Game;
import client.gui.Panel_Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created on 2017/04/30.
 */
public class Client implements ActionListener,KeyListener {
    InetAddress IPAddress;
    int portNumber;
    Panel_Login panel_login;
    Panel_Game panel_game;

    public Client() {
        panel_login = new Panel_Login(this);
    }

    public void start() {
        panel_login.display();
    }

    void handleInfo(String info) {
        // TODO
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
        }

        System.out.println(IPAddress);
        System.out.println(portNumber);
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

    }
}