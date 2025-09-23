import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

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

            for (int i = 0; i < matchingFiles.size(); i++) {
                System.out.println(matchingFiles.get(i).getAbsolutePath());
            }

            System.out.println("done");
        }
    }
}
