package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    /**
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it for removal and remove the file from the working directory
     * if the user has not already done so (do not remove it unless it is tracked in the current commit).
     * @param fileName
     */
    public void rm(String fileName) {
        Commit currentCommit = getCurrentCommit();
        TreeMap<String, String> addStage = readObject(ADD_STAGE_FILE, Stage.class).getData();
        TreeMap<String, String> removeStage = readObject(REMOVE_STAGE_FILE, Stage.class).getData();
        boolean flag = false;

        if (addStage.containsKey(fileName)) {
            addStage.remove(fileName);
            writeObject(ADD_STAGE_FILE, addStage);
            flag = true;
        }

        if (currentCommit.getFileBlobs().containsKey(fileName)) {
            removeStage.put(fileName, currentCommit.getFileBlobs().get(fileName));
            writeObject(REMOVE_STAGE_FILE, removeStage);
            flag = true;
            File fileToRemove = join(CWD, fileName);
            if (fileToRemove.exists()) {
                restrictedDelete(fileToRemove);
            }
        }
         if(!flag) {
             System.out.println("No reason to remove the file");
         }

    }

    public void log() {
        logHelper(getCurrentCommit());
    }

    private void logHelper(Commit commit) {
        if (commit.getParentId() != null) {
            CommitPrinter(commit);
            logHelper(getCommit(commit.getParentId()));
        }
    }

    public void global_log() {
        List<String> commitFileNames = plainFilenamesIn(COMMIT_DIR);
        for (String commitFileName : commitFileNames) {
            Commit commit = readObject(join(COMMIT_DIR, commitFileName), Commit.class);
            CommitPrinter(commit);
        }
    }

    public void find(String message) {
        boolean flag = true;
        List<String> commitFileNames = plainFilenamesIn(COMMIT_DIR);
        for (String commitFileName : commitFileNames) {
            Commit commit = readObject(join(COMMIT_DIR, commitFileName), Commit.class);
            if (commit.getMessage().equals(message)) {
                System.out.println(commit.getId());
                flag = false;
            }
        }
        if (flag) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void status() {
        // Display branches
        System.out.println("=== Branches ===");
        List<String> branchNames = plainFilenamesIn(HEADS_DIR);
        Collections.sort(branchNames); // Sort branch names in lexicographical order
        String headContent = readContentsAsString(HEAD_FILE);
        String currentBranchName;
        if (headContent.startsWith("ref: ")) {
            currentBranchName = headContent.substring(5);
        } else {
            // TODO: Handle detached HEAD
            return;
        }
        for (String branchName : branchNames) {
            if (branchName.equals(currentBranchName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }
        System.out.println();

        // Display staged files
        System.out.println("=== Staged Files ===");
        Stage addStage = readObject(ADD_STAGE_FILE, Stage.class);
        List<String> stagedFiles = new ArrayList<>(addStage.getData().keySet());
        Collections.sort(stagedFiles); // Sort staged files
        for (String fileName : stagedFiles) {
            System.out.println(fileName);
        }
        System.out.println();

        // Display removed files
        System.out.println("=== Removed Files ===");
        Stage removeStage = readObject(REMOVE_STAGE_FILE, Stage.class);
        List<String> removedFiles = new ArrayList<>(removeStage.getData().keySet());
        Collections.sort(removedFiles); // Sort removed files
        for (String fileName : removedFiles) {
            System.out.println(fileName);
        }
        System.out.println();

        // Display modifications not staged for commit
        System.out.println("=== Modifications Not Staged For Commit ===");
        List<String> cwdFiles = plainFilenamesIn(CWD);
        Collections.sort(cwdFiles); // Sort current working directory files
        TreeMap<String, String> currentBlobs = getCurrentCommit().getFileBlobs();
        Set<String> modifications = new TreeSet<>(); // Use TreeSet to store modifications sorted

        for (String fileName : cwdFiles) {
            File file = join(CWD, fileName);
            if (currentBlobs.containsKey(fileName)) {
                String blobId = currentBlobs.get(fileName);
                Blob currentBlob = readObject(join(BLOB_DIR, blobId), Blob.class);
                String currentContent = readContentsAsString(file);
                if (!currentBlob.getContent().equals(currentContent)) {
                    modifications.add(fileName + " (modified)");
                }
            } else if (addStage.getData().containsKey(fileName)) {
                String stagedBlobId = addStage.getData().get(fileName);
                Blob stagedBlob = readObject(join(BLOB_DIR, stagedBlobId), Blob.class);
                String currentContent = readContentsAsString(file);
                if (!stagedBlob.getContent().equals(currentContent)) {
                    modifications.add(fileName + " (modified)");
                }
            }
        }
        for (String fileName : currentBlobs.keySet()) {
            if (!cwdFiles.contains(fileName) && !removeStage.getData().containsKey(fileName)) {
                modifications.add(fileName + " (deleted)");
            }
        }
        for (String fileName : addStage.getData().keySet()) {
            if (!cwdFiles.contains(fileName)) {
                modifications.add(fileName + " (deleted)");
            }
        }

        for (String modification : modifications) {
            System.out.println(modification);
        }
        System.out.println();

        // Display untracked files
        System.out.println("=== Untracked Files ===");
        Set<String> untrackedFiles = new TreeSet<>(); // Use TreeSet to store untracked files sorted
        for (String fileName : cwdFiles) {
            if (!currentBlobs.containsKey(fileName) && !addStage.getData().containsKey(fileName)) {
                untrackedFiles.add(fileName);
            }
        }

        for (String fileName : untrackedFiles) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println();
    }


    private void CommitPrinter(Commit commit) {
        System.out.println("===");
        // Print commit SHA-1: "commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48"
        System.out.println("commit " + commit.getId());
        // If commit has two parent commits, “Merge:” consist of the first seven digits of the first and second parents’ commit ids, in that order.
        if (commit.getSecondParentId() != null) {
            System.out.println("Merge: " + commit.getParentId().substring(0, 6) + " " + commit.getSecondParentId().substring(0, 6));
        }
        // Print Data: "Date: Thu Nov 9 17:01:33 2017 -0800"
        System.out.println("Date: " + commit.getTimeStamp());
        // Print message
        System.out.println(commit.getMessage());
        System.out.println();
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

    private Commit getCommit(String id) {
        return readObject(join(COMMIT_DIR, id), Commit.class);
    }
}
