
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.*;
import junit.framework.Assert;
import survey.Main;
import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("37")
public class SurveyTest extends FestSwingJUnitTestCase {

    private FrameFixture frame;

    @Override
    protected void onSetUp() {
        try {
            ApplicationLauncher.application(Main.class).start();

            robot().settings().delayBetweenEvents(100);

            frame = WindowFinder.findFrame(JFrame.class).using(robot());

        } catch (Throwable t) {
            Assert.fail("Are you sure you create JFrame-object and set it visible with statement frame.setVisible(true)?");
        }
        Dimension d = frame.component().getSize();
        Assert.assertTrue("Set JFrame's width to be at least 100px and height 200px", d.height > 150 && d.width > 50);

        frame.resizeTo(new Dimension(250, 400));

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
            c = frame.robot.finder().find(new SurveyTest.M("(?i).*" + teksti + ".*"));
        } catch (ComponentLookupException e) {
            fail("Didn't find "+tyyppi.toString().replaceAll("class", "") +"-typed component which reads \"" + teksti + "\".\n\nAdditional information:\n" + e);
        }
        assertEquals("Component which reads \"" + teksti + "\" isn't of the right type!",
                tyyppi,
                c.getClass());
        return c;
    }

    @Test
    public void testComponentsAreFound() {
        LayoutManager lm = ((JFrame)frame.component()).getContentPane().getLayout();

        assertTrue("User interface's layout manager should be BoxLayout",lm instanceof BoxLayout);

        try {
            JLabelFixture k1 = frame.label(JLabelMatcher.withText("Are you?"));
        } catch (ComponentLookupException e) {
            fail("You didn't add JLabel-component with text \"Are you?\".\n\nAdditional information:\n" + e);
        }

        try {
            JLabelFixture k2 = frame.label(JLabelMatcher.withText("Why?"));
        } catch (ComponentLookupException e) {
            fail("You didn't add JLabel-component with text \"Why?\".\n\nAdditional information:\n" + e);
        }

        varmista("Yes!", JCheckBox.class);
        varmista("No!", JCheckBox.class);
        varmista("No reason.", JRadioButton.class);
        varmista("Because it is fun!", JRadioButton.class);
        varmista("Done!", JButton.class);
    }

    @Test
    public void testRadioButtons() {
        JRadioButton r1 = (JRadioButton) varmista("No reason.", JRadioButton.class);
        JRadioButton r2 = (JRadioButton) varmista("Because it is fun!", JRadioButton.class);

        assertFalse("At first no option should be chosen!",
                r1.isSelected() || r2.isSelected());

        frame.robot.focus(r1);
        frame.robot.click(r1);
        frame.robot.waitForIdle();
        assertTrue("Option \"No reason.\" should be chosen when user clicks it. Check that user interface is big enough so that button is visible!",
                r1.isSelected());
        assertFalse("Option \"Because it is fun!\" shouldn't be chosen when option \"No reason.\" is clicked",
                r2.isSelected());

        frame.robot.focus(r2);
        frame.robot.click(r2);
        frame.robot.waitForIdle();
        assertTrue("Option \"Because it is fun!\" should be chosen when user clicks it.",
                r2.isSelected());
        assertFalse("Option \"No reason.\" should be unchosen when option \"Because it is fun!\" is clicked.",
                r1.isSelected());

        frame.robot.focus(r1);
        frame.robot.click(r1);
        frame.robot.waitForIdle();
        assertTrue("Option \"No reason.\" should be chosen when user clicks it.",
                r1.isSelected());
        assertFalse("Option \"Because it is fun!\" should be unchosen when option \"No reason.\" is clicked.",
                r2.isSelected());

    }

    @Test
    public void testOrder() {
        JFrame jf = frame.targetCastedTo(JFrame.class);
        assertEquals("You're not using BoxLayout!",
                BoxLayout.class,
                jf.getContentPane().getLayout().getClass());
        Component[] cs = jf.getContentPane().getComponents();
        assertEquals("First component should be JLabel.",
                JLabel.class,
                cs[0].getClass());
        assertEquals("Second component should be JCheckBox.",
                JCheckBox.class,
                cs[1].getClass());
        assertEquals("Third component should be JCheckBox.",
                JCheckBox.class,
                cs[2].getClass());
        assertEquals("Fourth component should be JLabel.",
                JLabel.class,
                cs[3].getClass());
        assertEquals("Fifth component should be JRadioButton.",
                JRadioButton.class,
                cs[4].getClass());
        assertEquals("Sixth component should be JRadioButton.",
                JRadioButton.class,
                cs[5].getClass());
        assertEquals("Last component should be JButton.",
                JButton.class,
                cs[6].getClass());
    }
}
