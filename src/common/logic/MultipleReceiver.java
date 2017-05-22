package common.logic;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created on 2017/05/16.
 */
public class MultipleReceiver extends Thread {
    private InfoHandler  infoHandler;
    private ServerSocket serverSocket;


    public MultipleReceiver(int openOnPort, InfoHandler infoHandler) throws IOException {
        this.infoHandler = infoHandler;
        serverSocket = new ServerSocket(openOnPort);
    }


    public void run() {
        String          info = null;
        DataInputStream dataInputStream;
        InetAddress     address = null;

        try {
            while (true) {
                Socket socket   = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                info            = dataInputStream.readUTF();
                address         = socket.getInetAddress();
                System.out.println("Received " + info + " from " + address);
                infoHandler.handleInfo(info, address);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
