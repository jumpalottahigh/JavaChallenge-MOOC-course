
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
public class Reader {
    
    private Scanner reader;
    
    public Reader () {
        this.reader = new Scanner(System.in);
    }
    
    public String readString(){
        return this.reader.nextLine();
    }
    
    public int readInteger(){
        return Integer.parseInt(this.reader.nextLine());
    }
}
