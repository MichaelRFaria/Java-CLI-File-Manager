import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DirectoryNavigator {

    /*
    we need this method to return a boolean to indicate whether it should reopen or exit,
    but we also need this method to be able to return a string when we are navigating directories to find a directory to move a file to
    so we just make the return type String and return either "exit", or the absolute path of a directory
    */

    public static String navigateDirs(String mainOption, String path) { // sort by alphabetical, folders first, files second.
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        File navigate = new File(path);
        System.out.println(path);
        File[] directories = navigate.listFiles();
        String oldDirectoryPath;

        System.out.println(navigate.getAbsolutePath());

        int currentPage = 1;
        int viewType = 1; // 1 = both folders and files, 2 = folders, 3 = files

        updateNavigationMenu(viewType, currentPage, directories, navigate.getAbsolutePath(), mainOption);

        boolean exited = false;

        while (!exited) {
            String input = Main.getScanner().nextLine().trim();

            oldDirectoryPath = navigate.getAbsolutePath();

            switch (input) {
                // this should change depending on what you are doing (create/open etc.)
                case "$":
                    switch (mainOption) {
                        case "create", "modify":
                            Main.execOptionUntilSuccessful(true, mainOption, navigate + "\\");

                            directories = navigate.listFiles(); // update files list with the newly created file
                            updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                            break;

                        case "open":
                            Main.execOptionUntilSuccessful(true, mainOption, navigate + "\\");
                            break;
                        case "move":
                            return navigate.getAbsolutePath();
                    }
                    break;
                case "!":
                    currentPage = 1; // current page set to one to prevent empty pages from being displayed since filtering down the view changes the amount of pages.
                    viewType = (viewType + 1) % 3;
                    updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                    break;
                case "^":
                    if (navigate.getParent() != null) {
                        navigate = navigate.getParentFile();
                        directories = navigate.listFiles();
                    } else {
                        System.out.println("There is no parent directory. Please pick another option");
                    }
                    currentPage = 1;
                    updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                    break;
                case "<":
                    if (currentPage - 1 > 0) {
                        currentPage--;
                    }
                    updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                    break;
                case "<<":
                    currentPage = 1;
                    updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                    break;
                case ">":
                    if (currentPage < (directories.length + 4) / 5) {
                        currentPage++;
                    }
                    updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                    break;
                case ">>":
                    if (currentPage < (directories.length + 4) / 5) {
                        currentPage = (directories.length + 4) / 5;
                    }
                    updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                    break;
                case "~":
                    exited = true;
                    break;
                default: // inputting a folder name
                    File temp = new File(navigate.getAbsolutePath() + "\\" + input);
                    if (temp.isDirectory()) { // checking if subdirectory with the inputted name exists.
                        oldDirectoryPath = oldDirectoryPath + "\\" + input;
                        navigate = temp;
                        directories = temp.listFiles(); // updating file list with the files in the parent directory.
                        currentPage = 1;
                    } else {
                        System.out.println("There is no subdirectory with the inputted name. Please try again.");
                        Main.delay();
                    }
                    updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                    break;
            }
        }
        return null;
    }

    public static void updateNavigationMenu(int viewType, int currentPage, File[] directories, String oldDirectoryPath, String mainOption) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        String textViewType;

        // filtering to display only files, folders, or both.
        if (viewType == 1) { // sorting so folders are shown before files
            textViewType = "the folders and files";
            int i = 0;
            File[] updatedDirectories = new File[directories.length];

            // directories are already ordered alphabetically so all we need to do is add all the folders first, then files second to a new array.
            for (File file : directories) {
                if (file.isDirectory()) {
                    updatedDirectories[i] = file;
                    i++;
                }
            }

            for (File file : directories) {
                if (file.isFile()) {
                    updatedDirectories[i] = file;
                    i++;
                }
            }

            directories = updatedDirectories;
        } else {
            ArrayList<File> updatedDirectories = new ArrayList<>(Arrays.asList(directories)); // converting array into arraylist so that files can be removed
            if (viewType == 2) {
                textViewType = "the folders";
                updatedDirectories.removeIf(File::isFile);
            } else {
                textViewType = "the files";
                updatedDirectories.removeIf(File::isDirectory);
            }
            directories = updatedDirectories.toArray(new File[0]); // converting back to array to assign to directories variable

        }

    /* todo needs to be able to state the file path of empty folders
        if directory has files but not folders, vice versa, then this will be a problem
        if directory has neither files or folders, this will be a problem
     */
        String directoryPath;

        if (directories.length != 0) {
            directoryPath = directories[0].getParent();
        } else {
            directoryPath = oldDirectoryPath;
        }

        System.out.println("Currently viewing the " + textViewType + " of: " + directoryPath + "\n");

        int i = (currentPage - 1) * 5;

        while (i < (currentPage * 5) && i < directories.length) {
            System.out.println(directories[i].getName());
            i++;
        }

        int pageCount;

        if (directories.length == 0) {
            System.out.println("The current directory is empty!");
            pageCount = 1;
        } else {
            pageCount = (directories.length + 4) / 5;
        }

        System.out.println("\nPage " + currentPage + " of " + pageCount);

        System.out.println("Input a subdirectory name to enter a subdirectory, '$' to " + mainOption + " a file/folder in this directory, '!' to switch between viewing folders, files or both, " +
                "\n'^' to exit the current directory, '<','<<' and '>','>>' to navigate between pages, and '~' to exit the back to the sub menu.");

        /*
        TODO
            when opening/running, moving files,the above message should say "...'$' to " + mainOption + " a FILE in this directory..." (should not say folder)
         */
    }
}
