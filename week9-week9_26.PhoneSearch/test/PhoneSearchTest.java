
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import org.junit.*;
import static org.junit.Assert.*;

public class PhoneSearchTest {

    String klassName = "Main";
    Reflex.ClassRef<Object> klass;
    MockInOut mio;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void classPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    /*
     * osa 1
     */
    @Points("26.1")
    @Test
    public void printsMenuAndQuits() throws Throwable {
        String syote = "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String tuloste = mio.getOutput();
        String[] rivit = tuloste.split("\n");

        String rivi = "1 add a number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "2 search for a number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "3 search for a person by phone number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "x quit";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.1")
    @Test
    public void addingNumber() throws Throwable {
        String syote = "1\npekka\n040-12345\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "whose number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.1")
    @Test
    public void addingNumberToTwoPersons() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\nmikko\n040-34343\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
    }

    @Points("26.1")
    @Test
    public void addingTwoNumbersToOnePerson() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\npekka\n040-34343\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
    }

    @Points("26.1")
    @Test
    public void searchingAddedNumber() throws Throwable {
        String syote = "1\npekka\n040-12345\n2\npekka\nx\n";
        mio = new MockInOut(syote);
        assertEquals(0, mio.getOutput().length());
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "whose number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.1")
    @Test
    public void searchingTwoAddedNumbers() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\npekka\n040-43212\n2\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "040-43212";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.1")
    @Test
    public void noExtra() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\njukka\n040-11111\n1\npekka\n040-43212\n2\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "040-43212";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-11111";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.1")
    @Test
    public void nonExistentNotFound() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\njukka\n040-11111\n1\npekka\n040-43212\n2\narto\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "040-43212";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-11111";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "not found";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    /*
     *
     */

    @Points("26.2")
    @Test
    public void searchingAddedPerson() throws Throwable {
        String syote = "1\npekka\n040-12345\n3\n040-12345\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.2")
    @Test
    public void twoNumberSearchWithBothNumbers1() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\njukka\n040-11111\n1\npekka\n040-43212\n3\n040-12345\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-11111";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-43212";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.2")
    @Test
    public void twoNumberSearchWithBothNumbers2() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\njukka\n040-11111\n1\npekka\n040-43212\n3\n040-43212\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "number";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    /*
     * osa 2
     */
    @Points("26.3")
    @Test
    public void printsMenuAndQuitsPart2() throws Throwable {
        String syote = "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String tuloste = mio.getOutput();
        String[] rivit = tuloste.split("\n");

        String rivi = "4 add an address";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "5 search for personal information";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.3")
    @Test
    public void addingAddress() throws Throwable {
        String syote = "4\npekka\nMannerheimintie\nhelsinki\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "whose address";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "street";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "city";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.3")
    @Test
    public void searchingAddress() throws Throwable {
        String syote = "4\npekka\nmannerheimintie\nhelsinki\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "phone number not found";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.3")
    @Test
    public void searchingInfoWithNoAddress() throws Throwable {
        String syote = "1\npekka\n09-12345\n4\nantti\nmannerheimintie\nhelsinki\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "phone number not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.3")
    @Test
    public void numberWithAnAddress() throws Throwable {
        String syote = "4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "phone number not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.3")
    @Test
    public void numberWithAnAddress2() throws Throwable {
        String syote = "4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n2\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.3")
    @Test
    public void addressToOneWithNumber() throws Throwable {
        String syote = "1\npekka\n09-12345\n4\npekka\nmannerheimintie\nhelsinki\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "phone number not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.3")
    @Test
    public void nonExistentIsUnknown() throws Throwable {
        String syote = "1\npekka\n09-12345\n4\npekka\nmannerheimintie\nhelsinki\n5\nseppo\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "phone number not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "not found";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    /*
     * osa 3
     */
    @Points("26.4")
    @Test
    public void printsMenuAndQuitsPart3() throws Throwable {
        String syote = "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String tuloste = mio.getOutput();
        String[] rivit = tuloste.split("\n");

        String rivi = "6 delete personal information";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.4")
    @Test
    public void removal() throws Throwable {
        String syote = "4\npekka\nMannerheimintie\nhelsinki\n6\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "whose information";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.4")
    @Test
    public void removedNotFound() throws Throwable {
        String syote = "4\npekka\nMannerheimintie\nhelsinki\n6\npekka\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "not found";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.4")
    @Test
    public void removingNonExistent() throws Throwable {
        String syote = "4\npekka\nMannerheimintie\nhelsinki\n6\njukka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "whose information";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.4")
    @Test
    public void removalAndSearch1() throws Throwable {
        String syote = "1\njukka\n02-212121\n4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n5\npekka\n6\npekka\n"
                + "2\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.4")
    @Test
    public void removalAndSearch2() throws Throwable {
        String syote = "1\njukka\n02-212121\n4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n1\npekka\n09-54321\n6\npekka\n"
                + "3\n09-54321\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.4")
    @Test
    public void removalAndSearch3() throws Throwable {
        String syote = "1\njukka\n02-212121\n4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n1\npekka\n09-54321\n6\npekka\n"
                + "5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.4")
    @Test
    public void removalAndSearch4() throws Throwable {
        String syote = "1\njukka\n02-212121\n4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n1\npekka\n09-54321\n6\npekka\n"
                + "5\njukka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    /*
     * osa 4
     */
    @Points("26.5")
    @Test
    public void printsMenuAndQuitsPart4() throws Throwable {
        String syote = "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String tuloste = mio.getOutput();
        String[] rivit = tuloste.split("\n");

        String rivi = "6 delete personal information";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "7 filtered listing";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.5")
    @Test
    public void filteredSearch1() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\njukka\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.5")
    @Test
    public void filteredSearch2() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\npekka\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.5")
    @Test
    public void filteredSearch3() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\nseppo\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.5")
    @Test
    public void filteredSearch4() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\nhelsinki\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("26.5")
    @Test
    public void filteredSearch5() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\nkk\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));

        int pekka = nro(rivit, "pekka");
        int jukka = nro(rivit, "jukka");
        assertTrue("Verify that print output is exactly the same as in the assignment, when input is " + toS(syote) + " "
                + "Jukka's information should be before Pekka's information", jukka < pekka);
        pekka = nro(rivit, "pekka");
        jukka = nro(rivit, "02-21212");
        assertTrue("Verify that print output is exactly the same as in the assignment, when input is " + toS(syote) + " "
                + "Jukka's information should be before Pekka's information", jukka < pekka);
        pekka = nro(rivit, "helsinki");
        jukka = nro(rivit, "02-21212");
        assertTrue("Verify that print output is exactly the same as in the assignment, when input is " + toS(syote) + " "
                + "Jukka's information should be before Pekka's information", jukka < pekka);
    }

    @Points("26.5")
    @Test
    public void filteredSearch6() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "1\nantti\n040-111222\n"
                + "7\nkk\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "antti";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-111222";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));

        int pekka = nro(rivit, "pekka");
        int jukka = nro(rivit, "jukka");

        assertTrue("Verify that print output is exactly the same as in the assignment, when input is " + toS(syote) + " "
                + "Jukka's information should be before Pekka's information", jukka < pekka);
        pekka = nro(rivit, "pekka");
        jukka = nro(rivit, "02-21212");
        assertTrue("Verify that print output is exactly the same as in the assignment, when input is " + toS(syote) + " "
                + "Jukka's information should be before Pekka's information", jukka < pekka);
        pekka = nro(rivit, "helsinki");
        jukka = nro(rivit, "02-21212");
        assertTrue("Verify that print output is exactly the same as in the assignment, when input is " + toS(syote) + " "
                + "Jukka's information should be before Pekka's information", jukka < pekka);
    }

    @Points("26.5")
    @Test
    public void filteredSearch7() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "1\nantti\n040-111222\n"
                + "7\na\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "not found";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "address unknown";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "antti";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-111222";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));

        int pekka = nro(rivit, "pekka");
        int jukka = nro(rivit, "jukka");
        int antti = nro(rivit, "antti");

        assertTrue("Verify that print output is exactly the same as in the assignment"
                + "\nwhen input is " + toS(syote) + " "
                + "\nAntti's information should be before Jukka's information", antti < jukka);
        assertTrue("Verify that print output is exactly the same as in the assignment\n"
                + "when input is " + toS(syote) + " "
                + "\nJukka's information should be before Pekka's information", jukka < pekka);
        pekka = nro(rivit, "pekka");
        jukka = nro(rivit, "02-21212");
        assertTrue("Verify that print output is exactly the same as in the assignment\n"
                + "when input is " + toS(syote) + " "
                + "\nJukka's information should be before Pekka's information", jukka < pekka);
        pekka = nro(rivit, "helsinki");
        jukka = nro(rivit, "02-21212");
        assertTrue("Verify that print output is exactly the same as in the assignment, when input is " + toS(syote) + " "
                + "\nJukka's information should be before Pekka's information", jukka < pekka);
    }
    /*
     * filtteröity lista
     */
    /*
     * helpers
     */

    private String viesti(String rivi, String syote) {
        return "Verify that print output is exactly the same as in the assignment, "
                + "should have printed line which contains \"" + rivi + "\"\n"
                + "when input was " + toS(syote)+"\n"
                + "\nProgram printed:\n"+mio.getOutput();
    }

    private String viestiEi(String rivi, String syote) {
        return "Verify that print output is exactly the same as in the assignment, "
                + "shouldn't have printed line which contains \"" + rivi + "\"\n"
                + "when input was " + toS(syote)+"\n"
                + "\nProgram printed:\n"+mio.getOutput();
    }

    private String f(String syote) {
        return "\nuser input was:\n" + syote;
    }

    private void suorita(String error) throws Throwable {
        String[] args = new String[0];
        klass.staticMethod("main").
                returningVoid().
                taking(String[].class).withNiceError(error).
                invoke(args);
    }

    private void doStuff(String syote) throws Throwable {


        try {
            suorita(f(syote));
        } catch (Throwable t) {
            if (t.toString().contains("NoSuch")) {
                fail("execution of your program should stop with input " + toS(syote));
            }

            new MockInOut(syote);
            suorita(f(syote));
        }
    }

    private String toS(String syote) {
        return "\n"+syote;
        //return syote.replaceAll("\n", "<enter>");
    }

    private boolean sisaltaa(String[] rivit, String haettava) {
        return hae(rivit, haettava) != null;
    }

    private String hae(String[] rivit, String haettava) {

        for (String rivi : rivit) {
            if (rivi.toLowerCase().contains(haettava.toLowerCase())) {
                return rivi;
            }
        }

        return null;
    }

    private int nro(String[] rivit, String haettava) {
        for (int i = 0; i < rivit.length; i++) {
            String rivi = rivit[i];

            if (rivi.toLowerCase().contains(haettava.toLowerCase())) {
                return i;
            }
        }

        return -1;
    }
}
