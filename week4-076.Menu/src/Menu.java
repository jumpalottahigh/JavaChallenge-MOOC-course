
import java.util.ArrayList;

public class Menu {

    private ArrayList<String> meals;

    public Menu() {
        this.meals = new ArrayList<String>();
    }

    // add the methods here
    public void addMeal(String meal) {
        if (!this.meals.contains(meal)){
            this.meals.add(meal);
        }      
    }
    
    public void printMeals() {
        //for (int i =0; i <meals.size(); i++){
        //    System.out.println(meals.indexOf(i));
        //}
        //System.out.println(this.meals);
        for(String i : this.meals){
            System.out.println(i);
        }
    }
    
    public void clearMenu(){
        this.meals.clear();
    }
    
}
