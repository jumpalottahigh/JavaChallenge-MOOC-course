
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
public class Container {
    private int maxWeightLimit;
    private ArrayList<Suitcase> suitcases;
    
    public Container (int maxWeightLimit) {
        this.maxWeightLimit = maxWeightLimit;
        this.suitcases = new ArrayList<Suitcase>();
    }
    
    public void addSuitcase(Suitcase suitcase){
        int maxKilos = 0;
        
        for(Suitcase i : this.suitcases){
            maxKilos += i.totalWeight();
        }
        
        if (this.maxWeightLimit - maxKilos >= suitcase.totalWeight())
            this.suitcases.add(suitcase);
    }
    
    public String toString() {
        int kilos = 0;
        
        for(Suitcase i : this.suitcases){
            kilos += i.totalWeight();
        }
        
        return this.suitcases.size() + " suitcases (" + kilos + " kg)";
    }
    
    public void printThings(){
        for(Suitcase i : this.suitcases){
            i.printThings();
        }
    }
}
