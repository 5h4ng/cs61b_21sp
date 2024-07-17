package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private String id;
    private byte[] content;

    public Blob(File file) {
        this.content = Utils.readContents(file);
        this.id = Utils.sha1(content);
    }

    public String getId() {
        return id;
    }

    public byte[] getContent() {
        return content;
    }
}
