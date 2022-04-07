import java.io.File;
import java.io.IOException;

public class JarGenerate {
    public static void main(String []args)throws NullPointerException{
        System.out.println(args[0]);

        try {
            File myObj = new File("input.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
