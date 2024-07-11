package gitlet;

// TODO: any imports you need here

import com.sun.source.tree.Tree;

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.TreeMap;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {

    private String message;
    private String timeStamp;
    private String parentId;
    private String secondParentId;
    private TreeMap<String, String> fileBlobs;


}
