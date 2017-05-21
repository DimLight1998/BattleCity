package common.logic;


import client.logic.Client;
import server.logic.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created on 2017/05/18.
 */
public class AppRunner extends JPanel implements ActionListener {
    private JFrame  mainFrame;
    private boolean isClient   = false;
    private boolean isServer   = false;
    private boolean isDesigner = false;


    public static void main(String[] args) throws InterruptedException {
        AppRunner appRunner = new AppRunner();

        appRunner.display();
        while ((!appRunner.isServer) && (!appRunner.isClient) && (!appRunner.isDesigner)) {
            Thread.sleep(100);
        }

        if (appRunner.isClient) {
            openClient();
        } else if (appRunner.isServer) {
            openServer();
        } else {
            openDesigner();
        }
    }


    private void display() {
        mainFrame.setVisible(true);
    }


    private AppRunner() {
        mainFrame = new JFrame("Battle City");
        mainFrame.setLayout(new GridLayout(2, 1));

        JLabel label_select = new JLabel("Battle City", SwingConstants.CENTER);
        label_select.setFont(new Font("Romans Times", Font.BOLD, 20));
        mainFrame.add(label_select);

        JPanel operations = new JPanel(new GridLayout(1, 0));

        JButton button_client = new JButton("Start as a player");
        button_client.addActionListener(this);
        JButton button_server = new JButton("Start as server");
        button_server.addActionListener(this);
        JButton button_designer = new JButton("Map designer");
        button_designer.addActionListener(this);
        JButton button_exit = new JButton("Exit");
        button_exit.addActionListener(this);

        JPanel panel_client = new JPanel(new FlowLayout());
        panel_client.add(button_client);
        JPanel panel_server = new JPanel(new FlowLayout());
        panel_server.add(button_server);
        JPanel panel_designer = new JPanel(new FlowLayout());
        panel_designer.add(button_designer);
        JPanel panel_exit = new JPanel(new FlowLayout());
        panel_exit.add(button_exit);

        operations.add(panel_client);
        operations.add(panel_server);
        operations.add(panel_designer);
        operations.add(panel_exit);

        mainFrame.add(operations);
        mainFrame.setSize(600, 300);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        } else if (e.getActionCommand().endsWith("player")) {
            mainFrame.dispose();
            isClient = true;
        } else if (e.getActionCommand().endsWith("server")) {
            mainFrame.dispose();
            isServer = true;
        } else if (e.getActionCommand().endsWith("designer")) {
            mainFrame.dispose();
            isDesigner = true;
        }
    }


    private static void openClient() {
        try {
            new Client().start();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    private static void openServer() {
        try {
            new Server().start();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void openDesigner() {
        new MapDesigner().display();
    }
}
