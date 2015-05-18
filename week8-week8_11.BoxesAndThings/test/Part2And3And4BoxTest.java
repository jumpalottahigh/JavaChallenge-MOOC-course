
import java.util.Arrays;
import java.lang.reflect.Constructor;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import java.lang.reflect.Method;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.*;
import java.lang.reflect.Field;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class Part2And3And4BoxTest<_Box, _ToBeStored, _Book, _CD> {

    static final String kirjaNimi = "Book";
    static final String levyNimi = "CD";
    static final String laatikkoNimi = "Box";
    static final String talletettavaNimi = "ToBeStored";
    private Class talletettavaClazz;
    String klassName = "Box";
    Reflex.ClassRef<_Box> klass;
    Reflex.ClassRef<_CD> _CDRef;
    Reflex.ClassRef<_Book> _BookRef;
    Reflex.ClassRef<_ToBeStored> _ToBeStoredRef;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
        _CDRef = Reflex.reflect(levyNimi);
        _BookRef = Reflex.reflect(kirjaNimi);
        _ToBeStoredRef = Reflex.reflect(talletettavaNimi);

        try {
            talletettavaClazz = ReflectionUtils.findClass(talletettavaNimi);
        } catch (Exception e) {
        }
    }

    public _CD luoCD(String n, String l, int vuosi) throws Throwable {
        return _CDRef.constructor().taking(String.class, String.class, int.class).withNiceError().invoke(n, l, vuosi);
    }

    public _Book luoBook(String n, String l, double p) throws Throwable {
        return _BookRef.constructor().taking(String.class, String.class, double.class).withNiceError().invoke(n, l, p);
    }

    public _Box luoBox(double p) throws Throwable {
        return klass.constructor().taking(double.class).withNiceError().invoke(p);
    }

    @Test
    @Points("11.2")
    public void classIsPublic() {
        assertTrue("Clas " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    @Points("11.2 11.3")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 3, "an instance variable for maximum weight and an instance variable for storing list of ToBeStored-objects");
    }

    @Test
    @Points("11.2")
    public void boxConstructor() throws Throwable {
        Reflex.MethodRef1<_Box, _Box, Double> ctor = klass.constructor().taking(double.class).withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "(double maxWeight)", ctor.isPublic());
        String v = "error caused by code Box laatikko = new Box(10.0);";
        ctor.withNiceError(v).invoke(10.0);
    }

    @Test
    @Points("11.2")
    public void addThingMethod1() throws Throwable {
        _Box l = luoBox(10);
        _CD cd = luoCD("Pink Floyd", "Dark side of the moon", 1972);
        _ToBeStored t = (_ToBeStored) cd;

        String v = "\nBox laatikko = new Box(10.0); \n"
                + "ToBeStored t = new CD(\"Pink Floyd\", \"Dark side of the moon\";)\n"
                + "l.add(t);";

        assertTrue("Class Box should have method public void add(ToBeStored t)",
                klass.method(l, "add").returningVoid().taking(_ToBeStoredRef.cls()).withNiceError(v).isPublic());

        klass.method(l, "add").returningVoid().taking(_ToBeStoredRef.cls()).withNiceError(v).invoke(t);
    }

    @Test
    @Points("11.2")
    public void addThingMethod2() throws Throwable {
        _Box l = luoBox(10);
        _Book k = luoBook("Dostojevski", "Rikos ja Rangaistus", 1);
        _ToBeStored t = (_ToBeStored) k;

        String v = "\nBox laatikko = new Box(10.0); \n"
                + "ToBeStored t = new CD(\"Pink Floyd\", \"Dark side of the moon\";)\n"
                + "l.add(t);";

        assertTrue("Class Box should have method public void add(ToBeStored t)",
                klass.method(l, "add").returningVoid().taking(_ToBeStoredRef.cls()).withNiceError(v).isPublic());

        klass.method(l, "add").returningVoid().taking(_ToBeStoredRef.cls()).withNiceError(v).invoke(t);
    }

    public Object mk(double p) throws Throwable {
        Class kl = ReflectionUtils.findClass(kirjaNimi);
        Constructor c = ReflectionUtils.requireConstructor(kl, String.class, String.class, double.class);
        return ReflectionUtils.invokeConstructor(c, "ISO", "KIRJA", p);
    }

    @Test
    @Points("11.2")
    public void boxWorks() throws Throwable {
        Class kl = ReflectionUtils.findClass(laatikkoNimi);
        Constructor c = ReflectionUtils.requireConstructor(kl, double.class);
        Method add = ReflectionUtils.requireMethod(kl, "add", talletettavaClazz);
        Object o = ReflectionUtils.invokeConstructor(c, 10.0);

        ReflectionUtils.invokeMethod(Void.TYPE, add, o, mk(3));

        assertFalse("Create for class Box method toString as defined in the assignment",o.toString().contains("@"));

        assertEquals("Check that class " + laatikkoNimi + "'s method toString is correct!",
                "Box: 1 things, total weight 3.0 kg",
                o.toString());

        ReflectionUtils.invokeMethod(Void.TYPE, add, o, mk(4));

        assertEquals("Check that class " + laatikkoNimi + "'s method toString is correct!",
                "Box: 2 things, total weight 7.0 kg",
                o.toString());

        ReflectionUtils.invokeMethod(Void.TYPE, add, o, mk(4));

        assertEquals("Check that it isn't possible to add an item if it's too heavy!",
                "Box: 2 things, total weight 7.0 kg",
                o.toString());
    }

    @Test
    @Points("11.3")
    public void methodWeight() throws Throwable {
        _Box l = luoBox(10);

        String v = "\nBox laatikko = new Box(10.0); \n"
                + "l.weightt);";

        assertTrue("Class Box should have method public double weight()",
                klass.method(l, "weight").returning(double.class).takingNoParams().withNiceError(v).isPublic());

        klass.method(l, "weight").returning(double.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("11.3")
    public void boxWeightMethodWorks() throws Throwable {
        Object o = Reflex.reflect(laatikkoNimi).constructor().taking(double.class).invoke(10.0);
        MethodRef0<Object, Double> weight = Reflex.reflect(laatikkoNimi).method("weight").returning(double.class).takingNoParams();
        ClassRef talletettava = Reflex.reflect(talletettavaNimi);
        MethodRef1 add = Reflex.reflect(laatikkoNimi).method("add").returningVoid().taking(talletettava.getReferencedClass());

        double eps = 0.001;

        assertEquals("Empty box's weight should be 0!",
                0,
                weight.invokeOn(o),
                eps);

        add.invokeOn(o, mk(5));

        assertEquals("Weight should increase when an item is added to the box!",
                5,
                weight.invokeOn(o),
                eps);

        add.invokeOn(o, mk(0.5));

        assertEquals("Weight should increase when an item is added to the box!",
                5.5,
                weight.invokeOn(o),
                eps);

        add.invokeOn(o, mk(1000));

        assertEquals("Weight shouldn't increase when too heavy item is added to the box!",
                5.5,
                weight.invokeOn(o),
                eps);
    }

    @Test
    @Points("11.4")
    public void boxHasToBeStored() {
        Class talletettava = Reflex.reflect(talletettavaNimi).getReferencedClass();
        Class laatikko = Reflex.reflect(laatikkoNimi).getReferencedClass();

        Class is[] = laatikko.getInterfaces();
        Class oikein[] = {talletettava};

        assertTrue("Check that class " + laatikkoNimi + " implements interface ToBeStored",
                Arrays.equals(is, oikein));
    }

    @Test
    @Points("11.4")
    public void boxCountsItsWeight() throws Throwable {
        Object o = Reflex.reflect(laatikkoNimi).constructor().taking(double.class).invoke(10.0);
        Object laatikko = Reflex.reflect(laatikkoNimi).constructor().taking(double.class).invoke(20.0);
        MethodRef0<Object, Double> weight = Reflex.reflect(laatikkoNimi).method("weight").returning(double.class).takingNoParams();
        ClassRef talletettava = Reflex.reflect(talletettavaNimi);
        MethodRef1 add = Reflex.reflect(laatikkoNimi).method("add").returningVoid().taking(talletettava.getReferencedClass());

        double eps = 0.001;
        add.invokeOn(laatikko, o);

        assertEquals("Empty box's weight should be 0!",
                0,
                weight.invokeOn(o),
                eps);

        add.invokeOn(o, mk(5));

        assertEquals("Weight should increase when an item is added to the box! Check code\n"
                + "Box laatikko = new Box(10); "
                + "laatikko.add( new Book(\"Horstman\", \"Core Java\",5) );\n"
                + "laatikko.weight();\n",
                5,
                weight.invokeOn(o),
                eps);

        assertEquals("Weight should increase when box contains another box and a new item is added to the inner box!\n"
                + "Box isoBox = new Box(20); \n"
                + "Box laatikko = new Box(10); \n"
                + "isoBox.add(laatikko);\n"
                + "laatikko.add( new Book(\"Horstman\", \"Core Java\",5) );\n"
                + "isoBox.weight();\n",
                5,
                weight.invokeOn(laatikko),
                eps);

        add.invokeOn(o, mk(0.5));

        assertEquals("Weight should increase when an item is added to the box!\n"
                + "Box laatikko = new Box(10); "
                + "laatikko.add( new Book(\"Horstman\", \"Core Java\",5) );\n"
                + "laatikko.add( new Book(\"Beck\", \"Test Driven Development\",0.5) );\n"
                + "laatikko.weight();\n",
                5.5,
                weight.invokeOn(o),
                eps);

        assertEquals("Weight should increase when box contains another box and a new item is added to the inner box!\n"
                + "Box isoBox = new Box(20); \n"
                + "Box laatikko = new Box(10); \n"
                + "isoBox.add(laatikko);\n"
                + "laatikko.add( new Book(\"Horstman\", \"Core Java\",5) );\n"
                + "laatikko.add( new Book(\"Beck\", \"Test Driven Development\",0.5) );\n"
                + "isoBox.weight();\n",
                5.5,
                weight.invokeOn(laatikko),
                eps);


        add.invokeOn(o, mk(1000));

        assertEquals("Weight shouldn't increase when too heavy item is added to the box!\n"
                + "Box laatikko = new Box(10); "
                + "laatikko.add( new Book(\"Horstman\", \"Core Java\",5) );\n"
                + "laatikko.add( new Book(\"Beck\", \"Test Driven Development\",0.5) );\n"
                + "laatikko.add( new Book(\"Nietzsche\", \"Also spracht Zarahustra\",1000) );\n"
                + "laatikko.weight();\n",
                5.5,
                weight.invokeOn(o),
                eps);
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("you do not need \"static variables\", remove from class " + klassName + " the following variable: " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("All instance variables should be private but class " + klassName + " had: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("The class " + klassName + " should only have " + m + ", remove others", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}