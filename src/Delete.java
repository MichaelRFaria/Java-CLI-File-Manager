import java.io.File;

public class Delete {

    public static boolean deleteFile(String fileName, boolean isAbsolute) {
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

        System.out.println("deleting file");

        boolean deleted = file.delete();

        if (deleted) {
            System.out.println("Successfully deleted " + fileName);
        } else {
            System.out.println(fileName + " could not be deleted. Please try again.");
        }

        // ensuring there is enough time for either of the above messages to be read
        Main.delay();

        return deleted;
    }
}
