
import java.util.Scanner;

public class Usernames {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        
        System.out.println("Type your username: ");
        String user = reader.nextLine();
        System.out.println("Type your password: ");
        String pass = reader.nextLine();
        
        if (user.equals("alex") && pass.equals("mightyducks") || user.equals("emily") && pass.equals("cat"))
            System.out.println("You are now logged into the system!");
        else
            System.out.println("Your username or password was invalid!");
    }
}
