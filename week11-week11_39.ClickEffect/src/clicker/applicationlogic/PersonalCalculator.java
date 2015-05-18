/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clicker.applicationlogic;

/**
 *
 * @author jumpalottahigh
 */
public class PersonalCalculator implements Calculator{

    private int value;
    
    public PersonalCalculator(){
    }
    
    @Override
    public int giveValue() {
        return value;
    }

    @Override
    public void increase() {
        value++;
    }
    
}
