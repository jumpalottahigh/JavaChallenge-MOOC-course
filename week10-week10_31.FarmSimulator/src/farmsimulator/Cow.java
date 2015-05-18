/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmsimulator;

import java.util.Random;

/**
 *
 * @author jumpalottahigh
 */
public class Cow implements Milkable, Alive{
    private double udder_cap;
    private String name;
    private double current_vol;
    
    private Random rd = new Random();
    private static final String[] NAMES = new String[]{
        "Anu", "Arpa", "Essi", "Heluna", "Hely",
        "Hento", "Hilke", "Hilsu", "Hymy", "Ihq", "Ilme", "Ilo",
        "Jaana", "Jami", "Jatta", "Laku", "Liekki",
        "Mainikki", "Mella", "Mimmi", "Naatti",
        "Nina", "Nyytti", "Papu", "Pullukka", "Pulu",
        "Rima", "Soma", "Sylkki", "Valpu", "Virpi"};
    
    public Cow() { 
        this.udder_cap = 15 + rd.nextInt(26);
        
        int i = new Random().nextInt(NAMES.length+1);
        this.name= NAMES[i];
        this.current_vol =0;
    }

    public Cow(String name) {
        this.name = name;
         this.udder_cap = 15 + rd.nextInt(26);
         this.current_vol =0;
    }

    public double getCapacity() {
        return udder_cap;
    }

    public String getName() {
        return name;
    }
    
    public double getAmount(){
        return this.udder_cap - this.current_vol;
    }

    @Override
    public double milk() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void liveHour() {

        double temp = 0.7 + (2 - 0.7) * rd.nextDouble();  
    }
}
