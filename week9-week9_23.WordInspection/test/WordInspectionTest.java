
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WordInspectionTest {

    Class sanaLuokka;
    private final String ENCODINGPROBLEMS = "Mac and Windows users may have trouble with words containing characters ä and ö.\n"
            + "In this case create Scanner as follows: Scanner reader = new Scanner(file, \"UTF-8\");";
    String klassName = "wordinspection.WordInspection";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setUp() {
        classRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + s(klassName) + " {...\n}", classRef.isPublic());

        try {
            sanaLuokka = ReflectionUtils.findClass("wordinspection.WordInspection");
            assertNotNull(sanaLuokka);
        } catch (Exception e) {
            fail("Have you created class WordInspection inside the package wordinspection?");
        } catch (Throwable t) {
            fail("Have you created class \"WordInspection\" inside the package wordinspection?");
        }
    }

    @Test
    @Points("23.1")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 20, "");
    }

    @Test
    @Points("23.1")
    public void hasConstructor() throws Throwable {
        Reflex.MethodRef1<Object, Object, File> ctor = classRef.constructor().taking(File.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "(File file)", ctor.isPublic());
        String v = "error caused by code new WordInspection( new File(\"src/shortList.txt\") );\n";
        ctor.withNiceError(v).invoke(new File("shortList.txt"));
    }

    public Object luo(File file) throws Throwable {
        return classRef.constructor().taking(File.class).invoke(file);
    }

    @Test
    @Points("23.1")
    public void hasMethodWordCount() throws Throwable {
        Object o = luo(new File("shortList.txt"));


        assertTrue("Create method public int wordCount() for class WordInspection", classRef.method(o, "wordCount").returning(int.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.wordCount();";

        assertEquals(k, 24, (int) classRef.method(o, "wordCount").returning(int.class).takingNoParams().withNiceError(k).invoke());
    }

    @Test
    @Points("23.1")
    public void smallFile() throws Throwable {

        File eka;
        eka = File.createTempFile("eka", "txt");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(eka), "UTF-8");
        out.append("sana1\nsana2\nsana3\nsana4\nsana5\n");
        out.flush();
        out.close();

        Object o = luo(eka);

        assertTrue("Create method public int wordCount() for class WordInspection", classRef.method(o, "wordCount").returning(int.class).takingNoParams().isPublic());

        String k = "File content:\n"
                + "sana1\nsana2\nsana3\nsana4\nsana5"
                + "\ns.wordCount();";

        assertEquals(k, 5, (int) classRef.method(o, "wordCount").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    @Test
    @Points("23.1")
    public void biggerFile() throws Throwable {

        File eka;
        eka = File.createTempFile("eka", "txt");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(eka), "UTF-8");
        out.append("sana1\nsana2\nsana3\nsana4\nsana5\nlisäsana\n");
        out.flush();
        out.close();

        Object o = luo(eka);

        assertTrue("Create method public int wordCount() for class WordInspection", classRef.method(o, "wordCount").returning(int.class).takingNoParams().isPublic());

        String k = "File content:\n"
                + "sana1\nsana2\nsana3\nsana4\nsana5\nlisäsana\n"
                + "\ns.wordCount();";

        assertEquals(k, 6, (int) classRef.method(o, "wordCount").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    @Test
    @Points("23.1")
    public void wordCountBigList() throws Throwable {
        Object o = luo(new File("wordList.txt"));

        assertTrue("Create method public int wordCount() for class WordInspection", classRef.method(o, "wordCount").returning(int.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/wordList.txt\") );\n"
                + "s.wordCount();";

        assertEquals(k, 91591, (int) classRef.method(o, "wordCount").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    /*
     *
     */
    @Test
    @Points("23.2")
    public void hasMethodZZZ() throws Throwable {
        Object o = luo(new File("shortList.txt"));

        assertTrue("Create method public List<String> wordsContainingZ() for class WordInspection", classRef.method(o, "wordsContainingZ").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.wordsContainingZ();";

        classRef.method(o, "wordsContainingZ").returning(List.class).takingNoParams().withNiceError(k).invoke();
    }

    @Test
    @Points("23.2")
    public void wordsStartingWithZSmallList() throws Throwable {
        Object o = luo(new File("shortList.txt"));

        assertTrue("Create method public List<String> wordsContainingZ() for class WordInspection", classRef.method(o, "wordsContainingZ").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.wordsContainingZ();";

        List<String> zeta = (List<String>) classRef.method(o, "wordsContainingZ").returning(List.class)
                .takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        List<String> odotettu = new ArrayList<String>();
        Collections.addAll(odotettu, "appenzeller", "appenzellerjuusto", "gorgonzola", "gorgonzolajuusto");

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 4, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    @Test
    @Points("23.2")
    public void zTest() throws Exception, Throwable {
        FileWriter n;
        File eka = File.createTempFile("eka", "txt");
        n = new FileWriter(eka);
        n.append("jazz\nzombi\nbomb\npalmu\nzzz\nunilla\nzoo\n");
        n.flush();
        n.close();

        ArrayList<String> odotettu = new ArrayList<String>() {
            {
                add("jazz");
                add("zombi");
                add("zoo");
                add("zzz");
            }
        };

        Object o = luo(eka);

        String k = "File content:\n"
                + "jazz\nzombi\nbomb\npalmu\nzzz\nunilla\nzoo\n"
                + "\ns.wordCount();";

        List<String> zeta = (List<String>) classRef.method(o, "wordsContainingZ").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 4, zeta.size());


        k = "File content:\n"
                + "jazz\nzombi\nbomb\npalmu\nzzz\nunilla\nzoo\n"
                + "\ns.wordCount();\n"
                + "you returned: " + zeta;

        assertEquals(k, odotettu, zeta);
    }

    @Test
    @Points("23.2")
    public void wordsContainingZList() throws Throwable {
        Object o = luo(new File("wordList.txt"));

        assertTrue("Create method public List<String> wordsContainingZ() for class WordInspection", classRef.method(o, "wordsContainingZ").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/wordList.txt\") );\n"
                + "s.wordsContainingZ();";

        List<String> zeta = (List<String>) classRef.method(o, "wordsContainingZ").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        List<String> odotettu = new ArrayList<String>() {
            {
                add("appenzeller");
                add("appenzellerjuusto");
                add("azeri");
                add("blazer");
                add("buzuki");
                add("gorgonzola");
                add("gorgonzolajuusto");
                add("gorgonzolanjuusto");
                add("intermezzo");
                add("jazz");
                add("jazzfestivaali");
                add("jazzklubi");
                add("jazzlaulaja");
                add("jazzmessu");
                add("jazzmusiikki");
                add("jazzmuusikko");
                add("jazzorkesteri");
                add("jazztanssi");
                add("kamikazelentäjä");
                add("mezzoforte");
                add("mezzopiano");
                add("mezzosopraano");
                add("mezzotinto");
                add("nizzansalaatti");
                add("ouzo");
                add("paparazzi");
                add("pizza");
                add("pizzeria");
                add("pizzicato");
                add("puzzle");
                add("puzzlepeli");
                add("scherzo");
                add("zambo");
                add("zen");
                add("zenbuddhalaisuus");
                add("zeniitti");
                add("zeppeliini");
                add("zirkoni");
                add("zirkonium");
                add("zloty");
                add("zombi");
                add("zombie");
                add("zoologi");
                add("zoologia");
                add("zoologinen");
                add("zoomata");
                add("zoomaus");
                add("zoomi");
                add("zoonoosi");
                add("zucchini");
                add("zulu");
            }
        };

        Collections.sort(zeta);
        assertEquals(ENCODINGPROBLEMS+"\n"+k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 51, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    /*
     *
     */
    @Test
    @Points("23.3")
    public void hasMethodWordsEndingInL() throws Throwable {
        Object o = luo(new File("shortList.txt"));

        assertTrue("Create method public List<String> wordsEndingInL()", classRef.method(o, "wordsEndingInL").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.wordsEndingInL();";

        classRef.method(o, "wordsEndingInL").returning(List.class).takingNoParams().withNiceError(k).invoke();
    }

    @Test
    @Points("23.3")
    public void wordsEndingInLSmallList() throws Throwable {
        Object o = luo(new File("shortList.txt"));

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.wordsEndingInL();";

        List<String> zeta = (List<String>) classRef.method(o, "wordsEndingInL").returning(List.class)
                .takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        List<String> odotettu = new ArrayList<String>();
        Collections.addAll(odotettu, "askel", "kennel", "petkel");

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 3, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    @Test
    @Points("23.3")
    public void lTest() throws Exception, Throwable {
        FileWriter n;
        File eka = File.createTempFile("eka", "txt");
        n = new FileWriter(eka);
        n.append("kannel\ntalo\nsammal\nlol\nhoh\njoo\nlossi\nl\n");
        n.flush();
        n.close();

        String lisa = "Are you adding words to ArrayList which the method returns?";
        ArrayList<String> odotettu = new ArrayList<String>() {
            {
                add("kannel");
                add("l");
                add("lol");
                add("sammal");
            }
        };

        Object o = luo(eka);

        String k = "File content:\n"
                + "kannel\ntalo\nsammal\nlol\nhoh\njoo\nlossi\nl\n"
                + "\ns.wordsEndingInL();";

        List<String> zeta = (List<String>) classRef.method(o, "wordsEndingInL").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 4, zeta.size());
        assertEquals(k, odotettu, zeta);

    }

    @Test
    @Points("23.3")
    public void lTestModel() throws Throwable {
        Object o = luo(new File("wordList.txt"));

        assertTrue("Create method public List<String> wordsEndingInL() for class WordInspection", classRef.method(o, "wordsEndingInL").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/wordList.txt\") );\n"
                + "s.wordsEndingInL();";

        List<String> zeta = (List<String>) classRef.method(o, "wordsEndingInL").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        List<String> odotettu = new ArrayList<String>() {
            {
                add("alkucocktail");
                add("alkutaival");
                add("askel");
                add("baseball");
                add("becquerel");
                add("cocktail");
                add("diesel");
                add("edistysaskel");
                add("elontaival");
                add("elämäntaival");  //
                add("emmental");
                add("erämaataival");  //
                add("goodwill");
                add("gospel");
                add("hajasävel");     //
                add("harmosammal");
                add("havusammal");
                add("hetesammal");
                add("huippusävel");   //
                add("hyppyaskel");
                add("hölkkäaskel");   //
                add("ilonkyynel");
                add("iskusävel");     //
                add("jouhikannel");
                add("juoksuaskel");
                add("kajal");
                add("kannel");
                add("kardaaninivel");
                add("karhunsammal");
                add("karstasammal");
                add("kennel");
                add("ketjuommel");
                add("keuhkosammal");
                add("kiertonivel");
                add("kilpisammal");
                add("kinnernivel");
                add("kivisammal");
                add("kokoaskel");
                add("kokosävelaskel");    //
                add("korallisammal");
                add("koristeommel");
                add("korpitaival");
                add("koruommel");
                add("korusävel");   //
                add("kukonaskel");
                add("kulosammal");
                add("kynsisammal");
                add("kyynel");
                add("kyynärnivel");   //
                add("kärkinivel");   //
                add("kävelyaskel");  //
                add("laakasammal");
                add("lahosammal");
                add("lehtisammal");
                add("leposävel");   //
                add("leukanivel");
                add("liekosammal");
                add("lomasävel");
                add("lonkkanivel");
                add("lopputaival");
                add("maksasammal");
                add("metsätaival");   //
                add("mosel");
                add("murrosnivel");
                add("nilkkanivel");
                add("nivel");
                add("normaalisävel");  //
                add("nukkasammal");
                add("nuotiosammal");
                add("näkinsammal");   //
                add("olkanivel");
                add("ommel");
                add("osasävel");    //
                add("pallonivel");
                add("pascal");
                add("perussävel");  //
                add("petkel");
                add("pikataival");
                add("pohjasävel"); //
                add("polvinivel");
                add("puoliaskel");
                add("puolisävelaskel");  //
                add("rahkasammal");
                add("rannenivel");
                add("reikäommel");  //
                add("reunaommel");
                add("rial");
                add("ristiaskel");
                add("ristinivel");
                add("ruskosammal");
                add("sammal");
                add("seinäsammal");  //
                add("seppel");
                add("sial");
                add("siirtymätaival");   //
                add("siksakkiommel");
                add("sisal");
                add("sivuaskel");
                add("skool");
                add("sorminivel");
                add("soul");
                add("suikerosammal");
                add("suoraommel");
                add("surunkyynel");
                add("sävel");   //
                add("sävelaskel");  //
                add("taival");
                add("tanssiaskel");
                add("tappinivel");
                add("trial");
                add("tunnussävel");  //
                add("tuulikannel");
                add("vaihtoaskel");
                add("valenivel");
                add("vapaataival");
                add("varvasaskel");
                add("varvasnivel");
                add("vauhtiaskel");
                add("vemmel");
                add("virrensävel");  //
                add("virsikannel");
                add("yläsävel");  //
            }
        };

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 122, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    /*
     *
     */
    @Test
    @Points("23.4")
    public void hasMethodPalindromes() throws Throwable {
        Object o = luo(new File("shortList.txt"));

        assertTrue("Create method public List<String> palindromes() for class WordInspection", classRef.method(o, "palindromes").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.palindromes();";

        classRef.method(o, "palindromes").returning(List.class).takingNoParams().withNiceError(k).invoke();
    }

    @Test
    @Points("23.4")
    public void palindromesSmallList() throws Throwable {
        Object o = luo(new File("shortList.txt"));

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.palindromes();";

        List<String> zeta = (List<String>) classRef.method(o, "palindromes").returning(List.class)
                .takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        List<String> odotettu = new ArrayList<String>();
        Collections.addAll(odotettu, "autioitua", "suuruus", "utu");

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 3, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    @Test
    @Points("23.4")
    public void palindromesTest() throws Exception, Throwable {
        FileWriter n;
        File eka = File.createTempFile("eka", "txt");
        n = new FileWriter(eka);
        n.append("hissi\na\nb\nlol\nsaippuakauppias\nsiilo\nabba\n");
        n.flush();
        n.close();

        ArrayList<String> odotettu = new ArrayList<String>() {
            {
                add("a");
                add("abba");
                add("b");
                add("lol");
                add("saippuakauppias");
            }
        };

        Object o = luo(eka);

        String k = "File content:\n"
                + "hissi\na\nb\nlol\nsaippuakauppias\nsiilo\nabba\n"
                + "\ns.palindromes();";

        List<String> zeta = (List<String>) classRef.method(o, "palindromes").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 5, zeta.size());
        assertEquals(k, odotettu, zeta);

    }

    @Test
    @Points("23.4")
    public void palindromesWordList() throws Throwable {
        Object o = luo(new File("wordList.txt"));

        assertTrue("Create method public List<String> wordsContainingZ() for class WordInspection", classRef.method(o, "palindromes").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/wordList.txt\") );\n"
                + "s.palindromes();";

        List<String> zeta = (List<String>) classRef.method(o, "palindromes").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        List<String> odotettu = new ArrayList<String>() {
            {
                add("ajaja");
                add("akka");
                add("ala");
                add("alla");
                add("autioitua");
                add("ele");
                add("enne");
                add("hah");
                add("heh");
                add("huh");
                add("hyh");
                add("häh");       //
                add("imaami");
                add("isi");
                add("niin");
                add("oho");
                add("olo");
                add("opo");
                add("otto");
                add("piip");
                add("pop");
                add("sadas");
                add("sammas");
                add("sees");
                add("siis");
                add("sus");
                add("suuruus");
                add("sylys");
                add("sytytys");
                add("syys");
                add("syöppöys");   //
                add("tuut");
                add("tyyt");
                add("tööt");     //
                add("utu");
                add("yty");
                add("älä");     //
                add("ämmä");  //
                add("ässä");   //
            }
        };

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 39, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    /*
     *
     */
    @Test
    @Points("23.5")
    public void hasMethodWordsWhichContainAllVowels() throws Throwable {
        Object o = luo(new File("shortList.txt"));

        assertTrue("Create method public List<String> wordsWhichContainAllVowels()", classRef.method(o, "wordsWhichContainAllVowels").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.wordsWhichContainAllVowels();";

        classRef.method(o, "wordsWhichContainAllVowels").returning(List.class).takingNoParams().withNiceError(k).invoke();
    }

    @Test
    @Points("23.5")
    public void wordsWhichContainAllVowelsSmallList() throws Throwable {
        Object o = luo(new File("shortList.txt"));

        String k = "WordInspection s = new WordInspection( new File(\"src/shortList.txt\") );\n"
                + "s.wordsWhichContainAllVowels();";

        List<String> zeta = (List<String>) classRef.method(o, "wordsWhichContainAllVowels").returning(List.class)
                .takingNoParams().withNiceError(k).invoke();
        assertFalse(k + "\nReturned list was null.", zeta == null);

        List<String> odotettu = new ArrayList<String>();
        Collections.addAll(odotettu, "juustohöyläperiaate", "valkokaulustyöläinen");

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 2, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

//    @Test
//    @Points("23.5")
//    public void wordsWhichContainAllVowelsTest() throws Exception, Throwable {
//        FileWriter n;
//        File eka = File.createTempFile("eka", "txt");
//        n = new FileWriter(eka);
//        n.append("myöhäiselokuva\nb\nympäristöntuhoaja\nsiilo\nabba\naeiouyäö\n");
//        n.flush();
//        n.close();
//
//        String lisa = "Lisääthän sanat ArrayListiin, jonka metodi palauttaa?";
//        ArrayList<String> odotettu = new ArrayList<String>() {
//            {
//                add("aeiouyäö");
//                add("myöhäiselokuva");
//            }
//        };
//
//        Object o = luo(eka);
//
//        String k = "File content:\n"
//                + "myöhäiselokuva\nb\nympäristöntuhoaja\nsiilo\nabba\naeiouyäö\n"
//                + "\ns.palindromes();";
//
//        List<String> zeta = (List<String>) classRef.method(o, "wordsWhichContainAllVowels").returning(List.class).takingNoParams().withNiceError(k).invoke();
//
//        assertFalse(k + "\nReturned list was null.", zeta == null);
//
//        Collections.sort(zeta);
//        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 2, zeta.size());
//        assertEquals(k, odotettu, zeta);
//
//    }

    @Test
    @Points("23.5")
    public void wordsWhichContainAllVowelsWordList() throws Throwable {
        Object o = luo(new File("wordList.txt"));

        assertTrue("Create method public List<String> wordsWhichContainAllVowels() for class WordInspection", classRef.method(o, "wordsWhichContainAllVowels").returning(List.class).takingNoParams().isPublic());

        String k = "WordInspection s = new WordInspection( new File(\"src/wordList.txt\") );\n"
                + "s.wordsWhichContainAllVowels();";

        List<String> zeta = (List<String>) classRef.method(o, "wordsWhichContainAllVowels").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nReturned list was null.", zeta == null);

        List<String> odotettu = new ArrayList<String>() {
            {
                add("arvostelukyvyttömästi");
                add("juustohöyläperiaate");
                add("kotitaloustyöntekijä");
                add("maataloustyöntekijä");
                add("myöhäiselokuva");
                add("suojatyöntekijä");
                add("taloustyöntekijä");
                add("ulkomaantyöntekijä");
                add("valkokaulustyöläinen");
                add("valkokaulustyöntekijä");
                add("ympäristönsuojelija");
            }
        };

        Collections.sort(zeta);
        assertEquals(k + "\nLength of the returned list was incorrect\nReturned list was: " + zeta, 11, zeta.size());
        assertEquals(k, odotettu, zeta);
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
