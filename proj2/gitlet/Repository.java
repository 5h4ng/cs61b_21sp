package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Zshang
 */
public class Repository {
    /**
     * .gitlet
     * |--objects
     * | |--commit
     * | |--blob
     * |--refs
     * | |--heads
     * | |--master
     * |--HEAD
     * |--addstage
     * |--removestage
     */
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The objects directory (contains commits and blobs). */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    /** The commits directory (contains commit objects). */
    public static final File COMMIT_DIR = join(OBJECTS_DIR, "commit");
    /** The blobs directory (contains blob objects). */
    public static final File BLOB_DIR = join(OBJECTS_DIR, "blob");
    /** The refs directory (contains references to heads). */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The heads directory (contains branches). */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    /** The master branch file. */
    public static final File MASTER_FILE = join(HEADS_DIR, "master");
    /** The staging area for added files. */
    public static final File ADD_STAGE_FILE = join(GITLET_DIR, "addstage");
    /** The staging area for removed files. */
    public static final File REMOVE_STAGE_FILE = join(GITLET_DIR, "removestage");
    /**
     * Initializes a new Gitlet repository. Creates the necessary directory
     * structure and initial files.
     *
     * @throws GitletException if the repository already exists or if an error occurs during initialization.
     */
    public void init() {
        // Check if the .gitlet directory already exists
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        } else {
            // Create .gitlet directory and subdirectories
            createDirectory(GITLET_DIR);
            createDirectory(OBJECTS_DIR);
            createDirectory(COMMIT_DIR);
            createDirectory(BLOB_DIR);
            createDirectory(REFS_DIR);
            createDirectory(HEADS_DIR);

            // Create initial files
            createFile(ADD_STAGE_FILE);
            createFile(REMOVE_STAGE_FILE);
            createFile(MASTER_FILE);

            // Create initial commit
            Commit initCommit = new Commit("initial commit", new Date(0), null, null, null);
            saveCommit(initCommit);
        }
    }

    public void saveCommit(Commit commit) {
        File commitFile = join(COMMIT_DIR, commit.getUID());
        createFile(commitFile);
        writeObject(commitFile, commit);
    }

    private void createDirectory(File dir) {
        if (!dir.mkdir()) {
            throw new GitletException("Failed to create " + dir.getName() + " directory.");
        }
    }

    private void createFile(File file) {
        try {
            if (!file.createNewFile()) {
                throw new GitletException(file.getName() + " file already exists or could not be created.");
            }
        } catch (IOException e) {
            throw new GitletException("An error occurred while creating " + file.getName() + " file.");
        }
    }
}
