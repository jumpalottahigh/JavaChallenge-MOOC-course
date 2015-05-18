
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class B_SuitcaseTest<_Thing, _Suitcase> {

    private Class thingClass;
    private Constructor thingConstructor;
    private Class suitcaseClass;
    private Constructor suitcaseConstructor;
    String klassName = "Suitcase";
    Reflex.ClassRef<_Suitcase> _SuitcaseRef;
    Reflex.ClassRef<_Thing> _ThingRef;

    @Before
    public void setup() {
        _SuitcaseRef = Reflex.reflect("Suitcase");
        _ThingRef = Reflex.reflect("Thing");

        try {
            thingClass = ReflectionUtils.findClass("Thing");
            thingConstructor = ReflectionUtils.requireConstructor(thingClass, String.class, int.class);

            suitcaseClass = ReflectionUtils.findClass("Suitcase");
            suitcaseConstructor = ReflectionUtils.requireConstructor(suitcaseClass, int.class);

        } catch (Throwable t) {
        }
    }

    @Test
    @Points("4.2")
    public void classIsPublic() {
        assertTrue("Class " + klassName + " has to be public, so it must be defined as\npublic class " + klassName + " {...\n}", _SuitcaseRef.isPublic());
    }

    @Test
    @Points("4.2")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 3, "instance variables for maximum weight and a list of things and also a variable for their combined weight. The instance variable for combined weight might not be necessary!");
    }

    @Test
    @Points("4.2")
    public void testSuitcaseConstructor() throws Throwable {
        Reflex.MethodRef1<_Suitcase, _Suitcase, Integer> ctor = _SuitcaseRef.constructor().taking(int.class).withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "(int maxWeight)", ctor.isPublic());
        String v = "error caused by code new Suitcase( 10);";
        ctor.withNiceError(v).invoke(10);
    }

    public _Suitcase luoM(int paino) throws Throwable {
        return _SuitcaseRef.constructor().taking(int.class).withNiceError().invoke(paino);
    }

    public _Thing luoT(String nimi, int paino) throws Throwable {
        return _ThingRef.constructor().taking(String.class, int.class).withNiceError().invoke(nimi, paino);
    }

    @Test
    @Points("4.2")
    public void addThingMethod() throws Throwable {
        _Thing thing = luoT("book", 1);
        _Suitcase laukku = luoM(10);

        String v = "\nThing t = new Thing(\"book\",1); \n"
                + "Suitcase m = new Suitcase(10);\n"
                + "m.addThing(t);";

        assertTrue("Class Suitcase should have method public void addThing(Thing thing)", _SuitcaseRef.method(laukku, "addThing").returningVoid().taking(_ThingRef.cls()).withNiceError(v).isPublic());

        _SuitcaseRef.method(laukku, "addThing").returningVoid().taking(_ThingRef.cls()).withNiceError(v).invoke(thing);
    }

    @Test
    @Points("4.2")
    public void checkAddThing() {
        try {
            Object suitcase = ReflectionUtils.invokeConstructor(suitcaseConstructor, 64);

            Method addThingMeto = ReflectionUtils.requireMethod(suitcaseClass, "addThing", ReflectionUtils.findClass("Thing"));

            Object tiili = ReflectionUtils.invokeConstructor(thingConstructor, "Brick", 8);
            Object hammas = ReflectionUtils.invokeConstructor(thingConstructor, "Tooth", 8);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, tiili);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, hammas);

            List<Object> thingt = (List<Object>) oliomuuttujaLista(suitcaseClass, suitcase);
            if (!thingt.contains(tiili)) {
                fail("asd");
            }

            if (!thingt.contains(hammas)) {
                fail("asd");
            }

        } catch (Throwable t) {
            junit.framework.Assert.fail("Verify that method addThing of class Suitcase adds things to suitcase. Does the class also have an ArrayList which has been instantiated?");
        }
    }

    @Test
    @Points("4.2")
    public void checkAddThingWhenSuitcaseIsFull() {
        try {
            Object suitcase = ReflectionUtils.invokeConstructor(suitcaseConstructor, 20);

            Method addThingMeto = ReflectionUtils.requireMethod(suitcaseClass, "addThing", ReflectionUtils.findClass("Thing"));

            Object tiili = ReflectionUtils.invokeConstructor(thingConstructor, "Brick", 8);
            Object hammas = ReflectionUtils.invokeConstructor(thingConstructor, "Tooth", 8);
            Object porkkana = ReflectionUtils.invokeConstructor(thingConstructor, "Carrot", 8);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, tiili);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, hammas);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, porkkana);

            List<Object> thingt = (List<Object>) oliomuuttujaLista(suitcaseClass, suitcase);
            if (thingt.contains(porkkana)) {
                fail("asd");
            }

        } catch (Throwable t) {
            fail("Verify that method addThing of class Suitcase doesn't add a new thing if it exceeds suitcase's maximum weight.");
        }
    }

    @Test
    @Points("4.2")
    public void checkAddThingWhenSuitcaseWeightsTheSame() {
        try {
            Object suitcase = ReflectionUtils.invokeConstructor(suitcaseConstructor, 20);

            Method addThingMeto = ReflectionUtils.requireMethod(suitcaseClass, "addThing", ReflectionUtils.findClass("Thing"));

            Object tiili = ReflectionUtils.invokeConstructor(thingConstructor, "Brick", 20);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, tiili);

            List<Object> thingt = (List<Object>) oliomuuttujaLista(suitcaseClass, suitcase);
            if (!thingt.contains(tiili)) {
                fail("asd");
            }

        } catch (Throwable t) {
            fail("Verify that method addThing of class Suitcase accepts a thing which causes suitcase's total weight be the same as maximum weight.");
        }
    }

    @Test
    @Points("4.2")
    public void suitcaseToString() {
        String palautus = "";
        try {
            Object porkkanaLaatikko64 = ReflectionUtils.invokeConstructor(suitcaseConstructor, 20);
            Method suitcaseToString = ReflectionUtils.requireMethod(suitcaseClass, "toString");


            Method addThingMeto = ReflectionUtils.requireMethod(suitcaseClass, "addThing", ReflectionUtils.findClass("Thing"));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, porkkanaLaatikko64, ReflectionUtils.invokeConstructor(thingConstructor, "Uusi", 8));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, porkkanaLaatikko64, ReflectionUtils.invokeConstructor(thingConstructor, "Uusi", 8));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, porkkanaLaatikko64, ReflectionUtils.invokeConstructor(thingConstructor, "Uusi", 8));

            palautus = ReflectionUtils.invokeMethod(String.class, suitcaseToString, porkkanaLaatikko64);

            if (!sisaltaa(palautus, "2", "things", "16", "kg")) {
                fail("");
            }

        } catch (Throwable t) {
            junit.framework.Assert.fail("Verify that method toString of class Suitcase prints the information of the things it has, (e.g. : \"3 things (15 kg)\".\n"
                    + "toString of the suitcase with maximum weight of 20 after there has been added three things which all weight 8 kilos: " + palautus);
        }
    }

    @Test
    @Points("4.3")
    public void suitcaseMoreToString() {
        try {
            Object porkkanaLaatikko64 = ReflectionUtils.invokeConstructor(suitcaseConstructor, 20);
            Method suitcaseToString = ReflectionUtils.requireMethod(suitcaseClass, "toString");
            Method addThingMeto = ReflectionUtils.requireMethod(suitcaseClass, "addThing", ReflectionUtils.findClass("Thing"));


            String palautus = ReflectionUtils.invokeMethod(String.class, suitcaseToString, porkkanaLaatikko64);
            if (!sisaltaa(palautus, "empty", "0", "kg")) {
                fail("Verify that empty suitcase prints out \"empty (0 kg)\"");
            }

            ReflectionUtils.invokeMethod(void.class, addThingMeto, porkkanaLaatikko64, ReflectionUtils.invokeConstructor(thingConstructor, "Uusi", 8));

            palautus = ReflectionUtils.invokeMethod(String.class, suitcaseToString, porkkanaLaatikko64);
            if (!sisaltaa(palautus, "thing", "8", "kg") || sisaltaa(palautus, "things")) {
                fail("When suitcase has one thing it should print \"1 thing (<weight> kg)\", where <weight> is the weight of the suitcase.");
            }

            ReflectionUtils.invokeMethod(void.class, addThingMeto, porkkanaLaatikko64, ReflectionUtils.invokeConstructor(thingConstructor, "Uusi", 8));
            palautus = ReflectionUtils.invokeMethod(String.class, suitcaseToString, porkkanaLaatikko64);

            if (!sisaltaa(palautus, "2", "things", "16", "kg")) {
                fail("When suitcase has two things it should print \"2 things (<weight> kg)\", where <weight> is the weight of the suitcase.");
            }

        } catch (Throwable t) {
            junit.framework.Assert.fail(t.getMessage());
        }
    }

    @Test
    @Points("4.4")
    public void printThingsMethod() throws Throwable {
        _Suitcase laukku = luoM(10);

        String v = ""
                + "Suitcase m = new Suitcase(10);\n"
                + "m.printThings();";

        assertTrue("Class Suitcase should have method public void printThings()", _SuitcaseRef.method(laukku, "printThings").returningVoid().takingNoParams().withNiceError(v).isPublic());

        _SuitcaseRef.method(laukku, "printThings").returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("4.4")
    public void printThingsWorksCorrectly() {
        MockInOut io = new MockInOut("");

        try {
            Object suitcase = ReflectionUtils.invokeConstructor(suitcaseConstructor, 20);


            Class thing = ReflectionUtils.findClass("Thing");
            Constructor thingConst = ReflectionUtils.requireConstructor(thing, String.class, int.class);

            Object porkkana = ReflectionUtils.invokeConstructor(thingConst, "Carrot", 2);
            Object nauris = ReflectionUtils.invokeConstructor(thingConst, "Nauris", 4);
            Object kaali = ReflectionUtils.invokeConstructor(thingConst, "Kaali", 8);

            Method addThingMeto = ReflectionUtils.requireMethod(suitcaseClass, "addThing", ReflectionUtils.findClass("Thing"));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, porkkana);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, nauris);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, kaali);


            Method yhteisPainoMeto = ReflectionUtils.requireMethod(suitcaseClass, "printThings");
            ReflectionUtils.invokeMethod(void.class, yhteisPainoMeto, suitcase);

            if (!sisaltaa(io.getOutput(), "Carrot", "Nauris", "Kaali", "2", "4", "8", "kg")) {
                throw new Exception();
            }

            if (io.getOutput().split("\n").length < 2) {
                throw new Exception();
            }

        } catch (Throwable t) {
            junit.framework.Assert.fail("Check that method printThings works correctly: it should print things one by one on the screen."
                    + "\nThree things were added to suitcase and method printThings() was called, it printed:\n" + io.getOutput());
        }
    }

    @Test
    @Points("4.4")
    public void suitcaseTotalWeightMethod() throws Throwable {
        _Suitcase laukku = luoM(10);

        String v = ""
                + "Suitcase m = new Suitcase(10);\n"
                + "m.totalWeight();";

        assertTrue("Class Suitcase should have method public int totalWeight()", _SuitcaseRef.method(laukku, "totalWeight").returning(int.class).takingNoParams().withNiceError(v).isPublic());

        _SuitcaseRef.method(laukku, "totalWeight").returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("4.4")
    public void suitcaseTotalWeightReturnsCorrectValue() {
        try {
            Object suitcase = ReflectionUtils.invokeConstructor(suitcaseConstructor, 20);

            Method addThingMeto = ReflectionUtils.requireMethod(suitcaseClass, "addThing", ReflectionUtils.findClass("Thing"));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, ReflectionUtils.invokeConstructor(thingConstructor, "T1", 8));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, ReflectionUtils.invokeConstructor(thingConstructor, "T2", 6));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, ReflectionUtils.invokeConstructor(thingConstructor, "T3", 4));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, ReflectionUtils.invokeConstructor(thingConstructor, "T3", 4));

            Method yhteisPainoMeto = ReflectionUtils.requireMethod(suitcaseClass, "totalWeight");

            int arvo = ReflectionUtils.invokeMethod(int.class, yhteisPainoMeto, suitcase);
            if (arvo != 18) {
                throw new Exception();
            }
        } catch (Throwable t) {
            junit.framework.Assert.fail("Verify that method totalWeight of class Suitcase works correctly. It should return the sum of the weight of the things in the suitcase.");
        }
    }

    @Test
    @Points("4.5")
    public void heaviestThingTest() throws Throwable {
        _Suitcase laukku = luoM(10);


        String v = "\nError caused by code\n"
                + "Suitcase m = new Suitcase(10); "
                + "m.heaviestThing();";

        assertTrue("Class Suitcase should have method public Thing heaviestThing()", _SuitcaseRef.method(laukku, "heaviestThing").returning(_ThingRef.cls()).takingNoParams().withNiceError(v).isPublic());

        _SuitcaseRef.method(laukku, "heaviestThing").returning(_ThingRef.cls()).takingNoParams().withNiceError(v).invoke();

    }

    @Test
    @Points("4.5")
    public void heaviestThingFindsTheHeaviestThing() {
        Object ret = null;
        try {
            Object suitcase = ReflectionUtils.invokeConstructor(suitcaseConstructor, 20);
            Constructor thingConst = ReflectionUtils.requireConstructor(thingClass, String.class, int.class);

            Object porkkana = ReflectionUtils.invokeConstructor(thingConst, "Carrot", 2);
            Object nauris = ReflectionUtils.invokeConstructor(thingConst, "Nauris", 4);
            Object kaali = ReflectionUtils.invokeConstructor(thingConst, "Kaali", 8);

            Method addThingMeto = ReflectionUtils.requireMethod(suitcaseClass, "addThing", ReflectionUtils.findClass("Thing"));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, porkkana);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, kaali);
            ReflectionUtils.invokeMethod(void.class, addThingMeto, suitcase, nauris);

            Method suitcaseRaskainThing = ReflectionUtils.requireMethod(suitcaseClass, "heaviestThing");

            ret = ReflectionUtils.invokeMethod(thingClass, suitcaseRaskainThing, suitcase);

            if (ret != kaali) {
                throw new Exception();
            }
        } catch (Throwable t) {
            junit.framework.Assert.fail("Method heaviestThing should return the heaviest thing. Check code:\n"
                    + "Suitcase suitcase = new Suitcase(20);\n"
                    + "suitcase.addThing(new Thing(\"Carrot\", 2));\n"
                    + "suitcase.addThing(new Thing(\"Kaali\", 8));\n"
                    + "suitcase.addThing(new Thing(\"Nauris\", 4));\n"
                    + "Thing heaviest = suitcase.heaviestThing();\n"
                    + "returned thing: "+ret);
        }
    }

    @Test
    @Points("4.5")
    public void heaviestThingReturnsNullIfEmptySuitcase() {
        try {
            Object suitcase = ReflectionUtils.invokeConstructor(suitcaseConstructor, 20);
            Method suitcaseRaskainThing = ReflectionUtils.requireMethod(suitcaseClass, "heaviestThing");

            Object ret = suitcaseRaskainThing.invoke(suitcase);

            if (ret != null) {
                throw new Exception();
            }
        } catch (Throwable t) {
            junit.framework.Assert.fail(t.getMessage() + "Method heaviestThing should return null when suitcase is empty.");
        }
    }

    private boolean sisaltaa(String palautus, String... oletetutArvot) {
        for (String arvo : oletetutArvot) {
            if (!palautus.contains(arvo)) {
                return false;
            }
        }

        return true;
    }

    private Object oliomuuttujaLista(Class clazz, Object container) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.getType().equals(List.class)) {
                f.setAccessible(true);
                try {
                    return f.get(container);
                } catch (IllegalArgumentException ex) {
                } catch (IllegalAccessException ex) {
                }
            }

            if (f.getType().equals(ArrayList.class)) {
                f.setAccessible(true);
                try {
                    return f.get(container);
                } catch (IllegalArgumentException ex) {
                } catch (IllegalAccessException ex) {
                }
            }

            if (f.getType().equals(LinkedList.class)) {
                f.setAccessible(true);
                try {
                    return f.get(container);
                } catch (IllegalArgumentException ex) {
                } catch (IllegalAccessException ex) {
                }
            }
        }

        return null;
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("You do not need \"static variables\", remove from class " + klassName + " the following variable: " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All istance variables of the class should be private, but class " + klassName + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }

            assertTrue("The class " + klassName + " should only have " + m +", remove others", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}
