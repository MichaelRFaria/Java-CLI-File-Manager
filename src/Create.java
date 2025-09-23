import java.io.File;
import java.io.IOException;

public class Create {
    public static boolean createFile(String fileName, boolean isAbsolute) {
        String fullFileName;

        if (isAbsolute) {
            fullFileName = fileName;
        } else {
            fullFileName = Main.getDefaultPath() + fileName;
        }

        System.out.println("creating file/folder");

        try {
            boolean created;
            File file = new File(fullFileName);

            if (fullFileName.contains(".")) { // if there is a ',' then we must be creating a file
                created = file.createNewFile();
            } else { // if there is no '.' then we must be creating a folder
                created = file.mkdir();
            }

            // printing success/error message
            if (created) {
                System.out.println("successfully created " + file.getName());
            } else {
                System.out.println(file.getName() + " already exists");
            }

            // ensuring there is enough time for the above message to be read
            Main.delay();

            return created;

        } catch (IOException e) {
            System.out.println("An error occurred:");
            System.out.println(e.getMessage()+"\n");
            e.printStackTrace();
            return false;
        }
    }
}
