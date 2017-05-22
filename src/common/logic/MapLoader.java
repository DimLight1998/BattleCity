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


    private static int[] loadMap(InputStream inputStream, Tile[][] tiles)
        throws FileNotFoundException {
        int[] content = getInts(inputStream);
        loadMap(content,tiles);
        return content;
    }

    private static int[] getInts(InputStream inputStream) {
        int[] ret = new int[900];
        int counter = 0;

        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNextInt()){
            ret[counter] = scanner.nextInt();
            counter++;
        }

        return ret;
    }

    public static String intArrToString(int[] content) {
        StringBuilder ret = new StringBuilder();
        for(int i : content) {
            ret.append(Integer.toString(i));
        }

        return ret.toString();
    }

    public static int[] stringToIntArr(String text) {
        int[] ret = new int[900];

        for(int i = 0;i<900;i++) {
            ret[i] = Character.getNumericValue(text.charAt(i));
        }

        return ret;
    }
    
    private static Tile getTileByIndex(int index, int column, int row) {
        switch (index) {
            case PLAIN_TILE:
                return new PlainTile(column, row);
            case BRICK_WALL:
                return new BrickWall(column, row);
            case METAL_WALL:
                return new MetalWall(column, row);
            case METAL_TILE:
                return new MetalTile(column, row);
            case PLANT:
                return new Plant(column, row);
            case WATER:
                return new Water(column, row);
            case HEAD_QUARTER_LU:
                return new HeadQuarterLU(column, row);
            case HEAD_QUARTER_RU:
                return new HeadQuarterRU(column, row);
            case HEAD_QUARTER_LD:
                return new HeadQuarterLD(column, row);
            case HEAD_QUARTER_RD:
                return new HeadQuarterRD(column, row);
            default:
                return new PlainTile(column,row);
        }
    }

    public int[] loadMap(String mapName, Tile[][] tiles) {
        int[] ret = new int[900];

        try {
            InputStream in =
                getClass().getClassLoader().getResourceAsStream("res/map/" + mapName + ".map");
            ret = loadMap(in, tiles);
        } catch (Exception e) {
            try {
                InputStream in = new FileInputStream(mapName + ".map");
                ret = loadMap(in, tiles);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "File doesn't exist");
            }
        }

        return ret;
    }
    
    public static void loadMap(int[] mapContent,Tile[][] tiles) {
        int rowCounter    = 0;
        int columnCounter = 0;

        while(true) {
            int read = mapContent[30*rowCounter+columnCounter];

            tiles[rowCounter][columnCounter] = getTileByIndex(read,columnCounter,rowCounter);

            columnCounter++;
            if (columnCounter == MAX_MAP_SIZE_X) {
                columnCounter = 0;
                rowCounter++;

                if(rowCounter == MAX_MAP_SIZE_X) {
                    break;
                }
            }
        }
    }

    private final static int MAX_MAP_SIZE_X = 30;
}
