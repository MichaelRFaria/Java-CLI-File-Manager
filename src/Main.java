import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Command Line File Renamer");
        System.out.println("This program allows you to rename fileszn");
        System.out.println("Please input one of the following:");
        System.out.println("Input '1' if you would like to navigate your directories");
        System.out.println("Input '2' if you would like to enter a file/folder path");
        System.out.println("Input '0' if you would like to exit\n");
        System.out.println("Please input an option: ");

        while (true) {
            try {
                int num = scanner.nextInt();
                if (num == 1) {
                    navigate();
                } else if (num == 2) {
                    absolutePath();
                } else if (num == 0) {
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

    public static void navigate() {
        System.out.println("nav entererd");
    }
    public static void absolutePath() {
        System.out.println("abs entered");
    }
}
