package common.logic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created on 2017/05/01.
 */
public class Emitter {
    private Socket           localSocket;
    private DataOutputStream dataOutputStream;
    private boolean          isDisabled = false;


    public void disable() {
        isDisabled = true;
    }


    public void enable() {
        isDisabled = false;
    }


    public Emitter(InetAddress toAddress, int toPort) throws IOException {
        localSocket      = new Socket(toAddress, toPort);
        dataOutputStream = new DataOutputStream(localSocket.getOutputStream());
    }


    public void emit(String info) {
        synchronized (this) {
            emit(info, false);
        }
    }


    public void emit(String info, boolean override) {
        synchronized (this) {
            if ((!isDisabled) || override) {
                try {
                    dataOutputStream.writeUTF(info);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void closeSocket() {
        try {
            localSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
