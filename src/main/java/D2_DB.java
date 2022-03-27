import userinterface.*;

import java.io.File;

public class D2_DB
{
    public static void createLocalMetadataFile()
    {
        try
        {
            File fileWriter = new File(System.getProperty("user.dir") + "/Metadata_Local.txt");
            if(!fileWriter.exists() && !fileWriter.isFile())
            {
                boolean done = fileWriter.createNewFile();
                if (done)
                {
                    System.out.println();
                    System.out.println("Metadata_Local file is at " + fileWriter.getCanonicalPath());
                    System.out.println();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        System.out.println("Welcome to D2_DB application..!");

        UserProfile.createUserProfileFile();
        createLocalMetadataFile();
        Menu.mainMenu();
    }
}
