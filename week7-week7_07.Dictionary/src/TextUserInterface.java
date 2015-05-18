
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class TextUserInterface {
    private Scanner reader;
    private Dictionary dict;
    
    public TextUserInterface(Scanner reader, Dictionary dictionary){
        this.reader = reader;
        this.dict = dictionary;
    }
    
    public void start(){
        while(true){
            System.out.println("Statement: \n");
            System.out.println("    quit - quit the text user interface");
            
            String command = this.reader.nextLine();
            
            if(command.equals("quit")){
                System.out.println("Cheers!");
                break;
            } else {
                
                if(command.equals("add")){
                    System.out.println("In Finnish: ");
                    String word = this.reader.nextLine();
                    System.out.println("Translation: ");
                    String transl = this.reader.nextLine();
                    
                    this.dict.add(word, transl);
                }
                
                if(command.equals("translate")){
                    System.out.println("Give a word: ");
                    System.out.println("Translation: " + this.dict.translate(this.reader.nextLine()));
                }
                
                
                System.out.println("Unknown statement\n");
            }
            
            
        }
    }
     
}
