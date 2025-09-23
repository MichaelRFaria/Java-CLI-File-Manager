import java.io.File;

public class FileUtils {
    public static File createFileVarIfExists(String fileName, boolean isAbsolute) {
        String fullFileName;

        if (isAbsolute) {
            fullFileName = fileName;
        } else {
            fullFileName = Main.getDefaultPath() + fileName;
        }

        String message = fileName.contains(".") ? "file" : "folder";

        File file = new File(fullFileName);
        if (!file.exists()) {
            System.out.println("The " + message + " was not found, please try again.");
            Main.delay();
            return null;
        }
        return file;
    }

    public static String getActualFileName(File file) {
        return file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('\\') + 1);
    }
}
