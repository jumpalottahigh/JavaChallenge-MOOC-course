/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class Student {
    
    private String name;
    private String studentNumber;
    
    
    public Student (String name, String studentNumber) {
        this.name = name;
        this.studentNumber = studentNumber;
    }
    
    public Student() {
        this.name ="";
        this.studentNumber = "";
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getStudentNumber(){
        return this.studentNumber;
    }
    
    public String toString(){
        return this.name + " (" + this.studentNumber + ")";
    }
    
}
