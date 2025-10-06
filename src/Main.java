import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.util.function.BiFunction;

public class Main {
    /* We use a single Scanner throughout the entire program to handle user input.
    * We need to be able to access the defaultPath when handling operations in the default program folder "output". */
    private static final Scanner scanner = new Scanner(System.in);
    private static String defaultPath;
    private static final String[] mainMenuText = {"Welcome to the Command Line File Handler.", "This program allows you to perform several operations on files and folders.",
            "Including: creating, reading/running, modifying and searching files/folders.\n","Please input one of the following:",
            "Input '1' if you would like to create a new file/folder.", "Input '2' if you would like to read/run from an existing file/folder.",
            "Input '3' if you would like to modify an existing file/folder.", "Input '4' if you would like to search for an existing file/folder.",
            "Input '0' if you would like to exit.\n", "Please input an option: "};

    /*
    todo before finishing the project
        double check text formatting for all the menus
        run the program through cmd
        remove all unnecessary print statements
        update readme.md
        add comments and javadoc comments (can right click on method name and generate javadoc)
     */

    /**
     * Starts up the main menu.
     * Also figures out the default file path through the working directory.
     *
     * @param args Standard main method command-line arguments
     */
    public static void main(String[] args) {
        /* creating a file object will automatically configure the absolute path to the current working directory
        * therefore by appending it with the "output" subdirectory, we can get the default program folder's location */
        File file = new File("");
        defaultPath = file.getAbsolutePath() + "\\output\\";

        mainMenu();
    }

    /**
     * Displays the main menu options and handles user inputs into the menu.
     */
    public static void mainMenu() {
        for (String line : mainMenuText) {
            System.out.println(line);
        }

        while (true) {
            /* all inputs in this program are retrieved using nextLine() in order to prevent an input like "1 1 1" from being processed as separate tokens.
            * additionally it prevents the need of a try-catch block for InputMismatchException, if using nextInt */
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    subOptionMenu(Option.CREATE);
                    break;
                case "2":
                    subOptionMenu(Option.OPEN);
                    break;
                case "3":
                    subOptionMenu(Option.MODIFY);
                    break;
                case "4":
                    subOptionMenu(Option.SEARCH);
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    Utils.delay(1500);
            }
        }
    }

    /**
     * Displays the sub-options menu and handles user inputs into the menu.
     *
     * @param mainOption the main option selected from the main menu.
     */
    public static void subOptionMenu(Option mainOption) {
        boolean isAbsolute = false;
        boolean exited = false;
        boolean navMenuUsed;

        while (!exited) {
            printSubOptions(mainOption);
            navMenuUsed = false;

            String input = scanner.nextLine().trim();
            switch (input) {
                // executing the operation in the default program folder "output"
                case "1":
                    /* this is initialised as false but we need to set it to false regardless, as this is in a loop and if
                    * you were to execute case "2", then go back to case "1", we would need to reset isAbsolute back to false  */
                    isAbsolute = false;
                    break;
                // executing the operation via an inputted absolute file path
                case "2":
                    isAbsolute = true;
                    break;
                // executing the operation by finding the appropriate directory via the directory navigator system
                case "3":
                    DirectoryNavigator.navigateDirs(mainOption, getDefaultPath());
                    navMenuUsed = true;
                    break;
                // exiting the sub options menu, and returning to the main menu
                case "0":
                    reprintMainMenuOptions();
                    exited = true;
                    break;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    Utils.delay(1500);
                    continue;
            }
            
            /* if we are not using the DirectoryNavigator to execute our option, or if we haven't selected to leave this menu
            * then we must execute our option */
            if (!exited && !navMenuUsed) {
                execOptionUntilSuccessful(isAbsolute, mainOption, null);
            }
        }
    }

    /**
     * Executes the given {@code mainOption} until successful or an input is given to cancel the operation.
     *
     * @param isAbsolute whether you are inputting a relative file path (relative to the program's default directory "output") or if you are inputting an absolute file path
     * @param mainOption the main option selected from the main menu
     * @param currNavDir the current directory you are looking at in the directory navigator system (passed as null by default, unless called by DirectoryNavigator.java)
     */
    public static void execOptionUntilSuccessful(boolean isAbsolute, Option mainOption, String currNavDir) {
        /* this map allows the appropriate method to be called based on the param mainOption,
        * alternatively you could use a switch statement, but you would need several switch statements,
        * and all sets of switch statements would need to expand everytime you added a new main option, 
        * however, this can cause some strange special cases */

        Map <Option, BiFunction<String, Boolean, Boolean>> optionToExec = Map.of(
                Option.CREATE, Create::createFile,
                Option.OPEN, Open::openFile,
                Option.MODIFY, Modify::modifyFile,
                Option.SEARCH, Search::searchForFile
        );

        BiFunction<String, Boolean, Boolean> method = optionToExec.get(mainOption);

        /* this is false if the operation is unsuccessful due to error, the file not being found (when opening, modifying and searching),
        * or if the file already exists (when creating) */
        boolean successful;
        do {
            String fileName = setupFileName(isAbsolute, mainOption, currNavDir);

            // when an operation is cancelled we return out of this method, bringing us back to the previous sub-option menu
            if (fileName.contains("*")) {
                System.out.println("Operation cancelled.");
                Utils.delay(500);
                return;
            }

            successful = method.apply(fileName, isAbsolute);
        } while (!successful);
    }

    /**
     * Helper method to prepend and append the correct parameters and inputs to form the appropriate file name.
     *
     * @param isAbsolute whether you are inputting a relative file path (relative to the program's default directory "output") or if you are inputting an absolute file path
     * @param mainOption the main option selected from the main menu
     * @param currNavDir the current directory you are looking at in the directory navigator system (passed as null by default, unless called by DirectoryNavigator.java)
     * @return The appropriately formatted file name for the specific operation to be executed
     */
    private static String setupFileName(boolean isAbsolute, Option mainOption, String currNavDir) {
        String fileName = "";

        // always add the current directory if given (passed by calls within DirectoryNavigator.java)
        if (currNavDir != null) {
            fileName += currNavDir;
        }

        // special case: if main option is search, and we are executing in the program's default folder (isAbsolute is false)
        // or we are executing in a given directory from the DirectoryNavigator (currNavDir is not empty),
        // then we must not add a subdirectory name. We just execute the option where given
        if (!(mainOption == Option.SEARCH && (!isAbsolute || currNavDir != null))) {
            if (!isAbsolute || currNavDir != null) {
                fileName += UserInput.getFileNameInput();
            } else {
                fileName += UserInput.getAbsolutePathInput();
            }
        }
        return fileName;
    }


    /**
     * This method reprints the main menu text, upon exiting from a sub-option menu, however, it skips the introductory 3 lines, only requesting and displaying input options.
     */
    public static void reprintMainMenuOptions() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        for (int i = 3; i < mainMenuText.length; i++) {
            System.out.println(mainMenuText[i]);
        }
    }

    // todo - these subscriptions are phrased weirdly, maybe add a case statement that changes the print statements more, e.g: when passing search option 2 and 3 should say "folder" instead of "file"

    /**
     * This method prints the sub-options, substituting the verb with the appropriate {@code mainOption}.
     *
     * @param mainOption the main option selected from the main menu
     */
    public static void printSubOptions(Option mainOption) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        // converting enum to a lowercase string to add to the message
        String action = mainOption.toString().toLowerCase();

        System.out.println("Please input one of the following:");
        System.out.println("Input '1' if you would like to " + action + " a file in the program's default directory.");
        System.out.println("Input '2' if you would like to " + action + " a file in an inputted absolute file path.");
        System.out.println("Input '3' if you would like to navigate directories to " + action + " a file.");
        System.out.println("Input '0' if you would like to exit.\n");
        System.out.print("Please input an option: ");
    }

    /**
     * This method retrieves the default program directory ("output" folder), a location that can be used as the destination for the program's operations.
     *
     * @return the absolute file path of the default program folder
     */
    public static String getDefaultPath() {
        return defaultPath;
    }

    /**
     * This method retrieves the {@code Scanner} that is used across the entirety of the program
     *
     * @return the pre-instantiated {@code Scanner} object
     */
    public static Scanner getScanner() {
        return scanner;
    }
}