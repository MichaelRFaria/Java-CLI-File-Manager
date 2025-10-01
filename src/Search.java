import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Search {
    /**
     * The method to search for a file.
     *
     * @param fileName the directory in which you want to search in.
     * @param isAbsolute whether you have inputted you are searching in the default "output" folder, or an inputted absolute file path.
     * @return Whether the file was successfully found (and any operation executed on a search result was also successfully)
     *          or not (impossible file location given).
     */
    public static boolean searchForFile(String fileName, boolean isAbsolute) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        // creating a file variable that we can work on located at the directory chosen
        File file = FileUtils.createFileVarIfExists(fileName, isAbsolute);

        // if the file variable is null, then the given directory does not exist and we cannot continue
        if (file == null) {
            return false;
        // we can only search within directories, we cannot search within an actual file
        } else if (!file.isDirectory()) {
            System.out.println("You must give a file directory to search for a file/folder in.");
            Main.delay();
            return false;
        }

        /* we create an ArrayList to add all of our matching search results
        * we create a Stack to track our traversal progress through file contents and subdirectories */
        ArrayList<File> matchingFiles = new ArrayList<>();
        ArrayDeque<File> searchStack = new ArrayDeque<>();

        // pushing the starting directory
        searchStack.push(file);

        String input;

        // we loop until we successfully find an inputted file, or the operation is cancelled by the user
        while (true) {
            System.out.println("Please input the name of the file/folder that you would like to search for (or '*' to cancel): ");
            input = Main.getScanner().nextLine().trim();

            /* when the search operation is cancelled, we return true, to return us fully out of the menu
            * if we returned false, it would prompt the user for the directory location again,
            * if entering the directory to search in via an absolute path or the directory navigator system */
            if (input.contains("*")) {
                System.out.println("Operation cancelled.");
                Main.delay();
                return true;
            }

            // loop while there are still file objects to check
            while (!searchStack.isEmpty()) {
                // we pop the stack to get a file object to search
                File current = searchStack.pop();

                /* in case the file object is a directory, we need to get the contents of the subdirectory and search through them */
                File[] subFiles = current.listFiles();

                // if the current file object has contents (therefore being a non-empty directory) then we must search it
                if (subFiles != null) {
                    // iterating through file objects within the list of directory contents
                    for (File subFile : subFiles) {
                        // if the file/folder name matches the name we are looking for add it to the list
                        if (FileUtils.getActualFileName(subFile).equals(input)) {
                            matchingFiles.add(subFile);
                        }

                        //  if the file object is a directory, then add it to the search stack
                        if (subFile.isDirectory()) {
                            searchStack.push(subFile);
                        }
                    }
                }
            }

            // returns true if files were found, hence we can then do a follow-up operation on one of those files
            if (printResults(matchingFiles, input)) {
                // provides the option for a follow-up operation on a search result
                handleSuboptions(matchingFiles);
                return true;
            }

            // else, we loop again asking for another file to search for
        }
    }

    /**
     * The method to print out matching results, assuming any were found, otherwise it notifies the user that no matching files were found.
     *
     * @param results the array of matching files that match the inputted search name.
     * @param matchingString the file name we are searching for
     * @return whether any matching results were found
     */
    public static boolean printResults(ArrayList<File> results, String matchingString) {
        String fileType;

        /* checking if the inputted search string, is a file or directory, in order to display an appropriate message
         * if the file name has a dot, then we must be handling a file and not a folder */
        if (matchingString.contains(".")) {
            fileType = "file";
        } else {
            fileType = "folder";
        }

        // if there are results, then we must print them
        if (!results.isEmpty()) {
            System.out.println("\nThe following " + fileType + "s found are located at: ");

            for (int i = 0; i < results.size(); i++) {
                /* we print each result with a number before it so it can be easily selected,
                * if the user chooses to execute a follow-up operation */
                System.out.println(i+1 + ": " + results.get(i).getAbsolutePath());
            }

            // we return true, so we know that there are results that we could execute a follow-up operation on
            return true;
        /* if no results were found, then we notify the user and return false,
        * which will cause them to be prompted to enter another file name to search */
        } else {
            System.out.println("\nNo " + fileType + "s called " + matchingString + " were found. Please try again.");
            Main.delay();
            return false;
        }
    }

    /**
     * The method print out options of operations that can be executed on a search result, and handle the user input in the menu.
     *
     * @param results the array of matching files that match the inputted search name.
     */
    public static void handleSuboptions(ArrayList<File> results) {
        while (true) {
            System.out.println("\nPlease input one of the following: ");
            System.out.println("Input '1' if you would like to open/run one of the search results."); // should not work on folders
            System.out.println("Input '2' if you would like to modify one of the search results.");
            System.out.println("Input '0' if you would like to not execute on any result and exit this menu.");
            System.out.println("\nPlease input an option: ");

            String input = Main.getScanner().nextLine().trim();

            switch (input) {
                // opening/running a search result
                case "1":
                    executeOnSearchResult("open", results);

                    // return to break out of the loop (and method) after executing on a search result
                    return;
                // modifying a search result
                case "2":
                    executeOnSearchResult("modify", results);

                    // return to break out of the loop (and method) after executing on a search result
                    return;
                // not executing any operation on a search result, and exiting the menu
                case "0":
                    return;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    Main.delay();
                    break; // loops
            }
        }
    }

    /**
     * The method to execute a given operation on one of the search results.
     *
     * @param option the operation to execute on a search result.
     * @param results the array of matching files that match the inputted search name.
     */
    public static void executeOnSearchResult(String option, ArrayList<File> results) {
        while (true) {
            printOptionMessage(option);
            String input = Main.getScanner().nextLine().trim();

            /* all inputs in this program are retrieved using nextLine() in order to prevent an input like "1 1 1" from being processed as separate tokens.
             * additionally it prevents the need of a try-catch block for InputMismatchException, if using nextInt.
             * but in this case since we are working on the value of the input, we must parse it as an int afterward */
            int choice;

            try {
                choice = Integer.parseInt(input);

            // if you enter a non-integer, it loops again, requesting another input.
            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer option. Try again.");
                Main.delay();
                continue;
            }

            // if you enter a non-positive integer, it loops again, requesting another input.
            if (choice <= 0) {
                System.out.println("Invalid search result, please input an integer between 1 and " + results.size() + ".");
                Main.delay();
                continue;
            }

            switch (option) {
                case "open":
                    // choice of file must match one of the options given AND we must not be trying to execute this on a folder
                    if (choice <= results.size() && FileUtils.getActualFileName(results.get(choice - 1)).contains(".")) {
                        Open.openFile(results.get(choice - 1).getAbsolutePath(), true);
                        return;
                    }
                    break;
                case "modify":
                    // choice of file must match one of the options given
                    if (choice <= results.size()) {
                        Modify.modifyFile(results.get(choice - 1).getAbsolutePath(), true);
                        return;
                    }
                    break;
            }
        }
    }

    /**
     * Helper method to print out the appropriate message based on the given option.
     *
     * @param option the operation you would like to execute.
     */
    public static void printOptionMessage(String option) {
        if (option.equals("open")) {
            System.out.print("Please enter the number of the result you would like to open/run: ");
        } else { // if we aren't opening a file, the only other option is to modify it
            System.out.print("Please enter the number of the result you would like to modify: ");
        }
    }
}
