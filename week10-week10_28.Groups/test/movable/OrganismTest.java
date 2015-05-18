package movable;

import movable.Movable;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

@Points("28.1")
public class OrganismTest {

    private String klassName = "movable.Organism";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setUp() {
        classRef = Reflex.reflect(klassName);
    }

    @Test
    public void classExists() {
        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 2, "instance variables for coordinates x and y");
    }

    @Test
    public void constructor() throws Throwable {

        Reflex.MethodRef2<Object, Object, Integer, Integer> ctor = classRef.constructor().taking(int.class, int.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "(int x, int y)", ctor.isPublic());
        String v = "error caused by code new Organism(5,10);\n";
        ctor.withNiceError(v).invoke(5, 10);
    }

    public Movable newOrganism(int t1, int t2) throws Throwable {
        String klassName = "movable.Organism";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);
        Reflex.MethodRef2<Object, Object, Integer, Integer> ctor = classRef
                .constructor().taking(int.class, int.class).withNiceError();
        return (Movable) ctor.invoke(t1, t2);
    }

    @Test
    public void isMovable() {
        Class clazz = ReflectionUtils.findClass(klassName);
        boolean toteuttaaRajapinnan = false;
        Class kali = Movable.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class Organism implement interface Movable?");
        }
    }

    @Test
    public void hasMethodMove() throws Throwable {
        Movable e = newOrganism(5, 10);

        assertTrue("Class Organism should have method public void move(int dx, int dy)",
                classRef.method(e, "move").returningVoid().taking(int.class, int.class).isPublic());

        String v = "error caused by code\n"
                + "Organism e = new Organism(5,10);\n"
                + "e.move(1,1);\n";

        classRef.method(e, "move").returningVoid().taking(int.class, int.class).withNiceError(v).invoke(1, 1);
    }

    private void move(Object e, int dx, int dy, String v) throws Throwable {
        classRef.method(e, "move").returningVoid().taking(int.class, int.class).withNiceError(v).invoke(dx, dy);
    }

    @Test
    public void toStringDefined() throws Throwable {
        Movable e = newOrganism(5, 10);
        assertFalse("Create method toString for class Organism as defined in the assignment", e.toString().contains("@"));
        String v = "Organism e = new Organism(5,10);\n"
                + "e.toString();\n";
        assertEquals(v, "x: 5; y: 10", e.toString());

        e = newOrganism(1, 9);
        v = "Organism e = new Organism(1,9);\n"
                + "e.toString();\n";
        assertEquals(v, "x: 1; y: 9", e.toString());
    }

    @Test
    public void movesCorrectly1() throws Throwable {
        String v = ""
                + "Organism e = new Organism(5,10);\n"
                + "e.move(1,0);\n"
                + "t.toString()";

        Movable e = newOrganism(5,10);
        move(e, 1, 0, v);
        assertEquals(v, "x: 6; y: 10", e.toString());
    }

    @Test
    public void movesCorrectly2() throws Throwable {
        String v = ""
                + "Organism e = new Organism(5,10);\n"
                + "e.move(0,1);\n"
                + "t.toString()";

        Movable e = newOrganism(5,10);
        move(e, 0, 1, v);
        assertEquals(v, "x: 5; y: 11", e.toString());
    }

    @Test
    public void movesCorrectly3() throws Throwable {
        String v = ""
                + "Organism e = new Organism(5,10);\n"
                + "e.move(2,-8);\n"
                + "t.toString()";

        Movable e = newOrganism(5,10);
        move(e, 2, -8, v);
        assertEquals(v, "x: 7; y: 2", e.toString());
    }

    @Test
    public void movesCorrectly4() throws Throwable {
        String v = ""
                + "Organism e = new Organism(0,0);\n"
                + "e.move(2,5);\n"
                + "e.move(1,4);\n"
                + "e.move(5,-11);\n"
                + "t.toString()";

        Movable e = newOrganism(0,0);
        move(e, 2, 5, v);
        move(e, 1, 4, v);
        move(e, 5, -11, v);
        assertEquals(v, "x: 8; y: -2", e.toString());
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
}
