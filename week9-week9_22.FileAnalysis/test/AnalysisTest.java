
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef1;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class AnalysisTest {

    String luokanNimi = "Analysis";
    Class analysisClass;
    String klassName = "file.Analysis";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setUp() {

        classRef = Reflex.reflect(klassName);
        try {
            analysisClass = ReflectionUtils.findClass("file." + luokanNimi);
        } catch (Throwable t) {
            fail("Have you created class " + luokanNimi + " inside the package file?");
        }
    }

    @Test
    @Points("22.1")
    public void classAnalysis() {
        classRef = Reflex.reflect(klassName);

        assertTrue("Clas " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + s(klassName) + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("22.1")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 10, "");
    }

    @Test
    @Points("22.1")
    public void hasConstructorAnalysis() throws Throwable {
        MethodRef1<Object, Object, File> ctor = classRef.constructor().taking(File.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "(File file)", ctor.isPublic());
        String v = "error caused by code new Analysis( new File(\"test/testfile.txt\") );\n";
        ctor.withNiceError(v).invoke(new File("testfile.txt"));
    }

    public Object luo(File file) throws Throwable {
        return classRef.constructor().taking(File.class).invoke(file);
    }

    @Test
    @Points("22.1")
    public void hasMethodLines() throws Throwable {
        Object o = luo(new File("testfile.txt"));


        assertTrue("Create method public int lines() for class Analysis", classRef.method(o, "lines").returning(int.class).takingNoParams().isPublic());

        String k = "Analysis a = new Analysis( new File(\"test/testfile.txt\") );\n"
                + "a.lines();";

        assertEquals(k, 3, (int) classRef.method(o, "lines").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    @Test
    @Points("22.1")
    public void linesCanBeCalledManyTimes() throws Throwable {
        Object o = luo(new File("testfile.txt"));


        assertTrue("Create method public int lines() for class Analysis", classRef.method(o, "lines").returning(int.class).takingNoParams().isPublic());

        String k = "Analysis a = new Analysis( new File(\"test/testfile.txt\") );\n"
                + "a.lines();\n";

        assertEquals(k, 3, (int) classRef.method(o, "lines").returning(int.class).takingNoParams().withNiceError(k).invoke());

        k += "a.lines();\n";

        assertEquals(k, 3, (int) classRef.method(o, "lines").returning(int.class).takingNoParams().withNiceError(k).invoke());

        k += "a.lines();\n";

        assertEquals(k, 3, (int) classRef.method(o, "lines").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    private void testaaRivimaaraMetodia(File file, int lines, String tdst) {
        int rivit = -1;
        Object riviObj = luoAnalysisOlio(file);

        Method linesMethod = ReflectionUtils.requireMethod(analysisClass, "lines");

        try {
            rivit = ReflectionUtils.invokeMethod(int.class, linesMethod, riviObj);
        } catch (Throwable t) {
            fail("Are you catching all possible exceptions correctly?\n"
                    + "There was an error when counting lines: " + t);
        }
        assertEquals("When file had " + lines + " lines, your program found " + rivit + " lines.\n"
                + "content of the file:\n"
                + "------\n"
                + tdst
                + "\n------\n",
                lines, rivit);

    }

    private void testaaMerkkejaMetodia(File file, int characters, String tdst) {
        int merkit = -1;
        Object riviObj = luoAnalysisOlio(file);

        Method charactersMethod = ReflectionUtils.requireMethod(analysisClass, "characters");

        try {
            merkit = ReflectionUtils.invokeMethod(int.class, charactersMethod, riviObj);
        } catch (Throwable t) {
            fail("Are you catching all possible exceptions correctly?\n"
                    + "There was an error when counting characters: " + t);
        }
        assertEquals("When file had " + characters + " characters, your program found " + merkit + " characters. "
                + "Are you also counting line breaks as characters? \n"
                + "content of the file:\n"
                + "------\n"
                + tdst
                + "\n-------\n"
                + "", characters, merkit);
    }

    private Object luoAnalysisOlio(File file) {
        try {
            Constructor riviConstruktor = ReflectionUtils.requireConstructor(analysisClass, File.class);
            return ReflectionUtils.invokeConstructor(riviConstruktor, file);

        } catch (Throwable e) {
        }

        return null;
    }

    @Test
    @Points("22.1")
    public void oneLine() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");

            n = new FileWriter(eka);
            n.append("sana1");
            n.flush();
            n.close();
            testaaRivimaaraMetodia(eka, 1, "sana1");
        } catch (IOException ex) {
            fail("Writing to a test file failed! Additional information: " + ex);
        }
    }

    @Test
    @Points("22.1")
    public void fiveLinesTest() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("sana1\nsana2\nsana3\nsana4\nsana5\n");
            n.flush();
            n.close();
            testaaRivimaaraMetodia(eka, 5, "sana1\nsana2\nsana3\nsana4\nsana5");
        } catch (IOException ex) {
            fail("Writing to a test file failed! Additional information: " + ex);
        }
    }

    @Test
    @Points("22.1")
    public void tenLinesTest() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("sana1\naa\nbb\ncc\ndd  dd\nee e\nfff\nggg\nh\nsana1\n");
            n.flush();
            n.close();
            testaaRivimaaraMetodia(eka, 10, "sana1\naa\nbb\ncc\ndd  dd\nee e\nfff\nggg\nh\nsana1");
        } catch (IOException ex) {
            fail("Writing to a test file failed! Additional information: " + ex);
        }
    }

    @Test
    @Points("22.2")
    public void hasMethodCharacters() throws Throwable {
        Object o = luo(new File("test/testfile.txt"));

        assertTrue("Create method public int characters() for class Analysis", classRef.method(o, "characters").returning(int.class).takingNoParams().isPublic());

        String k = "Analysis a = new Analysis( new File(\"test/testfile.txt\") );\n"
                + "a.characters();\n";

        int tulos = classRef.method(o, "characters").returning(int.class).takingNoParams().withNiceError(k).invoke();
        assertEquals(k, 74, (int) classRef.method(o, "characters").returning(int.class).takingNoParams().withNiceError(k).invoke());
    }

    @Test
    @Points("22.2")
    public void fiveCharactersAndLineBreak() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("arska\n");
            n.flush();
            n.close();
            testaaMerkkejaMetodia(eka, 6, "arska");
        } catch (IOException ex) {
            fail("Writing to a test file failed! Additional information: " + ex);
        }
    }

    @Test
    @Points("22.2")
    public void twoCharacterLines() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("a\nb\n");
            n.flush();
            n.close();
            testaaMerkkejaMetodia(eka, 4, "a\nb\n");
        } catch (IOException ex) {
            fail("Writing to a test file failed! Additional information: " + ex);
        }
    }

    @Test
    @Points("22.2")
    public void lineBreakIsCounted() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("arska\nmusta\npekka\n");
            n.flush();
            n.close();
            testaaMerkkejaMetodia(eka, 18, "arska\nmusta\npekka");
        } catch (IOException ex) {
            fail("Writing to a test file failed! Additional information: " + ex);
        }
    }

    @Test
    @Points("22.2")
    public void bothMethodsWork() throws Throwable {
        Object o = luo(new File("test/testfile.txt"));

        assertTrue("Create method public int characters() for class Analysis", classRef.method(o, "characters").returning(int.class).takingNoParams().isPublic());

        String k = "Analysis a = new Analysis( new File(\"test/testfile.txt\") );\n"
                + "a.characters();\n";

        assertEquals(k, 74, (int) classRef.method(o, "characters").returning(int.class).takingNoParams().withNiceError(k).invoke());

        k += "a.lines()";
        assertEquals(k, 3, (int) classRef.method(o, "lines").returning(int.class).takingNoParams().withNiceError(k).invoke());
        k += "a.characters()";
        assertEquals(k, 74, (int) classRef.method(o, "characters").returning(int.class).takingNoParams().withNiceError(k).invoke());
        k += "a.lines()";
        assertEquals(k, 3, (int) classRef.method(o, "lines").returning(int.class).takingNoParams().withNiceError(k).invoke());


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
