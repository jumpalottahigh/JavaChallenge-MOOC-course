
public class Money {

    private final int euros;
    private final int cents;

    public Money(int euros, int cents) {

        if (cents > 99) {
            euros += cents / 100;
            cents %= 100;
        }

        this.euros = euros;
        this.cents = cents;
    }

    public int euros() {
        return euros;
    }

    public int cents() {
        return cents;
    }

    @Override
    public String toString() {
        String zero = "";
        if (cents < 10) {
            zero = "0";
        }

        return euros + "." + zero + cents + "e";
    }
    
    public Money plus(Money Added){
        Money buffer = new Money(this.euros + Added.euros, this.cents + Added.cents);
        return buffer;
    }
    
    public boolean less(Money compared){
        if(this.euros * 100 + this.cents  < compared.euros*100 + compared.cents){
            return true;
        } else {
            return false;
        }
    }
    
    public Money minus(Money decremented){
        if(this.euros - decremented.euros >0){
            if(this.cents < decremented.cents){
                Money buffer3 = new Money(this.euros-1-decremented.euros, this.cents+100-decremented.cents);
                return buffer3;
            } else {
                Money buffer1 = new Money (this.euros - decremented.euros, this.cents - decremented.cents);
                return buffer1;
            }
        } else {
            Money buffer2 = new Money (0, 0);
            return buffer2;
        }
        
    }

}
