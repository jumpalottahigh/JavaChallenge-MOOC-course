
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@Points("1")
public class SmileysTest {

    String klassName = "Smileys";
    Reflex.ClassRef<Object> klass;

    @Before
    public void justForKicks() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void classExists() {
        klass = Reflex.reflect(klassName);
        assertTrue("Class " + klassName + " should be public, define it with npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void hasMethodprintWithSmileys() throws Throwable {
        String metodi = "printWithSmileys";

        assertTrue("Add the class method private static void printWithSmileys(String characterString)",
                klass.staticMethod(metodi).returningVoid().taking(String.class).isPrivate());

        kutsu("Mikke");
    }

    @Test
    public void correctOutputWithMikael() throws Throwable {
        MockInOut io = new MockInOut("");

        kutsu("Mikael");

        String output = io.getOutput();
        assertFalse("You did not print anything" ,output.isEmpty());

        String[] rivit = output.split("\n");
        if (rivit.length != 3) {
            fail("You printed " + rivit.length + " linebreaks, when 3 was expected.");
        }

        if (rivit[0].contains(":):):):):):):)")) {
            fail("There is too much smileys at the first line, test your method with parameter  \"Mikael\"");
        }

        if (rivit[2].contains(":):):):):):):)")) {
            fail("There is too much smileys at the last line test your method with parameter  \"Mikael\"");
        }

        if (!output.contains(":) Mikael")) {
            fail("Ensure that there is a smiley and whitespace in front of the given parameter.");
        }
        if (!output.contains(" Mikael ")) {
            fail("Ensure that the given parameter has one whitespace printed on both sides.");
        }

        if (!output.contains(" Mikael :)\n")) {
            fail("Ensure that there is a whitespace, smiley and a linebreak at the right side of the given parameter.");
        }
        if (!rivit[0].equals(":):):):):):)")) {
            fail("First line is not correct with input \"Mikael\" - it should be \":):):):):):)\"");
        }
        if (!rivit[1].equals(":) Mikael :)")) {
            fail("Second line is not correct with input \"Mikael\" - it should have been \":) Mikael :)\"");
        }
        if (!rivit[2].equals(":):):):):):)")) {
            fail("Third line is not correct with input \"Mikael\" - it should have been \":):):):):):)\"");
        }
    }

    @Test
    public void correctOutputWithArto() throws Throwable {
        MockInOut io = new MockInOut("");

        kutsu("Arto");

        String output = io.getOutput();
        assertFalse("You did not print anything",output.isEmpty());

        String[] rivit = output.split("\n");
        if (rivit.length != 3) {
            fail("You printed " + rivit.length + " linebreaks, when 3 was expected.");
        }

        if (rivit[0].contains(":):):):):):)")) {
            fail("There is too much smileys at the first line, test your method with parameter  \"Arto\"");
        }

        if (rivit[2].contains(":):):):):):)")) {
            fail("There is too much smileys at the last line test your method with parameter  \"Arto\"");
        }

        if (!output.contains(":) Arto")) {
            fail("Ensure that there is a smiley and whitespace in front of the given parameter.");
        }
        if (!output.contains(" Arto ")) {
            fail("Ensure that the given parameter has one whitespace printed on both sides.");
        }

        if (!output.contains(" Arto :)\n")) {
            fail("Ensure that there is a whitespace, smiley and a linebreak at the right side of the given parameter.");
        }
        if (!rivit[0].equals(":):):):):)")) {
            fail("First line is not correct with input \"Arto\" - it should be \":):):):):)\"");
        }
        if (!rivit[1].equals(":) Arto :)")) {
            fail("Second line is not correct with input \"Arto\" - it should have been \":) Arto :)\"");
        }
        if (!rivit[2].equals(":):):):):)")) {
            fail("Third line is not correct with input \"Arto\" - it should have been \":):):):):)\"");
        }
    }

    @Test
    public void correctOutputWithMatti() throws Throwable {
        MockInOut io = new MockInOut("");

        kutsu("Matti");

        String output = io.getOutput();
        String[] rivit = output.split("\n");
        if (rivit.length != 3) {
            fail("You printed" + rivit.length + " linebreaks, when 3 was expected.");
        }
        if (rivit[0].contains(":):):):):):):)")) {
            fail("There is too much smileys at the first line, test your method with parameter \"Matti\"");
        }

        if (rivit[2].contains(":):):):):):):):)")) {
            fail("There is too much smileys at the last line, test your method with parameter \"Matti\"");
        }

        if (!output.contains(":) Matti")) {
            fail("Ensure that there is a smiely and whitespace in front of the given parameter.");
        }
        if (!output.contains(" Matti ")) {
            fail("Ensure that the given parameter has one whitespace printed on both sides.");
        }

        if (!output.contains(" Matti  :)\n")) {
            fail("Ensure that with parameter having an odd length you print two whitespaces, a smiley and a new line at the right side of the parameter.");
        }
        if (!rivit[0].equals(":):):):):):)")) {
            fail("First line is not correct with input \"Matti\" - it should have been \":):):):):):)\"");
        }
        if (!rivit[1].equals(":) Matti  :)")) {
            fail("Second line is not correct with input \"Matti\" - it should have been \":) Matti  :)\"");
        }
        if (!rivit[2].equals(":):):):):):)")) {
            fail("Third line is not correct with input \"Matti\" - it should have been \":):):):):):)\"");
        }
    }

    private void kutsu(String mj) throws Throwable {
        String v = "Problem was caused by "
                + "printWithSmileys(\"" + mj + "\");";
        String metodi = "printWithSmileys";
        klass.staticMethod(metodi).returningVoid().taking(String.class).withNiceError(v).invoke(mj);
    }
}