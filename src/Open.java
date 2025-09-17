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

        /* when using the file navigator to perform an action we take the file name as an absolute path,
        but set isAbsolute to false, so that we can call getFileNameInput instead of getAbsolutePathInput,
         ensuring we use the correct input checking, an alternative to this would be passing the current directory into getAbsolutePathInput. */

        if (!isAbsolute && fileName.indexOf('\\') == -1) {
            path = Main.getDefaultPath();
        }

        File file = new File(path + fileName);
        if (!file.exists()) {
            System.out.println("File was not found, please try again.");
            Main.delay();
            return false;
        }

        String fileExtension = "invalid";
        int posOfDot = fileName.indexOf('.');
        if (posOfDot != -1) {
            fileExtension = fileName.substring(posOfDot + 1);
        }

        /*
        todo
            maybe add page navigation system like DirectoryNavigator.updateNavigationMenu
         */

        switch (fileExtension) {
            case "txt":
                try {
                    Scanner reader = new Scanner(file);

                    System.out.println("\nPrinting the contents of: " + path + fileName + "\n");

                    if (reader.hasNextLine()) {
                        while (reader.hasNextLine()) {
                            String line = reader.nextLine();
                            System.out.println(line);
                        }
                    } else {
                        System.out.println("This file is empty!");
                    }

                    System.out.println("\nEnd of file...");

                    /*
                    this sucks so much. essentially we are figuring out if we are opening the file via the Modify.java class in which the below message is not appropriate.
                    ideally we would pass an isWriting bool into openFile, but it would mess up the bifunction map in Main.execOptionUntilSuccessful
                    which is super awesome and cool and doesn't deserve to be deleted :(
                    */

                    if (!Thread.currentThread().getStackTrace()[2].getClassName().equals("Modify")) {
                        System.out.println("\nEnter any character to close the text file.");
                        Main.getScanner().next();
                    }

                    return true;
                } catch (FileNotFoundException e) {
                    System.out.println("File was not found, please try again.");
                    Main.delay();
                    return false;
                }
            case "exe":
                try {
                    System.out.println(fileName);
                    String dir = fileName.substring(0, fileName.lastIndexOf('\\'));
                    // in the case of running through an absolute file path or the navigation menu, we need to find the working directory in order to execute our command
                    Runtime.getRuntime().exec(fileName, null, new File(dir));
                    System.out.println("\nRunning the executable at:" + fileName);
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
