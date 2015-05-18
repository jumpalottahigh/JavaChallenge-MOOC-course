
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.File;
import java.util.Scanner;
import reader.criteria.Criterion;
import reader.criteria.ContainsWord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CriterionTest {

    Reflex.ClassRef<Object> classRef;

    @Points("47.1")
    @Test
    public void allLinesExists() {
        String klassname = "reader.criteria.AllLines";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create class " + post(klassname) + " inside the package " + pre(klassname), classRef.isPublic());
    }

    @Points("47.1")
    @Test
    public void allLinesConstructor() throws Throwable {
        String klassname = "reader.criteria.AllLines";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create constructor public " + post(klassname) + "() for class " + post(klassname),
                classRef.constructor().takingNoParams().isPublic());

        String v = "error caused by code new " + post(klassname) + "();";
        classRef.constructor().takingNoParams().withNiceError(v).invoke();
    }

    private Criterion allLines() throws Throwable {
        String klassname = "reader.criteria.AllLines";
        classRef = Reflex.reflect(klassname);
        return (Criterion) classRef.constructor().takingNoParams().invoke();
    }

    @Points("47.1")
    @Test
    public void allLinesIsCriterion() {
        isCriterion("reader.criteria.AllLines");
    }

    @Points("47.1")
    @Test
    public void allLinesWorks() throws Throwable {
        String[][] sanat = {
            {"testi", "t"},
            {"Notice that you can combine criteria as you like.", "t"},
            {"eins dwei drei", "t"},
            {"Each and every day, I have less and less hope for the 2013-2014 season.", "t"},
            {"Talvivaara puhutti Ruotsin kaivosmielenosoituksessa", "t"},
            {"Kotimainen hunaja uhkaa loppua ennen kevättä", "t"},
            {"", "t"}
        };

        Criterion e = allLines();

        testaa("reader.criteria.AllLines", e, "Criterion criterion = new AllLines();", sanat);
    }

    /*
     *
     */
    @Points("47.2")
    @Test
    public void endsWithQuestionOrExclamationMarkExists() {
        String klassname = "reader.criteria.EndsWithQuestionOrExclamationMark";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create class " + post(klassname) + " inside the package " + pre(klassname), classRef.isPublic());
    }

    @Points("47.2")
    @Test
    public void endsWithQuestionOrExclamationMarkConstructor() throws Throwable {
        String klassname = "reader.criteria.EndsWithQuestionOrExclamationMark";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create constructor public " + post(klassname) + "() for class " + post(klassname),
                classRef.constructor().takingNoParams().isPublic());

        String v = "error caused by code new " + post(klassname) + "();";
        classRef.constructor().takingNoParams().withNiceError(v).invoke();
    }

    private Criterion endsWithQuestionOrExclamationMark() throws Throwable {
        String klassname = "reader.criteria.EndsWithQuestionOrExclamationMark";
        classRef = Reflex.reflect(klassname);
        return (Criterion) classRef.constructor().takingNoParams().invoke();
    }

    @Points("47.2")
    @Test
    public void endsWithQuestionOrExclamationMarkIsCriterion() {
        isCriterion("reader.criteria.EndsWithQuestionOrExclamationMark");
    }

    @Points("47.2")
    @Test
    public void endsWithQuestionOrExclamationMarkWorks() throws Throwable {
        String[][] sanat = {
            {"testi", "f"},
            {"testi!", "t"},
            {"testi?", "t"},
            {"Notice that you can combine criteria as you like?", "t"},
            {"eins dwei drei!", "t"},
            {"Each and every day, I have less and less hope for the 2013-2014 season!", "t"},
            {"Talvivaara puhutti Ruotsin kaivosmielenosoituksessa!", "t"},
            {"Kotimainen hunaja uhkaa loppua ennen kevättä?", "t"},
            {"Notice that you can combine criteria as you like.", "f"},
            {"eins dwei drei!a", "f"},
            {"Each and every day! I have less and less hope for the 2013-2014 season.", "f"},
            {"Talvivaara puhutti Ruotsin kaivosmielenosoituksessa", "f"},
            {"??!?!?!?!?Kotimainen hunaja uhkaa loppua ennen kevättä", "f"},
            {"!", "t"},
            {"", "f"}
        };

        Criterion e = endsWithQuestionOrExclamationMark();

        testaa("reader.criteria.EndsWithQuestionOrExclamationMark", e, "Criterion criterion = new EndsWithQuestionOrExclamationMark();", sanat);
    }
    /*
     *
     */

    @Points("47.3")
    @Test
    public void lengthAtLeastExists() {
        String klassname = "reader.criteria.LengthAtLeast";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create class " + post(klassname) + " inside the package " + pre(klassname), classRef.isPublic());
    }

    @Points("47.3")
    @Test
    public void lengthAtLeastConstructor() throws Throwable {
        String klassname = "reader.criteria.LengthAtLeast";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create constructor " + post(klassname) + "(int length) for class " + post(klassname),
                classRef.constructor().taking(int.class).isPublic());

        String v = "error caused by code new " + post(klassname) + "(10);";
        classRef.constructor().taking(int.class).withNiceError(v).invoke(10);
    }

    private Criterion lengthAtLeast(int p) throws Throwable {
        String klassname = "reader.criteria.LengthAtLeast";
        classRef = Reflex.reflect(klassname);
        return (Criterion) classRef.constructor().taking(int.class).invoke(p);
    }

    @Points("47.3")
    @Test
    public void lengthAtLeastIsCriterion() {
        isCriterion("reader.criteria.LengthAtLeast");
    }

    @Points("47.3")
    @Test
    public void lengthAtLeastWorks1() throws Throwable {
        String[][] sanat = {
            {"testi", "f"},
            {"Notice that you can combine criteria as you like.", "t"},
            {"eins dwei drei", "f"},
            {"Each and every day, I have less and less hope for the 2013-2014 season.", "t"},
            {"Talvivaara puhutti Ruotsin kaivosmielenosoituksessa", "t"},
            {"Kotimainen hunaja uhkaa loppua ennen kevättä", "t"},
            {"", "f"}
        };

        Criterion e = lengthAtLeast(20);

        testaa("reader.criteria.LengthAtLeast", e, "Criterion criterion = new LengthAtLeast(20);", sanat);
    }

    @Points("47.3")
    @Test
    public void lengthAtLeastWorks2() throws Throwable {
        String[][] sanat = {
            {"testi", "f"},
            {"Notice that you can combine criteria as you like.", "t"},
            {"eins dwei drei", "f"},
            {"Each and every day, I have less and less hope for the 2013-2014 season.", "t"},
            {"Talvivaara puhutti Ruotsin kaivosmielenosoituksessa", "t"},
            {"Kotimainen hunaja uhkaa loppua ennen kevättä", "f"},
            {"", "f"}
        };

        Criterion e = lengthAtLeast(47);

        testaa("reader.criteria.LengthAtLeast", e, "Criterion criterion = new LengthAtLeast(47);", sanat);
    }

    @Points("47.3")
    @Test
    public void lengthAtLeastWorks3() throws Throwable {
        for (int i = 5; i < 30; i++) {
            String s1 = luoSana(i - 1);
            String s2 = luoSana(i);
            String[][] sanat = {
                {s1, "f"},
                {s2, "t"}
            };

            Criterion e = lengthAtLeast(i);

            testaa("reader.criteria.LengthAtLeast", e, "Criterion criterion = new LengthAtLeast(" + i + ");", sanat);
        }

    }

    /*
     *
     */
    @Points("47.4")
    @Test
    public void bothExists() {
        String klassname = "reader.criteria.Both";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create class " + post(klassname) + " inside the package " + pre(klassname), classRef.isPublic());
    }

    @Points("47.4")
    @Test
    public void bothConstructor() throws Throwable {
        String klassname = "reader.criteria.Both";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create constructor " + post(klassname) + "(Criterion criterion1, Criterion criterion2) for class " + post(klassname),
                classRef.constructor().taking(Criterion.class, Criterion.class).isPublic());

        Criterion e1 = new ContainsWord("maito");
        Criterion e2 = new ContainsWord("vesi");

        String v = "error caused by code new " + post(klassname) + "(new ContainsWord(\"maito\"),"
                + "new ContainsWord(\"vesi\"));";
        classRef.constructor().taking(Criterion.class, Criterion.class).withNiceError(v).invoke(e1, e2);
    }

    private Criterion both(Criterion e1, Criterion e2) throws Throwable {
        String klassname = "reader.criteria.Both";
        classRef = Reflex.reflect(klassname);
        return (Criterion) classRef.constructor().taking(Criterion.class, Criterion.class).invoke(e1, e2);
    }

    @Points("47.4")
    @Test
    public void bothIsCriterion() {
        isCriterion("reader.criteria.Both");
    }

    @Points("47.4")
    @Test
    public void bothWorks1() throws Throwable {
        String[][] sanat = {
            {"testi", "f"},
            {"vesi vanhin voitehista, maito myös hyvä", "t"},
            {"vesi vanhin voitehista", "f"},
            {"maito myös hyvä", "f"},
            {"maitopoika ja vesimies", "t"},
            {"juo maitoa ja vettä", "f"},
            {"olutta sen pitää olla!", "f"},
            {"", "f"}
        };

        Criterion e1 = new ContainsWord("maito");
        Criterion e2 = new ContainsWord("vesi");
        Criterion e = both(e1, e2);

        testaa("reader.criteria.Both", e,
                "Criterion criterion = new Both("
                + "new ContainsWord(\"maito\"), "
                + "new ContainsWord(\"vesi\") );", sanat);
    }

    @Points("47.4")
    @Test
    public void bothWorks2() throws Throwable {
        String[][] sanat = {
            {"testi", "f"},
            {"java ja ruby ovat ohjelmointikieliä", "t"},
            {"java kehitettiin 90-luvulla", "f"},
            {"ruby kehitettiin 2000-luvulla", "f"},
            {"java on syntaksiltaan c++:n kaltainen. ruby on smalltalkihmisten mieleen", "t"},
            {"hyvä meininki", "f"},
            {"e = mc^2", "f"},
            {"", "f"}
        };

        Criterion e1 = new ContainsWord("java");
        Criterion e2 = new ContainsWord("ruby");
        Criterion e = both(e1, e2);

        testaa("reader.criteria.Both", e,
                "Criterion criterion = new Both("
                + "new ContainsWord(\"java\n), "
                + "new ContainsWord(\"ruby\n) );", sanat);
    }

    /*
     *
     */
    @Points("47.5")
    @Test
    public void notExists() {
        String klassname = "reader.criteria.Not";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create class " + post(klassname) + " inside the package " + pre(klassname), classRef.isPublic());
    }

    @Points("47.5")
    @Test
    public void notConstructor() throws Throwable {
        String klassname = "reader.criteria.Not";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create constructor " + post(klassname) + "(Criterion criterion) for class " + post(klassname),
                classRef.constructor().taking(Criterion.class).isPublic());

        Criterion e = new ContainsWord("maito");
        String v = "error caused by code new " + post(klassname) + "(new ContainsWord(\"maito\"));";
        classRef.constructor().taking(Criterion.class).withNiceError(v).invoke(e);
    }

    private Criterion not(Criterion e) throws Throwable {
        String klassname = "reader.criteria.Not";
        classRef = Reflex.reflect(klassname);
        return (Criterion) classRef.constructor().taking(Criterion.class).invoke(e);
    }

    @Points("47.5")
    @Test
    public void notIsCriterion() {
        isCriterion("reader.criteria.Not");
    }

    @Points("47.5")
    @Test
    public void notWorks1() throws Throwable {
        String[][] sanat = {
            {"testi", "t"},
            {"java ja ruby ovat ohjelmointikieliä", "f"},
            {"java kehitettiin 90-luvulla", "f"},
            {"ruby kehitettiin 2000-luvulla", "t"},
            {"java on syntaksiltaan c++:n kaltainen. ruby on smalltalkihmisten mieleen", "f"},
            {"hyvä meininki", "t"},
            {"e = mc^2", "t"},
            {"", "t"}
        };

        Criterion e = not(new ContainsWord(("java")));

        testaa("reader.criteria.Not", e, "Criterion criterion = new Not( new ContainsWord(\"java\") );", sanat);
    }

    @Points("47.5")
    @Test
    public void notWorks2() throws Throwable {
        String[][] sanat = {
            {"testi", "t"},
            {"testi!", "f"},
            {"testi?", "f"},
            {"Notice that you can combine criteria as you like?", "f"},
            {"eins dwei drei!", "f"},
            {"Each and every day, I have less and less hope for the 2013-2014 season!", "f"},
            {"Talvivaara puhutti Ruotsin kaivosmielenosoituksessa!", "f"},
            {"Kotimainen hunaja uhkaa loppua ennen kevättä?", "f"},
            {"Notice that you can combine criteria as you like.", "t"},
            {"eins dwei drei!a", "t"},
            {"Each and every day! I have less and less hope for the 2013-2014 season.", "t"},
            {"Talvivaara puhutti Ruotsin kaivosmielenosoituksessa", "t"},
            {"??!?!?!?!?Kotimainen hunaja uhkaa loppua ennen kevättä", "t"},
            {"!", "f"},
            {"", "t"}
        };

        Criterion e = not(endsWithQuestionOrExclamationMark());

        testaa("reader.criteria.Not", e, "Criterion criterion = new Not( new EndsWithQuestionOrExclamationMark() );", sanat);
    }

    /*
     *
     */
    @Points("47.6")
    @Test
    public void atLeastOneExists() {
        String klassname = "reader.criteria.AtLeastOne";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create class " + post(klassname) + " inside the package " + pre(klassname), classRef.isPublic());
    }

    @Points("47.6")
    @Test
    public void atLeastOneConstructor() throws Throwable {
        String klassname = "reader.criteria.AtLeastOne";
        classRef = Reflex.reflect(klassname);
        assertTrue("Create constructor " + post(klassname) + "(Criterion... criteria) for class " + post(klassname),
                classRef.constructor().taking(Criterion[].class).isPublic());

        Criterion e1 = new ContainsWord("maito");
        Criterion e2 = new ContainsWord("vesi");
        Criterion e3 = new ContainsWord("kahvi");

        assertEquals("Class AtLeastOne has too many constructors", 1, classRef.cls().getConstructors().length);

        String v = "Does the class " + post(klassname) + " have constructor public " + post(klassname) + "(Criterion... criteria)\n";

        assertTrue(v,vaihtuvaMaaraParametrejaKonstruktorilla());
    }

    private Criterion atLeastOne(Criterion... criteria) throws Throwable {
        String klassname = "reader.criteria.AtLeastOne";
        classRef = Reflex.reflect(klassname);
        return (Criterion) classRef.constructor().taking(Criterion[].class).invoke(criteria);
    }

    @Points("47.6")
    @Test
    public void atLeastOneIsCriterion() {
        isCriterion("reader.criteria.AtLeastOne");
    }

    @Points("47.6")
    @Test
    public void atLeastOneWorks1() throws Throwable {
        String[][] sanat = {
            {"testi", "f"},
            {"vesi vanhin voitehista, maito myös hyvä", "t"},
            {"vesi vanhin voitehista", "t"},
            {"maito myös hyvä", "t"},
            {"maitopoika ja vesimies", "t"},
            {"klara vappen", "f"},
            {"juo maitoa ja vettä", "t"},
            {"olutta sen pitää olla!", "f"},
            {"", "f"}
        };

        Criterion e1 = new ContainsWord("maito");
        Criterion e2 = new ContainsWord("vesi");
        Criterion e = atLeastOne(e1, e2);

        testaa("reader.criteria.AtLeastOne", e,
                "Criterion criterion = new AtLeastOne("
                + "new ContainsWord(\"maito\"), "
                + "new ContainsWord(\"vesi\") );", sanat);
    }

    @Points("47.6")
    @Test
    public void atLeastOneWorks2() throws Throwable {
        String[][] sanat = {
            {"testi", "f"},
            {"java ja ruby ovat ohjelmointikieliä", "t"},
            {"fortran kehitettiin 50-luvulla", "f"},
            {"ruby kehitettiin 2000-luvulla", "t"},
            {"java on syntaksiltaan c++:n kaltainen. ruby on smalltalkihmisten mieleen", "t"},
            {"hyvä meininki", "f"},
            {"Each and every day, I have less and less hope for the 2013-2014 season.", "f"},
            {"Talvivaara puhutti Ruotsin kaivosmielenosoituksessa", "f"},
            {"luokkakaavioilla voidaan kuvata ohjelman rakennetta", "f"},
            {"ei yhtään ohjelmointikieltä mainittu", "f"},
            {"", "f"}
        };

        Criterion e1 = new ContainsWord("ruby");
        Criterion e2 = new ContainsWord("java");
        Criterion e3 = new ContainsWord("c++");
        Criterion e = atLeastOne(e1, e2, e3);

        testaa("reader.criteria.AtLeastOne", e,
                "Criterion criterion = new AtLeastOne("
                + "new ContainsWord(\"java\"), "
                + "new ContainsWord(\"ruby\"),"
                + "new ContainsWord(\"c++\") );", sanat);
    }

    /*
     *
     */

    private String pre(String klassname) {
        int kohta = klassname.lastIndexOf(".");
        return klassname.substring(0, kohta);
    }

    private String post(String klassname) {
        int kohta = klassname.lastIndexOf(".");
        return klassname.substring(kohta + 1);
    }

    private void isCriterion(String klassname) {
        classRef = Reflex.reflect(klassname);
        Class clazz = classRef.cls();

        boolean toteuttaaRajapinnan = false;
        Class criterion = Criterion.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(criterion)) {
                toteuttaaRajapinnan = true;
            }
        }
        if (!toteuttaaRajapinnan) {
            fail("Does the class " + post(klassname) + " implement interface Criterion?");
        }
    }

    private boolean toteutuu(String klassname, Criterion e, String rivi, String v) throws Throwable {
        classRef = Reflex.reflect(klassname);
        return classRef.method(e, "complies").returning(boolean.class).taking(String.class).withNiceError(v).invoke(rivi);

    }

    private void testaa(String klassname, Criterion e, String v, String[][] rivit) throws Throwable {
        for (String[] rivi : rivit) {
            boolean odotettu = rivi[1].equals("t") ? true : false;
            String f = v + "\ncriterion.complies(\"" + rivi[0] + "\");\n";
            assertEquals(f, odotettu, toteutuu(klassname, e, rivi[0], "\nError caused by code:\n" + f));
        }
    }

    private String luoSana(int pit) {
        String s = "";
        for (int i = 0; i < pit; i++) {
            s += "a";
        }
        return s;
    }

    private boolean vaihtuvaMaaraParametrejaKonstruktorilla() {

        boolean ok = false;
        try {
            Scanner reader = new Scanner(new File("src/reader/criteria/AtLeastOne.java"));
            while (reader.hasNext()) {
                String rivi = reader.nextLine();

                if (rivi.indexOf("//") > -1) {
                    rivi = rivi.substring(0, rivi.indexOf("//"));
                }

                if (rivi.contains("Criterion... ") && rivi.contains("AtLeastOne")) {
                    ok = true;
                    break;
                }

            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
        return ok;
    }
}
