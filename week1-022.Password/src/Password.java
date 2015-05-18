
import java.util.Scanner;

public class Password {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        String password = "carrot"; // Use carrot as password when running tests.

        String input = null;
        // Write your code here
        
        
        
        while(!("carrot".equals(input))){
            System.out.println("Type the password: ");
            input = reader.nextLine();
            System.out.println("Wrong!");
        }
        
        System.out.println("Right!");
        System.out.println("The secret is: jryy qbar!");
        
    }
}
