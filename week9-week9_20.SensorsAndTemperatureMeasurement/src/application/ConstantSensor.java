/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

/**
 *
 * @author jumpalottahigh
 */
public class ConstantSensor implements Sensor{
    private int tem;
    
    public ConstantSensor(int tem){
        this.tem = tem;
    }
    
    public void on(){};
    
    public void off(){};
    
    public int measure(){
        return this.tem;
    }
    
    public boolean isOn(){
        return true;
    }
    
}
