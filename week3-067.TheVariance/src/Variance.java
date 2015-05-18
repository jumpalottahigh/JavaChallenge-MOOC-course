import java.util.ArrayList;

public class Variance {
    // Copy here sum from exercise 63 
    public static int sum(ArrayList<Integer> list) {
        int sum = 0;
        
        for(int i : list){
            sum = sum + i;
        }
        return sum;
    }
    
    // Copy here average from exercise 64 
    public static double average(ArrayList<Integer> list) {
        double total = sum(list);
        double average=0.0;
        
        return average = total/list.size();
    }

    public static double variance(ArrayList<Integer> list) {
        // write code here
        double average = average(list);
        double variance_buffer = 0.0;
        double variance_result;
        
        for(int i : list){
            variance_buffer = variance_buffer + Math.pow((i-average), 2);
        }
        
        variance_result = variance_buffer / (list.size()-1);
        
        return variance_result;
    }
    
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(3);
        list.add(2);
        list.add(7);
        list.add(2);
        
        System.out.println("The variance is: " + variance(list));
    }

}
