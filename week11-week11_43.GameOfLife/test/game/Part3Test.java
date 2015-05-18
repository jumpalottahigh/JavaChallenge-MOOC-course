package game;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import gameoflife.GameOfLifeBoard;
import java.lang.reflect.Constructor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

@Points("43.3")
public class Part3Test {

    @Test
    public void livingNeighboursTopLeftCorner() {
        GameOfLifeBoard oa = luoAlusta(2, 2);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(1, 1);
        oa.turnToLiving(0, 1);

        String k = ""
                + "PersonalBoard oa = new PersonalBoard(2, 2);\n"
                + "oa.turnToLiving(1, 0);\n"
                + "oa.turnToLiving(1, 1);\n"
                + "oa.turnToLiving(0, 1);\n"
                + "oa.getNumberOfLivingNeighbours(0, 0);\n";

        try {
            assertEquals("Check that alive neighbours are counted right in the corner.\n"
                    + "Executed code\n" + k, 3, oa.getNumberOfLivingNeighbours(0, 0));
        } catch (Exception e) {
            fail("Exception with code\n" + k + "additional information" + e);
        }
    }



    @Test
    public void aliveNeighboursLeftBorder() {
        GameOfLifeBoard oa = luoAlusta(3, 3);

        String k = ""
                + "PersonalBoard oa = new PersonalBoard(3, 3);\n"
                + "oa.turnToLiving(0, 0);\n"
                + "oa.turnToLiving(1, 0);\n"
                + "oa.turnToLiving(1, 1);\n"
                + "oa.turnToLiving(1, 2);\n"
                + "oa.turnToLiving(0, 2);\n"
                + "oa.getNumberOfLivingNeighbours(0, 1);\n";

        oa.turnToLiving(0, 0);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(1, 1);
        oa.turnToLiving(1, 2);
        oa.turnToLiving(0, 2);

        try {
            assertEquals("Check that alive neighbours are counted right in the left border."
                    + "Executed code\n" + k, 5, oa.getNumberOfLivingNeighbours(0, 1));
        } catch (Exception e) {
            fail("Exception with code\n" + k + "additional information" + e);
        }
    }

    @Test
    public void aliveNeighboursRightBorder() {
        GameOfLifeBoard oa = luoAlusta(2, 3);

        oa.turnToLiving(1, 0);
        oa.turnToLiving(0, 0);
        oa.turnToLiving(0, 2);
        oa.turnToLiving(1, 2);

        String k = ""
                + "PersonalBoard oa = new PersonalBoard(2, 3);\n"
                + "oa.turnToLiving(1, 0);\n"
                + "oa.turnToLiving(0, 0);\n"
                + "oa.turnToLiving(1, 2);\n"
                + "oa.turnToLiving(0, 2);\n"
                + "oa.getNumberOfLivingNeighbours(1, 1);\n";

        try {
            assertEquals("Check that alive neighbours are counted right in the right border."
                    + "Executed code\n" + k, 4, oa.getNumberOfLivingNeighbours(1, 1));
        } catch (Exception e) {
            fail("Exception with code\n" + k + "additional information" + e);
        }
    }

    @Test
    public void aliveNeighboursMiddle() {
        GameOfLifeBoard oa = luoAlusta(3, 3);

        oa.turnToLiving(1, 0);
        oa.turnToLiving(0, 0);
        oa.turnToLiving(0, 2);
        oa.turnToLiving(1, 2);
        oa.turnToLiving(2, 2);

        String k = ""
                + "PersonalBoard oa = new PersonalBoard(3, 3);\n"
                + "oa.turnToLiving(1, 0);\n"
                + "oa.turnToLiving(0, 0);\n"
                + "oa.turnToLiving(0, 2);\n"
                + "oa.turnToLiving(1, 2);\n"
                + "oa.turnToLiving(2, 2);\n"
                + "oa.getNumberOfLivingNeighbours(1, 1);\n";

        try {
            assertEquals("Check that alive neighbours are counted right if point is not on the edge of the board."
                    + "Executed code\n" + k, 5, oa.getNumberOfLivingNeighbours(1, 1));
        } catch (Exception e) {
            fail("Exception with code\n" + k + "additional information" + e);
        }
    }

    @Test
    public void aliveNeighboursBottomRightCorner() {
        GameOfLifeBoard oa = luoAlusta(5, 5);
        oa.turnToLiving(0, 3);
        oa.turnToLiving(0, 2);
        oa.turnToLiving(0, 1);
        oa.turnToLiving(1, 0);
        oa.turnToLiving(2, 0);
        oa.turnToLiving(3, 0);
        oa.turnToLiving(3, 3);
        oa.turnToLiving(3, 4);
        oa.turnToLiving(4, 3);

        String k = ""
                + "PersonalBoard oa = new PersonalBoard(5, 5);\n"
                + "oa.turnToLiving(1, 0);\n"
                + "oa.turnToLiving(2, 0);\n"
                + "oa.turnToLiving(3, 0);\n"
                + "oa.turnToLiving(0, 1);\n"
                + "oa.turnToLiving(0, 2);\n"
                + "oa.turnToLiving(0, 3);\n"
                + "oa.turnToLiving(3, 3);\n"
                + "oa.turnToLiving(3, 4);\n"
                + "oa.turnToLiving(4, 3);\n"
                + "oa.getNumberOfLivingNeighbours(4, 4);\n";

        try {
            assertEquals("Check that number of alive neighbours is counted correctly.\n"
                    + "Executed code\n" + k, 3, oa.getNumberOfLivingNeighbours(4, 4));
        } catch (Exception e) {
            fail("Exception with code\n" + k + "additional information" + e);
        }
    }

    @Test
    public void doNotCountSelfAsLivingNeighbour() {
        GameOfLifeBoard oa = luoAlusta(3, 3);

        oa.turnToLiving(1, 0);
        oa.turnToLiving(0, 0);
        oa.turnToLiving(0, 1);
        oa.turnToLiving(2, 0);
        oa.turnToLiving(0, 2);
        oa.turnToLiving(1, 2);
        oa.turnToLiving(2, 2);
        oa.turnToLiving(1, 1);

        String k = ""
                + "PersonalBoard oa = new PersonalBoard(3, 3);\n"
                + "oa.turnToLiving(1, 0);\n"
                + "oa.turnToLiving(0, 0);\n"
                + "oa.turnToLiving(0, 1);\n"
                + "oa.turnToLiving(2, 0);\n"
                + "oa.turnToLiving(0, 2);\n"
                + "oa.turnToLiving(1, 2);\n"
                + "oa.turnToLiving(2, 2);\n"
                + "oa.turnToLiving(1, 1);\n"
                + "oa.getNumberOfLivingNeighbours(1, 1);\n";

        try {
            assertEquals("Check that alive neighbours are counted right "
                    + " and cell itself isn't counted as its neighbour.\n"
                    + "Executed code\n" + k, 7, oa.getNumberOfLivingNeighbours(1, 1));
        } catch (Exception e) {
            fail("Exception with code\n" + k + "additional information" + e);
        }
    }

    private GameOfLifeBoard luoAlusta(int leveys, int korkeus) {
        Class alusta = ReflectionUtils.findClass("game.PersonalBoard");
        Constructor c = ReflectionUtils.requireConstructor(alusta, int.class, int.class);
        try {
            return (GameOfLifeBoard) ReflectionUtils.invokeConstructor(c, leveys, korkeus);
        } catch (Throwable ex) {
            fail("Does the class PersonalBoard inside the package game have constructor public PersonalBoard(int width, int height) and is the class itself public? Does the class inherit class GameOfLifeBoard?");
        }

        return null;
    }
}
