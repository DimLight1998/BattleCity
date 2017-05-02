package server.gui;

import server.logic.Server;

import javax.swing.*;

/**
 * Created on 2017/04/30.
 */
public class Panel_Setup extends JPanel{
    JTextField text_port;
    JFrame mainFrame;


    public Panel_Setup(Server server) {
        text_port = new JTextField(10);
        JLabel label_port  = new JLabel("Run server on port");
        JButton button_start = new JButton("Start");

        mainFrame = new JFrame("Setting");
        mainFrame.setContentPane(this);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setSize(300,180);

        mainFrame.getContentPane().add(label_port);
        mainFrame.getContentPane().add(text_port);
        mainFrame.getContentPane().add(button_start);

        button_start.addActionListener(server);
    }

    public void display() {
        mainFrame.setVisible(true);
    }

    public void dispose() {
        mainFrame.dispose();
    }

    public int getPortNumber() {
        return Integer.valueOf( text_port.getText());
    }

}
