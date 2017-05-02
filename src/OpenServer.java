import server.logic.Server;

import java.io.IOException;

/**
 * Created on 2017/05/01.
 */
public class OpenServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Server().start();
    }
}