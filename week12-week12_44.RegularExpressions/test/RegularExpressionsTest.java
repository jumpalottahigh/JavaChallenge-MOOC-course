
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.File;
import java.util.Scanner;
import org.junit.*;
import static org.junit.Assert.*;

public class RegularExpressionsTest {

    String klassName = "Main";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Points("44.1")
    @Test
    public void hasMethodIsAWeekDay() {
        String metodi = "isAWeekDay";
        assertTrue("Create method public static boolean isAWeekDay(String string) for class Main", klass.staticMethod(metodi)
                .returning(boolean.class).taking(String.class).isPublic());
    }

    @Points("44.1")
    @Test
    public void noForbiddenCommands() {
        noForbiddens();
    }

    @Points("44.1")
    @Test
    public void isAWeekDayAccepts() throws Throwable {
        String[] mj = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};

        for (String pv : mj) {
            String v = "check code: "
                    + "isAWeekDay(\"" + pv + "\")\n";
            assertEquals(v, true, isAWeekDay(pv, v));
        }

    }

    @Points("44.1")
    @Test
    public void isAWeekDayRejects() throws Throwable {
        String[] mj = {"m", "mond", "monday", "", "tuetue", "arto", "exam", "mon "};

        for (String pv : mj) {
            String v = "check code: isAWeekDay(\"" + pv + "\")\n";
            assertEquals(v, false, isAWeekDay(pv, v));
        }
    }

    @Points("44.2")
    @Test
    public void hasMethodAllVowels() {
        String virhe = "Create method public static boolean allVowels(String string) for class Main";
        String metodi = "allVowels";
        assertTrue(virhe, klass.staticMethod(metodi)
                .returning(boolean.class).taking(String.class).isPublic());
    }

    @Points("44.2")
    @Test
    public void acceptsVowels() throws Throwable {
        String[] mj = {"a", "aeiouäö", "aaa", "uiuiui", "uaa", "aaai", "ai"};

        for (String pv : mj) {
            String v = "check code: allVowels(\"" + pv + "\")\n";
            assertEquals(v, true, allVowels(pv, v));
        }

    }

    @Points("44.2")
    @Test
    public void rejectsIfNotAllVowels() throws Throwable {
        String[] mj = {"fågel", "aaaab", "waeiou", "x", "aaaaaaqaaaaaaaaa", "ala"};

        for (String pv : mj) {
            String v = "check code: allVowels(\"" + pv + "\")\n";
            assertEquals(v, false, allVowels(pv, v));
        }
    }

    @Points("44.2")
    @Test
    public void noForbiddenCommands2() {
        noForbiddens();
    }

    @Points("44.3")
    @Test
    public void hasMethodClockTime() {
        String virhe = "Create method public static boolean clockTime(String string) for class Main";
        String metodi = "clockTime";
        assertTrue(virhe, klass.staticMethod(metodi)
                .returning(boolean.class).taking(String.class).isPublic());
    }

    private boolean clockTime(String mj, String v) throws Throwable {
        String metodi = "clockTime";
        return klass.staticMethod(metodi)
                .returning(boolean.class).taking(String.class).withNiceError(v).invoke(mj);
    }

    private boolean isAWeekDay(String mj, String v) throws Throwable {
        String metodi = "isAWeekDay";
        return klass.staticMethod(metodi)
                .returning(boolean.class).taking(String.class).withNiceError(v).invoke(mj);
    }

    @Points("44.3")
    @Test
    public void clockTimeAccepts() throws Throwable {
        String[] mj = {"20:00:00", "11:24:00", "04:59:31", "14:41:16", "23:32:23", "20:00:59"};

        for (String pv : mj) {
            String v = "check code: clockTime(\"" + pv + "\")\n";
            assertEquals(v, true, clockTime(pv, v));
        }

    }

    @Points("44.3")
    @Test
    public void clockTimeRejects() throws Throwable {
        String[] mj = {"a", "aaaaaaa", "3:59:31", "24:41:16", "23:61:23", "20:00:79",
            "13:9:31", "21:41:6", "23,61:23", "20:00;79"};

        for (String pv : mj) {
            String v = "check code: clockTime(\"" + pv + "\")\n";
            assertEquals(v, false, clockTime(pv, v));
        }

    }

    private boolean allVowels(String m, String v) throws Throwable {
        String metodi = "allVowels";

        return klass.staticMethod(metodi)
                .returning(boolean.class).taking(String.class).withNiceError(v).invoke(m);
    }

    private void noForbiddens() {
        try {
            Scanner lukija = new Scanner(new File("src/Main.java"));
            int mainissa = 0;
            while (lukija.hasNext()) {

                String virhe = "Because we are exercising the usage of String.match, don't use command ";

                String rivi = lukija.nextLine();

                if (rivi.contains("void main(") || rivi.contains("boolean clockTime(")) {
                    mainissa++;
                } else if (mainissa > 0) {

                    if (rivi.contains("{") && !rivi.contains("}")) {
                        mainissa++;
                    }

                    if (rivi.contains("}") && !rivi.contains("{")) {
                        mainissa--;
                    }
                    continue;
                }

                if (mainissa > 0) {
                    continue;
                }

                String f = "equals";
                if (rivi.contains(f)) {
                    fail(virhe + f + " problem in line " + rivi);
                }

                f = "charAt";
                if (rivi.contains(f)) {
                    fail(virhe + f + " problem in line " + rivi);
                }

                f = "indexOf";
                if (rivi.contains(f)) {
                    fail(virhe + f + " problem in line " + rivi);
                }

                f = "contains";
                if (rivi.contains(f)) {
                    fail(virhe + f + " problem in line " + rivi);
                }

                f = "substring";
                if (rivi.contains(f)) {
                    fail(virhe + f + " problem in line " + rivi);
                }

            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
