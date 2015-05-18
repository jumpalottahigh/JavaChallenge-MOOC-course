/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jumpalottahigh
 */
public class AverageSensor implements Sensor{
    
    //private boolean aveSenState;
    private ArrayList<Sensor> sensors;
    private List<Integer> reads;
    
    public AverageSensor() {
        //this.aveSenState = false;
        this.sensors = new ArrayList<Sensor>();
        
        this.reads = new ArrayList<Integer>();
    }

    public void addSensor(Sensor additional){
        this.sensors.add(additional);
        
    }
    
    @Override
    public boolean isOn() {
        boolean allON = false;
        
        for(Sensor i : this.sensors){
            if(i.isOn()){
                allON = true;
            } else {
                allON = false;
            }
        }
        
        return allON;
    }

    @Override
    public void on() {
        for(Sensor i : this.sensors){
            i.on();
        }
        
        //this.aveSenState = true;
    }

    @Override
    public void off() {
        for(Sensor i : this.sensors){
            i.off();
        }
        
        //this.aveSenState = false;
    }

    @Override
    public int measure() {
        if(this.sensors.size() == 0 || this.isOn() == false){
            throw new IllegalStateException();
        }
        
        int sum = 0;
        int count = 0;
        int ave = 0;
        
        for(Sensor i : this.sensors){
            count++;
            sum +=i.measure();
        }
        
        ave = sum/count;
        
        this.reads.add(ave);
        
        return ave;
    }
    
    public List<Integer> readings(){
        return this.reads;
    }
    
}
