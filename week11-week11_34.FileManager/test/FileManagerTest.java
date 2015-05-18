
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileManagerTest {

    Random arpa = new Random();

    @Points("34.1")
    @Test
    public void readsLines1() throws FileNotFoundException {
        ArrayList<String> tekstit = new ArrayList<String>();
        tekstit.add("eka");
        tekstit.add("toka");
        rivienLuku(tekstit, "src/testinput1.txt", "test/tmp/testinput1.txt");
    }

    @Points("34.1")
    @Test
    public void readsLines2() throws FileNotFoundException {
        ArrayList<String> tekstit = new ArrayList<String>();
        tekstit.add("yy");
        tekstit.add("kaa");
        tekstit.add("koo nee vii");
        rivienLuku(tekstit, "src/testinput2.txt", "test/tmp/testinput2.txt");
    }

    @Points("34.2")
    @Test
    public void savesLine1() throws FileNotFoundException, IOException {
        rivinTallennus(new FileManager(), "eka koerivi");
    }

    @Points("34.2")
    @Test
    public void savesLine2() throws FileNotFoundException, IOException {
        rivinTallennus(new FileManager(), "toinen koerivi");
    }

    @Points("34.2")
    @Test
    public void savesLine3() throws FileNotFoundException, IOException {
        rivinTallennus(new FileManager(), "kolmas koerivi");
    }

    @Points("34.3")
    @Test
    public void savesLines1() throws FileNotFoundException, IOException {
        ArrayList<String> tekstit = new ArrayList<String>();
        tekstit.add("eins");
        tekstit.add("zwei");
        tekstit.add("drei");

        rivienTallennus(new FileManager(), tekstit);
    }

    @Points("34.3")
    @Test
    public void savesLines2() throws FileNotFoundException, IOException {
        ArrayList<String> tekstit = new ArrayList<String>();
        tekstit.add("yy");
        tekstit.add("kaa");
        tekstit.add("koo");
        tekstit.add("nee");
        tekstit.add("vii");

        rivienTallennus(new FileManager(), tekstit);
    }

    private List<String> lue(String tiedosto) throws FileNotFoundException {
        Scanner s = new Scanner(new File(tiedosto));
        ArrayList<String> rivit = new ArrayList<String>();

        while (s.hasNextLine()) {
            rivit.add(s.nextLine());
        }
        return rivit;
    }

    private void rivinTallennus(FileManager t, String teksti) throws IOException, FileNotFoundException {
        int arvottu = arpa.nextInt(100000);
        String tdsto = "test/tmp/tmp" + arvottu + ".txt";
        t.save(tdsto, teksti);

        File tied = new File(tdsto);

        String k = "FileManager t = new FileManager();\n"
                + "t.save(\"" + tdsto + "\"," + teksti + ");\n";

        assertTrue("File isn't created when executing code\n" + k, tied.exists());

        List<String> rivit = lue(tdsto);
        tied.delete();
        assertEquals("Incorrect number of lines in the created file when executing code\n"
                + k, 1, rivit.size());
        assertEquals("Content of the file isn't right when executing code\n"
                + k, teksti, rivit.get(0));

    }

    private void rivienTallennus(FileManager t, ArrayList<String> tekstit) throws IOException, FileNotFoundException {
        int arvottu = arpa.nextInt(100000);
        String tdsto = "test/tmp/tmp" + arvottu + ".txt";
        t.save(tdsto, tekstit);

        File tied = new File(tdsto);

        String k = "FileManager t = new FileManager();\n"
                + "List<String> tekstit = new ArrayList<String>();\n";

        for (String teksti : tekstit) {
            k += "tekstit.add(\"" + teksti + "\");\n";
        }

        k += "t.save(\"" + tdsto + "\",tekstit);\n";

        assertTrue("File isn't created when executing code\n" + k, tied.exists());

        List<String> rivit = lue(tdsto);
        tied.delete();

        assertEquals("Incorrect number of lines in the created file when executing code\n"
                + k, tekstit.size(), rivit.size());
        assertEquals("Content of the file isn't right when executing code\n"
                + k, tekstit, rivit);
    }

    private void rivienLuku(ArrayList<String> tekstit, String tiedosto, String td) throws FileNotFoundException {
        String sisalto = "";
        for (String teksti : tekstit) {
            sisalto += teksti + "\n";
        }

        FileManager t = new FileManager();
        List<String> luettu = t.read(td);

        String k = "FileManager t = new FileManager();\n"
                + "t.read(\"" + tiedosto + "\");";

        assertEquals("Content of file " + tiedosto + ":\n" + sisalto + "\n"
                + "Number of read lines was wrong with code\n"
                + k, tekstit.size(), luettu.size());

        assertEquals("Content of file " + tiedosto + ":\n" + sisalto + "\n"
                + "Returned list wrong with code\n"
                + k, tekstit, luettu);
    }
}
