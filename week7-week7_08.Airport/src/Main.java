
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        // Write your main program here. Implementing your own classes will be useful.
        
        Scanner reader = new Scanner(System.in);
        Airplanes planes = new Airplanes();
        Flights flights = new Flights();

        TUI ui = new TUI(reader, planes, flights);
        ui.start();
    }
}
