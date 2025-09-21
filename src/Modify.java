import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Modify {
    public static boolean modifyFile(String fileName, boolean isAbsolute) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        String fullFileName;
        String message;

        if (isAbsolute) {
            fullFileName = fileName;
        } else {
            fullFileName = Main.getDefaultPath() + fileName;
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
            printSubOptions(file);
            input = Main.getScanner().nextLine().trim();

            // need a way to exit from these options like typing something special "~", etc
            switch (input) { // renaming file/folder
                case "1":
                    file = renameFile(file);
                    break;
                case "2": // moving a file
                    File newFile = moveFile(file);
                    if (newFile != null) {
                        file = newFile;
                    }
                    break;
                case "3": // deleting a file
                    if (deleteFile(file)) {return true; }
                    break;
                case "4": // editing the contents of a text file
                    editTextFile(file);
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

    public static File renameFile(File file) {
        String fileExtension = "folder";

        int posOfDot = getActualFileName(file).lastIndexOf('.');
        if (posOfDot != -1) { // if the file name has a dot, then we must be handling a file and not a folder
            fileExtension = getActualFileName(file).substring(posOfDot + 1);
        }

        String path;
        path = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('\\') + 1); // getting the path so we can append the inputted new name of the file to the original path

        String newFileName = path + UserInput.getFileNameInput();

        String newFileExtension = "folder";
        posOfDot = newFileName.lastIndexOf('.');
        if (posOfDot != -1) {  // if the file name has a dot, then we must be handling a file and not a folder
            newFileExtension = newFileName.substring(posOfDot + 1);
        }

        boolean successful = false;

        File renamedFile = new File(newFileName);
        if (fileExtension.equals("folder") == newFileExtension.equals("folder")) { // either we are renaming a folder to a folder, or renaming a file to a file
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

    public static File moveFile(File file) {
        String destination;

        int posOfDot = getActualFileName(file).indexOf('.');
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
                    destination = DirectoryNavigator.navigateDirs("move", file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('\\') + 1));
                    break;
                default:
                    System.out.println("Please input an option from the list above, try again.");
                    Main.delay();
                    continue; // causes loop
            }

            File newFile = new File(destination + "\\" + getActualFileName(file));
            System.out.println(destination + "\\" + getActualFileName(file));
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

    public static boolean deleteFile(File file) {
        System.out.println("deleting file");

        boolean successful = file.delete();

        System.out.println(successful ? "Successfully deleted " + getActualFileName(file) : getActualFileName(file) + " could not be deleted. Please try again.");

        // ensuring there is enough time for either of the above messages to be read
        Main.delay();
        return successful;
    }

    public static void editTextFile(File file) {
        if (!getActualFileName(file).substring(getActualFileName(file).lastIndexOf('.') + 1).equals("txt")) { // checking if we are working on a text file
            System.out.println("This option is only compatible with text files.");
            Main.delay();
            return;
        }

        boolean exited = false;

        while (!exited) {
            Open.openFile(file.getAbsolutePath(), true);

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
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true))) {
                            writer.append(input);
                            writer.newLine();
                            writer.flush();
                        }
                        Main.delay();
                        break;
                    case "2":
                        System.out.println("Removing last line from the file...");
                        Scanner reader = new Scanner(file);
                        ArrayList<String> lines = new ArrayList<>();

                        while (reader.hasNextLine()) {
                            lines.add(reader.nextLine());
                        }

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
                            for (int i = 0; i < lines.size() - 1; i++) {
                                writer.write(lines.get(i));
                                writer.newLine();
                            }
                        }

                        reader.close();
                        Main.delay();
                        break;
                    case "3":
                        System.out.println("Adding a line break...");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true))) {
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
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
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

    public static String getActualFileName(File file) {
        return file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('\\') + 1);
    }

    public static void printSubOptions(File file) {
        String message = getActualFileName(file).indexOf('.') != -1 ? "file" : "folder";

        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
        System.out.println("Now modifying the " + message + ": " + getActualFileName(file) + " located at: " + file.getAbsolutePath() + "\n");
        System.out.println("Please input one of the following: ");
        System.out.println("Input '1' if you would like to rename this file");
        System.out.println("Input '2' if you would like to move this file");
        System.out.println("Input '3' if you would like to delete this file");
        System.out.println("Input '4' if you would like to edit the contents of this file (text files only)");
        System.out.println("Input '0' if you would like to exit from this menu");
        System.out.println("\nPlease input an option: ");
    }
}


