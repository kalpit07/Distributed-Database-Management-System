package userinterface;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Registration
{
    public static void registerUser()
    {
        Scanner sc = new Scanner(System.in);
        boolean isThere;

        try
        {
            System.out.print("Please enter your username: ");
            String username = sc.nextLine();
            System.out.print("Please enter your password: ");
            String password = sc.nextLine();

            String hashedUsername = Hash.userID(username);
            String hashedPassword = Hash.password(password);

            isThere = checkUser(hashedUsername);

            if (isThere)
            {
                System.out.println("Sorry, this user is already present in the database. " +
                        "Try with a different username");
            }
            else
            {
                System.out.print("Please enter a security question: ");
                String securityQuestion = sc.nextLine();
                System.out.print("Please enter answer to your security question: ");
                String securityAnswer = sc.nextLine();

                FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/User_Profile.txt", true);
                writer.write(hashedUsername + "|" + hashedPassword + "|" + securityQuestion + "|" + securityAnswer + "\n");
                writer.close();
                System.out.println("User registered successfully.");
                System.out.println();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean checkUser(String hashedUsername) throws IOException
    {
        String line = "";
        boolean userAlreadyThere = false;
        BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") +
                "/User_Profile.txt"));
        while((line = br.readLine()) != null)
        {
            String[] allLines = line.split("\n");
            for(String everyLine : allLines)
            {
                String[] values = everyLine.split("\\|");
                if(values[0].equals(hashedUsername))
                {
                    return true;
                }
            }
        }
        return userAlreadyThere;
    }
}
