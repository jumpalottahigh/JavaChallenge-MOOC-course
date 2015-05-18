
import clicker.applicationlogic.Calculator;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("39.2")
public class ClickListenerTest {

    public static final String SOVELLUSLOGIIKAN_LUOKAN_NIMI =
            "clicker.ui.ClickListener";
    Reflex.ClassRef<Object> klass;
    String klassName = SOVELLUSLOGIIKAN_LUOKAN_NIMI;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void twoParameterConstructor() throws Throwable {
        Reflex.MethodRef2<Object, Object, Calculator, JLabel> ctor = klass.constructor().taking(Calculator.class, JLabel.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: public " + s(klassName) + "(Calculator calculator, JLabel text)", ctor.isPublic());
        String v = "error caused by code new ClickListener(new PersonalCalculator(), new JLabel());";
        ctor.withNiceError(v).invoke(new Calculator() {
            @Override
            public int giveValue() {
                return 0;
            }

            @Override
            public void increase() {
            }
        }, new JLabel());
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 10, "");
    }

    @Test
    public void implementsInterface() {
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = ActionListener.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class ClickListener implement interface ActionListener?");
        }
    }

    @Test
    public void buttonWorks() throws Throwable {
        Reflex.MethodRef2<Object, Object, Calculator, JLabel> ctor = klass.constructor().taking(Calculator.class, JLabel.class).withNiceError();

        String v = "error caused by code \n"
                + "ClickListener listener = new ClickListener(new PersonalCalculator(), new JLabel());\n"
                + "listener.actionPerformed(new ActionEvent(...));";
        Object o = ctor.withNiceError(v).invoke(new Calculator() {
            @Override
            public int giveValue() {
                return 0;
            }

            @Override
            public void increase() {
            }
        }, new JLabel());

        klass.method(o, "actionPerformed").returningVoid().
                taking(ActionEvent.class).withNiceError(v).invoke(new ActionEvent(new JButton(), 1, "foo"));
    }

    @Test
    public void testClickListener() {
        String nappiTeksti = "Click!";

        Calculator laskuri = CalculatorTest.luoSovelluslogiikanInstanssi();
        int laskurinNumero = laskuri.giveValue();

        JLabel tekstiLabel = new JLabel();
        JButton sourceButton = new JButton(nappiTeksti);

        ActionListener tapahtumanKuuntelija = luoActionListener(laskuri, tekstiLabel);

        ActionEvent tapahtuma = new ActionEvent(sourceButton, 1, "command");
        tapahtumanKuuntelija.actionPerformed(tapahtuma);

        Assert.assertEquals("Text of the button shouldn't change when button is pressed.", nappiTeksti, sourceButton.getText());
        Assert.assertEquals("When button is pressed, value of the Calculator should increase by one.", laskurinNumero + 1, laskuri.giveValue());
        Assert.assertEquals("When button is pressed, text should be updated to be the new value of the calculator. Value should be \"" + (laskurinNumero + 1) + "\", but now it was \"" + tekstiLabel.getText().trim() + "\".", "" + (laskurinNumero + 1), tekstiLabel.getText().trim());
    }

    private ActionListener luoActionListener(Calculator laskuri, JLabel teksti) {
        Reflex.ClassRef<?> luokka;
        try {
            luokka = Reflex.reflect(SOVELLUSLOGIIKAN_LUOKAN_NIMI);
        } catch (Throwable t) {
            Assert.fail("Class " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " doesn't exist. In this assignment you have to create that class.");
            return null;
        }
        if (!ActionListener.class.isAssignableFrom(
                luokka.getReferencedClass())) {
            Assert.fail("Class " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " should "
                    + "implement interface " + Calculator.class.getName());
        }

        Object instanssi;
        try {
            instanssi = luokka.constructor().taking(Calculator.class, JLabel.class).invoke(laskuri, teksti);
        } catch (Throwable t) {
            Assert.fail("Class " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " isn't public or it doesn't have public constructor public ClickListener(Calculator calculator, JLabel text).");
            return null;
        }

        return (ActionListener) instanssi;
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "").replace("java.io.", "");
    }

    private String s(String klassName) {
        return klassName.substring(klassName.lastIndexOf(".") + 1);
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("you do not need \"static variables\", remove from class " + s(klassName) + " the following variable: " + kentta(field.toString(), s(klassName)), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All instance variables should be private but class " + s(klassName) + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("The class " + s(klassName) + " should only have " + m + ", remove others", var <= n);
        }
    }
}