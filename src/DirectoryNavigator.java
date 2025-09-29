import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DirectoryNavigator {
        /* TODO - could add option to increase and reduce the number of pages that are shown at a time,
            displayedFilesCount = some default input, then prompt the user for an input on how many files to show
            will need to update appropriate contents too */

    /**
     * This method allows you to navigate directories on your system via the console, in order to easily find a location to execute one of the program's operations.
     *
     * @param mainOption the name of the operation we are executing.
     * @param path the absolute file path location of the directory that you want to start navigating from.
     * @return null by default, unless {@code mainOption} is "move", where it will return the absolute file path of the directory currently shown.
     */
    public static String navigateDirs(String mainOption, String path) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        // creating the File object with the starting directory to display
        File navigate = new File(path);
        //System.out.println(path);
        File[] directoryContents = navigate.listFiles();

        //System.out.println(navigate.getAbsolutePath());

        // variables to handle which files are currently being displayed
        int currentPage = 1;
        // viewType acts as a filter for what directory contents are shown: 1 = both folders and files, 2 = folders, 3 = files
        int viewType = 1;

        /* loops until either a move operation is complete, where it will return the directory location to move the file to as a string to be used in Modify.moveFile,
        * or until you select the exit option, where the method returns null */
        while (true) {
            /* we update the menu after any of the options are executed as we need to display new files when creating and renaming,
             * and when changing pages, filtering the types of files shown, etc */
            updateNavigationMenu(viewType, currentPage, directoryContents, navigate.getAbsolutePath(), mainOption);

            String input = Main.getScanner().nextLine().trim();

            switch (input) {
                // executing the main option in the currently shown directory
                case "$":
                    switch (mainOption) {
                        /* for the main options we will execute the option until successful, and then update our directoryContents array with the new contents of the directory .
                        * specifically creating a file, and modifying a file (by either renaming or deleting a file) require us to refresh the directoryContents array */
                        case "create", "open", "modify", "search":
                            Main.execOptionUntilSuccessful(true, mainOption, navigate + "\\");

                            directoryContents = navigate.listFiles();
                            break;
                        /* Modify.moveFile calls this method with the "move" mainOption parameter, in this case, we are calling this method
                        * to obtain a destination, to move our file to, therefore we return the currently shown directory's absolute file path */
                        case "move":
                            return navigate.getAbsolutePath();
                    }
                    break;
                // changing the filter on what type of files are shown (just folders, just files, or both)
                case "!":
                    /* we reset currentPage to 1, when filtering out results, not just for convenience for the user, but also as the current page number,
                    * may exceed the new total number of pages */
                    currentPage = 1;
                    viewType = (viewType + 1) % 3;
                    break;
                // exiting the current subdirectory
                case "^":
                    // if the current directory has a parent, then we can set the current directory to that parent, and update our directoryContents array
                    if (navigate.getParent() != null) {
                        navigate = navigate.getParentFile();
                        directoryContents = navigate.listFiles();
                    // otherwise we cannot exit the current subdirectory (this case only happens at the root directory)
                    } else {
                        System.out.println("There is no parent directory. Please pick another option");
                    }
                    /* we reset currentPage to 1, when exiting directories, not just for convenience for the user, but also as the current page number,
                     * may exceed the new total number of pages */
                    currentPage = 1;
                    break;
                // display previous page
                case "<":
                    /* if the previous page will be larger than 0, then reduce the current page number by 1.
                    * this could also be replaced with if page is not 1, but I believe this is more robust */
                    if (currentPage - 1 > 0) {
                        currentPage--;
                    }
                    break;
                // display first page
                case "<<":
                    currentPage = 1;
                    break;
                // display next page
                case ">":
                    /* ceiling division to ensure the current page does not exceed the maximum number of pages
                    * that can be formed given the number of directoryContents.
                    * e.g: we have 16 files, and show 5 files per page.
                    * 16 / 5 = 3.2 pages, rounded up to 4 pages (as we can display the remainder of the files on an extra page) */
                    if (currentPage < Math.ceil((double) directoryContents.length / 5)) {
                        currentPage++;
                    }
                    break;
                // display last page
                case ">>":
                    /* ceiling division to ensure the current page does not exceed the maximum number of pages
                    * that can be formed given the number of directoryContents. */
                    if (currentPage < Math.ceil((double) directoryContents.length / 5)) {
                        currentPage = (int) Math.ceil((double) directoryContents.length / 5);
                    }
                    break;
                // exiting the directory navigator
                case "~":
                    return "exit";
                /* inputting a subdirectory name
                * we assume an input that does not match any case, is the name of a subdirectory we want to enter */
                default:
                    // creating a temporary File object to check if a directory with the inputted name exists
                    File temp = new File(navigate.getAbsolutePath() + "\\" + input);
                    // if the file with the inputted name exists, and is a directory, we update our variables appropriately to the new directory
                    if (temp.isDirectory()) {
                        navigate = temp;
                        directoryContents = temp.listFiles();
                        currentPage = 1;
                    // otherwise, we inform the user that no subdirectory was found
                    } else {
                        System.out.println("There is no subdirectory with the inputted name. Please try again.");
                        Main.delay();
                    }
                    break;
            }
        }
    }

    /**
     * This method displays the navigation menu, showing a single page of the contents of a given directory.
     *
     * @param viewType the filter of what files to display.
     * @param currentPage the number of the current page to be displayed.
     * @param directoryContents the array with all the directory's contents, that will be displayed.
     * @param currDirPath the absolute file path of the directory that will be displayed.
     * @param mainOption the name of the operation we are executing in the directory.
     */
    public static void updateNavigationMenu(int viewType, int currentPage, File[] directoryContents, String currDirPath, String mainOption) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        String textViewType;

        // filtering the contents of the directory shown and figuring out what the view type is so we can display it (textViewType)

        // displaying both files and folders.
        /* this is what is shown by default, so we don't actually need to do anything,
        * but we display the contents in alphabetical order with folders shown before files for convenience */
        if (viewType == 1) {
            textViewType = "folders and files";
            int i = 0;
            File[] updatedDirectoryContents = new File[directoryContents.length];

            // the directoryContents array is already ordered alphabetically, so all we need to do is add the folders to a new array in order, then add the files to the new array in order
            // adding the folders to the new array
            for (File file : directoryContents) {
                if (file.isDirectory()) {
                    updatedDirectoryContents[i] = file;
                    i++;
                }
            }

            // adding the files to the new array
            for (File file : directoryContents) {
                if (file.isFile()) {
                    updatedDirectoryContents[i] = file;
                    i++;
                }
            }

            directoryContents = updatedDirectoryContents;
        } else {
            // converting array into ArrayList so that folders/files can be removed
            ArrayList<File> updatedDirectoryContents = new ArrayList<>(Arrays.asList(directoryContents));

            // displaying only folders
            if (viewType == 2) {
                textViewType = "folders";
                // removing all the files so only folders remain
                updatedDirectoryContents.removeIf(File::isFile);
            // displaying only files
            } else {
                textViewType = "files";
                // removing all the folders so only files remain
                updatedDirectoryContents.removeIf(File::isDirectory);
            }

            // converting back to array to reassign the new array to the directoryContents variable
            directoryContents = updatedDirectoryContents.toArray(new File[0]);

        }

        // figuring out the directory path so we can display it
        String directoryPath;

        if (directoryContents.length != 0) {
            directoryPath = directoryContents[0].getParent();
        } else {
            directoryPath = currDirPath;
        }

        System.out.println("Currently viewing the " + textViewType + " of: " + directoryPath + "\n");

        // displaying only the contents for the current page
        int i = (currentPage - 1) * 5;

        while (i < (currentPage * 5) && i < directoryContents.length) {
            System.out.println(directoryContents[i].getName());
            i++;
        }

        // figuring out the total number of pages for the directory
        int pageCount;

        // if the directory is empty, we must indicate this to the user via an appropriate message
        if (directoryContents.length == 0) {
            System.out.println("The current directory has no " + textViewType + "!");
            pageCount = 1;
        } else {
            pageCount = (int) Math.ceil((double) directoryContents.length / 5);
        }

        System.out.println("\nPage " + currentPage + " of " + pageCount);

        // printing the menu controls, along with the correct verb based on what we are attempting to execute
        System.out.println("Input a subdirectory name to enter a subdirectory, '$' to " + mainOption + " a file/folder in this directory, '!' to switch between viewing folders, files or both, " +
                "\n'^' to exit the current directory, '<','<<' and '>','>>' to navigate between pages, and '~' to exit the back to the sub menu.");

        /*
        TODO
            when opening/running, moving files,the above message should say "...'$' to " + mainOption + " a FILE in this directory..." (should not say folder)
         */
    }
}
