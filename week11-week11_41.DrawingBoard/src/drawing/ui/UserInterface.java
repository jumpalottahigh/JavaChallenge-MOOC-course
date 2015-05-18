package drawing.ui;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class UserInterface implements Runnable {

    private JFrame frame;
    private DrawingBoard board;

    @Override
    public void run() {
        // DON'T CHANGE THIS CODE!!
        frame = new JFrame("Drawing Board");
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container container) {
        board = new DrawingBoard();
        container.add(board);
    }

    public JFrame getFrame() {
        return frame;
    }
}
