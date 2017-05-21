package common.logic;

import common.item.tile.*;
import jdk.internal.util.xml.impl.Input;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

import static common.item.tile.Tile.*;

/**
 * Created on 2017/05/03.
 */
public class MapLoader {
    public MapLoader() {}


    private static void loadMap(InputStream inputStream, Tile[][] tiles)
        throws FileNotFoundException {
        Scanner mapScanner = new Scanner(inputStream);

        int rowCounter    = 0;
        int columnCounter = 0;

        while (mapScanner.hasNextInt()) {
            int read = mapScanner.nextInt();

            switch (read) {
                case PLAIN_TILE:
                    tiles[rowCounter][columnCounter] = new PlainTile(columnCounter, rowCounter);
                    break;
                case BRICK_WALL:
                    tiles[rowCounter][columnCounter] = new BrickWall(columnCounter, rowCounter);
                    break;
                case METAL_WALL:
                    tiles[rowCounter][columnCounter] = new MetalWall(columnCounter, rowCounter);
                    break;
                case METAL_TILE:
                    tiles[rowCounter][columnCounter] = new MetalTile(columnCounter, rowCounter);
                    break;
                case PLANT:
                    tiles[rowCounter][columnCounter] = new Plant(columnCounter, rowCounter);
                    break;
                case WATER:
                    tiles[rowCounter][columnCounter] = new Water(columnCounter, rowCounter);
                    break;
                case HEAD_QUARTER_LU:
                    tiles[rowCounter][columnCounter] = new HeadQuarterLU(columnCounter, rowCounter);
                    break;
                case HEAD_QUARTER_RU:
                    tiles[rowCounter][columnCounter] = new HeadQuarterRU(columnCounter, rowCounter);
                    break;
                case HEAD_QUARTER_LD:
                    tiles[rowCounter][columnCounter] = new HeadQuarterLD(columnCounter, rowCounter);
                    break;
                case HEAD_QUARTER_RD:
                    tiles[rowCounter][columnCounter] = new HeadQuarterRD(columnCounter, rowCounter);
                    break;
                default:
                    break;
            }

            columnCounter++;
            if (columnCounter == MAX_MAP_SIZE_X) {
                columnCounter = 0;
                rowCounter++;
            }
        }
    }

    public void loadMap(String mapName, Tile[][] tiles) {
        try {
            InputStream in =
                getClass().getClassLoader().getResourceAsStream("res/map/" + mapName + ".map");
            loadMap(in, tiles);
        } catch (Exception e) {
            try {
                InputStream in = new FileInputStream(mapName + ".map");
                loadMap(in, tiles);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "File doesn't exist");
            }
        }
    }

    private final static int MAX_MAP_SIZE_X = 30;
}
