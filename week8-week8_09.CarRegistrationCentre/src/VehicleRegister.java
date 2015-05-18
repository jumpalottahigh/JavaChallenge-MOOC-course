
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
public class VehicleRegister {
    
    private HashMap<RegistrationPlate, String> db;
    
    public VehicleRegister(){
        this.db = new HashMap<RegistrationPlate, String>();
    }
    
    public boolean add(RegistrationPlate plate, String owner){
        if(!this.db.containsKey(plate)){
            this.db.put(plate, owner);
            return true;
        } else {
            return false;
        }
    }
    
    public String get(RegistrationPlate plate){
        return this.db.get(plate);
    }
    
    public boolean delete(RegistrationPlate plate){
        if(this.db.containsKey(plate)){
            this.db.remove(plate);
            return true;
        } else {
            return false;
        }
    }
    
    public void printRegistrationPlates(){
        for(RegistrationPlate i : this.db.keySet()){
            System.out.println(i);
        }
    }
    
    public void printOwners(){
        ArrayList<String> buffer = new ArrayList<String>();
        String toAdd = "";
        
        for(RegistrationPlate i : this.db.keySet()){
            //System.out.println(this.db.get(i));
            toAdd = this.db.get(i);
            if(!buffer.contains(toAdd)){
                buffer.add(toAdd);
            }
        }
        
        for(String j : buffer){
            System.out.println(j);
        }
    }
}
