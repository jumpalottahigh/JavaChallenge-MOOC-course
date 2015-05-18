
import java.util.Scanner;

public class AgeOfMajority {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        // Type your program here
        System.out.println("How old are you? ");
        int a = Integer.parseInt(reader.nextLine());
        
        if (a<18)
            System.out.println("You have not reached the age of majority yet!");
        else
            System.out.println("You have reached the age of majority!");
    }
}
