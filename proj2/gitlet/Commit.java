package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;

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
    private final String parentId;
    private final String secondParentId;
    private final TreeMap<String, String> fileBlobs;
    private final String UID;

    public Commit(String message, Date currentTime, String parentId, String secondParentId, TreeMap<String, String> fileBlobs) {
        this.message = message;
        this.parentId = parentId;
        this.currentTime = currentTime;
        this.secondParentId = secondParentId;
        this.fileBlobs = fileBlobs;
        this.timeStamp = dateToTimeStamp(currentTime);
        this.UID = Utils.sha1(
                (message != null) ? message : "",
                timeStamp,
                (parentId != null) ? parentId : "",
                (secondParentId != null) ? secondParentId : "",
                (fileBlobs != null) ? fileBlobs.toString() : ""
        );
    }

    public String getMessage() {
        return message;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public String getParentId() {
        return parentId;
    }

    public String getSecondParentId() {
        return secondParentId;
    }

    public TreeMap<String, String> getFileBlobs() {
        return fileBlobs;
    }
    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUID() {
        return UID;
    }
    private String dateToTimeStamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

}
