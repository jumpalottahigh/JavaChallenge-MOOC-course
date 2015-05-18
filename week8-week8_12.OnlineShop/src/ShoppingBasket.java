
import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class ShoppingBasket {
    private Map<String, Purchase> sb = new HashMap<String, Purchase>();
    
    public ShoppingBasket(){
    
    }
    
    public void add(String product, int price) {
        if(this.sb.containsKey(product)){
            this.sb.get(product).increaseAmount();
        } else {
        Purchase buffer = new Purchase(product, 1, price);
        this.sb.put(product, buffer);
        }
    }
    
    public int price(){
        int buffer = 0;
        
        for(Purchase i : this.sb.values()){
            buffer += i.price();
        }
        
        return buffer;
    }
    
    public void print(){
        for(Purchase i : this.sb.values()){
            System.out.println(i);
        }
    }
}
