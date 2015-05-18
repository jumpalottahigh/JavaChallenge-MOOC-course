
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@Points("6")
public class PromissoryNoteTest {

    String klassName = "PromissoryNote";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "an instance variable of type HashMap<String,Double> to keep track of debts");
    }

    @Test
    public void isHashMap() {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();
        assertTrue("Add to "+klassName+" an instance variable of type HashMap<String, Double>", kentat.length==1 );
        assertTrue("Class "+klassName+" should have an instance variable of type Map<String, Double>", kentat[0].toString().contains("Map"));
    }

    @Test
    public void testConstructor() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for " + klassName + " a public constructor: public " + klassName + "()", ctor.isPublic());
        String v = "Error caused by code new PromissoryNote();";
        ctor.withNiceError(v).invoke();
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    @Test
    public void setLoan() throws Throwable {
        String metodi = "setLoan";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(String toWhom, double value) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(String.class, double.class).isPublic());

        String v = "\nError caused by code PromissoryNote debts = new PromissoryNote(); "
                + "v.setLoan(\"Pekka\", 5.0);";


        klass.method(olio, metodi)
                .returningVoid().taking(String.class, double.class).withNiceError(v).invoke("Pekka", 5.0);
    }

    @Test
    public void howMuchIsTheDebt() throws Throwable {
        String metodi = "howMuchIsTheDebt";

        Object olio = luo();

        assertTrue("Create method " + metodi + "(String whose) for class " + klassName,
                klass.method(olio, metodi)
                .returning(double.class).taking(String.class).isPublic());

        String v = "\nError caused by code PromissoryNote debts = new PromissoryNote(); "
                + "v.howMuchIsTheDebt(\"Pekka\");";

        klass.method(olio, metodi)
                .returning(double.class).taking(String.class).withNiceError(v).invoke("Pekka");
    }

    @Test
    public void testPromissoryNote() throws Exception {
        Object velkakirja = luoPromissoryNote();
        testaaVelka(velkakirja, "Arto", 919.83);
        testaaVelka(velkakirja, "Matti", 32.1);
        testaaVelka(velkakirja, "Joel", -5);
        testaaAsettamatonVelka(velkakirja, "Mikael");
    }

    private void testaaVelka(Object velkakirja, String kenelle, double maara) {
        setLoan(velkakirja, kenelle, maara);
        double velka = howMuchIsTheDebt(velkakirja, kenelle);
        if (velka <= (maara - 0.1) || velka >= (maara + 0.1)) {
            fail("Person " + kenelle + " was set to have a loan of " + maara
                    + ", but returned debt was: " + velka);
        }
    }

    private void testaaAsettamatonVelka(Object velkakirja, String kenelle) {
        double velka = howMuchIsTheDebt(velkakirja, kenelle);
        if (velka != 0) {
            fail("Person " + kenelle + " wasn't set to have any loan, "
                    + "but returned debt was: " + velka);
        }
    }

    private Object luoPromissoryNote() throws Exception {
        return Class.forName("PromissoryNote").getConstructor().newInstance();
    }

    private void setLoan(Object velkakirja, String kenelle, double maara) {
        Method metodi;
        try {
            metodi = velkakirja.getClass().getMethod("setLoan", String.class, double.class);
        } catch (Exception e) {
            fail("Class PromissoryNote doesn't have method: public void setLoan(String toWhom, double value).");
            return;
        }

        if (!metodi.getReturnType().equals(void.class)) {
            fail("PromissoryNote's method setLoan(String toWhom, double value) shouldn't have a return value.");
            return;
        }

        try {
            metodi.invoke(velkakirja, kenelle, maara);
        } catch (Exception e) {
            fail("There happened an exception in PromissoryNote's method setLoan: " + e.toString());
        }
    }

    private double howMuchIsTheDebt(Object velkakirja, String kuka) {
        Method metodi;
        try {
            metodi = velkakirja.getClass().getMethod("howMuchIsTheDebt", String.class);
        } catch (Exception e) {
            fail("PromissoryNote doesn't have method: public double howMuchIsTheDebt(String whose).");
            return -1;
        }

        if (!metodi.getReturnType().equals(double.class)) {
            fail("PromissoryNote's method howMuchIsTheDebt(String whose) should return a value of type double.");
            return -1;
        }

        try {
            return (Double) metodi.invoke(velkakirja, kuka);
        } catch (java.lang.reflect.InvocationTargetException e) {
            fail("Check that null-references aren't tried to change to variables of primitive data type.");
            return -1;
        } catch (Exception e) {
            fail("There happened an exception in PromissoryNote's method howMuchIsTheDebt: " + e.toString());
            return -1;
        }
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("you do not need \"static variables\", remove from class " + klassName + " the following variable: " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All instance variables should be private but class " + klassName + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("The class " + klassName + " should only have " + m + ", remove others", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}
