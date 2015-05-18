
import java.util.Arrays;
import java.lang.reflect.Constructor;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import java.lang.reflect.Method;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

@Points("11.1")
public class Part1ToBeStoredTest {

    static final String kirjaNimi = "Book";
    static final String levyNimi = "CD";
    static final double eps = 0.00000001;
    Class tallc;
    Reflex.ClassRef<Object> klass;

    @Before
    public void getToBeStored() {
        try {
            tallc = ReflectionUtils.findClass("ToBeStored");
        } catch (AssertionError e) {
            fail("Add interface ToBeStored!");
        }

        String klassName = "ToBeStored";
        klass = Reflex.reflect(klassName);

        assertTrue("Define interface " + klassName + " as follows: \npublic interface " + klassName + " {...\n}", klass.isPublic());

    }

    @Test
    public void toBeStoredCorrect() {

        assertTrue("ToBeStored has to be an interface class!", tallc.isInterface());

        Method ms[] = tallc.getDeclaredMethods();

        assertEquals("In interface ToBeStored, there should be one method!", 1, ms.length);

        assertEquals("Interface ToBeStored should have method double weight()",
                "public abstract double ToBeStored.weight()",
                ms[0].toString());
    }

    public void implementsToBeStored(String name) {
        String klassName = name;
        klass = Reflex.reflect(klassName);
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());

        Class kl = null;
        try {
            kl = ReflectionUtils.findClass(name);
        } catch (Throwable t) {
            fail("Check that you have created class " + name);
        }

        Class is[] = kl.getInterfaces();
        Class oikein[] = {tallc};

        assertTrue("Check that " + name + " implements interface ToBeStored",
                Arrays.equals(is, oikein));

    }

    @Test
    public void bookImplementsToBeStored() {
        implementsToBeStored(kirjaNimi);
    }

    @Test
    public void bookWorks() throws Throwable {
        String klassName = "Book";
        klass = Reflex.reflect(klassName);

        Reflex.MethodRef3<Object, Object, String, String, Double> ctor = klass.constructor().
                taking(String.class, String.class, double.class).withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public "
                + klassName + "(String writer, String name, double weight)", ctor.isPublic());
        String v = "error caused by code new Book(\"Big Bad Wolf\", \"Tasty pork chop\", 9000.0);";
        Object olio = ctor.withNiceError(v).invoke("Big Bad Wolf", "Tasty pork chop", 9000.0);

        String metodi = "weight";

        assertTrue("Create method public double " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returning(double.class).takingNoParams().isPublic());

        v = "\nError caused by code new Book k = Book(\"Big Bad Wolf\", \"Tasty pork chop\", 9000.0); "
                + "k.weight();";

        double p = klass.method(olio, metodi)
                .returning(double.class).takingNoParams().withNiceError(v).invoke();

        assertEquals(" new Book k = Book(\"Big Bad Wolf\", \"Tasty pork chop\", 9000.0); "
                + "k.weight();", 9000., p, 0.01);

        assertFalse("Create for class Book method toString as defined in the assignment", olio.toString().contains("@"));

        assertEquals("toString doesn't return what was expected", "Big Bad Wolf: Tasty pork chop", olio.toString());

    }

    @Test
    public void cdImplementsToBeStored() {
        implementsToBeStored(levyNimi);
    }

    @Test
    public void cdWorks() throws Throwable {
        String klassName = "CD";
        klass = Reflex.reflect(klassName);

        Reflex.MethodRef3<Object, Object, String, String, Integer> ctor = klass.constructor().
                taking(String.class, String.class, int.class).withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public "
                + klassName + "(String artist, String name, int year)", ctor.isPublic());
        String v = "error caused by code new CD(\"Big Bad Wolf\", \"Pork-rock\", 1830);";

        Object olio = ctor.withNiceError(v).invoke("Big Bad Wolf", "Pork-rock", 1830);

        String metodi = "weight";

        assertTrue("Create method public double " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returning(double.class).takingNoParams().isPublic());

        v = "\nError caused by code CD cd = new CD(\"Big Bad Wolf\", \"Pork-rock\", 1830); "
                + "cd.weight();";

        double p = klass.method(olio, metodi)
                .returning(double.class).takingNoParams().withNiceError(v).invoke();

        assertEquals("CD cd = new CD(\"Big Bad Wolf\", \"Pork-rock\", 1830); "
                + "cd.weight();", .1, p,  0.001);

        assertFalse("Create for class CD method toString as defined in the assignment", olio.toString().contains("@"));

        assertEquals("toString doesn't return what was expected", "Big Bad Wolf: Pork-rock (1830)", olio.toString());


    }
}
