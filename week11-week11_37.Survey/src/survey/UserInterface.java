package survey;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.*;

public class UserInterface implements Runnable {

    private JFrame frame;

    @Override
    public void run() {
        // Create your app here
        frame = new JFrame("Survey");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        createComponents(frame.getContentPane());
        
        frame.pack();
        frame.setSize(new Dimension(100,200));
        frame.setVisible(true);
    }
    
    private void createComponents(Container container){
        //JPanel panel = new JPanel(); 
        LayoutManager layout  = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(layout);
       
        JLabel are = new JLabel("Are you?");
        JCheckBox yes = new JCheckBox("Yes!");
        JCheckBox no = new JCheckBox("No!");
        
        JLabel why = new JLabel("Why?");
        
        ButtonGroup bg = new ButtonGroup();
        JRadioButton rb1 = new JRadioButton("No reason.");
        JRadioButton rb2 = new JRadioButton("Because it is fun!");
        bg.add(rb1);
        bg.add(rb2);
        
        JButton btn = new JButton("Done!");
        
        container.add(are);
        container.add(yes);
        container.add(no);
        container.add(why);
        container.add(rb1);
        container.add(rb2);
        container.add(btn);
       
        
        //container.add(panel);

    }


    public JFrame getFrame() {
        return frame;
    }
}
