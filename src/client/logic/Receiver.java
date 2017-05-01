package client.logic;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created on 2017/05/01.
 */
public class Receiver extends Thread{
    private ServerSocket serverSocket;
    private Client observer;


    Receiver(int port,Client observer) throws IOException {
        this.observer = observer;
        this.serverSocket = new ServerSocket(port);
    }


    public void run() {
        // 只用从一个服务端进行监听，所以不需要每次更新套接字
        Socket socket = null;
        DataInputStream dataInputStream = null;

        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
             dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            String info = null;
            try {
                info = dataInputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            observer.handleInfo(info);
        }
    }

    void closeSocket() throws IOException {
        serverSocket.close( );
    }
}
