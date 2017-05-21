package client.gui;

import client.logic.Client;
import client.logic.HistoryItem;
import client.logic.Recorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created on 2017/05/17.
 */
public class Panel_Save extends JPanel implements ActionListener {
    private JTextField text_name;
    private JFrame     mainFrame;
    private Recorder   recorder;
    private Client     client;

    public static void main(String[] a) {
        new Panel_Save(null, null).display();
    }

    public Panel_Save(Client client, File historyFile) {
        recorder    = new Recorder(historyFile);
        text_name   = new JTextField("Player", 20);
        this.client = client;

        mainFrame = new JFrame("Hall of Fame");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setContentPane(this);
        mainFrame.setLayout(new GridLayout(0, 1));
        mainFrame.setResizable(false);
        mainFrame.setSize(450, 700);
        mainFrame.setLocationRelativeTo(null);

        JLabel label_gameOver = new JLabel("", SwingConstants.CENTER);
        if (client.getIsWin()) {
            label_gameOver.setFont(new Font("TimesRoman", Font.BOLD, 20));
            label_gameOver.setText("You Win !");
        } else {
            label_gameOver.setFont(new Font("TimesRoman", Font.BOLD, 20));
            label_gameOver.setText("You Lose !");
        }

        JPanel[] panels_history = new JPanel[5];
        JLabel[] labels_history = new JLabel[5];

        ArrayList<HistoryItem> items = Recorder.getTopFive(recorder.getHistoryItems());

        for (int i = 0; i < 5; i++) {
            panels_history[i] = new JPanel(new FlowLayout());
            labels_history[i] = new JLabel();
            labels_history[i].setText(
                String.format("%s      %d", items.get(i).getName(), items.get(i).getScore()));
            panels_history[i].add(labels_history[i]);
        }

        JPanel form       = new JPanel(new FlowLayout());
        JLabel label_name = new JLabel("Your name is ");
        form.add(label_name);
        form.add(text_name);

        JButton button_submit = new JButton("Confirm");
        button_submit.addActionListener(this);

        this.add(label_gameOver);

        for (JPanel panel : panels_history) {
            this.add(panel);
        }

        this.add(form);
        this.add(button_submit);
    }

    public void display() {
        mainFrame.setVisible(true);
    }

    private void dispose() {
        mainFrame.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Confirm")) {
            recorder.insertItem(new HistoryItem(text_name.getText(), client.getScore()));
            dispose();

            System.exit(0);
        }
    }
}
