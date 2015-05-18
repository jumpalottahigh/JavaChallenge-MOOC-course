
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TextUserInterfaceTest<_Dictionary> {

    private Class dictionaryClass;
    private Constructor dictionaryConstructor;
    private Method translateMethod;
    private Method addMethod;

    private Class textUserInterfaceClass;
    private Constructor textUserInterfaceConstructor;
    private Method startMethod;

    String klassName = "TextUserInterface";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
        try {
            dictionaryClass = ReflectionUtils.findClass("Dictionary");
            dictionaryConstructor = ReflectionUtils.requireConstructor(dictionaryClass);
            translateMethod = ReflectionUtils.requireMethod(dictionaryClass, "translate", String.class);
            addMethod = ReflectionUtils.requireMethod(dictionaryClass, "add", String.class, String.class);
            textUserInterfaceClass = ReflectionUtils.findClass("TextUserInterface");
            textUserInterfaceConstructor = ReflectionUtils.requireConstructor(textUserInterfaceClass, Scanner.class, dictionaryClass);
            startMethod = ReflectionUtils.requireMethod(textUserInterfaceClass, "start");
        } catch (Throwable t) {
        }
    }

    @Test
    @Points("7.4")
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    @Points("7.4 7.5")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 2, "instance variables of type Scanner and Dictionary");
    }

    @Test
    @Points("7.4")
    public void constructor()  throws Throwable {
        Reflex.ClassRef<_Dictionary> _DictionaryRef = Reflex.reflect("Dictionary");
        _Dictionary sk = _DictionaryRef.constructor().takingNoParams().invoke();

        Reflex.MethodRef2<Object, Object, Scanner, _Dictionary> ctor = klass.constructor().
                taking(Scanner.class, _DictionaryRef.cls()).withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "(Scanner reader, Dictionary dictionary)", ctor.isPublic());
        String v = "error caused by code new TextUserInterface(new Scanner(System.in), new Dictionary());";
        ctor.withNiceError(v).invoke(new Scanner(System.in), sk);
    }

    public Object luo() throws Throwable {
        Reflex.ClassRef<_Dictionary> _DictionaryRef = Reflex.reflect("Dictionary");
        _Dictionary sk = _DictionaryRef.constructor().takingNoParams().invoke();

        Reflex.MethodRef2<Object, Object, Scanner, _Dictionary> ctor = klass.constructor().
                taking(Scanner.class, _DictionaryRef.cls()).withNiceError();
        return ctor.withNiceError().invoke(new Scanner("quit"), sk);
    }

    public Object luo(Scanner skanneri) throws Throwable {
        Reflex.ClassRef<_Dictionary> _DictionaryRef = Reflex.reflect("Dictionary");
        _Dictionary sk = _DictionaryRef.constructor().takingNoParams().invoke();

        Reflex.MethodRef2<Object, Object, Scanner, _Dictionary> ctor = klass.constructor().
                taking(Scanner.class, _DictionaryRef.cls()).withNiceError();
        return ctor.withNiceError().invoke(skanneri, sk);
    }

    @Test
    @Points("7.4")
    public void startMethod() throws Throwable {
        String metodi = "start";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nError caused by code\n "
                + "TextUserInterface t = new TextUserInterface(new Scanner(System.in), new Dictionary());\n"
                + "t.start();";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("7.4")
    public void startMethod2() throws Throwable {
        Scanner lukija = new Scanner("add\nporkkana\ncarrot\nquit\n");
        String metodi = "start";

        Object olio = luo(lukija);

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nError caused by code\n "
                + "TextUserInterface t = new TextUserInterface(new Scanner(System.in), new Dictionary());\n"
                + "t.start();\n"
                + "User's input was:\n"
                + " add\n"
                + " porkkana\n"
                + " carrto\n"
                + " quit\n";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Test(timeout = 200)
    @Points("7.4")
    public void textUserInterFaceStatementQuitWorks() {
        Scanner lukija = new Scanner("quit\n");
        Object textUserInterface = luoTextUserInterfaceScannerillaJaSanoilla(lukija, "a", "b");
        try {
            startMethod.invoke(textUserInterface);
        } catch (Throwable t) {
            fail("Verify that user exits from text user interface when he writes statement \"quit\". Are you using Scanner given as a parameter in the constructor?");
        }
    }

    @Test(timeout = 200)
    @Points("7.4")
    public void textUserInterFaceStatementQuitWorksEvenWhenTheresOtherStatements() {
        Scanner lukija = new Scanner("apu\nporkkana\nquit\n");
        Object textUserInterface = luoTextUserInterfaceScannerillaJaSanoilla(lukija, "a", "b");
        try {
            startMethod.invoke(textUserInterface);
        } catch (Throwable t) {
            fail("Verify that user exits from text user interface when he writes statement \"quit\".");
        }
    }

    @Test(timeout = 200)
    @Points("7.5")
    public void addStatementWorks() {
        Scanner lukija = new Scanner("add\nporkkana\ncarrot\nquit\n");
        Object dictionary = luoDictionarySanoilla();
        Object textUserInterface = luoTextUserInterface(lukija, dictionary);

        try {
            startMethod.invoke(textUserInterface);
        } catch (Throwable t) {
            fail("Verify that user exits from text user interface when he writes statement \"quit\".");
        }

        if (!sisaltaaSanaparin(dictionary, "porkkana", "carrot")) {
            fail("Verify that statement \"add\" adds a new word pair to the dictionary.");
        }
    }

    @Test(timeout = 200)
    @Points("7.5")
    public void addStatementWorksWithMultipleWordPairs() {
        Scanner lukija = new Scanner("add\nporkkana\ncarrot\nadd\navain\nkey\nquit\n");
        Object dictionary = luoDictionarySanoilla();
        Object textUserInterface = luoTextUserInterface(lukija, dictionary);

        try {
            startMethod.invoke(textUserInterface);
        } catch (Throwable t) {
            fail("Verify that user exits from text user interface when he writes statement \"quit\".");
        }

        if (!sisaltaaSanaparin(dictionary, "porkkana", "carrot")) {
            fail("Verify that statement \"add\" adds a new word pair to the dictionary.");
        }

        if (!sisaltaaSanaparin(dictionary, "avain", "key")) {
            fail("Verify that statement \"add\" adds a new word pair to the dictionary.");
        }
    }

    @Test(timeout = 500)
    @Points("7.5")
    public void translateStatementWorksWithMultipleWordPairs() {
        long start = System.currentTimeMillis();
        MockInOut io = new MockInOut("");
        Scanner lukija = new Scanner("translate\nporkkana\nquit\n");
        Object dictionary = luoDictionarySanoilla("porkkana", "carrot");
        Object textUserInterface = luoTextUserInterface(lukija, dictionary);

        try {
            startMethod.invoke(textUserInterface);
        } catch (Throwable t) {
            fail("Verify that user exits from text user interface when he writes statement \"quit\".");
        }

        String output = io.getOutput();
        if (!output.contains("carrot")) {
            fail("Verify that statement \"translate\" returns desired string.");
        }
        int maxAllowed = 350;
        long end = System.currentTimeMillis();
        assertTrue("Test timed out. Limit " + maxAllowed + " milliseconds, took " + (end - start) + " milliseconds", end-start < maxAllowed);
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

    private Object luoTextUserInterfaceScannerillaJaSanoilla(Scanner lukija, String... sanatJaKaannokset) {
        Object dictionary = luoDictionarySanoilla(sanatJaKaannokset);

        try {
            return ReflectionUtils.invokeConstructor(textUserInterfaceConstructor, lukija, dictionary);

        } catch (Throwable ex) {
            return null;
        }
    }

    private Object luoTextUserInterface(Scanner lukija, Object dictionary) {
        try {
            return ReflectionUtils.invokeConstructor(textUserInterfaceConstructor, lukija, dictionary);
        } catch (Throwable ex) {
            return null;
        }
    }

    private boolean sisaltaaSanaparin(Object dictionary, String sana, String kaannos) {
        try {
            return kaannos.equals(translateMethod.invoke(dictionary, sana));
        } catch (Throwable t) {
        }

        return false;
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
