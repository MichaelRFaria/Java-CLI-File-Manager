import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.util.function.BiFunction;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static String defaultPath;
    private static final String[] mainMenuText = {"Welcome to the Command Line File Handler", "This program allows you to perform several operations on files and folders",
            "Including: creating, reading/running, modifying, and deleting files/folders\n","Please input one of the following:",
            "Input '1' if you would like to create a new file/folder", "Input '2' if you would like to read/run from an existing file/folder",
            "Input '3' if you would like to modify an existing file/folder", "Input '4' if you would like to delete an existing file/folder",
            "Input '0' if you would like to exit\n", "Please input an option: "};

    /*
    TODO:
        (file search option?)
            maybe allow user to configure how many files they would like displayed on the screen at a time
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
        String fileName;
        boolean isAbsolute = false;
        boolean exited = false;

        while (!exited) {
            printSubOptions(mainOption);

            try {
                int num = scanner.nextInt();
                switch (num) {
                    case 1:
                        break;
                    case 2:
                        isAbsolute = true;
                        break;
                    case 3:
                        DirectoryNavigator.navigateDirs(mainOption);
                        break;
                    case 0:
                        reprintMainMenuOptions();
                        exited = true;
                        break;
                    default:
                        System.out.println("Please input an option from the list above, try again.");
                        delay();
                        continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please input an integer, try again.");
                delay();
                scanner.next();
            }

            if (!exited) {
                switch (mainOption) {
                    case "create":
                        if (isAbsolute) {
                            fileName = UserInput.getAbsolutePathInput();
                        } else {
                            fileName = UserInput.getFileNameInput();
                        }
                        Create.createFile(fileName, isAbsolute);
                        break;

                    case "open", "modify", "delete":
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
                "open", Open::openFile,
                "modify", Modify::modifyFile,
                "delete", Delete::deleteFile
        );

        BiFunction<String, Boolean, Boolean> method = optionToExec.get(mainOption);

        //System.out.println(Thread.currentThread().getStackTrace()[2].getClassName());

        boolean successful; // this is false if the operation is unsuccessful due to error, or the file is not found at fileName location

        do {
            String fileName = "";
            if (currNavDir != null) {
                fileName += currNavDir;
            }
            if (isAbsolute) {
                fileName = UserInput.getAbsolutePathInput();
            } else {
                fileName += UserInput.getFileNameInput();
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