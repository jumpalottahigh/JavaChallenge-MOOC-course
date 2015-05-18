
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class D_HandTest<_Hand> {

    String klassName = "Hand";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    private void add(Object olio, Card k) throws Throwable {
        klass.method(olio, "add")
                .returningVoid()
                .taking(Card.class)
                .invoke(k);
    }

    @Test
    @Points("15.6")
    public void methodSortAgainstSuit() throws Throwable {
        String metodi = "sortAgainstSuit";

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
    @Points("15.6")
    public void sortAgainstSuitPrintWorks() throws Throwable {
        MockInOut io = new MockInOut("");

        Object olio = luo();

        Card k0 = new Card(14, Card.CLUBS);
        Card k = new Card(12, Card.HEARTS);
        Card k3 = new Card(14, Card.DIAMONDS);
        Card k2 = new Card(2, Card.CLUBS);
        Card k4 = new Card(7, Card.DIAMONDS);

        add(olio, k0);
        add(olio, k);
        add(olio, k3);
        add(olio, k2);
        add(olio, k4);

        String v = "Hand hand = new Hand();\n"
                + "hand.add( new Card(14,Card.SPADES) ); \n"
                + "hand.add( new Card(12,Card.HEARTS) ); \n"
                + "hand.add( new Card(14,Card.DIAMONDS) ); \n"
                + "hand.add( new Card(2,Card.SPADES) ); \n"
                + "hand.add( new Card(7,Card.DIAMONDS) ); \n"
                + "hand.sortAgainstSuit();\n"
                + "hand.print()\n";

        klass.method(olio, "sortAgainstSuit")
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).
                invoke();

        klass.method(olio, "print")
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).
                invoke();

        String out = io.getOutput();

        assertTrue("Code \n" + v + "should print 5 lines, now print output was\n" + out, out.split("\n").length > 4);

        int j1 = out.indexOf(k4.toString());
        int j2 = out.indexOf(k3.toString());
        int j3 = out.indexOf(k.toString());
        int j4 = out.indexOf(k2.toString());
        int j5 = out.indexOf(k0.toString());

        assertTrue("Every card wasn't printed with code \n" + v + "print output was\n" + out, j1 > -1 && j2 > -1 && j3 > -1 && j4 > -1 && j5 > -1);

        assertTrue("With code \n" + v + "first should be printed " + k4 + "\nprint output was\n" + out, j1 < j2 && j1 < j3 && j1 < j4 && j1 < j5);
        assertTrue("With code \n" + v + "second should be printed " + k3 + "\nprint output was\n" + out, j2 < j3 && j2 < j4 && j2 < j4);
        assertTrue("With code \n" + v + "third should be printed " + k + "\nprint output was\n" + out, j3 < j4 && j3 < j5);
        assertTrue("With code \n" + v + "fourth should be printed " + k2 + "\nprint output was\n" + out, j4 < j5);

//        String left = out;
//        assertTrue("With code \n" + v + "ensin should be printed " + k4 + "\nprint output was\n" + out, left.contains(k4.toString()));
//        left = left.substring(left.indexOf(k4.toString()));
//        assertTrue("With code \n" + v + "toisena should be printed " + k3 + "\nprint output was\n" + out, left.contains(k3.toString()));
//        left = left.substring(left.indexOf(k3.toString()));
//        assertTrue("With code \n" + v + "kolmantena should be printed " + k + "\nprint output was\n" + out, left.contains(k.toString()));
//        left = left.substring(left.indexOf(k.toString()));
//        assertTrue("With code \n" + v + "neljäntenä should be printed " + k2 + "\nprint output was\n" + out, left.contains(k2.toString()));
//        left = left.substring(left.indexOf(k2.toString()));
//        assertTrue("With code \n" + v + "viimeisenä should be printed " + k0 + "\nprint output was\n" + out, left.contains(k0.toString()));
    }
}
