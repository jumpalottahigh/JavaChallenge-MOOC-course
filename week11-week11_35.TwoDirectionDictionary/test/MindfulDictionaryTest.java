
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MindfulDictionaryTest {

    Reflex.ClassRef<Object> klass;
    String klassName = "dictionary.MindfulDictionary";

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
        teeTiedosto();
    }

    @Points("35.1")
    @Test
    public void classPublic() {
        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Points("35.1")
    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 10, "");
    }

    @Test
    @Points("35.1")
    public void emptyConstructor() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: public " + klassName + "()", ctor.isPublic());
        String v = "error caused by code new MindfulDictionary();";
        ctor.withNiceError(v).invoke();
    }

    @Test
    @Points("35.1")
    public void addMethod() throws Throwable {
        String metodi = "add";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(String word, String translation) for class " + s(klassName),
                klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).isPublic());

        String v = "\nError caused by code\n"
                + "MindfulDictionary s = new MindfulDictionary();\n"
                + "s.add(\"apina\",\"monkey\");\n";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("apina", "monkey");
    }

    @Test
    @Points("35.1")
    public void translateMethod() throws Throwable {
        String metodi = "translate";

        Object olio = luo();

        assertTrue("Create method public String " + metodi + "(String word) for class " + s(klassName),
                klass.method(olio, metodi)
                .returning(String.class)
                .taking(String.class)
                .isPublic());

        String v = "\nError caused by code \n"
                + "MindfulDictionary s = new MindfulDictionary();\n"
                + "s.translate(\"apina\");\n";

        klass.method(olio, metodi)
                .returning(String.class).taking(String.class).withNiceError(v).invoke("apina");
    }

    @Test
    @Points("35.1")
    public void addAndTranslateWorks() throws Throwable {
        String v = "\n"
                + "MindfulDictionary s = new MindfulDictionary();\n"
                + "s.add(\"apina\", \"monkey\");\n"
                + "s.add(\"tietokone\", \"computer\");\n";

        Object o = luo();
        add(o, "apina", "monkey", v);
        add(o, "tietokone", "computer", v);

        String w = v + "s.translate(\"apina\");\n";
        assertEquals(w, "monkey", translate(o, "apina", w));
        w = v + "s.translate(\"tietokone\");\n";
        assertEquals(w, "computer", translate(o, "tietokone", w));
        w = v + "s.translate(\"monkey\");\n";
        assertEquals(w, "apina", translate(o, "monkey", w));
        w = v + "s.translate(\"computer\");\n";
        assertEquals(w, "tietokone", translate(o, "computer", w));

        w = v + "s.translate(\"ihminen\");\n";
        assertEquals(w, null, translate(o, "ihminen", w));

        add(o, "apina", "apfe", v);
        w = v + "s.add(\"apina\", \"apfe\");\n "
                + "s.translate(\"apina\");\n";
        assertEquals(w, "monkey", translate(o, "apina", w));
    }

    /*
     *
     */
    @Test
    @Points("35.2")
    public void removeMethod() throws Throwable {
        String metodi = "remove";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(String word) for class " + s(klassName),
                klass.method(olio, metodi)
                .returningVoid()
                .taking(String.class)
                .isPublic());

        String v = "\nError caused by code \n"
                + "MindfulDictionary s = new MindfulDictionary();\n"
                + "s.add(\"apina\", \"monkey\");\n"
                + "s.remove(\"apina\");\n";

        add(olio, "apina", "monkey", v);

        klass.method(olio, metodi)
                .returningVoid().taking(String.class).withNiceError(v).invoke("apina");
    }

    @Test
    @Points("35.2")
    public void removeWorks() throws Throwable {
        String v = "\n"
                + "MindfulDictionary s = new MindfulDictionary();\n"
                + "s.add(\"apina\", \"monkey\");\n"
                + "s.add(\"tietokone\", \"computer\");\n"
                + "s.remove(\"apina\");\n";

        Object o = luo();
        add(o, "apina", "monkey", v);
        add(o, "tietokone", "computer", v);

        remove(o, "apina", v);

        String w = v + "s.translate(\"apina\");\n";
        assertEquals(w, null, translate(o, "apina", w));
        w = v + "s.translate(\"tietokone\");\n";
        assertEquals(w, "computer", translate(o, "tietokone", w));
        w = v + "s.translate(\"monkey\");\n";
        assertEquals(w, null, translate(o, "monkey", w));
        w = v + "s.translate(\"computer\");\n";
        assertEquals(w, "tietokone", translate(o, "computer", w));

        w = v + "s.translate(\"ihminen\");\n";
        assertEquals(w, null, translate(o, "ihminen", w));

        add(o, "apina", "apfe", v);
        w = v + "s.add(\"apina\", \"apfe\");\n "
                + "s.translate(\"apina\n);\n";
        assertEquals(w, "apfe", translate(o, "apina", w));
    }

    /*
     *
     */
    @Test
    @Points("35.3")
    public void constructorWithParameters() throws Throwable {
        Reflex.MethodRef1<Object, Object, String> ctor = klass.constructor().taking(String.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: public " + s(klassName) + "(String file)", ctor.isPublic());
        String v = "error caused by code new MindfulDictionary(\"src/words.txt\");";
        ctor.withNiceError(v).invoke("/test/words.txt");
    }

    @Test
    @Points("35.3")
    public void methodLoad() throws Throwable {
        String metodi = "load";

        String v = "MindfulDictionary s = new MindfulDictionary(\"src/words.txt\");\n";

        Object olio = luo("test/words.txt", v);

        assertTrue("Create method public boolean " + metodi
                + "() for class " + s(klassName),
                klass.method(olio, metodi)
                .returning(boolean.class)
                .takingNoParams()
                .isPublic());

        Class[] e = klass.method(olio, metodi)
                .returning(boolean.class)
                .takingNoParams().getMethod().getExceptionTypes();

        assertFalse("Class " + s(klassName) + "'s method public boolean " + metodi
                + "() mustn't throw an exception!\n"
                + "",e.length>0);

        v = ""
                + "MindfulDictionary s = new MindfulDictionary(\"src/words.txt\");\n"
                + "s.load();\n";

        assertEquals(v, true, (boolean) klass.method(olio, metodi)
                .returning(boolean.class).takingNoParams()
                .withNiceError("\nError caused by code" + v).invoke());
    }

    @Test
    @Points("35.3")
    public void loadedDictionaryWorks() throws Throwable {

        String v = ""
                + "MindfulDictionary s = new MindfulDictionary(\"src/words.txt\");\n"
                + "s.load();\n";

        Object o = luo("test/words.txt", v);

        assertEquals(v, true, load(o, v));

        String w = v + "s.translate(\"apina\");\n";
        assertEquals(w, "monkey", translate(o, "apina", w));
        w = v + "s.translate(\"alla oleva\");\n";
        assertEquals(w, "below", translate(o, "alla oleva", w));
        w = v + "s.translate(\"monkey\");\n";
        assertEquals(w, "apina", translate(o, "monkey", w));
        w = v + "s.translate(\"below\");\n";
        assertEquals(w, "alla oleva", translate(o, "below", w));
        w = v + "s.translate(\"ihminen\");\n";
        assertEquals(w, null, translate(o, "ihminen", w));

        v += "s.add(\"ohjelmointi\", \"programming\");\n";

        add(o, "ohjelmointi", "programming", v);

        w = v + "s.translate(\"ohjelmointi\");\n";
        assertEquals(v, "programming", translate(o, "ohjelmointi", v));
        w = v + "s.translate(\"programming\");\n";
        assertEquals(v, "ohjelmointi", translate(o, "programming", v));

        v += "s.remove(\"olut\")\n";

        remove(o, "olut", v);
        w = v + "s.translate(\"below\");\n";
        assertEquals(v, "alla oleva", translate(o, "below", v));
        w = v + "s.translate(\"beer\");\n";
        assertEquals(v, null, translate(o, "beer", v));
        w = v + "s.translate(\"olut\");\n";
        assertEquals(v, null, translate(o, "olut", v));
    }

    @Test
    @Points("35.3")
    public void noLoadingUntilInMethod() throws Throwable {

        String v = "Notice that dictionary isn't loaded yet and words shouldn't be found!\n"
                + "MindfulDictionary s = new MindfulDictionary(\"src/words.txt\");\n"
                ;

        Object o = luo("test/words.txt", v);

        String w = v + "s.translate(\"apina\");\n";
        assertEquals(w, null, translate(o, "apina", w));
        w = v + "s.translate(\"alla oleva\");\n";
        assertEquals(w, null, translate(o, "alla oleva", w));
        w = v + "s.translate(\"monkey\");\n";
        assertEquals(w, null, translate(o, "monkey", w));
        w = v + "s.translate(\"below\");\n";
        assertEquals(w, null, translate(o, "below", w));
        w = v + "s.translate(\"ihminen\");\n";
        assertEquals(w, null, translate(o, "ihminen", w));
    }

    @Points("35.3")
    public void nonExistentDictionaryFile() throws Throwable {
        String v = ""
                + "MindfulDictionary s = new MindfulDictionary(\"src/fileJotaEiOleOlemassa.txt\");\n"
                + "s.load();\n";

        Object o = luo("test/fileJotaEiOleOlemassa", v);

        assertEquals(v, true, load(o, v));

        String w = v + "s.translate(\"apina\");\n";
        assertEquals(w, null, translate(o, "apina", w));
        w = v + "s.translate(\"alla oleva\");\n";
        assertEquals(w, null, translate(o, "alla oleva", w));
        w = v + "s.translate(\"monkey\");\n";
        assertEquals(w, null, translate(o, "monkey", w));
        w = v + "s.translate(\"below\");\n";
        assertEquals(w, null, translate(o, "below", w));
        w = v + "s.translate(\"ihminen\");\n";
        assertEquals(w, null, translate(o, "ihminen", w));

        v += "s.add(\"apina\", \"monkey\");\n";

        add(o, "apina", "monkey", v);
        add(o, "tietokone", "computer", v);

        w = v + "s.translate(\"apina\");\n";
        assertEquals(w, "monkey", translate(o, "apina", w));

    }

    /*
     *
     */
    @Test
    @Points("35.4")
    public void methodSave() throws Throwable {
        String metodi = "save";

        String v = "MindfulDictionary s = new MindfulDictionary(\"src/words.txt\");\n";

        Object olio = luo("test/words.txt", v);

        assertTrue("Create method public boolean " + metodi
                + "() for class " + s(klassName),
                klass.method(olio, metodi)
                .returning(boolean.class)
                .takingNoParams()
                .isPublic());

        Class[] e = klass.method(olio, metodi)
                .returning(boolean.class)
                .takingNoParams().getMethod().getExceptionTypes();

        assertFalse("Class " + s(klassName) + "'s method public boolean " + metodi
                + "() mustn't throw an exception!\n"
                + "",e.length>0);

        v = ""
                + "MindfulDictionary s = new MindfulDictionary(\"src/words.txt\");\n"
                + "s.load();\n"
                + "s.save();\n";

        assertEquals(v, true, (boolean) klass.method(olio, metodi)
                .returning(boolean.class).takingNoParams()
                .withNiceError("\nError caused by code" + v).invoke());
    }

    @Test
    @Points("35.4")
    public void dictionarySavedIfFileDoesntExistYet() throws Throwable {
        String file = teeNimi();

        String v = ""
                + "MindfulDictionary s = new MindfulDictionary(\"" + file + "\");\n"
                + "s.add(\"tietokone\", \"computer\");\n"
                + "s.save();\n";

        Object o = luo(file, v);
        add(o, "tietokone", "computer", v);
        assertEquals(v, true, talenna(o, v));

        File f = new File(file);
        assertTrue("Next code should save dictionary into file "+file+"\n"
                + v+"\n"
                + "file wasn't created!",f.exists() && f.canRead());

        List<String> sisalto = lue(file);

        assertEquals("With code\n" + v + " saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "number of lines wasn't correct", 1, sisalto.size());

        String rivi = sisalto.get(0).trim();

        assertTrue("With code\n" + v + " saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "content wasn't correct", rivi.equals("tietokone:computer") || rivi.equals("computer:tietokone"));
    }

    @Test
    @Points("35.4")
    public void existingDictionaryFileContent() throws Throwable {
        String file = "test/words.txt";

        String v = ""
                + "MindfulDictionary s = new MindfulDictionary(\"" + file + "\");\n"
                + "s.load();\n"
                + "s.translate(\"olut\");\n"
                + "s.save();\n";

        teeTiedosto(file);

        Object o = luo(file, v);
        load(o, v);

        translate(o, "olut", v);

        assertEquals(v, true, talenna(o, v));
        List<String> sisalto = lue(file);

        assertEquals("With code\n" + v + "saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "number of lines wasn't correct", 3, sisalto.size());

        assertTrue("With code\n" + v + "saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "content wasn't correct because olut --> beer wasn't found", loytyy(sisalto, "olut", "beer"));

        assertTrue("With code\n" + v + "saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "content wasn't correct because apina --> monkey wasn't found", loytyy(sisalto, "apina", "monkey"));

        assertTrue("With code\n" + v + "saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "content wasn't correct because alla oleva --> below wasn't found", loytyy(sisalto, "alla oleva", "below"));
    }

    @Test
    @Points("35.4")
    public void existingDictionaryFileChangesAreSaved() throws Throwable {
        String file = "test/words.txt";

        String v = ""
                + "MindfulDictionary s = new MindfulDictionary(\"" + file + "\");\n"
                + "s.load();\n"
                + "s.remove(\"below\");\n"
                + "s.add(\"tieokone\", \"computer\");\n"
                + "s.save();\n";

         teeTiedosto(file);

        Object o = luo(file, v);

        load(o, v);

        remove(o, "below", v);
        add(o, "tietokone", "computer", v);

        assertEquals(v, true, talenna(o, v));
        List<String> sisalto = lue(file);

        assertEquals("With code\n" + v + "saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "number of lines wasn't correct", 3, sisalto.size());

        assertTrue("With code\n" + v + "saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "content wasn't correct because olut --> beer wasn't found", loytyy(sisalto, "olut", "beer"));

        assertTrue("With code\n" + v + "saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "content wasn't correct because apina --> monkey wasn't found", loytyy(sisalto, "apina", "monkey"));

        assertTrue("With code\n" + v + "saved into file\n"
                + "--\n" + flatten(sisalto) + "--\n"
                + "content wasn't correct because tietokone --> computer wasn't found", loytyy(sisalto, "tietokone", "computer"));
    }

    /*
     *
     */
    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    public Object luo(String t, String v) throws Throwable {
        Reflex.MethodRef1<Object, Object, String> ctor = klass.constructor().taking(String.class).withNiceError();
        return ctor.withNiceError(v).invoke(t);
    }

    private void add(Object o, String s, String w, String v) throws Throwable {
        klass.method(o, "add")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke(s, w);
    }

    private String translate(Object o, String s, String v) throws Throwable {
        return klass.method(o, "translate")
                .returning(String.class).taking(String.class).withNiceError(v).invoke(s);
    }

    private boolean load(Object o, String v) throws Throwable {
        return klass.method(o, "load")
                .returning(boolean.class).takingNoParams().withNiceError(v).invoke();
    }

    private boolean talenna(Object o, String v) throws Throwable {
        return klass.method(o, "save")
                .returning(boolean.class).takingNoParams().withNiceError(v).invoke();
    }

    private void remove(Object o, String s, String v) throws Throwable {
        klass.method(o, "remove")
                .returningVoid().taking(String.class).withNiceError(v).invoke(s);
    }

    /*
     *
     */
    private String s(String klassName) {
        return klassName.substring(klassName.lastIndexOf(".") + 1);
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("you do not need \"static variables\", remove from class " + s(klassName) + " the following variable: " + kentta(field.toString(), s(klassName)), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All isntance variables should be private but class " + s(klassName) + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
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

    private void teeTiedosto() {
        teeTiedosto("test/words.txt");
    }

    private void teeTiedosto(String file) {

        try {
            FileWriter kirjoittaja = new FileWriter(file);
            kirjoittaja.write("apina:monkey\n");
            kirjoittaja.write("alla oleva:below\n");
            kirjoittaja.write("olut:beer\n");
            kirjoittaja.close();
        } catch (Exception e) {
            fail("serious problem, contact lecturer");
        }
    }

    private List<String> lue(String file) throws FileNotFoundException {
        Scanner s = new Scanner(new File(file));
        ArrayList<String> rivit = new ArrayList<String>();

        while (s.hasNextLine()) {
            rivit.add(s.nextLine());
        }
        return rivit;
    }

    private String teeNimi() {
        Random arpa = new Random();
        int arvottu = arpa.nextInt(100000);
        return "test/tmp/tmp" + arvottu + ".txt";
    }

    private String flatten(List<String> s) {
        String t = "";
        for (String string : s) {
            t += string + "\n";
        }
        return t;
    }

    private boolean loytyy(List<String> lista, String s, String w) {
        for (String rivi : lista) {
            if (rivi.equals(s + ":" + w)) {
                return true;
            }
            if (rivi.equals(w + ":" + s)) {
                return true;
            }
        }

        return false;
    }
}
