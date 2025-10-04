import java.io.File;

public class UserInput {
    /**
     * This method prompts the user to enter a file/folder name, until a valid file name is given.
     *
     * @return The file name inputted by the user.
     */
    public static String getFileNameInput() {
        while (true) {
            System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
            System.out.println("Please input a valid file/folder name (or '*' to cancel): ");

            String input = Main.getScanner().nextLine().trim();

            /* a single '*' is the input required to cancel the current operation
            * what to do following a cancelled operation is handled in the individual classes themselves as it can vary */
            if (input.contains("*")) {
                return input;
            /* input must use valid characters for a Windows system file, must be shorter than the max file name length (255),
            * must not end with a leading space, and can contain a '.' (for file extensions), but must not end with it */
            } else if (input.matches("[a-zA-Z0-9_\\-.]+") && input.length() < 255 && !input.endsWith(" ") && !input.endsWith(".")) { // name checking rules go here
                return input;
            } else {
                System.out.println("File name input is invalid. Please try again.");
                Utils.delay();
            }
        }
    }

    /**
     * This method prompts the user to enter a file/folder absolute file path, until a valid file name is given.
     *
     * @return The absolute file path inputted by the user.
     */
    public static String getAbsolutePathInput() {
        while (true) {
            System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
            System.out.println("Please input a valid absolute path (or '*' to cancel): ");

            String input = Main.getScanner().nextLine().trim();

            /* a single '*' is the input required to cancel the current operation
             * what to do following a cancelled operation is handled in the individual classes themselves as it can vary */
            if (input.contains("*")) {
                return input;
            }

            // we can create a temporary File object to use the File.isAbsolute method to find out if the given input corresponds to a valid absolute file path
            File temp = new File(input);
            if (temp.isAbsolute()) { // path checking rules go here
                return input;
            } else {
                System.out.println("File path is entered incorrectly. Please try again.");
                Utils.delay();
            }
        }
    }
}
