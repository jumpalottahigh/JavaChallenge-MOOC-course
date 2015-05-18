/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordinspection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author jumpalottahigh
 */
public class WordInspection {
    private File f;
    
    public WordInspection(File file){
        this.f = file;
    }
    
    public int wordCount() throws Exception{
        Scanner reader = new Scanner(this.f);
        
        String buffer = "";
        int counter = 0;
        
        while(reader.hasNextLine()){
            buffer = reader.nextLine();
            counter++;
        }
        
        return counter;
    }
    
    public List<String> wordsContainingZ() throws Exception{
        ArrayList<String> list = new ArrayList<String>();
        Scanner reader = new Scanner(this.f, "UTF-8");
        
        String buffer = "";
        
        while(reader.hasNextLine()){
            buffer = reader.nextLine();
            
            if(buffer.contains("z")){
                list.add(buffer);
            }
        }
        
        
        
        return list;
    }
    
    public List<String> wordsEndingInL() throws Exception{
        ArrayList<String> list = new ArrayList<String>();
        Scanner reader = new Scanner(this.f, "UTF-8");
        
        String buffer = "";
        
        while(reader.hasNextLine()){
            buffer = reader.nextLine();
            
            if(buffer.endsWith("l")){
                list.add(buffer);
            }
        }
        
        
        
        return list;
        
    }
    
    public List<String> palindromes() throws Exception{
        
        
        ArrayList<String> list = new ArrayList<String>();
        Scanner reader = new Scanner(this.f, "UTF-8");
        
        String buffer = "";
        String reverseBuffer = "";
        
        while(reader.hasNextLine()){
            buffer = reader.nextLine();
            
            int n = buffer.length() - 1;
          
            //Build reverese
            for(int i=n; i >= 0; i--){
                
                reverseBuffer += buffer.charAt(i);
                
                
            }
            
            if(buffer.equals(reverseBuffer)){
                    list.add(buffer);
            }
            
            reverseBuffer = "";
            
        }
        
        
        
        return list;
        
    }
    
    
    public List<String> wordsWhichContainAllVowels() throws Exception{
        
        
        ArrayList<String> list = new ArrayList<String>();
        Scanner reader = new Scanner(this.f, "UTF-8");
        
        String buffer = "";
        
        while(reader.hasNextLine()){
            buffer = reader.nextLine();
           
            if(buffer.contains("a") && buffer.contains("e") && buffer.contains("i") && buffer.contains("o") && buffer.contains("u") && buffer.contains("y") && buffer.contains("ä") && buffer.contains("ö")){
                list.add(buffer);
            }
            
        }
       
        return list;
    }
}
