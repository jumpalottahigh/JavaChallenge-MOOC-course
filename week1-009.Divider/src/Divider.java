
import java.util.Scanner;

public class Divider {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        
        // Implement your program here. Remember to ask the input from user.
        System.out.println("Type a numer: ");
        double a = Double.parseDouble(reader.nextLine());
        System.out.println("Type another number: ");
        double b = Double.parseDouble(reader.nextLine());
        double result = a/b;
        System.out.println("Division: "+a+"/"+b+"="+result);
    }
}
