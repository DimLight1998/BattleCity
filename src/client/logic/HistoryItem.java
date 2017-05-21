package client.logic;

/**
 * Created on 2017/05/17.
 */
public class HistoryItem {
    String name;
    int    score;

    public HistoryItem(String name, int score) {
        this.name = name;
        if (this.name.isEmpty()) {
            this.name = "Unnamed";
        }
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
