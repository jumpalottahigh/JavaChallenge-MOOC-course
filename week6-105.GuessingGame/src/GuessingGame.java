import java.util.Scanner;

public class GuessingGame {

    private Scanner reader;
    
    public GuessingGame() {
        // use only this scanner, othervise the tests do not work
        this.reader = new Scanner(System.in);
    }

    public void play(int lowerLimit, int upperLimit) {
        instructions(lowerLimit, upperLimit);
        
        
        // write the guessing logic here
        //while (true){
        int i = -1;
        while(i<howManyTimesHalvable(upperLimit-lowerLimit)){
            if(lowerLimit == upperLimit) {
                break;
            }
            
            if(this.isGreaterThan(this.average(upperLimit, lowerLimit))){
                lowerLimit = this.average(upperLimit, lowerLimit);
            } else {
                upperLimit = this.average(upperLimit, lowerLimit);
            }
            
            i++;
            
        }
        
        System.out.println("The number you're thinking of is "+ upperLimit);
        
        
        
        

    }

    // implement here the methods isGreaterThan and average

    public void instructions(int lowerLimit, int upperLimit) {
        int maxQuestions = howManyTimesHalvable(upperLimit - lowerLimit);

        System.out.println("Think of a number between " + lowerLimit + "..." + upperLimit + ".");

        System.out.println("I promise you that I can guess the number you are thinking with " + maxQuestions + " questions.");
        System.out.println("");
        System.out.println("Next I'll present you a series of questions. Answer them honestly.");
        System.out.println("");
    }

    // a helper method:
    public static int howManyTimesHalvable(int number) {
        // we create a base two logarithm  of the given value

        // Below we swap the base number to base two logarithms!
        return (int) (Math.log(number) / Math.log(2)) + 1;
    }
    
    public boolean isGreaterThan(int value){
        System.out.println("Is your number greater than "+value+ "? (y/n)");
        if(this.reader.nextLine().equals("y")){
            return true;
        } else {
            return false;
        }
    }
    
    public int average (int firstNumber, int secondNumber) {
        double res = (firstNumber + secondNumber)/2.0;
        Math.round(res);
        //System.out.println(res);
        return (int)res;  
    }
}
