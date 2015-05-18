
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class C_ComparatorTest {

    String klassName = "SortAgainstSuitAndValue";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    @Points("15.5")
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Points("15.5")
    @Test
    public void hasImplementComparableHand() {
        String nimi = klassName;
        Class kl;
        try {
            kl = ReflectionUtils.findClass(klassName);
            Class is[] = kl.getInterfaces();
            Class oikein[] = {java.util.Comparator.class};

            assertTrue("Check that " + nimi + " implements interface Comparator<Card>",
                    Arrays.equals(is, oikein));

        } catch (Throwable t) {
            fail("Are you sure that class " + nimi + " implements interface \"Comparator<Card>\"?");
        }
    }

    @Test
    @Points("15.5")
    public void hasCompareMethod() throws Throwable {
        String metodi = "compare";

        Object k1 = klass.constructor().takingNoParams().invoke();

        assertTrue("Create method public int " + metodi + "(Card k1, Card k2) for class " + klassName,
                klass.method(k1, metodi)
                .returning(int.class).taking(Card.class, Card.class).isPublic());

        String v = "\nError caused by code\n"
                + "SortAgainstSuitAndValue comparator = new SortAgainstSuitAndValue();\n"
                + "Card k1 = new Card(3, Card.HEARTS);\n"
                + "Card k2 = new Card(4, Card.SPADES));\n"
                + "comparator.compareTo(k1, k2);";

        klass.method(k1, metodi)
                .returning(int.class).taking(Card.class, Card.class).withNiceError(v).
                invoke(new Card(3, Card.HEARTS), new Card(4, Card.CLUBS));
    }

    @Test
    @Points("15.5")
    public void test() throws Throwable {
        testaa(1, 1);
        testaa(5, 8);
        testaa(14, 3);

        int[][] luvut = {
            {2, 2, 4, 3},
            {3, 0, 2, 3},
            {4, 2, 5, 2},
            {7, 4, 12, 4},
        };

        for (int[] is : luvut) {
            testaa(is[0], is[1], is[2], is[3], false);
            testaa(is[2], is[3], is[0], is[1], true);
        }
    }

    public int testaaKahta(Card h1, Card h2) throws Throwable {
        String metodi = "compare";

        Object komparaattori = klass.constructor().takingNoParams().invoke();

        return klass.method(komparaattori, metodi)
                .returning(int.class).taking(Card.class, Card.class).
                invoke(h1, h2);
    }

    public void testaa(int a1, int m1, int a2, int m2, boolean pos) throws Throwable {
        int vastaus = testaaKahta(new Card(a1, m1), new Card(a2, m2));
        String t = pos ? "positive" : "negative";
        boolean tulos = pos ? vastaus > 0 : vastaus < 0;

        assertTrue("result should have been a " + t + " integer\n"
                + "SortAgainstSuitAndValue comparator = new SortAgainstSuitAndValue();\n"
                + "Card first = new Card(" + a1 + "," + m(m1) + ");\n"
                + "Card second = new Card(" + a2 + "," + m(m2) + ");\n"
                + "comparator.compare(first,second)\n"
                + "result was: " + vastaus, tulos);
    }

    public void testaa(int a1, int m1) throws Throwable {
        int vastaus = testaaKahta(new Card(a1, m1), new Card(a1, m1));

        assertEquals(
                "SortAgainstSuitAndValue comparator = new SortAgainstSuitAndValue();\n"
                + "Card first = new Card(" + a1 + "," + m(m1) + ");\n"
                + "Card second = new Card(" + a1 + "," + m(m1) + ");\n"
                + "comparator.compare(first,second)", 0, vastaus);
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
