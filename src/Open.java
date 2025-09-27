import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Open {
    /**
     * The method to open a file.
     *
     * @param fileName the name of the file/folder you want to open, either given by a relative file path (lone file name),
     *                 or an absolute file path, differentiated by the {@code isAbsolute} parameter.
     * @param isAbsolute whether you have inputted a relative file path (relative to the program's default directory "output") or an absolute file path.
     * @return Whether the file was successfully opened or not (either does not exist, or operation error).
     */
    public static boolean openFile(String fileName, boolean isAbsolute) {
        /* creating the File object so we can work on the file
        * will be instantiated as false, if the file does not exist */
        File file = FileUtils.createFileVarIfExists(fileName, isAbsolute);
        // we return unsuccessful if the file does not exist.
        if (file == null) {return false; }

        String fileExtension;
        int posOfDot = FileUtils.getActualFileName(file).indexOf('.');
        // if the file has a '.' then we are working on a file, and must extract the file extension to figure out what we are working on
        if (posOfDot != -1) {
            fileExtension = FileUtils.getActualFileName(file).substring(posOfDot + 1);
        // if the file does not have a '.', then we must be working on a folder, which is not possible
        } else {
            System.out.println("We cannot execute or read a folder, please try with a file.");
            Main.delay();
            return false;
        }

        /*
        todo
            maybe add page navigation system like DirectoryNavigator.updateNavigationMenu for text files
         */

        switch (fileExtension) {
            case "txt":
                try {
                    // we create a Scanner passing the File object into it as it's source parameter
                    Scanner reader = new Scanner(file);

                    System.out.println("\nPrinting the contents of: " + file.getAbsolutePath() + "\n");

                    // we check if the file is empty or not before we start reading lines
                    if (reader.hasNextLine()) {
                        // while there are more lines in the file we print them to the console
                        while (reader.hasNextLine()) {
                            String line = reader.nextLine();
                            System.out.println(line);
                        }
                    // the file is empty: we print an appropriate message to indicate the fact to the user
                    } else {
                        System.out.println("This file is empty!");
                    }

                    // we print an indicator that the file's contents have finished printing
                    System.out.println("\nEnd of file...");

                    /* we need to figure out if we are opening the file as part of the Modify.editTextFile method, which would not require us to print the message below
                    * as the text file contents must remain on the screen for clarity while editing the file. Ideally we would pass an isWriting boolean into this method,
                    * but that would ruin the BiFunction map utilised in Main.execOptionUntilSuccessful. */

                    /* if we are not calling this method from the Modify class, then we have the option of closing the file via a user input
                    * if the conditional is false the stack trace would appear as ...->Modify.modifyFile->Modify.editTextFile->Open.openFile*/
                    if (!Thread.currentThread().getStackTrace()[2].getClassName().equals("Modify")) {
                        System.out.println("\nEnter any character to close the text file.");
                        // scanner prevents the program from continuing until an input is given, thus the text file content's remain on screen until the user is ready to exit
                        Main.getScanner().nextLine().trim();
                    }

                    // we must close the scanner to "unlock" the file, so other operations can be executed on the same file
                    reader.close();
                    return true;
                } catch (FileNotFoundException e) {
                    System.out.println("File was not found, please try again.");
                    Main.delay();
                    return false;
                }
            case "exe":
                try {
                    // Runtime.getRuntime().exec(...) requires the working directory as it's third parameter, below we are calculating where that is
                    String dir = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('\\'));
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
                System.out.println("This program is not able to work on files with the " + fileExtension + " file extension.");
                Main.delay();
                return false;
        }
    }
}
