import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Open {

        /*
    TODO
        allow reading of text files (DONE), running of exes (DONE), .bat? etc
        return to suboption after typing exit or something
     */

    public static boolean openFile(String fileName, boolean isAbsolute) { // returns true if file is found, and false if file is not found
        String path = "";

        if (!isAbsolute) {
            path = Main.getDefaultPath();
        }

        File file = new File(path + fileName);
        if (!file.exists()) {
            System.out.println("File was not found, please try again.");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        }

        String fileExtension = "invalid";
        int posOfDot = fileName.indexOf('.');
        if (posOfDot != -1) {
            fileExtension = fileName.substring(posOfDot + 1);
        }

        switch (fileExtension) {
            case "txt":
                try {
                    Scanner reader = new Scanner(file);

                    System.out.println("Printing the contents of: " + path + fileName + "\n");
                    while (reader.hasNextLine()) {
                        String line = reader.nextLine();
                        System.out.println(line);
                    }

                    System.out.println("\nEnd of file...");

                    System.out.println("\nPress enter to return close the text file.");
                    Main.getScanner().next();
                    // doesn't actually close file right now, but can either "close" file by using the cmd clear technique or by reprinting menu options in loop of the method call

                    return true;
                } catch (FileNotFoundException e) {
                    System.out.println("File was not found, please try again.");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    return false;
                }
            case "exe":
                try {
                    System.out.println(fileName);
                    String dir = fileName.substring(0, fileName.lastIndexOf('\\'));
                    // in the case of running through an absolute file path or the navigation menu, we need to find the working directory in order to execute our command
                    Runtime.getRuntime().exec(fileName, null, new File(dir));
                    System.out.println("Running the executable at:" + fileName);
                    return true;
                } catch (IOException e) {
                    System.out.println("an error occurred when starting the .exe");
                    e.printStackTrace();
                    return false;
                }
            default:
                break;
        }
        return false;
    }
}
