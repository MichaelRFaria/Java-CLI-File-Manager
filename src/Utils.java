import java.io.File;

public class Utils {
    /**
     * Helper method to parse a {@code String} input, taken from the {@code Scanner}, into an int, so it can be worked on.
     * Only accepts positive integers.
     * On an invalid input provides an appropriate error message.
     *
     * @param input the {@code String} input you want to parse into an int.
     * @return either a positive integer when successful or -1 on failure.
     */
    public static int handleParsingInput(String input) {
        /* all inputs in this program are retrieved using nextLine() in order to prevent an input like "1 1 1" from being processed as separate tokens.
         * additionally it prevents the need of a try-catch block for InputMismatchException, if using nextInt.
         * however if we want to work on the value, we must parse it. */

        int parsedVal;

        try {
            parsedVal = Integer.parseInt(input);

            if (parsedVal <= 0) {
                System.out.println("Please enter a positive integer.");
                Utils.delay(1500);
                // we return -1, which the caller will evaluate and handle the failure
                return -1;
            } else {
                return parsedVal;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter an integer.");
            Utils.delay(1500);
            // we return -1, which the caller will evaluate and handle the failure
            return -1;
        }
    }

    /**
     * This method creates a {@code Thread} that sleeps {@code Time} milliseconds.
     * Used to create a window of time when printed messages can be read by the user
     * or a window of time between an input and a result for a smoother console, before the program continues.
     *
     * @param time how long to delay
     */
    public static void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

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
            delay(1500);
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
