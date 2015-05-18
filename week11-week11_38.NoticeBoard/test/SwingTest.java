
import fi.helsinki.cs.tmc.edutestutils.Points;
import noticeboard.Main;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import junit.framework.Assert;
import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import static org.junit.Assert.*;

@Points("38")
public class SwingTest extends FestSwingJUnitTestCase {

    private FrameFixture frame;

    @Override
    protected void onSetUp() {
        try {
            ApplicationLauncher.application(Main.class).start();

            robot().settings().delayBetweenEvents(300);
            frame = WindowFinder.findFrame(JFrame.class).using(robot());
        } catch (Throwable t) {
            Assert.fail("Do you create JFrame in the run-method: frame = new JFrame(\"Frame\") and set it visible with method setVisible?\n\n\nAdditional information: " + t);
        }

        Dimension d = frame.component().getSize();
        Assert.assertTrue("Set JFrame's width to be at least 400px and height 200px", d.height > 150 && d.width > 350);

    }

    @Test
    public void layoutOK() {
        LayoutManager lm = ((JFrame) frame.component()).getContentPane().getLayout();

        Assert.assertTrue("Layout manager of the user interface should be GridLayout", lm instanceof GridLayout);

        GridLayout gl = (GridLayout) lm;

        Assert.assertEquals("GridLayout's number of rows is incorrect",
                3, gl.getRows());

        Assert.assertEquals("GridLayout's number of columns is incorrect",
                1, gl.getColumns());
    }

    @Test
    public void correctlyNamedButton() {
        varmista("Copy!", JButton.class);
        try {
            frame.label();
            frame.textBox();
        } catch (Throwable t) {
            Assert.fail("Does your user interface have all the components? It should have"
                    + " JLabel, JTextField and JButton.");
        }
    }

    @Test
    public void testOrder() {
        JFrame jf = frame.targetCastedTo(JFrame.class);
        Component[] cs = jf.getContentPane().getComponents();
        assertEquals("First component should be JTextField.",
                JTextField.class,
                cs[0].getClass());
        assertEquals("Second component should be JButton.",
                JButton.class,
                cs[1].getClass());
        assertEquals("Third component should be JLabel.",
                JLabel.class,
                cs[2].getClass());
    }

    @Test
    public void testCopying() {
        yksiMerkkiJono("test");
    }

    @Test
    public void testSendingMessage() {
        yksiMerkkiJono("asd");
        yksiMerkkiJono("Potato");
        yksiMerkkiJono("isoviha");
        yksiMerkkiJono("red");
        yksiMerkkiJono("stone");
        yksiMerkkiJono("karpaatti");
        yksiMerkkiJono("MOOC");
        yksiMerkkiJono("is");
        yksiMerkkiJono("awesome");
        yksiMerkkiJono("kthxbye");
        yksiMerkkiJono("ääkkösiä");

    }

    public void yksiMerkkiJono(String expected) {
        JLabelFixture messageLabel = null;
        JTextComponentFixture syote = null;
        JButtonFixture paivita = null;
        String received = "";
        try {
            messageLabel = frame.label();
            syote = frame.textBox();
            paivita = frame.button();
        } catch (Throwable t) {
            Assert.fail("Does your user interface have all the components? It should have"
                    + " JLabel, JTextField and JButton.");
        }
        try {
            syote.focus().setText(expected);
            paivita.focus().click();
            received = messageLabel.text();
            messageLabel.requireText(expected);
            syote.requireEmpty();
        } catch (Throwable t) {
            Assert.fail("When JButton is pressed, do you set the text from JTextField to JLabel?\n"
                    + "Do you also remember to empty JTextField after copying, setting its content to string \"\"?\n"
                    + "\nAction event listener must be created for JButton which handles the text copying to JLabel and empties the JTextField"
                    + "\n\n\nAdditional information: " + t);
        }
    }

    static class M implements ComponentMatcher {

        public final String pattern;

        public M(String p) {
            pattern = p;
        }

        @Override
        public boolean matches(Component cmpnt) {
            if (!(cmpnt instanceof AbstractButton)) {
                return false;
            }
            AbstractButton ab = (AbstractButton) cmpnt;
            return ab.getText().matches(pattern);
        }

        @Override
        public String toString() {
            return "M{" + "pattern=" + pattern + '}';
        }
    }

    public Component varmista(String teksti, Class tyyppi) {
        Component c = null;
        try {
            c = frame.robot.finder().find(new SwingTest.M("(?i).*" + teksti + ".*"));
        } catch (ComponentLookupException e) {
            Assert.fail("Couldn't find " + tyyppi.toString().replaceAll("class", "") + "-typed component, that reads \"" + teksti + "\".\n\nAdditional information:\n" + e);
        }
        Assert.assertEquals("Component that reads \"" + teksti + "\" isn't of the right type!",
                tyyppi,
                c.getClass());
        return c;
    }
}
