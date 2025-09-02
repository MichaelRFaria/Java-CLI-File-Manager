import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static String defaultPath;
    private static final  String[] mainMenuText = {"Welcome to the Command Line File Handler", "This program allows you to perform several operations on files and folders",
            "Including: creating, reading/running, modifying, and deleting files/folders\n","Please input one of the following:",
            "Input '1' if you would like to create a new file/folder", "Input '2' if you would like to read/run from an existing file/folder",
            "Input '3' if you would like to modify an existing file/folder", "Input '4' if you would like to delete an existing file/folder",
            "Input '0' if you would like to exit\n", "Please input an option: "};


    /*
    TODO:
        expand all of the below to include folders based on context (ie. files end with suffixes(.txt/.exe,etc), folders dont)
        implement default file path variable for creation (DONE), reading, modification, and deleting (create data folder)
        implement folder navigating (DONE)
        (above could lead to a file searching option)
            maybe allow user to configure how many files they would like displayed on the screen at a time
        implement input of absolute file path and relative file path (method to take input and check if its valid:
            file name (cant have several '.' illegal characters etc)
            for absolute, file path must exist, maybe give the option to create folders if the directory doesnt exist?)
        implement exit option for each option and suboption
            change while loop condition to a boolean variable, then set boolean to true by default, and false when a switch-case or if-conditional triggers it
            after default and absolute, it should auto go back to suboption menu

    */


    public static void main(String[] args) {
        File file = new File("");
        defaultPath = file.getAbsolutePath() + "\\output\\"; // default path for files

        mainMenu();
    }

    public static void mainMenu() {
        for (String line : mainMenuText) {
            System.out.println(line);
        }

        while (true) {
            String choice = scanner.next();

            switch (choice) {
                case "1":
                    subOptionMenu("create");
                    break;
                case "2":
                    subOptionMenu("open");
                    break;
                case "3":
                    subOptionMenu("modify");
                    break;
                case "4":
                    subOptionMenu("delete");
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please try again");
            }
        }
    }

    public static void subOptionMenu(String mainOption) {
        String fileName = null;
        boolean isAbsolute = false;

        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        System.out.println("Please input one of the following: ");
        System.out.println("Input '1' if you would like to " + mainOption + " a file in the program's default directory");
        System.out.println("Input '2' if you would like to " + mainOption + " a file in an inputted absolute file path");
        System.out.println("Input '3' if you would like to navigate directories to " + mainOption + " a file");
        System.out.println("Input '0' if you would like to exit\n");
        System.out.println("Please input an option: ");

        while (true) {
            try {
                int num = scanner.nextInt();
                switch (num) {
                    case 1:
                        fileName = getFileNameInput();
                        break;
                    case 2:
                        fileName = getAbsolutePathInput();
                        isAbsolute = true;
                        break;
                    case 3:
                        navigateDirs(mainOption);
                        break;
                    case 0:
                        reprintMainMenuOptions();
                        break;
                    default:
                        System.out.println("Please input an option from the list above, try again: ");
                        continue;
                }

                if (fileName != null) { // fileName will be null if you choose to exit from the submenu, in that case we should not (and cannot) execute any of these.
                    switch (mainOption) {
                        case "create":
                            createFile(fileName, isAbsolute);
                            break;
                        case "open":
                            openFile(fileName, isAbsolute);
                            break;
                        case "modify":
                            modifyFile(fileName, isAbsolute);
                            break;
                        case "delete":
                            deleteFile(fileName, isAbsolute);
                            break;
                        default:
                    }
                }

            } catch (InputMismatchException e) {
                System.out.println("Please input a valid option, try again: ");
                scanner.next();
            }
        }
    }

    // both methods below can be merged into one, but it will lead to nested if-statements within the loop, and a param
    /*
    TODO
        implement actual name/path checking rules. scanner blocks on empty input, so cant if-else print an error in the while loops, for that
        but could still check for illegal characters etc
     */
    public static String getFileNameInput() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        System.out.println("Please input a valid file/folder name: ");

        while (true) {
            String input = scanner.next();
            if (!input.isEmpty()) { // name checking rules go here
                return input;
            }
        }
    }

    public static String getAbsolutePathInput() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        System.out.println("Please input a valid absolute path: ");

        while (true) {
            String input = scanner.next();
            File temp = new File(input);
            if (temp.isAbsolute()) { // path checking rules go here
                return input;
            }
        }
    }

    /*
    TODO
        when creating a file in default/absolute go back to sub menu after a few seconds
     */

    public static void createFile(String fileName, boolean isAbsolute) {
        String path = "";

        if (!isAbsolute) {
            path = defaultPath;
        }

        System.out.println("creating file/folder");

        try {
            boolean created;
            File file = new File(path + fileName);

            if ((path + fileName).indexOf('.') == -1) { // if there is no '.' then we must be creating a folder
                created = file.mkdir();
            } else { // if there is a ',' then we must be creating a file
                created = file.createNewFile();
            }

            // printing success/error message
            if (created) {
                System.out.println("successfully created " + file.getName());
            } else {
                System.out.println(file.getName() + " already exists");
            }

            // ensuring there is enough time for the above message to be read
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("An error occurred:");
                System.out.println(e.getMessage()+"\n");
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("An error occurred:");
            System.out.println(e.getMessage()+"\n");
            e.printStackTrace();
        }
    }


    public static void navigateDirs(String mainOption) { // sort by alphabetical, folders first, files second.
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        File navigate = new File(defaultPath);
        File[] directories = navigate.listFiles();

        System.out.println(navigate.getAbsolutePath());

        int currentPage = 1;
        int viewType = 1; // 1 = both folders and files, 2 = folders, 3 = files

        updateNavigationMenu(viewType, currentPage, directories, mainOption);

        while (true) {
            String input = scanner.next();

            switch (input) {
                // this should change depending on what you are doing (create/open/delete, etc)
                case "$":
                    switch (mainOption) {
                        case "create":
                            String fileName1 = navigate + "\\" + getFileNameInput();
                            createFile(fileName1, true);

                            directories = navigate.listFiles(); // update files list with the newly created file
                            updateNavigationMenu(viewType, currentPage, directories, mainOption);
                            break;

                            /*
                            TODO
                                add navigation method of opening/running, modifying and deleting files/folders
                             */
                        case "open":
                            String fileName2 = navigate + "\\" + getFileNameInput();
                            openFile(fileName2, true);

                            break;
                        case "modify":
                            break;
                        case "delete":
                            break;
                    }
                case "!":
                    currentPage = 1; // current page set to one to prevent empty pages from being displayed since filtering down the view changes the amount of pages.
                    viewType = (viewType + 1) % 3;
                    updateNavigationMenu(viewType, currentPage, directories, mainOption);
                    break;
                case "^":
                    if (navigate.getParent() != null) {
                        navigate = navigate.getParentFile();
                        directories = navigate.listFiles();
                    } else {
                        System.out.println("There is no parent directory. Please pick another option");
                    }
                    currentPage = 1;
                    updateNavigationMenu(viewType, currentPage, directories, mainOption);
                    break;
                case "<":
                    if (currentPage - 1 > 0) {
                        currentPage--;
                    }
                    updateNavigationMenu(viewType, currentPage, directories, mainOption);
                    break;
                case "<<":
                    currentPage = 1;
                    updateNavigationMenu(viewType, currentPage, directories, mainOption);
                    break;
                case ">":
                    if (currentPage < (directories.length + 4) / 5) {
                        currentPage++;
                    }
                    updateNavigationMenu(viewType, currentPage, directories, mainOption);
                    break;
                case ">>":
                    if (currentPage < (directories.length + 4) / 5) {
                        currentPage = (directories.length + 4) / 5;
                    }
                    updateNavigationMenu(viewType, currentPage, directories, mainOption);
                    break;
                default: // inputting a folder name
                    File temp = new File(navigate.getAbsolutePath() + "\\" + input);
                    if (temp.isDirectory()) { // checking if subdirectory with the inputted name exists.
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
                    updateNavigationMenu(viewType, currentPage, directories, mainOption);
                    break;
            }
        }

    }

    public static void updateNavigationMenu(int viewType, int currentPage, File[] directories, String mainOption) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        String textViewType;

        // filtering to display only files, folders, or both.
        if (viewType == 1) { // sorting so folders are shown before files
            textViewType = "the folders and files";
            int i = 0;
            File[] updatedDirectories = new File[directories.length];

            // directories is already ordered alphabetically so all we need to do is add all the folders first, then files second to a new array.
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


        System.out.println("Currently viewing the " + textViewType + " of: " + directories[0].getParent() + "\n");

        int i = (currentPage - 1) * 5;

        while (i < (currentPage * 5) && i < directories.length) {
            System.out.println(directories[i].getName());
            i++;
        }

        System.out.println("\nPage " + currentPage + " of " + (directories.length + 4) / 5);

        System.out.println("Input a subdirectory name to enter a subdirectory, '$' to " + mainOption + " a file/folder in this directory, '!' to switch between viewing folders, files or both, " +
                "\n'^' to exit the current directory and '<','<<' and '>','>>' to navigate between pages.");
    }

    /*
    TODO
        allow reading of text files (DONE), running of exes, etc
        return to suboption after typing exit or something
     */

    public static void openFile(String fileName, boolean isAbsolute) {
        String path = "";

        if (!isAbsolute) {
            path = defaultPath;
        }

        System.out.println("reading file");

        System.out.println("Printing the contents of: " + path + fileName + "\n");

        try {
            File textFile = new File(path + fileName);
            Scanner reader = new Scanner(textFile);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                System.out.println(line);
            }

            System.out.println("\nEnd of file...");
        } catch (FileNotFoundException e) {
            System.out.println("an error occured when reading the file");
            e.printStackTrace();
        }
    }
    public static void modifyFile(String fileName, boolean isAbsolute) {
        System.out.println("modifying file");
    }
    public static void deleteFile(String fileName, boolean isAbsolute) {
        System.out.println("deleting file");
    }

    public static void reprintMainMenuOptions() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        for (int i = 3; i < mainMenuText.length; i++) {
            System.out.println(mainMenuText[i]);
        }
    }
}