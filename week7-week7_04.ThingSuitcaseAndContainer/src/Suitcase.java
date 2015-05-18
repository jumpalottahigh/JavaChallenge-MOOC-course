
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
public class Suitcase {
    private ArrayList<Thing> things;
    private int maxWeight;
    
    public Suitcase(int maxWeight){
        this.things = new ArrayList<Thing>();
        this.maxWeight = maxWeight;
    }
    
    public void addThing(Thing thing){
        
        if(this.maxWeight-this.totalWeight() >= thing.getWeight())
            this.things.add(thing);
    }
    
    public String toString(){
           
        switch(this.things.size()){
            case 0:
                    return "empty (" + this.totalWeight() + " kg)";
            case 1:
                    return this.things.size() + " thing (" + this.totalWeight() + " kg)";
            default:
                    return this.things.size() + " things (" + this.totalWeight() + " kg)";
        }
        
    }
    
    public void printThings(){
        for(Thing i : this.things){
            System.out.println(i);
        }
    }
    
    public int totalWeight(){
        int buffer = 0;
        
        for(Thing i : this.things){
            buffer += i.getWeight();
        }
        return buffer;
    }
    
    public Thing heaviestThing () {
        if(this.things.size()>0){
            int heaviest = this.things.get(0).getWeight();
            //Thing buffer;
            
            for(Thing i : this.things){
                if(i.getWeight()>= heaviest){
                    heaviest = i.getWeight();
                }
            }

            for(Thing i : this.things){
                if(i.getWeight() == heaviest)
                    //buffer = (Thing) i;
                    return i;
            }
            //return buffer;
        }
            return null;
        
    }
}
