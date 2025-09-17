import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;

public class Modify {
    /* modifications could include:
        renaming files (DONE)
        editing text files
        moving files (DONE)
        copying files
     */

    public static boolean modifyFile(String fileName, boolean isAbsolute) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        String path = "";
        String originalFileName = "";
        String message;

        if (!isAbsolute) { // original file name is needed to append to the destination directory, when moving the file
            originalFileName = fileName;
        } else {
            originalFileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
        }

        /* when using the file navigator to perform an action we take the file name as an absolute path,
        but set isAbsolute to false, so that we can call getFileNameInput instead of getAbsolutePathInput,
         ensuring we use the correct input checking, an alternative to this would be passing the current directory into getAbsolutePathInput. */

        if (!isAbsolute && fileName.indexOf('\\') == -1) {
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

        while (true) {
            try {
                printSubOptions(message, fileName);
                int num = Main.getScanner().nextInt();

                // need a way to exit from these options like typing something special "~", etc
                switch (num) { // renaming file/folder
                    case 1:
                        String fileExtension = "folder";

                        posOfDot = fileName.lastIndexOf('.');
                        if (posOfDot != -1) {
                            fileExtension = fileName.substring(posOfDot + 1);
                        }

                        fileName = path + fileName;

                        if (fileName.indexOf('\\') != -1) {
                            int posOfSlash = fileName.lastIndexOf('\\');
                            path = fileName.substring(0, posOfSlash + 1);
                        }

                        String newFileName = path + UserInput.getFileNameInput();

//                        System.out.println("Path: " + path);
//                        System.out.println("filename after path added: " + fileName);
//                        System.out.println("orginal filename: " + originalFileName);
//                        System.out.println("newfilename: " + newFileName);

                        String newFileExtension = "folder";
                        posOfDot = newFileName.indexOf('.');
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
                    case 2: // moving a file
                        String destination = null;
                        posOfDot = fileName.indexOf('.');
                        if (posOfDot == -1) { // we can only move files, if there is not a dot in the file name, then it must be a folder
                            System.out.println("Cannot perform the move operation on a folder. Please try with a file instead.");
                            Main.delay();
                            break;
                        }

                        fileName = path + fileName;

                        while (true) { // breaks when moving is successful, where it will return true.
                            System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

                            System.out.println("Please input one of the following: ");
                            System.out.println("Input '1' if you would like to enter the destination via an absolute path");
                            System.out.println("Input '2' if you would like to enter the destination via navigating the directories");
                            System.out.println("\nPlease input an option: ");

                            try {
                                int input = Main.getScanner().nextInt();
                                switch (input) {
                                    case 1:
                                        destination = UserInput.getAbsolutePathInput();

                                        File temp = new File(destination);
                                        if (temp.isDirectory()) {
                                            break;
                                        } else {
                                            System.out.println("This directory does not exist, please try again.");
                                            Main.delay();
                                            continue;
                                        }
                                    case 2:
                                        destination = DirectoryNavigator.navigateDirs("move");
                                        break;
                                    default:
                                        System.out.println("Please input an option from the list above, try again.");
                                        Main.delay();
                                        continue;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please input a valid option, try again.");
                                Main.delay();
                                Main.getScanner().next();
                            }

                            File newFile = new File(destination + "\\" + originalFileName);
                            System.out.println(destination + "\\" + originalFileName);
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
                    case 3: // deleting a file
                        System.out.println("deleting file");

                        successful = file.delete();

                        System.out.println(successful ? "Successfully deleted " + fileName : fileName + " could not be deleted. Please try again.");

                        // ensuring there is enough time for either of the above messages to be read
                        Main.delay();
                        if (successful) {
                            return true;
                        }
                        break;
                    case 4: // editing the contents of a text file
                        /*
                        todo
                             must read text file,
                             allow the user to:
                             clear the file
                             write new lines to the file (done)
                             delete one line of the file
                             add new lines (actual new lines \n)

                         */
                        if (!fileName.substring(fileName.lastIndexOf('.') + 1).equals("txt")) { // checking if we are working on a text file
                            System.out.println("This option is only compatible with text files.");
                            Main.delay();
                            break;
                        }

                        boolean exited = false;
                        fileName = path + fileName;

                        while (!exited) {
                            Open.openFile(fileName, isAbsolute);

                            System.out.println("\nNow please input one of the following: ");
                            System.out.println("Input '1' if you would like to write a new line to the file");
                            System.out.println("Input '2' if you would like to remove the previous line from the file");
                            System.out.println("Input '3' if you would like to add a line break to the file");
                            System.out.println("Input '4' if you would like to clear the file");
                            System.out.println("Input '0' if you would like to stop editing the file");
                            System.out.println("\nPlease input an option: ");

                            num = Main.getScanner().nextInt();
                            Main.getScanner().nextLine();

                            switch (num) {
                                case 1:
                                    System.out.print("Enter your line to add: ");
                                    String line = Main.getScanner().nextLine();
                                    // try-with-resources (special syntax, works with objects that have AutoCloseable, catch block optional, seems pretty neat)
                                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                                        writer.append(line);
                                        writer.newLine();
                                        writer.flush();
                                    }
                                    break;
                                case 2:
                                    System.out.println("remove");
                                    break;
                                case 3:
                                    System.out.println("line break");
                                    break;
                                case 4:
                                    System.out.println("clear");
                                    break;
                                case 0:
                                    exited = true;
                                    break;
                                default:
                                    System.out.println("Please input an option from the list above, try again.");
                                    Main.delay();
                                    break;
                            }
                        }
                        break;
                    case 0: // exiting this menu
                        return true;
                    default:
                        System.out.println("Please input an option from the list above, try again.");
                        Main.delay();
                        break;

                }
            } catch (InputMismatchException e) {
                System.out.println("Please input an integer, try again.");
                Main.delay();
                Main.getScanner().next();
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


