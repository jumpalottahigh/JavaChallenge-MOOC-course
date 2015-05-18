
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import greeter.ui.Main;
import junit.framework.Assert;

@Points("36")
public class SwingTest extends FestSwingJUnitTestCase {

    private FrameFixture frame;

    @Override
    protected void onSetUp() {
        try {
            ApplicationLauncher.application(Main.class).start();

            robot().settings().delayBetweenEvents(100);

            frame = WindowFinder.findFrame(JFrame.class).using(robot());
        } catch (Throwable t) {
            Assert.fail("Are you sure you create JFrame-object in your user interface and you set it visible with statement frame.setVisible(true)?");
        }
    }

    @Test
    public void sizeAndTitleCorrect() {
        Dimension d = frame.component().getSize();
        Assert.assertTrue("Set JFrame's width to be at least 400px and height at least 200px",d.height>150 && d.width>300);
        Assert.assertEquals("Set correct title for JFrame", "Swing on",frame.component().getTitle());
    }

    @Test
    public void testSendingMessage() {
        JLabelFixture messageLabel = null;
        String oli = "";
        try {
            messageLabel = frame.label();
        } catch (Throwable t) {
            Assert.fail("Are you sure you create JLabel-object in your user interface and add it to JFrame's Container-object?");
        }

        try {
            oli = messageLabel.text();
            messageLabel.requireText("Hi!");
        } catch (Throwable t) {
            Assert.fail("Does your JLabel-object's text equal \"Hi!\"?\n"
                    + "Now it was: \"" + oli + "\"");
        }

    }
}
