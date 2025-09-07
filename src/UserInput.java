import java.io.File;

public class UserInput {

    // both methods below can be merged into one, but it will lead to nested if-statements within the loop, and a param
    /*
    TODO
        implement actual name/path checking rules. scanner blocks on empty input, so cant if-else print an error in the while loops, for that
        but could still check for illegal characters etc
        when opening/running files, this should say "Please input a valid file name" (should not say folder)
     */
    public static String getFileNameInput() {

        while (true) {
            System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
            System.out.println("Please input a valid file/folder name: ");

            String input = Main.getScanner().next();
            // scanner doesnt except empty inputs this is stupid until actual name rules checking rules are implemented
            if (!input.isEmpty()) { // name checking rules go here
                return input;
            } else {
                System.out.println("File name input is invalid. Please try again.");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static String getAbsolutePathInput() {

        while (true) {
            System.out.println(System.lineSeparator().repeat(50)); // clears console in a way that is not environment-dependent
            System.out.println("Please input a valid absolute path: ");

            String input = Main.getScanner().next();
            File temp = new File(input);
            if (temp.isAbsolute()) { // path checking rules go here
                return input;
            } else {
                System.out.println("File path is entered incorrectly. Please try again.");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
