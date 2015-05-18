package game;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import gameoflife.GameOfLifeBoard;
import java.lang.reflect.Constructor;
import static org.junit.Assert.*;
import org.junit.Test;

@Points("43.2")
public class Part2Test {

    @Test
    public void initialisationOneEveryoneAlive() {
        GameOfLifeBoard oa = luoAlusta(4, 5);
        try {
            oa.initiateRandomCells(1);
        } catch (Exception e) {
            fail(""
                    + "PersonalBoard board = new PersonalBoard(4,5);\n"
                    + "board.initiateRandomCells(1.0);\n"
                    + "caused an error: " + e);
        }
        assertEquals("When executing code\n"
                + "PersonalBoard board = new PersonalBoard(4,5);\n"
                + "board.initiateRandomCells(1.0);\n"
                + "Check that every cell is alive. Percentage of alive cells:"
                + "", 100, 100 * prosenttiaElossa(oa.getBoard()), 3);
    }

    @Test
    public void initialisationZeroEveryoneDead() {
        GameOfLifeBoard oa = luoAlusta(4, 5);
        try {
            oa.initiateRandomCells(0);
        } catch (Exception e) {
            fail(""
                    + "PersonalBoard board = new PersonalBoard(4,5);\n"
                    + "board.initiateRandomCells(0.0);\n"
                    + "caused an error: " + e);
        }
        assertEquals("When executing code\n"
                + "PersonalBoard board = new PersonalBoard(4,5);\n"
                + "board.initiateRandomCells(0.0);\n"
                + "Check that no cell is alive. Percentage of alive cells:"
                + "", 0, 100 * prosenttiaElossa(oa.getBoard()), 3);
    }

    @Test
    public void initialisationHalfNoZeroNoOne() {
        GameOfLifeBoard oa = luoAlusta(20, 20);
        try {
            oa.initiateRandomCells(0.5);
        } catch (Exception e) {
            fail(""
                    + "PersonalBoard board = new PersonalBoard(20,20);\n"
                    + "board.initiateRandomCells(0.5);\n"
                    + "caused an error: " + e);
        }
        assertEquals("When executing code\n"
                + "PersonalBoard board = new PersonalBoard(20,20);\n"
                + "board.initiateRandomCells(0.5);\n"
                + "Check that no cell is alive. Percentage of alive cells:"
                + "", 50, 100 * prosenttiaElossa(oa.getBoard()), 5);
    }

    @Test
    public void initialisationTwentyPercentageProbabilityCorrect() {
        GameOfLifeBoard oa = luoAlusta(20, 20);
        try {
            oa.initiateRandomCells(0.2);
        } catch (Exception e) {
            fail(""
                    + "PersonalBoard board = new PersonalBoard(20,20);\n"
                    + "board.initiateRandomCells(0.2);\n"
                    + "caused an error: " + e);
        }

        assertEquals("When executing code\n"
                + "PersonalBoard board = new PersonalBoard(20,20);\n"
                + "board.initiateRandomCells(0.2);\n"
                + "Check that no cell is alive. Percentage of alive cells:"
                + "", 20, 100 * prosenttiaElossa(oa.getBoard()), 5);
    }

    @Test
    public void initialisationEightyPercentageProbabilityCorrect() {

        GameOfLifeBoard oa = luoAlusta(20, 20);
        try {
            oa.initiateRandomCells(0.8);
        } catch (Exception e) {
            fail(""
                    + "PersonalBoard board = new PersonalBoard(20,20);\n"
                    + "board.initiateRandomCells(0.8);\n"
                    + "caused an error: " + e);
        }
        assertEquals("When executing code\n"
                + "PersonalBoard board = new PersonalBoard(20,20);\n"
                + "board.initiateRandomCells(0.8);\n"
                + "Check that no cell is alive. Percentage of alive cells:"
                + "", 80, 100 * prosenttiaElossa(oa.getBoard()), 5);
    }

    public static double prosenttiaElossa(boolean[][] matriisi) {
        int koko = matriisi.length * matriisi[0].length;

        int lkm = 0;
        for (boolean[] rivi : matriisi) {
            for (boolean alkio : rivi) {
                if (alkio) {
                    lkm++;
                }
            }
        }

        return 1.0 * lkm / koko;
    }

    private GameOfLifeBoard luoAlusta(int leveys, int korkeus) {
        Class board = ReflectionUtils.findClass("game.PersonalBoard");
        Constructor c = ReflectionUtils.requireConstructor(board, int.class, int.class);
        try {
            return (GameOfLifeBoard) ReflectionUtils.invokeConstructor(c, leveys, korkeus);
        } catch (Throwable ex) {
            fail("Does class PersonalBoard inside the package game have constructor public PersonalBoard(int width, int height) and is the class itself public? Does the class also inherit class GameOfLifeBoard?");
        }

        return null;
    }
}
