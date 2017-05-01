package server.logic;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created on 2017/05/01.
 */
public class Receiver extends Thread{
    private Server observer;
    private ServerSocket serverSocket;


    Receiver(int port, Server observer) throws IOException {
        this.observer = observer;
        serverSocket = new ServerSocket(port);
    }


    public void run() {
        // 从两个不同的客户端进行监听，所以每次都要换一个套接字
        DataInputStream dataInputStream;

        while(true) {
            String info = null;

            try {
                Socket socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                info = dataInputStream.readUTF();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            observer.handleInfo(info);
        }
    }


    public void close() throws IOException {
        serverSocket.close();
    }
}
