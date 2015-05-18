
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Box implements ToBeStored{
    
    private double maxWeight;
    private ArrayList<ToBeStored> db;
    
    public Box(double maxWeight){
        this.maxWeight = maxWeight;
        this.db = new ArrayList<ToBeStored>();
    }
    
    public void add(ToBeStored item){
        if((this.maxWeight - this.weight()) >= item.weight())
            this.db.add(item);
    }
    
    public double weight(){
        double weight = 0;
        
        for(ToBeStored item : this.db){
            weight += item.weight();
        }
        return weight;
    }
    
    public String toString(){
        return "Box: "+this.db.size()+" things, total weight "+this.weight()+" kg";
    }
}
