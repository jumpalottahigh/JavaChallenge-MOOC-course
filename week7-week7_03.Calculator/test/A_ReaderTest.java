
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.*;
import static org.junit.Assert.*;

@Points("3.1")
public class A_ReaderTest {

    String klassName = "Reader";
    Reflex.ClassRef<Object> klass;
    String luokanNimi = "Reader";
    Class c;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
        try {
            c = ReflectionUtils.findClass(luokanNimi);
        } catch (Throwable e) {
        }
    }

    @Test
    public void classIsPublic() {
        assertTrue("Class " + klassName + " has to be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "one instance variable (the one that is of the type Scanner)");
    }

    @Test
    public void hasMethodReadString() throws Throwable {
        new MockInOut("coffee\nmilk\npowerking\n");
        String metodi = "readString";

        String vv = "error caused by code Reader reader = new Reader();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("Create method public String " + metodi + "() for class " + klassName, klass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());

        String v = "\nError cause by code Reader reader = new Reader(); "
                + "reader.readString();";

        klass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    public void noExceptionsWhenReadingString() {
        new MockInOut("coffee\nmilk\npowerking\n");
        Object olio = newLukija();

        String syote = "coffee<enter>milk<enter>powerking<enter>";
        try {
            lueMerkkijono(olio);
            lueMerkkijono(olio);
            lueMerkkijono(olio);
        } catch (Throwable t) {
            if (t.toString().contains("such element")) {
                fail("User's input " + syote + ", reader.readString(); reader.readString(); System.out.print( reader.readString() ); caused exception " + t + "\n"
                        + "et kai kutsu Scannerin metodia liian monta kertaa?");
            } else {
                fail("User's input " + syote + ", reader.readString(); reader.readString(); System.out.print( reader.readString() ); caused exception " + t);
            }
        }
    }

    @Test
    public void returnsString() throws Throwable {
        new MockInOut("test\n");
        Object olio = newLukija();
        String vast1 = lueMerkkijono(olio);
        String syote = "test<enter>";
        assertEquals("User's input " + syote + ", calling reader.readString() ", "test", vast1);
    }

    @Test
    public void returnsManyStrings() throws Throwable {
        MockInOut io = new MockInOut("coffee\nmilk\npowerking\ncoca cola\n");
        Object olio = newLukija();

        String syote = "coffee<enter>milk<enter>powerking<enter>coca cola<enter>";

        String vast1 = lueMerkkijono(olio);
        String vast2 = lueMerkkijono(olio);
        String vast3 = lueMerkkijono(olio);
        String vast4 = lueMerkkijono(olio);

        assertEquals("User's input " + syote + ", System.out.print( reader.readString() );", "coffee", vast1);
        assertEquals("User's input " + syote + ", reader.readString(); System.out.print( reader.readString() ); ", "milk", vast2);
        assertEquals("User's input " + syote + ", reader.readString(); reader.readString(); System.out.print( reader.readString() ); ", "powerking", vast3);
        assertFalse("User's input was \"coca cola\", command reader.readString() returned \"coca\" \n"
                + "are you sure you're not using Scanner's method next(), always read the whole line, in other words use method nextLine()!", vast4.equals("coca"));
        assertEquals("User's input " + syote + ", reader.readString(); reader.readString(); System.out.print( reader.readString() ); \n"
                + "", "coca cola", vast4);
    }

    @Test
    public void hasMethodReadInteger() throws Throwable {
        String metodi = "readInteger";
        new MockInOut("1\n2\n3\n");

        String vv = "Error caused by Reader reader = new Reader();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("Create method public int " + metodi + "() for class " + klassName, klass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nError caused by  Reader reader = new Reader(); "
                + "reader.readInteger();";

        klass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();

//        String virhe = "tee luokalle " + luokanNimi + " metodi public int " + metodi + "()";
//        Method m = null;
//        try {
//            m = ReflectionUtils.requireMethod(c, metodi);
//        } catch (Throwable t) {
//            fail(virhe);
//        }
//        assertTrue(virhe, m.toString().contains("public"));
//        assertFalse(virhe, m.toString().contains("static"));
    }

    @Test
    public void noExceptionsWhenReadingValue() {
        new MockInOut("4\n-3\n16\n");
        Object olio = newLukija();

        String syote = "4<enter>-3<enter>16<enter>";
        try {
            lueKokonaisluku(olio);
            lueKokonaisluku(olio);
            lueKokonaisluku(olio);
        } catch (Throwable t) {
            if (t.toString().contains("such element")) {
                fail("User's input " + syote + ", reader.readInteger(); reader.readInteger(); System.out.print( reader.readInteger() ); caused exception " + t + "\n"
                        + "are you sure you're not calling Scanner's method too many times?");
            } else {
                fail("User's input " + syote + ", reader.readInteger(); reader.readInteger(); System.out.print( reader.readInteger() ); caused exception " + t);
            }
        }
    }

    @Test
    public void returnsValue() throws Throwable {
        new MockInOut("4\n");
        Object olio = newLukija();
        int vast1 = lueKokonaisluku(olio);
        String syote = "4<enter>";
        assertEquals("User's input " + syote + ", calling reader.readInteger() ", 4, vast1);
    }

    @Test
    public void returnsManyValues() throws Throwable {
        MockInOut io = new MockInOut("4\n-3\n16\n");
        Object olio = newLukija();

        String syote = "4<enter>-3<enter>16<enter>";
        int vast1 = lueKokonaisluku(olio);
        int vast2 = lueKokonaisluku(olio);
        int vast3 = lueKokonaisluku(olio);
        assertEquals("User's input " + syote + ", System.out.print( reader.readInteger() ); ", 4, vast1);
        assertEquals("User's input " + syote + ", reader.readInteger(); System.out.print( reader.readInteger() ); ", -3, vast2);
        assertEquals("User's input " + syote + ", reader.readInteger(); reader.readInteger(); System.out.print( reader.readInteger() ); ", 16, vast3);
    }

    @Test
    public void noExceptionsWhenReadingBothIntegerAndString() {
        MockInOut io = new MockInOut("java\n4\nolio\n16\nmethod\n");
        Object olio = newLukija();

        String syote = "java<enter>4<enter>olio<enter>16<enter>method<enter>";
        try {
            lueMerkkijono(olio);
            lueKokonaisluku(olio);
            lueMerkkijono(olio);
            lueKokonaisluku(olio);
            lueMerkkijono(olio);
        } catch (Throwable t) {
            if (t.toString().contains("such element")) {
                fail("User's input " + syote + ", reader.readString(); reader.readInteger(); reader.readString(); reader.readInteger(); reader.readString();  caused exception " + t + "\n"
                        + "are you sure you're not calling Scanner's method too many times?");
            } else {
                fail("User's input " + syote + ", reader.readString(); reader.readInteger(); reader.readString(); reader.readInteger(); reader.readString();  caused exception " + t + "\n"
                        + "are you sure you're not using Scanner's method nextInt() to read an integer value?"
                        + " if so, use command Integer.parseInt( scanner.nextLine() ); instead");
            }
        }
    }

    @Test
    public void worksWhenReadingBothIntegerAndString() throws Throwable {
        MockInOut io = new MockInOut("java\n4\nolio\n16\nmethod\n");
        Object olio = newLukija();

        String syote = "java<enter>4<enter>olio<enter>16<enter>method<enter>";

        String v1 = lueMerkkijono(olio);
        int v2 = lueKokonaisluku(olio);
        String v3 = lueMerkkijono(olio);
        int v4 = lueKokonaisluku(olio);
        String v5 = lueMerkkijono(olio);

        assertEquals("User's input " + syote + ", "
                + "System.out.print( reader.readString() ); ", "java", v1);
        assertEquals("User's input " + syote + ", "
                + "reader.readString() ; System.out.print( reader.readInteger() ); ", 4, v2);
        assertEquals("User's input " + syote + ", "
                + "reader.readString(); reader.readInteger(); System.out.print( reader.readString() ); ", "olio", v3);
        assertEquals("User's input " + syote + ", "
                + "reader.readString(); reader.readInteger(); reader.readString(); System.out.print(reader.readInteger() ); ", 16, v4);
        assertEquals("User's input " + syote + ", "
                + "reader.readString(); reader.readInteger(); reader.readString(); reader.readInteger() ; System.out.print(reader.readString() ); ", "method", v5);

    }

    private Object newLukija() {
        try {
            c = ReflectionUtils.findClass(luokanNimi);
            return ReflectionUtils.invokeConstructor(c.getConstructor());
        } catch (Throwable t) {
            fail("verify that the next operation can be successfully done in Main.java:  Reader reader = new Reader();");
        }
        return null;
    }

    private String lueMerkkijono(Object lukija) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "readString");
            return ReflectionUtils.invokeMethod(String.class, metodi, lukija);
        } catch (Throwable t) {
            throw t;
        }
    }

    private int lueKokonaisluku(Object lukija) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "readInteger");
            return ReflectionUtils.invokeMethod(int.class, metodi, lukija);
        } catch (Throwable t) {
            throw t;
        }
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("you don't need \"static variables\", remove from class " + klassName + " the following variable: " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All object variables of the class should be private, but class " + klassName + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }

            assertTrue("The class " + klassName + " should have only " + m + ", remove others", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}
