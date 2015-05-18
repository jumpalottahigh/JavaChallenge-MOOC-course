
public class Main {
    public static void main(String[] args) {
        // write testcode here
        
        int[] values = {-1, 6, 9, 8, 12};
System.out.println(indexOfTheSmallestStartingFrom(values, 1));
System.out.println(indexOfTheSmallestStartingFrom(values, 2));
System.out.println(indexOfTheSmallestStartingFrom(values, 4));
    }
    
    public static int smallest(int[] array){
        int smallest = array[0];
        for (int i : array){
            if(i<smallest){
                smallest = i;
            }
        }
        return smallest;
    }
    
    public static int indexOfTheSmallest(int[] array){
        int smallest = smallest(array);
        int buffer = 0;
        
        for(int i : array){
            
            if(i == smallest){
                break;
            }
            buffer++;
        }
        
        return buffer;
    }
    
    public static int indexOfTheSmallestStartingFrom(int[] array, int index){
        int[] buffer2 = new int[20];
        int small;
        int buff = 0;
        int elem = index + 1;
        
        for (int i = index; i< array.length; i++){
            if(array[i]<array[i+1]){
                buff++;
            } 
        }
        
        //small = indexOfTheSmallest(buffer2);
        return buff;
        
    }
}
