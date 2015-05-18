/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clicker.ui;

import clicker.applicationlogic.Calculator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

/**
 *
 * @author jumpalottahigh
 */
public class ClickListener implements ActionListener{
    
    private Calculator calc;
    private JLabel lab;
    
    public ClickListener(Calculator calc, JLabel lab){
        this.calc = calc;
        this.lab = lab;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        calc.increase();
        lab.setText(""+calc.giveValue());
    }
}
