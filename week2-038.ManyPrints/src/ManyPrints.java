
import java.util.Scanner;

public class ManyPrints {
    // NOTE: do not change the method definition, e.g. add parameters to method
    public static void printText() {
        // Write your code here
        System.out.println("In the beginning there were the swamp, the hoe and Java.");
    }

    public static void main(String[] args) {
        // ask the user how many times the text should be printed
        // use the while structure to call the printText method several times
        Scanner reader = new Scanner(System.in);
        
        int num = 0;
        
        int i = 0;
        
        System.out.println("How many?");
        
        num = reader.nextInt();
        
        while(i<num){
            printText();
            i++;
        }
        
    }
}