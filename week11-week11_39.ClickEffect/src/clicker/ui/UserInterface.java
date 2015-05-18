package clicker.ui;

import clicker.applicationlogic.Calculator;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;

public class UserInterface implements Runnable {
    private JFrame frame;

    private Calculator cal;
    private JLabel lab;
    
    public UserInterface(Calculator calc){
        this.cal = calc;
    }

    @Override
    public void run() {
        frame = new JFrame("Click Effect");
        frame.setPreferredSize(new Dimension(200, 100));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container container) {
        
        GridLayout layout = new GridLayout(2,1);
        container.setLayout(layout);
        
        lab = new JLabel("0");
        
        JButton btn = new JButton("Click!");
        
        ClickListener cl = new ClickListener(this.cal, this.lab);
        btn.addActionListener(cl);
        
        container.add(lab);
        container.add(btn);
        
    }

    public JFrame getFrame() {
        return frame;
    }
}
