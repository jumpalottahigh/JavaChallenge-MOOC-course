
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Flights {
    private ArrayList<String> flights;
    
    public Flights(){
        this.flights = new ArrayList<String>();
    }
    
    public void add(String entry){
        this.flights.add(entry);
    }
    
    /*
    public String get(String what){
        return this.flights.get(what);
    }
    */
    
    public String toString(){
        String buffer ="";
        
        for(String i : this.flights){
            buffer += i + "\n";
        }
        
        
        return buffer;
        
    }
    
}
