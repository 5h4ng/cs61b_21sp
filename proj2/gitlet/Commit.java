package gitlet;

// TODO: any imports you need here

import com.sun.source.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.TreeMap;

import static gitlet.Utils.join;
import static gitlet.Utils.writeObject;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File COMMIT_DIR = join(CWD, ".gitlet", "objects", "commit");
    private final String message;
    private final Date timeStamp;
    private final Commit parent;
    private final Commit secondParent;
    private final TreeMap<String, String> fileBlobs;

    public Commit(String message, Date timeStamp, Commit parent, Commit secondParent, TreeMap<String, String> fileBlobs) {
        this.message = message;
        this.parent = parent;
        this.timeStamp = timeStamp;
        this.secondParent = secondParent;
        this.fileBlobs = fileBlobs;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Commit getParent() {
        return parent;
    }

    public Commit getSecondParent() {
        return secondParent;
    }

    public TreeMap<String, String> getFileBlobs() {
        return fileBlobs;
    }

    public String getUID() {
        return Utils.sha1(message, timeStamp, parent, secondParent, fileBlobs.toString());
    }


}
