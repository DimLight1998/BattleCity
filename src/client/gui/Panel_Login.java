package client.gui;

import client.logic.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 2017/04/30.
 */
public class Panel_Login extends JPanel implements ActionListener{
    private  JTextField text_address;
    private JTextField text_port;
    private JFrame mainFrame;

    public Panel_Login(Client client) {
        mainFrame = new JFrame("Panel_Login");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setContentPane(this);
        mainFrame.setResizable(false);
        mainFrame.setSize(300,180);

        JPanel panel_address = new JPanel(new FlowLayout());
        JPanel panel_port = new JPanel(new FlowLayout());
        JPanel panel_buttons = new JPanel(new FlowLayout());

        JLabel label_address = new JLabel("IP Address");
        JLabel label_port = new JLabel("Port Number");
        text_address = new JTextField(10);
        text_port = new JTextField(10);
        JButton button_start = new JButton("Start");
        JButton button_exit = new JButton("Exit");

        button_start.addActionListener(client);
        button_exit.addActionListener(this);

        panel_address.add(label_address);
        panel_address.add(text_address);
        panel_port.add(label_port);
        panel_port.add(text_port);
        panel_buttons.add(button_start);
        panel_buttons.add(button_exit);

        mainFrame.getContentPane().add(panel_address);
        mainFrame.getContentPane().add(panel_port);
        mainFrame.getContentPane().add(panel_buttons);

        mainFrame.setLocationRelativeTo(null);
    }

    public void display() {
        mainFrame.setVisible(true);
    }

    void dispose() {
        mainFrame.dispose();
    }

    public String getAddress() {
        return text_address.getText();
    }

    public String getPort() {
        return text_port.getText();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }
}
