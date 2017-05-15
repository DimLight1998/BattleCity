package common.logic;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created on 2017/05/15.
 */
public class Receiver extends Thread{
    private InfoHandler infoHandler;
    private ServerSocket serverSocket;


    public Receiver(int openOnPort, InfoHandler infoHandler) throws IOException {
        this.infoHandler = infoHandler;
        serverSocket = new ServerSocket(openOnPort);
    }


    public void run() {
        String info = null;
        DataInputStream dataInputStream;
        InetAddress address = null;

        try {
            Socket socket = serverSocket.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());

            while(true) {
                info = dataInputStream.readUTF();
                address = socket.getInetAddress();
                System.out.println("Received "+info+" from "+address);
                infoHandler.handleInfo(info,address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeSocket() throws IOException {
        serverSocket.close();
    }


    public int getLocalPort() {
        return serverSocket.getLocalPort();
    }

}
