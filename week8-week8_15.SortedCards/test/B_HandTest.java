
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class B_HandTest<_Hand> {

    String klassName = "Hand";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    @Points("15.2")
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    @Points("15.2")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "an instance variable for storing cards");
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    @Test
    @Points("15.2")
    public void methodAdd() throws Throwable {
        String metodi = "add";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(Card k) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(Card.class).isPublic());

        String v = "Hand hand = new Hand();\n"
                + "Card card = new Card(12,Card.HEARTS );  \n"
                + "hand.add(card);";

        klass.method(olio, metodi)
                .returningVoid().taking(Card.class).withNiceError("error caused by code \n" + v).
                invoke(new Card(12, Card.HEARTS));
    }

    private void add(Object olio, Card k) throws Throwable {
        klass.method(olio, "add")
                .returningVoid()
                .taking(Card.class)
                .invoke(k);
    }

    @Test
    @Points("15.2")
    public void methodPrint() throws Throwable {
        String metodi = "print";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "Hand hand = new Hand();\n"
                + "hand.print();";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).
                invoke();
    }

    @Test
    @Points("15.2")
    public void printWorks() throws Throwable {
        MockInOut io = new MockInOut("");

        Object olio = luo();

        Card k = new Card(12, Card.HEARTS);

        add(olio, k);

        String v = "Hand hand = new Hand();\n"
                + "Card card = new Card(12,Card.HEARTS ); \n"
                + "hand.add(card);\n"
                + "hand.print()\n";

        klass.method(olio, "print")
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).
                invoke();

        String out = io.getOutput();

        assertTrue("With code \n" + v + "the print output was\n" + out, out.contains(k.toString()));

        io = new MockInOut("");
        Card k2 = new Card(2, Card.CLUBS);

        add(olio, k2);
        Card k3 = new Card(14, Card.DIAMONDS);

        add(olio, k3);

        v = "Hand hand = new Hand();\n"
                + "hand.add( new Card(12,Card.HEARTS) ); \n"
                + "hand.add( new Card(2,Card.SPADES) ); \n"
                + "hand.add( new Card(14,Card.DIAMONDS) ); \n"
                + "hand.print()\n";

        klass.method(olio, "print")
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).
                invoke();

        out = io.getOutput();

        assertTrue("Code \n" + v + "should print 3 lines, now print output was\n" + out, out.split("\n").length > 2);

        assertTrue("With code \n" + v + "print output was\n" + out, out.contains(k.toString()));
        assertTrue("With code \n" + v + "print output was\n" + out, out.contains(k2.toString()));
        assertTrue("With code \n" + v + "print output was\n" + out, out.contains(k3.toString()));

    }

    /*
     *
     */
    @Test
    @Points("15.3")
    public void methodSort() throws Throwable {
        String metodi = "sort";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "Hand hand = new Hand();\n"
                + "hand.print();";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).
                invoke();
    }

    @Test
    @Points("15.3")
    public void sortedPrintWorks() throws Throwable {
        MockInOut io = new MockInOut("");

        Object olio = luo();

        Card k = new Card(12, Card.HEARTS);
        Card k3 = new Card(14, Card.DIAMONDS);
        Card k2 = new Card(2, Card.CLUBS);
        Card k4 = new Card(2, Card.SPADES);

        add(olio, k);
        add(olio, k3);
        add(olio, k2);
        add(olio, k4);

        String v = "Hand hand = new Hand();\n"
                + "hand.add( new Card(12,Card.HEARTS) ); \n"
                + "hand.add( new Card(14,Card.DIAMONDS) ); \n"
                + "hand.add( new Card(2,Card.SPADES) ); \n"
                + "hand.add( new Card(2,Card.CLUBS) ); \n"
                + "hand.sort();\n"
                + "hand.print()\n";

        klass.method(olio, "sort")
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).
                invoke();

        klass.method(olio, "print")
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).
                invoke();

        String out = io.getOutput();

        assertTrue("Code \n" + v + "should print 4 lines, now print output was\n" + out, out.split("\n").length > 3);

        int j1 = out.indexOf(k4.toString());
        int j2 = out.indexOf(k2.toString());
        int j3 = out.indexOf(k.toString());
        int j4 = out.indexOf(k3.toString());

        assertTrue("Every card wasn't printed with code \n" + v + "print output was\n" + out, j1 >-1 && j2 >-1 && j3 > -1 && j4 > -1);

        assertTrue("With code \n" + v + "first should be printed " + k4 + "\nprint output was\n" + out, j1 < j2 && j1 < j3 && j1 < j4);
        assertTrue("With code \n" + v + "second should be printed " + k2 + "\nprint output was\n" + out, j2 < j3 && j2 < j4);
        assertTrue("With code \n" + v + "third should be printed " + k + "\nprint output was\n" + out, j3 < j4);
    }

    /*
     *  käsi comparable
     */
    @Points("15.4")
    @Test
    public void hasImplementComparableHand() {
        String nimi = "Hand";
        Class kl;
        try {
            kl = ReflectionUtils.findClass("Hand");
            Class is[] = kl.getInterfaces();
            Class oikein[] = {java.lang.Comparable.class};
            for (int i = 0; i < is.length; i++) {
            }
            assertTrue("Check that " + nimi + " implements interface Comparable",
                    Arrays.equals(is, oikein));

        } catch (Throwable t) {
            fail("Are you sure that class " + nimi + " implements interface \"Comparable<Hand>\"?");
        }
    }

    @Points("15.4")
    @Test
    public void hasCompareToMethod() throws Throwable {
        String metodi = "compareTo";

        Reflex.ClassRef<_Hand> _HandRef = Reflex.reflect("Hand");

        _Hand k1 = _HandRef.constructor().takingNoParams().invoke();
        _Hand k2 = _HandRef.constructor().takingNoParams().invoke();

        assertTrue("Create method public int " + metodi + "(Hand comparable) for class " + klassName,
                klass.method(k1, metodi)
                .returning(int.class).taking(_HandRef.cls()).isPublic());

        String v = "\nError caused by code\n"
                + "Hand hand1 = new Hand();\n"
                + "Hand hand2 = new Hand();\n"
                + "hand1.compareTo(hand2);";

        klass.method(k1, metodi)
                .returning(int.class).taking(_HandRef.cls()).withNiceError(v).invoke(k2);

    }

    @Points("15.4")
    @Test
    public void comparingWorks() throws Throwable {
        Reflex.ClassRef<_Hand> _HandRef = Reflex.reflect("Hand");

        _Hand hand1 = _HandRef.constructor().takingNoParams().invoke();
        _Hand hand2 = _HandRef.constructor().takingNoParams().invoke();

        Card k = new Card(12, Card.HEARTS);
        Card k2 = new Card(14, Card.DIAMONDS);

        add(hand1, k);
        add(hand1, k2);

        Card k3 = new Card(3, Card.CLUBS);

        add(hand2, k3);

        String v = "Hand hand1 = new Hand();\n"
                + "hand1.add( new Card(12,Card.HEARTS) ); \n"
                + "hand1.add( new Card(14,Card.DIAMONDS) ); \n"
                + "Hand hand2 = new Hand();\n"
                + "hand2.add( new Card(3,Card.HEARTS) ); \n"
                + "hand1.compareTo(hand2)\n"
                + "result was: ";

        int vast = klass.method(hand1, "compareTo")
                .returning(int.class).taking(_HandRef.cls()).withNiceError(v).invoke(hand2);

        String od = "Result should have been positive with code:\n";

        assertTrue(od + v + vast, vast > 0);
    }

    @Points("15.4")
    @Test
    public void comparingWorks2() throws Throwable {
        Reflex.ClassRef<_Hand> _HandRef = Reflex.reflect("Hand");

        _Hand hand1 = _HandRef.constructor().takingNoParams().invoke();
        _Hand hand2 = _HandRef.constructor().takingNoParams().invoke();

        Card k = new Card(12, Card.HEARTS);
        Card k2 = new Card(14, Card.DIAMONDS);

        add(hand1, k);
        add(hand1, k2);

        Card k3 = new Card(3, Card.CLUBS);

        add(hand2, k3);

        String v = "Hand hand1 = new Hand();\n"
                + "hand1.add( new Card(12,Card.HEARTS) ); \n"
                + "hand1.add( new Card(14,Card.DIAMONDS) ); \n"
                + "Hand hand2 = new Hand();\n"
                + "hand2.add( new Card(3,Card.HEARTS) ); \n"
                + "hand2.compareTo(hand1)\n"
                + "result was: ";

        int vast = klass.method(hand2, "compareTo")
                .returning(int.class).taking(_HandRef.cls()).withNiceError(v).invoke(hand1);

        String od = "Result should have been negative with code:\n";

        assertTrue(od + v + vast, vast < 0);
    }

    @Points("15.4")
    @Test
    public void comparingWorks3() throws Throwable {
        Reflex.ClassRef<_Hand> _HandRef = Reflex.reflect("Hand");

        _Hand hand1 = _HandRef.constructor().takingNoParams().invoke();
        _Hand hand2 = _HandRef.constructor().takingNoParams().invoke();

        Card k = new Card(12, Card.HEARTS);

        add(hand1, k);

        Card k3 = new Card(3, Card.CLUBS);
        Card k2 = new Card(9, Card.CLUBS);

        add(hand2, k3);
        add(hand2, k2);

        String v = "Hand hand1 = new Hand();\n"
                + "hand1.add( new Card(12,Card.HEARTS) ); \n"
                + "Hand hand2 = new Hand();\n"
                + "hand2.add( new Card(3,Card.CLUBS) ); \n"
                + "hand2.add( new Card(9,Card.CLUBS) ); \n"
                + "hand2.compareTo(hand1)\n";

        int vast = klass.method(hand2, "compareTo")
                .returning(int.class).taking(_HandRef.cls()).withNiceError(v).invoke(hand1);

        assertEquals(v, 0, vast);
    }

    @Points("15.4")
    @Test
    public void comparingWorks4() throws Throwable {
        Reflex.ClassRef<_Hand> _HandRef = Reflex.reflect("Hand");

        _Hand hand1 = _HandRef.constructor().takingNoParams().invoke();
        _Hand hand2 = _HandRef.constructor().takingNoParams().invoke();

        Card k = new Card(12, Card.HEARTS);
        Card k2 = new Card(14, Card.DIAMONDS);

        add(hand1, k);
        add(hand1, k2);

        Card k3 = new Card(3, Card.CLUBS);
        Card k4 = new Card(8, Card.DIAMONDS);
        Card k5 = new Card(7, Card.SPADES);
        Card k6 = new Card(9, Card.HEARTS);

        add(hand2, k3);
        add(hand2, k4);
        add(hand2, k5);
        add(hand2, k6);

        String v = "Hand hand1 = new Hand();\n"
                + "hand1.add( new Card(12,Card.HEARTS) ); \n"
                + "hand1.add( new Card(14,Card.DIAMONDS) ); \n"
                + "Hand hand2 = new Hand();\n"
                + "hand2.add( new Card(3,Card.SPADES) ); \n"
                + "hand2.add( new Card(8,Card.DIAMONDS) ); \n"
                + "hand2.add( new Card(7,Card.CLUBS) ); \n"
                + "hand2.add( new Card(9,Card.HEARTS) ); \n"
                + "hand2.compareTo(hand1)\n"
                + "result was: ";

        int vast = klass.method(hand2, "compareTo")
                .returning(int.class).taking(_HandRef.cls()).withNiceError(v).invoke(hand1);

        String od = "Result should have been positive with code:\n";

        assertTrue(od + v + vast, vast > 0);
    }

    @Points("15.4")
    @Test
    public void comparingWorks5() throws Throwable {
        Reflex.ClassRef<_Hand> _HandRef = Reflex.reflect("Hand");

        _Hand hand1 = _HandRef.constructor().takingNoParams().invoke();
        _Hand hand2 = _HandRef.constructor().takingNoParams().invoke();

        Card k = new Card(12, Card.HEARTS);
        Card k2 = new Card(14, Card.DIAMONDS);

        add(hand1, k);
        add(hand1, k2);

        Card k3 = new Card(3, Card.CLUBS);
        Card k4 = new Card(8, Card.DIAMONDS);
        Card k5 = new Card(7, Card.SPADES);
        Card k6 = new Card(9, Card.HEARTS);

        add(hand2, k3);
        add(hand2, k4);
        add(hand2, k5);
        add(hand2, k6);

        String v = "Hand hand1 = new Hand();\n"
                + "hand1.add( new Card(12,Card.HEARTS) ); \n"
                + "hand1.add( new Card(14,Card.DIAMONDS) ); \n"
                + "Hand hand2 = new Hand();\n"
                + "hand2.add( new Card(3,Card.SPADES) ); \n"
                + "hand2.add( new Card(8,Card.DIAMONDS) ); \n"
                + "hand2.add( new Card(7,Card.CLUBS) ); \n"
                + "hand2.add( new Card(9,Card.HEARTS) ); \n"
                + "hand1.compareTo(hand2)\n"
                + "result was: ";

        int vast = klass.method(hand1, "compareTo")
                .returning(int.class).taking(_HandRef.cls()).withNiceError(v).invoke(hand2);

        String od = "Result should have been negative with code:\n";

        assertTrue(od + v + vast, vast < 0);
    }

    /*
     * komparaattorit
     */
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
