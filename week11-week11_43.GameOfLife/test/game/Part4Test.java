package game;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import gameoflife.GameOfLifeBoard;
import java.lang.reflect.Constructor;
import static org.junit.Assert.*;
import org.junit.Test;

@Points("43.4")
public class Part4Test {

    @Test
    public void manageCellDoesntManageOtherCells() {
        GameOfLifeBoard oa = luoAlusta(1, 1);

        String k = ""
                + "PersonalBoard oa = new PersonalBoard(1,1);\n"
                + "oa.manageCell(0,0);\n";

        try {
            oa.manageCell(0, 0, 0);
        } catch (Exception e) {
            fail("Executing the following code led to an exception \n" + k
                    + "Additional information" + e);
        }
    }

    @Test
    public void diesIfOnlyOneNeighbourAlive() {
        GameOfLifeBoard oa = luoAlusta(2, 2);
        oa.turnToLiving(0, 0);
        oa.turnToLiving(0, 1);
        oa.manageCell(0, 0, 1);

        String k = ""
                + "PersonalBoard oa = new PersonalBoard(2,2);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.manageCell(0,0,1);\n"
                + "oa.isAlive(0,0)\n";

        assertEquals("If only one neighbour is alive, alive cell dies. "
                + "Executed code\n"
                + k, false, oa.isAlive(0, 0));
    }

    @Test
    public void diesIfNoLivingNeighbours() {
        GameOfLifeBoard oa = luoAlusta(2, 2);
        oa.turnToLiving(0, 0);
        oa.manageCell(0, 0, 0);
        String k = ""
                + "PersonalBoard oa = new PersonalBoard(2,2);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.manageCell(0,0);\n"
                + "oa.isAlive(0,0)\n";

        assertEquals("If no neighbour is alive, alive cell dies."
                + "Executed code\n"
                + k, false, oa.isAlive(0, 0));
    }

    @Test
    public void diesIfOverThreeNeighboursAlive() {
        GameOfLifeBoard oa = luoAlusta(3, 3);
        oa.turnToLiving(1, 1);
        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(2, 0);
        oa.turnToLiving(0, 1);
        oa.manageCell(1, 1, 4);
        String k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(1,1);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 4 living neighbours, alive cell dies.\n" + k,
                false, oa.isAlive(1, 1));

        oa.turnToLiving(0, 2);
        oa.turnToLiving(1, 1);
        oa.manageCell(1, 1, 5);

        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(1,1);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.turnToLiving(0,2);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 5 living neighbours, alive cell dies.\n" + k, false, oa.isAlive(1, 1));


        oa.turnToLiving(1, 2);
        oa.turnToLiving(1, 1);
        oa.manageCell(1, 1, 6);

        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(1,1);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.turnToLiving(0,2);\n"
                + "oa.turnToLiving(1,2);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 6 living neighbours, alive cell dies.\n" + k, false, oa.isAlive(1, 1));

        oa.turnToLiving(2, 2);
        oa.turnToLiving(1, 1);
        oa.manageCell(1, 1, 7);

        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(1,1);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.turnToLiving(0,2);\n"
                + "oa.turnToLiving(1,2);\n"
                + "oa.turnToLiving(2,2);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 7 living neighbours, alive cell dies.\n" + k, false, oa.isAlive(1, 1));

    }

    /*
     *
     */
    @Test
    public void staysaliveIfTwoOrThreeLivingNeighbours() {
        GameOfLifeBoard oa = luoAlusta(3, 3);
        oa.turnToLiving(1, 1);
        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.manageCell(1, 1, 2);
        String k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(1,1);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 2 living neighbours, alive cell stays alive.\n" + k,
                true, oa.isAlive(1, 1));

        oa.turnToLiving(2, 0);
        oa.manageCell(1, 1, 3);

        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(1,1);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 3 living neighbours, alive cell stays alive.\n" + k,
                true, oa.isAlive(1, 1));
    }

    @Test
    public void deadCellBecomesAliveIfThreeLivingNeighbours() {
        GameOfLifeBoard oa = luoAlusta(3, 3);
        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(2, 0);
        oa.manageCell(1, 1, 3);
        String k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 3 living neighbours, dead cell becomes alive.\n" + k,
                true, oa.isAlive(1, 1));

    }

    @Test
    public void deadStaysDeadIfNoThreeLivingNeighbours() {
        GameOfLifeBoard oa = luoAlusta(3, 3);
        oa.manageCell(1, 1, 0);
        String k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 0 living neighbours, dead cell stays dead.\n" + k,
                false, oa.isAlive(1, 1));

        oa.turnToLiving(0, 0);
        oa.manageCell(1, 1, 1);
        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 1 living neighbour, dead cell stays dead.\n" + k,
                false, oa.isAlive(1, 1));

        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.manageCell(1, 1, 2);
        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 2 living neighbours, dead cell stays dead.\n" + k,
                false, oa.isAlive(1, 1));

        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(2, 0);
        oa.turnToLiving(0, 1);
        oa.manageCell(1, 1, 4);
        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 4 living neighbours, dead cell stays dead.\n" + k,
                false, oa.isAlive(1, 1));

        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(2, 0);
        oa.turnToLiving(0, 1);
        oa.turnToLiving(0, 2);
        oa.manageCell(1, 1, 5);
        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.turnToLiving(0,2);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 5 living neighbours, dead cell stays dead.\n" + k,
                false, oa.isAlive(1, 1));

        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(2, 0);
        oa.turnToLiving(0, 1);
        oa.turnToLiving(0, 2);
        oa.turnToLiving(1, 2);
        oa.manageCell(1, 1, 6);
        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.turnToLiving(0,2);\n"
                + "oa.turnToLiving(1,2);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 6 living neighbours, dead cell stays dead.\n" + k,
                false, oa.isAlive(1, 1));


        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(2, 0);
        oa.turnToLiving(0, 1);
        oa.turnToLiving(0, 2);
        oa.turnToLiving(1, 2);
        oa.turnToLiving(2, 2);
        oa.manageCell(1, 1, 7);
        k = ""
                + "PersonalBoard oa = new PersonalBoard(3,3);\n"
                + "oa.turnToLiving(0,0);\n"
                + "oa.turnToLiving(1,0);\n"
                + "oa.turnToLiving(2,0);\n"
                + "oa.turnToLiving(0,1);\n"
                + "oa.turnToLiving(0,2);\n"
                + "oa.turnToLiving(1,2);\n"
                + "oa.turnToLiving(2,2);\n"
                + "oa.manageCell(1,1);\n"
                + "oa.isAlive(1,1)\n";

        assertEquals("If 7 living neighbours, dead cell stays dead.\n" + k,
                false, oa.isAlive(1, 1));
    }

    private GameOfLifeBoard luoAlusta(int width, int height) {
        Class board = ReflectionUtils.findClass("game.PersonalBoard");
        Constructor c = ReflectionUtils.requireConstructor(board, int.class, int.class);
        try {
            return (GameOfLifeBoard) ReflectionUtils.invokeConstructor(c, width, height);
        } catch (Throwable ex) {
            fail("Does the class PersonalBoard inside the package game have constructor public PersonalBoard(int width, int height) and is the class itself public? Does the class inherit class GameOfLifeBoard?");
        }

        return null;
    }
}
