import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DirectoryNavigator {
    public static void navigateDirs(String mainOption) { // sort by alphabetical, folders first, files second.
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        File navigate = new File(Main.getDefaultPath());
        File[] directories = navigate.listFiles();
        String oldDirectoryPath;

        System.out.println(navigate.getAbsolutePath());

        int currentPage = 1;
        int viewType = 1; // 1 = both folders and files, 2 = folders, 3 = files

        updateNavigationMenu(viewType, currentPage, directories, navigate.getAbsolutePath(), mainOption);

        String fileName;

        while (true) {
            String input = Main.getScanner().next();

            oldDirectoryPath = navigate.getAbsolutePath();

            switch (input) {
                // this should change depending on what you are doing (create/open/delete, etc.)
                case "$":
                    switch (mainOption) {
                        case "create":
                            fileName = navigate + "\\" + UserInput.getFileNameInput();
                            Create.createFile(fileName, true);

                            directories = navigate.listFiles(); // update files list with the newly created file
                            updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                            break;

                        case "open","modify","delete":
                            Main.execOptionUntilSuccessful(false, mainOption, navigate + "\\");
                    }
                    break; // this causes navigation menu to close after action is completed
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
                default: // inputting a folder name
                    File temp = new File(navigate.getAbsolutePath() + "\\" + input);
                    if (temp.isDirectory()) { // checking if subdirectory with the inputted name exists.
                        oldDirectoryPath = oldDirectoryPath + "\\" + input;
                        navigate = temp;
                        directories = temp.listFiles(); // updating file list with the files in the parent directory.
                        currentPage = 1;
                    } else {
                        System.out.println("There is no subdirectory with the inputted name. Please try again");
                        try {
                            Thread.sleep(1500); // thread to ensure error message displays on screen momentarily before console clears.
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    updateNavigationMenu(viewType, currentPage, directories, oldDirectoryPath, mainOption);
                    break;
            }
        }

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
                updatedDirectories.removeIf(File::isFile); // generated by IDE simplified version of commented loop below
            } else {
                textViewType = "the files";
                updatedDirectories.removeIf(File::isDirectory); // generated by IDE, simplified version of commented loop below
            }
            directories = updatedDirectories.toArray(new File[0]); // converting back to array to assign to directories variable

            /*
            for (File file : updatedDirectories) {
                if (file.isDirectory()) {
                    updatedDirectories.remove(file);
                }
            }
            */

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
                "\n'^' to exit the current directory and '<','<<' and '>','>>' to navigate between pages.");

        /*
        TODO
            when opening/running files,the above message should say "...'$' to " + mainOption + " a FILE in this directory..." (should not say folder)
         */
    }
}
