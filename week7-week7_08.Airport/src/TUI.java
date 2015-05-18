
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class TUI {
    private Scanner reader;
    private Airplanes planes;
    private Flights flights;
    
    public TUI(Scanner reader, Airplanes planes, Flights flights){
        this.reader = reader;
        this.planes = planes;
        this.flights = flights;
    }
    
    public void start() {
        boolean end = false;
        
        System.out.println("Airport panel\n-------------------\n");
        while (true) {
            System.out.println("Choose operation: \n[1] Add airplane\n[2] Add flight\n[x] Exit");
            System.out.print("> ");
            
            String command = this.reader.nextLine();
            
            if(command.equals("1")){
                System.out.println("Give plane ID: ");
                String planeID = this.reader.nextLine();
                System.out.println("Give plane capacity: ");
                String capacity = this.reader.nextLine();
                
                this.planes.add(planeID, capacity);
            }
            
            if(command.equals("2")){
                System.out.println("Give plane ID: ");
                String planeID = this.reader.nextLine();
                
                String planeIDbuff = this.planes.get(planeID);
             
                System.out.println("Give departure airport code: ");
                String depCode = this.reader.nextLine();
                
                System.out.println("Give destination airport code: ");
                String destCode = this.reader.nextLine();
                
                String construct = "("+depCode + "-" + destCode+")";
                
                this.flights.add(planeIDbuff +" "+ construct);
                
            }
            
            if(command.equals("x")){
                System.out.println("Flight service\n------------\n");
                
                while(true){
                    System.out.println("Choose operation: \n[1] Print planes\n[2] Print flights\n[3] Print plane info\n[x] Quit");
                    System.out.print("> ");
                    
                    String command2 = this.reader.nextLine();
                    
                    if(command2.equals("1")){
                        System.out.println(this.planes);
                    }
                    
                    if(command2.equals("2")){
                        
                        System.out.println(this.flights);
                    }
                    
                    if(command2.equals("3")){
                        System.out.println("Give plane ID: ");
                        String com = this.reader.nextLine();
                        
                        System.out.println(this.planes.get(com));
                    }
                    
                    if(command2.equals("x")){
                        end = true;
                        break;
                    }
                    
                }
            }
            
            if(end)
                break;
        }
    }
    
}
