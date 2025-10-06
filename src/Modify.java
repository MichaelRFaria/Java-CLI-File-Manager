import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Modify {
    /**
     * The method to modify a file.
     *
     * @param fileName the name of the file/folder you want to modify, either given by a relative file path (lone file name),
     *                 or an absolute file path, differentiated by the {@code isAbsolute} parameter.
     * @param isAbsolute whether you have inputted a relative file path (relative to the program's default directory "output") or an absolute file path.
     * @return Whether the file was successfully modified (deleted, or exited manually) or not (either file does not exist, or operation error).
     */
    public static boolean modifyFile(String fileName, boolean isAbsolute) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        /* creating the File object, so we can work on the file
         * will be instantiated as false, if the file does not exist */
        File file = Utils.createFileVarIfExists(fileName, isAbsolute);
        // we return unsuccessful if the file does not exist.
        if (file == null) {return false; }

        String input;

        /* loops until you enter the exit menu, or until you successfully delete the file (as we can no longer modify the file)
        * in both these cases we return true */
        while (true) {
            // we print the possible inputs to the screen
            printSubOptions(file);
            input = Main.getScanner().nextLine().trim();

            switch (input) {
                // renaming file/folder
                case "1":
                    /* we set the file object to the newly renamed file, so we can keep working on it
                    * if the file is unsuccessfully renamed, then the renameFile method, returns the original file parameter  */
                    file = renameFile(file);
                    break;
                // moving a file
                case "2":
                    /* we set the file object to the newly moved file, so we can keep working on it
                     * if the file is unsuccessfully moved, then the movedFile method, returns the original file parameter  */
                    file = moveFile(file);
                    break;
                // deleting a file
                case "3":
                    // if the file is successfully deleted, we return out of this menu, as we can no longer modify the file
                    if (deleteFile(file)) {return true; }
                    break;
                // editing the contents of a text file
                case "4":
                    editTextFile(file);
                    break;
                // exiting this menu
                case "0":
                    return true;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    Utils.delay();
                    break;
            }
        }
    }

    /**
     * The method to rename a file's name.
     *
     * @param file the file you want to rename.
     * @return the newly renamed file (if successful) or the original file parameter (if unsuccessful).
     */
    public static File renameFile(File file) {
        // we assume the given file object represents a folder (therefore it has no file extension), until checked
        String fileExtension = "";

        /* checking if the file object represents a file, in which we need the correct file extension
        * if the file name has a dot, then we must be handling a file and not a folder */
        int posOfDot = Utils.getActualFileName(file).lastIndexOf('.');
        if (posOfDot != -1) {
            fileExtension = Utils.getActualFileName(file).substring(posOfDot);
        }

        // getting the path to the file, so we can append the inputted new name of the file to the original path
        String path;
        path = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('\\') + 1);

        // requesting the new file name from the user
        String newFileName = path + UserInput.getFileNameInput();

        // when the rename operation is cancelled, we return the original file.
        if (newFileName.contains("*")) {
            System.out.println("Operation cancelled.");
            Utils.delay();
            return file;
        }

        String newFileExtension = "";
        posOfDot = newFileName.lastIndexOf('.');
        if (posOfDot != -1) {  // if the file name has a dot, then we must be handling a file and not a folder
            newFileExtension = newFileName.substring(posOfDot);
        }

        boolean successful = false;

        File renamedFile = new File(newFileName);

        /* either we are renaming a folder in which, there is no both file extensions = "" executing the condition's body,
        * or we are renaming a file, in which both file extensions must be equal */
        if (fileExtension.equals(newFileExtension)) {
            successful = file.renameTo(renamedFile);
        }

        // configuring the correct noun, based on the presence or lack of a file extension
        String message = fileExtension.isEmpty() ? "Folder" : "File";

        // printing appropriate message based on if the operation was successful
        System.out.println(successful ? message + " was successfully renamed." : message + " renaming was unsuccessful, please try again.");

        // delaying to ensure the above message has time to be read
        Utils.delay();

        /* if we are successful we return the newly renamed file
        * if we are unsuccessful we return the original file parameter. */
        if (successful) {
            return renamedFile;
        } else {
            return file;
        }
    }

    /**
     * The method to move a file.
     *
     * @param file the file you want to move.
     * @return the newly moved file (if successful) or the original file parameter (if unsuccessful).
     */
    public static File moveFile(File file) {
        String destination = "";

        int posOfDot = Utils.getActualFileName(file).indexOf('.');
        /* we can only move files, if there is not a dot in the file name, then it must be a folder
        * (technically folders can have dots in their names in Windows, but this program does not recognise it that way*/
        if (posOfDot == -1) {
            System.out.println("Cannot perform the move operation on a folder. Please try with a file instead.");
            Utils.delay();
            return null;
        }

        boolean destinationEntered = false;

        // we loop until a valid destination to move the file to is given
        while (!destinationEntered) {
            System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

            System.out.println("Please input one of the following:");
            System.out.println("Input '1' if you would like to enter the destination via an absolute path.");
            System.out.println("Input '2' if you would like to enter the destination via navigating the directories.");
            System.out.println("Input '0' if you would like to exit.");
            System.out.print("\nPlease input an option: ");

            String input = Main.getScanner().nextLine().trim();

            switch (input) {
                // entering the destination via an inputted absolute path
                case "1":
                    destination = UserInput.getAbsolutePathInput();

                    // if we choose to cancel the move operation, we stop here and return the original file parameter
                    if (destination.contains("*")) {
                        System.out.println("Operation cancelled.");
                        Utils.delay();
                        return file;
                    }

                    // we create a temporary File object to test whether the input corresponds to an actual directory
                    File temp = new File(destination);

                    // if the input leads to a directory, then we can exit the loop and start moving the file
                    if (temp.isDirectory()) {
                        destinationEntered = true;
                        break;
                    // if the input does not lead to a directory, we print a message and continue the loop
                    } else {
                        System.out.println("This directory does not exist, please try again.");
                        Utils.delay();
                        continue;
                    }
                // entering the destination via the directory navigator system
                case "2":
                    /* by passing "move" as the mainOption parameter,
                    * this method will return the absolute path of the currently shown directory */
                    destination = DirectoryNavigator.navigateDirs("move", file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('\\') + 1));

                    /* if we exit from the directory navigator menu, we return "exit",
                    * in this case we go back to the start of the loop and request the user to pick another option */
                    if (destination.equals("exit")) {
                        continue;
                    }

                    destinationEntered = true;
                    break;
                // exiting the menu
                case "0":
                    /* we must return the original file parameter when exiting
                    * as the method is called to reassign the file object
                    * regardless of whether the file was moved or not for consistency */
                    return file;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    Utils.delay();
            }
        }

        // moving a file works the same way as renaming a file

        // we append the actual file name to the destination where the file is to be moved
        File newFile = new File(destination + "\\" + Utils.getActualFileName(file));
        //System.out.println(destination + "\\" + Utils.getActualFileName(file));
        boolean successful = file.renameTo(newFile);

        // printing appropriate message based on if the operation was successful
        System.out.println(successful ? "File was successfully moved." : "File moving was unsuccessful, please try again.");

        // ensuring there is enough time for either of the above messages to be read
        Utils.delay();
        if (successful) {
            return newFile;
        } else {
            return file;
        }
    }

    /**
     * The method to delete a file.
     *
     * @param file the file you want to delete.
     * @return whether the file was deleted successfully.
     */
    public static boolean deleteFile(File file) {
        // deleting the file
        boolean successful = file.delete();

        // printing appropriate message based on if the operation was successful
        System.out.println(successful ? "Successfully deleted " + Utils.getActualFileName(file) : Utils.getActualFileName(file) + " could not be deleted, please try again.");

        // ensuring there is enough time for either of the above messages to be read
        Utils.delay();
        return successful;
    }

    /**
     * The method to edit the contents of a text file.
     *
     * @param file the file you want to edit the contents of.
     */
    public static void editTextFile(File file) {
        /* if the file does not end in ".txt", it is not a text file, therefore we cannot edit it
        * in this case we simply print an error message and return out of the method */
        if (!Utils.getActualFileName(file).substring(Utils.getActualFileName(file).lastIndexOf('.') + 1).equals("txt")) { // checking if we are working on a text file
            System.out.println("This option is only compatible with text files.");
            Utils.delay();
            return;
        }

        // we loop until the user decides to stop editing the file, in which they will return from the method
        while (true) {
            /* we open the text file in order to show what the current contents of the file are
            * this method call is in the loop so the user can see the changes as they are made */
            Open.openFile(file.getAbsolutePath(), true);

            System.out.println("\nNow please input one of the following:");
            System.out.println("Input '1' if you would like to write lines to the file.");
            System.out.println("Input '2' if you would like to remove a number of lines from the file.");
            System.out.println("Input '3' if you would like to add a line break to the file.");
            System.out.println("Input '4' if you would like to clear the file.");
            System.out.println("Input '0' if you would like to stop editing the file.");
            System.out.print("\nPlease input an option: ");

            String input = Main.getScanner().nextLine().trim();

            try {
                switch (input) {
                    // writing lines to the file
                    case "1":
                        System.out.println("Enter your lines to add ('*' to finish writing): ");

                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));

                            while (true) {
                                input = Main.getScanner().nextLine(); // not trimmed in case the user wants leading spaces for some reason

                                // exit condition
                                if (input.equals("*")) {
                                    break;
                                }

                                // append the input, then move to the next line
                                writer.append(input);
                                writer.newLine();
                            }

                            writer.close();
                            System.out.println("Writing lines to the file...");

                        } catch (IOException e) {
                            System.out.println("Writer could not be opened. Please try again.");
                        }

                        Utils.delay();
                        break;
                    // removing lines from the file
                    case "2":
                        System.out.print("Enter how many lines you want to remove: ");
                        input = Main.getScanner().nextLine().trim();

                        /* all inputs in this program are retrieved using nextLine() in order to prevent an input like "1 1 1" from being processed as separate tokens.
                         * additionally it prevents the need of a try-catch block for InputMismatchException, if using nextInt.
                         * but in this case since we are working on the value of the input, we must parse it as an int afterward */
                        int linesToRemove;

                        try {
                            linesToRemove = Integer.parseInt(input);
                        // if you enter a non-integer, it loops again, requesting another input.
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter an integer option.");
                            Utils.delay();
                            break;
                        }

                        /* we read the lines of text and add them to an array,
                        * as we will be clearing the contents of the file and rewriting certain lines back */
                        Scanner reader = new Scanner(file);
                        ArrayList<String> lines = new ArrayList<>();

                        while (reader.hasNextLine()) {
                            lines.add(reader.nextLine());
                        }

                        /* if the number of lines in the file exceeds the number of lines we are attempting to remove
                        * then we will remove that amount of lines */
                        if (lines.size() >= linesToRemove) {
                            System.out.println("Removing lines from the file...");
                            /* try-with-resources syntax, to use AutoCloseable and remove the mandatory catch block
                             * we open FileWriter without append mode, automatically clearing the file */
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
                                // we rewrite all the lines of text back, except the last
                                for (int i = 0; i < lines.size() - linesToRemove; i++) {
                                    writer.write(lines.get(i));
                                    writer.newLine();
                                }
                            }
                        // otherwise, we cannot remove any lines from the file
                        } else {
                            System.out.println("Invalid amount of lines.");
                        }

                        /* we close the reader to prevent it from "locking" the file,
                        * which will prevent other operations from being able to execute on the file */
                        reader.close();
                        Utils.delay();
                        break;
                    // adding a line break
                    case "3":
                        System.out.println("Adding a line break...");

                        // try-with-resources syntax, to use AutoCloseable and remove the mandatory catch block
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true))) {
                            // move to next line, flush the stream
                            writer.newLine();
                            writer.flush();
                        }

                        Utils.delay();
                        break;
                    // clearing the file
                    case "4":
                        System.out.print("This option will delete all of the contents of the selected file. Please enter 'Y' to confirm that you want to clear the file, or any other character to cancel the operation: ");
                        input = Main.getScanner().nextLine().trim();

                        // we check for user input confirming to clear the file
                        if (input.equalsIgnoreCase("y")) {
                            System.out.println("\nClearing the file contents...");

                            // opening FileWriter without append mode, automatically clears the file
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {}

                            Utils.delay();
                        }
                        break;
                    // exiting
                    case "0":
                        return;
                    default:
                        System.out.println("Please input an option from the list above, try again.");
                        Utils.delay();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to print the possible options that can be executed on a file/folder.
     *
     * @param file The file you want to modify. We pass this into the method, so we can find out where the file is located, it's name and if it's a file or a folder.
     */
    public static void printSubOptions(File file) {
        // figuring out the correct noun to use for the file (file or folder)
        String message = Utils.getActualFileName(file).contains(".") ? "file" : "folder";

        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        System.out.println("Now modifying the " + message + ": " + Utils.getActualFileName(file) + " located at: " + file.getAbsolutePath() + "\n");
        System.out.println("Please input one of the following:");
        System.out.println("Input '1' if you would like to rename this file.");
        System.out.println("Input '2' if you would like to move this file.");
        System.out.println("Input '3' if you would like to delete this file.");
        System.out.println("Input '4' if you would like to edit the contents of this file (text files only.)");
        System.out.println("Input '0' if you would like to exit from this menu.");
        System.out.print("\nPlease input an option: ");
    }
}


