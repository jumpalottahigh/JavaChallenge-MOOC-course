/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author jumpalottahigh
 */
public class Analysis {
    
    private File f;
    
    public Analysis(File file){
        this.f = file;
    }
    
    public int lines() throws Exception{
        Scanner reader = new Scanner(this.f);
        String buffer = "";
        
        int counter=0;
        
        while(reader.hasNextLine()){
            
            buffer = reader.nextLine();
            
            //if(buffer.equals("\n")){
                counter++;
            //}
        }
        
        reader.close();
        return counter;
    }
    
    public int characters() throws Exception{
        Scanner reader = new Scanner(this.f);
        
        String buffer = "";
        int counter = 0;
        
        while(reader.hasNext()){
            buffer = reader.next();
            
            counter += buffer.length();
            counter++;
        }
       
        reader.close();
        
        //counter = buffer.length();
        
        //counter += this.lines();
        
        return counter;
        
        
    }
}
