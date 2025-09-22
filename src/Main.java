import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.util.function.BiFunction;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static String defaultPath;
    private static final String[] mainMenuText = {"Welcome to the Command Line File Handler", "This program allows you to perform several operations on files and folders",
            "Including: creating, reading/running, modifying and searching files/folders\n","Please input one of the following:",
            "Input '1' if you would like to create a new file/folder", "Input '2' if you would like to read/run from an existing file/folder",
            "Input '3' if you would like to modify an existing file/folder", "Input '4' if you would like to search for an existing file/folder",
            "Input '0' if you would like to exit\n", "Please input an option: "};

    /*
    todo next time
        implement file search option
        should only work with folders
        requests a name of a folder/file
        uses depth search to find it
        system to list duplicates and request user to input which one they want to open
        if the folder/file is found or a duplicate is specified
            list of main options for that file/folder (read/run or modify)
     */

    /*
    todo before finishing the project
        double check text formatting for all the menus
        run the program through cmd
        remove all unnecessary print statements
        update readme.md
        add comments and javadoc comments (can right click on method name and generate javadoc)
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
            String choice = scanner.nextLine().trim();

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
                    subOptionMenu("search");
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
        String fileName;
        boolean isAbsolute = false;
        boolean exited = false;
        boolean navMenuUsed;

        while (!exited) {
            printSubOptions(mainOption);
            navMenuUsed = false;

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    // this is initialised as false but we need to set it to false regardless as if we option "2" eventually we will return back to this menu and isAbsolute will still be set to true
                    isAbsolute = false;
                    break;
                case "2":
                    isAbsolute = true;
                    break;
                case "3":
                    DirectoryNavigator.navigateDirs(mainOption, getDefaultPath());
                    navMenuUsed = true;
                    break;
                case "0":
                    reprintMainMenuOptions();
                    exited = true;
                    break;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    delay();
                    continue;
            }


            if (!exited && !navMenuUsed) {
                switch (mainOption) {
                    case "create":
                        if (isAbsolute) {
                            fileName = UserInput.getAbsolutePathInput();
                        } else {
                            fileName = UserInput.getFileNameInput();
                        }
                        Create.createFile(fileName, isAbsolute);
                        break;
                    case "search":
                        if (isAbsolute) {
                            fileName = UserInput.getAbsolutePathInput();
                        } else {
                            fileName = UserInput.getFileNameInput();
                        }
                        Search.searchForFile(fileName, isAbsolute);
                        break;
                    case "open", "modify":
                        execOptionUntilSuccessful(isAbsolute, mainOption, null);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + mainOption);
                }
            }
        }
    }

    public static void execOptionUntilSuccessful(boolean isAbsolute, String mainOption, String currNavDir) {
        /*
        this map allows the appropriate method to be called based on the param mainOption,
        alternatively you could use a switch case statement, but you would need two switch cases which would be ugly and stupid and boring
         */
        Map <String, BiFunction<String, Boolean, Boolean>> optionToExec = Map.of(
                "create", Create::createFile,
                "open", Open::openFile,
                "modify", Modify::modifyFile
        );

        BiFunction<String, Boolean, Boolean> method = optionToExec.get(mainOption);

        //System.out.println(Thread.currentThread().getStackTrace()[2].getClassName());

        boolean successful; // this is false if the operation is unsuccessful due to error, or the file is not found at fileName location

        do {
            String fileName = "";
            if (currNavDir != null) {
                fileName += currNavDir;
            }
            if (!isAbsolute || currNavDir != null) {
                fileName += UserInput.getFileNameInput();
            } else {
                fileName += UserInput.getAbsolutePathInput();
            }
            successful = method.apply(fileName, isAbsolute);
        } while (!successful);
    }

    public static void reprintMainMenuOptions() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        for (int i = 3; i < mainMenuText.length; i++) {
            System.out.println(mainMenuText[i]);
        }
    }

    // todo - these suboptions are phrased wierdly, maybe add a case statement that changes the print statements more, e.g: when passing search option 2 and 3 should say "folder" instead of "file"

    public static void printSubOptions(String mainOption) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        System.out.println("Please input one of the following: ");
        System.out.println("Input '1' if you would like to " + mainOption + " a file in the program's default directory");
        System.out.println("Input '2' if you would like to " + mainOption + " a file in an inputted absolute file path");
        System.out.println("Input '3' if you would like to navigate directories to " + mainOption + " a file");
        System.out.println("Input '0' if you would like to exit\n");
        System.out.println("Please input an option: ");
    }

    public static void delay() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static String getDefaultPath() {
        return defaultPath;
    }

    public static Scanner getScanner() {
        return scanner;
    }
}