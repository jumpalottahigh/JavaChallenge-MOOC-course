
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@Points("4.1")
public class A_ThingTest {

    String klassName = "Thing";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void classIsPublic() {
        assertTrue("Class " + klassName + " has to be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 2, "instance variables for name and weight only");
    }

    @Test
    public void testConstructor() throws Throwable {
        Reflex.MethodRef2<Object, Object, String, Integer> ctor = klass.constructor().taking(String.class, int.class).withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "(String name, int weight)", ctor.isPublic());
        String v = "Error caused by code new Thing(\"Phone\", 1);";
        ctor.withNiceError(v).invoke("Phone", 1);
    }

    public Object luo(String nimi, int paino) throws Throwable {
        Reflex.MethodRef2<Object, Object, String, Integer> ctor = klass.constructor().taking(String.class, int.class).withNiceError();
        return ctor.invoke(nimi, paino);
    }

    @Test
    @Points("4.1")
    public void thingGetName() throws Throwable {
        String metodi = "getName";
        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassName);

        Object olio = luo("Book", 2);

        assertTrue("Create method public String " + metodi + "() for class " + klassName, tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());


        String v = "\nError cause by code Thing t = new Thing(\"Book\", 2); "
                + "t.getName();";

        assertEquals("Verify code Thing t = new Thing(\"Book\", 2); "
                + "t.getName();", "Book", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());

        olio = luo("Car", 800);


        v = "\nError cause by code Thing t = new Thing(\"Car\", 800); "
                + "t.getName();";

        assertEquals("Verify code Thing t = new Thing(\"Car\", 800);  "
                + "t.getName();", "Car", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());
    }

    @Test
    @Points("4.1")
    public void thingGetWeight() throws Throwable {
        String metodi = "getWeight";
        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassName);

        Object olio = luo("Book", 2);

        assertTrue("Create method public int " + metodi + "() for class " + klassName, tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nError cause by code Thing t = new Thing(\"Book\", 2); "
                + "t.getWeight();";

        assertEquals("Verify code Thing t = new Thing(\"Book\", 2); "
                + "t.getWeight();", 2, (int)tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke());

        olio = luo("Car", 800);

        v = "\nError cause by code Thing t = new Thing(\"Car\", 800); "
                + "t.getWeight();";

        assertEquals("Verify code Thing t = new Thing(\"Car\", 800);  "
                + "t.getWeight();", 800, (int)tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke());
    }

    @Test
    @Points("4.1")
    public void thingToString() throws Throwable {
        Object olio = luo("Book", 2);

        assertFalse("Implement method toString for class Thing as instructed in the exercise", olio.toString().contains("@"));

        assertTrue("Check that method toString works as specified. \n"
                + "Thing t = new Thing(\"Book\", 2); t.toString();  \n"+olio.toString(),sisaltaa(olio.toString(), "Book", "2", "kg"));
    }

    private boolean sisaltaa(String palautus, String... oletetutArvot) {
        for (String arvo : oletetutArvot) {
            if (!palautus.contains(arvo)) {
                return false;
            }
        }

        return true;
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("You do not need \"static variables\", remove from class " + klassName + " the following variable: " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All instance variables of the class should be private, but class " + klassName + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("The class " + klassName + " should have " + m + ", remove others", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}
