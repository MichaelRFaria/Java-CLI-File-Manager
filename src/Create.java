import java.io.File;
import java.io.IOException;

public class Create {
    /**
     * The method to create a file.
     *
     * @param fileName the name of the file/folder you want to create, either given by a relative file path (lone file name),
     *                 or an absolute file path, differentiated by the {@code isAbsolute} parameter.
     * @param isAbsolute whether you have inputted a relative file path (relative to the program's default directory "output") or an absolute file path.
     * @return Whether the file was successfully created or not (either already exists, or operation error).
     */
    public static boolean createFile(String fileName, boolean isAbsolute) {
        String fullFileName;

        // if-conditional to figure out if we need to prepend the default directory path
        if (isAbsolute) {
            // if isAbsolute is true, then we already have the full file path
            fullFileName = fileName;
        } else {
            // if isAbsolute is false, then we must be working in the program's default directory "output", so we prepend the default directory path
            fullFileName = Main.getDefaultPath() + fileName;
        }

        try {
            boolean created;
            File file = new File(fullFileName);

            /* this program assumes if folders don't have a '.' and files have a '.' (the file extension)
            * figuring out if we are creating a file or a folder */

            // if there is a ',' then we must be creating a file
            if (fullFileName.contains(".")) {
                created = file.createNewFile();
            // if there is no '.' then we must be creating a folder
            } else {
                created = file.mkdir();
            }

            // printing success/error message
            if (created) {
                System.out.println("Successfully created " + file.getName());
            } else {
                System.out.println(file.getName() + " already exists, please try again.");
            }

            // ensuring there is enough time for the above message to be read
            Utils.delay(1500);

            return created;
        } catch (IOException e) {
            System.out.println("An error occurred:");
            System.out.println(e.getMessage()+"\n");
            e.printStackTrace();
            return false;
        }
    }
}
