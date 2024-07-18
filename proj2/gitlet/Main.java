package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        Repository repo = new Repository();
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                repo.init();
                break;
            case "add":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                } else {
                    repo.add(args[1]);
                }
                break;
            case "commit":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                } else {
                    repo.commit(args[1]);
                }
                break;
            case "rm":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                } else {
                    repo.rm(args[1]);
                }
                break;
            case "log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                } else {
                    repo.log();
                }
                break;
            case "global-log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands");
                } else {
                    repo.global_log();
                }
                break;
            case "find":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                } else {
                    repo.find(args[1]);
                }
                break;
            case "status":
                if (args.length != 1) {
                    System.out.println("Incorrect operands");
                } else {
                    repo.status();
                }
                break;
            case "checkout":
                if (args.length == 3 && args[1].equals("--")) {
                    // java gitlet.Main checkout -- [file name]
                    String fileName = args[2];
                    repo.checkoutFileInHead(fileName);
                } else if (args.length == 4 && args[2].equals("--")) {
                    // java gitlet.Main checkout [commit id] -- [file name]
                    String commitId = args[1];
                    String fileName = args[3];
                    repo.checkoutFileInCommit(commitId, fileName);
                } else if (args.length == 2) {
                    // java gitlet.Main checkout [branch name]
                    String branchName = args[1];
                    repo.checkoutBranch(branchName);
                } else {
                    System.out.println("Incorrect operands.");
                }
                break;
            case "branch":
                break;
            case "rm-branch":
                break;
            case "reset":
                break;
            case "merge":
                break;

        }
    }
}
