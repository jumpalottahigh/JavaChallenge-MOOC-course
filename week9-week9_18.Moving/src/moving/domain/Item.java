/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moving.domain;

/**
 *
 * @author jumpalottahigh
 */
public class Item implements Thing, Comparable<Item>{
    
    private String name;
    private int vol;
    
    public Item(String name, int vol){
        this.name = name;
        this.vol = vol;
    }
    
    
    public String getName(){
        return this.name;
    }
    
    public String toString(){
        return this.name + " ("+this.vol+" dm^3)";
    }
    
    public int getVolume(){
        return this.vol;
    }
 
    @Override
    public int compareTo(Item comparable){
        /*
        if(this.vol < comparable.getVolume()){
            return -1;
        } else {
            return 1;
        }
                */
        return this.vol - comparable.getVolume();
    }
}
