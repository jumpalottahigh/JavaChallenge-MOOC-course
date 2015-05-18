package wormgame;

import wormgame.Direction;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import wormgame.gui.UserInterface;
import wormgame.gui.Updatable;
import wormgame.game.WormGame;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class WormGameTest<_P, _O, _M, _G, _N, _A, _K> {

    private String pakkaus = "wormgame.domain";
    private String palaLuokka = "Piece";
    private String appleLuokka = "Apple";
    private String wormLuokka = "Worm";
    private Class palaClass;
    private Class appleClass;
    private Class wormClass;
    private String domainPakkaus = "wormgame.domain";
    private String peliPakkaus = "wormgame.game";
    private String wormgameLuokka = "WormGame";
    private Class wormgameClass;
    String uiPakkaus = "wormgame.gui";
    String nappaimistonkuuntelijaLuokka = "KeyboardListener";
    Class nappaimistonKuuntelijaClass;
    String piirtoalustaLuokka = "DrawingBoard";
    Class piirtoalustaClass;
    Method piirtoalustaPaintComponentMethod;
    String kayttoliittymaLuokka = "UserInterface";
    Method kayttoliittymaLuoKomponentitMethod;
    Reflex.ClassRef<_P> _PRef;
    Reflex.ClassRef<_O> _ORef;
    Reflex.ClassRef<_M> _MRef;
    Reflex.ClassRef<_G> _GRef;
    Reflex.ClassRef<_N> _NRef;
    Reflex.ClassRef<_A> _ARef;

    @Before
    public void setUp() {
        try {
            String pala = pakkaus + "." + palaLuokka;
            palaClass = ReflectionUtils.findClass(pala);
            _PRef = Reflex.reflect(pala);

            String apple = pakkaus + "." + appleLuokka;
            appleClass = ReflectionUtils.findClass(apple);
            _ORef = Reflex.reflect(apple);

            String worm = pakkaus + "." + wormLuokka;
            wormClass = ReflectionUtils.findClass(worm);
            _MRef = Reflex.reflect(worm);

            String game = peliPakkaus + "." + wormgameLuokka;
            wormgameClass = ReflectionUtils.findClass(game);
            _GRef = Reflex.reflect(game);

            nappaimistonKuuntelijaClass = ReflectionUtils.findClass(uiPakkaus + "." + nappaimistonkuuntelijaLuokka);
            _NRef = Reflex.reflect(uiPakkaus + "." + nappaimistonkuuntelijaLuokka);

            piirtoalustaClass = ReflectionUtils.findClass(uiPakkaus + "." + piirtoalustaLuokka);
            _ARef = Reflex.reflect(uiPakkaus + "." + piirtoalustaLuokka);

            for (Method m : piirtoalustaClass.getDeclaredMethods()) {
                if (m.getName().equals("paintComponent")) {
                    piirtoalustaPaintComponentMethod = m;
                    break;
                }
            }

            piirtoalustaPaintComponentMethod.setAccessible(true);
        } catch (Throwable e) {
        }
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void isPiece() throws Throwable {
        assertNotNull("Did you create class " + palaLuokka + " inside the package " + pakkaus + "?", palaClass);
        assertTrue("Create constructor public Piece(int x, int y) for class Piece", _PRef.constructor().taking(int.class, int.class).isPublic());
        assertTrue("Create method public int getX() for class Piece", _PRef.method("getX").returning(int.class).takingNoParams().isPublic());
        assertTrue("Create method public int getY() for class Piece", _PRef.method("getY").returning(int.class).takingNoParams().isPublic());

        String v = "Piece p1 = new Piece(2,4);\n";

        _P kakkosNelonen = _PRef.constructor().taking(int.class, int.class).withNiceError(v).invoke(2, 4);

        v += "p1.getX()\n";
        int x = _PRef.method("getX").returning(int.class).takingNoParams().withNiceError(v).invokeOn(kakkosNelonen);
        assertEquals(v, 2, x);

        v += "p1.getY()\n";
        int y = _PRef.method("getY").returning(int.class).takingNoParams().withNiceError(v).invokeOn(kakkosNelonen);
        assertEquals(v, 4, y);

        _P kolmosKasi = _PRef.constructor().taking(int.class, int.class).invoke(3, 8);
        String vv = "Piece p4 = new Piece(3,8);\n"
                + "p4.getX()\n";
        x = _PRef.method("getX").returning(int.class).takingNoParams().withNiceError(v).invokeOn(kolmosKasi);

        assertEquals(vv, 3, x);

        vv = "Piece p4 = new Piece(3,8);\n"
                + "p4.getX()\n";
        x = _PRef.method("getY").returning(int.class).takingNoParams().withNiceError(v).invokeOn(kolmosKasi);

        assertEquals(vv, 8, x);

        v = ""
                + "Piece p1 = new Piece(2,4);\n"
                + "Piece p2 = new Piece(2,4);\n"
                + "p1.runsInto(p2);\n";
        _P toinenKakkosNelonen = newPiece(2, 4);

        assertTrue("Create method public boolean runsInto(Piece piece) for class Piece", _PRef.method("runsInto").returning(boolean.class).taking(palaClass).isPublic());

        boolean samat = _PRef.method(kakkosNelonen, "runsInto").returning(boolean.class).taking(_PRef.cls()).invoke(toinenKakkosNelonen);
        assertEquals(v, true, samat);

        v = ""
                + "Piece p1 = new Piece(2,4);\n"
                + "Piece p3 = new Piece(3,4);\n"
                + "p1.runsInto(p3);\n";

        _P kolmosNelonen = _PRef.constructor().taking(int.class, int.class).invoke(3, 4);
        samat = _PRef.method(kakkosNelonen, "runsInto").returning(boolean.class).taking(_PRef.cls()).invoke(kolmosNelonen);
        assertEquals(v, false, samat);

        v = ""
                + "Piece p1 = new Piece(2,4);\n"
                + "Piece p3 = new Piece(4,3);\n"
                + "p1.runsInto(p3);\n";

        _P nelosKolmonen = _PRef.constructor().taking(int.class, int.class).invoke(4, 3);
        samat = _PRef.method("runsInto").returning(boolean.class).taking(_PRef.cls()).invokeOn(nelosKolmonen, toinenKakkosNelonen);
        assertEquals(v, false, samat);

        v = ""
                + "Piece p1 = new Piece(2,4);\n"
                + "Piece p3 = new Piece(2,3);\n"
                + "p1.runsInto(p3);\n";

        _P kakkosKolmonen = _PRef.constructor().taking(int.class, int.class).invoke(2, 3);
        samat = _PRef.method("runsInto").returning(boolean.class).taking(_PRef.cls()).invokeOn(kakkosKolmonen, toinenKakkosNelonen);
        assertEquals(v, false, samat);

        String tulostus = nelosKolmonen.toString();

        assertFalse("Create method toString for class Piece as defined in the assignment", tulostus.contains("@"));
        assertEquals("Piece p = new Piece(4,3);\n"
                + "System.out.println(p);\n", "(4,3)", tulostus.replaceAll("\\s+", ""));
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void isApple() {
        assertNotNull("Did you create class " + appleLuokka + " inside the package " + pakkaus + "?", appleClass);
        assertTrue("Does the class " + appleLuokka + " inherit class " + palaLuokka + "?", palaClass.isAssignableFrom(appleClass));
        assertTrue("Create constructor public Apple(int x, int y) for class Apple", Reflex.reflect(appleClass).constructor().taking(int.class, int.class).isPublic());
        assertTrue("Class Apple should inherit method public int getX()", Reflex.reflect(appleClass).method("getX").returning(int.class).takingNoParams().isPublic());
        assertTrue("Class Apple should inherit method public int getY()", Reflex.reflect(appleClass).method("getY").returning(int.class).takingNoParams().isPublic());
        assertTrue("Class Apple should inherit method public boolean runsInto(Piece piece)", Reflex.reflect(appleClass).method("runsInto").returning(boolean.class).taking(palaClass).isPublic());

        assertTrue("Does the class " + appleLuokka + " have 0 instance variables?", appleClass.getDeclaredFields().length == 0);
        assertTrue("Does the class " + appleLuokka + " have 0 non-inherited methods?", appleClass.getDeclaredMethods().length == 0);
    }
    /*
     *
     */

    @Test
    @Points(Exercise.ID + ".2")
    public void isWorm() throws Throwable {
        assertNotNull("Did you create class " + wormLuokka + " inside the package " + pakkaus + "?", wormClass);

        assertTrue("Create constructor public Worm(int x, int y, Direction direction) for class Worm", _MRef.constructor().taking(int.class, int.class, Direction.class).isPublic());

        saniteettitarkastus(pakkaus + "." + wormLuokka, 10, "");

        assertTrue("Create method public Direction getDirection() for class Worm",
                _MRef.method("getDirection").returning(Direction.class).takingNoParams().isPublic());

        String v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n";
        _M worm = _MRef.constructor().taking(int.class, int.class, Direction.class).withNiceError(v).invoke(1, 1, Direction.RIGHT);
        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.getDirection();\n";
        assertEquals(Direction.RIGHT, _MRef.method(worm, "getDirection").returning(Direction.class).takingNoParams().withNiceError(v).invoke());


        assertTrue("Create method public void setDirection(Direction direction) for class Worm", _MRef.method("setDirection").returningVoid().taking(Direction.class).isPublic());

        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.setDirection(Direction.DOWN);\n"
                + "m.getDirection();\n";
        kaanny(worm, Direction.DOWN, v);
        assertEquals(v, Direction.DOWN, _MRef.method(worm, "getDirection").returning(Direction.class).takingNoParams().withNiceError(v).invoke());

        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.setDirection(Direction.DOWN);\n"
                + "m.setDirection(Direction.UP);\n"
                + "m.getDirection();\n";
        _MRef.method(worm, "setDirection").returningVoid().taking(Direction.class).withNiceError(v).invoke(Direction.UP);
        assertEquals(v, Direction.UP, _MRef.method(worm, "getDirection").returning(Direction.class).takingNoParams().withNiceError(v).invoke());

        worm = _MRef.constructor().taking(int.class, int.class, Direction.class).withNiceError(v).invoke(1, 1, Direction.RIGHT);

        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.getLength();\n";
        assertTrue("Create method public int getLength() for class Worm", _MRef.method("getLength").returning(int.class).takingNoParams().isPublic());
        assertEquals(v, 1, (int) pituus(worm, v));

        assertTrue("Create method public List<Piece> getPieces() for class Worm", _MRef.method("getPieces").returning(List.class).takingNoParams().isPublic());

        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.getPieces();\n";

        _P p = newPiece(1, 1);
        List<_P> exp = new ArrayList<_P>();
        exp.add(p);

        List<_P> ret = _MRef.method(worm, "getPieces").returning(List.class).takingNoParams().invoke();
        assertTrue(v + "you returned: " + exp, samat(exp, ret));

        worm = _MRef.constructor().taking(int.class, int.class, Direction.class).withNiceError(v).invoke(3, 5, Direction.RIGHT);
        v = "Worm m = new Worm(3, 5, Direction.RIGHT);\n"
                + "m.getPieces();\n";

        p = newPiece(3, 5);
        exp = new ArrayList<_P>();
        exp.add(p);

        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        // metodi move

        assertTrue("Create method public void move() for class Worm", _MRef.method("move").returningVoid().takingNoParams().isPublic());

        worm = newWorm(1, 1, Direction.RIGHT, v);
        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.getPieces();\n";

        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(1, 1));
        exp.add(newPiece(2, 1));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.getLength();\n";

        assertEquals(v, 2, (int) pituus(worm, v));

        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getPieces();\n";

        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(2, 1));
        exp.add(newPiece(3, 1));
        exp.add(newPiece(4, 1));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getLength();\n";

        assertEquals(v, 3, (int) pituus(worm, v));

        v = "Worm m = new Worm(1, 1, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.pituus();\n";

        assertEquals(v, 3, (int) pituus(worm, v));

        // move vasemmalle

        worm = newWorm(5, 1, Direction.LEFT, v);
        v = "Worm m = new Worm(1, 1, Direction.LEFT);\n"
                + "m.move();\n"
                + "m.getPieces();\n";

        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 1));
        exp.add(newPiece(4, 1));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(5, 1, Direction.LEFT);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getPieces();\n";

        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(4, 1));
        exp.add(newPiece(3, 1));
        exp.add(newPiece(2, 1));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        // move alas

        worm = newWorm(5, 1, Direction.DOWN, v);
        v = "Worm m = new Worm(5, 1, Direction.DOWN);\n"
                + "m.move();\n"
                + "m.getPieces();\n";

        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 1));
        exp.add(newPiece(5, 2));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(5, 1, Direction.DOWN);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getPieces();\n";

        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 2));
        exp.add(newPiece(5, 3));
        exp.add(newPiece(5, 4));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        // move ylös

        worm = newWorm(5, 5, Direction.UP, v);
        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.getPieces();\n";

        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 5));
        exp.add(newPiece(5, 4));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getPieces();\n";

        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 4));
        exp.add(newPiece(5, 3));
        exp.add(newPiece(5, 2));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        // käänny

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.setDirection(Direction.RIGHT)\n"
                + "m.move();"
                + "m.move();"
                + "m.getPieces();\n";

        kaanny(worm, Direction.RIGHT, v);
        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 2));
        exp.add(newPiece(6, 2));
        exp.add(newPiece(7, 2));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.setDirection(Direction.RIGHT)\n"
                + "m.move();"
                + "m.move();"
                + "m.setDirection(Direction.DOWN);"
                + "m.move();"
                + "m.getPieces();\n";

        kaanny(worm, Direction.DOWN, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(6, 2));
        exp.add(newPiece(7, 2));
        exp.add(newPiece(7, 3));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));
        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.setDirection(Direction.RIGHT)\n"
                + "m.move();"
                + "m.move();"
                + "m.setDirection(Direction.DOWN);"
                + "m.move();"
                + "m.getPieces();\n"
                + "m.setDirection(Direction.DOWN);"
                + "m.move();"
                + "m.move();"
                + "m.move();"
                + "";

        kaanny(worm, Direction.LEFT, v);
        move(worm, v);
        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(6, 3));
        exp.add(newPiece(5, 3));
        exp.add(newPiece(4, 3));
        ret = palat(worm, v);
        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        // grow

        assertTrue("Create method public void grow() for class Worm", _MRef.method("grow").returningVoid().takingNoParams().isPublic());

        worm = newWorm(5, 5, Direction.UP, v);
        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.grow();\n";

        grow(worm, v);

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.grow();\n"
                + "m.getPieces();\n";

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 5));
        ret = palat(worm, v);
        assertTrue("Notice that worm grows only when it moves!\n" + v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.grow();\n"
                + "m.getLength();\n";

        assertEquals("Notice that worm grows only when it moves!\n" + v, 1, (int) pituus(worm, v));

        // grow ja move

        worm = newWorm(5, 5, Direction.UP, v);
        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.getPieces()\n";

        move(worm, v);
        move(worm, v);
        grow(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 5));
        exp.add(newPiece(5, 4));
        exp.add(newPiece(5, 3));
        exp.add(newPiece(5, 2));
        ret = palat(worm, v);

        assertTrue("Worm should grow\n" + v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.getLength();\n";

        assertEquals("Worm should grow\n" + v, 4, (int) pituus(worm, v));

        // ei grow liikaa

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getPieces()\n";

        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 4));
        exp.add(newPiece(5, 3));
        exp.add(newPiece(5, 2));
        exp.add(newPiece(5, 1));
        ret = palat(worm, v);

        assertTrue("Worm should grow only once per method call\n" + v + "you returned: " + ret, samat(exp, ret));

        assertEquals("Worm should grow only once per method call\n" + v, 4, (int) pituus(worm, v));

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.setDirection(Direction.RIGHT);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getPieces()\n";

        kaanny(worm, Direction.RIGHT, v);
        grow(worm, v);
        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 3));
        exp.add(newPiece(5, 2));
        exp.add(newPiece(5, 1));
        exp.add(newPiece(6, 1));
        exp.add(newPiece(7, 1));
        ret = palat(worm, v);

        assertTrue(v + "you returned: " + ret, samat(exp, ret));

        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.setDirection(Direction.RIGHT);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.setDirection(Direction.DOWN);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getPieces()\n";

        kaanny(worm, Direction.DOWN, v);
        grow(worm, v);
        move(worm, v);
        grow(worm, v);
        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 2));
        exp.add(newPiece(5, 1));
        exp.add(newPiece(6, 1));
        exp.add(newPiece(7, 1));
        exp.add(newPiece(7, 2));
        exp.add(newPiece(7, 3));
        exp.add(newPiece(7, 4));
        ret = palat(worm, v);

        assertTrue(v + "you returned: " + ret, samat(exp, ret));

       // liian aikainen kasvu ei tee mitään

        worm = newWorm(5, 5, Direction.UP, v);
        v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                + "m.move();\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.move();\n"
                + "m.getPieces()\n";

        move(worm, v);
        grow(worm, v);
        move(worm, v);
        move(worm, v);

        exp = new ArrayList<_P>();
        exp.add(newPiece(5, 4));
        exp.add(newPiece(5, 3));
        exp.add(newPiece(5, 2));
        ret = palat(worm, v);

        assertTrue("If worm's length is 1 or 2 and method grow() is called, it shouldn't have any effect"
                + "\n" + v + "you returned: " + ret, samat(exp, ret));



        // osuu

        assertTrue("Create method public boolean runsInto(Piece piece) for class Worm", _MRef.method("runsInto").returning(boolean.class).taking(_PRef.cls()).isPublic());

        _M worm2 = newWorm(5, 5, Direction.UP, v);
        p = newPiece(5, 5);

        v = ""
                + "Worm m = new Worm(5, 5, Direction.UP, v);\n"
                + "m.runsInto(new Piece(5,5));\n";
        assertEquals(v, true, _MRef.method(worm2, "runsInto").returning(boolean.class).taking(_PRef.cls()).withNiceError(v).invoke(p));

        worm2 = newWorm(4, 6, Direction.UP, v);
        p = newPiece(4, 6);

        v = ""
                + "Worm m = new Worm(5, 5, Direction.UP, v);\n"
                + "m.runsInto(new Piece(5,5));\n";
        assertEquals(v, true, _MRef.method(worm2, "runsInto").returning(boolean.class).taking(_PRef.cls()).withNiceError(v).invoke(p));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 4 && j == 6) {
                    continue;
                }
                p = newPiece(i, j);
                v = ""
                        + "Worm m = new Worm(4, 6, Direction.UP, v);\n"
                        + "m.runsInto(new Piece(" + i + "," + j + "));\n";
                assertEquals(v, false, _MRef.method(worm2, "runsInto").returning(boolean.class).taking(_PRef.cls()).withNiceError(v).invoke(p));

            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 4 && j == 6) {
                    continue;
                }
                p = newPiece(i, j);

                v = "Worm m = new Worm(5, 5, Direction.UP);\n"
                        + "m.move();\n"
                        + "m.move();\n"
                        + "m.grow();\n"
                        + "m.move();\n"
                        + "m.move();\n"
                        + "m.setDirection(Direction.RIGHT);\n"
                        + "m.grow();\n"
                        + "m.move();\n"
                        + "m.move();\n"
                        + "m.setDirection(Direction.DOWN);\n"
                        + "m.grow();\n"
                        + "m.move();\n"
                        + "m.grow();\n"
                        + "m.move();\n"
                        + "m.move();\n"
                        + "m.getPieces()\n"
                        + "m.runsInto(new Piece(" + i + "," + j + "));\nworm's pieces: " + exp + "\n";
                boolean vast = sis(exp, p);
                assertEquals(v, vast, _MRef.method(worm, "runsInto").returning(boolean.class).taking(_PRef.cls()).withNiceError(v).invoke(p));
            }

        }

        // osuu itseensa

        assertTrue("Create method public boolean runsIntoItself() for class Worm",
                _MRef.method("runsIntoItself").returning(boolean.class).takingNoParams().isPublic());

        worm = newWorm(3, 3, Direction.RIGHT, v);
        v = ""
                + "Worm m = new Worm(3, 3, Direction.RIGHT);\n"
                + "m.runsIntoItself();\n";

        assertEquals(v, false, runsIntoItself(worm, v));
        move(worm, v);

        v = ""
                + "Worm m = new Worm(3, 3, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.runsIntoItself();\n";

        assertEquals(v, false, runsIntoItself(worm, v));

        kaanny(worm, Direction.LEFT, v);
        move(worm, v);
        v = ""
                + "Worm m = new Worm(3, 3, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.setDirection(Direction.LEFT);\n"
                + "m.move();\n"
                + "m.runsIntoItself();\n";

        assertEquals(v, true, runsIntoItself(worm, v));


        worm = newWorm(3, 3, Direction.RIGHT, v);
        v = ""
                + "Worm m = new Worm(3, 3, Direction.RIGHT);\n"
                + "m.runsIntoItself();\n";

        assertEquals(v, false, runsIntoItself(worm, v));

        move(worm, v);

        v = ""
                + "Worm m = new Worm(3, 3, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.runsIntoItself();\n";

        assertEquals(v, false, runsIntoItself(worm, v));

        kaanny(worm, Direction.DOWN, v);
        grow(worm, v);
        move(worm, v);
        v = ""
                + "Worm m = new Worm(3, 3, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.setDirection(Direction.DOWN);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.runsIntoItself();\n";

        assertEquals(v, false, runsIntoItself(worm, v));

        kaanny(worm, Direction.LEFT, v);
        grow(worm, v);
        move(worm, v);

        v = ""
                + "Worm m = new Worm(3, 3, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.setDirection(Direction.DOWN);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.setDirection(Direction.LEFT);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.runsIntoItself();\n";

        assertEquals(v, false, runsIntoItself(worm, v));

        v = ""
                + "Worm m = new Worm(3, 3, Direction.RIGHT);\n"
                + "m.move();\n"
                + "m.setDirection(Direction.DOWN);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.setDirection(Direction.LEFT);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.runsIntoItself();\n"
                + "m.setDirection(Direction.UP);\n"
                + "m.grow();\n"
                + "m.move();\n"
                + "m.runsIntoItself();\n";

        kaanny(worm, Direction.UP, v);
        grow(worm, v);
        move(worm, v);

        assertEquals(v, true, runsIntoItself(worm, v));
    }

    @Test
    @Points(Exercise.ID + ".2")
    public void wormDoesCircle() throws Throwable {
        _M worm = newWorm(1, 1, Direction.RIGHT, "");
        moveYmpyra(worm, 1, 1);
    }

    /*
     *
     */
    @Test
    @Points(Exercise.ID + ".3")
    public void isWormGame() {
        assertNotNull("Have you created class " + wormgameLuokka + " inside the package " + peliPakkaus + "?", wormgameClass);
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void isInterfacesAndImplementations() throws Throwable {

        assertNotNull("Does the class " + wormgameLuokka + " exist inside the package " + peliPakkaus + "?", wormgameClass);

        saniteettitarkastus(peliPakkaus + "." + wormgameLuokka, 10, "");

        assertTrue("Does the class " + wormgameLuokka + " inherit class javax.swing.Timer?", javax.swing.Timer.class.isAssignableFrom(wormgameClass));
        assertTrue("Does the class " + wormgameLuokka + " implement interface ActionListener?", ActionListener.class.isAssignableFrom(wormgameClass));

        assertTrue("Create method public Worm getWorm() for class WormGame", _GRef.method("getWorm").returning(wormClass).takingNoParams().isPublic());

        String v = ""
                + "WormGame mp = WormGame(10,10)\n"
                + "Worm m = mp.getWorm();\n";
        WormGame mp = new WormGame(10, 10);

        _M worm = _GRef.method((_G) mp, "getWorm").returning(_MRef.cls()).takingNoParams().withNiceError(v).invoke();

        assertFalse("Returned worm cannot be null with code\n" + v, worm == null);

        assertEquals(v + "m.getLength();\n", 1, (int) pituus(worm, v + "m.getLength();"));

        assertEquals(v + "m.getDirection();\n", Direction.DOWN,
                _MRef.method(worm, "getDirection")
                .returning(Direction.class).takingNoParams().withNiceError(v + "m.getDirection();\n").invoke());

        List<_P> ret = palat(worm, v + "m.getPieces();");

        List<_P> exp = new ArrayList<_P>();
        exp.add(newPiece(5, 5));
        ret = palat(worm, v);
        assertTrue("Worm should be in the center at first\n" + v + "m.getPieces();\n" + "you returned: " + ret, samat(exp, ret));

        assertTrue("Create method public void setWorm(Worm worm) for class WormGame", _GRef.method("setWorm").returningVoid().taking(_MRef.cls()).isPublic());

        v = ""
                + "WormGame mp = WormGame(10,10)\n"
                + "Worm m = new Worm(1,1,Direction.UP);\n"
                + "mp.setWorm(m);\n";

        _M worm2 = newWorm(1, 1, Direction.UP, "");
        _GRef.method((_G) mp, "setWorm").returningVoid().taking(_MRef.cls()).withNiceError(v).invoke(worm2);

        v = ""
                + "WormGame mp = WormGame(10,10)\n"
                + "Worm m = new Worm(1,1,Direction.UP);\n"
                + "mp.setWorm(m);\n"
                + "m == mp.getWorm()";

        _M worm3 = _GRef.method((_G) mp, "getWorm").returning(_MRef.cls()).takingNoParams().withNiceError(v).invoke();

        assertEquals("Has the new worm been set? Check code:\n" + v, true, worm3 == worm2);

        assertTrue("Create method public Apple getApple() for class WormGame", _GRef.method("getApple").returning(_ORef.cls()).takingNoParams().isPublic());

        v = ""
                + "WormGame mp = WormGame(10,10)\n"
                + "Apple o = mp.getApple();\n";

        _O o = _GRef.method((_G) mp, "getApple").returning(_ORef.cls()).takingNoParams().withNiceError(v).invoke();
        assertTrue("Returned apple was null with code\n" + v, o != null);

        int x = _ORef.method(o, "getX").returning(int.class).takingNoParams().withNiceError(v).invoke();
        int y = _ORef.method(o, "getY").returning(int.class).takingNoParams().withNiceError(v).invoke();

        assertTrue("Location of the apple was " + x + ", " + y + " with code:\n" + v, -1 < x && x < 10 && -1 < y && y < 10);

        assertTrue("Create method  public void setApple(Apple apple) for class WormGame", _GRef.method("setApple").returningVoid().taking(_ORef.cls()).isPublic());

        v = ""
                + "WormGame mp = WormGame(10,10)\n"
                + "Apple o = new Apple(1,1);\n"
                + "mp.setApple(m);\n";

        _O oSet = _ORef.constructor().taking(int.class, int.class).withNiceError(v).invoke(2, 2);
        _GRef.method((_G) mp, "setApple").returningVoid().taking(_ORef.cls()).withNiceError(v).invoke(oSet);
        _O o2 = _GRef.method((_G) mp, "getApple").returning(_ORef.cls()).takingNoParams().withNiceError(v).invoke();

        _GRef.method((_G) mp, "setApple").returningVoid().taking(_ORef.cls()).withNiceError(v).invoke(oSet);
        assertEquals("Has the new apple been set? Check code: " + v + "o == mp.getApple()\n", true, o2 == oSet);

    }

    @Test
    @Points(Exercise.ID + ".3")
    public void appleDoesntGoOutsideOrOnTheWorm() throws Throwable {
        HashSet<Integer> xx = new HashSet<Integer>();
        HashSet<Integer> yy = new HashSet<Integer>();


        for (int i = 0; i < 200; i++) {
            String v = "WormGame mp = new WormGame(6,6);\n"
                    + "mp.getApple();\n";
            WormGame mp = new WormGame(6, 6);

            _M worm = _GRef.method((_G) mp, "getWorm").returning(_MRef.cls()).takingNoParams().withNiceError(v).invoke();

            assertFalse("Returned worm cannot be null with code\n" + v, worm == null);

            assertEquals(v + "m.getLength();\n", 1, (int) pituus(worm, v + "m.getLength();"));

            assertEquals(v + "m.getDirection();\n", Direction.DOWN,
                    _MRef.method(worm, "getDirection")
                    .returning(Direction.class).takingNoParams().withNiceError(v + "m.getDirection();\n").invoke());
            List<_P> ret = palat(worm, v + "m.getPieces();");

            List<_P> exp = new ArrayList<_P>();
            exp.add(newPiece(3, 3));
            ret = palat(worm, v);
            assertTrue("Worm should be in the center at first\n" + v + "m.getPieces();\n" + "you returned: " + ret, samat(exp, ret));


            _O o = _GRef.method((_G) mp, "getApple").returning(_ORef.cls()).takingNoParams().withNiceError(v).invoke();
            assertTrue("Returned apple was null with code\n" + v, o != null);

            int x = _ORef.method(o, "getX").returning(int.class).takingNoParams().withNiceError(v).invoke();
            int y = _ORef.method(o, "getY").returning(int.class).takingNoParams().withNiceError(v).invoke();
            xx.add(x);
            yy.add(y);

            assertFalse("Location of the apple was " + x + "," + y + " which is equal to worm's location with code:\n" + v, x == 3 && y == 3);
            assertFalse("Location of the apple was " + x + "," + y + " with code:\n" + v, 0 > x || x >= 6 || 0 > y || y >= 6);

        }

        assertFalse("Apple is set always in the same place when \n"
                + "WormGame mp = new WormGame();", xx.size() == 1 && yy.size() == 1);

        assertTrue("Apple's location isn't random enough with code: \n"
                + "WormGame mp = new WormGame();", xx.size() == 6 && yy.size() == 6);
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void wormAlwaysStartsInTheCenter() throws Throwable {
        isInterfacesAndImplementations();

        int leveys = 20;
        int korkeus = 10;

        for (int i = 0; i < 10; i++) {
            Object peli = Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
            Object worm = Reflex.reflect(wormgameClass).method("getWorm").returning(wormClass).takingNoParams().invokeOn(peli);

            if (worm == null) {
                fail("Is the class " + wormgameLuokka + "'s method getWorm() public and do you create a new worm in the constructor?");
            }


            List palat = (List) Reflex.reflect(wormClass).method("getPieces").returning(List.class).takingNoParams().invokeOn(worm);

            if (palat == null || palat.size() != 1 || !palat.get(0).getClass().equals(palaClass)) {
                fail("Worm's length should be 1 when it is just created. Therefore worm's method getPieces should return a list which contains only 1 Piece-object.");
            }

            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(palat.get(0));
            assertTrue("Worm's first piece should be in the center of the game when it is just created. If width is " + leveys + ", x-coordinate of the first piece should be " + (leveys / 2), x == (leveys / 2));
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(palat.get(0));
            assertTrue("Worm's first piece should be in the center of the game when it is just created. If height is " + korkeus + ", y-coordinate of the first piece should be " + (korkeus / 2), y == (korkeus / 2));
        }
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void setInitialDelayAndSetDelayCalls() throws Throwable {


        Timer peli = (Timer) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(20, 20);
        assertEquals("In the " + wormgameLuokka + "'s constructor, do you set the initial delay as 2000ms? (Call setInitialDelay(2000)).", 2000, peli.getInitialDelay());
        assertEquals("In the " + wormgameLuokka + "'s constructor, do you set the normal delay as 1000ms? (Call setDelay(1000).", 1000, peli.getDelay());

        boolean loytyi = false;
        for (ActionListener listener : peli.getActionListeners()) {
            if (listener == peli) {
                loytyi = true;
                break;
            }
        }

        assertTrue("In the " + wormgameLuokka + "'s constructor, do you add action listener which is the class-object itself? (Call addActionListener(this)).", loytyi);
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void actionListenerTest() throws Throwable {
        isInterfacesAndImplementations();

        Timer peli = (Timer) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(20, 20);

        boolean loytyi = false;
        for (ActionListener listener : peli.getActionListeners()) {
            if (listener == peli) {
                loytyi = true;
                break;
            }
        }

        assertTrue("In the " + wormgameLuokka + "'s constructor, do you add action listener which is the class-object itself? (Call addActionListener(this)).", loytyi);
    }

    /*
     *
     */
    @Points(Exercise.ID + ".4")
    @Test
    public void updatable() throws Throwable {
        assertTrue("Does the class " + wormgameLuokka + " implement interface ActionListener?", ActionListener.class.isAssignableFrom(wormgameClass));

        Object wormgame = Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(20, 10);
        MockUpdatable mockUpdatable = new MockUpdatable();

        Reflex.reflect(wormgameClass).method("setUpdatable").returningVoid().taking(Updatable.class).invokeOn(wormgame, mockUpdatable);
    }

    @Points(Exercise.ID + ".4")
    @Test
    public void actionListener() throws Throwable {
        int leveys = 20;
        int korkeus = 10;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();

        _M worm = worm(mp);

        asetaUpdatable(mp, mockUpdatable);

        mp.actionPerformed(null);

        List<_P> palat = palat(worm, "");

        if (palat == null || palat.size() != 2 || !palat.get(0).getClass().equals(palaClass)) {
            fail("Do you move worm in the WormGame's method actionPerformed? Do you also add pieces to worm in the first few move-calls?");
        }

        int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(palat.get(0));
        int tokaX = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(palat.get(1));
        assertTrue("x-coordinate of the worm shouldn't change if the worm is moving down.", x == tokaX);
        int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(palat.get(0));
        int tokaY = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(palat.get(1));
        assertTrue("y-coordinate of the worm should increase if the worm is moving down. Substraction of two pieces' y-coordinates should be max. 1.", Math.abs(y - tokaY) == 1);

        assertTrue("Object which implements Updatable-interface, do you call its update-method in the end of WormGame's actionPerformed-method?", mockUpdatable.paivitetty);

    }

    public _M worm(ActionListener al) throws Throwable {
        return _GRef.method("getWorm").returning(_MRef.cls()).takingNoParams().invokeOn((_G) al);
    }

    public _O apple(ActionListener al) throws Throwable {
        return _GRef.method("getApple").returning(_ORef.cls()).takingNoParams().invokeOn((_G) al);
    }

    @Points(Exercise.ID + ".4")
    @Test
    public void whenAppleIsntRanIntoOrItIsntEatenOrItsLocationDoesntChange() throws Throwable {
        int leveys = 20;
        int korkeus = 10;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();

        _O apple = _ORef.constructor().taking(int.class, int.class).invoke(0, 0);
        int alkupX = (Integer) Reflex.reflect(appleClass).method("getX").returning(int.class).takingNoParams().invokeOn(apple);
        int alkupY = (Integer) Reflex.reflect(appleClass).method("getY").returning(int.class).takingNoParams().invokeOn(apple);

        _GRef.method("setApple").returningVoid().taking(_ORef.cls()).invokeOn((_G) mp, apple);

        asetaUpdatable(mp, mockUpdatable);
        mp.actionPerformed(null);

        _M worm = worm(mp);
        List<_P> palat = palat(worm, "");

        if (palat == null || palat.size() != 2 || !palat.get(0).getClass().equals(palaClass)) {
            fail("Do you move worm in the WormGame's method actionPerformed? Do you also add pieces to worm in the first few move-calls?");
        }

        _O haettuApple = apple(mp);

        int x = getX(haettuApple);
        int y = getY(haettuApple);

        assertEquals("When worm is moved in actionPerformed-call and it doesn't run into an apple "
                + "apple shouldn't be eaten.", alkupX, x);
        assertEquals("When worm is moved in actionPerformed-call and it doesn't run into an apple "
                + "apple shouldn't be eaten.", alkupY, y);

        assertTrue("Object which implements Updatable-interface, do you call its update-method in the end of WormGame's actionPerformed-method?", mockUpdatable.paivitetty);
    }

    @Points(Exercise.ID + ".4")
    @Test
    public void whenAppleIsHitAndEatenAndThenNewAppleIsCreated() throws Throwable {
        int leveys = 10;
        int korkeus = 10;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();

        asetaUpdatable(mp, mockUpdatable);

        _O applex = _ORef.constructor().taking(int.class, int.class).invoke(0, 0);
        _GRef.method("setApple").returningVoid().taking(_ORef.cls()).invokeOn((_G) mp, applex);

        _M worm = worm(mp);

        mp.actionPerformed(null);
        mp.actionPerformed(null);

        List palat = palat(worm, "");
        if (palat == null || palat.size() < 3) {
            fail("After two moves, number of worm's pieces was " + palat.size() + " although it should have been " + 3 + ", are you sure that worm is created only in the constructor?");
        }

        int palaX = -1;
        int palaY = -1;

        for (Object pala : palat) {
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(pala);
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(pala);

            if (y > palaY) {
                palaY = y;
            }

            if (x > palaX) {
                palaX = x;
            }
        }

        if (palaY <= 4) {
            fail("Does the worm's y-coordinate increase when it's moving down?");
        }

        _O apple = _ORef.constructor().taking(int.class, int.class).invoke(palaX, palaY + 1);
        _GRef.method("setApple").returningVoid().taking(_ORef.cls()).invokeOn((_G) mp, apple);

        int alkupX = getX(apple);
        int alkupY = getY(apple);

        mp.actionPerformed(null);
        mp.actionPerformed(null);

        palat = palat(worm, "");

        if (palat == null || palat.size() != 4) {
            fail("When worm eats an aple, it's length should increase by one after the next move-call.\n"
                    + ""
                    + kentta(palat, leveys, korkeus));
        }

        _O haettuApple = apple(mp);

        int x = getX(haettuApple);
        int y = getY(haettuApple);

        assertTrue("When worm finds an apple in the actionPerformed-call, apple must be eaten and new apple should be created. \n"
                + "Now location stayed the same\n"
                + "apple: " + x + "," + y + "\n"
                + kentta(palat, 10, 10)
                + "", alkupX != x || alkupY != y);

        assertTrue("New apple cannot be spawned on the worm:\n"
                + "apple: " + x + "," + y + "\n"
                + kentta(palat, 10, 10)
                + "", eiOsuWormon(haettuApple, palat));

        // ei törmäys
        boolean continues = (Boolean) Reflex.reflect(wormgameClass).method("continues").returning(boolean.class).takingNoParams().invokeOn(mp);
        assertTrue("Game should continue when worm hasn't run into itself. Check that method continues() returns true when game is on.", continues);

        assertTrue("Object which implements Updatable-interface, do you call its update-method in the end of WormGame's actionPerformed-method?", mockUpdatable.paivitetty);
    }

    private String kentta(List<_P> palat, int x, int y) throws Throwable {
        char[][] t = new char[x][y];
        for (char[] cs : t) {
            for (int i = 0; i < cs.length; i++) {
                cs[i] = '_';
            }
        }
        for (_P p : palat) {
            t[gtY(p)][gtX(p)] = 'x';
        }
        t[gtY(palat.get(palat.size() - 1))][gtX(palat.get(palat.size() - 1))] = 'h';

        String mj = "";

        for (char[] cs : t) {
            for (char c : cs) {
                mj += c;
            }
            mj += "\n";
        }

        return mj;
    }

    @Points(Exercise.ID + ".4")
    @Test
    public void newAppleDoesntSpawnOnWorm() throws Throwable {
        for (int i = 0; i < 10; i++) {
            int leveys = 5;
            int korkeus = 1;

            ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
            MockUpdatable mockUpdatable = new MockUpdatable();

            asetaUpdatable(mp, mockUpdatable);

            _M worm = worm(mp);
            _O apple = _ORef.constructor().taking(int.class, int.class).invoke(3, 0);
            _GRef.method("setApple").returningVoid().taking(_ORef.cls()).invokeOn((_G) mp, apple);
            mp.actionPerformed(null);
            _O haettuApple = apple(mp);
            assertTrue("New apple cannot be spawned on worm", getY(haettuApple) != 2 && getY(haettuApple) != 3);
        }

    }

    @Points(Exercise.ID + ".4")
    @Test
    public void eatingAppleGrowsWorm() throws Throwable {
        int leveys = 20;
        int korkeus = 10;

        assertTrue("Does the class " + wormgameLuokka + " implement interface ActionListener?", ActionListener.class.isAssignableFrom(wormgameClass));

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();
        asetaUpdatable(mp, mockUpdatable);

        _M worm = worm(mp);
        kaanny(worm, Direction.RIGHT, "");

        mp.actionPerformed(null);
        mp.actionPerformed(null);
        mp.actionPerformed(null);

        List<_P> palat = palat(worm, pakkaus);
        if (palat == null || palat.size() < 3) {
            fail("After three moves, number of worm's pieces was " + palat.size() + ", are you sure that worm is created only in the constructor?");
        }

        int wormX = -1;
        int wormY = -1;

        for (_P pala : palat) {
            int x = gtX(pala);
            int y = gtY(pala);

            if (y > wormY) {
                wormY = y;
            }

            if (x > wormX) {
                wormX = x;
            }
        }

        Reflex.reflect(wormgameClass).method("setApple").returningVoid().taking(appleClass).invokeOn(mp, Reflex.reflect(appleClass).constructor().taking(int.class, int.class).invoke(wormX + 1, wormY));

        int uusiWormX = -1;
        int uusiWormY = -1;

        mp.actionPerformed(null);
        mp.actionPerformed(null);


        if (palat == null || palat.size() != 4) {
            fail("When worm finds an apple in the actionPerformed-method, its length should increase by one. When worm, which length was three pieces, ate an apple and its move-method was called, its length should have been 4. Now it was " + palat.size());
        }

        worm = worm(mp);
        palat = palat(worm, "");

        if (palat == null || palat.size() != 4) {
            fail("When worm finds an apple in the actionPerformed-method, its length should increase by one. When worm, which length was three pieces, ate an apple and its move-method was called, its length should have been 4. Now it was " + palat.size());
        }

        for (Object pala : palat) {
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(pala);
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(pala);

            if (y > uusiWormY) {
                uusiWormY = y;
            }

            if (x > uusiWormX) {
                uusiWormX = x;
            }
        }

        assertEquals("When worm moves right, its y-coordinate shouldn't change.", uusiWormY, wormY);
        assertEquals("When worm moves right two times, its x-coordinate should increase by two.", uusiWormX, wormX + 2);

        Reflex.reflect(wormgameClass).method("setApple").returningVoid().taking(appleClass).invokeOn(mp, Reflex.reflect(appleClass).constructor().taking(int.class, int.class).invoke(uusiWormX + 1, wormY));

        mp.actionPerformed(null);
        mp.actionPerformed(null);

        worm = worm(mp);
        palat = (List) Reflex.reflect(wormClass).method("getPieces").returning(List.class).takingNoParams().invokeOn(worm);

        if (palat == null || palat.size() != 5) {
            fail("When worm finds an apple in the actionPerformed-method, its length should increase by one. When worm, which length was 4 pieces, ate an apple and its move-method was called, its length should have been 5. Now it was " + palat.size());
        }

        assertTrue("Object which implements Updatable-interface, do you call its update-method in the end of WormGame's actionPerformed-method?", mockUpdatable.paivitetty);
    }

    @Points(Exercise.ID + ".4")
    @Test
    public void runningIntoItselfQuits1() throws Throwable {
        int leveys = 6;
        int korkeus = 6;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();

        asetaUpdatable(mp, mockUpdatable);

        _M worm = worm(mp);

        mp.actionPerformed(null);
        mp.actionPerformed(null);
        kaanny(worm, Direction.UP, "");
        String k = kentta(palat(worm, ""), leveys, korkeus);
        mp.actionPerformed(null);
        boolean continues = (Boolean) Reflex.reflect(wormgameClass).method("continues").returning(boolean.class).takingNoParams().invokeOn(mp);
        assertFalse("Game should end, meaning method continues() should return false, when worm runs into itself. \nCheck that worm cannot run into itself so that it has first moved down and then it moves up where he came from.\n"
                + "UP\n" + k + "", continues);

        assertTrue("Object which implements Updatable-interface, do you call its update-method in the end of WormGame's actionPerformed-method?", mockUpdatable.paivitetty);

    }

    @Points(Exercise.ID + ".4")
    @Test
    public void runningIntoItselfQuits2() throws Throwable {
        int leveys = 10;
        int korkeus = 10;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();

        asetaUpdatable(mp, mockUpdatable);

        _M worm = worm(mp);
        mp.actionPerformed(null);
        mp.actionPerformed(null);
        uusiApple(mp, 5, 8);
        mp.actionPerformed(null);
        uusiApple(mp, 5, 9);
        mp.actionPerformed(null);
        kaanny(worm, Direction.RIGHT, "");
        mp.actionPerformed(null);
        kaanny(worm, Direction.UP, "");
        mp.actionPerformed(null);
        kaanny(worm, Direction.LEFT, "");
        String k = kentta(palat(worm, ""), leveys, korkeus);
        mp.actionPerformed(null);

        assertFalse("Game should end, meaning method continues() should return false, when worm runs into itself. \n"
                + "LEFT\n"
                + k, peliJatkuu(mp));

        assertTrue("Object which implements Updatable-interface, do you call its update-method in the end of WormGame's actionPerformed-method?", mockUpdatable.paivitetty);

    }

    @Points(Exercise.ID + ".4")
    @Test
    public void hittingBorderQuits1() throws Throwable {
        int leveys = 4;
        int korkeus = 4;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();
        asetaUpdatable(mp, mockUpdatable);

        mp.actionPerformed(null);
        _M worm = worm(mp);
        String k = kentta(palat(worm, ""), leveys, korkeus);
        mp.actionPerformed(null);
        assertFalse("Game should end, meaning method continues() should return false, when worm hits the border. \n"
                + "DOWN\n"
                + k, peliJatkuu(mp));
    }

    @Points(Exercise.ID + ".4")
    @Test
    public void hittingBorderQuits2() throws Throwable {
        int leveys = 4;
        int korkeus = 4;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();
        asetaUpdatable(mp, mockUpdatable);

        mp.actionPerformed(null);
        _M worm = worm(mp);
        kaanny(worm, Direction.RIGHT, "");
        mp.actionPerformed(null);
        String k = kentta(palat(worm, ""), leveys, korkeus);
        mp.actionPerformed(null);
        assertFalse("Game should end, meaning method continues() should return false, when worm hits the border. \n"
                + "RIGHT\n"
                + k, peliJatkuu(mp));
    }

    @Points(Exercise.ID + ".4")
    @Test
    public void hittingBorderQuits3() throws Throwable {
        int leveys = 4;
        int korkeus = 4;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();
        asetaUpdatable(mp, mockUpdatable);

        mp.actionPerformed(null);
        _M worm = worm(mp);
        kaanny(worm, Direction.LEFT, "");
        mp.actionPerformed(null);
        mp.actionPerformed(null);
        String k = kentta(palat(worm, ""), leveys, korkeus);
        mp.actionPerformed(null);
        assertFalse("Game should end, meaning method continues() should return false, when worm hits the border. \n"
                + "LEFT\n"
                + k, peliJatkuu(mp));
    }

    @Points(Exercise.ID + ".4")
    @Test
    public void hittingBorderQuits4() throws Throwable {
        int leveys = 4;
        int korkeus = 4;

        ActionListener mp = (ActionListener) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();
        asetaUpdatable(mp, mockUpdatable);
        _M worm = worm(mp);
        kaanny(worm, Direction.LEFT, "");
        mp.actionPerformed(null);
        kaanny(worm, Direction.UP, "");
        mp.actionPerformed(null);
        mp.actionPerformed(null);
        String k = kentta(palat(worm, ""), leveys, korkeus);
        mp.actionPerformed(null);
        assertFalse("Game should end, meaning method continues() should return false, when worm hits the border. \n"
                + "UP\n"
                + k, peliJatkuu(mp));
    }

    @Test
    @Points(Exercise.ID + ".4")
    public void delayChangesWhenGameProgresses() throws Throwable {
        isInterfacesAndImplementations();

        Timer peli = (Timer) Reflex.reflect(wormgameClass).constructor().taking(int.class, int.class).invoke(20, 20);
        assertEquals("In the " + wormgameLuokka + "'s constructor, do you set the initial delay as 2000ms? (Call setInitialDelay(2000)).", 2000, peli.getInitialDelay());
        assertEquals("In the " + wormgameLuokka + "'s constructor, do you set the normal delay as 1000ms? (Call setDelay(1000)).", 1000, peli.getDelay());

        boolean loytyi = false;
        for (ActionListener listener : peli.getActionListeners()) {
            if (listener == peli) {
                loytyi = true;
                break;
            }
        }

        assertTrue("Do you set the WormGame to listen to its own events?", loytyi);

        ActionListener actionListener = (ActionListener) peli;
        MockUpdatable mockUpdatable = new MockUpdatable();
        asetaUpdatable(actionListener, mockUpdatable);

        actionListener.actionPerformed(null);
        actionListener.actionPerformed(null);
        actionListener.actionPerformed(null);

        assertTrue("Does the game's update rate speed up when worm grows? Game's update rate was one update per " + peli.getDelay() + " milliseconds, even though length of the worm was already 3.", peli.getDelay() < 500);
    }

    @Test
    @Points(Exercise.ID + ".5")
    public void keyboardListener() throws Throwable {
        assertNotNull("Have you created class " + nappaimistonkuuntelijaLuokka + ", which is inside the package " + uiPakkaus + "?", nappaimistonKuuntelijaClass);

        assertTrue("Does the class " + nappaimistonkuuntelijaLuokka + " implement interface KeyListener?", KeyListener.class.isAssignableFrom(nappaimistonKuuntelijaClass));

        assertTrue("Create constructor public KeyboardListener(Worm worm) for keyboard listener",
                _NRef.constructor().taking(_MRef.cls()).isPublic());

        _M worm = newWorm(1, 1, Direction.DOWN, "");

        KeyListener listener = (KeyListener) _NRef.constructor().taking(_MRef.cls()).invoke(worm);
        WormGameTest.MockKeyEvent ylos = new WormGameTest.MockKeyEvent(KeyEvent.VK_UP);
        String v = "Worm worm = new Worm(1, 1, Direction.DOWN);\n"
                + "KeyboardListener nk = KeyboardListener(worm);\n"
                + "nk.keyPressed( KeyEvent.VK_UP );\n"
                + "worm.getDirection();\n";

        List<_P> orig = new ArrayList(palat(worm, ""));

        try {
            listener.keyPressed(ylos);
        } catch (Throwable t) {
            fail("Error caused by code \n" + v + "additional information: " + t);
        }

        Direction nykyinenDirection = (Direction) Reflex.reflect(wormClass).method("getDirection").returning(Direction.class).takingNoParams().invokeOn(worm);

        assertEquals("When up arrow key is pressed, worm's direction should be set as Direction.UP\n"
                + v, Direction.UP, nykyinenDirection);

        List<_P> nyt = palat(worm, "");

        assertTrue("Worm cannot grow or change its location when direction is changed, check code:\n" + v, samat(orig, nyt));

        grow(worm, "");
        assertEquals(1, (int) pituus(worm, ""));

        //

        orig = new ArrayList(palat(worm, ""));
        WormGameTest.MockKeyEvent oikealle = new WormGameTest.MockKeyEvent(KeyEvent.VK_RIGHT);

        v = "Worm worm = new Worm(1, 1, Direction.UP);\n"
                + "KeyboardListener nk = KeyboardListener(worm);\n"
                + "nk.keyPressed( KeyEvent.VK_RIGHT );\n"
                + "worm.getDirection();\n";

        try {
            listener.keyPressed(oikealle);
        } catch (Throwable t) {
            fail("Error caused by code \n" + v + "additional information: " + t);
        }

        nykyinenDirection = (Direction) Reflex.reflect(wormClass).method("getDirection").returning(Direction.class).takingNoParams().invokeOn(worm);

        assertEquals("When right arrow key is pressed, worm's direction should be set as Direction.RIGHT\n"
                + v, Direction.RIGHT, nykyinenDirection);

        nyt = palat(worm, "");

        assertTrue("Worm cannot grow or change its location when direction is changed, check code:\n" + v, samat(orig, nyt));

        //

        orig = new ArrayList(palat(worm, ""));
        WormGameTest.MockKeyEvent vas = new WormGameTest.MockKeyEvent(KeyEvent.VK_LEFT);

        v = "Worm worm = new Worm(1, 1, Direction.RIGHT);\n"
                + "KeyboardListener nk = KeyboardListener(worm);\n"
                + "nk.keyPressed( KeyEvent.VK_LEFT );\n"
                + "worm.getDirection();\n";

        try {
            listener.keyPressed(vas);
        } catch (Throwable t) {
            fail("Error caused by code \n" + v + "additional information: " + t);
        }

        nykyinenDirection = (Direction) Reflex.reflect(wormClass).method("getDirection").returning(Direction.class).takingNoParams().invokeOn(worm);

        assertEquals("When left arrow key is pressed, worm's direction should be set as Direction.LEFT\n"
                + v, Direction.LEFT, nykyinenDirection);

        nyt = palat(worm, "");

        assertTrue("Worm cannot grow or change its location when direction is changed, check code:\n" + v, samat(orig, nyt));

        //

        orig = new ArrayList(palat(worm, ""));
        WormGameTest.MockKeyEvent alas = new WormGameTest.MockKeyEvent(KeyEvent.VK_DOWN);

        v = "Worm worm = new Worm(1, 1, Direction.LEFT);\n"
                + "KeyboardListener nk = KeyboardListener(worm);\n"
                + "nk.keyPressed( KeyEvent.VK_DOWN );\n"
                + "worm.getDirection();\n";

        try {
            listener.keyPressed(alas);
        } catch (Throwable t) {
            fail("Error caused by code \n" + v + "additional information: " + t);
        }

        nykyinenDirection = (Direction) Reflex.reflect(wormClass).method("getDirection").returning(Direction.class).takingNoParams().invokeOn(worm);

        assertEquals("When down arrow key is pressed, worm's direction should be set as Direction.DOWN\n"
                + v, Direction.DOWN, nykyinenDirection);

        nyt = palat(worm, "");

        assertTrue("Worm cannot grow or change its location when direction is changed, check code:\n" + v, samat(orig, nyt));

        //

        move(worm, "");
        move(worm, "");

        v = "Worm worm = new Worm(1, 1, Direction.DOWN);\n"
                + "KeyboardListener nk = KeyboardListener(worm);\n"
                + "worm.move();\n"
                + "worm.move();\n"
                + "nk.keyPressed( KeyEvent.VK_DOWN );\n";

        try {
            listener.keyPressed(alas);
        } catch (Throwable t) {
            fail("Error caused by code \n" + v + "additional information: " + t);
        }

        move(worm, "");
        v = "Worm worm = new Worm(1, 1, Direction.DOWN);\n"
                + "KeyboardListener nk = KeyboardListener(worm);\n"
                + "worm.move();\n"
                + "worm.move();\n"
                + "nk.keyPressed( KeyEvent.VK_DOWN );\n"
                + "worm.move();\n"
                + "m.getLength()\n";

        assertEquals("Are you sure that keyboard listener doesn't call worm's method grow?\n"
                + v, 3, (int) pituus(worm, ""));

        try {
            listener.keyReleased(vas);
        } catch (Throwable t) {
            fail("Check that KeyboardListener's method keyReleased doesn't contain any lines of code. "
                    + "\nNow its execution causes an exception: " + t);
        }

        try {
            listener.keyTyped(vas);
        } catch (Throwable t) {
            fail("Check that KeyboardListener's method keyPressed doesn't contain any lines of code. "
                    + "\nNow its execution causes an exception: " + t);
        }
    }

    @Test
    @Points(Exercise.ID + ".6")
    public void isDrawingBoard() throws Throwable {

        int sivunPituus = 10;
        assertNotNull("Have you created class " + piirtoalustaLuokka + " inside the package " + uiPakkaus + "?", piirtoalustaClass);

        assertTrue("Create constructor public DrawingBoard(WormGame peli, int pieceSideLength) for class DrawingBoard",
                _ARef.constructor().taking(_GRef.cls(), int.class).isPublic());

        assertTrue("Does the class " + piirtoalustaLuokka + " inherit class JPanel?", JPanel.class.isAssignableFrom(piirtoalustaClass));

        assertTrue("Does the class " + piirtoalustaLuokka + " implement interface Updatable?", Updatable.class.isAssignableFrom(piirtoalustaClass));

        int leveys = 20;
        int korkeus = 10;

        String v = ""
                + "WormGame mp = new WormGame(20,10);\n"
                + "DrawingBoard pa = new DrawingBoard(mp, 10)\n;";

        _G wormgame = _GRef.constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();

        asetaUpdatable((ActionListener) wormgame, mockUpdatable);

        _M worm = worm((ActionListener) wormgame);

        ActionListener actionListener = (ActionListener) wormgame;
        actionListener.actionPerformed(null);
        actionListener.actionPerformed(null);
        actionListener.actionPerformed(null);

        List<Point> madonPiecet = new ArrayList<Point>();

        List<_P> palat = palat(worm, v);

        for (_P pala : palat) {
            madonPiecet.add(new Point(gtX(pala), gtY(pala)));
        }

        _O apple = apple(actionListener);

        int appleX = getX(apple);
        int appleY = getY(apple);

        Point applePoint = new Point(appleX, appleY);

        JPanel alusta = (JPanel) _ARef.constructor().taking(_GRef.cls(), int.class).invoke(wormgame, sivunPituus);
        WormGameTest.MockGraphics mc = new WormGameTest.MockGraphics();

        v = ""
                + "WormGame mp = new WormGame(20,10);\n"
                + "mp.actionPerformed(null)\n"
                + "mp.actionPerformed(null)\n"
                + "mp.actionPerformed(null)\n"
                + "DrawingBoard pa = new DrawingBoard(mp, 10);\n"
                + "pa.paintComonent(g);\n";

        assertFalse("Class DrawingBoard should overwrite JPanel's method public void paintComponent(Graphics g)"
                + " so that worm and apple are drawn.", piirtoalustaPaintComponentMethod == null);

        _ARef.method((_A) alusta, "paintComponent").returningVoid().taking(Graphics.class).withNiceError("error caused by code:" + v).invoke(mc);

        assertTrue("Use Graphics-object's fillOval-method for drawing the apple. "
                + "Apple should be drawn in drawing board's paintComponent-method. \n"
                + "executed code \n" + v + "fillOval() method calls: " + mc.fillOvalKutsut.size(), mc.fillOvalKutsut.size() == 1);
        assertEquals("Do you use Graphics-object's fill3DRect-method for drawing the worm. "
                + "There must be one call per each piece of the worm!"
                + "\nWorm should be drawn in drawing board's paintComponent-method.\n"
                + "When executed code\n" + v + ""
                + "length of the worm is 3 and fill3DRect-calls: ", 3, mc.fill3DRectKutsut.size());

        int applenVasenYlaKulmaX = applePoint.x * sivunPituus;
        int applenVasenYlaKulmaY = applePoint.y * sivunPituus;
        Rectangle appleOval = (Rectangle) mc.fillOvalKutsut.get(0);
        assertEquals("When side length of the piece is " + sivunPituus + ", and apple's x-coordinate is "
                + applePoint.x + ", x-coordinate of the fillOval-call should be "
                + applenVasenYlaKulmaX, applenVasenYlaKulmaX, appleOval.x);
        assertEquals("When side length of the piece is " + sivunPituus + ", and apple's y-coordinate is "
                + applePoint.y + ", y-coordinate of the fillOval-call should be "
                + applenVasenYlaKulmaY, applenVasenYlaKulmaY, appleOval.y);

        assertTrue("When side length of the piece is " + sivunPituus + ", when drawing apple, fillOval-method "
                + "must be given width and height as " + sivunPituus + "\nwidth was " + appleOval.width + " and height " + appleOval.height,
                sivunPituus == appleOval.width && sivunPituus == appleOval.height);

        for (Point pala : madonPiecet) {
            int palanVasenYlaKulmaX = pala.x * sivunPituus;
            int palanVasenYlaKulmaY = pala.y * sivunPituus;

            boolean loytyi = false;

            List<Rectangle> mcf = (List<Rectangle>) mc.fill3DRectKutsut;

            for (Rectangle wormPiece : mcf) {
                if (wormPiece.x == palanVasenYlaKulmaX && wormPiece.y == palanVasenYlaKulmaY) {
                    loytyi = true;
                    break;
                }
            }

            if (!loytyi) {
                fail("When x-coordinate of worm's piece is " + pala.x + " and side length is " + sivunPituus
                        + ", fill3DRect-method's given x-coordinate should be " + palanVasenYlaKulmaX
                        + ". \nSame way, if y-coordinate of piece is " + pala.y + ", fill3DRect-method's given y-coordinate "
                        + "should be " + palanVasenYlaKulmaY
                        + "\nwhen worm's pieces are in: " + f(madonPiecet) + "\n"
                        + "you call:\n" + ff(mcf));
            }

            for (Rectangle rectangle : mcf) {
                assertTrue("When side length of the piece is " + sivunPituus + ", fill3DRect-method "
                        + "should be given width and height as " + sivunPituus + "\n"
                        + "Now you call: \n" + ff(mcf), rectangle.height > sivunPituus - 2 && rectangle.width > sivunPituus - 2);
            }
        }

    }

    @Test
    @Points(Exercise.ID + ".6")
    public void drawingBoardNoticesTheSideLength() throws Throwable {

        int sivunPituus = 20;
        assertNotNull("Have you created class " + piirtoalustaLuokka + " inside the package " + uiPakkaus + "?", piirtoalustaClass);

        assertTrue("Create constructor public DrawingBoard(WormGame game, int pieceSideLength) for class DrawingBoard",
                _ARef.constructor().taking(_GRef.cls(), int.class).isPublic());

        assertTrue("Does the class " + piirtoalustaLuokka + " inherit class JPanel?", JPanel.class.isAssignableFrom(piirtoalustaClass));

        assertTrue("Does the class " + piirtoalustaLuokka + " implement interface Updatable?", Updatable.class.isAssignableFrom(piirtoalustaClass));

        int leveys = 20;
        int korkeus = 10;

        String v = ""
                + "WormGame mp = new WormGame(20,10);\n"
                + "DrawingBoard pa = new DrawingBoard(mp, 20)\n;";

        _G wormgame = _GRef.constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        MockUpdatable mockUpdatable = new MockUpdatable();

        asetaUpdatable((ActionListener) wormgame, mockUpdatable);

        _M worm = worm((ActionListener) wormgame);

        ActionListener actionListener = (ActionListener) wormgame;
        actionListener.actionPerformed(null);
        actionListener.actionPerformed(null);
        actionListener.actionPerformed(null);

        List<Point> madonPiecet = new ArrayList<Point>();

        List<_P> palat = palat(worm, v);

        for (_P pala : palat) {
            madonPiecet.add(new Point(gtX(pala), gtY(pala)));
        }

        _O apple = apple(actionListener);

        int appleX = getX(apple);
        int appleY = getY(apple);

        Point applePoint = new Point(appleX, appleY);

        JPanel alusta = (JPanel) _ARef.constructor().taking(_GRef.cls(), int.class).invoke(wormgame, sivunPituus);
        WormGameTest.MockGraphics mc = new WormGameTest.MockGraphics();

        v = ""
                + "WormGame mp = new WormGame(20,10);\n"
                + "DrawingBoard pa = new DrawingBoard(mp, 20);\n"
                + "pa.paintComonent(g);\n";

        assertFalse("Class DrawingBoard should overwrite JPanel's method "
                + "public void paintComponent(Graphics g) "
                + "so that you draw worm and apple in the paintComponent-method.",
                piirtoalustaPaintComponentMethod == null);

        _ARef.method((_A) alusta, "paintComponent").returningVoid().taking(Graphics.class).withNiceError("error caused by code:" + v).invoke(mc);

        assertTrue("Do you use Graphics-object's fillOval-method for drawing the apple? "
                + "Apple should be drawn in drawing board's paintComponent-method.. \n"
                + "executed code \n" + v + "fillOvat() metodikutsuja: " + mc.fillOvalKutsut.size(), mc.fillOvalKutsut.size() == 1);
        assertTrue("Do you use Graphics-object's fill3DRect-method for drawing the worm? "
                + "Worm should be drawn in drawing board's paintComponent-method.\n"
                + "executed code\n" + v + "fill3DRect-calls: " + mc.fill3DRectKutsut.size(), mc.fill3DRectKutsut.size() > 2);

        int applenVasenYlaKulmaX = applePoint.x * sivunPituus;
        int applenVasenYlaKulmaY = applePoint.y * sivunPituus;
        Rectangle appleOval = (Rectangle) mc.fillOvalKutsut.get(0);
        assertEquals("When side length of the piece is " + sivunPituus + ", and apple's x-coordinate is "
                + applePoint.x + ", fillOval-call's x-coordinate should be "
                + applenVasenYlaKulmaX, applenVasenYlaKulmaX, appleOval.x);
        assertEquals("When side length of the piece is  " + sivunPituus + ", and apple's y-coordinate is "
                + applePoint.y + ", fillOval-call's x-coordinate should be "
                + applenVasenYlaKulmaY, applenVasenYlaKulmaY, appleOval.y);

        assertTrue("When side length of the piece is  " + sivunPituus + ", fillOval-method should be given "
                + "width and height as " + sivunPituus + "\nwidth was " + appleOval.width + " and height " + appleOval.height,
                sivunPituus == appleOval.width && sivunPituus == appleOval.height);

        for (Point pala : madonPiecet) {
            int palanVasenYlaKulmaX = pala.x * sivunPituus;
            int palanVasenYlaKulmaY = pala.y * sivunPituus;

            boolean loytyi = false;

            List<Rectangle> mcf = (List<Rectangle>) mc.fill3DRectKutsut;

            for (Rectangle wormPiece : mcf) {
                if (wormPiece.x == palanVasenYlaKulmaX && wormPiece.y == palanVasenYlaKulmaY) {
                    loytyi = true;
                    break;
                }
            }

            if (!loytyi) {
                fail("When x-coordinate of piece is " + pala.x + " and side length is " + sivunPituus
                        + ", fill3DRect-method's x-coordinate should be " + palanVasenYlaKulmaX
                        + ". \nSame way, if y-coordinate of piece is " + pala.y + ", fill3DRect-call's "
                        + "y-coordinate should be " + palanVasenYlaKulmaY
                        + "\nWhen worm's pieces are in: " + f(madonPiecet) + "\n"
                        + "you call:\n" + ff(mcf));
            }

            for (Rectangle rectangle : mcf) {
                assertTrue("When side length of the piece is  " + sivunPituus + ", fill3DRect-method's "
                        + "width and height should be given as " + sivunPituus + "\n"
                        + "Now you call: \n" + ff(mcf), rectangle.height > sivunPituus - 2 && rectangle.width > sivunPituus - 2);
            }
        }

        boolean musta = false;
        boolean punainen = false;
        List<Color> varit = mc.varit;
        for (Color color : varit) {
            if (Color.RED == color) {
                punainen = true;
            }
            if (Color.BLACK == color) {
                musta = true;
            }
        }
        assertTrue("When drawing worm and apple you should use black and red as their colours. Now red wasn't used.", punainen);
        assertTrue("When drawing worm and apple you should use black and red as their colours. Now black wasn't used.", musta);

    }

    @Test
    @Points(Exercise.ID + ".6")
    public void updateCallsRepaint() {
        try {
            Scanner lukija = new Scanner(new File("src/wormgame/gui/DrawingBoard.java"));
            int metodissa = 0;

            int repaint = 0;
            while (lukija.hasNext()) {

                String rivi = lukija.nextLine();

                if (rivi.indexOf("//") > -1) {
                    rivi = rivi.substring(0, rivi.indexOf("//"));
                }

                if (rivi.contains("void") && rivi.contains("update(")) {
                    metodissa++;

                } else if (metodissa > 0) {
                    if (rivi.contains("{") && !rivi.contains("}")) {
                        metodissa++;
                    }

                    if (rivi.contains("}") && !rivi.contains("{")) {
                        metodissa--;
                    }

                    if (rivi.contains("repaint(")) {
                        repaint++;
                    }


                } else {

                    if (rivi.contains("repaint(")) {
                        fail("Class DrawingBoard's method update should call method inherited from JLabel which is"
                                + " repaint(). \n"
                                + "You shouldn't call repaint()-method from anywhere else, but now forbidden call was found!");
                    }
                }

            }
            assertTrue("Class DrawingBoard's method update should call method inherited from JLabel which is"
                    + " repaint()", repaint == 1);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Points(Exercise.ID + ".6")
    public void paintComponentCallsSuperClassMethod() {
        try {
            Scanner lukija = new Scanner(new File("src/wormgame/gui/DrawingBoard.java"));
            int metodissa = 0;

            boolean eka = false;;
            while (lukija.hasNext()) {

                String rivi = lukija.nextLine();

                if (rivi.indexOf("//") > -1) {
                    rivi = rivi.substring(0, rivi.indexOf("//"));
                }

                if (rivi.contains("void") && rivi.contains("paintComponent(")) {
                    metodissa++;
                    eka = true;

                } else if (metodissa > 0) {
                    if (eka) {
                        if (!rivi.contains("super.paintComponent(")) {
                            fail("Method paintComponent should immediately call superclass' version of the method so"
                                    + " first line should be super.paintComponent(g);");
                        }
                        eka = false;
                    } else {
                        if (rivi.contains("super.paintComponent(")) {
                            fail("call super.paintComponent(g); was in the wrong place, it should be first line of the method paintComponent");
                        }
                    }

                    if (rivi.contains("{") && !rivi.contains("}")) {
                        metodissa++;
                    }

                    if (rivi.contains("}") && !rivi.contains("{")) {
                        metodissa--;
                    }

                } else {

                    if (rivi.contains("super.paintComponent(")) {
                        fail("call super.paintComponent(g); was in the wrong place, it should be first line of the method paintComponent");
                    }
                }

            }


        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Points(Exercise.ID + ".7")
    public void isUserInterface() throws Throwable {

        Reflex.ClassRef<UserInterface> KRef = Reflex.reflect("wormgame.gui.UserInterface");
        assertTrue("Does the class UserInterface have constructor public UserInterface(WormGame game, int sideLength)?",
                KRef.constructor().taking(_GRef.cls(), int.class).isPublic());

        int leveys = 20;
        int korkeus = 10;

        int sivunLeveys = 20;
        _G wormgame = _GRef.constructor().taking(int.class, int.class).invoke(leveys, korkeus);
        UserInterface kali = KRef.constructor().taking(_GRef.cls(), int.class).invoke(wormgame, sivunLeveys);

        JFrame frame = new JFrame();

        for (Field f : UserInterface.class.getDeclaredFields()) {
            if (f.toString().contains("JFrame")) {
                f.setAccessible(true);
                f.set(kali, frame);
            }
        }

        WormGameTest.MockContainer mockContainer = new WormGameTest.MockContainer();

        KRef.method(kali, "createComponents").returningVoid().taking(Container.class).getMethod().setAccessible(true);
        KRef.method(kali, "createComponents").returningVoid().taking(Container.class).invoke(mockContainer);

        assertTrue("In UserInterface's method createComponents, do you add drawing board to Container-object which is given as a parameter? "
                + "\nDo you also add keyboard listener to frame-object in the end of createComponents-method?",
                mockContainer.lisatyt.size() > 0);

        assertTrue("Keyboard listener should be added to frame-object in the method createComponents,"
                + " now it was added to Container!", mockContainer.getKeyListeners().length==0);

        assertTrue("Add keyboard listener to frame-object in the method createComponents", frame.getKeyListeners().length > 0);

        // tänne lisää, langotus kunnossa??

        assertTrue("Create method public Updatable getUpdatable() for class UserInterface", KRef.method("getUpdatable").returning(Updatable.class).takingNoParams().isPublic());

        Updatable updatable = (Updatable) KRef.method("getUpdatable").returning(Updatable.class).takingNoParams().invokeOn(kali);
        assertTrue("Does the UserInterface's method getUpdatable() return drawing board which was added to container-object?", mockContainer.lisatyt.contains(updatable));

    }

    /*
     * TODO: langotus
     *
     *
     */
    private String f(List<Point> points) {
        String m = "";

        for (Point point : points) {
            m += " (" + point.x + "," + point.y + ") ";
        }

        return m;
    }

    private String ff(List<Rectangle> rects) {
        String m = "";

        for (Rectangle rect : rects) {
            m += " g.draw3DRectangle(" + rect.x + "," + rect.y + "," + rect.width + "," + rect.height + ", true);\n";
        }

        return m;
    }

    private boolean peliJatkuu(ActionListener mp) throws Throwable {
        return (Boolean) Reflex.reflect(wormgameClass).method("continues").returning(boolean.class).takingNoParams().invokeOn(mp);
    }

    public void uusiApple(ActionListener mp, int x, int y) throws Throwable {
        _GRef.method("setApple").returningVoid().taking(appleClass).invokeOn((_G) mp, Reflex.reflect(appleClass).constructor().taking(int.class, int.class).invoke(x, y));
    }

    private boolean eiOsuWormon(_O o, List<_P> p) throws Throwable {
        for (_P _p : p) {
            if (gtX(_p) == getX(o) && gtY(_p) == getY(o)) {
                return false;
            }
        }

        return true;
    }

    private int gtX(_P o) throws Throwable {
        return _PRef.method(o, "getX").returning(int.class).takingNoParams().invoke();
    }

    private int gtY(_P o) throws Throwable {
        return _PRef.method(o, "getY").returning(int.class).takingNoParams().invoke();
    }

    private int getX(_O o) throws Throwable {
        return _ORef.method(o, "getX").returning(int.class).takingNoParams().invoke();
    }

    private int getY(_O o) throws Throwable {
        return _ORef.method(o, "getY").returning(int.class).takingNoParams().invoke();
    }

    private void moveYmpyra(_M worm, int origX, int origY) throws Throwable {
        String v = "Worm = new Worm(1,1,Direction.RIGHT);\n"
                + "worm.move();\n"
                + "worm.move();\n'"
                + "worm.move();\n"
                + "worm.grow();\n"
                + "worm.move();\n";

        move(worm, v);
        move(worm, v);
        move(worm, v);
        grow(worm, v);
        move(worm, v);

        int pituus = pituus(worm, "");
        assertEquals(v + "worm.getLength();\n", 4, pituus);

        move(worm, v);

        v += "worm.move();\n";
        pituus = pituus(worm, "");
        assertEquals(v + "worm.getLength();\n", 4, pituus);

        List palat = palat(worm, v);

        if (palat == null || palat.size() != 4 || !palat.get(0).getClass().equals(palaClass)) {
            fail("When worm's length is 4, method getPieces should return a list which contains 4 Piece-objects.");
        }

        for (Object p : palat) {
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("When direction of the piece is Direction.RIGHT, its y-coordinate's value shouldn't change when piece moves.", y == 1);
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("When direction of the piece is Direction.RIGHT, its x-coordinate's value should increase when piece moves.", x > 1);
        }

        v += "worm.setDirection(Direction.DOWN);\n";

        kaanny(worm, Direction.DOWN, v);

        v += "5 times: worm.move();\n";

        move(worm, v);
        move(worm, v);
        move(worm, v);
        move(worm, v);
        move(worm, v);

        palat = palat(worm, v);

        int alkupX = -100;
        for (Object p : palat) {
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("When direction of the worm is Direction.DOWN, its y-coordinate values should increase when worm moves down.", y > 1);
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(p);
            if (alkupX == -100) {
                alkupX = x;
                continue;
            }

            assertTrue("When direction of the worm is Direction.DOWN, its x-coordinate values should stay the same when worm moves down.", x == alkupX);
        }

        v += "worm.setDirection(Direction.LEFT);\n";

        kaanny(worm, Direction.LEFT, v);

        v += "5 times: worm.move();\n";

        move(worm, v);
        move(worm, v);
        move(worm, v);
        move(worm, v);
        move(worm, v);

        palat = (List) _MRef.method("getPieces").returning(List.class).takingNoParams().invokeOn(worm);

        int alkupY = -100;
        for (Object p : palat) {
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(p);
            if (alkupY == -100) {
                alkupY = y;
                continue;
            }
            assertTrue("When direction of the worm is Direction.LEFT, its y-coordinate values should stay the same.", y == alkupY);
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(p);

            assertTrue("When direction of the worm is Direction.LEFT, its x-coordinate value should decrease when worm moves.", x < 5);
        }

        v += "worm.setDirection(Direction.UP);\n";

        kaanny(worm, Direction.UP, v);

        v += "5 times: worm.move();\n";

        move(worm, v);
        move(worm, v);
        move(worm, v);
        move(worm, v);
        move(worm, v);

        palat = (List) _MRef.method("getPieces").returning(List.class).takingNoParams().invokeOn(worm);

        boolean jokuAlkupisteessa = false;
        for (Object p : palat) {
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("When direction of the worm is Direction.UP, its y-coordinate values should decrease.", y < 5);
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("When direction of the worm is Direction.UP, its x-coordinate values should stay the same.", x < 5);

            if (x == origX && y == origY) {
                jokuAlkupisteessa = true;
            }
        }

        if (!jokuAlkupisteessa) {
            fail("When moving 5 times in every direction, worm should end up in the initial location.");
        }

    }

    private _P newPiece(int x, int y) throws Throwable {
        _P toinenKakkosNelonen = _PRef.constructor().taking(int.class, int.class).invoke(x, y);
        return toinenKakkosNelonen;
    }

    private boolean samat(List<_P> ex, List<_P> was) throws Throwable {
        if (ex.size() != was.size()) {
            return false;
        }

        for (int i = 0; i < was.size(); i++) {
            _P p1 = ex.get(i);
            _P p2 = was.get(i);

            int p1x = _PRef.method("getX").returning(int.class).takingNoParams().invokeOn(p1);
            int p2x = _PRef.method("getX").returning(int.class).takingNoParams().invokeOn(p2);
            int p1y = _PRef.method("getY").returning(int.class).takingNoParams().invokeOn(p1);
            int p2y = _PRef.method("getY").returning(int.class).takingNoParams().invokeOn(p2);

            if (p1 == null || p2 == null) {
                return false;
            }
            if (p1x != p2x) {
                return false;
            }
            if (p1y != p2y) {
                return false;
            }
        }

        return true;
    }

    private List<_P> palat(_M worm, String v) throws Throwable {
        List<_P> ret;
        ret = _MRef.method(worm, "getPieces").returning(List.class).takingNoParams().withNiceError(v).invoke();
        return ret;
    }

    private Integer pituus(_M worm, String v) throws Throwable {
        return _MRef.method(worm, "getLength").returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    private _M newWorm(int x, int y, Direction s, String v) throws Throwable {
        return _MRef.constructor().taking(int.class, int.class, Direction.class).withNiceError(v).invoke(x, y, s);
    }

    private void move(_M worm, String v) throws Throwable {
        _MRef.method(worm, "move").returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    private void kaanny(_M worm, Direction s, String v) throws Throwable {
        _MRef.method(worm, "setDirection").returningVoid().taking(Direction.class).withNiceError(v).invoke(s);
    }

    private void grow(_M worm, String v) throws Throwable {
        _MRef.method(worm, "grow").returningVoid().takingNoParams().withNiceError(v).invoke();
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
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }

    private String s(String klassName) {
        return klassName.substring(klassName.lastIndexOf(".") + 1);
    }

    private boolean sis(List<_P> exp, _P p) throws Throwable {
        for (_P _p : exp) {
            if (sama(_p, p)) {
                return true;
            }
        }

        return false;
    }

    private boolean sama(_P p1, _P p2) throws Throwable {
        int p1x = _PRef.method("getX").returning(int.class).takingNoParams().invokeOn(p1);
        int p2x = _PRef.method("getX").returning(int.class).takingNoParams().invokeOn(p2);
        int p1y = _PRef.method("getY").returning(int.class).takingNoParams().invokeOn(p1);
        int p2y = _PRef.method("getY").returning(int.class).takingNoParams().invokeOn(p2);

        if (p1 == null || p2 == null) {
            return false;
        }
        if (p1x != p2x) {
            return false;
        }
        if (p1y != p2y) {
            return false;
        }

        return true;
    }

    private Boolean runsIntoItself(_M m, String v) throws Throwable {
        return _MRef.method(m, "runsIntoItself").returning(boolean.class).takingNoParams().withNiceError(v).invoke();
    }

    private void asetaUpdatable(ActionListener al, MockUpdatable mockUpdatable) throws Throwable {
        _GRef.method("setUpdatable").returningVoid().taking(Updatable.class).invokeOn((_G) al, mockUpdatable);
    }

    private class MockKeyEvent extends KeyEvent {

        public MockKeyEvent(int keyCode) {
            super(new WormGameTest.MockComponent(), keyCode, 1, 1, keyCode);
        }
    }

    private class MockComponent extends Component {
    }

    private class MockContainer extends Container {

        List<Component> lisatyt = new ArrayList<Component>();

        @Override
        public Component add(Component cmpnt) {
            Component cp = super.add(cmpnt);
            lisatyt.add(cmpnt);
            return cp;
        }
    }

    private class MockGraphics extends Graphics {

        List<Rectangle> fillOvalKutsut = new ArrayList<Rectangle>();
        List<Rectangle> fill3DRectKutsut = new ArrayList<Rectangle>();
        List<Color> varit = new ArrayList();
        int ensin = 0;

        @Override
        public Graphics create() {
            return this;
        }

        @Override
        public void translate(int i, int i1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Color getColor() {
            if (varit.isEmpty()) {
                return Color.WHITE;
            }

            return varit.get(varit.size() - 1);
        }

        @Override
        public void setColor(Color color) {
            varit.add(color);
        }

        @Override
        public void setPaintMode() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setXORMode(Color color) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Font getFont() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setFont(Font font) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public FontMetrics getFontMetrics(Font font) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Rectangle getClipBounds() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clipRect(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setClip(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Shape getClip() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setClip(Shape shape) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void copyArea(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawLine(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillRect(int i, int i1, int i2, int i3) {
        }

        @Override
        public void clearRect(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawOval(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillOval(int i, int i1, int i2, int i3) {
            if (ensin == 0) {
                ensin = 1;
            }
            fillOvalKutsut.add(new Rectangle(i, i1, i2, i3));
        }

        @Override
        public void drawArc(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillArc(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawPolyline(int[] ints, int[] ints1, int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawPolygon(int[] ints, int[] ints1, int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillPolygon(int[] ints, int[] ints1, int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawString(String string, int i, int i1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawString(AttributedCharacterIterator aci, int i, int i1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, int i2, int i3, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, Color color, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, int i2, int i3, Color color, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void dispose() {
            return;
        }

        @Override
        public void fill3DRect(int i, int i1, int i2, int i3, boolean bln) {
            if (ensin == 0) {
                ensin = 2;
            }
            fill3DRectKutsut.add(new Rectangle(i, i1, i2, i3));
        }
    }
}
class MockUpdatable implements Updatable {

    boolean paivitetty = false;

    @Override
    public void update() {
        paivitetty = true;
    }
}