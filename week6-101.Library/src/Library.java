
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Library {
    private ArrayList<Book> Library;
    
    public Library(){
        this.Library = new ArrayList<Book>();
    }
    
    public void addBook(Book newBook){
        this.Library.add(newBook);
    }
    
    public void printBooks(){
        for(Book i : this.Library){
            System.out.println(i);
        }
    }
    
    public ArrayList<Book> searchByTitle(String title) {
        ArrayList<Book> found = new ArrayList<Book>();
        
        for(Book i : this.Library){
            if(StringUtils.included(i.title(), title)){
                found.add(i);
            }
        }
        
        return found;
    }
    
    public ArrayList<Book> searchByPublisher(String publisher) {
        ArrayList<Book> found = new ArrayList<Book>();
        
        for(Book i : this.Library){
            if(StringUtils.included(i.publisher(), publisher)){
                found.add(i);
            }
        }
        
        return found;
    }
    
    public ArrayList<Book> searchByYear(int year) {
        ArrayList<Book> found = new ArrayList<Book>();
        
        for(Book i : this.Library){
            if(i.year() == year){
                found.add(i);
            }
        }
        
        return found;
    }
}
