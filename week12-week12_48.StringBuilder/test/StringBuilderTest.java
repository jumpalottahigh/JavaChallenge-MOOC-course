
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("48")
public class StringBuilderTest {

    Reflex.ClassRef<Object> classRef;

    @Before
    public void setUp() {
        classRef = Reflex.reflect("Main");
    }

    @Test
    public void buildWorks() throws Throwable {
        int[][] tt = {
            {1, 2, 3, 4},
            {1, 2, 3},
            {1, 2},
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1}
        };
        String[] vv = {""
            + "{\n"
            + " 1, 2, 3, 4\n"
            + "}",
            ""
            + "{\n"
            + " 1, 2, 3\n"
            + "}",
            ""
            + "{\n"
            + " 1, 2\n"
            + "}",
            ""
            + "{\n"
            + " 1, 2, 3, 4,\n"
            + " 5\n"
            + "}",
            ""
            + "{\n"
            + " 1, 2, 3, 4,\n"
            + " 5, 6, 7, 8,\n"
            + " 9\n"
            + "}",
            ""
            + "{\n"
            + " 15, 14, 13, 12,\n"
            + " 11, 10, 9, 8,\n"
            + " 7, 6, 5, 4,\n"
            + " 3, 2, 1\n"
            + "}"};

        for (int i = 0; i < vv.length; i++) {
            testaa(vv[i].split("\n"), tt[i]);
        }
    }

    @Test
    public void usesStringBuilder() throws Throwable {
        int[] t = new int[20000];
        long aika = System.currentTimeMillis();
        build(t, "int{[ t = //size of the array "+t.length+"\n"
                + "build(t);");
        aika = System.currentTimeMillis() - aika;
        assertTrue("int{[ t = //size of the array "+t.length+"\n"
                + "build(t);\n"
                + "time passed "+aika+" milliseconds, that's too much\n"
                + "you didn't use StringBuilder or then you did something else which took too much time!",aika<500);
    }

    private String build(int[] t, String v) throws Throwable {
        return classRef.staticMethod("build").returning(String.class).taking(int[].class).withNiceError(v).invoke(t);
    }

    private void testaa(String[] odotettu, int[] t) throws Throwable {
        String v = "int[] t = " + Arrays.toString(t).replace('[', '{').replace(']', '}')
                + "\nbuild(t);\n";
        String tt = build(t, v);
        String[] result = tt.split("\n");
        assertEquals("wrong number of lines\n" + v + "result:\n" + tt + "\n", odotettu.length, result.length);
        assertEquals("last line should only contain }\n" + v + "result:\n" + tt + "\n", "}", result[result.length - 1].replaceAll(" ", ""));
        assertEquals("first line should only contain {\n" + v + "result:\n" + tt + "\n", "{", result[0].replaceAll(" ", ""));
        for (int i = 1; i < result.length - 1; i++) {
            String pitas = odotettu[i].replaceAll("\n", "");
            assertTrue((i + 1) + ". line should have one whitespace at the beginning\n" + v + "result:\n" + tt + "\n", result[i].startsWith(" "));
            assertEquals((i + 1) + ". line's number of commas is wrong\n" + v + "result:\n" + tt + "\n", pilkut(pitas), pilkut(result[i]));
            assertTrue((i + 1) + ". line should be " + pitas + "\n" + v + "result:\n" + tt + "\n", result[i].startsWith(pitas));
            assertTrue(result[i]+ "should be "+pitas , result[i].replaceAll(" ", "").equals(pitas.replaceAll(" ", "")));
        }

        assertEquals("last line should only contain }\n" + v + "result:\n" + tt + "\n", "}", result[result.length - 1].replaceAll(" ", ""));
    }

    private int pilkut(String rivi) {
        int p = 0;
        for (int i = 0; i < rivi.length(); i++) {
            if (rivi.charAt(i) == ',') {
                p++;
            }
        }
        return p;
    }
}
