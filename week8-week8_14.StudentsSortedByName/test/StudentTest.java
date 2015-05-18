
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

@Points("14")
public class StudentTest {

    Class opiskelijaClass;
    String klassName = "Student";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);

        try {
            opiskelijaClass = ReflectionUtils.findClass("Student");
        } catch (Throwable t) {
            fail("Are you sure you have class \"Student\"");
        }
    }

    @Test
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void hasImplementComparableStudent() {
        String nimi = "Student";
        Class kl;
        try {
            kl = opiskelijaClass;
            Class is[] = kl.getInterfaces();
            Class oikein[] = {java.lang.Comparable.class};
            for (int i = 0; i < is.length; i++) {
            }
            assertTrue("Check that " + nimi + " implements interface Comparable",
                    Arrays.equals(is, oikein));

        } catch (Throwable t) {
            fail("Are you sure that class " + nimi + " implements interface \"Comparable<Student>\"");
        }
    }

    @Test
    public void hasCompareToMethod() throws Throwable {
        String metodi = "compareTo";

        Student pekka = new Student("Pekka");
        Student arto = new Student("Arto");

        assertTrue("Create method public int " + metodi + "(Student other) for class " + klassName,
                klass.method(pekka, metodi)
                .returning(int.class).taking(Student.class).isPublic());

        String v = "\nError caused by code\n"
                + "Student pekka = new Student(\"Pekka\");\n"
                + "Student arto = new Student(\"Arto\");\n"
                + "pekka.compareTo(arto);";

        klass.method(pekka, metodi)
                .returning(int.class).taking(Student.class).withNiceError(v).invoke(arto);

        try {
            ReflectionUtils.requireMethod(opiskelijaClass, int.class, "compareTo", Student.class);
        } catch (Throwable t) {
            fail("Have you implemented method \"public int compareTo(Student other)\"");
        }
    }

    private Object teeStudent(String nimi) {
        return new Student(nimi);
    }

    private Method opiskelijaCompareToMethod() {
        Method m = ReflectionUtils.requireMethod(opiskelijaClass, "compareTo", Student.class);
        return m;
    }

    @Test
    public void testCompareTo() {
        try {
            Object h1 = teeStudent("Ville");
            Object h2 = teeStudent("Aapo");
            Method m = opiskelijaCompareToMethod();
            int tulos = ReflectionUtils.invokeMethod(int.class, m, h1, h2);

        } catch (Throwable ex) {

            fail("Are you sure you have implemented method \"public int compareTo(Student other)\" for class \"Student\"?\n"
                    + "Does the Student-class also implement interface Comparable<Student>?");
        }
    }

    @Test
    public void implementsComparable() {
        try {
            assertTrue("Your Student-class is not implementing interface Comparable!", opiskelijaClass.getInterfaces()[0].equals(Comparable.class));
        } catch (Throwable t) {
            fail("Your Student-class is not implementing interface Comparable!");
        }
    }

    public int testaaKahta(String ekaNimi, String tokaNimi) {
        try {
            Object h1 = teeStudent(ekaNimi);
            Object h2 = teeStudent(tokaNimi);
            Method m = opiskelijaCompareToMethod();

            int tulos = ReflectionUtils.invokeMethod(int.class, m, h1, h2);
            return tulos;
        } catch (Throwable ex) {

            fail("Are you sure you have implemented method \"public int compareTo(Student other)\" for class \"Student\"?\n"
                    + "Does the Student-class also implement interface Comparable<Student>?");
            return -111;
        }
    }

    //Tarkistaa listan opiskelijoita kerrallaan
    public void onkoJarjestyksessa(List<String> lista) {
        Collections.sort(lista);
        for (int i = 0; i < lista.size() - 1; i++) {
            if (testaaKahta(lista.get(i), (lista.get(i + 1))) > 0) {
                fail("Problem with code: \n"
                        + "Student eka = new Student(\"" + lista.get(i) + "\");\n"
                        + "Student toka = new Student(\"" + lista.get(i + 1) + "\");\n"
                        + "System.out.println(eka.compareTo(toka));\n"
                        + "printed: " + testaaKahta(lista.get(i), lista.get(i + 1)));
            }
        }
    }

    public void testaa(String eka, String toka) {
        int vastaus = testaaKahta(eka, toka);
        String t = eka.compareTo(toka) > 0 ? "positive" : "negative";
        boolean tulos = eka.compareTo(toka) > 0 ? vastaus > 0 : vastaus < 0;

        assertTrue("print output should have been " + t + "\n"
                + "Student eka = new Student(\"" + eka + "\");\n"
                + "Student toka = new Student(\"" + toka + "\");\n"
                + "eka.compareTo(toka)\n"
                + "result was: " + vastaus, tulos);
    }

    @Test
    public void notInOrder() {
        String[][] sanat = {
            {"Pekka", "Aku"},
            {"Aku", "Aapo"},
            {"Gödel", "Dijkstra"},
            {"Jukka", "Jaana"},
            {"Arto", "Edsger"},
            {"Kalle", "Kaarle"}
        };

        for (String[] strings : sanat) {
            testaa(strings[0], strings[1]);
        }

    }

    @Test
    public void testEqualNames() {
        String eka = "Aku";
        String toka = "Aku";
        testaaSama(eka, toka);

        eka = "Aapo";
        toka = "Aapo";
        testaaSama(eka, toka);

        eka = "Pelle";
        toka = "Pelle";
        testaaSama(eka, toka);
    }


    private void testaaSama(String eka, String toka) {
        int vastaus = testaaKahta(eka, toka);
        assertEquals("Student eka = new Student(\"" + eka + "\");\n"
                + "Student toka = new Student(\"" + toka + "\");\n"
                + "eka.compareTo(toka);", (int) vastaus, 0);
    }
}
