
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@Points("13")
public class PersonTest {

    String klassName = "Person";
    Class c;
    Reflex.ClassRef<Object> klass;
    private Class ihminenClass;
    private final String vastaus = "compareTo-method should return a positive number if this.salary is smaller compared to the other Person-object's salary.\n"
            + "When salaries are equal, method should return 0.\n"
            + "When the other Person-object has a smaller salary, compareTo-method should return a negative number.\n";

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);

        try {
            ihminenClass = ReflectionUtils.findClass("Person");
        } catch (Throwable t) {
            fail("Are you sure that you have a class Person?");
        }
    }

    @Test
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void hasImplementComparablePerson() {
        String nimi = "Person";
        Class kohdeLuokka;
        try {
            kohdeLuokka = ihminenClass;
            Class kohdeTaulukko[] = kohdeLuokka.getInterfaces();
            Class oikein[] = {java.lang.Comparable.class};
            for (int i = 0; i < kohdeTaulukko.length; i++) {
            }
            assertTrue("Check that " + nimi + " implements interface Comparable",
                    Arrays.equals(kohdeTaulukko, oikein));

        } catch (Throwable t) {
            fail("Are you sure that class " + nimi + " implements interface \"Comparable<Person>\"");
        }
    }

    @Test
    public void hasCompareToMethod() throws Throwable {
        String metodi = "compareTo";

        Person pekka = new Person("Pekka", 1600);
        Person arto = new Person("Arto", 3500);

        assertTrue("Create method public int " + metodi + "(Person other) for class " + klassName,
                klass.method(pekka, metodi)
                .returning(int.class).taking(Person.class).isPublic());

        String v = "\nError caused by code\n"
                + "Person pekka = new Person(\"Pekka\",1600);\n"
                + "Person arto = new Person(\"Arto\",3500);\n"
                + "pekka.compareTo(arto);";

        klass.method(pekka, metodi)
                .returning(int.class).taking(Person.class).withNiceError(v).invoke(arto);
    }

    private Object teePerson(String nimi, int palkka) {
        try {
            Constructor ihminenConst = ReflectionUtils.requireConstructor(ihminenClass, String.class, int.class);
            Object ihminenObject = ReflectionUtils.invokeConstructor(ihminenConst, nimi, palkka);
            return ihminenObject;
        } catch (Throwable t) {
            fail("Are you sure that you have a class \"Person\"");
            return null;
        }
    }

    private Method ihmisCompareToMethod() {
        Method m = ReflectionUtils.requireMethod(ihminenClass, "compareTo", Person.class);
        return m;
    }

    public int testaaKahta(String ekaNimi, int ekaPalkka, String tokaNimi, int tokaPalkka) {
        try {
            Object henkilo1 = teePerson(ekaNimi, ekaPalkka);
            Object henkilo2 = teePerson(tokaNimi, tokaPalkka);
            Method m = ihmisCompareToMethod();
//            int tulos = ReflectionUtils.invokeMethod(int.class, m, henkilo1, henkilo2);
            return ReflectionUtils.invokeMethod(int.class, m, henkilo1, henkilo2);
        } catch (Throwable ex) {

            fail("Are you sure that you have implemented method \"public int compareTo(Person other)\" to class \"Person\"?\n"
                    + "Check also that Person-class implements interface Comparable.");
            return -111;
        }
    }

    @Test
    public void othersSalaryIsBigger() {
        String ekaNimi = "Aku";
        String tokaNimi = "Roope";
        int ekaPalkka = 0;
        int tokaPalkka = Integer.MAX_VALUE;
        int tulos = testaaKahta(ekaNimi, ekaPalkka, tokaNimi, tokaPalkka);

        String lisaVihje = ""
                + "Person eka = new Person(" + ekaNimi + ", " + ekaPalkka + ");\n"
                + "Person eka = new Person(" + tokaNimi + ", " + tokaPalkka + ");\n"
                + "System.out.println(eka.compareTo(toka));\n"
                + "printed: "+tulos;

        if (tulos == -111) {
            fail("Are you sure that you have implemented method \"public int compareTo(Person other)\" to class \"Person\"?\n"
                    + "Check also that Person-class implements interface Comparable.");
        } else {
            assertTrue(vastaus + "\n" + lisaVihje, tulos > 0);
        }
    }

    @Test
    public void othersSalaryIsSmaller() {
        String ekaNimi = "Roope";
        String tokaNimi = "Aku";
        int ekaPalkka = Integer.MAX_VALUE;
        int tokaPalkka = 0;

        int tulos = testaaKahta(ekaNimi, ekaPalkka, tokaNimi, tokaPalkka);

        String lisaVihje = ""
                + "Person eka = new Person(" + ekaNimi + ", " + ekaPalkka + ");\n"
                + "Person eka = new Person(" + tokaNimi + ", " + tokaPalkka + ");\n"
                + "System.out.println(eka.compareTo(toka));\n"
                + "printed: "+tulos;

        assertTrue(vastaus + "\n" + lisaVihje, tulos < 0);
    }

    @Test
    public void equalSalaries() {
        String ekaNimi = "Hessu";
        String tokaNimi = "Taavi";
        int ekaPalkka = 3000;
        int tokaPalkka = 3000;

        int tulos = testaaKahta(ekaNimi, ekaPalkka, tokaNimi, tokaPalkka);

        String lisaVihje = ""
                + "Person eka = new Person(" + ekaNimi + ", " + ekaPalkka + ");\n"
                + "Person eka = new Person(" + tokaNimi + ", " + tokaPalkka + ");\n"
                + "System.out.println(eka.compareTo(toka));\n"
                + "printed: "+tulos;

        assertTrue(vastaus + "\n" + lisaVihje, tulos == 0);
    }

    @Test
    public void additionalTests() {
        String ekaNimi = "Hessu";
        String tokaNimi = "Taavi";
        int ekaPalkka = 3000;
        int tokaPalkka = 30000;
        int vastausInt = testaaKahta(ekaNimi, ekaPalkka, tokaNimi, tokaPalkka);

        String lisaVihje = ""
                + "Person eka = new Person(" + ekaNimi + ", " + ekaPalkka + ");\n"
                + "Person eka = new Person(" + tokaNimi + ", " + tokaPalkka + ");\n"
                + "System.out.println(eka.compareTo(toka));\n"
                + "printed: "+vastausInt;

        assertTrue("Your compareTo-method gave the wrong answer. When this.salary is: " + ekaPalkka
                + ", and other.salary is: " + tokaPalkka + ", compareTo-method returns " + vastausInt + "\n"
                + lisaVihje, vastausInt > 0);


        ekaPalkka = 0;
        tokaPalkka = 0;
        vastausInt = testaaKahta(ekaNimi, ekaPalkka, tokaNimi, tokaPalkka);
        assertTrue("Your compareTo-method gave the wrong answer. When this.salary is: " + ekaPalkka
                + ", and other.salary is: " + tokaPalkka + ", compareTo-method returns " + vastausInt + "\n" + lisaVihje, vastausInt == 0);

        ekaPalkka = 300;
        tokaPalkka = 10;
        vastausInt = testaaKahta(ekaNimi, ekaPalkka, tokaNimi, tokaPalkka);
        assertTrue("Your compareTo-method gave the wrong answer. When this.salary is: " + ekaPalkka
                + ", and other.salary is " + tokaPalkka + ", compareTo-method returns " + vastausInt + "\n" + lisaVihje, vastausInt < 0);

    }
}