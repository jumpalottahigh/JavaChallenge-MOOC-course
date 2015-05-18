import java.util.Random;

public class PasswordRandomizer {
    // Define the variables
    private int length;
    private Random random = new Random();

    public PasswordRandomizer(int length) {
        // Initialize the variable
        this.length = length;
        //this.random.nextInt(length);
    }

    public String createPassword() {
        // write code that returns a randomized password
        String buffer = "";
        
        for(int i=0; i<this.length;i++){
            char symbol = "abcdefghijklmnopqrstuvwxyz".charAt(this.random.nextInt(26));
            buffer += symbol;    
        }
        
        
        
        return buffer;
    }
}
