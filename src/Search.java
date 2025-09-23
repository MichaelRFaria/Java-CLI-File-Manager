import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

/*
todo
    implement exit upon successful search
    implement system to be able to select a searched file and either open or modify it
        take input, input must be between 0 and results.size(), then results.get(input), switch statement with either open or modify then pass in that file
 */

public class Search {
    public static boolean searchForFile(String fileName, boolean isAbsolute) {
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

            printResults(matchingFiles, input);
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
}
