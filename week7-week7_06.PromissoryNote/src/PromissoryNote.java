
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
public class PromissoryNote {
    
    private HashMap<String, Double> note;
    
    public PromissoryNote(){
        this.note = new HashMap<String, Double>();
    }
    
    public void setLoan(String toWhom, double value){
        note.put(toWhom, value);
    }
    
    public double howMuchIsTheDebt(String whose){
        if(this.note.containsKey(whose)){
            return note.get(whose);
        } else {
            return 0.0;
        }
    }
}
