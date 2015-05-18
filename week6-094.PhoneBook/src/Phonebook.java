
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
public class Phonebook {
    private ArrayList<Person> entries = new ArrayList<Person>();

    public Phonebook() {
        this.entries.clear();
    }
    
    public void add(String name, String number){
        Person buffer = new Person(name, number);
        this.entries.add(buffer);
    }
    
    public void printAll(){
        for(Person i : this.entries){
            i.toString();
        }
    }
    
    public String searchNumber(String name){
        if(this.entries.contains(name)){
            //return this.entries.getNumber;
        } else {
            return "number not known";
        }
    }
}
