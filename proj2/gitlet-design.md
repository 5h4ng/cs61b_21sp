# Gitlet Design Document

**zshang**

## 1. Classes and Data Structures

### 1.1. Repository

#### Description
The `Repository` class is the core of Gitlet, managing instances of all other classes and implementing the main version control functionalities (e.g., commit, checkout, branch). It contains global state information and operations.

#### Fields
- `TreeMap<String, Commit> commits`: Stores all commit objects, with the commit hash as the key.
- `TreeMap<String, String> branches`: Stores all branch names and their corresponding latest commit hashes.
- `String head`: The name of the current branch.
- `Set<String> addStage`: Staging area, storing file paths to be added.
- `Set<String> removeStage`: Removal area, storing file paths to be removed.

#### Methods
- `void init()`: Initializes a new Gitlet repository.
- `void add(String fileName)`: Adds a file to the staging area.
- `void commit(String message)`: Creates a new commit.
- `void remove(String fileName)`: Removes a file from the staging area or version control.
- `void checkout(String commitId, String fileName)`: Restores a file from the specified commit.
- `void branch(String branchName)`: Creates a new branch.
- `void merge(String branchName)`: Merges the specified branch.

### 1.2. Commit

#### Description
The `Commit` class represents a single commit, including the commit message, timestamp, parent commit, and the file blobs referenced by their hashes.

#### Fields
- `String message`: The commit message.
- `Date timestamp`: The commit timestamp.
- `String parent`: The hash of the parent commit.
- `String secondParent`: The hash of the second parent commit (for merge commits).
- `Map<String, String> fileBlobs`: A map where keys are file names and values are blob hashes.

#### Methods
- `Commit(String message, String parent, String secondParent, Map<String, String> fileBlobs)`: Constructor.
- `String getId()`: Gets the hash of the commit.
- `String getMessage()`: Gets the commit message.
- `Date getTimestamp()`: Gets the commit timestamp.
- `String getParent()`: Gets the parent commit hash.
- `String getSecondParent()`: Gets the second parent commit hash.
- `Map<String, String> getFileBlobs()`: Gets the file blobs map.

### 1.3. Blob

#### Description
The `Blob` class represents a snapshot of a file's content, allowing the file to be restored in different commits.

#### Fields
- `String id`: The hash of the blob.
- `byte[] content`: The content of the file.

#### Methods
- `Blob(String filePath)`: Constructor, creates a Blob from a file path.
- `String getId()`: Gets the hash of the blob.
- `byte[] getContent()`: Gets the content of the blob.

## 2. Persistence

### Directory Structure
```plaintext
.gitlet
|--objects
|   |--commits (stores commit objects)
|   |--blobs (stores blob objects)
|--refs
|   |--heads (stores the latest commit of each branch)
|--HEAD (stores the name of the current branch)
|--addstage (staging area file list)
|--removestage (removal area file list)
```

#### Description
The persistence structure is used to save all necessary information of the repository, allowing it to be restored after the program restarts.

- `objects/commits`: Files named by commit hashes, storing the serialized data of `Commit` objects.
- `objects/blobs`: Files named by blob hashes, storing the content of `Blob` objects.
- `refs/heads`: Stores the latest commit hash of each branch.
- `HEAD`: Stores the name of the current branch.
- `addstage`: Stores the paths of files in the staging area in text format, one file per line.
- `removestage`: Stores the paths of files in the removal area in text format, one file per line.

#### Example Files

- `objects/commits/abc123`: Stores the commit object with hash `abc123`.
- `objects/blobs/def456`: Stores the file content with hash `def456`.
- `refs/heads/master`: Stores the latest commit hash of the master branch.
- `HEAD`: Content is `master`, indicating the current branch is master.
- `addstage`: Stores file paths in the staging area, one per line.
- `removestage`: Stores file paths in the removal area, one per line.