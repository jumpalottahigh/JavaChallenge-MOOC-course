
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class B_CalculatorTest {
    String klassName = "Calculator";
    Reflex.ClassRef<Object> klass;

    String luokanNimi = "Calculator";
    Class c;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
        try {
            c = ReflectionUtils.findClass(luokanNimi);
        } catch (Throwable e) {
        }
    }

    @Points("3.2")
    @Test
    public void classIsPublic() {
        assertTrue("Class " + klassName + " has to be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Points("3.2")
    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 2, "one instance variable (the one that is of the type Reader) and instance variable which remembers" +
                            " the number of completed operations");
    }

    @Points("3.2")
    @Test
    public void hasMethodStart() throws Throwable {
        String metodi = "start";
        new MockInOut("end\n");

        String vv = "error caused by code Calculator calculator = new Calculator();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("Create method public void " + metodi + "() for class " + klassName, klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nError caused by code Calculator calculator = new Calculator(); "
                + "calculator.start();";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Points("3.2")
    @Test
    public void hasSum() throws Throwable {
        String metodi = "sum";

        new MockInOut("2\n3\n");

        String vv = "error caused by code Calculator calculator = new Calculator();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("Create method private void " + metodi + "() for class " + klassName, klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPrivate());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError().invoke();

    }

    @Points("3.2")
    @Test
    public void hasDifference() throws Throwable {
        new MockInOut("2\n3\n");
        String metodi = "difference";
        String vv = "error caused by code Calculator calculator = new Calculator();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();

        assertTrue("Create method private void " + metodi + "() for class " + klassName, klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPrivate());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError().invoke();

    }

    @Points("3.2")
    @Test
    public void hasProduct() throws Throwable {
        new MockInOut("2\n3\n");
        String metodi = "product";
        String vv = "error caused by code Calculator calculator = new Calculator();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();

        assertTrue("Create method private void " + metodi + "() for class " + klassName, klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPrivate());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError().invoke();

    }

    @Points("3.2")
    @Test
    public void hasStatistics() throws Throwable {
        String metodi = "statistics";
        String vv = "error caused by code Calculator calculator = new Calculator();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("Create method private void " + metodi + "() for class " + klassName, klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPrivate());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError().invoke();
    }

    @Points("3.2")
    @Test
    public void hasReaderButNotScanner() {
        Field[] kentat = ReflectionUtils.findClass(luokanNimi).getDeclaredFields();

        int lukija = 0;
        for (Field field : kentat) {
            if (field.toString().contains("Reader")) {
                lukija++;
            }

            assertFalse("In class Calculator, do not use directly Scanner, so remove " + kentta(field.toString()), field.toString().contains("Scanner"));
            assertFalse("You do not need \"static variables\", remove " + kentta(field.toString()), field.toString().contains("static") && !field.toString().contains("final"));

            assertTrue("All instance variables of the class should be private, change " + kentta(field.toString()), field.toString().contains("private"));
        }
        assertTrue("In class Calculator, there should be an instance variable of type Reader", lukija == 1);

        lueKoodi();
    }

    @Points("3.3")
    @Test
    public void sumWorks1() throws Throwable {
        MockInOut mio = new MockInOut("2\n3\n");
        Object laskin = newLaskin();
        String met = "sum";
        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("Calling method " + met + " causes an exception\n"
                    + "the reason might be that instance of class Reader hasn't been created in the constructor.");
        }
        String tulos = mio.getOutput();

        assertTrue("does the method " + met + "work correctly? When calling " + met + " with input 2 and 3, it printed " + tulos, tulos.contains("5"));
    }

    @Points("3.3")
    @Test
    public void sumWorks2() throws Throwable {
        int l1 = 10;
        int l2 = -4;
        int t = 6;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "sum";
        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("Calling method " + met + " causes an exception\n"
                    + "the reason might be that instance of class Reader hasn't been created in the constructor.");
        }
        String tulos = mio.getOutput();

        assertTrue("does the method " + met + "work correctly? When calling " + met + " with input " + l1 + " and " + l2 + ", it printed " + tulos, tulos.contains("" + t));
    }

    @Points("3.3")
    @Test
    public void differenceWorks() throws Throwable {
        int l1 = 10;
        int l2 = 4;
        int t = 6;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "difference";
        try {
            doStuff(met, laskin);
        } catch (NullPointerException e) {
            fail("Calling method " + met + " causes an exception\n"
                    + "the reason might be that instance of class Reader hasn't been created in the constructor.");
        }
        String tulos = mio.getOutput();

        assertTrue("does the method " + met + "work correctly? When calling " + met + " with input " + l1 + " and " + l2 + ", it printed " + tulos, tulos.contains("" + t));
    }

    @Points("3.3")
    @Test
    public void differenceWorks2() throws Throwable {
        int l1 = 11;
        int l2 = 19;
        int t = -8;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "difference";
        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("Method " + met + " causes an exception!");
        }
        String tulos = mio.getOutput();

        assertTrue("does the method " + met + "work correctly? When calling " + met + " with input " + l1 + " and " + l2 + ", it printed " + tulos, tulos.contains("" + t));
    }

    @Points("3.3")
    @Test
    public void productWorks() throws Throwable {
        int l1 = 3;
        int l2 = 7;
        int t = 21;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "product";
        try {
            doStuff(met, laskin);
        } catch (NullPointerException e) {
            fail("Calling method " + met + " causes an exception\n"
                    + "the reason might be that instance of class Reader hasn't been created in the constructor.");
        }
        String tulos = mio.getOutput();

        assertTrue("does the method " + met + "work correctly? When calling " + met + " with input " + l1 + " and " + l2 + ", it printed " + tulos, tulos.contains("" + t));
    }

    @Points("3.3")
    @Test
    public void productWorks2() throws Throwable {
        int l1 = -4;
        int l2 = 3;
        int t = -12;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "product";

        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("Method " + met + " causes an exception!");
        }
        String tulos = mio.getOutput();

        assertTrue("does the method " + met + "work correctly? When calling " + met + " with input " + l1 + " and " + l2 + ", it printed " + tulos, tulos.contains("" + t));
    }

    @Points("3.3")
    @Test
    public void manyCommandsDoNotCauseException() throws Throwable {
        String input = "sum\n1\n2\nproduct\n2\n3\ndifference\n4\n2\nsum\n3\n3\nend\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("start", laskin);
        } catch (Throwable e) {
            fail("new Calculator.start(); causes an exception with user input " + input + ", additional information: " + e);

        }
        String[] tulos = mio.getOutput().split("\n");
    }

    @Points("3.3")
    @Test
    public void wholeApplication1() throws Throwable {
        String input = "sum\n1\n2\nend\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("start", laskin);
        } catch (Throwable e) {
            fail("new Calculator.start(); causes an exception with user input " + input + ", additional information: " + e);
        }
        String[] tulos = mio.getOutput().split("\n");
        String summa = hae(tulos, "sum");
        assertFalse("With input " + input + " new Calculator.start(); should print line  \"sum\"", summa == null);
        assertTrue("With input " + input + " new Calculator.start(); should print line \"sum of the values 3\" " + summa, summa.contains("3"));
    }

    @Points("3.3")
    @Test
    public void wholeApplication2() throws Throwable {
        String input = "sum\n1\n2\nproduct\n2\n3\ndifference\n4\n2\nend\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("start", laskin);
        } catch (Throwable e) {
            fail("new Calculator.start(); causes an exception with user input " + input + ", additional information: " + e);
        }
        String[] tulos = mio.getOutput().split("\n");
        String res = hae(tulos, "sum");
        assertFalse("With input " + input + " new Calculator.start(); should print line  \"sum\"", res == null);
        assertTrue("With input " + input + " new Calculator.start(); should print line \"sum of the values 3\" " + res, res.contains("3"));

        res = hae(tulos, "product");
        assertFalse("With input " + input + " new Calculator.start(); should print line  \"product\"", res == null);
        assertTrue("With input " + input + " new Calculator.start(); should print line \"product of the values 6\" " + res, res.contains("6"));

        res = hae(tulos, "difference");
        assertFalse("With input " + input + " new Calculator.start(); should print line  \"difference\"", res == null);
        assertTrue("With input " + input + " new Calculator.start(); should print line \"difference of the values 2\" " + res, res.contains("2"));
    }

    @Points("3.4")
    @Test
    public void statisticsPrints() throws Throwable {

        MockInOut mio = new MockInOut("");
        Object laskin = newLaskin();
        String met = "statistics";

        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("Method " + met + " causes an exception!");
        }
        String tulos = mio.getOutput();

        assertTrue("Call of the method statistics should print string \"Calculations done n\" where n is an integer", tulos.contains("done"));
    }

    @Points("3.4")
    @Test
    public void statistics1() throws Throwable {
        String input = "end\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("start", laskin);
        } catch (Throwable e) {
            fail("new Calculator.start(); causes an exception with user input " + input + ", additional information: " + e);
        }
        String[] tulos = mio.getOutput().split("\n");
        String res = hae(tulos, "done");
        assertFalse("With input " + input + " new Calculator.start(); should print line  \"Calculations done\"", res == null);
        assertTrue("With input " + input + " new Calculator.start(); should print line \"Calculations done 0\" " + res, res.contains("0"));

        int lask = 0;
        for (String rivi : tulos) {
            if (rivi.contains("done")) {
                lask++;
            }
        }
        assertTrue("With input " + input + " new Calculator.start(); should print line  \"Calculations done\" only once!", lask == 1);
    }

    @Points("3.4")
    @Test
    public void statistics2() throws Throwable {
        String input = "sum\n1\n2\nproduct\n2\n3\ndifference\n4\n2\nend\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("start", laskin);
        } catch (Throwable e) {
            fail("new Calculator.start(); causes an exception with user input " + input + ", additional information: " + e);
        }
        String[] tulos = mio.getOutput().split("\n");
        String res = hae(tulos, "done");
        assertFalse("With input " + input + " new Calculator.start(); should print line  \"Calculations done\"", res == null);
        assertTrue("With input " + input + " new Calculator.start(); should print line \"Calculations done 3\" " + res, res.contains("3"));

    }

    @Points("3.4")
    @Test
    public void statistics3() throws Throwable {
        String input = "sum\n1\n2\nintegral\ndifference\n4\n2\nend\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("start", laskin);
        } catch (Throwable e) {
            fail("new Calculator.start(); causes an exception with user input " + input + "\n"
                    + "did you notice that if program is given an unknown command like now, program must disqualify the command and continue by asking for the next command\n additional information: " + e);

        }
        String[] tulos = mio.getOutput().split("\n");
        String res = hae(tulos, "done");
        assertFalse("With input " + input + " new Calculator.start(); should print line  \"Calculations done\"", res == null);
        assertTrue("With input " + input + " new Calculator.start(); should print line \"Calculations done 2\" " + res, res.contains("2"));
    }

    private Object newLaskin() throws Throwable {
        try {
            c = ReflectionUtils.findClass(luokanNimi);
            return ReflectionUtils.invokeConstructor(c.getConstructor());
        } catch (Throwable t) {
            try {
                Class laskinClass = Class.forName(luokanNimi);
                java.lang.reflect.Constructor[] laskinConstructor = laskinClass.getDeclaredConstructors();
                laskinConstructor[0].setAccessible(true);
                return laskinConstructor[0].newInstance();
            } catch (Throwable t2) {
            }
            throw t;
        }
    }

    private void doStuff(String meto, Object olio) throws Throwable {
        try {
            Method metodi = haeMetodi(c, meto);
            metodi.setAccessible(true);
            metodi.invoke(olio);
            //ReflectionUtils.invokeMethod(void.class, metodi, olio);
        } catch (Throwable t) {
            throw t;
        }
    }

    private void kaynnista(Object olio) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "start");
            ReflectionUtils.invokeMethod(void.class, metodi, olio);
        } catch (Throwable t) {
            throw t;
        }
    }

    private String kentta(String toString) {
        return toString.replace(luokanNimi + ".", "");
    }

    private Method haeMetodi(Class c, String nimi) {
        for (Method m : c.getDeclaredMethods()) {
            if (m.getName().equals(nimi)) {
                String mj = m.toString();
                if (mj.contains("()") && mj.contains("void")) {
                    return m;
                }
            }
        }
        return null;
    }

    private void lueKoodi() {
        int lukijoita = 0;
        try {
            Scanner lukija = new Scanner(new File("src/Calculator.java"));
            StringBuilder sb = new StringBuilder();

            while (lukija.hasNextLine()) {
                sb.append(lukija.nextLine());

            }
            String rivi = sb.toString().replaceAll("\\s+", "");
            if (rivi.contains("Scanner(")) {
                fail("You're not allowed to use Scanner in class Calculator!");
            }
            if (rivi.contains("newReader(")) {
                lukijoita++;
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("Instance of class Reader must be created exactly once in the class Calculator, now there are", 1, lukijoita);
    }

    private String hae(String[] rivit, String sana) {
        for (String rivi : rivit) {
            if (rivi.toLowerCase().contains(sana.toLowerCase())) {
                return rivi;
            }
        }

        return null;
    }

        private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("you don't need \"static variables\", remove from class " + klassName + " the following variable: " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All object variables of the class should be private, but class " + klassName + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }

            assertTrue("The class " + klassName + " should have only " + m + ", remove others", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}
