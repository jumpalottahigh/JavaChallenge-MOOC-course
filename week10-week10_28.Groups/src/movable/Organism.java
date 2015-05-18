/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movable;

/**
 *
 * @author jumpalottahigh
 */
public class Organism implements Movable{
    private int x_cod;
    private int y_cod;

    public Organism(int x_cod, int y_cod) {
        this.x_cod = x_cod;
        this.y_cod = y_cod;
    }

    @Override
    public String toString() {
        return "x: " + x_cod + "; y: " + y_cod;
    }
    
    
    
    @Override
    public void move(int dx, int dy) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.x_cod += dx;
        this.y_cod += dy;
    }
}
