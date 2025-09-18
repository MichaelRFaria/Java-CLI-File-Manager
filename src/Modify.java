import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Modify {
    public static boolean modifyFile(String fileName, boolean isAbsolute) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        String path = "";
        String actualFileName;
        String message;

        if (isAbsolute) {
            actualFileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
        } else {
            actualFileName = fileName;
            path = Main.getDefaultPath();
        }

        message = fileName.indexOf('.') != -1 ? "file" : "folder";

        File file = new File(path + fileName);
        if (!file.exists()) {
            System.out.println(message + " was not found, please try again.");
            Main.delay();
            return false;
        }

        int posOfDot;
        boolean successful = false;
        String input;
        String fullFileName = path + fileName;

        while (true) {
            System.out.println("ACTUAL FILE NAME: " + actualFileName);
            System.out.println("FULL FILE NAME (PATH): " + fullFileName);
            try {
                printSubOptions(message, actualFileName);
                input = Main.getScanner().nextLine().trim();

                // need a way to exit from these options like typing something special "~", etc
                switch (input) { // renaming file/folder
                    case "1":
                        String fileExtension = "folder";

                        posOfDot = actualFileName.lastIndexOf('.');
                        if (posOfDot != -1) {
                            fileExtension = actualFileName.substring(posOfDot + 1);
                        }

                        String tempPath = path;

                        if (fullFileName.indexOf('\\') != -1) {
                            int posOfSlash = fullFileName.lastIndexOf('\\');
                            tempPath = fullFileName.substring(0, posOfSlash + 1);
                        }

                        String newFileName = tempPath + UserInput.getFileNameInput();

                        System.out.println("Path: " + tempPath);
                        System.out.println("filename after path added: " + fullFileName);
                        System.out.println("original filename: " + actualFileName);
                        System.out.println("new filename: " + newFileName);

                        String newFileExtension = "folder";
                        posOfDot = newFileName.lastIndexOf('.');
                        if (posOfDot != -1) {
                            newFileExtension = newFileName.substring(posOfDot + 1);
                        }

                        if (fileExtension.equals("folder") == newFileExtension.equals("folder")) { // either we are renaming a folder to a folder, or renaming a file to a file
                            //System.out.println(file.getAbsolutePath());
                            File newFile = new File(newFileName);
                            //System.out.println(newFile.getAbsolutePath());
                            successful = file.renameTo(newFile);
                            //System.out.println(successful);
                        }

                        message = fileExtension.equals("folder") ? "Folder" : "File";

                        System.out.println(successful ? message + " was successfully renamed." : message + " renaming was unsuccessful. Please try again.");

                        Main.delay();
                        if (successful) {
                            return true;
                        }
                        break;
                    case "2": // moving a file
                        String destination;
                        posOfDot = actualFileName.indexOf('.');
                        if (posOfDot == -1) { // we can only move files, if there is not a dot in the file name, then it must be a folder (technically folders can have dots in their names in windows, but I don't care)
                            System.out.println("Cannot perform the move operation on a folder. Please try with a file instead.");
                            Main.delay();
                            break;
                        }

                        while (true) { // breaks when moving is successful, where it will return true.
                            System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

                            System.out.println("Please input one of the following: ");
                            System.out.println("Input '1' if you would like to enter the destination via an absolute path");
                            System.out.println("Input '2' if you would like to enter the destination via navigating the directories");
                            System.out.println("\nPlease input an option: ");

                            input = Main.getScanner().nextLine().trim();

                            switch (input) {
                                case "1":
                                    destination = UserInput.getAbsolutePathInput();

                                    File temp = new File(destination);
                                    if (temp.isDirectory()) {
                                        break;
                                    } else {
                                        System.out.println("This directory does not exist, please try again.");
                                        Main.delay();
                                        continue;
                                    }
                                case "2":
                                    destination = DirectoryNavigator.navigateDirs("move");
                                    break;
                                default:
                                    System.out.println("Please input an option from the list above, try again.");
                                    Main.delay();
                                    continue;
                            }


                            File newFile = new File(destination + "\\" + actualFileName);
                            System.out.println(destination + "\\" + actualFileName);
                            successful = file.renameTo(newFile);

                            System.out.println(successful ? "File was successfully moved." : "File moving was unsuccessful. Please try again.");

                            // ensuring there is enough time for either of the above messages to be read
                            Main.delay();
                            if (successful) {
                                return true;
                            }
                            break;
                        }
                        break;
                    case "3": // deleting a file
                        System.out.println("deleting file");

                        successful = file.delete();

                        System.out.println(successful ? "Successfully deleted " + actualFileName : actualFileName + " could not be deleted. Please try again.");

                        // ensuring there is enough time for either of the above messages to be read
                        Main.delay();
                        if (successful) {
                            return true;
                        }
                        break;
                    case "4": // editing the contents of a text file
                        if (!actualFileName.substring(actualFileName.lastIndexOf('.') + 1).equals("txt")) { // checking if we are working on a text file
                            System.out.println("This option is only compatible with text files.");
                            Main.delay();
                            break;
                        }

                        boolean exited = false;

                        while (!exited) {
                            Open.openFile(fullFileName, isAbsolute);

                            System.out.println("\nNow please input one of the following: ");
                            System.out.println("Input '1' if you would like to write a new line to the file");
                            System.out.println("Input '2' if you would like to remove the previous line from the file");
                            System.out.println("Input '3' if you would like to add a line break to the file");
                            System.out.println("Input '4' if you would like to clear the file");
                            System.out.println("Input '0' if you would like to stop editing the file");
                            System.out.println("\nPlease input an option: ");

                            input = Main.getScanner().nextLine().trim();
                            //Main.getScanner().nextLine();

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
                                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullFileName))) {} // opening FileWriter without append mode, automatically clears the file's contents
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
                        }
                        break;
                    case "0": // exiting this menu
                        return true;
                    default:
                        System.out.println("Please input an option from the list above, try again.");
                        Main.delay();
                        break;

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Something went wrong with the writer's operations.");
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


