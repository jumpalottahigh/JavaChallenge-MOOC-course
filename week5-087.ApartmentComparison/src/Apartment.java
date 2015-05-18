
public class Apartment {

    private int rooms;
    private int squareMeters;
    private int pricePerSquareMeter;

    public Apartment(int rooms, int squareMeters, int pricePerSquareMeter) {
        this.rooms = rooms;
        this.squareMeters = squareMeters;
        this.pricePerSquareMeter = pricePerSquareMeter;
    }
    
    public boolean larger(Apartment otherApartment){
        if(this.squareMeters>otherApartment.squareMeters){
            return true;
        } else {
            return false;
        }
    }
    
    public int priceDifference(Apartment otherApartment){
        int thisBuffer = this.squareMeters * this.pricePerSquareMeter;
        int Buffer = otherApartment.squareMeters * otherApartment.pricePerSquareMeter;
        
        return Math.abs(thisBuffer - Buffer);
        //return thisBuffer - Buffer;
    }
    
    public boolean moreExpensiveThan(Apartment otherApartment) {
        //int thisBuffer = this.squareMeters * this.pricePerSquareMeter;
        //int Buffer = otherApartment. * otherApartment.pricePerSquareMeter;
        int Buffer2 = otherApartment.squareMeters * otherApartment.pricePerSquareMeter;
        int Buffer1 = this.squareMeters * this.pricePerSquareMeter;
        
        //if()
        if (Buffer1> Buffer2)
            return true;
        else
            return false;
    }
    
}
