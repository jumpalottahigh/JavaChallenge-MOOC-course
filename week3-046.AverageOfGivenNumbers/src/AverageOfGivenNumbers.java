
public class AverageOfGivenNumbers {
    
    // implement here again the method of exercise 43
    public static int sum(int number1, int number2, int number3, int number4) {
        int sum;
        
        sum = number1 + number2 + number3 + number4;
        
        return sum;
    }

    public static double average(int number1, int number2, int number3, int number4) {
        double average;
        
        average = sum(number1,number2,number3,number4)/4.0;
        
        return average;
    }

    public static void main(String[] args) {
        double result = average(4, 3, 6, 1);
        System.out.println("Average: " + result);
    }
}
