
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Storehouse {
    private Map<String, Integer>store = new HashMap<String, Integer>();
    private Map<String, Integer>store_stock = new HashMap<String, Integer>();
    
    public void addProduct(String product, int price, int stock){
        this.store.put(product, price);
        this.store_stock.put(product, stock);
    }
    
    public int price(String product){
        if(this.store.containsKey(product)){
            return this.store.get(product);
        } else {
            return -99;
        }
    }
    
    public int stock(String product){
        if(this.store_stock.containsKey(product))
            return this.store_stock.get(product);
        else
            return 0;
    }
    
    public boolean take(String product){
        int buffer = 0;
        
        
        if(this.store_stock.containsKey(product)){
            buffer = this.store_stock.get(product);
            if(buffer>0){
                buffer--;
                this.store_stock.replace(product, buffer);
                return true;
            } else 
            return false;
        } else {
            return false;
        }
    }
    
    public Set<String> products() {
        Set<String> buffer = new HashSet<String>();

        for(String i : this.store_stock.keySet()){
            buffer.add(i);
        }
        
        return buffer;
    }
}
