
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class RingingCentre {
    private Map<Bird, List<String>> Rmap;

    public RingingCentre() {
        this.Rmap = new HashMap<Bird, List<String>>();
    }
    
    public void observe(Bird bird, String place){
        if(!this.Rmap.containsKey(bird)){
            ArrayList<String> temp = new ArrayList<String>();
            temp.add(place);
            this.Rmap.put(bird, temp);
        }
        else{
           ArrayList<String> temp = (ArrayList)this.Rmap.get(bird);
           temp.add(place);
           this.Rmap.put(bird, temp);
        }
    }
    
    public void observations(Bird bird){
        System.out.print(bird);       
        System.out.print(" observations: ");
        if(this.Rmap.containsKey(bird)){
            ArrayList<String> temp = (ArrayList)this.Rmap.get(bird);
            System.out.println(""+ temp.size());
            for(String temp1 : temp){
                System.out.println(temp1);
            }
        }
        else{
            System.out.println(""+ 0);
        }
    }
}
