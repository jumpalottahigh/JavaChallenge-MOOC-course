
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.ClassRef;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tools.DuplicateRemover;
import static org.junit.Assert.*;

@Points("25")
public class DuplicateRemoverTest {

    String klassName = "tools.PersonalDuplicateRemover";
    Reflex.ClassRef<Object> klass;
    private Object olio;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void classPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 2, "instance variables for set of unique strings and for number of detected duplicates");
    }

    @Test
    public void emptyConstructor() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: "
                + "public " + s(klassName) + "()", ctor.isPublic());
        String v = "error caused by code new PersonalDuplicateRemover();";
        ctor.withNiceError(v).invoke();
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    @Test
    public void implementsInterface() {
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = DuplicateRemover.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class PersonalDuplicateRemover implement interface DuplicateRemover?");
        }
    }

    @Test
    public void addMethod() throws Throwable {
        String metodi = "add";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(String characterString) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(String.class).isPublic());

        String v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class).withNiceError(v).invoke("apina");
    }

    @Test
    public void getNumberOfDetectedDuplicatesMethod() throws Throwable {
        String metodi = "getNumberOfDetectedDuplicates";

        Object olio = luo();

        assertTrue("Create method public int " + metodi + "() for class " + s(klassName),
                klass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.getNumberOfDetectedDuplicates();";

        assertEquals(v, 0, (int) klass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke());
    }

    @Test
    public void addAndDuplicates() throws Throwable {
        Object olio = luo();

        String v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.getNumberOfDetectedDuplicates();";

        add(olio, "apina", v);

        assertEquals(v, 0, dup(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.getNumberOfDetectedDuplicates();";

        add(olio, "apina", v);
        assertEquals(v, 1, dup(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.getNumberOfDetectedDuplicates();";

        add(olio, "gorilla", v);
        assertEquals(v, 1, dup(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"apina\");\n"
                + "s.getNumberOfDetectedDuplicates();";

        add(olio, "gorilla", v);
        add(olio, "apina", v);

        assertEquals(v, 3, dup(olio, v));
    }

    @Test
    public void uniqueCharacterStringsMethod() throws Throwable {
        Object olio = luo();

        assertTrue("Create method public Set<String> getUniqueCharacterStrings() for class " + klassName,
                klass.method(olio, "getUniqueCharacterStrings")
                .returning(Set.class)
                .takingNoParams()
                .isPublic());

        String v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.getUniqueCharacterStrings();";

        add(olio, "apina", klassName);

        klass.method(olio, "getUniqueCharacterStrings")
                .returning(Set.class)
                .takingNoParams().withNiceError(v).invoke();

    }

    @Test
    public void uniquesWork() throws Throwable {
        Object olio = luo();

        String v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.getUniqueCharacterStrings()";

        add(olio, "apina", v);

        Set<String> odot = new HashSet<String>();
        odot.add("apina");

        assertEquals(v, odot, uniq(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.getUniqueCharacterStrings()";

        add(olio, "apina", v);
        assertEquals(v, odot, uniq(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.getUniqueCharacterStrings()";

        add(olio, "gorilla", v);
        odot.add("gorilla");
        assertEquals(v, odot, uniq(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"oranki\");\n"
                + "s.add(\"apina\");\n"
                + "s.getUniqueCharacterStrings();";

        add(olio, "gorilla", v);
        add(olio, "oranki", v);
        add(olio, "apina", v);

        add(olio, "gorilla", v);
        odot.add("gorilla");
        odot.add("oranki");
        assertEquals(v, odot, uniq(olio, v));
    }

    @Test
    public void emptyMethod() throws Throwable {
        Object olio = luo();

        assertTrue("Create method void empty() for class " + klassName,
                klass.method(olio, "empty")
                .returningVoid()
                .takingNoParams()
                .isPublic());

        String v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.empty();";

        add(olio, "apina", klassName);

        klass.method(olio, "empty")
                .returningVoid()
                .takingNoParams().withNiceError(v).invoke();

    }

    @Test
    public void emptyWorks() throws Throwable {
        Object olio = luo();

        String v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.getUniqueCharacterStrings()";

        add(olio, "apina", v);

        Set<String> odot = new HashSet<String>();
        odot.add("apina");

        assertEquals(v, odot, uniq(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.getUniqueCharacterStrings()";

        add(olio, "apina", v);
        assertEquals(v, odot, uniq(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.getUniqueCharacterStrings()";

        add(olio, "gorilla", v);
        odot.add("gorilla");
        assertEquals(v, odot, uniq(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"oranki\");\n"
                + "s.add(\"apina\");\n"
                + "s.getUniqueCharacterStrings();";

        add(olio, "gorilla", v);
        add(olio, "oranki", v);
        add(olio, "apina", v);

        add(olio, "gorilla", v);
        odot.add("gorilla");

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"oranki\");\n"
                + "s.add(\"apina\");\n"
                + "s.empty();\n"
                + "s.getUniqueCharacterStrings();";

        tyhj(olio, v);

        odot = new HashSet();

        assertEquals(v, odot, uniq(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"oranki\");\n"
                + "s.add(\"apina\");\n"
                + "s.empty();\n"
                + "s.getNumberOfDetectedDuplicates();";

        assertEquals(v, 0, dup(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"oranki\");\n"
                + "s.add(\"apina\");\n"
                + "s.empty();\n"
                + "s.add(\"kivi\");\n"
                + "s.add(\"kivi\");\n"
                + "s.getUniqueCharacterStrings();";

        add(olio, "kivi", v);
        add(olio, "kivi", v);

        odot.add("kivi");

        assertEquals(v, odot, uniq(olio, v));

        v = "\nError caused by code DuplicateRemover s = new PersonalDuplicateRemover();\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"apina\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"gorilla\");\n"
                + "s.add(\"oranki\");\n"
                + "s.add(\"apina\");\n"
                + "s.empty();\n"
                + "s.add(\"kivi\");\n"
                + "s.add(\"kivi\");\n"
                + "s.getNumberOfDetectedDuplicates();";

        assertEquals(v, 1, dup(olio, v));
    }

    private void tyhj(Object o, String v) throws Throwable {
        klass.method(o, "empty")
                .returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    private Set uniq(Object o, String v) throws Throwable {
        return klass.method(o, "getUniqueCharacterStrings")
                .returning(Set.class).takingNoParams().withNiceError(v).invoke();
    }

    private int dup(Object o, String v) throws Throwable {
        return (int) klass.method(o, "getNumberOfDetectedDuplicates")
                .returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    private void add(Object o, String sana, String v) throws Throwable {
        klass.method(o, "add")
                .returningVoid().taking(String.class).withNiceError(v).invoke(sana);
    }
    /*
     *
     */

    @Test
    public void testAddingDuplicates() {
        DuplicateRemover poistaja = luoInstanssi();
        testaaMerkkijononLisays(poistaja, "eka");
        testaaMerkkijononLisays(poistaja, "toka");
        testaaMerkkijononLisays(poistaja, "kolmas");
        testaaMerkkijononLisays(poistaja, "toka");
        testaaMerkkijononLisays(poistaja, "eka");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
    }

    @Test
    public void testEmpty() {
        DuplicateRemover poistaja = luoInstanssi();

        testaaMerkkijononLisays(poistaja, "eka");
        testaaMerkkijononLisays(poistaja, "toka");
        testaaMerkkijononLisays(poistaja, "kolmas");
        testaaMerkkijononLisays(poistaja, "toka");
        testaaMerkkijononLisays(poistaja, "eka");

        testaaTyhjennys(poistaja);

        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");

        testaaTyhjennys(poistaja);
    }

    private void testaaTyhjennys(DuplicateRemover poistaja) {
        poistaja.empty();
        Set<String> uniikitJonot = poistaja.getUniqueCharacterStrings();
        if (uniikitJonot == null) {
            Assert.fail("Method getUniqueCharacterStrings() returned null, "
                    + "although returned value should always be "
                    + "an object which implements the interface Set<String>");
            return;
        }

        int duplikaatit = poistaja.getNumberOfDetectedDuplicates();
        Assert.assertTrue("After calling method empty(), number of detected duplicates should be zero. Returned value was: "
                + duplikaatit, duplikaatit == 0);

        boolean tyhja = uniikitJonot.isEmpty();
        Assert.assertTrue("After calling method empty(), list of unique characterStrings should be empty. Returned list was: "
                + uniikitJonot.toString(), tyhja);
    }

    private void testaaMerkkijononLisays(DuplicateRemover poistaja,
            String characterString) {
        if (poistaja.getUniqueCharacterStrings() == null) {
            Assert.fail("Method getUniqueCharacterStrings() returned null, "
                    + "although returned value should always be "
                    + "an object which implements the interface Set<String>");
            return;
        }

        boolean loytyyDuplikaatti = poistaja.getUniqueCharacterStrings().contains(characterString);

        int maaraEnnen = poistaja.getUniqueCharacterStrings().size();
        int duplikaatitEnnen = poistaja.getNumberOfDetectedDuplicates();
        poistaja.add(characterString);

        int maaraJalkeen = poistaja.getUniqueCharacterStrings().size();
        int duplikaatitJalkeen = poistaja.getNumberOfDetectedDuplicates();

        if (loytyyDuplikaatti) {
            Assert.assertTrue("Amount of unique characterStrings shouldn't change when previously added "
                    + " characterString is added again ( = duplicate). "
                    + "Amount before adding was: " + maaraEnnen
                    + ", amount after adding was: " + maaraJalkeen,
                    maaraJalkeen == maaraEnnen);
            Assert.assertTrue("Number of duplicates should increase by one, "
                    + "when previously added characterString is added again ( = duplicate). Number of duplicates before adding was: " + duplikaatitEnnen
                    + ", number of duplicates after adding was: " + duplikaatitJalkeen, duplikaatitJalkeen == (duplikaatitEnnen + 1));
        } else {
            Assert.assertTrue("Number of unique characterStrings should increase by one "
                    + "when a new unique characterString is added. "
                    + "Amount before adding was: " + maaraEnnen
                    + ", amount after adding was: " + maaraJalkeen,
                    maaraJalkeen == (maaraEnnen + 1));
            Assert.assertTrue("Number of duplicates shouldn't change, "
                    + "when a new unique characterString is added. Number of duplicates before adding was: " + duplikaatitEnnen
                    + ", number of duplicates after adding was: " + duplikaatitJalkeen, duplikaatitJalkeen == duplikaatitEnnen);
        }

    }

    private DuplicateRemover luoInstanssi() {
        String luokanNimi = "tools.PersonalDuplicateRemover";
        ClassRef<?> luokka;
        try {
            luokka = Reflex.reflect(luokanNimi);
        } catch (Throwable t) {
            Assert.fail("Class " + luokanNimi + " doesn't exist. You have to create this class.");
            return null;
        }
        if (!DuplicateRemover.class.isAssignableFrom(
                luokka.getReferencedClass())) {
            Assert.fail("Class " + luokanNimi + " should "
                    + "implement interface tools.DuplicateRemover");
        }

        Object instanssi;
        try {
            instanssi = luokka.constructor().takingNoParams().invoke();
        } catch (Throwable t) {
            Assert.fail("Class " + luokanNimi + " doesn't have a public constructor which takes no parameters.");
            return null;
        }

        return (DuplicateRemover) instanssi;
    }

    private String s(String klassName) {
        return klassName.substring(klassName.lastIndexOf(".") + 1);
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("you do not need \"static variables\", remove from class " + s(klassName) + " the following variable: " + kentta(field.toString(), s(klassName)), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All instance variables should be private but class " + s(klassName) + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("The class " + s(klassName) + " should only have " + m + ", remove others", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "").replace("java.io.", "");
    }
}
