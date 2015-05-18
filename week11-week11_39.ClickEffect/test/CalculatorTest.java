
import clicker.applicationlogic.Calculator;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("39.1")
public class CalculatorTest {

    public static final String SOVELLUSLOGIIKAN_LUOKAN_NIMI =
            "clicker.applicationlogic.PersonalCalculator";
    Reflex.ClassRef<Object> klass;
    String klassName = SOVELLUSLOGIIKAN_LUOKAN_NIMI;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "instance variable for value");
    }

    @Test
    public void emptyConstructor() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: public " + klassName + "()", ctor.isPublic());
        String v = "error caused by code new PersonalCalculator();";
        ctor.withNiceError(v).invoke();
    }

    @Test
    public void implementsInterface() {
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = Calculator.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class PersonalCalculator implement interface Calculator?");
        }
    }

    @Test
    public void testCalculator() throws Throwable {
        Calculator olio = (Calculator) luo();

        String k1 = ""
                + "\nCalculator c = new PersonalCalculator();\n"
                + "c.giveValue();\n";

        assertEquals(k1, 0, (int) klass.method(olio, "giveValue").returning(int.class).takingNoParams().withNiceError(k1).invoke());

        k1 = ""
                + "\nCalculator c = new PersonalCalculator();\n"
                + "c.increase();\n"
                + "c.giveValue();\n";

        klass.method(olio, "increase").returningVoid().takingNoParams().withNiceError(k1).invoke();

        assertEquals(k1, 1, (int) klass.method(olio, "giveValue").returning(int.class).takingNoParams().withNiceError(k1).invoke());
    }

    @Test
    public void testPersonalCalculator() {
        Calculator laskuri = luoSovelluslogiikanInstanssi();
        Assert.assertEquals("Calculator's value should be 0 at first", 0, laskuri.giveValue());

        for (int i = 1; i < 100; i++) {
            laskuri.increase();
            Assert.assertEquals("After " + i + " increase()-calls, Calculator's value should be " + i + ".", i, laskuri.giveValue());
        }
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    public static Calculator luoSovelluslogiikanInstanssi() {
        Reflex.ClassRef<?> luokka;
        try {
            luokka = Reflex.reflect(SOVELLUSLOGIIKAN_LUOKAN_NIMI);
        } catch (Throwable t) {
            Assert.fail("Class " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " doesn't exist. In this assignment you have to create that class.");
            return null;
        }
        if (!Calculator.class.isAssignableFrom(
                luokka.getReferencedClass())) {
            Assert.fail("Class " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " should "
                    + "implement interface " + Calculator.class.getName());
        }

        Object instanssi;
        try {
            instanssi = luokka.constructor().takingNoParams().invoke();
        } catch (Throwable t) {
            Assert.fail("Class " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " doesn't have public constructor which takes no parameters.");
            return null;
        }

        return (Calculator) instanssi;
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
