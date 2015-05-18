/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Bird {
    private String name;
    private String latName;
    private int observation;
    
    public Bird(String name, String latName) {
        this.name = name;
        this.latName = latName;
        this.observation = 0;
    }
    
    public void Observed(){
        this.observation++;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String toString(){
        return "\n"+this.name+" ("+this.latName+"): " + this.observation + " observations";
    }
    
}
