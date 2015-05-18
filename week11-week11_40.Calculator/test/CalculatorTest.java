
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.Collection;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import junit.framework.Assert;
import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTest extends FestSwingJUnitTestCase {

    private FrameFixture frame;

    @Override
    protected void onSetUp() {
        try {
            ApplicationLauncher.application(Main.class).start();

            robot().settings().delayBetweenEvents(100);

            frame = WindowFinder.findFrame(JFrame.class).using(robot());
        } catch (Throwable t) {
            Assert.fail("Does your user interface create a JFrame-object and is it set visible with statement frame.setVisible(true)?");
        }
    }

    // ulkonäkö
    @Points("40.1")
    @Test
    public void sizeAndTitleCorrect() {
        Dimension d = frame.component().getSize();
        Assert.assertTrue("Set JFrame's width to be at least 300px and height 150px", d.height > 100 && d.width > 200);
        Assert.assertEquals("Set correct title to JFrame", "Calculator", frame.component().getTitle());
    }

    @Points("40.1")
    @Test
    public void layoutOK() {
        LayoutManager lm = ((JFrame) frame.component()).getContentPane().getLayout();

        assertTrue("Layout manager of the graphic calculator should be GridLayout", lm instanceof GridLayout);

        GridLayout gl = (GridLayout) lm;

        assertEquals("GridLayout which is graphic calculator's layout manager has an incorrect number of lines",
                3, gl.getRows());

        assertEquals("GridLayout which is graphic calculator's layout manager has an incorrect number of columns",
                1, gl.getColumns());

        assertTrue("JPanel, which contains the buttons, should have GridLayout as its layout manager with 1 row and 3 columns", paneliKunnossa());

    }

    @Points("40.1")
    @Test
    public void testComponentsAreFound() {

        varmista("\\+", JButton.class);
        varmista("-", JButton.class);
        varmista("Z", JButton.class);

        Collection<JTextField> kentat = haeTekstikentat();
        assertEquals("Calculator has wrong amount of JTextField-elements", 2, kentat.size());
        int e = 0;
        int d = 0;
        for (JTextField jTextField : kentat) {
            if (jTextField.isEnabled()) {
                e++;
            } else {
                d++;
            }
        }

        assertEquals("Calculator has wrong amount of JTextField-elements which are set to enabled-state", 1, e);
        assertEquals("Calculator has wrong amount of JTextField-elements which are not enabled", 1, d);
    }

    @Points("40.1")
    @Test
    public void testOrder() {
        JFrame jf = frame.targetCastedTo(JFrame.class);

        Component[] cs = jf.getContentPane().getComponents();

        assertEquals("First component should be JTextField ",
                JTextField.class,
                cs[0].getClass());
        assertEquals("Second component should be JTextField ",
                JTextField.class,
                cs[1].getClass());

        assertEquals("First JTextField's content is wrong in the beginning",
                "0",
                ((JTextField) cs[0]).getText());

        assertEquals("First component should be JTextField which is not enabled, meaning you can't write any text to it",
                false,
                ((JTextField) cs[0]).isEnabled());
        assertEquals("Second component should be JTextField which is enabled, meaning that you can write text to it",
                true,
                ((JTextField) cs[1]).isEnabled());

        assertTrue(editoitavaTekstikentta() != null);
        assertTrue(tulosTekstikentta() != null);
    }

    // plus miinus, nollaus
    @Test
    @Points("40.2")
    public void plusMinusAndResettingWorks() {

        final JButton plus = (JButton) varmista("\\+", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("2");
                plus.doClick();
            }
        });

        assertEquals("When user writes 2 to input field and presses +, result field should have value", "2", tulosTekstikentta().getText());

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("7");
                plus.doClick();
            }
        });

        assertEquals("At first result field has value 2. When user writes 7 to input field and presses +, result field should have value", "9", tulosTekstikentta().getText());

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("101");
                plus.doClick();
            }
        });

        assertEquals("At first result field has value 9. When user writes 101 to input field and presses +, result field should have value", "110", tulosTekstikentta().getText());

        final JButton miinus = (JButton) varmista("-", JButton.class);

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("11");
                miinus.doClick();
            }
        });

        assertEquals("At first result field has value 110. When user writes 11 to input field and presses -, result field should have value", "99", tulosTekstikentta().getText());

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("200");
                miinus.doClick();
            }
        });

        assertEquals("At first result field has value 99. When user writes 200 to input field and presses -, result field should have value", "-101", tulosTekstikentta().getText());

        final JButton nollaa = (JButton) varmista("Z", JButton.class);

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("200");
                nollaa.doClick();
            }
        });

        assertEquals("When result field has value -110 and user presses Z, result field should have value", "0", tulosTekstikentta().getText());
    }

    // nollausnappi disable, virheiden käsittely
    @Points("40.3")
    @Test
    public void resettingButtonEnabledAndDisabled() {
        final JButton nollaa = (JButton) varmista("Z", JButton.class);
        assertFalse("At first Z-button should be disabled", nollaa.isEnabled());

        final JButton plus = (JButton) varmista("\\+", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("2");
                plus.doClick();
            }
        });

        assertTrue("When result field has value which isn't 0, Z-button should be enabled", nollaa.isEnabled());

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("200");
                nollaa.doClick();
            }
        });

        assertFalse("After pressing Z-button, the button should be disabled", nollaa.isEnabled());
    }

    @Points("40.3")
    @Test
    public void inputFieldGoesEmpty() {
        final JButton plus = (JButton) varmista("\\+", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("5");
                plus.doClick();
            }
        });

        assertEquals("When pressing +, input field should be cleared", "", editoitavaTekstikentta().getText());

        final JButton minus = (JButton) varmista("-", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("2");
                minus.doClick();
            }
        });

        assertEquals("Whe pressing -, input field should be cleared", "", editoitavaTekstikentta().getText());

        final JButton nollaa = (JButton) varmista("Z", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("2");
                nollaa.doClick();
            }
        });

        assertEquals("Whe pressing R, input field should be cleared", "", editoitavaTekstikentta().getText());
    }

    @Points("40.3")
    @Test
    public void invalidInputsIgnoredWhenPlussing() {
        final JButton plus = (JButton) varmista("\\+", JButton.class);

        try {

            GuiActionRunner.execute(new GuiTask() {
                @Override
                protected void executeInEDT() throws Throwable {
                    editoitavaTekstikentta().setText("xx");
                    plus.doClick();
                }
            });
        } catch (Exception e) {
            fail("When input field has an input which is not a valid number, and user presses +, input field should be cleared and nothing else should happen\n"
                    + "now happened "+e);
        }

        assertEquals("When input field has an input which is not a valid number, and user presses +, input field should be cleared and nothing else should happen\n"
                + "value of the input field", "", editoitavaTekstikentta().getText());
        assertEquals("When input field has an input which is not a valid number, and user presses +, input field should be cleared and nothing else should happen\n"
                + "Result field was at first 0 and after invalid input it was", "0", tulosTekstikentta().getText());

    }

    @Points("40.3")
    @Test
    public void invalidInputsIgnoredWhenSubstracting() {
        final JButton plus = (JButton) varmista("\\-", JButton.class);

        try {

            GuiActionRunner.execute(new GuiTask() {
                @Override
                protected void executeInEDT() throws Throwable {
                    editoitavaTekstikentta().setText("xx");
                    plus.doClick();
                }
            });
        } catch (Exception e) {
            fail("When input field has an input which is not a valid number, and user presses -, input field should be cleared and nothing else should happen\n"
                    + "now happened "+e);
        }

        assertEquals("When input field has an input which is not a valid number, and user presses -, input field should be cleared and nothing else should happen\n"
                + "value of the input field", "", editoitavaTekstikentta().getText());
        assertEquals("When input field has an input which is not a valid number, and user presses -, input field should be cleared and nothing else should happen\n"
                + "Result field was at first 0 and after invalid input it was", "0", tulosTekstikentta().getText());

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

    public boolean paneliKunnossa() {
        Collection<Component> cs = frame.robot.finder().findAll(new GenericTypeMatcher(JPanel.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        boolean ok = false;
        for (Component component : cs) {
            JPanel jp = (JPanel) component;
            LayoutManager lm = jp.getLayout();
            if (lm instanceof GridLayout) {
                if (((GridLayout) lm).getRows() == 1 && ((GridLayout) lm).getColumns() == 3) {
                    ok = true;
                    break;
                }
            }
        }

        return ok;
    }

    public Collection<JTextField> haeTekstikentat() {
        return frame.robot.finder().findAll(new GenericTypeMatcher(JTextField.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });
    }

    public JButton minus() {
        Collection<JButton> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JButton.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JButton n : tk) {
            if (n.getText().equals("-")) {
                return n;
            }
        }

        return null;
    }

    public JButton reset() {
        Collection<JButton> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JButton.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JButton n : tk) {
            if (n.getText().equals("Z")) {
                return n;
            }
        }

        return null;
    }

    public JButton plus() {
        Collection<JButton> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JButton.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JButton n : tk) {
            if (n.getText().equals("+")) {
                return n;
            }
        }

        return null;
    }

    public JTextField editoitavaTekstikentta() {
        Collection<JTextField> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JTextField.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JTextField jTextField : tk) {
            if (jTextField.isEnabled() == true) {
                return jTextField;
            }
        }

        return null;
    }

    public JTextField tulosTekstikentta() {
        Collection<JTextField> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JTextField.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JTextField jTextField : tk) {
            if (jTextField.isEnabled() == false) {
                return jTextField;
            }
        }

        return null;
    }

    public Component varmista(String teksti, Class tyyppi) {
        Component c = null;
        try {
            c = frame.robot.finder().find(new CalculatorTest.M("(?i).*" + teksti + ".*"));
        } catch (ComponentLookupException e) {
            fail("Couldn't find " + tyyppi.toString().replaceAll("class", "") + "-typed component, that reads \"" + teksti + "\".\n\nAdditional information:\n" + e);
        }
        assertEquals("Component that reads \"" + teksti + "\" isn't of the right type!",
                tyyppi,
                c.getClass());
        return c;
    }
}
