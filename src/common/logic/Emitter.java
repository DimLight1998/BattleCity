package common.logic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created on 2017/05/01.
 */
public class Emitter{
    private Socket socket;


    public Emitter(InetAddress address, int port) throws IOException {
        socket = new Socket(address,port);
    }


    public void emit(String info) {
        new EmitterThread(socket,info).start();
    }
}

class EmitterThread extends Thread {
    Socket socket;
    String info;

    EmitterThread(Socket socket,String info) {
        this.socket = socket;
        this.info = info;
    }

    public void run() {
        DataOutputStream dataOutputStream = null;

        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Emitting "+ info + " to "+socket.getInetAddress()+":"+socket.getPort());

        try {
            dataOutputStream.writeUTF(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
