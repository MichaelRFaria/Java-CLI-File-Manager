import java.io.File;
import java.io.IOException;

public class Create {

        /*
    TODO
        when creating a file in default/absolute go back to sub menu after a few seconds
     */

    public static void createFile(String fileName, boolean isAbsolute) {
        String path = "";

        /* when using the file navigator to perform an action we take the file name as an absolute path,
        but set isAbsolute to false, so that we can call getFileNameInput instead of getAbsolutePathInput,
         ensuring we use the correct input checking, an alternative to this would be passing the current directory into getAbsolutePathInput. */

        if (!isAbsolute && fileName.indexOf('\\') == -1) {
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

        } catch (IOException e) {
            System.out.println("An error occurred:");
            System.out.println(e.getMessage()+"\n");
            e.printStackTrace();
        }
    }
}
