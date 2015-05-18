
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("16.1 16.2 16.3 16.4")
public class SkiJumpingTest {

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
    public void quits() throws Throwable {
        String syote = "Arto\n\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        try {
            suorita(f(syote));
        } catch (Throwable e) {
            if (e.toString().contains("NoSuchElementExc")) {
                fail("Check, that your program quits with input\n" + syote);
            }
        }
    }

    @Test
    public void oneRoundOneJumperPrintingOk() throws Throwable {
        String syote = "Arto\n\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] rivit = {
            "Kumpula ski jumping week",
            "Write the names of the participants one at a time; an empty string brings you to the jumping phase.",
            "  Participant name:",
            "  Participant name:",
            "The tournament begins!",
            "Write \"jump\" to jump; otherwise you quit:",
            "Jumping order:",
            "  1. Arto (0 points)",
            "Results of round 1",
            "  Arto",
            "    length:",
            "    judge votes:",
            "Write \"jump\" to jump; otherwise you quit:",
            "Thanks!",
            "Tournament results:",
            "Position    Name",
            "1           Arto",
            "            jump lengths:"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : rivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void twoRoundsOneJumperPrintingOk() throws Throwable {
        String syote = "Arto\n\njump\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] rivit = {
            "Kumpula ski jumping week",
            "Write the names of the participants one at a time; an empty string brings you to the jumping phase.",
            "  Participant name:",
            "  Participant name:",
            "The tournament begins!",
            "Write \"jump\" to jump; otherwise you quit:",
            "Jumping order:",
            "  1. Arto (0 points)",
            "Results of round 1",
            "  Arto",
            "    length:",
            "    judge votes:",
            "Write \"jump\" to jump; otherwise you quit:",
            "Jumping order:",
            "  1. Arto (",
            "Results of round 2",
            "  Arto",
            "    length:",
            "    judge votes:",
            "Write \"jump\" to jump; otherwise you quit:",
            "Thanks!",
            "Tournament results:",
            "Position    Name",
            "1           Arto",
            "            jump lengths:"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : rivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void threeRoundsOneJumperPrintingOk() throws Throwable {
        String syote = "Arto\n\njump\njump\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] rivit = {
            "Kumpula ski jumping week",
            "Write the names of the participants one at a time; an empty string brings you to the jumping phase.",
            "  Participant name:",
            "  Participant name:",
            "The tournament begins!",
            "Write \"jump\" to jump; otherwise you quit:",
            "Jumping order:",
            "  1. Arto (0 points)",
            "Results of round 1",
            "  Arto",
            "    length:",
            "    judge votes:",
            "Write \"jump\" to jump; otherwise you quit:",
            "Jumping order:",
            "  1. Arto (",
            "Results of round 2",
            "  Arto",
            "    length:",
            "    judge votes:",
            "Write \"jump\" to jump; otherwise you quit:",
            "Jumping order:",
            "  1. Arto (",
            "Results of round 3",
            "  Arto",
            "    length:",
            "    judge votes:",
            "Write \"jump\" to jump; otherwise you quit:",
            "Thanks!",
            "Tournament results:",
            "Position    Name",
            "1           Arto",
            "            jump lengths:"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : rivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void oneRoundOneJumperPointCalculationCorrect() throws Throwable {
        String syote = "Arto\n\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String kierros1 = kierroksenTulokset(io.getOutput(), 1);
        ArrayList<SkiJumpingTest.PitPist> pitPist = lengthJaPisteet(kierros1, 1);

        assertPisteetOk(pitPist, 1, kierros1);

        loppuTulostenIlmoitus1osallistuja1kierros(io.getOutput(), pitPist);
    }

    @Test
    public void twoRoundsOneJumperPointsCalculationCorrect() throws Throwable {
        String syote = "Arto\n\njump\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String out = io.getOutput();

        String kierros1 = kierroksenTulokset(out, 1);
        ArrayList<SkiJumpingTest.PitPist> pitPist1 = lengthJaPisteet(kierros1, 1);
        assertPisteetOk(pitPist1, 1, kierros1);

        String kierros2 = kierroksenTulokset(out, 2);
        ArrayList<SkiJumpingTest.PitPist> pitPist2 = lengthJaPisteet(kierros2, 2);
        assertPisteetOk(pitPist2, 2, kierros2);

        eiAinaSamaaTulosta(pitPist1, pitPist2, out);

        loppuTulostenIlmoitus1osallistuja2kierrosta(io.getOutput(), pitPist1, pitPist2);
    }

    @Test
    public void threeRoundsOneJumperPointsCalculationCorrect() throws Throwable {
        String syote = "Arto\n\njump\njump\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String out = io.getOutput();

        String kierros1 = kierroksenTulokset(out, 1);
        ArrayList<SkiJumpingTest.PitPist> pitPist1 = lengthJaPisteet(kierros1, 1);
        assertPisteetOk(pitPist1, 1, kierros1);

        String kierros2 = kierroksenTulokset(out, 2);
        ArrayList<SkiJumpingTest.PitPist> pitPist2 = lengthJaPisteet(kierros2, 2);
        assertPisteetOk(pitPist2, 2, kierros2);

        String kierros3 = kierroksenTulokset(out, 3);
        ArrayList<SkiJumpingTest.PitPist> pitPist3 = lengthJaPisteet(kierros3, 3);
        assertPisteetOk(pitPist3, 3, kierros3);

        eiAinaSamaaTulosta(pitPist1, pitPist2, out);
        eiAinaSamaaTulosta(pitPist1, pitPist3, out);
        eiAinaSamaaTulosta(pitPist3, pitPist2, out);

        loppuTulostenIlmoitus1osallistuja3kierrosta(io.getOutput(), pitPist1, pitPist2, pitPist3);
    }

    @Test
    public void oneRoundTwoJumpers() throws Throwable {
        String syote = "Arto\nPekka\n\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] rivit = {
            "Kumpula ski jumping week",
            "Write the names of the participants one at a time; an empty string brings you to the jumping phase.",
            "  Participant name:",
            "  Participant name:",
            "  Participant name:",
            "The tournament begins!",
            "Write \"jump\" to jump; otherwise you quit:",
            "Jumping order:",};

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : rivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

        String tulos = kierroksenTulokset(output, 1);
        ArrayList<SkiJumpingTest.PitPist> pitPist = lengthJaPisteet(tulos, 1);
        assertTrue(tulos.length() > 1);
        assertPisteetOk(pitPist, 1, tulos);

        loppuTulostenIlmoitus2osallistuja1kierros(output, pitPist);

        jarjestysKunnossa(output, pitPist);
    }

    @Test
    public void oneRoundFiveJumpers() throws Throwable {
        String syote = "Arto\nPekka\nMatti\nMikko\nJukka\n\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String output = io.getOutput();

        String tulos = kierroksenTulokset(output, 1);
        ArrayList<SkiJumpingTest.PitPist> pitPist = lengthJaPisteet(tulos, 1);
        assertTrue(tulos.length() > 1);
        assertPisteetOk(pitPist, 1, tulos);

        eiSamaaPituuttaKaikilla(pitPist, tulos);

        jarjestysKunnossa(output, pitPist);
    }

    @Test
    public void twoRoundsFiveJumpers() throws Throwable {
        String syote = "Arto\nPekka\nMatti\nMikko\nJukka\n\njump\njump\nquit\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String output = io.getOutput();

        String tulos1 = kierroksenTulokset(output, 1);
        ArrayList<SkiJumpingTest.PitPist> pitPist1 = lengthJaPisteet(tulos1, 1);
        assertTrue(tulos1.length() > 1);
        assertPisteetOk(pitPist1, 1, tulos1);

        String tulos2 = kierroksenTulokset(output, 1);
        ArrayList<SkiJumpingTest.PitPist> pitPist2 = lengthJaPisteet(tulos2, 2);
        assertTrue(tulos2.length() > 1);
        assertPisteetOk(pitPist2, 2, tulos2);

        oikeaHyppyjarjestys(output, pitPist1);

    }

    private String kierroksenTulokset(String output, int k) {
        String rivi = haeRiviJolla(output, "Results of round " + k);
        if (rivi == null) {
            // ei pitäs päätyä tänne
            rivi = "Results of round " + k;
        }

        int alku = output.indexOf(rivi) + (rivi).length();
        int loppu = output.substring(alku).indexOf("Write \"jump\" to jump; otherwise you quit:");
        String tulos = output.substring(alku, alku + loppu);
        return tulos;
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

    private void assertRight(String menuRivi, String syote, String output, boolean ehto) {
        assertTrue("Check that your program's print output is exactly same as given in the assignment\n"
                + f(syote) + "\nprogram should have printed line in the right place which reads \"" + menuRivi + "\" "
                + "\n"
                + "your program printed:\n\n" + output, ehto);
    }

    private boolean viisiLukua(String mj) {
        return mj.matches("\\d+\\s+\\d+\\s+\\d+\\s+\\d+\\s+\\d+");
    }

    private void assertPisteetOk(ArrayList<SkiJumpingTest.PitPist> pitPist, int k, String kierros) {
        for (SkiJumpingTest.PitPist pitPist1 : pitPist) {
            assertTrue("Jumper " + pitPist1.nimi + "'s jump length on round " + k + " wasn\'t in the allowed range\n"
                    + "Results of round " + k + " were: \n" + kierros, pitPist1.pit > 59 && pitPist1.pit < 121);

            for (int p : pitPist1.pist) {
                assertTrue("Jumper " + pitPist1.nimi + "'s judge votes on round " + k + " weren\'t in the allowed range\n"
                        + "Results of round " + k + " were: \n" + kierros, p > -1 && p < 21);

            }
        }
    }

    private int[] otaHypyt(String rivi) {
        if (!rivi.contains("jump lengths:")) {
            return null;
        }

        String hypyt = rivi.replaceAll("\\D", " ").trim();

        ArrayList<Integer> hyppylista = new ArrayList<Integer>();

        Scanner luvut = new Scanner(hypyt);
        while (luvut.hasNextInt()) {
            hyppylista.add(luvut.nextInt());
        }

        int[] hyppyTaulukko = new int[hyppylista.size()];

        for (int i = 0; i < hyppyTaulukko.length; i++) {
            hyppyTaulukko[i] = hyppylista.get(i);
        }

        return hyppyTaulukko;
    }

    private int laskePisteet(SkiJumpingTest.PitPist... ppp) {
        int p = 0;
        for (SkiJumpingTest.PitPist pp : ppp) {
            Arrays.sort(pp.pist);
            p += pp.pist[1] + pp.pist[2] + pp.pist[3] + pp.pit;
        }

        return p;
    }

    private int etsiRivi(String[] rivit, String nimi) {
        for (int i = 0; i < rivit.length; i++) {
            if (rivit[i].contains(nimi)) {
                return i;
            }
        }

        return -1;
    }

    private int otaPisteet(String rivi) {
        int p = -1;

        try {
            rivi = rivi.substring(2);
            rivi = rivi.replaceAll("\\D", "").trim();
            p = Integer.parseInt(rivi);
        } catch (Exception e) {
        }

        return p;
    }

    private void loppuTulostenIlmoitus2osallistuja1kierros(String output, ArrayList<SkiJumpingTest.PitPist> pitPist) {
        SkiJumpingTest.PitPist[] ppt = {pitPist.get(0)};

        lopputulostenIlmoitus1osallistuja(ppt, output);

        SkiJumpingTest.PitPist[] ppt2 = {pitPist.get(1)};

        lopputulostenIlmoitus1osallistuja(ppt2, output);
    }

    private void loppuTulostenIlmoitus1osallistuja1kierros(String output, ArrayList<SkiJumpingTest.PitPist> pitPist) {
        SkiJumpingTest.PitPist[] ppt = {pitPist.get(0)};

        lopputulostenIlmoitus1osallistuja(ppt, output);
    }

    private void loppuTulostenIlmoitus1osallistuja2kierrosta(String output, ArrayList<SkiJumpingTest.PitPist> pitPist, ArrayList<SkiJumpingTest.PitPist> pitPist2) {
        SkiJumpingTest.PitPist[] ppt = {pitPist.get(0), pitPist2.get(0)};

        lopputulostenIlmoitus1osallistuja(ppt, output);
    }

    private void loppuTulostenIlmoitus1osallistuja3kierrosta(String output, ArrayList<SkiJumpingTest.PitPist> pitPist, ArrayList<SkiJumpingTest.PitPist> pitPist2, ArrayList<SkiJumpingTest.PitPist> pitPist3) {
        SkiJumpingTest.PitPist[] ppt = {pitPist.get(0), pitPist2.get(0), pitPist3.get(0)};

        lopputulostenIlmoitus1osallistuja(ppt, output);
    }

    private void links(String output, SkiJumpingTest.PitPist pp1, SkiJumpingTest.PitPist pp2) {

        if (laskePisteet(pp1) > laskePisteet(pp2)) {
            assertTrue("jump order of round 2 is wrong\n"
                    + "your program printed\n"
                    + output, output.indexOf(pp1.nimi) > output.indexOf(pp2.nimi));

        } else if (laskePisteet(pp1) < laskePisteet(pp2)) {
            assertTrue("jump order of round 2 is wrong\n"
                    + "your program printed\n"
                    + output, output.indexOf(pp1.nimi) < output.indexOf(pp2.nimi));
        }
    }

    private void rechts(String output, SkiJumpingTest.PitPist pp1, SkiJumpingTest.PitPist pp2) {
        String loppu = output.substring(output.indexOf("Position    Name") + "Position    Name".length());
        if (laskePisteet(pp1) > laskePisteet(pp2)) {
            assertTrue("tournament results aren\'t shown in the correct order\n"
                    + "your program printed\n"
                    + output, loppu.indexOf(pp1.nimi) < loppu.indexOf(pp2.nimi));

        } else if (laskePisteet(pp1) < laskePisteet(pp2)) {
            assertTrue("tournament results aren\'t shown in the correct order\n"
                    + "your program printed\n"
                    + output, loppu.indexOf(pp1.nimi) > loppu.indexOf(pp2.nimi));

        }
    }

    private void hyppyJarjestysKunnossa(String output, ArrayList<SkiJumpingTest.PitPist> pitPist) {

        for (int i = 0; i < pitPist.size(); i++) {
            for (int j = i + 1; j < pitPist.size(); j++) {
                links(output, pitPist.get(i), pitPist.get(j));
            }

        }

    }

    private void jarjestysKunnossa(String output, ArrayList<SkiJumpingTest.PitPist> pitPist) {

        for (int i = 0; i < pitPist.size(); i++) {
            for (int j = i + 1; j < pitPist.size(); j++) {
                rechts(output, pitPist.get(i), pitPist.get(j));
            }

        }

    }

    private void lopputulostenIlmoitus1osallistuja(SkiJumpingTest.PitPist[] ppt, String output) {
        String nimi = ppt[0].nimi;

        String loppu = output.substring(output.indexOf("Position    Name") + "Position    Name".length());
        String[] rivit = loppu.split("\n");

        int ind = etsiRivi(rivit, nimi);

        int pist = otaPisteet(rivit[ind]);
        assertFalse("There was an error in printing the tournament results, "
                + "print output should have contained line of format\n"
                + "1           " + nimi + " (" + laskePisteet(ppt) + " points)\n"
                + "program printed \n" + loppu, pist == -1);

        assertEquals("Jumper " + nimi + "'s points are printed incorrectly in the tournament results\n"
                + "your program prints\n" + output, laskePisteet(ppt), pist);

        int[] hypyt = otaHypyt(rivit[ind + 1]);
        assertFalse("Jumper " + nimi + "'s jump lengths are printed incorrectly in the tournament results\n"
                + "your program prints\n" + output, hypyt == null);

        assertTrue("Jumper " + nimi + "'s jump lengths are printed incorrectly in the tournament results\n"
                + "your program prints\n" + output, hypyt.length == ppt.length);

        for (int i = 0; i < hypyt.length; i++) {
            assertEquals("Jumper " + nimi + "'s jump lengths are printed incorrectly in the tournament results\n"
                    + "your program prints\n" + output, hypyt[i], ppt[i].pit);

        }
    }

    private void eiAinaSamaaTulosta(ArrayList<SkiJumpingTest.PitPist> pitPist1, ArrayList<SkiJumpingTest.PitPist> pitPist2, String out) {
        SkiJumpingTest.PitPist pp1 = pitPist1.get(0);
        SkiJumpingTest.PitPist pp2 = pitPist2.get(0);

        boolean sama = true;
        for (int i = 0; i < 5; i++) {
            if (pp1.pist[i] != pp2.pist[i]) {
                sama = false;
            }
        }

        assertFalse("Style points (judge votes) must be randomized!\n"
                + "Your program printed\n"
                + out, sama);

        sama = true;

        for (int i = 0; i < 5; i++) {
            if (pp1.pist[0] != pp1.pist[i]) {
                sama = false;
            }
        }

        assertFalse("Style points (judge votes) must be randomized!!\n"
                + "Your program printed\n"
                + out, sama);

        sama = true;

        for (int i = 0; i < 5; i++) {
            if (pp2.pist[0] != pp2.pist[i]) {
                sama = false;
            }
        }

        assertFalse("Style points (judge votes) must be randomized!!\n"
                + "Your program printed\n"
                + out, sama);
    }

    private void oikeaHyppyjarjestys(String output, ArrayList<SkiJumpingTest.PitPist> pitPist) {
        output = output.substring(output.indexOf("Jumping order") + "Jumps".length());
        output = output.substring(output.indexOf("Jumping order"));

        hyppyJarjestysKunnossa(output, pitPist);
    }

    private String haeRiviJolla(String output, String teksti) {
        String[] rivit = output.split("\n");
        for (String rivi : rivit) {
            if (rivi.contains(teksti)) {
                return rivi;
            }
        }


        return null;
    }

    private void eiSamaaPituuttaKaikilla(ArrayList<SkiJumpingTest.PitPist> pitPist, String tulos) {
        int[] pituudet = new int[pitPist.size()];

        int i = 0;
        for (SkiJumpingTest.PitPist pp : pitPist) {
            pituudet[i++] = pp.pit;
        }

        for (int j = 0; j < pituudet.length; j++) {
            assertTrue("Jump length had to be in range of 60-120\n"
                    + "Your program printed:\n", pituudet[j] > 59 && pituudet[j] < 121);
        }

        boolean sama = true;

        for (int j = 0; j < pituudet.length; j++) {
            if (pituudet[0] != pituudet[j]) {
                sama = false;
            }
        }

        assertFalse("Jump lengths must be randomized!\n"
                + "Your program printed\n"
                + tulos, sama);
    }

    class PitPist {

        String nimi;
        int pit;
        int[] pist;

        public PitPist(String nimi, int pit, int[] pist) {
            this.nimi = nimi;
            this.pit = pit;
            this.pist = pist;
        }

        @Override
        public String toString() {
            return nimi + " pit: " + pit + " pist: " + Arrays.toString(pist);

        }
    }

    private ArrayList<SkiJumpingTest.PitPist> lengthJaPisteet(String kierros, int k) {
        ArrayList<SkiJumpingTest.PitPist> pp = new ArrayList<SkiJumpingTest.PitPist>();
        String[] rivit = kierros.split("\n");

        int r = 0;

        while (rivit[r].isEmpty()) {
            r++;
        }

        while (true) {

            String nimi = "";
            try {
                nimi = rivit[r].trim();
                r++;
                while (rivit[r].isEmpty()) {
                    r++;
                }

            } catch (Exception n) {
                fail("there were problems printing the results on round " + k
                        + " program printed\n" + kierros);
            }

            int pit = -1;

            String pitMj = "";
            try {
                pitMj = (rivit[r].substring(rivit[r].indexOf("length:") + "length:".length())).trim();
                pit = Integer.parseInt(pitMj);
            } catch (Exception n) {
                fail("Printing the jump length of jumper " + nimi + " was problematic on round " + k
                        + " program printed\n" + kierros);
            }

            try {
                r++;
                while (rivit[r].isEmpty()) {
                    r++;
                }
            } catch (Exception n) {
                fail("there were problems printing the results on round " + k
                        + " program printed\n" + kierros);
            }

            String pisteet = "";
            try {
                pisteet = rivit[r].replaceAll("\\D", " ").trim();
            } catch (Exception e) {
                fail("Printing the style points of jumper " + nimi + " was problematic on round " + k
                        + " program printed\n" + kierros);
            }

            assertTrue("Printing the style points of jumper " + nimi + " was problematic on round " + k
                    + " program printed\n" + kierros, viisiLukua(pisteet));
            Scanner luvut = new Scanner(pisteet);
            int[] pisTau = {0, 0, 0, 0, 0};
            try {
                pisTau[0] = luvut.nextInt();
                pisTau[1] = luvut.nextInt();
                pisTau[2] = luvut.nextInt();
                pisTau[3] = luvut.nextInt();
                pisTau[4] = luvut.nextInt();
            } catch (Exception e) {
                fail("Printing the style points of jumper " + nimi + " was problematic on round " + k
                        + " program printed\n" + kierros);
            }

            pp.add(new SkiJumpingTest.PitPist(nimi, pit, pisTau));

            r++;

            try {
                while (rivit[r].isEmpty()) {
                    r++;
                }
            } catch (Exception e) {
                break;
            }

            if (r > rivit.length - 2) {
                break;
            }

        }

        return pp;
    }
}
