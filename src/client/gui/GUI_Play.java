package client.gui;

import client.logic.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public class GUI_Play extends JPanel{
    Panel_Game panel_game;
    JFrame mainFrame;


    GUI_Play(Client client) {
        panel_game = new Panel_Game();

        this.setLayout(new GridLayout(1,1));
        this.add(panel_game);

        mainFrame = new JFrame("Battle City");

        mainFrame.setContentPane(this);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setSize(750,600 /*509 is best size*/);
        mainFrame.setLocationRelativeTo(null);

        mainFrame.addKeyListener(client);
    }

    void display() {
        mainFrame.setVisible(true);
    }
}
