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
                mainMenu();
                break;
            }

            case 2:
            {
                Login.loginUser();
                mainMenu();
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
                mainMenu();
            }
        }
    }

    public static void loginMenu(String username)
    {
        System.out.println("\nPlease choose one of the following options:");
        System.out.println("1. WRITE QUERIES");
        System.out.println("2. EXPORT");
        System.out.println("3. DATA MODEL");
        System.out.println("4. ANALYTICS");
        System.out.println("5. SHOW LOGS");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();

        switch (option)
        {
            case 1:
            {

            }

            case 2:
            {

            }

            case 3:
            {

            }

            case 4:
            {

            }

            case 5:
            {

            }

            default:
            {
                System.out.println("You entered an invalid input. Please choose from one of the below options!");
                loginMenu(username);
            }
        }
    }
}
