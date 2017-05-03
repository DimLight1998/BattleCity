import client.logic.Client;

import java.io.IOException;

/**
 * Created on 2017/05/01.
 */
public class OpenClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Client().start();
    }
}
