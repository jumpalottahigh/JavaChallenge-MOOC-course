
import java.io.File;
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
public class Printer {
    private String fname;
    //private Scanner reader;
    
    public Printer(String fileName) throws Exception{
        this.fname = fileName;
        //f = new File(fileName);
        //reader = new Scanner(f, "UTF-8");
    }
    
    public void printLinesWhichContain(String word) throws Exception{
        File f = new File(this.fname);
        
        Scanner reader = new Scanner(f);
        
        String buffer = "";
        String line = "";
        
        while(reader.hasNextLine()){
            line = reader.nextLine();
            
            if(line.contains(word)){
                buffer += line;
                buffer +="\n";
                System.out.println(line);
            }
            
        }
 
        //System.out.println(buffer);
    }
}
