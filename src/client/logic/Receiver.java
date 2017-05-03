package client.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created on 2017/05/01.
 */
public class Receiver extends Thread{
    private ServerSocket serverSocket;
    private Client observer;


    Receiver(Client observer) throws IOException {
        this.observer = observer;
        this.serverSocket = new ServerSocket(0);
    }


    int getLocalPort() {
        return serverSocket.getLocalPort();
    }


    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String info = dataInputStream.readUTF();

                observer.handleInfo(info);
                System.out.println("Received " + info + " from " + socket.getInetAddress() + ":" + socket.getPort());
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeSocket() throws IOException {
        serverSocket.close( );
    }
}
