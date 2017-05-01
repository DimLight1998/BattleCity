package common.logic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created on 2017/05/01.
 */
public class Emitter extends Thread{
    private Socket socket;
    String info;


    Emitter(InetAddress address,int port,String info) throws IOException {
        socket = new Socket(address,port);
        this.info = info;
    }


    public void run() {
        DataOutputStream dataOutputStream = null;

        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dataOutputStream.writeUTF(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
