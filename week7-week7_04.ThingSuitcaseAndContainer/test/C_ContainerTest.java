
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class C_ContainerTest<_Thing, _Suitcase, _Container> {

    private Class thingClass;
    private Constructor thingConstructor;
    private Method thingToString;
    private Class suitcaseClass;
    private Constructor suitcaseConstructor;
    private Class containerClass;
    private Constructor containerConstructor;
    String klassName = "Container";
    Reflex.ClassRef<_Container> _ContainerRef;
    Reflex.ClassRef<_Suitcase> _SuitcaseRef;
    Reflex.ClassRef<_Thing> _ThingRef;

    @Before
    public void setup() {
        _SuitcaseRef = Reflex.reflect("Suitcase");
        _ThingRef = Reflex.reflect("Thing");
        _ContainerRef = Reflex.reflect("Container");

        try {
            thingClass = ReflectionUtils.findClass("Thing");
            thingConstructor = ReflectionUtils.requireConstructor(thingClass, String.class, int.class);
            thingToString = ReflectionUtils.requireMethod(thingClass, "toString");

            suitcaseClass = ReflectionUtils.findClass("Suitcase");
            suitcaseConstructor = ReflectionUtils.requireConstructor(suitcaseClass, int.class);

            containerClass = ReflectionUtils.findClass("Container");
            containerConstructor = ReflectionUtils.requireConstructor(containerClass, int.class);
        } catch (Throwable t) {
        }
    }

    @Test
    @Points("4.6")
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", _SuitcaseRef.isPublic());
    }

    @Test
    @Points("4.6")
    public void noRedundantVariables() {

        saniteettitarkastus(klassName, 3, "instance variables for maximum weight and a list of suitcases and also a variable for their combined weight. The instance variable for combined weight might not be necessary!");
    }

    @Test
    @Points("4.6")
    public void testContainerConstructor() throws Throwable {
        Reflex.MethodRef1<_Container, _Container, Integer> ctor = _ContainerRef.constructor().taking(int.class).withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "(int maxWeight)", ctor.isPublic());
        String v = "error caused by code new Container(10);";
        ctor.withNiceError(v).invoke(10);
    }

    public _Suitcase luoM(int paino) throws Throwable {
        return _SuitcaseRef.constructor().taking(int.class).withNiceError().invoke(paino);
    }

    public _Thing luoT(String nimi, int paino) throws Throwable {
        return _ThingRef.constructor().taking(String.class, int.class).withNiceError().invoke(nimi, paino);
    }

    public _Container luoL( int paino) throws Throwable {
        return _ContainerRef.constructor().taking(int.class).withNiceError().invoke(paino);
    }


    @Test
    @Points("4.6")
    public void containerAddSuitcaseMethod() throws Throwable {
        _Suitcase laukku = luoM(10);
        _Container ruuma = luoL(100);


        String v = "\n"
                + "Suitcase m = new Suitcase(10);\n"
                + "Container r = new Container(100;\n)"
                + "r.addSuitcase(m);";

        assertTrue("Class Container should have method public void addSuitcase(Suitcase suitcase)", _ContainerRef.method(ruuma, "addSuitcase").returningVoid().taking(_SuitcaseRef.cls()).withNiceError(v).isPublic());

        _ContainerRef.method(ruuma, "addSuitcase").returningVoid().taking(_SuitcaseRef.cls()).withNiceError(v).invoke(laukku);
    }

    @Test
    @Points("4.6")
    public void containerIfCantAdd() {
        try {
            Object container = luoRuuma(20);
            Object ekaLaukku = luoSuitcase(10);
            lisaaLaukkuun(ekaLaukku, luoThing("Porsas", 7));
            lisaaLaukkuun(ekaLaukku, luoThing("Siili", 2));

            lisaaRuumaan(container, ekaLaukku);

            Object tokaLaukku = luoSuitcase(10);
            lisaaLaukkuun(tokaLaukku, luoThing("Kana", 5));
            lisaaLaukkuun(tokaLaukku, luoThing("Kettu", 3));

            lisaaRuumaan(container, tokaLaukku);


            Object kolmasLaukku = luoSuitcase(10);
            lisaaLaukkuun(kolmasLaukku, luoThing("Kana", 5));
            lisaaLaukkuun(kolmasLaukku, luoThing("Kettu", 3));

            lisaaRuumaan(container, kolmasLaukku);

            Object laukut = oliomuuttujaLista(containerClass, container);
            if (laukut == null) {
                fail("Verify that class Container has a list (e.g. ArrayList) where suitcases are added.");
            }

            List<Object> ruumanLaukut = (List<Object>) laukut;


            if (!ruumanLaukut.contains(tokaLaukku)) {
                fail("Verify that suitcases are added to container's internal list structure.");
            }

            if (ruumanLaukut.contains(kolmasLaukku)) {
                fail("Verify that it isn't possible to add more suitcases to container if it exceeds the maximum weight.");
            }

        } catch (Throwable t) {
            junit.framework.Assert.fail(t.getMessage());
        }
    }

    @Test
    @Points("4.6")
    public void containerCanAddToMaxWeight() {
        try {
            Object container = luoRuuma(20);
            Object laukku = luoSuitcase(20);
            lisaaLaukkuun(laukku, luoThing("Tiili", 20));
            lisaaRuumaan(container, laukku);

            Object laukut = oliomuuttujaLista(containerClass, container);
            if (laukut == null) {
                fail("Verify that class Container has a list (e.g. ArrayList) where suitcases are added.");
            }

            List<Object> ruumanLaukut = (List<Object>) laukut;

            if (!ruumanLaukut.contains(laukku)) {
                fail("Verify that suitcases can be added to container until it exceeds maximum weight.");
            }
        } catch (Throwable t) {
            junit.framework.Assert.fail(t.getMessage());
        }
    }

    @Test
    @Points("4.6")
    public void checkPrint() {
        try {
            Object container = luoRuuma(128);

            Object ekaLaukku = luoSuitcase(10);
            lisaaLaukkuun(ekaLaukku, luoThing("Porsas", 7));
            lisaaLaukkuun(ekaLaukku, luoThing("Siili", 2));

            lisaaRuumaan(container, ekaLaukku);

            Object tokaLaukku = luoSuitcase(10);
            lisaaLaukkuun(tokaLaukku, luoThing("Kana", 5));
            lisaaLaukkuun(tokaLaukku, luoThing("Kettu", 3));

            lisaaRuumaan(container, tokaLaukku);


            Object kolmasLaukku = luoSuitcase(10);
            lisaaLaukkuun(kolmasLaukku, luoThing("Kana", 5));
            lisaaLaukkuun(kolmasLaukku, luoThing("Kettu", 3));

            lisaaRuumaan(container, kolmasLaukku);


            Method toString = ReflectionUtils.requireMethod(ReflectionUtils.findClass("Container"), "toString");
            String palautus = ReflectionUtils.invokeMethod(String.class, toString, container);


            if (!sisaltaa(palautus, "3", "suitcases", "25", "kg")) {
                throw new Exception();
            }

        } catch (Throwable t) {
            junit.framework.Assert.fail("Verify that method toString of container which has 3 suitcases returns string \"3 suitcases (<weight> kg)\", where <weight> is the combined weight of the container's suitcases.");
        }
    }

    @Test
    @Points("4.7")
    public void containerPrintThingsMethod() throws Throwable {

        _Suitcase laukku = luoM(10);
        _Container ruuma = luoL(100);


        String v = "\n"

                + "Container r = new Container(100;\n)"
                + "r.printThings();";

        assertTrue("Class Container should have method public void printThings()",
                _ContainerRef.method(ruuma, "printThings").returningVoid().takingNoParams().withNiceError(v).isPublic());

        _ContainerRef.method(ruuma, "printThings").returningVoid().takingNoParams().withNiceError(v).invoke();

    }

    @Test
    @Points("4.7")
    public void containerCheckPrint() {
        MockInOut io = new MockInOut("");

        try {
            Object container = luoRuuma(128);

            Object suitcase = luoSuitcase(10);
            lisaaLaukkuun(suitcase, luoThing("Porsas", 7));
            lisaaLaukkuun(suitcase, luoThing("Siili", 2));

            lisaaRuumaan(container, suitcase);

            suitcase = luoSuitcase(10);
            lisaaLaukkuun(suitcase, luoThing("Kana", 5));
            lisaaLaukkuun(suitcase, luoThing("Kettu", 3));

            lisaaRuumaan(container, suitcase);


            suitcase = luoSuitcase(10);
            lisaaLaukkuun(suitcase, luoThing("Silli", 5));
            lisaaLaukkuun(suitcase, luoThing("Siika", 3));

            lisaaRuumaan(container, suitcase);


            Method m = ReflectionUtils.requireMethod(ReflectionUtils.findClass("Container"), "printThings");
            ReflectionUtils.invokeMethod(void.class, m, container);


            if (!sisaltaa(io.getOutput(), "Porsas", "Siili", "Siika", "Silli", "Kana", "Kettu")) {
                throw new Exception();
            }

        } catch (Throwable t) {
            junit.framework.Assert.fail("Verify that method printThings of the class Container prints the things of every suitcase.");
        }
    }

    @Test
    @Points("4.8")
    public void containerCheckPrintWithBricks() {
        MockInOut io = new MockInOut("");
        Method lisaaTiiliskiviaRuumaanMeto = null;
        try {
            lisaaTiiliskiviaRuumaanMeto = ReflectionUtils.requireMethod(Main.class, "addSuitcasesFullOfBricks", containerClass);
        } catch (Throwable t) {
            fail("Does the class Main have method public static void addSuitcasesFullOfBricks(Container container)?");
        }

        Object container = luoRuuma(3);
        try {
            lisaaTiiliskiviaRuumaanMeto.invoke(null, container);
        } catch (Throwable ex) {
            fail("Verify that class Main has method public static void addSuitcasesFullOfBricks(Container container), and that it tries to add 100 suitcases with bricks inside them to container.");
        }


        Method toString = ReflectionUtils.requireMethod(containerClass, "toString");
        try {

            String palautus = ReflectionUtils.invokeMethod(String.class, toString, container);

            if (!sisaltaa(palautus, "2", "suitcase", "3")) {
                fail("Verify that the method addSuitcasesFullOfBricks inside the class Main tries to add the suitcases so that first it adds a suitcase with a brick weighing 1 kilo, then a brick weighing 2 kilos, ...");
            }
        } catch (Throwable t) {
            fail(t.getMessage());
        }



        container = luoRuuma(2000);
        try {
            lisaaTiiliskiviaRuumaanMeto.invoke(null, container);
        } catch (Throwable ex) {
            fail("Verify that class Main has method public static void addSuitcasesFullOfBricks(Container container), and that it tries to add 100 suitcases with bricks inside them to container.");
        }

        try {

            String palautus = ReflectionUtils.invokeMethod(String.class, toString, container);
            if (!sisaltaa(palautus, "62", "suitcase", "1953")) {
                fail("Verify that the method addSuitcasesFullOfBricks inside the class Main tries to add the suitcases so that first it adds a suitcase with a brick weighing 1 kilo, then a brick weighing 2 kilos, ...");            }
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    private void lisaaRuumaan(Object ruuma, Object laukku) {
        try {
            Method lisaaRuumaanMeto = ReflectionUtils.requireMethod(ReflectionUtils.findClass("Container"), "addSuitcase", ReflectionUtils.findClass("Suitcase"));
            ReflectionUtils.invokeMethod(void.class, lisaaRuumaanMeto, ruuma, laukku);
        } catch (Throwable ex) {
        }
    }

    private void lisaaLaukkuun(Object laukku, Object thing) {
        try {
            Method addThingMeto = ReflectionUtils.requireMethod(ReflectionUtils.findClass("Suitcase"), "addThing", ReflectionUtils.findClass("Thing"));
            ReflectionUtils.invokeMethod(void.class, addThingMeto, laukku, thing);
        } catch (Throwable ex) {
        }
    }

    private Object luoRuuma(int kapasiteetti) {
        try {

            return ReflectionUtils.invokeConstructor(containerConstructor, kapasiteetti);
        } catch (Throwable ex) {
            return null;
        }
    }

    private Object luoSuitcase(int kapasiteetti) {
        try {
            return ReflectionUtils.invokeConstructor(suitcaseConstructor, kapasiteetti);
        } catch (Throwable ex) {
            return null;
        }
    }

    private Object luoThing(String nimi, int paino) {
        try {
            return ReflectionUtils.invokeConstructor(thingConstructor, nimi, paino);
        } catch (Throwable ex) {
            return null;
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
                    Logger.getLogger(C_ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(C_ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (f.getType().equals(ArrayList.class)) {
                f.setAccessible(true);
                try {
                    return f.get(container);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(C_ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(C_ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (f.getType().equals(LinkedList.class)) {
                f.setAccessible(true);
                try {
                    return f.get(container);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(C_ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(C_ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("You do not need \"static variables\", remove from class " + klassName + " the following variable: " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All instance variables of the class should be private, but class " + klassName + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("The class " + klassName + "should have " + m + ", remove others", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}
