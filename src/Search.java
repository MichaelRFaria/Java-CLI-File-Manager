import java.io.File;

public class Search {
    public static boolean searchForFile(String fileName, boolean isAbsolute) {
        File file = Main.createFileVarIfExists(fileName, isAbsolute);
        if (file == null) {return false; }

        return true; // remove
    }
}
