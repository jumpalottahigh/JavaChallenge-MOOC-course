/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jumpalottahigh
 */
public class Group implements Movable{
    private List <Movable> G_list;

    public Group() {
         this.G_list = new ArrayList<Movable>();
    }
    
    public void addToGroup(Movable movable){
        this.G_list.add(movable);
    }

    @Override
    public String toString() {
        StringBuilder SB = new StringBuilder();
        for(Movable temp : this.G_list){
            Organism temp_o = (Organism) temp;
            SB.append(temp_o.toString());
            SB.append("\n");
        }
        return SB.toString();
    }
    
    
    
    @Override
    public void move(int dx, int dy) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        for(Movable temp : this.G_list){
            Organism temp_o = (Organism) temp;
            temp_o.move(dx, dy);
        }
    }
}
