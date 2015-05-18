
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("27.1")
public class BirdTest {

    @Test
    public void birdEquals() {
        testEquals("Varis", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952, true);
        testEquals("Nebelkrähe", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952, true);
        testEquals("Lokki", "Larus", 2011, "Lokki", "Larus", 2011, true);
        testEquals("Lokki", "Larus", 2011, "Seagul", "Larus", 2011, true);
        testEquals("Lokki", "Larus", 2012, "Lokki", "Larus", 2011, false);
        testEquals("Lokki", "Larus", 2012, "Lokki", "Laruc", 2012, false);
        testaaEqualsParametrinTyyppi("Varis", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952, true);
        testaaEqualsParametrinTyyppi("Nebelkrähe", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952, true);
    }

    @Test
    public void birdHashCode() {
        testaaHash("Varis", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952);
        testaaHash("Nebelkrähe", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952);
        testaaHash("Lokki", "Larus", 2011, "Lokki", "Larus", 2011);
        testaaHash("Lokki", "Larus", 2011, "Seagul", "Larus", 2011);
        Bird r1 = new Bird("Nebelkrähe", "Corvus corone cornix", 1952);
        Bird r2 = new Bird("Seagul", "Larus", 2011);
        Bird r3 = new Bird("rusokottarainen", "Sturnus roseus", 2012);
        assertFalse("Method hashCode seems to return the same value for every bird: " + r1.hashCode(),
                r1.hashCode() == r2.hashCode() && r2.hashCode() == r3.hashCode());

    }

    private void testEquals(String m1, String b1, int v1, String m2, String b2, int v2, boolean vast) {
        Bird rb1 = new Bird(m1, b1, v1);
        Bird rb2 = new Bird(m2, b2, v2);

        String v = "Bird b1 = new Bird(\"" + m1 + "\", \"" + b1 + "\", " + v1 + ");\n"
                + "Bird b2 = new Bird(\"" + m2 + "\", \"" + b2 + "\", " + v2 + ");\n"
                + "b1.equals(b2)";
        assertEquals(v, vast, rb1.equals(rb2));
    }

    private void testaaEqualsParametrinTyyppi(String m1, String b1, int v1, String m2, String b2, int v2, boolean vast) {
        Object rb1 = new Bird(m1, b1, v1);
        Object rb2 = new Bird(m2, b2, v2);

        String v = "equals-method's parameter should be of type Object!\n\n"
                + "Object b1 = new Bird(\"" + m1 + "\", \"" + b1 + "\", " + v1 + ");\n"
                + "Object b2 = new Bird(\"" + m2 + "\", \"" + b2 + "\", " + v2 + ");\n"
                + "b1.equals(b2)";
        assertEquals(v, vast, rb1.equals(rb2));
    }

    private void testaaHash(String m1, String b1, int v1, String m2, String b2, int v2) {
        Bird rb1 = new Bird(m1, b1, v1);
        Bird rb2 = new Bird(m2, b2, v2);

        String v = "Bird b1 = new Bird(\"" + m1 + "\", \"" + b1 + "\", " + v1 + ");\n"
                + "Bird b2 = new Bird(\"" + m2 + "\", \"" + b2 + "\", " + v2 + ");\n"
                + "b1.hashCode() == b2.HashCode()";
        assertEquals(v, true, rb1.hashCode() == rb2.hashCode());
    }
}
