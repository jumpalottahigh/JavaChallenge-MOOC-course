/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Person {
    private String name;
    private String phone;
    
    public Person(String name, String phone){
        this.name = name;
        this.phone = phone;
    }
    
    public String toString(){
        return this.name + "  number: " + this.phone;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getNumber(){
        return this.phone;
    }
    
    public void changeNumber(String newNumber){
        this.phone = newNumber;
    }
    
}
