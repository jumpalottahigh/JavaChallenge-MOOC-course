import java.util.Scanner;

public class Factorial {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        
        System.out.println("Type a number: ");
        int i = reader.nextInt();
        
        int num = 1;
        int factorial = 1;
        
        
        while (num <= i) {
            factorial *=num;
            num++;
        }
        
        System.out.println("Factorial is " + factorial);
    }
}
