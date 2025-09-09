import java.io.File;

public class Delete {

    public static boolean deleteFile(String fileName, boolean isAbsolute) {
        String path = "";

        if (!isAbsolute) {
            path = Main.getDefaultPath();
        }

        File file = new File(path + fileName);
        if (!file.exists()) {
            System.out.println("File was not found, please try again.");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
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
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println("An error occurred:");
            System.out.println(e.getMessage()+"\n");
            e.printStackTrace();
        }

        return deleted;
    }
}
