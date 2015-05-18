/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Change {
    private char from;
    private char to;
    
    public Change (char fromCharacter, char toCharacter) {
        this.from = fromCharacter;
        this.to = toCharacter;
    }
    
    public String change(String characterString) {
        return characterString.replace(from, to);
    }
    
}
