import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

/*
todo
    implement system to be able to select a searched file and either open or modify it
        take input, input must be between 0 and results.size(), then results.get(input), switch statement with either open or modify then pass in that file
    implement the file navigation method
    the default file path method should just run in output, it should not request for the name of a folder
 */

public class Search {
    public static boolean searchForFile(String fileName, boolean isAbsolute) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        File file = FileUtils.createFileVarIfExists(fileName, isAbsolute);
        if (file == null) {
            return false;
        } else if (!file.isDirectory()) {
            System.out.println("You must give a file directory to search for a file/folder in.");
            Main.delay();
            return false;
        }

        ArrayList<File> matchingFiles = new ArrayList<>();
        ArrayDeque<File> searchStack = new ArrayDeque<>();
        searchStack.push(file); // pushing the starting directory

        String input;

        while (true) {
            System.out.println("Please input the name of the file/folder that you would like to search for:");
            input = Main.getScanner().nextLine().trim();

            while (!searchStack.isEmpty()) {
                File current = searchStack.pop(); // either file or folder

                File[] subFiles = current.listFiles();

                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        if (FileUtils.getActualFileName(subFile).equals(input)) { // if the file/folder name matches the name we are looking for add it to the list
                            matchingFiles.add(subFile);
                        }

                        if (subFile.isDirectory()) { //  if the file var is a folder, then add it to the search stack
                            searchStack.push(subFile);
                        }
                    }
                }
            }

            if (printResults(matchingFiles, input)) { // returns true if files were found, hence we can then do a follow-up operation on one of those files
                handleSuboptions(matchingFiles);
               return true;
            }
        }
    }

    public static boolean printResults(ArrayList<File> results, String matchingString) {
        String fileType;
        if (matchingString.contains(".")) {
            fileType = "file";
        } else {
            fileType = "folder";
        }

        if (results.isEmpty()) {
            System.out.println("No " + fileType + "s called " + matchingString + " were found. Please try again.");
            Main.delay();
            return false;
        } else {
            System.out.println("The following " + fileType + "s found are located at: ");
            for (int i = 0; i < results.size(); i++) {
                System.out.println(i+1 + ": " + results.get(i).getAbsolutePath());
            }
            return true;
        }
    }

    public static boolean handleSuboptions(ArrayList<File> results) {
        while (true) {
            System.out.println("\nPlease input one of the following: ");
            System.out.println("Input '1' if you would like to open/run one of the search results."); // should not work on folders
            System.out.println("Input '2' if you would like to modify one of the search results.");
            System.out.println("Input '0' if you would like to not execute on any result and exit this menu.");
            System.out.println("\nPlease input an option: ");

            String input = Main.getScanner().nextLine().trim();

            switch (input) {
                case "1":
                    return(executeOnSearchResult("open", results)); // return to break out of the loop after executing on a search result
                case "2":
                    return(executeOnSearchResult("modify", results)); // return to break out of the loop after executing on a search result
                case "0":
                    return true;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    Main.delay();
                    break; // loops
            }
        }
    }

    public static boolean executeOnSearchResult(String option, ArrayList<File> results) {
        while (true) {
            printOptionMessage(option);
            String input = Main.getScanner().nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) { // if you enter a non-integer, it loops again, requesting another input.
                System.out.println("Please enter an integer option. Try again.");
                Main.delay();
                continue;
            }

            if (choice <= 0) { // if you enter a non-positive integer, it loops again, requesting another input.
                System.out.println("Invalid search result, please input an integer between 1 and " + results.size() + ".");
                Main.delay();
                continue;
            }

            switch (option) {
                case "open": // choice of file must match one of the options given AND we must not be trying to execute this on a folder
                    if (choice <= results.size() && FileUtils.getActualFileName(results.get(choice - 1)).contains(".")) {
                        return (Open.openFile(results.get(choice - 1).getAbsolutePath(), true));
                    }
                    break;
                case "modify": // choice of file must match one of the options given
                    if (choice <= results.size()) { // choice of file must match one of the options given
                        return (Modify.modifyFile(results.get(choice - 1).getAbsolutePath(), true));
                    }
                    break;
            }
        }
    }

    public static void printOptionMessage(String option) {
        if (option.equals("open")) {
            System.out.print("Please enter the number of the result you would like to open/run: ");
        } else { // if we aren't opening a file, the only other option is to modify it
            System.out.print("Please enter the number of the result you would like to modify: ");
        }
    }
}
