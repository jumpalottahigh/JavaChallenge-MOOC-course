import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        // implement your program here
        // do not put all to one method/class but rather design a proper structure to your program
       
        ArrayList<Integer> grades = new ArrayList<Integer>();
        ArrayList<String> stars = new ArrayList<String>();
          
        int buffer = 0;
        int count_1s = 0;
        int count_2s = 0;
        int count_3s = 0;
        int count_4s = 0;
        int count_5s = 0;
        int count_0s = 0;
        
        double accepted = 0.0;
        int sum = 0;
        
        System.out.println("Type the exam scores, -1 completes:");
        
        
        while(buffer != -1){
            buffer = reader.nextInt();
            
            if(buffer >=0 && buffer<=60){
                grades.add(buffer);
            }
        }
        
        System.out.println("Grade distribution:");
        
        for(int a : grades){
            if(a>0 && a<30)
                count_0s++;
            if(a>=30 && a<35)
                count_1s++;               
            if(a>=35 && a<40)
                count_2s++;
            if(a>=40 && a<45)
                count_3s++;
            if(a>=45 && a<50)
                count_4s++;
            if(a>=50 && a<=60)
                count_5s++;
        }
        
        for(int b : grades){
            if(b>=30){
                sum++;
            }
        }
        
        if(count_1s+count_2s+count_3s+count_4s+count_5s > 0)
            accepted = 100*sum/(grades.size());
        
        System.out.print("5: ");
        for(int i=0; i<count_5s; i++){System.out.print("*");}
        System.out.println("");
        System.out.print("4: ");
        for(int i=0; i<count_4s; i++){System.out.print("*");}
        System.out.println("");
        System.out.print("3: ");
        for(int i=0; i<count_3s; i++){System.out.print("*");}
        System.out.println("");
        System.out.print("2: ");
        for(int i=0; i<count_2s; i++){System.out.print("*");}
        System.out.println("");
        System.out.print("1: ");
        for(int i=0; i<count_1s; i++){System.out.print("*");}
        System.out.println("");
        System.out.print("0: ");
        for(int i=0; i<count_0s; i++){System.out.print("*");}
        
        System.out.println("");
        System.out.println("Acceptance percentage: " + accepted);
        
    }
}
