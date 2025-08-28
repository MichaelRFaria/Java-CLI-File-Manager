import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        /* maybe figure out a cleaner way to print all this text
        (e.g: put all the text in an array and loop through, printing each line)
        will be good as you can easily reprint the text if you want to return back from an option
        additionally it allows you to print select bits of text*/

        System.out.println("Welcome to the Command Line File Handler");
        System.out.println("This program allows you to perform several operations on files");
        System.out.println("Including: creating, reading, updating, and deleting files");
        System.out.println("Please input one of the following:");
        System.out.println("Input '1' if you would like to create a new file");
        System.out.println("Input '2' if you would like to read from an existing file");
        System.out.println("Input '3' if you would like to modify an existing file");
        System.out.println("Input '4' if you would like to delete an existing file");
        System.out.println("Input '0' if you would like to exit\n");
        System.out.println("Please input an option: ");

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
