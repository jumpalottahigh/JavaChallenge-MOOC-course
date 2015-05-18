
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("27.2")
public class RingingCentreTest {

    String klassName = "RingingCentre";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void classPublic() {
        klass = Reflex.reflect(klassName);
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 2, "instance variable of type Map<Bird, List<String>> for storing observations");
    }

    @Test
    public void hasHashMap() {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();
        assertFalse("Create for class " + klassName + " an instance variable of type Map<Bird, List<String>>", kentat.length < 1);
        assertTrue("Class " + klassName + " should only have an instance variable of type Map<Bird, List<String>>"
                + "remove others", kentat.length == 1);

        assertTrue("Class " + klassName + " should have an instance variable of type Map<Bird, List<String>> " + kentat[0].toString(), kentat[0].toString().contains("Map"));

    }

    @Test
    public void emptyConstructor() throws Throwable {
        klass = Reflex.reflect(klassName);
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "()", ctor.isPublic());
        String v = "error caused by code new RingingCentre();";
        ctor.withNiceError(v).invoke();
    }

    @Test
    public void observeMethod() throws Throwable {
        String metodi = "observe";

        Object olio = luo();

        assertTrue("Create method " + metodi + " public void(Bird bird, String location) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(Bird.class, String.class).isPublic());

        String v = "\nError caused by code\n "
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n";

        klass.method(olio, metodi)
                .returningVoid().taking(Bird.class, String.class).
                withNiceError(v).invoke(new Bird("Nebelkrähe", "Corvus corone cornix", 2000), "Berlin");

        v = "\nError caused by code\n "
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.observe( new Bird(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n";

        klass.method(olio, metodi)
                .returningVoid().taking(Bird.class, String.class).
                withNiceError(v).invoke(new Bird("Harmaalokki", "Larus argentatus", 2012), "Kumpula");

        v = "\nError caused by code\n "
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.observe( new Bird(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.observe( new Bird(\"Varis\", \"Corvus corone cornix\", 2000), \"Arabia\");\n";

        klass.method(olio, metodi)
                .returningVoid().taking(Bird.class, String.class).
                withNiceError(v).invoke(new Bird("Varis", "Corvus corone cornix", 2000), "Arabia");
    }

    @Test
    public void observationsMethod() throws Throwable {
        String metodi = "observations";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(Bird bird) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(Bird.class).isPublic());

        String v = "\nError caused by code\n "
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observations(new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000));\n";

        klass.method(olio, metodi)
                .returningVoid().
                taking(Bird.class).
                withNiceError(v).invoke(new Bird("Nebelkrähe", "Corvus corone cornix", 2000));
    }

    @Test
    public void observationsWorks() throws Throwable {
        MockInOut mio = new MockInOut("");

        Object olio = luo();

        String v = "\n"
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.observations( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000) )";

        observe(olio, "Nebelkrähe", "Corvus corone cornix", 2000, "Berlin", v);
        observations(olio, v, new Bird("Nebelkrähe", "Corvus corone cornix", 2000));
        String out = mio.getOutput();

        assertFalse("Should have printed 2 lines with code\n" + v
                + "\nNow program printed nothing\n", out.isEmpty());

        assertTrue("Should have printed 2 lines with code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n").length > 1);

        assertTrue("First line should have been \"Corvus corone cornix (2000) observations: 1"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("Corvus corone cornix (2000)"));
        assertTrue("First line should have been \"Corvus corone cornix (2000) observations: 1"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("observations: 1"));

        assertTrue("Second line should have been \"Berlin\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[1].contains("Berlin"));
    }

    @Test
    public void observationsWorks2() throws Throwable {
        MockInOut mio = new MockInOut("");

        Object olio = luo();

        String v = "\n"
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.observe( new Bird(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.observations( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000) )";

        observe(olio, "Nebelkrähe", "Corvus corone cornix", 2000, "Berlin", v);
        observe(olio, "Harmaalokki", "Larus argentatus", 2012, "Kumpula", v);

        observations(olio, v, new Bird("Nebelkrähe", "Corvus corone cornix", 2000));

        String out = mio.getOutput();

        assertTrue("Should have printed 2 lines with code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n").length > 1);

        assertTrue("First line should have been \"Corvus corone cornix (2000) observations: 1"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("Corvus corone cornix (2000)"));
        assertTrue("First line should have been \"Corvus corone cornix (2000) observations: 1"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("observations: 1"));
        assertTrue("Second line should have been \"Berlin\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[1].contains("Berlin"));

        mio = new MockInOut("");
        observations(olio, v, new Bird("Harmaalokki", "Larus argentatus", 2012));

        v = "\nError caused by code\n "
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.observe( new Bird(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.observations( new Bird(\"Harmaalokki\", \"Larus argentatus\", 2012) )";

        observations(olio, v, new Bird("Harmaalokki", "Larus argentatus", 2012));

        out = mio.getOutput();

        assertTrue("Should have printed 2 lines with code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n").length > 1);

        assertTrue("First line should have been \"Larus argentatus (2012) observations: 1"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("Larus argentatus (2012) observations: 1"));
        assertTrue("First line should have been \"Larus argentatus (2012)"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("observations: 1"));

        assertTrue("Second line should have been \"Kumpula\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[1].contains("Kumpula"));
    }

    @Test
    public void observationsWorks3() throws Throwable {
        MockInOut mio = new MockInOut("");

        Object olio = luo();

        String v = "\n"
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.observe( new Bird(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.observe( new Bird(\"Varis\", \"Corvus corone cornix\", 2000), \"Arabia\");\n"
                + "rt.observe( new Bird(\"lokki\", \"Larus argentatus\", 2012), \"Korso\");\n"
                + "rt.observe( new Bird(\"Varis\", \"Corvus corone cornix\", 2000), \"Kamppi\");\n"
                + "rt.observations( new Bird(\"Varis\", \"Corvus corone cornix\", 2000) )\n";

        observe(olio, "Nebelkrähe", "Corvus corone cornix", 2000, "Berlin", v);
        observe(olio, "Harmaalokki", "Larus argentatus", 2012, "Kumpula", v);
        observe(olio, "Varis", "Corvus corone cornix", 2000, "Arabia", v);
        observe(olio, "lokki", "Larus argentatus", 2012, "Korso", v);
        observe(olio, "Varis", "Corvus corone cornix", 2000, "Kamppi", v);

        observations(olio, v, new Bird("Nebelkrähe", "Corvus corone cornix", 2000));

        String out = mio.getOutput();

        assertTrue("Should have printed 4 lines with code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n").length > 1);

        assertTrue("First line should have been \"Corvus corone cornix (2000) observations: 3"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("observations: 3"));
        assertTrue("First line should have been \"Corvus corone cornix (2000) observations: 3"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("Corvus corone cornix (2000)"));

        assertTrue("Print output should have contained \"Berlin\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.contains("Berlin"));
        assertTrue("Print output should have contained \"Arabia\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.contains("Arabia"));
        assertTrue("Print output should have contained \"Kumpula\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.contains("Kamppi"));


        mio = new MockInOut("");

        v = "\nError caused by code\n "
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.observe( new Bird(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.observe( new Bird(\"Varis\", \"Corvus corone cornix\", 2000), \"Arabia\");\n"
                + "rt.observe( new Bird(\"lokki\", \"Larus argentatus\", 2012), \"Korso\");\n"
                + "rt.observe( new Bird(\"Varis\", \"Corvus corone cornix\", 2000), \"Kamppi\");\n"
                + "rt.observations( new Bird(\"Harmaalokki\", \"Larus argentatus\", 2012) );";

        observations(olio, v, new Bird("Harmaalokki", "Larus argentatus", 2012));

        out = mio.getOutput();

        assertTrue("Should have printed 2 lines with code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n").length > 1);

        assertTrue("First line should have been \"Larus argentatus (2012) observations: 2"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("Larus argentatus (2012)") && out.split("\n")[0].contains("observations: 2"));
        assertTrue("Print output should have contained \"Korso\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.contains("Korso"));
        assertTrue("Print output should have contained \"Kumpula\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.contains("Kumpula"));
    }

    @Test
    public void observationsWorks4() throws Throwable {
        MockInOut mio = new MockInOut("");

        Object olio = luo();

        String v = "\n"
                + "RingingCentre rt = new RingingCentre();\n"
                + "rt.observe( new Bird(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.observations( new Bird(\"Varsi\", \"Corvus corone cornix\", 2012) )";

        observe(olio, "Nebelkrähe", "Corvus corone cornix", 2000, "Berlin", v);
        observations(olio, v, new Bird("Varis", "Corvus corone cornix", 2012));
        String out = mio.getOutput();

        assertFalse("Should have printed 1 line with code\n" + v
                + "\nNow program printed nothing\n", out.isEmpty());

        assertTrue("Should have printed 1 line with code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n").length == 1);

        assertTrue("Printed line should have been \"Corvus corone cornix (2012) observations: 0"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("Corvus corone cornix (2012)"));
        assertTrue("Printed line should have been \"Corvus corone cornix (2012) observations: 0"
                + "\"\nwith code\n" + v
                + "\nprogram printed:\n" + out, out.split("\n")[0].contains("observations: 0"));
    }

    /*
     *
     */
    public void observations(Object o, String v, Bird l) throws Throwable {
        klass.method(o, "observations")
                .returningVoid().
                taking(Bird.class).
                withNiceError(v).invoke(l);
    }

    public void observe(Object olio, String n, String l, int v, String h, String vi) throws Throwable {
        klass.method(olio, "observe")
                .returningVoid().taking(Bird.class, String.class).
                withNiceError(vi).invoke(new Bird(n, l, v), h);
    }

    public Object luo() throws Throwable {
        klass = Reflex.reflect(klassName);
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
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
