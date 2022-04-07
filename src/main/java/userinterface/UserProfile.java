package userinterface;
import java.io.File;

public class UserProfile
{
    public static void createUserProfileFile()
    {
        try
        {
            File fileWriter = new File(System.getProperty("user.dir") + "/User_Profile.txt");
            if(!fileWriter.exists() && !fileWriter.isFile())
            {
                boolean done = fileWriter.createNewFile();
                if (done)
                {
                    System.out.println();
                    System.out.println("User_Profile file is at " + fileWriter.getCanonicalPath());
                    System.out.println();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
