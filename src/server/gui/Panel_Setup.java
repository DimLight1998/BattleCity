package server.gui;

import common.logic.MapLoader;
import server.logic.Server;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2017/04/30.
 */
public class Panel_Setup extends JPanel {
    JTextField        text_port;
    JComboBox<String> combo_map;
    JFrame            mainFrame;
    JButton           button_start;


    public Panel_Setup(Server server) {
        text_port = new JTextField(10);
        combo_map = new JComboBox<>();
        for (String item : MapLoader.getMapList()) {
            combo_map.addItem(item);
        }

        JLabel label_port = new JLabel("Run server on port");
        JLabel label_map  = new JLabel("Play on map");
        button_start      = new JButton("Start");

        mainFrame = new JFrame("Settings");
        mainFrame.setContentPane(this);
        mainFrame.getContentPane().setLayout(new GridLayout(3, 1));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setSize(300, 180);

        JPanel panel_port  = new JPanel(new FlowLayout());
        JPanel panel_map   = new JPanel(new FlowLayout());
        JPanel panel_start = new JPanel(new FlowLayout());

        panel_port.add(label_port);
        panel_port.add(text_port);

        panel_map.add(label_map);
        panel_map.add(combo_map);

        panel_start.add(button_start);

        mainFrame.getContentPane().add(panel_port);
        mainFrame.getContentPane().add(panel_map);
        mainFrame.getContentPane().add(panel_start);

        button_start.addActionListener(server);

        mainFrame.setLocationRelativeTo(null);
    }

    public void display() {
        mainFrame.setVisible(true);
    }

    public void dispose() {
        mainFrame.dispose();
    }

    public int getPortNumber() {
        return Integer.valueOf(text_port.getText());
    }

    public String getMapName() {
        return combo_map.getSelectedItem().toString();
    }

    public void deactivate() {
        button_start.setText("Server is running");
        button_start.setEnabled(false);
    }
}
