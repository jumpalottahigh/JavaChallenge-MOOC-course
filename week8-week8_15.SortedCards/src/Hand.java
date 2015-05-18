
import java.util.ArrayList;
import java.util.Collections;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Hand implements Comparable<Hand>{
    
    private ArrayList<Card> hand;
    
    public Hand(){
        this.hand = new ArrayList<Card>();
    }
    
    public void add(Card card){
        this.hand.add(card);
    }
    
    public void print(){
        for(Card i : this.hand){
            System.out.println(i);
        }
    }
    
    public void sort(){
        Collections.sort(this.hand);
    }
    
    
    public int compareTo(Hand other){
        
        int sum1 = 0;
        int sum2 = 0;
        
        for(Card i : this.hand){
            sum1 += i.getValue();
        }
        
        for(Card j : other.hand){
            sum2 += j.getValue();
        }
        
        
        if(sum1 < sum2){
            return -1;
        } else if (sum1 == sum2){
            return 0;
        } else {
            return 1;
        }
    }
    
    public void sortAgainstSuit(){
        Collections.sort(this.hand, new SortAgainstSuitAndValue());
    }
    
}
