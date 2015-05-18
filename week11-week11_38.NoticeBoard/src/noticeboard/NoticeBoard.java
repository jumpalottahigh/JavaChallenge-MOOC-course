package noticeboard;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;

public class NoticeBoard implements Runnable {

    private JFrame frame;

    @Override
    public void run() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       
        
        createComponents(frame);
        
        frame.pack();
         frame.setSize(new Dimension(400, 200));
        frame.setVisible(true);
    }

    private void createComponents(Container container) {
        
        GridLayout layout = new GridLayout(3,1);
        container.setLayout(layout);
        
        JTextField tf = new JTextField();
        JButton btn = new JButton("Copy!");
        JLabel lbl = new JLabel();
        
        ActionEventListener ael = new ActionEventListener(tf, lbl);
        btn.addActionListener(ael);
        
        container.add(tf);
        container.add(btn);
        container.add(lbl);
        
      
        
    }
}
