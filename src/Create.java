import java.io.File;
import java.io.IOException;

public class Create {

        /*
    TODO
        when creating a file in default/absolute go back to sub menu after a few seconds
     */

    public static boolean createFile(String fileName, boolean isAbsolute) {
        String path = "";

        if (!isAbsolute) {
            path = Main.getDefaultPath();
        }

        System.out.println("creating file/folder");

        try {
            boolean created;
            File file = new File(path + fileName);

            if ((path + fileName).indexOf('.') == -1) { // if there is no '.' then we must be creating a folder
                created = file.mkdir();
            } else { // if there is a ',' then we must be creating a file
                created = file.createNewFile();
            }

            // printing success/error message
            if (created) {
                System.out.println("successfully created " + file.getName());
            } else {
                System.out.println(file.getName() + " already exists");
            }

            // ensuring there is enough time for the above message to be read
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("An error occurred:");
                System.out.println(e.getMessage()+"\n");
                e.printStackTrace();
            }

            if (created) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            System.out.println("An error occurred:");
            System.out.println(e.getMessage()+"\n");
            e.printStackTrace();
            return false;
        }
    }
}
