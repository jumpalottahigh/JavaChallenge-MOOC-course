
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.ClassRef;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef0;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef5;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@Points("33.1 33.2 33.3 33.4")
public class DungeonTest {

    ClassRef dungeon;
    MethodRef5<Void, Object, Integer, Integer, Integer, Integer, Boolean> cons;
    MethodRef0<Object, Void> run;

    @Before
    public void hae() {
        dungeon = Reflex.reflect("dungeon.Dungeon");
        cons = dungeon.constructor().taking(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE);
        assertTrue("Create for class Dungeon a constructor public Dungeon(int length, int height, int vampires, int moves, boolean vampiresMove)", cons.isPublic());
        run = dungeon.method("run").returningVoid().takingNoParams();
        assertTrue("Create method public void run() for class Dungeon", run.isPublic());
    }

    class P implements Runnable {

        Object dungeon;
        PipedOutputStream toOut, fromOut;
        PipedInputStream toIn, fromIn;
        PrintStream toOut2;
        Scanner fromIn2;

        public P(int x, int y, int hirv, int siirt, boolean b) throws Throwable {
            toIn = new PipedInputStream();
            toOut = new PipedOutputStream(toIn);
            toOut2 = new PrintStream(toOut);

            fromIn = new PipedInputStream();
            //fromIn2 = new Scanner(fromIn);
            fromOut = new PipedOutputStream(fromIn);

            System.setIn(toIn);
            System.setOut(new PrintStream(fromOut));

            dungeon = cons.invoke(x, y, hirv, siirt, b);

        }
        public volatile boolean running;

        @Override
        public void run() {
            try {
                run.withNiceError().invokeOn(dungeon);
            } catch (NoSuchElementException e) {
                // ignore
            } catch (Throwable ex) {
                throw new Error("Something went wrong:", ex);
            }
        }

        public void write(String s) {
            toOut2.print(s);
            toOut2.flush();
        }

        public String read() throws IOException {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            String out = "";
            while (fromIn.available() > 0) {
                out += (char) fromIn.read();
            }
            return out;
        }
    }

    @Test
    public void testFirstPrintOutput() throws Throwable {
        DungeonTest.P p = null;
        String viesti = "Tested Dungeon dungeon = new Dungeon(5,5,3,3,false); dungeon.run();\n";

        try {
            p = new DungeonTest.P(5, 5, 3, 3, false);
        } catch (Throwable t) {
            fail(viesti+"Additional information about the error: "+t);
        }

        Thread t = new Thread(p);
        t.start();

        String s = p.read();
        t.interrupt();
        s = s.replaceAll("\r\n", "\n");
        s = s.replaceAll("\r", "\n");

        String[] rivit = s.split("\n");
        assertTrue(viesti + "There should be at least 3 printed lines. Your print output was:\n" + s,
                rivit.length >= 3);

        assertEquals(viesti + "You didn't print number of moves in the first line. Your print output was:\n" + s,
                "3",
                rivit[0].trim());
        assertTrue(viesti + "Second line should be empty. Your print output was: \n" + s,
                "".equals(rivit[1].trim()));
        assertEquals(viesti + "You didn't print the coordinates of the player. Your print output was:\n" + s,
                "@ 0 0",
                rivit[2].trim());

        assertTrue(viesti + "There should be at least 5 printed lines. Your print output was:\n" + s,
                rivit.length >= 5);
        for (int i = 1; i <= 3; i++) {
            if (!rivit[2 + i].startsWith("v")) {
                fail(viesti + "You didn't print vampire line. Incorrect line is:\n" + rivit[2 + i] + "\nYour whole print output was:\n" + s);
            }
        }
        assertTrue(viesti + "There should be at least 11 printed lines. Your print output was:\n" + s,
                rivit.length >=11);
        assertTrue(viesti + "Before printing the dungeon there should be one empty line. Your print output was: \n" + s,
                "".equals(rivit[6].trim()));
        for (int i = 1; i <= 5; i++) {
            if (rivit[6 + i].length() != 5) {
                fail(viesti + "Dungeon line has wrong length. Incorrect line is:\n" + rivit[6 + i] + "\nYour whole print output was:\n" + s);
            }
        }
    }

    @Test
    public void testDefeat() throws Throwable {
        DungeonTest.P p = new DungeonTest.P(5, 5, 5, 5, true);
        p.write("w\nw\nw\nw\nw\n");
        String viesti = "Tested Dungeon dungeon = new Dungeon(5,5,5,5,false); dungeon.run();\n";
        Thread t = new Thread(p);
        t.start();

        String s = p.read();

        assertTrue(viesti + "Game should end in defeat when moving 5 times! Your print output was:\n" + s,
                s.contains("LOSE"));

    }

    @Test
    public void testVictory() throws Throwable {
        DungeonTest.P p = new DungeonTest.P(4, 4, 1, 100, false);
        p.write("s\ns\ns\nd\nw\nw\nw\nd\ns\ns\ns\nd\nw\nw\nw\n");
        String syote = "s s s d w w w d s s s d w w w";
        String viesti = "Tested Dungeon dungeon = new Dungeon(4,4,1,100,false); dungeon.run();\n";
        Thread t = new Thread(p);
        t.start();

        String s = p.read();

        assertTrue(viesti + "Game should end in victory when visiting all panels! \n"
                + "\nWhen input was "+syote
                + "\nYour print output was:\n" + s,
                s.contains("WIN"));
    }

    @Test
    public void testThatLampsDecreaseWhenCheckingSituation() throws Throwable {
        DungeonTest.P p = new DungeonTest.P(4, 4, 2, 100, false);
        p.write("swswswswswsw\ns\ns\ns\nd\nw\nw\nw\nd\ns\ns\ns\nd\nw\nw\nw\n");
        String viesti = "Tested Dungeon dungeon = new Dungeon(4,4,1,100,false); dungeon.run();\n";

        Thread t = new Thread(p);
        t.start();

        String s = p.read();

        assertTrue(viesti + "Blinkings of the lamp should decrease by one per one turn. Player can walk around in the darkness as much as he wants without the lamp battery going decreasing.",
                containsInOrder(s, "99", "98", "97"));
    }

    private boolean containsInOrder(String data, String... args) {
        int lastIndex = -1;
        for (String arg : args) {
            if (!data.contains(arg)) {
                return false;
            }

            if (data.indexOf(arg) <= lastIndex) {
                return false;
            }

            lastIndex = data.indexOf(arg);
        }

        return true;
    }

    @Test
    public void testTurn() throws Throwable {

        DungeonTest.P p = new DungeonTest.P(10, 10, 5, 100, true);

        String viesti = "Tested Dungeon dungeon = new Dungeon(10,10,5,100,true); dungeon.run();\n";
        String viesti2 = "Your print output was:\n";
        Thread t = new Thread(p);
        t.start();

        String eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        String[] ekat = eka.split("\n");
        assertEquals(viesti + "In the beginning the number of turns should be 100. " + viesti2 + eka,
                "100",
                ekat[0].trim());

        p.write("s\n");

        String toka = p.read();
        toka = toka.replaceAll("\r\n", "\n");
        toka = toka.replaceAll("\r", "\n");
        String[] tokat = toka.split("\n");
        assertEquals(viesti + "After one turn the number of turns should be 99. " + viesti2 + toka,
                "99",
                tokat[0].trim());
        assertEquals(viesti + "Player should move downward when giving command s. " + viesti2 + toka,
                "@ 0 1",
                tokat[2].trim());

        boolean liik = false;
        for (int i = 1; i < 4; i++) {
            if (!ekat[2 + i].equals(tokat[2 + i])) {
                liik = true;
            }
        }
        assertTrue(viesti + "No vampire moved! " + viesti2 + eka + toka,
                liik);

    }

    @Test
    public void testMoving() throws Throwable {
        DungeonTest.P p = new DungeonTest.P(10, 10, 5, 100, false);
        String viesti = "Tested Dungeon dungeon = new Dungeon(10,10,5,100,false); dungeon.run();\n";
        String viesti2 = "Your print output was:\n";
        Thread t = new Thread(p);
        t.start();

        String eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        String[] ekat = eka.split("\n");

        p.write("s\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Player should move downward when giving command s. " + viesti2 + eka,
                "@ 0 1",
                ekat[2].trim());

        p.write("dd\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Player should move two times to the right when giving command dd. " + viesti2 + eka,
                "@ 2 1",
                ekat[2].trim());
        eka = p.read();

        p.write("w\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Player should move up when giving command w. " + viesti2 + eka,
                "@ 2 0",
                ekat[2].trim());

        p.write("a\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Player should move left when giving command a. " + viesti2 + eka,
                "@ 1 0",
                ekat[2].trim());


        p.write("ssssssddddsdsdsdsdsdsdsdsdssssdddssssdddssdsdsdsdsdsdsdsd\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Player's movement should stop when player faces a wall of the dungeon." + viesti2 + eka,
                "@ 9 9",
                ekat[2].trim());

    }

    @Test
    public void testVampireDontMove() throws Throwable {
        DungeonTest.P p = new DungeonTest.P(3, 3, 1, 20, false);
        String viesti = "Tested Dungeon dungeon = new Dungeon(3,3,1,20,false); dungeon.run();\n";
        Thread t = new Thread(p);
        t.start();

        p.write("w\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\n");

        String eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        String[] ekat = eka.split("\n");
        String prev = null;
        for (String row : ekat) {
            if(!row.matches("v\\s+\\d+\\s+\\d+"))continue;
            System.out.println(row);
            if(prev == null){
                prev = row;
                continue;
            }
            if(!prev.equals(row))fail(viesti + "Vampires should not move!");
            prev = row;
        }
    }

    @Test
    public void testVampireMove() throws Throwable {
        DungeonTest.P p = new DungeonTest.P(3, 3, 1, 20, true);
        String viesti = "Tested Dungeon dungeon = new Dungeon(3,3,1,20,true); dungeon.run();\n";
        Thread t = new Thread(p);
        t.start();

        p.write("w\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\n");

        String eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        String[] ekat = eka.split("\n");
        String prev = null;
        for (String row : ekat) {
            if(!row.matches("v\\s+\\d+\\s+\\d+"))continue;
            System.out.println(row);
            if(prev == null){
                prev = row;
                continue;
            }
            if(!prev.equals(row))return;
            prev = row;
        }

        fail(viesti + "Vampires should move!");
    }

}
