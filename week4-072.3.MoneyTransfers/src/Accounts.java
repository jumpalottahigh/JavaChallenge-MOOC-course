
public class Accounts {
    
    public static void transfer(Account from, Account to, double howMuch){
            from.withdrawal(howMuch);
            to.deposit(howMuch);
        }

    public static void main(String[] args) {
        // Code in Account.Java should not be touched!
        // write your code here
        
        Account A = new Account("A", 100.0);
        Account B = new Account("B", 0.0);
        Account C = new Account("C", 0.0);
        
        
        
        
        
        
        transfer(A, B, 50.0);
        transfer(B, C, 25.0);
        
    }


}
