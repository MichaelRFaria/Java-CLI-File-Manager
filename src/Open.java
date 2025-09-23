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
        File file = FileUtils.createFileVarIfExists(fileName, isAbsolute);
        if (file == null) {return false; }

        String fileExtension = "invalid";
        int posOfDot = FileUtils.getActualFileName(file).indexOf('.');
        if (posOfDot != -1) {
            fileExtension = FileUtils.getActualFileName(file).substring(posOfDot + 1);
        } else {
            System.out.println("We cannot execute or read a folder, please try with a file.");
            Main.delay();
            return false;
        }

        /*
        todo
            maybe add page navigation system like DirectoryNavigator.updateNavigationMenu
         */

        switch (fileExtension) {
            case "txt":
                try {
                    Scanner reader = new Scanner(file);

                    System.out.println("\nPrinting the contents of: " + file.getAbsolutePath() + "\n");

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
                        Main.getScanner().nextLine().trim();
                    }

                    /*
                    always close any scanner you make that you don't plan to use, spent so much time trying to find out why some operations fail after editing a text file in Modify.java
                    turns out it was just this, which locks the file making renaming/moving/deleting fail whilst editing the file still succeeds
                     */

                    reader.close();
                    return true;
                } catch (FileNotFoundException e) {
                    System.out.println("File was not found, please try again.");
                    Main.delay();
                    return false;
                }
            case "exe":
                try {
                    //System.out.println(file.getAbsolutePath());
                    String dir = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('\\'));
                    // in the case of running through an absolute file path or the navigation menu, we need to find the working directory in order to execute our command
                    Runtime.getRuntime().exec(file.getAbsolutePath(), null, new File(dir));
                    System.out.println("\nRunning the executable " + FileUtils.getActualFileName(file) + " at: " + file.getAbsolutePath());
                    Main.delay();
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
