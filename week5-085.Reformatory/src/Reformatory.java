public class Reformatory {
    
    private int total = 0;
    
    public int weight(Person person) {
        // return the weight of the person
        total++;
        
        return person.getWeight();
    }
    
    public void feed(Person person){
        int buffer = person.getWeight();
        buffer++;
        person.setWeight(buffer);
    }
    
    public int totalWeightsMeasured() {
        return total;
    }

}
