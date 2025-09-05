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
                        fileName = UserInput.getFileNameInput();
                        break;
                    case 2:
                        fileName = UserInput.getAbsolutePathInput();
                        isAbsolute = true;
                        break;
                    case 3:
                        DirectoryNavigator.navigateDirs(mainOption);
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
                            Create.createFile(fileName, isAbsolute);
                            break;
                        case "open":
                            boolean fileWasFound = Open.openFile(fileName, isAbsolute);

                            while (!fileWasFound) {
                                if (isAbsolute) {
                                    fileName = UserInput.getAbsolutePathInput();
                                } else {
                                    fileName = UserInput.getFileNameInput();
                                }
                                fileWasFound = Open.openFile(fileName, isAbsolute);
                            }

                            break;
                        case "modify":
                            Modify.modifyFile(fileName, isAbsolute);
                            break;
                        case "delete":
                            Delete.deleteFile(fileName, isAbsolute);
                            break;
                        default:
                    }
                } else {
                    break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Please input a valid option, try again: ");
                scanner.next();
            }
        }
    }

    public static void reprintMainMenuOptions() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        for (int i = 3; i < mainMenuText.length; i++) {
            System.out.println(mainMenuText[i]);
        }
    }
    
    public static String getDefaultPath() {
        return defaultPath;
    }

    public static Scanner getScanner() {
        return scanner;
    }
}