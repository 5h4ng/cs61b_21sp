package gitlet;

public class Blob {
    private String id;
    private Byte[] content;

    public Blob(String id, Byte[] content) {
        this.id = id;
        this.content = content;
    }
}
