/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Calculator {
    
    private Reader reader;
    private int stats;
    
    public Calculator(){
        this.reader = new Reader();
        this.stats = 0;
    }
    
    public void start() {
        while (true) {
            System.out.print("command: ");
            String command = reader.readString();
            if (command.equals("end")) {
                break;
            }

            if (command.equals("sum")) {
                sum();
            } else if (command.equals("difference")) {
                difference();
            } else if (command.equals("product")) {
                product();
            }
        }

        statistics();
    }
    
    private void sum (){
        System.out.println("value1: ");
        int value1 = this.reader.readInteger();
        System.out.println("value2: ");
        int value2 = this.reader.readInteger();
        
        int sum = value1 + value2;
        
        System.out.println("sum: " + sum);
        this.stats++;
    }
    
    private void difference(){
        System.out.println("value1: ");
        int value1 = this.reader.readInteger();
        System.out.println("value2: ");
        int value2 = this.reader.readInteger();
        
        int sum = value1 - value2;
        
        System.out.println("difference of the values " + sum);
        this.stats++;
    }
    
    private void product(){
        System.out.println("value1: ");
        int value1 = this.reader.readInteger();
        System.out.println("value2: ");
        int value2 = this.reader.readInteger();
        
        int sum = value1 * value2;
        
        System.out.println("product of the values " + sum);
        this.stats++;
    }
    
    private void statistics () {
        System.out.println("Calculations done: " + this.stats);
        
    }
}
