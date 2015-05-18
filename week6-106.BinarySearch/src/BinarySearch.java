public class BinarySearch {
    public static boolean search(int[] array, int searchedValue) {
        int beginning = 0;
        int end = array.length - 1;
        int middle = (beginning + end) / 2;
        
        while (beginning <= end) {
            
            if(array[middle] < searchedValue)
                beginning = middle +1;
            else if (array[middle] == searchedValue) {
                return true;
            } else {
                end = middle -1;
            }
            
            middle = (beginning + end) / 2;
            // restrict the search area 
        }
        return false;
    }
}
