package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.TreeMap;

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
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");

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
            createFile(HEAD_FILE);

            // Create and save initial commit
            Commit initCommit = new Commit("initial commit", new Date(0), null, null, null);
            saveCommit(initCommit);

            // Deal with the HEAD
            writeContents(HEAD_FILE, "ref: master");
            writeContents(MASTER_FILE, initCommit.getId());

            // Deal with staging area
            writeObject(ADD_STAGE_FILE, new Stage());
            writeObject(REMOVE_STAGE_FILE, new Stage());

        }
    }

    public void add(String fileName) {
        File fileToAdd = join(CWD, fileName);
        if (!fileToAdd.exists()) {
            System.out.println("File does not exist.");
        } else if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
        } else {
            // Read current commit's blobs
            Commit currentCommit = getCurrentCommit();
            TreeMap<String, String> currentBlobs = currentCommit.getFileBlobs();
            // Read the file content
            String fileContent = readContentsAsString(fileToAdd);
            // Check if the file is identical to the one in the current commit
            // If the file is identical to the current commit's version, remove it from staging area if present
            // Else, stage it for addition
            Blob blobToAdd = new Blob(fileToAdd);
            TreeMap<String, String> addStage = readObject(ADD_STAGE_FILE, Stage.class).getData();
            TreeMap<String, String> removeStage = readObject(REMOVE_STAGE_FILE, Stage.class).getData();

            if (currentBlobs.containsKey(fileName) && (Objects.equals(blobToAdd.getId(), currentBlobs.get(fileName)))) {
                addStage.remove(fileName);
                writeObject(ADD_STAGE_FILE, addStage);
            } else {
                addStage.put(fileName, blobToAdd.getId());
                saveBlob(blobToAdd);
                writeObject(ADD_STAGE_FILE, addStage);
            }
            // Deal with remove stage
            if (removeStage.containsKey(fileName)) {
                removeStage.remove(fileName);
                writeObject(REMOVE_STAGE_FILE, removeStage);
            }
        }
    }

    public void commit(String message) {
        // If no files have been staged, print messages "No change added to the commit."
        TreeMap<String, String> addStage = readObject(ADD_STAGE_FILE, Stage.class).getData();
        TreeMap<String, String> removeStage = readObject(REMOVE_STAGE_FILE, Stage.class).getData();
        Commit parentCommit = getCurrentCommit();
        if (addStage.isEmpty() && removeStage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message");
            return;
        }

        // process the new blob reference of the new commit. 1. add stage 2. remove stage
        TreeMap<String, String> newBlobs = new TreeMap<String, String>(parentCommit.getFileBlobs());
        for (String fileName : addStage.keySet()) {
            newBlobs.put(fileName, addStage.get(fileName));
        }
        for (String fileName : removeStage.keySet()) {
            newBlobs.remove(fileName);
        }
        // create a new commit containing the message, date, parent reference...
        Commit currentCommit = new Commit(message, new Date(), parentCommit.getId(), null, newBlobs);
        saveCommit(currentCommit);
        // clear the staging area
        writeObject(ADD_STAGE_FILE, new Stage());
        writeObject(REMOVE_STAGE_FILE, new Stage());
        // deal with HEAD
        String headContent = readContentsAsString(HEAD_FILE);
        if (headContent.startsWith("ref: ")) {
            String branchName = headContent.substring(5);
            File branchFile = join(HEADS_DIR, branchName);
            writeContents(branchFile, currentCommit.getId());
        } else {
            // Handle detached HEAD state if needed
            writeContents(HEAD_FILE, currentCommit.getId());
        }


    }

    private Commit getCurrentCommit() {
        String HEAD_content = readContentsAsString(HEAD_FILE);
        if (!HEAD_content.startsWith("ref: ")) {
            // TODO: Detached HEAD
            return null;
        } else {
            String headName = HEAD_content.substring(5);
            File headFile = join(HEADS_DIR, headName);
            String commitId = readContentsAsString(headFile);
            File commitFile = join(COMMIT_DIR, commitId);
            return readObject(commitFile, Commit.class);
        }
    }

    private void saveCommit(Commit commit) {
        File commitFile = join(COMMIT_DIR, commit.getId());
        createFile(commitFile);
        writeObject(commitFile, commit);
    }

    private void saveBlob(Blob blob) {
        File blobFile = join(BLOB_DIR, blob.getId());
        createFile(blobFile);
        writeObject(blobFile, blob);
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
