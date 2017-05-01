package server.logic;

import common.item.bullet.Bullet;
import common.item.tank.Tank;
import common.item.tile.Tile;
import common.logic.Emitter;

import java.util.ArrayList;

/**
 * Created on 2017/05/01.
 */
public class Server {
    Tile[][] tiles;
    ArrayList<Tank> tanks;
    ArrayList<Bullet> bullets;
    Emitter emitter;
    Receiver receiver;

    Server() {
        tiles = new Tile[30][30];
        tanks = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    void handleInfo(String info) {
        // TODO
    }
}
