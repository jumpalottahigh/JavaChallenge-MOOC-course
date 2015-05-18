
import java.util.Scanner;

public class Adder {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter number 1: ");
        int a = Integer.parseInt(reader.nextLine());
        System.out.println("Enter number 2: ");
        int b = Integer.parseInt(reader.nextLine());
        int result = a + b;
        System.out.println("Sum of the numbers: " + result);
        // Implement your program here. Remember to ask the input from user
    }
}
