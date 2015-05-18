
import clicker.ui.Main;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import junit.framework.Assert;
import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("39.3")
public class XUserInterfaceTest extends FestSwingJUnitTestCase {

    private static String NAPIN_TEKSTI = "Click!";
    private FrameFixture frame;

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
            c = frame.robot.finder().find(new XUserInterfaceTest.M("(?i).*" + teksti + ".*"));
        } catch (ComponentLookupException e) {
            fail("Couldn't find " + tyyppi.toString().replaceAll("class", "") + "-typed component that reads \"" + teksti + "\".\n\nAdditional information:\n" + e);
        }
        assertEquals("Component that reads \"" + teksti + "\" isn't of the right type!",
                tyyppi,
                c.getClass());
        return c;
    }

    @Override
    protected void onSetUp() {
        ApplicationLauncher.application(Main.class).start();
        robot().settings().delayBetweenEvents(100);
        frame = WindowFinder.findFrame(JFrame.class).using(robot());

        Dimension d = frame.component().getSize();
        Assert.assertTrue("Set JFrame's width to be at least 200px and height 100px", d.height > 70 && d.width > 150);

    }

    @Test
    public void hasComponents() {
        JLabelFixture teksti = null;
        try {
            teksti = frame.label(JLabelMatcher.withText("0"));
        } catch (ComponentLookupException e) {
            Assert.fail("User interface didn't have a text field with value 0. Check that user interface has JLabel-component which has value 0 in the beginning.\n\n\nAdditional information:\n" + e);
        }

        varmista("click", JButton.class);

        System.out.println("");
    }

    @Test
    public void isEverythingVisible() {
        JFrame jf = frame.targetCastedTo(JFrame.class);
        Component[] cs = jf.getContentPane().getComponents();
        for (Component component : cs) {
            Point p = component.getLocation();
            int h = component.getHeight();
            int w = component.getWidth();
            Rectangle rect = component.getBounds();

            assertFalse("Component "+component.getClass()+" isn't visible\n"
                    + "when you add it to container, remember to give it position like\n"
                    + "container.add(component, BorderLayout.NORTH);\n"
                    + "button and label must be set to different positions like BorderLayout.NORTH and BorderLayout.SOUTH"
                    + "", h==0 || w==0);
        }
    }

    @Test
    public void buttonPressedOnce() {
        JLabelFixture teksti = frame.label();
        String tekstiEnnen = teksti.text();
        Assert.assertEquals("Text field's value should be \"0\" at first.", "0", tekstiEnnen);

        final JButton nappi = (JButton) varmista("click", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                nappi.doClick();
            }
        });

        String tekstiJalkeen = frame.label().text();
        Assert.assertEquals("Text field's value should be \"1\" after pressing button once.", "1", tekstiJalkeen);
    }

    @Test
    public void buttonPressedThreeTimes() {
        JLabelFixture teksti = frame.label();
        String tekstiEnnen = teksti.text();
        Assert.assertEquals("Text field's value should be \"0\" at first.", "0", tekstiEnnen);

        final JButton nappi = (JButton) varmista("click", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                nappi.doClick();
                nappi.doClick();
                nappi.doClick();
            }
        });

        String tekstiJalkeen = frame.label().text();
        Assert.assertEquals("Text field's value should be \"3\" after pressing button three times.", "3", tekstiJalkeen);
    }
}
