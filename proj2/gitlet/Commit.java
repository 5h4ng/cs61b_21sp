package gitlet;

// TODO: any imports you need here

import com.sun.source.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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
    private final String message;
    private final Date currentTime;
    private final String timeStamp;
    private final Commit parent;
    private final Commit secondParent;
    private final TreeMap<String, String> fileBlobs;

    public Commit(String message, Date currentTime, Commit parent, Commit secondParent, TreeMap<String, String> fileBlobs) {
        this.message = message;
        this.parent = parent;
        this.currentTime = currentTime;
        this.secondParent = secondParent;
        this.fileBlobs = fileBlobs;
        this.timeStamp = dateToTimeStamp(currentTime);
    }

    public String getMessage() {
        return message;
    }

    public Date getCurrentTime() {
        return currentTime;
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
    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUID() {
        return Utils.sha1(message, timeStamp, parent, secondParent, fileBlobs.toString());
    }
    private String dateToTimeStamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

}
