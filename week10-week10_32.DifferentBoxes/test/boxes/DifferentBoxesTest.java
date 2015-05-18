package boxes;

import boxes.Thing;
import boxes.Box;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import org.junit.*;
import static org.junit.Assert.*;

public class DifferentBoxesTest {

    Class maxWeightllinenBox;
    Class yhdenThingnBox;
    Class hukkaavaBox;

    @Before
    public void setUp() {
        try {
            maxWeightllinenBox = ReflectionUtils.findClass("boxes.MaxWeightBox");
            yhdenThingnBox = ReflectionUtils.findClass("boxes.OneThingBox");
            hukkaavaBox = ReflectionUtils.findClass("boxes.BlackHoleBox");
        } catch (Throwable t) {
        }
    }

    @Test
    @Points("32.1")
    public void thingConstructorThrowsExceptionWhenNegativeWeight() {
        try {
            Thing tavara = new Thing("Hesse", -1);
        } catch (IllegalArgumentException t) {
            return;
        }

        Assert.fail("Are you sure you're throwing exception IllegalArgumentException in class Thing's constructor if the weight is negative?\n"
                + "Test code new Thing(\"höyhen\",-1);");
    }

    @Test
    @Points("32.1")
    public void thingConstructorNoExceptionIfWeightOK() {
        try {
            Thing tavara = new Thing("Hesse", 0);
        } catch (Throwable t) {
            Assert.fail("Are you sure you're not throwing an exception when thing's weight is 0?\n"
                    + "Test code new Thing(\"höyhen\",0);");
        }
    }

    @Test
    @Points("32.1")
    public void equalsMethodImplemented() {
        Thing tavara = new Thing("Hesse", 5);
        Assert.assertEquals("Did you implement equals-method for class Thing so that you check that compared things have the same names but weight doesn't matter?\n"
                + "Test code\n"
                + "Thing eka = new Thing(\"Kivi\", 5);\n"
                + "eka.equals( new Thing(\"Kivi\", 1) );\n", true, tavara.equals(new Thing("Hesse")));
        Assert.assertEquals("Did you implement equals-method for class Thing so that you check that compared things have the same names but weight doesn't matter?\n"
                + "Test code\n"
                + "Thing eka = new Thing(\"Kivi\", 5);\n"
                + "eka.equals( new Thing(\"Kirja\", 1) );\n", false, tavara.equals(new Thing("Siddhartha")));
    }

    @Test
    @Points("32.1")
    public void hashCodeMethodImplemented() {
        Thing tavara = new Thing("Hesse", 5);
        Assert.assertEquals("Did you implement hashCode-method for class Thing so that you calculate hash value for thing's name but you ignore the weight?\n"
                + "Test code\n"
                + "Thing eka = new Thing(\"Kivi\", 5);\n"
                + "Thing toka = new Thing(\"Kivi\", 1) );\n"
                + "eka.hashCode() == toka.hashCode();\n", true, tavara.hashCode() == new Thing("Hesse").hashCode());
        Assert.assertEquals("Did you implement hashCode-method for class Thing so that you calculate hash value for thing's name but you ignore the weight?\n"
                + "Test code\n"
                + "Thing eka = new Thing(\"Kivi\", 5);\n"
                + "Thing toka = new Thing(\"Kirja\", 1) );\n"
                + "eka.hashCode() == toka.hashCode();\n", false, tavara.hashCode() == new Thing("Siddhartha").hashCode());
    }

    /*
     *
     */
    @Test
    @Points("32.2")
    public void maxWeightBox() throws Throwable {
        Assert.assertNotNull("Have you created class MaxWeightBox inside the package boxes?", maxWeightllinenBox);
        tarkistaPerinta(maxWeightllinenBox);

        saniteettitarkastus("boxes.MaxWeightBox", 2, "instance variables for maximum weight and for list of things");

        Constructor tilavuudellinenBoxConstructor = null;
        try {
            tilavuudellinenBoxConstructor = ReflectionUtils.requireConstructor(maxWeightllinenBox, int.class);
        } catch (Throwable t) {
            Assert.fail("Does the class MaxWeightBox have a constructor public MaxWeightBox(int maxWeight)?");
        }

        Box box = null;
        try {
            box = (Box) ReflectionUtils.invokeConstructor(tilavuudellinenBoxConstructor, 5);
        } catch (Throwable ex) {
            Assert.fail("MaxWeightBox-class's constructor call failed when maxWeight was 5. Error: " + ex.getMessage());
        }

        String v = "\n"
                + "Box box = new MaxWeightBox(5);\n"
                + "box.add(new Thing(\"a\", 1));\n";

        addMPL(box, new Thing("a", 1), v);
        v += "box.add(new Thing(\"b\", 2));\n";
        addMPL(box, new Thing("b", 2), v);
        v += "box.add(new Thing(\"c\", 2));\n";
        addMPL(box, new Thing("c", 2), v);
        v += "box.add(new Thing(\"d\", 1));\n";
        addMPL(box, new Thing("d", 1), v);
        v += "box.add(new Thing(\"f\", 0));\n";
        addMPL(box, new Thing("f", 0), v);

        Assert.assertEquals("Does the box remember a thing which was added to it?\n"
                + "Test code\n" + v
                + "box.isInTheBox(new Thing(\"a\"))\n", true, isInTheBox(box, new Thing("a"), v + "box.isInTheBox(new Thing(\"a\"));\n "));
        Assert.assertEquals("Does the box remember a thing which was added to it?\n"
                + "Test code\n" + v
                + "box.isInTheBox(new Thing(\"b\"))\n", true, isInTheBox(box, new Thing("b"), v + "box.isInTheBox(new Thing(\"b\"));\n "));
        Assert.assertEquals("Does the thing fit inside the box when combined weight of the thing and things inside the box is exactly box's maxWeight?"
                + "\nTest code\n" + v
                + "box.isInTheBox(new Thing(\"c\"))\n", true, isInTheBox(box, new Thing("c"), v + "box.isInTheBox(new Thing(\"c\"));\n "));
        Assert.assertEquals("Check that you can't add things to box when box's maxWeight is exceeded!\n"
                + "\nTest code\n" + v
                + "box.isInTheBox(new Thing(\"d\"))\n", false, isInTheBox(box, new Thing("d"), v + "box.isInTheBox(new Thing(\"d\"));\n "));
        Assert.assertEquals("Even though box is full, it must be possible to add things to it which have a weight of 0.\n"
                + "\nTest code\n" + v
                + "box.isInTheBox(new Thing(\"f\"))\n", true, isInTheBox(box, new Thing("f"), v + "box.isInTheBox(new Thing(\"f\"));\n "));
        Assert.assertEquals("No extra?\n"
                + "Test code\n" + v
                + "box.isInTheBox(new Thing(\"kivi\"))\n", false, isInTheBox(box, new Thing("kivi"), v + "box.isInTheBox(new Thing(\"kivi\"));\n "));
    }

    private void addMPL(Object olio, Thing t, String v) throws Throwable {
        String klassName = "boxes.MaxWeightBox";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        classRef.method(olio, "add").returningVoid()
                .taking(Thing.class).withNiceError(v).invoke(t);
    }

    private void addYTL(Object olio, Thing t, String v) throws Throwable {
        String klassName = "boxes.OneThingBox";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        classRef.method(olio, "add").returningVoid()
                .taking(Thing.class).withNiceError(v).invoke(t);
    }

    private void addHL(Object olio, Thing t, String v) throws Throwable {
        String klassName = "boxes.BlackHoleBox";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        classRef.method(olio, "add").returningVoid()
                .taking(Thing.class).withNiceError(v).invoke(t);
    }

    private boolean isInTheBox(Object olio, Thing t, String v) throws Throwable {
        String klassName = "boxes.MaxWeightBox";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        return classRef.method(olio, "isInTheBox").returning(boolean.class)
                .taking(Thing.class).withNiceError(v).invoke(t);
    }

    private boolean onkoYLaatikossa(Object olio, Thing t, String v) throws Throwable {
        String klassName = "boxes.OneThingBox";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        return classRef.method(olio, "isInTheBox").returning(boolean.class)
                .taking(Thing.class).withNiceError(v).invoke(t);
    }

    private boolean onkoHLaatikossa(Object olio, Thing t, String v) throws Throwable {
        String klassName = "boxes.BlackHoleBox";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        return classRef.method(olio, "isInTheBox").returning(boolean.class)
                .taking(Thing.class).withNiceError(v).invoke(t);
    }

    /*
     *
     */
    @Test
    @Points("32.3")
    public void oneThingBox() throws Throwable {
        Assert.assertNotNull("Have you created class OneThingBox inside the package boxes?", yhdenThingnBox);
        tarkistaPerinta(yhdenThingnBox);

 saniteettitarkastus("boxes.OneThingBox", 2, "instance variable for thing which is stored inside the box");

        Constructor yhdenThingnBoxConstructor = null;
        try {
            yhdenThingnBoxConstructor = ReflectionUtils.requireConstructor(yhdenThingnBox);
        } catch (Throwable t) {
            Assert.fail("Does the class OneThingBox have a constructor public OneThingBox()?");
        }

        Box box = null;
        try {
            box = (Box) ReflectionUtils.invokeConstructor(yhdenThingnBoxConstructor);
        } catch (Throwable ex) {
            Assert.fail("OneThingBox-class's constructor call failed. Error: " + ex.getMessage());
        }

        String v = "\nBox box = new OneThingBox();\n"
                + "box.isInTheBox(new Thing(\"a\"));";

        Assert.assertFalse("Are you sure that the box doesn't have anything at first?\n"
                + "check code\n"
                + "", onkoYLaatikossa(box, new Thing("a"), v));

        v = "\n"
                + "Box box = new OneThingBox();\n"
                + "box.add(new Thing(\"a\", 1));\n";

        addYTL(box, new Thing("a", 1), v);
        v += "box.add(new Thing(\"b\", 2));\n";
        addYTL(box, new Thing("b", 2), v);
        v += "box.add(new Thing(\"c\", 2));\n";
        addYTL(box, new Thing("c", 2), v);

        Assert.assertEquals("Does the box remember a thing which was added to it?\n"
                + "check code\n"
                + v + "box.isInTheBox(new Thing(\"a\"));\n", true, onkoYLaatikossa(box, new Thing("a"), v + "box.isInTheBox(new Thing(\"a\"));"));
        Assert.assertEquals("When one-thing-box has already a thing, it shouldn't be possible to add more things inside it.\n"
                + "check code\n"
                + v + "box.isInTheBox(new Thing(\"b\"));\n", false, onkoYLaatikossa(box, new Thing("b"), v + "box.isInTheBox(new Thing(\"b\"));"));
        Assert.assertEquals("When one-thing-box has already a thing, it shouldn't be possible to add more things inside it.\n"
                + "check code\n"
                + v + "box.isInTheBox(new Thing(\"c\"));\n", false, onkoYLaatikossa(box, new Thing("c"), v + "box.isInTheBox(new Thing(\"c\"));"));
    }

    @Test
    @Points("32.3")
    public void blackHoleBox() throws Throwable {
        Assert.assertNotNull("Have you created class BlackHoleBox inside the package boxes?", hukkaavaBox);
        tarkistaPerinta(hukkaavaBox);

        Constructor hukkaavaBoxConstructor = null;
        try {
            hukkaavaBoxConstructor = ReflectionUtils.requireConstructor(hukkaavaBox);
        } catch (Throwable t) {
            Assert.fail("Does the class BlackHoleBox have a constructor public BlackHoleBox()?");
        }

        Box box = null;
        try {
            box = (Box) ReflectionUtils.invokeConstructor(hukkaavaBoxConstructor);
        } catch (Throwable ex) {
            Assert.fail("BlackHoleBox-class's constructor call failed. Error: " + ex.getMessage());
        }

        String v = "\n"
                + "Box box = new BlackHoleBox();\n"
                + "box.isInTheBox(new Thing(\"a\", 1));\n";
        Assert.assertEquals("Does the black hole box lose things that are added inside it?\n"
                + "check code\n"
                + v, false, onkoHLaatikossa(box, new Thing("a"),v));

         v = "\n"
                + "Box box = new BlackHoleBox();\n"
                + "box.add(new Thing(\"a\", 1));\n";

        addHL(box, new Thing("a", 1), v);
        v += "box.add(new Thing(\"b\", 2));\n";
        addHL(box, new Thing("b", 1), v);

        Assert.assertEquals("Does the black hole box lose things that are added inside it?\n"
                + "check code\n"
                + v + "box.isInTheBox(new Thing(\"a\"));", false, onkoHLaatikossa(box, new Thing("a"), v + "box.isInTheBox(new Thing(\"a\"));"));

        Assert.assertEquals("Does the black hole box lose things that are added inside it?\n"
                + "check code\n"
                + v + "box.isInTheBox(new Thing(\"b\"));", false, onkoHLaatikossa(box, new Thing("b"), v + "box.isInTheBox(new Thing(\"b\"));"));

    }

    private void tarkistaPerinta(Class clazz) {
        if (!(clazz.getSuperclass().equals(Box.class))) {
            Assert.fail("Does the class " + s(clazz.getName()) + " extend class Box?");
        }
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
