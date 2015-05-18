package movable;

import movable.Movable;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

@Points("28.2")
public class GroupTest {

    private final String PAKKAUS = "movable";
    private final String LUOKKA = "Group";
    private Class groupClass;
    private Class olioClass;
    private String klassName = "movable.Group";
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
        saniteettitarkastus(klassName, 1, "instance variable for remembering the members of the group");
    }

    @Test
    public void constructor() throws Throwable {

        Reflex.MethodRef0<Object, Object> ctor = classRef.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "()", ctor.isPublic());
        String v = "error caused by code new Group();\n";
        ctor.withNiceError(v).invoke();
    }

    public Movable newOrganism(int t1, int t2) throws Throwable {
        String klassName = "movable.Organism";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);
        Reflex.MethodRef2<Object, Object, Integer, Integer> ctor = classRef
                .constructor().taking(int.class, int.class).withNiceError();
        return (Movable) ctor.invoke(t1, t2);
    }

    public Movable newGroup() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = classRef
                .constructor().takingNoParams().withNiceError();
        return (Movable) ctor.invoke();
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
            fail("Does the class Group implement interface Movable?");
        }
    }

    @Test
    public void hasMethodMove() throws Throwable {
        Movable e = newGroup();

        assertTrue("Class Group should have method public void move(int dx, int dy)",
                classRef.method(e, "move").returningVoid().taking(int.class, int.class).isPublic());

        String v = "error caused by code\n"
                + "Group e = new Group();\n"
                + "e.move(1,1);\n";

        classRef.method(e, "move").returningVoid().taking(int.class, int.class).withNiceError(v).invoke(1, 1);
    }

    private void move(Object e, int dx, int dy, String v) throws Throwable {
        classRef.method(e, "move").returningVoid().taking(int.class, int.class).withNiceError(v).invoke(dx, dy);
    }

    @Test
    public void hasMethodAddToGroup() throws Throwable {
        Movable e = newGroup();

        assertTrue("Class Group should have method public void addToGroup(Movable movable)",
                classRef.method(e, "addToGroup").returningVoid().taking(Movable.class).isPublic());

        String v = "error caused by code\n"
                + "Group e = new Group();\n"
                + "e.addToGroup( new Organism(1,1) );\n";

        classRef.method(e, "addToGroup")
                .returningVoid()
                .taking(Movable.class).withNiceError(v).invoke(newOrganism(1, 1));
    }

    private void addToGroup(Object e, Movable s, String v) throws Throwable {
        classRef.method(e, "addToGroup").returningVoid().taking(Movable.class).withNiceError(v).invoke(s);
    }

    @Test
    public void toStringDefined() throws Throwable {
        Movable l = newGroup();
        assertFalse("Create method toString to class Group as defined in the assignment", l.toString().contains("@"));
        String v = "Group l = new Group();\n"
                + "l.addToGroup( new Organism(1,9) );\n"
                + "l.addToGroup( new Organism(4,2) );\n"
                + "l.toString();\n";

        Movable e1 = newOrganism(1, 9);
        Movable e2 = newOrganism(4, 2);

        addToGroup(l, e1, v);
        addToGroup(l, e2, v);

        String mj = l.toString();

        assertTrue("String should have 2 lines with code \n" + v + ""
                + "returned string had\n" + mj, mj.split("\n").length > 1);
        assertTrue("Returned string should contain \"" + e1 + "\"\n" + v + ""
                + "returned string had\n" + mj, mj.contains(e1.toString()));
        assertTrue("Returned string should contain \"" + e2 + "\"\n" + v + ""
                + "returned string had\n" + mj, mj.contains(e1.toString()));
    }

    @Test
    public void groupOfSizeOneMovesCorrectly1() throws Throwable {
        String v = ""
                + "Group group = new Group();\n"
                + "group.addToGroup( new Organism(5,10) );\n"
                + "group.move(1,0);\n"
                + "group.toString()";

        Movable l = newGroup();
        addToGroup(l, newOrganism(5, 10), v);
        move(l, 1, 0, v);
        assertTrue("new location of the only member of the group should be"
                + "x: 6; y: 10 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(), l.toString().contains("x: 6; y: 10"));
    }

    @Test
    public void groupOfSizeOneMovesCorrectly2() throws Throwable {
        String v = ""
                + "Group group = new Group();\n"
                + "group.addToGroup( new Organism(5,10) );\n"
                + "group.move(0,1);\n"
                + "group.toString()";

        Movable l = newGroup();
        addToGroup(l, newOrganism(5, 10), v);
        move(l, 0, 1, v);
        assertTrue("new location of the only member of the group should be"
                + "x: 5; y: 11 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(), l.toString().contains("x: 5; y: 11"));
    }

    @Test
    public void groupOfSizeOneMovesCorrectly3() throws Throwable {
        String v = ""
                + "Group group = new Group();\n"
                + "group.addToGroup( new Organism(5,10) );\n"
                + "group.move(0,1);\n"
                + "group.move(3,5);\n"
                + "group.move(-20,2);\n"
                + "group.move(9,3);\n"
                + "group.toString()";

        Movable l = newGroup();
        addToGroup(l, newOrganism(5, 10), v);
        move(l, 0, 1, v);
        move(l, 3, 5, v);
        move(l, -20, 2, v);
        move(l, 9, 3, v);
        assertTrue("new location of the only member of the group should be"
                + "x: -3; y: 21 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(),
                l.toString().contains("x: -3; y: 21"));
    }

    @Test
    public void groupOfSizeTwoMovesCorrectly1() throws Throwable {
        String v = ""
                + "Group group = new Group();\n"
                + "group.addToGroup( new Organism(5,10) );\n"
                + "group.addToGroup( new Organism(2,8) );\n"
                + "group.move(1,0);\n"
                + "group.toString()";

        Movable l = newGroup();
        addToGroup(l, newOrganism(5, 10), v);
        addToGroup(l, newOrganism(2, 8), v);
        move(l, 1, 0, v);
        assertTrue("the new locations of the members of the group should be"
                + "x: 6; y: 10  ja x: 3; y: 8 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(), l.toString().contains("x: 6; y: 10"));
        assertTrue("the new locations of the members of the group should be"
                + "x: 6; y: 10  ja x: 3; y: 8 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(), l.toString().contains("x: 3; y: 8"));
    }

    @Test
    public void groupOfSizeTwoMovesCorrectly2() throws Throwable {
        String v = ""
                + "Group group = new Group();\n"
                + "group.addToGroup( new Organism(5,10) );\n"
                + "group.addToGroup( new Organism(2,8) );\n"
                + "group.move(0,1);\n"
                + "group.toString()";

        Movable l = newGroup();
        addToGroup(l, newOrganism(5, 10), v);
        addToGroup(l, newOrganism(2, 8), v);
        move(l, 0, 1, v);
        assertTrue("the new locations of the members of the group should be"
                + "x: 5; y: 11  ja x: 2; y: 9 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(), l.toString().contains("x: 5; y: 11"));
        assertTrue("the new locations of the members of the group should be"
                + "x: 5; y: 11  ja x: 2; y: 9 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(), l.toString().contains("x: 2; y: 9"));
    }

    @Test
    public void groupOfSizeTwoMovesCorrectly3() throws Throwable {
        String v = ""
                + "Group group = new Group();\n"
                + "group.addToGroup( new Organism(5,10) );\n"
                + "group.addToGroup( new Organism(2,8) );\n"
                + "group.move(0,1);\n"
                + "group.move(8,-3);\n"
                + "group.move(11,1);\n"
                + "group.toString()";

        Movable l = newGroup();
        addToGroup(l, newOrganism(5, 10), v);
        addToGroup(l, newOrganism(2, 8), v);
        move(l, 0, 1, v);
        move(l, 8, -3, v);
        move(l, 11, 1, v);
        assertTrue("the new locations of the members of the group should be"
                + "x: 24; y: 9  ja x: 21; y: 7 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(),
                l.toString().contains("x: 24; y: 9"));
        assertTrue("the new locations of the members of the group should be"
                + "x: 24; y: 9  ja x: 21; y: 7 when executing code\n" + v + "\n"
                + "\naccording to your code the new location is\n" + l.toString(),
                l.toString().contains("x: 21; y: 7"));
    }

    @Test
    public void bigGroupMovesCorrectly() throws Throwable {
        String v = ""
                + "Group group = new Group();\n"
                + "group.addToGroup( new Organism(5,10) );\n"
                + "group.addToGroup( new Organism(2,8) );\n"
                + "group.addToGroup( new Organism(7,-4) );\n"
                + "group.addToGroup( new Organism(99,-200) );\n"
                + "group.move(5,-2);\n"
                + "group.move(1,4);\n"
                + "group.toString()";

        Movable l = newGroup();
        addToGroup(l, newOrganism(5, 10), v);
        addToGroup(l, newOrganism(2, 8), v);
        addToGroup(l, newOrganism(7, -4), v);
        addToGroup(l, newOrganism(99, -200), v);
        move(l, 5, -2, v);
        move(l, 1, 4, v);
        assertTrue("group doesn't move correctly when executing code\n" + v + "\n"
                + "returned string had\n" + l.toString(), l.toString().contains("x: 11; y: 12"));
        assertTrue("group doesn't move correctly when executing code\n" + v + "\n"
                + "returned string had\n" + l.toString(), l.toString().contains("x: 8; y: 10"));
        assertTrue("group doesn't move correctly when executing code\n" + v + "\n"
                + "returned string had\n" + l.toString(), l.toString().contains("x: 13; y: -2"));
        assertTrue("group doesn't move correctly when executing code\n" + v + "\n"
                + "returned string had\n" + l.toString(), l.toString().contains("x: 105; y: -198"));


    }

    /*
     *
     */
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
