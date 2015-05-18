/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jumpalottahigh
 */
public class MindfulDictionary {
    private Map<String, String> db;
    private File f;
    private Scanner reader;
    private FileWriter writer;
    
    
    public MindfulDictionary(){
        this.db = new HashMap<String, String>();
    }
    
    public MindfulDictionary(String file) throws Exception{
        this.db = new HashMap<String, String>();
        this.f = new File(file);
        if(this.f.exists()){
            this.reader = new Scanner(this.f, "UTF-8");
            this.writer = new FileWriter(this.f);
        }
    }
    
    public boolean load(){
        if(this.f.exists()){
            this.db.clear();
            
            String line = "";
            String[] parts = null;

            while(this.reader.hasNextLine()){
                line = this.reader.nextLine();
                parts = line.split(":");
                
                //this.add(parts[0], parts[1]);
                if(!this.db.containsKey(parts[0]) && !this.db.containsValue(parts[0])){
            this.db.put(parts[0], parts[1]);
            this.db.put(parts[1], parts[0]);
        }
            }
            
            return true;
            
        } else {
            return false;
        }
    }
    
    public boolean save(){
        if(!this.f.exists()){
            return false;
        } else {
            for(String i : this.db.keySet()){
                try {
                    this.writer.write(i);
                } catch (IOException ex) {
                    Logger.getLogger(MindfulDictionary.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //this.writer.close();
            return true;
        }
    }
    
    public void add(String word, String translation){
        if(!this.db.containsKey(word) && !this.db.containsValue(word)){
            this.db.put(word, translation);
            this.db.put(translation, word);
        }
    }
    
    public String translate(String word){
        if(this.db.containsKey(word)){
            return this.db.get(word);
        } else {
            return null;
        }
    }
    
    public void remove(String word){
        String translation = "";
        
        if(this.db.containsKey(word)){
            translation = this.db.get(word);
            this.db.remove(word);
            this.db.remove(translation);
        }
    }
}
