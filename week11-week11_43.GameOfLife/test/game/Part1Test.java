package game;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import gameoflife.GameOfLifeBoard;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import org.junit.Test;

@Points("43.1")
public class Part1Test {

    @Test
    public void classPersonalBoardExists() {
        ReflectionUtils.findClass("game.PersonalBoard");
    }

    @Test
    public void initialisationEveryoneDead() throws IllegalArgumentException, IllegalAccessException {
        GameOfLifeBoard board = luoAlusta(5, 5);

        eiLokaaliaTaulukkoa(board);

        for (boolean[] rivi : board.getBoard()) {
            for (boolean solu : rivi) {
                if (solu) {
                    fail("Don't change PersonalBoard's constructor. Cells should be dead at first.");
                }
            }
        }
    }

    @Test
    public void turnToLivingWorks() {
        GameOfLifeBoard board = luoAlusta(5, 5);
        boolean[][] taul = board.getBoard();

        assertFalse("Check that PersonalBoard's constructor initializes cells to be dead at first.", taul[3][3]);
        try {
            board.turnToLiving(3, 3);
        } catch (Exception e) {
            fail("Error when executing code\n"
                    + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                    + "oa.turnToLiving(3,3)\n"
                    + "additional information " + e);
        }
        assertEquals("Check that method turnToLiving works, i.e. it assigns the value true to the cell.\n"
                + "When executing the following code, that didn't happen:\n"
                + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                + "oa.turnToLiving(3,3)\n"
                + "oa.isAlive(3,3)\n"
                + "", true, taul[3][3]);
    }

    @Test
    public void turnToLivingFirstXThenY() {
        GameOfLifeBoard board = luoAlusta(5, 5);
        boolean[][] taul = board.getBoard();

        assertFalse("Check that PersonalBoard's constructor initializes cells to be dead at first.", taul[3][1]);
        board.turnToLiving(3, 1);
        assertTrue("Check that board's array is used always in the format of [x][y].", taul[3][1]);
    }

    @Test
    public void turnToLivingDoesNothingIfCoordinatesOutsideTheBoard1() {
        GameOfLifeBoard board = luoAlusta(5, 5);
        boolean[][] taul = board.getBoard();

        assertFalse("Check that PersonalBoard's constructor initializes cells to be dead at first.", taul[3][3]);
        try {
            board.turnToLiving(-1, 3);
        } catch (Exception e) {
            fail("Error when executing code\n"
                    + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                    + "oa.turnToLiving(-1,3)\n"
                    + "additional information " + e);
        }
        assertTrue("Check that method turnToLiving does nothing if coordinates are outside the board\n"
                + "When executing the following code, that didn't happen:\n"
                + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                + "oa.turnToLiving(-1,3)\n"
                + "", Part2Test.prosenttiaElossa(board.getBoard())<0.01);
    }

    @Test
    public void turnToLivingDoesNothingIfCoordinatesOutsideTheBoard2() {
        GameOfLifeBoard board = luoAlusta(5, 5);
        boolean[][] taul = board.getBoard();

        assertFalse("Check that PersonalBoard's constructor initializes cells to be dead at first.", taul[3][3]);
        try {
            board.turnToLiving(1, 7);
        } catch (Exception e) {
            fail("Error when executing code\n"
                    + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                    + "oa.turnToLiving(1,7)\n"
                    + "additional information " + e);
        }
        assertTrue("Check that method turnToLiving does nothing if coordinates are outside the board\n"
                + "When executing the following code, that didn't happen:\n"
                + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                + "oa.turnToLiving(1,7)\n"
                + "", Part2Test.prosenttiaElossa(board.getBoard())<0.01);
    }

    @Test
    public void turnToDeadWorks() {
        GameOfLifeBoard board = luoAlusta(5, 5);
        boolean[][] taul = board.getBoard();
        taul[3][3] = true;

        try {
            board.turnToDead(3, 3);
        } catch (Exception e) {
            fail("Error when executing code\n"
                    + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                    + "oa.turnToDead(3,3)\n"
                    + "additional information " + e);
        }

        assertFalse("Check that method turnToDead kills the cell by assigning its value to false."
                + "\nWhen executing the following code, that didn't happen:\n"
                + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                + "oa.turnToLiving(3,3)\n"
                + "oa.turnToDead(3,3)\n", taul[3][3]);
    }

    @Test
    public void turnToDeadWorksFirstXThenY() {
        GameOfLifeBoard board = luoAlusta(5, 5);
        boolean[][] taul = board.getBoard();
        taul[3][1] = true;

        board.turnToDead(3, 1);
        assertFalse("turnToDead: Check that board's array is always used in the format of [x][y].", taul[3][1]);
    }

    @Test
    public void turnToDeadDoesNothingIfCoordinatesOutsideTheBoard1() {
        GameOfLifeBoard board = luoAlusta(5, 5);
        boolean[][] taul = board.getBoard();

        assertFalse("Check that PersonalBoard's constructor initializes cells to be dead at first.", taul[3][3]);
        try {
            board.turnToDead(-1, 3);
        } catch (Exception e) {
            fail("Error when executing code\n"
                    + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                    + "oa.turnToDead(-1,3)\n"
                    + "additional information " + e);
        }
        assertTrue("Check that method turnToDead does nothing if coordinates are outside the board\n"
                + "When executing the following code, that didn't happen:\n"
                + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                + "oa.turnToDead(-1,3)\n"
                + "", Part2Test.prosenttiaElossa(board.getBoard())<0.01);
    }

    @Test
    public void turnToDeadDoesNothingIfCoordinatesOutsideTheBoard2() {
        GameOfLifeBoard board = luoAlusta(5, 5);
        boolean[][] taul = board.getBoard();

        assertFalse("Check that PersonalBoard's constructor initializes cells to be dead at first.", taul[3][3]);
        try {
            board.turnToDead(1, 7);
        } catch (Exception e) {
            fail("Error when executing code\n"
                    + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                    + "oa.turnToDead(1,7)\n"
                    + "additional information " + e);
        }
        assertTrue("Check that method turnToDead does nothing if coordinates are outside the board\n"
                + "When executing the following code, that didn't happen:\n"
                + "PersonalBoard oa = new PersonalBoard(5,5);\n"
                + "oa.turnToDead(1,7)\n"
                + "", Part2Test.prosenttiaElossa(board.getBoard())<0.01);
    }

    @Test
    public void isAliveWorks() {
        GameOfLifeBoard board = luoAlusta(3, 3);
        boolean[][] taul = board.getBoard();

        taul[0][1] = true;
        taul[2][2] = true;
        taul[1][0] = true;

        for (int x = 0; x < taul.length; x++) {
            for (int y = 0; y < taul[x].length; y++) {
                boolean vast = false;
                try {
                    vast = board.isAlive(x, y);
                } catch (Exception e) {
                    fail("Error when executing code\n"
                            + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                            + "oa.isAlive(" + x + "," + y + ")\n"
                            + "additional information " + e);
                }

                assertEquals("Check that method isAlive returns true if cell is alive, otherwise false.\n"
                        + "Error with code\n"
                        + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                        + "oa.turnToLiving(0,1)\n"
                        + "oa.turnToLiving(2,2)\n"
                        + "oa.turnToLiving(1,0)\n"
                        + "oa.isAlive(" + x + "," + y + ")\n"
                        + "", taul[x][y], vast);
            }
        }
    }

    @Test
    public void isDeadOutsideTheBoardWorks() {
        GameOfLifeBoard board = luoAlusta(3, 3);
        String v =
                "";
        try {

            String a = "board = new PersonalBoard(3,3);\n";
            v = a + "board.isAlive(-1,1);\n";
            assertEquals("Check that method isAlive returns false if given coordinates are outside the board:" + v,
                    false, board.isAlive(-1, 1));
            v = a + "board.isAlive(1,-1);\n";
            assertEquals("Check that method isAlive returns false if given coordinates are outside the board:" + v,
                    false, board.isAlive(1, -1));
            v = a + "board.isAlive(3,3);\n";
            assertEquals("Check that method isAlive returns false if given coordinates are outside the board: "
                    + v,
                    false, board.isAlive(3, 1));
            v = a + "board.isAlive(-1,3);";
            assertEquals("Check that method isAlive returns false if given coordinates are outside the board:" + v,
                    false, board.isAlive(1, 3));
            v = a + "board.isAlive(3,-1);";
            assertEquals("Check that method isAlive returns false if given coordinates are outside the board:" + v,
                    false, board.isAlive(3, -1));
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Check that method isAlive returns false if given coordinates are outside the board. \n"
                    + v + "\ncaused an error " + e);
        }
    }

    @Test
    public void testXAndY() {
        GameOfLifeBoard board = luoAlusta(10, 2);
        try {
            boolean[][] taul = board.getBoard();
            taul[5][1] = true;
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Check that you're using coordinates the right way. If you create a board which width is 10 and height is 2, board's array should have an index [5][1]. So index is in the format of [x][y].");
        }

        board = luoAlusta(2, 10);
        try {
            boolean[][] taul = board.getBoard();
            taul[1][5] = true;
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Check that you're using coordinates the right way. If you create a board which width is 2 and height is 10, board's array should have an index [1][5]. So index is in the format of [x][y].");
        }
    }

    private GameOfLifeBoard luoAlusta(int leveys, int korkeus) {
        Class board = ReflectionUtils.findClass("game.PersonalBoard");

        Constructor c = null;

        try {
            c = ReflectionUtils.requireConstructor(board, int.class, int.class);

        } catch (Throwable ex) {
            fail("Does the class PersonalBoard inside the package game have constructor public PersonalBoard(int width, int height) and is the class itself public?");
        }

        Object a = null;
        try {
            a = ReflectionUtils.invokeConstructor(c, leveys, korkeus);
        } catch (Throwable t) {
            fail("Error with code PersonalBoard a = new PersonalBoard(" + leveys + "," + korkeus + ");" + t);
        }
        try {
            return (GameOfLifeBoard) a;
        } catch (Throwable ex) {
            fail("Does the class PersonalBoard inherit class GameOfLifeBoard?");
        }

        return null;
    }

    private void eiLokaaliaTaulukkoa(GameOfLifeBoard board) throws IllegalArgumentException, IllegalAccessException {
        Field[] kentat = ReflectionUtils.findClass("game.PersonalBoard").getDeclaredFields();
        for (Field f : kentat) {

            if (f.toString().contains("boolean[][]")) {
                f.setAccessible(true);
                String m = "Class PersonalBoard doesn't need to create its own board\n"
                        + "board is inherited from superclass and you can access it by using method getBoard\n"
                        + "remove instance variable " + f.getName();
                assertTrue(m, f.get(board) == null && f.get(board) != board.getBoard());
            }

        }

    }
}
