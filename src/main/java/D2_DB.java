import userinterface.*;

import java.io.File;

public class D2_DB
{
    static String BASE_DIRECTORY = "VM/";
    static String LOCAL_METADATA_FILE = "Local_Meta_Data.txt";
    static String GLOBAL_METADATA_FILE = "Global_Data_Dictionary.txt";

    public static void checkRootDirectory()
    {
        try
        {
            File root_directory = new File(BASE_DIRECTORY);
            if(!root_directory.exists())
            {
                root_directory.mkdirs();
            }
            checkMetadataFile();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void checkMetadataFile()
    {
        try
        {
            File local_metadata = new File(BASE_DIRECTORY + LOCAL_METADATA_FILE);
            File global_metadata = new File(BASE_DIRECTORY + GLOBAL_METADATA_FILE);

            if (!local_metadata.exists())
            {
                boolean done = local_metadata.createNewFile();
                if(done)
                {
                    System.out.println();
                    System.out.println("Local_Meta_Data file is at " + local_metadata.getCanonicalPath());
                }
            }

            if (!global_metadata.exists())
            {
                boolean done = global_metadata.createNewFile();
                if(done)
                {
                    System.out.println();
                    System.out.println("Global_Data_Dictionary file is at " + global_metadata.getCanonicalPath());
                }
            }

            UserProfile.createUserProfileFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String[] args)
    {
        System.out.println("Welcome to D2_DB application..!");
        checkRootDirectory();
        Menu.mainMenu();
    }
}
