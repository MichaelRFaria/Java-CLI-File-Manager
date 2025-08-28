import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static String defaultPath;
    static String[] mainMenuText = {"Welcome to the Command Line File Handler", "This program allows you to perform several operations on files",
            "Including: creating, reading, updating, and deleting files","Please input one of the following:",
            "Input '1' if you would like to create a new file", "Input '2' if you would like to read from an existing file",
            "Input '3' if you would like to modify an existing file", "Input '4' if you would like to delete an existing file",
            "Input '0' if you would like to exit\n", "Please input an option: "};


    /*
    TODO:
        simplify text printing, making it easier to print and reprint
        clear text upon entering an option, reprint when exiting an option
        implement default file path variable for creation, reading, modification, and deleting (create data folder)
        implement folder navigating (page system? limit files shown by 10-20 and store the others, then allow page navigation)
        implement input of absolute file path and relative file path (method to take input and check if its valid:
            file name (cant have several '.' illegal characters etc)
            for absolute, file path must exist, maybe give the option to create folders if the directory doesnt exist?)
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
                    startCreatingFile();
                    break;
                case "2":
                    readFile();
                    break;
                case "3":
                    modifyFile();
                    break;
                case "4":
                    deleteFile();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please try again");
            }
        }
    }

    /*
    option for default file path
    option for absolute file path (input)
    option for relative file path (navigation system)
     */
    public static void startCreatingFile() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        System.out.println("Please input one of the following: ");
        System.out.println("Input '1' if you would like to create a file in the program's default directory");
        System.out.println("Input '2' if you would like to create a file in an inputted absolute file path");
        System.out.println("Input '3' if you would like to navigate directories to create a file");
        System.out.println("Input '0' if you would like to exit\n");
        System.out.println("Please input an option: ");

        while (true) {
            try {
                int num = scanner.nextInt();
                if (num == 1) {
                    String fileName = getFileNameInput();
                    createFile(fileName, "");
                } else if (num == 2) {
                    // absolute file path
                } else if (num == 3) {
                    // navigate file paths
                } else if (num == 0) {
                    printMainMenuOptions();
                    break;
                } else {
                    System.out.println("Please input an option from the list above, try again: ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please input an integer, try again: ");
                scanner.next();
            }
        }
    }

    public static String getFileNameInput() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        System.out.println("Please input a valid file name: ");

        while (true) {
            String input = scanner.next();
            if (!input.isEmpty()) { // name checking rules go here
                return input;
            }
        }
    }

    public static void createFile(String fileName, String path) {
        if (path.isEmpty()) {
            path = defaultPath;
        }
        System.out.println("creating file");

        try {
            File file = new File(defaultPath + fileName);
            if (file.createNewFile()) {
                System.out.println("successfully created " + file.getName());
            } else {
                System.out.println(file.getName() + " already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred;");
            e.printStackTrace();
        }
    }

    public static void readFile() {
        System.out.println("reading file");
    }
    public static void modifyFile() {
        System.out.println("modifying file");
    }
    public static void deleteFile() {
        System.out.println("deleting file");
    }

    public static void printMainMenuOptions() {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        for (int i = 3; i < mainMenuText.length; i++) {
            System.out.println(mainMenuText[i]);
        }
    }
}
