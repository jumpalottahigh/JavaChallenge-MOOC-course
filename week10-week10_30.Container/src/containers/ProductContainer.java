/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containers;

/**
 *
 * @author jumpalottahigh
 */
public class ProductContainer extends Container{
    private String name;
    
    public ProductContainer(String name, double capacity) {
        super(capacity);
        this.name = name;   
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String newName){
        this.name = newName;
    }
    
    public String toString(){
        return this.name+": "+super.toString();
    }
}
