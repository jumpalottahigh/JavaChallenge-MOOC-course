
import java.util.Scanner;


public class UpToCertainNumber {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        
        int num,i = 1;
        // Write your code here
        System.out.println("Up to what number? ");
        num = reader.nextInt();
        
        while (i<=num) {
            System.out.println(i);
            i++;
        }
        
    }
}
