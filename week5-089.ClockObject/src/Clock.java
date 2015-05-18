public class Clock {
    private BoundedCounter hours;
    private BoundedCounter minutes;
    private BoundedCounter seconds;
   
    public Clock(int hoursAtBeginning, int minutesAtBeginning, int secondsAtBeginning) {
        // the counters that represent hours, minutes and seconds are created and set to have the correct initial values
        //this.hours.setValue(hoursAtBeginning);
        this.hours = new BoundedCounter(hoursAtBeginning);
        //this.minutes.setValue(minutesAtBeginning);
        this.minutes = new BoundedCounter(minutesAtBeginning);
        this.seconds = new BoundedCounter(secondsAtBeginning);
        //this.seconds.setValue(secondsAtBeginning);
    }
    
    public void tick() {
        // Clock advances by one second
        //this.seconds.next();
        
        if(seconds.getValue() == 59){    
            if(minutes.getValue() == 59) {
                if(hours.getValue() == 23){
                    hours.setValue(0);
                } else {
                    hours.next();
                }
            }
             minutes.next();
             seconds.next();
         } else {
             seconds.next();
         }
        
    }
    
    public String toString() {
        // returns the string representation
        return ""+this.hours +":" + this.minutes +":" +this.seconds;
    }
}
