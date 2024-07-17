package gitlet;

import java.io.Serializable;
import java.util.TreeMap;

public class Stage implements Serializable {
    private TreeMap<String, String> data;
    public Stage() {
        this.data = new TreeMap<>();
    }

    public Stage(TreeMap<String, String> data) {
        this.data = data;
    }

    public TreeMap<String, String> getData() {
        return data;
    }

    public void setData(TreeMap<String, String> data) {
        this.data = data;
    }
}
