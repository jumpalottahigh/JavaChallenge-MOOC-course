import java.util.Scanner;

public class GreaterNumber {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.println("Type the first number: ");
        int a = Integer.parseInt(reader.nextLine());
        
        System.out.println("Type the second number: ");
        int b = Integer.parseInt(reader.nextLine());
        
        if(a==b)
            System.out.println("The numbers are equal!");
        else if (a>b)
                System.out.println("Greater number: "+a);
            else
                System.out.println("Greater number: "+b);
    }
}
