import java.io.File;

public class Modify {
    /* modifications could include:
        renaming files
        editing text files
        moving files
     */

    public static boolean modifyFile(String fileName, boolean isAbsolute) {
        System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent

        String path = "";
        String message;

        message = fileName.indexOf('.') != -1 ? "file" : "folder";

        if (isAbsolute) {
            System.out.println("Now modifying the " + message + " at: " + fileName + "\n");
        } else {
            System.out.println("Now modifying the " + message + ": " + fileName + "\n");
            path = Main.getDefaultPath();
        }

        System.out.println("Please input one of the following: ");
        System.out.println("Input '1' if you would like to rename this file");
        System.out.println("\nPlease input an option: ");

        while (true) {
            String input = Main.getScanner().next();

            // need a way to exit from these options like typing something special "~", etc
            switch (input) {
                case "1":
                    String fileExtension = "folder";
                    int posOfDot = fileName.indexOf('.');
                    if (posOfDot != -1) {
                        fileExtension = fileName.substring(posOfDot + 1);
                    }

                    fileName = path + fileName;

                    // need to implement isAbsolute checking
                    if (isAbsolute) {
                        int posOfSlash = fileName.lastIndexOf('\\');
                        path = fileName.substring(0, posOfSlash + 1);
                    }

                    while (true) { // breaks when renaming is successful, where it will return true.

                        String newFileName = path + UserInput.getFileNameInput();

                        String newFileExtension = "folder";
                        posOfDot = newFileName.indexOf('.');
                        if (posOfDot != -1) {
                            newFileExtension = newFileName.substring(posOfDot + 1);
                        }

//                    System.out.println("original file name :" + fileName);
//                    System.out.println("original file ext :" + fileExtension);
//                    System.out.println("new file name :" + newFileName);
//                    System.out.println("new file ext :" + newFileExtension);

                        boolean successful = false;

                        if (fileExtension.equals("folder") == newFileExtension.equals("folder")) { // either we are renaming a folder to a folder, or renaming a file to a file
                            File oldFile = new File(fileName);
                            //System.out.println(oldFile.getAbsolutePath());
                            File newFile = new File(newFileName);
                            //System.out.println(newFile.getAbsolutePath());
                            successful = oldFile.renameTo(newFile);
                            //System.out.println(test);
                        }

                        message = fileExtension.equals("folder") ? "Folder" : "File";

                        System.out.println(successful ? message + " was successfully renamed." : message + " renaming was unsuccessful. Please try again.");

                        try {
                            Thread.sleep(1500);
                            if (successful) {
                                return true;
                            }
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
            }
        }
    }
}
