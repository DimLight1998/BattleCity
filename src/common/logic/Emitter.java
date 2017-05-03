package common.logic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created on 2017/05/01.
 */
public class Emitter{
    InetAddress address;
    int port;


    public Emitter(InetAddress address, int port) throws IOException {
        this.address = address;
        this.port = port;
    }


    public void emit(String info) {

        try {
            new EmitterThread(address,port,info).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class EmitterThread extends Thread {
    Socket socket;
    String info;

    EmitterThread(InetAddress address,int port,String info) throws IOException {
        this.socket = new Socket(address,port);
        this.info = info;
    }

    public void run() {
        DataOutputStream dataOutputStream = null;

        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Emitting "+ info + " to "+socket.getInetAddress()+":"+socket.getPort());
            dataOutputStream.writeUTF(info);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
