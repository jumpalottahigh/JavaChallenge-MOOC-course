
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jumpalottahigh
 */
public class CalculatorZ implements ActionListener{
    
    private JTextField input;
    private JTextField output;
    private JButton z;
    
    public CalculatorZ(JTextField input, JTextField output, JButton z){
        this.input = input;
        this.output = output;
        this.z = z;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        int bufferOut = 0;
        int bufferIn = 0;
        
        bufferIn = Integer.parseInt(input.getText());
        bufferOut = Integer.parseInt(output.getText());
        
        bufferOut += bufferIn;
        */
        this.input.setText("");
        this.output.setText("0");
        z.setEnabled(false);
        
    }
}
