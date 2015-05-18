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
public class Thermometer implements Sensor{

    private boolean state;
    
    public Thermometer() {
        state = false;
    }

    @Override
    public boolean isOn() {
        return this.state;
    }

    @Override
    public void on() {
        this.state = true;
    }

    @Override
    public void off() {
        this.state = false;
    }

    @Override
    public int measure() {
        if(this.state){
            if(Math.random() > 0.5f){
                return (int) ((-1) * Math.random() * 30); 
            } else {
                return (int) (Math.random() * 30);
            }
        } else {
            throw new IllegalStateException();
        }
    }
    
}
