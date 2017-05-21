package client.logic;

import java.io.*;
import java.util.*;

/**
 * Created on 2017/05/17.
 */
public class Recorder {
    private File historyFile;

    public Recorder(File file) {
        historyFile = file;
        try {
            historyFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<HistoryItem> getHistoryItems() {
        ArrayList<HistoryItem> ret = null;

        try {
            Scanner scanner = new Scanner(historyFile);

            ret = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                if (name.isEmpty()) {
                    break;
                }

                int score = Integer.parseInt(scanner.nextLine());
                ret.add(new HistoryItem(name, score));
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }


    private void saveHistoryItems(ArrayList<HistoryItem> list) {
        try {
            FileWriter writer = new FileWriter(historyFile);

            for (HistoryItem historyItem : list) {
                writer.write(historyItem.getName() + "\n");
                writer.write(Integer.toString(historyItem.getScore()) + "\n");
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<HistoryItem> getTopFive(ArrayList<HistoryItem> historyItems) {
        ArrayList<HistoryItem> ret = new ArrayList<>();

        historyItems.sort((o1, o2) -> {
            Integer score_1 = o1.getScore();
            Integer score_2 = o2.getScore();
            return score_1.compareTo(score_2);
        });

        Collections.reverse(historyItems);

        for (int i = 0; i < Integer.min(5, historyItems.size()); i++) {
            ret.add(historyItems.get(i));
        }

        int size = ret.size();
        for (int i = 0; i < 5 - size; i++) {
            ret.add(new HistoryItem("Unnamed", 0));
        }

        return ret;
    }


    public void insertItem(HistoryItem item) {
        ArrayList<HistoryItem> items = getHistoryItems();
        items.add(item);

        saveHistoryItems(items);
    }
}
