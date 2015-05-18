
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef1;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("21")
public class PrinterTest {

    String klassName = "Printer";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setUp() {
        classRef = Reflex.reflect(klassName);
    }

    @Test
    public void classExists() {
        classRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + s(klassName) + " {...\n}", classRef.isPublic());
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 10, "");
    }

    @Test
    public void hasConstructor() throws Throwable {
        MethodRef1<Object, Object, String> ctor = classRef.constructor().taking(String.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "(String fileName)", ctor.isPublic());
        String v = "error caused by code new Printer( \"src/textfile.txt\" );\n";
        ctor.withNiceError(v).invoke("textfile.txt");
    }

    public Object luo(String t) throws Throwable {
        return classRef.constructor().taking(String.class).invoke(t);
    }

    @Test
    public void hasMethodPrintLinesWhichContain() throws Throwable {

        Object o = luo("textfile.txt");

        assertTrue("Create method public void printLinesWhichContain(String word) for class Printer",
                classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).isPublic());

        String k = "Printer t = new Printer( \"src/textfile.txt\" );\n"
                + "t.printLinesWhichContain(\"vanha\");";

        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).withNiceError(k).invoke("vanha");
    }

    @Test
    public void printLinesWhichContainVanha() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("textfile.txt");

        String k = "Printer t = new Printer( \"src/textfile.txt\" );\n"
                + "t.printLinesWhichContain(\"vanha\");\n";

        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).withNiceError(k).invoke("vanha");

        String out = io.getOutput();

        assertFalse("Should have printed 2 lines with code" + k + "you didn't print anything!" + out, out.isEmpty());

        assertEquals("Should have printed 2 lines with code" + k + "print output was\n" + out, 2, out.split("\n").length);

        assertTrue("First printed line should have been\n"
                + "\"Siinä vanha Väinämöinen\"\n with code" + k + "print output was\n" + out, out.split("\n")[0].startsWith("Siin"));
        assertTrue("Second printed line should have been\n"
                + "\"Sanoi vanha Väinämöinen\"\n with code" + k + "print output was\n" + out, out.split("\n")[1].startsWith("Sanoi"));
    }

    @Test
    public void printLinesWhichContainTuli() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("textfile.txt");

        String k = "Printer t = new Printer( \"src/textfile.txt\" );\n"
                + "t.printLinesWhichContain(\"tuli\");\n";

        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).withNiceError(k).invoke("tuli");

        String out = io.getOutput();

        assertEquals("Should have printed 1 line with code" + k + "print output was\n" + out, 1, out.split("\n").length);
        assertTrue("Printed line should have been\n"
                + "\"Niin tuli kevätkäkönen\"\n with code" + k + "print output was\n" + out, out.split("\n")[0].startsWith("Niin tuli"));

    }

    @Test
    public void printAllLines() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("textfile.txt");

        String k = "Printer t = new Printer( \"src/textfile.txt\" );\n"
                + "t.printLinesWhichContain(\"\");\n";

        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).withNiceError(k).invoke("");

        String out = io.getOutput();

        assertEquals("Should have printed 7 lines with code" + k + "print output was\n" + out, 7, out.split("\n").length);
        assertTrue("First printed line should have been\n"
                + "\"Siinä vanha Väinämöinen\"\n with code" + k + "print output was\n" + out, out.split("\n")[0].startsWith("Siin"));
        assertTrue("Last printed line should have been\n"
                + "\"Sanoi vanha Väinämöinen\"\n with code" + k + "print output was\n" + out, out.split("\n")[6].startsWith("Sanoi"));
    }

    @Test
    public void extraTest() throws Throwable {
        MockInOut io = new MockInOut("");

        File file = File.createTempFile("temp", "txt");

        FileWriter writer = new FileWriter(file);
        writer.append("word 1\nword 2\nkoe\n");
        writer.flush();
        writer.close();

        Object o = luo(file.getAbsolutePath());


        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).invoke("word");

        String out = io.getOutput();

        assertEquals("Called t.printLinesWhichContain(\"word\"); when content of the file was\n"
                + "----\n"
                + "word 1\nword 2\nkoe\n"
                + "----\n"
                + "Should have printed 2 lines. Print output was\n" + out, 2, out.split("\n").length);

        assertTrue("Called t.printLinesWhichContain(\"word\"); when content of the file was\n"
                + "----\n"
                + "word 1\nword 2\nkoe\n"
                + "----\n"
                + "First printed line should have been \"word1\n. \n"
                + "Print output was\n" + out, out.split("\n")[0].contains("word 1"));
    }

    @Test
    public void testPrinterMultipleCalls() throws Throwable {
        MockInOut io = new MockInOut("");

        File file = File.createTempFile("temp", "txt");

        FileWriter writer = new FileWriter(file);
        writer.append("word 1\nword 2\nkoe\n");
        writer.flush();
        writer.close();

        Object o = luo(file.getAbsolutePath());


        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).invoke("word");
        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).invoke("koe");

        String out = io.getOutput();

        assertEquals("Called t.printLinesWhichContain(\"word\");\nt.printLinesWhichContain(\"koe\"); when content of the file was\n"
                + "----\n"
                + "word 1\nword 2\nkoe\n"
                + "----\n"
                + "Should have printed 3 lines. Print output was\n" + out, 3, out.split("\n").length);

        assertTrue("Called t.printLinesWhichContain(\"word\");\nt.printLinesWhichContain(\"koe\"); when content of the file was\n"
                + "----\n"
                + "word 1\nword 2\nkoe\n"
                + "----\n"
                + "First printed line should have been \"word1\n. \n"
                + "Print output was\n" + out, out.split("\n")[0].contains("word 1"));

        assertTrue("Called t.printLinesWhichContain(\"word\");\nt.printLinesWhichContain(\"koe\"); when content of the file was\n"
                + "----\n"
                + "word 1\nword 2\nkoe\n"
                + "----\n"
                + "First printed line should have been \"koe\n. \n"
                + "Print output was\n" + out, out.split("\n")[2].contains("koe"));
    }

    @Test
    public void kalevalaOlut() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("kalevala.txt");

        String k = "Printer t = new Printer( \"src/kalevala.txt\" );\n"
                + "t.printLinesWhichContain(\"olut\");\n";

        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).withNiceError(k).invoke("olut");

        String out = io.getOutput();

        assertEquals("Should have printed 41 lines with code" + k + "print output was\n" + out, 41, out.split("\n").length);
        assertTrue("First printed line should have been\n"
                + "\"Kun ei tuotane olutta\"\n with code" + k + "print output was\n" + out, out.split("\n")[0].contains("Kun ei tuotane olutta"));
    }

    @Test
    public void kalevalaMaito() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("kalevala.txt");

        String k = "Printer t = new Printer( \"src/kalevala.txt\" );\n"
                + "t.printLinesWhichContain(\"maito\");\n";

        classRef.method(o, "printLinesWhichContain").returningVoid().taking(String.class).withNiceError(k).invoke("maito");

        String out = io.getOutput();

        assertEquals("Should have printed 24 lines with code" + k + "print output was\n" + out, 24, out.split("\n").length);
        assertTrue("First printed line should have been\n"
                + "\"maitopartana pahaisna\"\n with code" + k + "print output was\n" + out, out.split("\n")[0].contains("maitopartana pahaisna"));
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
