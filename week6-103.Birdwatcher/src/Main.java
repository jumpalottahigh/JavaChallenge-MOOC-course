
import java.util.ArrayList;
import java.util.Scanner;

public class Main {  

    public static void main(String[] args) {
    // implement your program here
    // do not put all to one method/class but rather design a proper structure to your program
        
    // Your program should use only one Scanner object, i.e., it is allowed to call 
    // new Scanner only once. If you need scanner in multiple places, you can pass it as parameter
         ArrayList<Bird> db = new ArrayList<Bird>();
        
        Scanner reader = new Scanner(System.in);
        
        while (true){
            System.out.println("?");
            String command = reader.nextLine();
            
            if(command.equals("Add")){
                System.out.println("Name: ");
                String name = reader.nextLine();
                System.out.println("Latin Name: ");
                String latName = reader.nextLine();
                Bird buffer = new Bird(name, latName);
                db.add(buffer);
                //return;
            }
            
            if (command.equals("Observation")){
                System.out.println("What was observed:?");
                String obs = reader.nextLine();
                
                //if(db.contains(obs))
                    //int i = db.indexOf(obs);
                
                for(Bird i : db){
                    if(i.getName().equals(obs)){
                        i.Observed();
                    } else {
                        System.out.println("Is not a bird!");
                    }
                }
                //return;
            }
            
            if (command.equals("Statistics")){
                for (Bird i : db) {
                    System.out.println(i);
                }
                //return;
            } 
            
            if (command.equals("Show")) {
                System.out.println("What? ");
                String nam = reader.nextLine();
                for(Bird i : db){
                    if(i.getName().equals(nam)){
                        System.out.println(i);
                    }
                }
                //return;
            }
            
            if (command.equals("Quit")){
                break;
            }
        }
    }

}
