
public class Greatest {

    public static int greatest(int number1, int number2, int number3) {
        if(number1>=number2 && number1>=number3){
            return number1;
        } else if(number2>=number3 && number2>=number1){
            return number2;
        } else {
            return number3;
        }
    }

    public static void main(String[] args) {
        int result = greatest(2, 7, 3);
        System.out.println("Greatest: " + result);
    }
}