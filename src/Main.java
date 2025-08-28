import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    /*
    TODO:
        simplify text printing, making it easier to print and reprint
        implement default file path variable for creation, reading, modification, and deleting (create data folder)
        implement folder navigating (page system? limit files shown by 10-20 and store the others, then allow page navigation)
        implement input of absolute file path and relative file path (method to take input and check if its valid:
            file name (cant have several '.' illegal characters etc)
            for absolute, file path must exist, maybe give the option to create folders if the directory doesnt exist?)
    */



    public static void main(String[] args) {
        String[] intro = {"Welcome to the Command Line File Handler", "This program allows you to perform several operations on files",
                "Including: creating, reading, updating, and deleting files", "Please input one of the following:", "Input '1' if you would like to create a new file",
                "Input '2' if you would like to read from an existing file", "Input '3' if you would like to modify an existing file",
                "Input '4' if you would like to delete an existing file", "Input '0' if you would like to exit\n",
                "Please input an option: "};

        for (String line : intro) {
            System.out.println(line);
        }

        while (true) {
            try {
                int num = scanner.nextInt();
                if (num == 1) {
                    createFile();
                } else if (num == 2) {
                    readFile();
                } else if (num == 3) {
                    modifyFile();
                } else if (num == 4) {
                    deleteFile();
                }else if (num == 0) {
                    System.exit(0);
                } else {
                    System.out.println("Please input an option from the list above, try again: ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please input an integer, try again: ");
                scanner.next();
            }
        }
    }

    public static void createFile() {
        System.out.println("creating file");
        // testing file creation default path
        try {
            File file = new File("test.txt");
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
}
