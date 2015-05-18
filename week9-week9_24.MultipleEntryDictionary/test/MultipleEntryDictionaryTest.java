
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.ClassRef;
import java.lang.reflect.Field;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
import dictionary.MultipleEntryDictionary;

@Points("24")
public class MultipleEntryDictionaryTest {

    String klassName = "dictionary.PersonalMultipleEntryDictionary";
    Reflex.ClassRef<Object> klass;

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
        saniteettitarkastus(klassName, 3, "instance variable for storing translations");
    }

    @Test
    public void emptyConstructor() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: "
                + "public " + s(klassName) + "()", ctor.isPublic());
        String v = "error caused by code new PersonalMultipleEntryDictionary();";
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
        Class kali = MultipleEntryDictionary.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class PersonalMultipleEntryDictionary implement interface MultipleEntryDictionary?");
        }
    }

    /*
     *
     */
    @Test
    public void addMethod() throws Throwable {
        String metodi = "add";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(String word, String entry) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).isPublic());

        String v = "\nError caused by code MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("apina", "monkey");
    }

    @Test
    public void translateMethod() throws Throwable {
        String metodi = "translate";

        Object olio = luo();

        assertTrue("Create method public Set<String> " + metodi + "(String word) for class " + klassName,
                klass.method(olio, metodi)
                .returning(Set.class)
                .taking(String.class)
                .isPublic());

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).invoke("apina", "monkey");

        String v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.translate(\"apina\");\n";


        Set vast = new HashSet();
        vast.add("monkey");

        assertEquals(v, vast, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));
    }

    @Test
    public void translateMethodNoWord() throws Throwable {
        String metodi = "translate";

        Object olio = luo();

        String v = "\nError caused by code\n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.translate(\"apina\");\n";

        assertEquals(v, null, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));
    }

    @Test
    public void translateMethodTwoTranslations() throws Throwable {
        String metodi = "translate";

        Object olio = luo();

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).invoke("apina", "monkey");
        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).invoke("apina", "apfe");

        String v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.add(\"apina\",\"apfe\");\n"
                + "s.translate(\"apina\");\n";


        Set vast = new HashSet();
        vast.add("monkey");
        vast.add("apfe");

        assertEquals(v, vast, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));
    }

    @Test
    public void translateMethodMultipleWords() throws Throwable {
        String metodi = "translate";

        Object olio = luo();

        String v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.add(\"apina\",\"apfe\");\n"
                + "s.add(\"juusto\",\"cheese\");\n"
                + "s.add(\"maito\",\"milk\");\n"
                + "s.translate(\"apina\");\n";

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("apina", "monkey");
        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("apina", "apfe");
        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("juusto", "cheese");
        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("maito", "milk");

        Set vast = new HashSet();
        vast.add("monkey");
        vast.add("apfe");

        assertEquals(v, vast, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));

        v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.add(\"apina\",\"apfe\");\n"
                + "s.add(\"juusto\",\"cheese\");\n"
                + "s.add(\"maito\",\"milk\");\n"
                + "s.translate(\"juusto\");\n";

        vast = new HashSet();
        vast.add("cheese");
        assertEquals(v, vast, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("juusto"));

        v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.add(\"apina\",\"apfe\");\n"
                + "s.add(\"juusto\",\"cheese\");\n"
                + "s.add(\"maito\",\"milk\");\n"
                + "s.translate(\"peruna\");\n";

        assertEquals(v, null, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("peruna"));
    }

    public void testaaYhdenKaannoksenLisays() {
        MultipleEntryDictionary dictionary = luoInstanssi();
        Set<String> kaannokset = new HashSet<String>();
        kaannokset.add("word");

        testaaKaannos(dictionary, "word", kaannokset);
    }

    @Test
    public void testAddingMultipleTranslations() {
        MultipleEntryDictionary dictionary = luoInstanssi();
        Set<String> kaannokset = new HashSet<String>();
        kaannokset.add("word");

        testaaKaannos(dictionary, "word", kaannokset);

        kaannokset.add("ord");
        kaannokset.add("käännös1");
        kaannokset.add("käännös2");

        testaaKaannos(dictionary, "word", kaannokset);
    }

    @Test
    public void testAddingMultipleWords() {
        MultipleEntryDictionary dictionary = luoInstanssi();
        Set<String> kaannokset = new HashSet<String>();
        kaannokset.add("word");
        kaannokset.add("ord");
        kaannokset.add("käännös1");
        kaannokset.add("käännös2");

        testaaKaannos(dictionary, "word", kaannokset);

        Set<String> kaannokset2 = new HashSet<String>();
        kaannokset2.add("jungle");
        kaannokset2.add("jungel");
        kaannokset2.add("käännös3");
        kaannokset2.add("käännös4");

        testaaKaannos(dictionary, "viidakko", kaannokset2);
    }

    /*
     *
     */
    @Test
    public void removeMethod() throws Throwable {
        String metodi = "remove";

        Object olio = luo();

        assertTrue("Create method public String " + metodi + "(String word) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid()
                .taking(String.class)
                .isPublic());
    }

    @Test
    public void removeExistingEntry() throws Throwable {
        String metodi = "remove";

        Object olio = luo();

        String v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.remove(\"apina\");\n"
                + "s.translate(\"apina\");";

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).invoke("apina", "monkey");

        klass.method(olio, "remove")
                .returningVoid().taking(String.class).withNiceError(v).invoke("apina");

        assertEquals(v, null, klass.method(olio, "translate")
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));
    }

    public void removeIfMultiple() throws Throwable {
        String metodi = "remove";

        Object olio = luo();

        String v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.add(\"apina\",\"apfe\");\n"
                + "s.remove(\"apina\");\n"
                + "s.translate(\"apina\");";

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).invoke("apina", "monkey");

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).invoke("apina", "apfe");

        klass.method(olio, "remove")
                .returningVoid().taking(String.class).withNiceError(v).invoke("apina");

        assertEquals(v, null, klass.method(olio, "translate")
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));
    }

    @Test
    public void multipleTranslationsAndRemoves() throws Throwable {
        String metodi = "translate";

        Object olio = luo();

        String v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.add(\"apina\",\"apfe\");\n"
                + "s.add(\"juusto\",\"cheese\");\n"
                + "s.remove(\"apina\");"
                + "s.add(\"maito\",\"milk\");\n"
                + "s.translate(\"apina\");\n";

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("apina", "monkey");
        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("apina", "apfe");
        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("juusto", "cheese");
        klass.method(olio, "remove")
                .returningVoid().taking(String.class).withNiceError(v).invoke("apina");

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("maito", "milk");

        assertEquals(v, null, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));

        v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.add(\"apina\",\"apfe\");\n"
                + "s.add(\"juusto\",\"cheese\");\n"
                + "s.remove(\"apina\");"
                + "s.add(\"maito\",\"milk\");\n"
                + "s.translate(\"juusto\");\n";

        HashSet vast = new HashSet();
        vast.add("cheese");
        assertEquals(v, vast, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("juusto"));

        v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.add(\"apina\",\"apfe\");\n"
                + "s.add(\"juusto\",\"cheese\");\n"
                + "s.remove(\"apina\");\n"
                + "s.add(\"maito\",\"milk\");\n"
                + "s.add(\"apina\",\"monkee\");\n"
                + "s.translate(\"apina\");\n";


        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("apina", "monkee");


        vast = new HashSet();
        vast.add("monkee");
        assertEquals(v, vast, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));
    }

    public void removeNonExistent() throws Throwable {
        String metodi = "remove";

        Object olio = luo();

        String v = "\nError caused by code \n"
                + "MultipleEntryDictionary s = new PersonalMultipleEntryDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n"
                + "s.remove(\"kerma\");\n"
                + "s.translate(\"apina\");";

        klass.method(olio, "add")
                .returningVoid().taking(String.class, String.class).invoke("apina", "monkey");

        klass.method(olio, "remove")
                .returningVoid().taking(String.class).withNiceError(v).invoke("kerma");


        Set vast = new HashSet();
        vast.add("monkey");
        vast.add("apfe");

        assertEquals(v, vast, klass.method(olio, metodi)
                .returning(Set.class).taking(String.class).withNiceError(v).invoke("apina"));
    }

    @Test
    public void testAddingManyWordsAndRemove() {
        MultipleEntryDictionary dictionary = luoInstanssi();
        Set<String> kaannokset = new HashSet<String>();
        kaannokset.add("word");
        kaannokset.add("ord");
        kaannokset.add("käännös1");
        kaannokset.add("käännös2");

        testaaKaannos(dictionary, "word", kaannokset);

        Set<String> kaannokset2 = new HashSet<String>();
        kaannokset2.add("jungle");
        kaannokset2.add("jungel");
        kaannokset2.add("käännös3");
        kaannokset2.add("käännös4");

        testaaKaannos(dictionary, "viidakko", kaannokset2);

        testaaPoisto(dictionary, "word");

        testaaPoisto(dictionary, "viidakko");
    }

    @Test
    public void testNonExistentWord() {
        MultipleEntryDictionary dictionary = luoInstanssi();
        testaaOlematonSana(dictionary, "olematonword");

        Set<String> kaannokset2 = new HashSet<String>();
        kaannokset2.add("jungle");
        kaannokset2.add("jungel");
        kaannokset2.add("käännös3");
        kaannokset2.add("käännös4");

        testaaKaannos(dictionary, "viidakko", kaannokset2);

        testaaPoisto(dictionary, "word");

        testaaOlematonSana(dictionary, "olematonword2");
    }

    private void testaaOlematonSana(MultipleEntryDictionary dictionary,
            String word) {
        Set<String> kaannokset = dictionary.translate(word);
        Assert.assertTrue("Tried to get word \"" + word + "\", "
                + "which wasn't added to dictionary but returned translations were: "
                + kaannokset, kaannokset == null || kaannokset.isEmpty());
    }

    private void testaaKaannos(MultipleEntryDictionary dictionary,
            String word, Set<String> kaannokset) {
        for (String entry : kaannokset) {
            dictionary.add(word, entry);
        }

        Set<String> palautetut = dictionary.translate(word);
        if (palautetut == null) {
            Assert.fail("Added word \"" + word + "\" "
                    + "with translations: " + kaannokset + ", but translate()-method "
                    + "returns null for word.");
            return;
        }

        Assert.assertTrue("Added word \"" + word + "\" "
                + "with translations: " + kaannokset + ", but "
                + "returned list of translations was: " + palautetut,
                palautetut.containsAll(kaannokset));
    }

    private void testaaPoisto(MultipleEntryDictionary dictionary,
            String word) {
        dictionary.remove(word);
        Set<String> kaannokset = dictionary.translate(word);
        Assert.assertTrue("Removed word \"" + word + "\", "
                + "but returned list of translations wasn't null or empty: "
                + kaannokset, kaannokset == null || kaannokset.isEmpty());
    }

    private MultipleEntryDictionary luoInstanssi() {
        String luokanNimi = "dictionary.PersonalMultipleEntryDictionary";
        ClassRef<?> luokka;
        try {
            luokka = Reflex.reflect(luokanNimi);
        } catch (Throwable t) {
            Assert.fail("Class " + luokanNimi + " doesn't exist. In this exercise you have to create this class.");
            return null;
        }
        if (!MultipleEntryDictionary.class.isAssignableFrom(
                luokka.getReferencedClass())) {
            Assert.fail("Class " + luokanNimi + " must "
                    + "implement interface dictionary.MultipleEntryDictionary");
        }

        Object instanssi;
        try {
            instanssi = luokka.constructor().takingNoParams().invoke();
        } catch (Throwable t) {
            Assert.fail("Class " + luokanNimi + " doesn't have public constructor which takes no parameters.");
            return null;
        }

        return (MultipleEntryDictionary) instanssi;
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
