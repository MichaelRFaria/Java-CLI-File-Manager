import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Modify {
    /*
    todo (notable)
        by making File file a static variable we can create setters and getters capable of getting fullFileName and actualFileName from the file variable itself
        thus we could remove the actualFileName and fullFileName variables, in fact you wouldnt even need to pass anything into the methods
     */
    public static boolean modifyFile(String fileName, boolean isAbsolute) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        String actualFileName;
        String fullFileName;
        String message;

        if (isAbsolute) {
            fullFileName = fileName;
            actualFileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
        } else {
            fullFileName = Main.getDefaultPath() + fileName;
            actualFileName = fileName;
        }

        message = fileName.indexOf('.') != -1 ? "file" : "folder";

        File file = new File(fullFileName);
        if (!file.exists()) {
            System.out.println(message + " was not found, please try again.");
            Main.delay();
            return false;
        }

        String input;

        while (true) {
            printSubOptions(message, actualFileName);
//            System.out.println("ACTUAL FILE NAME: " + actualFileName);
//            System.out.println("FULL FILE NAME (PATH): " + fullFileName);
            input = Main.getScanner().nextLine().trim();

            // need a way to exit from these options like typing something special "~", etc
            switch (input) { // renaming file/folder
                case "1":
                    file = renameFile(actualFileName, fullFileName, file);
                    fullFileName = file.getAbsolutePath();
                    actualFileName = fullFileName.substring(fullFileName.lastIndexOf('\\') + 1);
                    break;
                case "2": // moving a file
                    File newFile = moveFile(actualFileName, file);
                    if (newFile != null) {
                        file = newFile;
                        fullFileName = file.getAbsolutePath();
                        actualFileName = fullFileName.substring(fullFileName.lastIndexOf('\\') + 1);
                    }
                    break;
                case "3": // deleting a file
                    if (deleteFile(actualFileName, file)) {return true; }
                    break;
                case "4": // editing the contents of a text file
                    editTextFile(actualFileName, fullFileName);
                    break;
                case "0": // exiting this menu
                    return true;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    Main.delay();
                    break;
            }
        }
    }

    public static File renameFile(String actualFileName, String fullFileName, File file) {
        String fileExtension = "folder";

        int posOfDot = actualFileName.lastIndexOf('.');
        if (posOfDot != -1) { // if the file name has a dot, then we must be handling a file and not a folder
            fileExtension = actualFileName.substring(posOfDot + 1);
        }

        String path;
        path = fullFileName.substring(0, fullFileName.lastIndexOf('\\') + 1); // getting the path so we can append the inputted new name of the file to the original path

        String newFileName = path + UserInput.getFileNameInput();

//        System.out.println("Path: " + path);
//        System.out.println("filename after path added: " + fullFileName);
//        System.out.println("original filename: " + actualFileName);
//        System.out.println("new filename: " + newFileName);

        String newFileExtension = "folder";
        posOfDot = newFileName.lastIndexOf('.');
        if (posOfDot != -1) {  // if the file name has a dot, then we must be handling a file and not a folder
            newFileExtension = newFileName.substring(posOfDot + 1);
        }

        boolean successful = false;

        File renamedFile = new File(newFileName);
        if (fileExtension.equals("folder") == newFileExtension.equals("folder")) { // either we are renaming a folder to a folder, or renaming a file to a file
            //System.out.println(file.getAbsolutePath());
            //System.out.println(newFile.getAbsolutePath());
            successful = file.renameTo(renamedFile);
            //System.out.println(successful);
        }

        String message = fileExtension.equals("folder") ? "Folder" : "File";

        System.out.println(successful ? message + " was successfully renamed." : message + " renaming was unsuccessful. Please try again.");

        Main.delay();
        if (successful) {
            return renamedFile;
        } else {
            return file;
        }
    }

    public static File moveFile(String actualFileName, File file) {
        String destination;

        int posOfDot = actualFileName.indexOf('.');
        if (posOfDot == -1) { // we can only move files, if there is not a dot in the file name, then it must be a folder (technically folders can have dots in their names in windows, but I don't care)
            System.out.println("Cannot perform the move operation on a folder. Please try with a file instead.");
            Main.delay();
            return null;
        }

        while (true) { // breaks when moving is successful, where it will return the new file
            System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

            System.out.println("Please input one of the following: ");
            System.out.println("Input '1' if you would like to enter the destination via an absolute path");
            System.out.println("Input '2' if you would like to enter the destination via navigating the directories");
            System.out.println("\nPlease input an option: ");

            String input = Main.getScanner().nextLine().trim();

            switch (input) {
                case "1":
                    destination = UserInput.getAbsolutePathInput();

                    File temp = new File(destination);
                    if (temp.isDirectory()) {
                        break;
                    } else {
                        System.out.println("This directory does not exist, please try again.");
                        Main.delay();
                        continue; // causes loop
                    }
                case "2":
                    destination = DirectoryNavigator.navigateDirs("move");
                    break;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    Main.delay();
                    continue; // causes loop
            }


            File newFile = new File(destination + "\\" + actualFileName);
            System.out.println(destination + "\\" + actualFileName);
            boolean successful = file.renameTo(newFile);

            System.out.println(successful ? "File was successfully moved." : "File moving was unsuccessful. Please try again.");

            // ensuring there is enough time for either of the above messages to be read
            Main.delay();
            if (successful) {
                return newFile;
            } else {
                return null;
            }
        }
    }

    public static boolean deleteFile(String actualFileName, File file) {
        System.out.println("deleting file");

        boolean successful = file.delete();

        System.out.println(successful ? "Successfully deleted " + actualFileName : actualFileName + " could not be deleted. Please try again.");

        // ensuring there is enough time for either of the above messages to be read
        Main.delay();
        return successful;
    }

    public static void editTextFile(String actualFileName, String fullFileName) {
        if (!actualFileName.substring(actualFileName.lastIndexOf('.') + 1).equals("txt")) { // checking if we are working on a text file
            System.out.println("This option is only compatible with text files.");
            Main.delay();
            return;
        }

        boolean exited = false;

        while (!exited) {
            Open.openFile(fullFileName, true);

            System.out.println("\nNow please input one of the following: ");
            System.out.println("Input '1' if you would like to write a new line to the file");
            System.out.println("Input '2' if you would like to remove the previous line from the file");
            System.out.println("Input '3' if you would like to add a line break to the file");
            System.out.println("Input '4' if you would like to clear the file");
            System.out.println("Input '0' if you would like to stop editing the file");
            System.out.println("\nPlease input an option: ");

            String input = Main.getScanner().nextLine().trim();
            //Main.getScanner().nextLine();

            try {
                switch (input) {
                    case "1":
                        System.out.print("Enter your line to add: ");
                        input = Main.getScanner().nextLine(); // not trimmed in case the user wants leading spaces for some reason
                        // try-with-resources (special syntax, works with objects that have AutoCloseable, catch block optional, seems pretty neat)
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullFileName, true))) {
                            writer.append(input);
                            writer.newLine();
                            writer.flush();
                        }
                        break;
                    case "2":
                        System.out.println("remove");
                        break;
                    case "3":
                        System.out.println("Adding a line break...");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullFileName, true))) {
                            writer.newLine();
                            writer.flush();
                        }
                        Main.delay();
                        break;
                    case "4":
                        System.out.print("This option will delete all of the contents of the selected file. Please enter 'Y' to confirm that you want to clear the file, or any other character to cancel the operation: ");
                        input = Main.getScanner().nextLine().trim();
                        if (input.equalsIgnoreCase("y")) {
                            System.out.println("\nClearing the file contents");
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullFileName))) {
                            } // opening FileWriter without append mode, automatically clears the file's contents
                            Main.delay();
                        }
                        break;
                    case "0":
                        exited = true;
                        break;
                    default:
                        System.out.println("Please input an option from the list above, try again.");
                        Main.delay();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void printSubOptions(String message, String fileName) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        System.out.println("Now modifying the " + message + ": " + fileName + "\n");
        System.out.println("Please input one of the following: ");
        System.out.println("Input '1' if you would like to rename this file");
        System.out.println("Input '2' if you would like to move this file");
        System.out.println("Input '3' if you would like to delete this file");
        System.out.println("Input '4' if you would like to edit the contents of this file (text files only)");
        System.out.println("Input '0' if you would like to exit from this menu");
        System.out.println("\nPlease input an option: ");
    }
}


