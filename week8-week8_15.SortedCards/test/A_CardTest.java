
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("15.1")
public class A_CardTest {

    Class cardClass;
    String klassName = "Card";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);

        try {
            cardClass = ReflectionUtils.findClass("Card");
        } catch (Throwable t) {
            fail("Are you sure you have class \"Card\"?");
        }
    }

    @Test
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void hasImplementComparableCard() {
        String nimi = "Card";
        Class kl;
        try {
            kl = cardClass;
            Class is[] = kl.getInterfaces();
            Class oikein[] = {java.lang.Comparable.class};
            for (int i = 0; i < is.length; i++) {
            }
            assertTrue("Check that " + nimi + " implements interface Comparable",
                    Arrays.equals(is, oikein));

        } catch (Throwable t) {
            fail("Are you sure that class " + nimi + " implements interface \"Comparable<Card>\"?");
        }
    }

    @Test
    public void hasCompareToMethod() throws Throwable {
        String metodi = "compareTo";

        Card first = new Card(1, Card.HEARTS);
        Card second = new Card(10, Card.SPADES);

        assertTrue("Create method public int " + metodi + "(Card comparable) for class " + klassName,
                klass.method(first, metodi)
                .returning(int.class).taking(Card.class).isPublic());

        String v = "\nError caused by code\n"
                + "Card first = new Card(1,Card.HEARTS);\n"
                + "Card second = new Card(10,Card.CLUBS);\n"
                + "first.compareTo(second);";

        klass.method(first, metodi)
                .returning(int.class).taking(Card.class).withNiceError(v).invoke(second);

        try {
            ReflectionUtils.requireMethod(cardClass, int.class, "compareTo", Card.class);
        } catch (Throwable t) {
            fail("Have you created method \"public int compareTo(Card comparable)\"?");
        }
    }

    private Method compareToMethod() {
        Method m = ReflectionUtils.requireMethod(cardClass, "compareTo", Card.class);
        return m;
    }

    @Test
    public void testCompareTo() {
        try {
            Object h1 = new Card(1, Card.HEARTS);
            Object h2 = new Card(2, Card.HEARTS);
            Method m = compareToMethod();
            int tulos = ReflectionUtils.invokeMethod(int.class, m, h1, h2);

        } catch (Throwable ex) {

            fail("Have you created method \"public int compareTo(Card comparable)\" for class \"Card\"?.\n"
                    + "Does the class Card also implement interface Comparable<Card>?");
        }
    }

    @Test
    public void implementsComparable() {
        try {
            assertTrue("Class Card doesn't implement interface Comparable!", cardClass.getInterfaces()[0].equals(Comparable.class));
        } catch (Throwable t) {
            fail("Class Card doesn't implement interface Comparable!");
        }
    }

    @Test
    public void test() {
        testaa(1, 1);
        testaa(5, 2);
        testaa(14, 3);

        int[][] luvut = {
            {4, 2, 5, 2},
            {3, 2, 4, 3},
            {6, 2, 8, 3},
            {10, 2, 10, 3},
            {11, 1, 11, 2}
        };

        for (int[] is : luvut) {
            testaa(is[0], is[1], is[2], is[3], false);
            testaa(is[2], is[3], is[0], is[1], true);
        }
    }

    public int testaaKahta(Card h1, Card h2) {
        try {
            Method m = compareToMethod();

            int tulos = ReflectionUtils.invokeMethod(int.class, m, h1, h2);
            return tulos;
        } catch (Throwable ex) {

            fail("Have you created method \"public int compareTo(Card comparable)\" for class \"Card\"?.\n"
                    + "Does the class Card also implement interface Comparable<Card>?");
            return -111;
        }
    }

    public void testaa(int a1, int m1, int a2, int m2, boolean pos) {
        int vastaus = testaaKahta(new Card(a1, m1), new Card(a2, m2));
        String t = pos ? "positive" : "negative";
        boolean tulos = pos ? vastaus > 0 : vastaus < 0;

        assertTrue("result should have been a " + t + " integer\n"
                + "Card first = new Card(" + a1 + "," + m(m1) + ");\n"
                + "Card second = new Card(" + a2 + "," + m(m2) + ");\n"
                + "first.compareTo(second)\n"
                + "result was: " + vastaus, tulos);
    }

    public void testaa(int a1, int m1) {
        int vastaus = testaaKahta(new Card(a1, m1), new Card(a1, m1));

        assertEquals(
                "Card first = new Card(" + a1 + "," + m(m1) + ");\n"
                + "Card second = new Card(" + a1 + "," + m(m1) + ");\n"
                + "first.compareTo(second)", 0, vastaus);
    }

    private String m(int m) {
        if (m == 0) {
            return "Card.CLUBS";
        }

        if (m == 1) {
            return "Card.DIAMONDS";
        }

        if (m == 2) {
            return "Card.HEARTS";
        }

        if (m == 3) {
            return "Card.SPADES";
        }
        return "XX";
    }
}
