
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

@Points("8.1 8.2 8.3")
public class AirportTest {

    String klassName = "Main";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void printsMenusAndExits() throws Throwable {
        String syote = "x\nx\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Airport panel",
            "[1] Add airplane",
            "[2] Add flight",
            "[x] Exit",
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void printingWhenAddingAirplane() throws Throwable {
        String syote = "1\nAY-123\n108\nx\nx\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Airport panel",
            "[1] Add airplane",
            "[2] Add flight",
            "[x] Exit",
            "Give plane ID:",
            "Give plane capacity:",
            "[1] Add airplane",
            "[2] Add flight",
            "[x] Exit",
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void printingWhenAddingFlight() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "2\nAY-123\nHEL\nTXL\n"
                + "\nx\nx\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Airport panel",
            "[1] Add airplane",
            "[2] Add flight",
            "[x] Exit",
            "Give plane ID:",
            "Give plane capacity:",
            "[1] Add airplane",
            "[2] Add flight",
            "[x] Exit",
            "Give plane ID:",
            "Give departure airport code:",
            "Give destination airport code:",
            "[1] Add airplane",
            "[2] Add flight",
            "[x] Exit",
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

    }

    @Test
    public void airplanePrinting1() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "x\n"
                + "1\n"
                + "x\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit",
            "AY-123 (108 ppl)"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void airplanePrinting2() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "1\nDE-213\n75\n"
                + "x\n"
                + "1\n"
                + "x\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit",};

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

        String rivi = "AY-123 (108 ppl)";
        assertRight(rivi, syote, output, output.contains(rivi));

        rivi = "DE-213 (75 ppl)";
        assertRight(rivi, syote, output, output.contains(rivi));
    }

    @Test
    public void flightPrinting1() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "2\nAY-123\nHEL\nTXL\n"
                + "x\n"
                + "2\n"
                + "x\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit",
            "AY-123 (108 ppl) (HEL-TXL)"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

    }

    @Test
    public void flightPrinting2() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "2\nAY-123\nHEL\nTXL\n"
                + "2\nAY-123\nJFK\nHEL\n"
                + "x\n"
                + "2\n"
                + "x\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit",
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

        String rivi = "AY-123 (108 ppl) (HEL-TXL)";
        assertRight(rivi, syote, output, output.contains(rivi));

        rivi = "AY-123 (108 ppl) (JFK-HEL)";
        assertRight(rivi, syote, output, output.contains(rivi));
    }

    @Test
    public void flightPrinting3() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "1\nDE-213\n75\n"
                + "2\nAY-123\nHEL\nTXL\n"
                + "2\nAY-123\nJFK\nHEL\n"
                + "2\nDE-213\nTXL\nBAL\n"
                + "x\n"
                + "2\n"
                + "x\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit",
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

        String rivi = "AY-123 (108 ppl) (HEL-TXL)";
        assertRight(rivi, syote, output, output.contains(rivi));

        rivi = "AY-123 (108 ppl) (JFK-HEL)";
        assertRight(rivi, syote, output, output.contains(rivi));

        rivi = "DE-213 (75 ppl) (TXL-BAL)";
        assertRight(rivi, syote, output, output.contains(rivi));
    }

    @Test
    public void flightInfoPrinting1() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "x\n"
                + "3\n"
                + "AY-123\n"
                + "x\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit",
            "Give plane ID:",
            "AY-123 (108 ppl)"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void flightInfoPrinting2() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "1\nDE-213\n75\n"
                + "x\n"
                + "3\n"
                + "AY-123\n"
                + "x\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit",
            "Give plane ID:",
            "AY-123 (108 ppl)"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

        String rivi = "DE-213";
        assertFalse("Verify that your program's output is exactly same as in the assignment's example\n"
                + f(syote) + "\nprogram shouldn't have printed line with text \"" + rivi + "\"!\n"
                + "your program's output was:\n\n" + output, output.contains("DE-213"));
    }
    @Test
    public void complexInput() throws Throwable {
        String syote = "1\nAY-123\n108\n"
                + "1\nDE-213\n75\n"
                + "1\nRU-999\n430\n"
                + "2\nAY-123\nHEL\nTXL\n"
                + "2\nAY-123\nJFK\nHEL\n"
                + "2\nDE-213\nTXL\nBAL\n"
                + "x\n"
                + "2\n"
                + "1\n"
                + "3\n"
                + "AY-123\n"
                + "x\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] menuRivit = {
            "Flight service",
            "[1] Print planes",
            "[2] Print flights",
            "[3] Print plane info",
            "[x] Quit",};

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : menuRivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

        String rivi = "AY-123 (108 ppl) (HEL-TXL)";
        assertRight(rivi, syote, output, output.contains(rivi));

        rivi = "AY-123 (108 ppl) (JFK-HEL)";
        assertRight(rivi, syote, output, output.contains(rivi));

        rivi = "DE-213 (75 ppl) (TXL-BAL)";
        assertRight(rivi, syote, output, output.contains(rivi));

        int ind = op.indexOf("DE-213 (75 ppl) (TXL-BAL)");
        op = op.substring(ind + 1);

        rivi = "AY-123 (108 ppl)";
        assertRight(rivi, syote, output, op.contains(rivi));

        rivi = "DE-213 (75 ppl)";
        assertRight(rivi, syote, output, op.contains(rivi));

        rivi = "RU-999 (430 ppl)";
        assertRight(rivi, syote, output, op.contains(rivi));

        ind = op.indexOf("RU-999 (430 ppl)");
        op = op.substring(ind + 1);

        rivi = "AY-123 (108 ppl)";
        assertRight(rivi, syote, output, op.contains(rivi));
    }

    private void assertRight(String menuRivi, String syote, String output, boolean ehto) {
        assertTrue("Verify that your program's output is exactly same as in the assignment's example\n"
                + f(syote) + "\nprogram should have printed line \"" + menuRivi + "\" in the right part\n"
                + "your program's output was:\n\n" + output, ehto);
    }

    private String f(String syote) {
        return "\nuser's input was:\n" + syote;
    }

    private void suorita(String error) throws Throwable {
        String[] args = new String[0];
        klass.staticMethod("main").
                returningVoid().
                taking(String[].class).withNiceError(error).
                invoke(args);
    }
}
