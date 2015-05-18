
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DictionaryTest {

    private Class dictionaryClass;
    private Constructor dictionaryConstructor;
    private Method translateMethod;
    private Method addMethod;
    private Method translationListMethod;
    private Method amountOfWordsMethod;
    String klassName = "Dictionary";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
        try {
            dictionaryClass = ReflectionUtils.findClass("Dictionary");
            dictionaryConstructor = ReflectionUtils.requireConstructor(dictionaryClass);
            translateMethod = ReflectionUtils.requireMethod(dictionaryClass, "translate", String.class);
            addMethod = ReflectionUtils.requireMethod(dictionaryClass, "add", String.class, String.class);
            amountOfWordsMethod = ReflectionUtils.requireMethod(dictionaryClass, "amountOfWords");
            translationListMethod = ReflectionUtils.requireMethod(dictionaryClass, "translationList");


        } catch (Throwable t) {
        }
    }

    @Test
    @Points("7.1")
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    @Points("7.1 7.2 7.3")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "an instance variable of type HashMap for storing translations");
    }

    @Test
    @Points("7.1 7.2 7.3")
    public void hasHashMap() {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();
        assertTrue("Add to class " + klassName + " an instance variable of type HashMap<String, String>", kentat.length == 1);
        assertTrue("Class " + klassName + " should have an instance variable of type Map<String, String>", kentat[0].toString().contains("Map"));
    }

    @Test
    @Points("7.1")
    public void emptyConstructor() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "()", ctor.isPublic());
        String v = "error caused by code new Dictionary();";
        ctor.withNiceError(v).invoke();
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    @Test
    @Points("7.1")
    public void addMethod() throws Throwable {
        String metodi = "add";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(String word, String translation) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).isPublic());

        String v = "\nError caused by code Dictionary s = new Dictionary(); "
                + "s.add(\"apina\",\"monkey\");";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("apina", "monkey");
    }

    @Test
    @Points("7.1")
    public void translateMethod() throws Throwable {
        String metodi = "translate";

        Object olio = luo();

        assertTrue("Create method public String " + metodi + "(String word) for class " + klassName,
                klass.method(olio, metodi)
                .returning(String.class)
                .taking(String.class)
                .isPublic());

        String v = "\nError caused by code Dictionary s = new Dictionary(); "
                + "s.translate(\"apina\");";

        klass.method(olio, metodi)
                .returning(String.class).taking(String.class).withNiceError(v).invoke("apina");
    }

    @Test
    @Points("7.1")
    public void dictionaryShouldReturnNullIfTranslationDoesntExist() {
        Object dictionary = luoDictionarySanoilla("jakkara", "avföring");
        if (dictionary == null) {
            fail("Are you sure that adding a new translation to the dictionary works?");
        }

        try {
            Assert.assertNotNull("If method translate is called with a word that's known by the dictionary, it should return the translation of the word. Ensure also that add-method adds a new word and its translation correctly to the HashMap.", translateMethod.invoke(dictionary, "jakkara"));
        } catch (Throwable t) {
            fail(t.getMessage());
        }

        try {
            Assert.assertNull("If method translate is called with an unknown word, the dictionary should return null.", translateMethod.invoke(dictionary, "kuppi"));
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    @Test
    @Points("7.1")
    public void dictionaryShouldReturnTranslation() {
        String[] sanat = new String[20];
        for (int i = 0; i < sanat.length; i++) {
            sanat[i] = "" + i;
        }

        Object dictionary = luoDictionarySanoilla(sanat);
        if (dictionary == null) {
            fail("Are you sure that adding a new translation to the dictionary works?");
        }

        for (int i = 0; i < sanat.length; i += 2) {
            String sana = sanat[i];
            String kaannos = sanat[i + 1];

            try {
                Assert.assertEquals(kaannos, translateMethod.invoke(dictionary, sana));
            } catch (Throwable t) {
                fail("If 10 words and their translations are added to the dictionary, then every word and their matching translation should be found from the dictionary.");
            }
        }
    }

    @Test
    @Points("7.2")
    public void methodAmountOfWords() throws Throwable {
        String metodi = "amountOfWords";

        Object olio = luo();

        assertTrue("Create method public int " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returning(int.class)
                .takingNoParams()
                .isPublic());

        String v = "\nError caused by code Dictionary s = new Dictionary(); "
                + "s.amountOfWords();";

        klass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("7.2")
    public void methodAmountOfWordsCorrectWhenTwoWords() throws Exception {
        Object dictionary = luoDictionarySanoilla("a", "b", "c", "d");

        int sanoja = (Integer) amountOfWordsMethod.invoke(dictionary);

        assertEquals("Method amountOfWords() doesn't work when dictionary has two words", 2, sanoja);
    }

    @Test
    @Points("7.2")
    public void methodAmountOfWordsCorrectWhenThreeWords() throws Exception {
        Object dictionary = luoDictionarySanoilla("a", "b", "c", "d", "e", "f");

        int sanoja = (Integer) amountOfWordsMethod.invoke(dictionary);

        assertEquals("Method amountOfWords() doesn't work when dictionary has three words", 3, sanoja);

    }

    @Test
    @Points("7.3")
    public void methodTranslationList() throws Throwable {
        String metodi = "translationList";

        Object olio = luo();

        boolean isListInterface = false;

        if (klass.method(olio, metodi)
                .returning(List.class)
                .takingNoParams()
                .isPublic()) {
            isListInterface = true;
        }

        assertTrue("Create method public List<String> " + metodi + "() for class " + klassName,
                isListInterface || klass.method(olio, metodi)
                .returning(ArrayList.class)
                .takingNoParams()
                .isPublic());

        String v = "\nError caused by code Dictionary s = new Dictionary(); "
                + "s.translationList();";

        if (isListInterface) {
            klass.method(olio, metodi)
                .returning(List.class).takingNoParams().withNiceError(v).invoke();
        } else {
            klass.method(olio, metodi)
                .returning(ArrayList.class).takingNoParams().withNiceError(v).invoke();
        }
    }

    @Test
    @Points("7.3")
    public void methodTranslationListWorks() {
        String[] sanat = new String[20];
        for (int i = 0; i < sanat.length; i++) {
            sanat[i] = "" + i;
        }

        Object dictionary = luoDictionarySanoilla(sanat);
        if (dictionary == null) {
            fail("Are you sure that adding a new translation to the dictionary works?");
        }

        ArrayList<String> translationList = null;
        try {
            translationList = (ArrayList<String>) translationListMethod.invoke(dictionary);
        } catch (Throwable t) {
            fail("Are you sure that method translationList is public and it always returns an ArrayList-object?");
        }

        assertFalse("Method translationList returns null. Method should always return an ArrayList-object!", translationList == null);

        for (int i = 0; i < sanat.length; i += 2) {
            String sana = sanat[i];
            String kaannos = sanat[i + 1];

            boolean loytyi = false;
            for (String kaannospari : translationList) {
                if (kaannospari.contains(sana) && kaannospari.contains(kaannos) && kaannospari.contains("=")) {
                    loytyi = true;
                    break;
                }
            }

            if (!loytyi) {
                fail("Check that method translationList returns list of \"key = value\"-pairs");
            }
        }
    }

    /*
     *
     */
    private Object luoDictionarySanoilla(String... sanatJaKaannokset) {
        try {
            Object dictionary = ReflectionUtils.invokeConstructor(dictionaryConstructor);
            for (int i = 0; i < sanatJaKaannokset.length; i += 2) {
                addMethod.invoke(dictionary, sanatJaKaannokset[i], sanatJaKaannokset[i + 1]);
            }
            return dictionary;
        } catch (Throwable ex) {
            return null;
        }
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
