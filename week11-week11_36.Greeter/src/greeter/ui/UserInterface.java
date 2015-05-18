package greeter.ui;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class UserInterface implements Runnable {

    private JFrame frame;
    private Dimension dim;
    
    @Override
    public void run() {
        this.frame = new JFrame("Swing on");
        
        dim = new Dimension(400,200);
        
        this.frame.setSize(dim);
        
        this.createComponents(frame);
        this.frame.setVisible(true);
    }

    private void createComponents(Container container) {
        //this.frame = new JFrame("Hello!");
        //container.add(frame);
        //this.frame.setVisible(true);
        container.add(new JLabel("Hi!"));

    }

    public JFrame getFrame() {
        return frame;
    }
}
