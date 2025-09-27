import java.io.File;

public class FileUtils {

    /**
     * This method creates a {@code File} object for a file that exists at a given location.
     *
     * @param fileName either the relative file path, or an absolute file path, differentiated by the {@code isAbsolute} parameter.
     * @param isAbsolute whether you have inputted a relative file path (relative to the program's default directory "output") or an absolute file path.
     * @return The created {@code File} object if the file exists, otherwise {@code null} .
     */
    public static File createFileVarIfExists(String fileName, boolean isAbsolute) {
        String fullFileName;

        // if-conditional to figure out if we need to prepend the default directory path
        if (isAbsolute) {
            // if isAbsolute is true, then we already have the full file path
            fullFileName = fileName;
        } else {
            // if isAbsolute is false, then we must be working in the program's default directory "output", so we prepend the default directory path
            fullFileName = Main.getDefaultPath() + fileName;
        }

        /* this program assumes if folders don't have a '.' and files have a '.' (the file extension)
        * figuring out the correct null for an error message, if unsuccessful */
        String message = fileName.contains(".") ? "file" : "folder";

        File file = new File(fullFileName);
        if (!file.exists()) {
            System.out.println("The " + message + " was not found, please try again.");
            Main.delay();
            return null;
        }
        return file;
    }

    /**
     * This method obtains a {@code File} object's "actual name" by subtracting the file's path from the root, from the actual file.
     *
     * @param file the file object to get the actual file name from.
     * @return The file's name.
     */
    public static String getActualFileName(File file) {
        return file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('\\') + 1);
    }
}
