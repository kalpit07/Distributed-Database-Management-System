package userinterface;
import java.util.Scanner;

public class Menu
{
    public static void mainMenu()
    {
        System.out.println("Please choose one of the following options:");
        System.out.println("1. REGISTER NEW USER");
        System.out.println("2. LOGIN USER");
        System.out.println("3. EXIT");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();

        switch (option)
        {
            case 1:
            {
                Registration.registerUser();
                break;
            }

            case 2:
            {
                Login.loginUser();
                break;
            }

            case 3:
            {
                System.out.println("Exiting the application...");
                System.exit(0);
            }

            default:
            {
                System.out.println("You entered an invalid input. Please choose from one of the below options!");
                Menu.mainMenu();
            }
        }
    }

    public static int loginMenu(String username)
    {
        System.out.println("Please choose one of the following options:");
        System.out.println("1. WRITE QUERIES");
        System.out.println("2. EXPORT");
        System.out.println("3. DATA MODEL");
        System.out.println("3. ANALYTICS");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }
}
