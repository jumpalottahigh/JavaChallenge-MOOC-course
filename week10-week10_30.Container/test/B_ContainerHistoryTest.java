
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.junit.*;
import static org.junit.Assert.*;

public class B_ContainerHistoryTest {

    String klassName = "containers.ContainerHistory";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setup() {
        classRef = Reflex.reflect(klassName);
    }

    @Test
    @Points("30.3")
    public void classExists() {
        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("30.3")
    public void noInheritance() {
        Class c = ReflectionUtils.findClass(klassName);

        Class sc = c.getSuperclass();
        assertTrue("Class ContainerHistory shouldn't inherit any class!", sc.getName().equals("java.lang.Object"));
    }

    @Test
    @Points("30.3")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "instance variable of type list");
    }

    @Test
    @Points("30.3")
    public void hasConstructor() throws Throwable {
        newContainerHistory();
    }

    @Test
    @Points("30.3")
    public void hasMethodAdd() throws Throwable {
        String metodi = "add";
        String virhe = "Create method public void add(double situation) for class ContainerHistory";

        Object o = newContainerHistory();

        assertTrue(virhe,
                classRef.method(o, metodi).returningVoid().taking(double.class).isPublic());

        String v = "ContainerHistory m = new ContainerHistory();\n"
                + "m.add(99);\n";

        classRef.method(o, metodi).returningVoid().taking(double.class).withNiceError(v).invoke(99.0);
    }

    @Test
    @Points("30.3")
    public void hasMethodReset() throws Throwable {
        String metodi = "reset";
        String virhe = "Create method public void reset() for class ContainerHistory";

        Object o = newContainerHistory();

        assertTrue(virhe,
                classRef.method(o, metodi).returningVoid().takingNoParams().isPublic());

        String v = "ContainerHistory m = new ContainerHistory();\n"
                + "m.reset();\n";

        classRef.method(o, metodi).returningVoid().takingNoParams().withNiceError(v).invoke();

    }

    @Test
    @Points("30.3")
    public void hasToString() throws Throwable {
        Object mh = newContainerHistory();
        String v = "ContainerHistory m = new ContainerHistory();\n"
                + "m.toString();\n";

        assertFalse("Create method toString for class ContainerHistory as specified in the assignment"
                + "\n i.e. calling the toString-method of the list ContainerHistory"
                + " has as an instance variable", toString(mh,v).contains("@"));
    }

    private String toString(Object o, String v) throws Throwable{
         return classRef.method(o, "toString").returning(String.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("30.3")
    public void toStringEmptyHistory() throws Throwable {
        Object mh = newContainerHistory();
        ArrayList<Double> lista = new ArrayList<Double>();

        assertEquals("Create method toString for class ContainerHistory as specified in the assignment\n"
                + "i.e. calling the toString-method of the list ContainerHistory has as an instance variable", lista.toString(), mh.toString());
    }

    /*
     *
     */
    @Test
    @Points("30.3")
    public void toStringWorks1() throws Throwable {
        Object mh = newContainerHistory();

        String k = "check code\nmh = new ContainerHistory();\n"
                + "mh.add(5.0);\n";

        add(mh, 5, k);
        ArrayList<Double> lista = new ArrayList<Double>();
        lista.add(5.0);

        k = "check code\nmh = new ContainerHistory();\n"
                + "mh.add(5.0);\n"
                + "System.out.println(mh)";

        assertEquals(k, lista.toString(), toString(mh,k));
    }

    @Test
    @Points("30.3")
    public void toStringWorks2() throws Throwable {
        Object mh = newContainerHistory();

        String k = "check code \n"
                + "mh = new ContainerHistory();\n"
                + "mh.add(5.0);\n"
                + "mh.add(12.0);\n"
                + "mh.add(4.0);\n";
        add(mh, 5, k);
        add(mh, 12, k);
        add(mh, 4, k);
        ArrayList<Double> lista = new ArrayList<Double>();
        lista.add(5.0);
        lista.add(12.0);
        lista.add(4.0);

        k = "check code\nmh = new ContainerHistory();\nmh.add(5.0);\nmh.add(12.0);\n"
                + "mh.add(4.0);\nSystem.out.println(mh)\n";

        assertEquals(k, lista.toString(), toString(mh,k));

        k = "check code\nmh = new ContainerHistory();\nmh.add(5.0);\nmh.add(12.0);\n"
                + "mh.add(4.0);\nmh.reset();\nSystem.out.println(mh)\n";

        reset(mh, "check code\nmh = new ContainerHistory();\nmh.add(5.0);\n"
                + "mh.add(12.0);\nmh.add(4.0);\nmh.reset()\n;");
        lista.clear();

        assertEquals(k, lista.toString(), toString(mh,k));
    }

    // ************
    @Test
    @Points("30.4")
    public void hasMethodMinValue() throws Throwable {
        String metodi = "minValue";
        String virhe = "Create method public double minValue() for class ContainerHistory";

        Object o = newContainerHistory();

        assertTrue(virhe,
                classRef.method(o, metodi).returning(double.class).takingNoParams().isPublic());

        String v = "ContainerHistory m = new ContainerHistory();\n"
                + "m.add(99);\n"
                + "m.minValue();\n";

        classRef.method(o, "add").returningVoid().taking(double.class).withNiceError(v).invoke(99.0);

        assertEquals(v, 99.0,
                classRef.method(o, metodi).returning(double.class).takingNoParams().withNiceError(v).invoke(), 0.01);
    }

    @Test
    @Points("30.4")
    public void hasMethodMaxValue() throws Throwable {
        String metodi = "maxValue";
        String virhe = "Create method public double maxValue() for class ContainerHistory";
        Object o = newContainerHistory();

        assertTrue(virhe,
                classRef.method(o, metodi).returning(double.class).takingNoParams().isPublic());

        String v = "ContainerHistory m = new ContainerHistory();\n"
                + "m.add(99);\n"
                + "m.maxValue();\n";

        classRef.method(o, "add").returningVoid().taking(double.class).withNiceError(v).invoke(99.0);

        assertEquals(v, 99.0,
                classRef.method(o, metodi).returning(double.class).takingNoParams().withNiceError(v).invoke(), 0.01);
    }

    @Test
    @Points("30.4")
    public void hasMethodAverage() throws Throwable {
        String metodi = "average";
        String virhe = "Create method public double average() for class ContainerHistory";
        Object o = newContainerHistory();

        assertTrue(virhe,
                classRef.method(o, metodi).returning(double.class).takingNoParams().isPublic());

        String v = "ContainerHistory m = new ContainerHistory();\n"
                + "m.add(99);\n"
                + "m.average();\n";

        classRef.method(o, "add").returningVoid().taking(double.class).withNiceError(v).invoke(99.0);

        assertEquals(v, 99.0,
                classRef.method(o, metodi).returning(double.class).takingNoParams().withNiceError(v).invoke(), 0.01);
    }

    /*
     *
     */
    @Test
    @Points("30.4")
    public void valueCalculatedRight1() throws Throwable {
        String k = "check code\n"
                + "mh = new ContainerHistory();\nmh.add(4.0);\nmh.add(-1.0);\nmh.add(3);\n";


        Object mh = newContainerHistory();
        add(mh, 4, k);
        add(mh, -1, k);
        add(mh, 3, k);

        assertEquals(k + "mh.minValue() ", -1, minValue(mh, k + "mh.minValue()"), 0.01);
        assertEquals(k + "mh.maxValue() ", 4, maxValue(mh, k + "mh.maxValue()"), 0.01);
        assertEquals(k + "mh.average() ", 2, average(mh, k + "mh.average()"), 0.01);
    }

    @Test
    @Points("30.4")
    public void valuesCalculatedRightWhenResetting() throws Throwable {
        String k = "check code\nmh = new ContainerHistory();\nmh.add(7.0);\nmh.reset();\nmh.add(4.0);\nmh.add(-1.0);\nmh.add(3);\n";


        Object mh = newContainerHistory();
        add(mh, 7, k);
        reset(mh, "check code\nmh = new ContainerHistory();\nmh.add(7.0);\nmh.reset();\n");
        add(mh, 4, k);
        add(mh, -1, k);
        add(mh, 3, k);


        assertEquals(k + "mh.minValue() ", -1, minValue(mh, k + "mh.minValue()"), 0.01);
        assertEquals(k + "mh.maxValue() ", 4, maxValue(mh, k + "mh.maxValue()"), 0.01);
        assertEquals(k + "mh.average() ", 2, average(mh, k + "mh.average()"), 0.01);
    }

    @Test
    @Points("30.4")
    public void valueCalculatedRight2() throws Throwable {
        for (int i = 0; i < 5; i++) {
            ArrayList<Double> luvut = arvoLukuja();

            Object mh = newContainerHistory();
            String lvut = "";
            for (Double luku : luvut) {
                lvut += luku + " ";
                add(mh, luku, "when added the following numbers to container history: " + lvut);
            }

            assertEquals("when added the following numbers to container history: " + luvut + " minValue ", Collections.min(luvut), minValue(mh, "when added the following numbers to container history: " + luvut + " minValue "), 0.01);
            assertEquals("when added the following numbers to container history: " + luvut + " maxValue ", Collections.max(luvut), maxValue(mh, "when added the following numbers to container history: " + luvut + " maxValue "), 0.01);
            assertEquals("when added the following numbers to container history: " + luvut + " average ", ka(luvut), average(mh, "when added the following numbers to container history: " + luvut + " average "), 0.01);
        }
    }

    // ***********
    /*
     *
     */
    @Test
    @Points("30.5")
    public void hasMethodGreatestFluctuation() throws Throwable {
        String metodi = "greatestFluctuation";
        String virhe = "Create method public double greatestFluctuation() for class ContainerHistory";
        Object o = newContainerHistory();

        assertTrue(virhe,
                classRef.method(o, metodi).returning(double.class).takingNoParams().isPublic());

        String v = "ContainerHistory m = new ContainerHistory();\n"
                + "m.add(99);\n"
                + "m.add(100);\n"
                + "m.greatestFluctuation();\n";

        classRef.method(o, "add").returningVoid().taking(double.class).withNiceError(v).invoke(99.0);
        classRef.method(o, "add").returningVoid().taking(double.class).withNiceError(v).invoke(100.0);


        assertEquals(v, 1.0,
                classRef.method(o, metodi).returning(double.class).takingNoParams().withNiceError(v).invoke(), 0.01);
    }

    @Test
    @Points("30.5")
    public void hasMethodVariance() throws Throwable {
        String metodi = "variance";
        String virhe = "Create method public double variance() for class ContainerHistory";
        Object o = newContainerHistory();

        assertTrue(virhe,
                classRef.method(o, metodi).returning(double.class).takingNoParams().isPublic());

        String v = "ContainerHistory m = new ContainerHistory();\n"
                + "m.add(99);\n"
                + "m.add(99);\n"
                + "m.variance();\n";

        classRef.method(o, "add").returningVoid().taking(double.class).withNiceError(v).invoke(99.0);
        classRef.method(o, "add").returningVoid().taking(double.class).withNiceError(v).invoke(99.0);

        assertEquals(v,0.0,
                classRef.method(o, metodi).returning(double.class).takingNoParams().withNiceError(v).invoke(), 0.01);
    }

    /*
     *
     */
    @Test
    @Points("30.5")
    public void greatestFluctuationCalculatedRight1() throws Throwable {
        Object mh = newContainerHistory();

        String k = "check code\nmh = new ContainerHistory();\nmh.add(1.0);\nmh.add(2.0);\nmh.add(4.0); ";

        add(mh, 1, k);
        add(mh, 2, k);
        add(mh, 4, k);

        assertEquals(k + "mh.greatestFluctuation() ", 2, greatestFluctuation(mh, k + "mh.greatestFluctuation();"), 0.01);
    }

    @Test
    @Points("30.5")
    public void greatestFluctuationCalculatedRight2() throws Throwable {
        Object mh = newContainerHistory();

        String k = "check code\nmh = new ContainerHistory();\nmh.add(5.0); ";
        add(mh, 5, k);

        assertEquals(k + "mh.greatestFluctuation() ", 0, greatestFluctuation(mh,k + "mh.greatestFluctuation();"), 0.01);
    }

    @Test
    @Points("30.5")
    public void greatestFluctuationCalculatedRight3() throws Throwable {
        Object mh = newContainerHistory();

        String k = "check code\nmh = new ContainerHistory();\nmh.add(5.0);\nmh.add(4.0);\nmh.add(7.0); ";

        add(mh, 5,k);
        add(mh, 4,k);
        add(mh, 7,k);

        assertEquals(k + "mh.greatestFluctuation() ", 3, greatestFluctuation(mh, k + "mh.greatestFluctuation();"), 0.01);
    }

    @Test
    @Points("30.5")
    public void greatestFluctuationCalculatedRight4() throws Throwable {

        String k = "check code\nmh = new ContainerHistory();\nmh.add(5.0);\nmh.add(4.0);\nmh.add(7.0);\nmh.add(3.0);\nmh.add(12.0);\nmh.add(3.0);";

        Object mh = newContainerHistory();
        add(mh, 5,k);
        add(mh, 4,k);
        add(mh, 7,k);
        add(mh, 1,k);
        add(mh, 12,k);
        add(mh, 3,k);

        assertEquals(k + "mh.greatestFluctuation() ", 11, greatestFluctuation(mh,k + "mh.greatestFluctuation();"), 0.01);
    }

    @Test
    @Points("30.5")
    public void varianceCalculatedRight1() throws Throwable {
        Object mh = newContainerHistory();


        String k = "check code\nmh = new ContainerHistory();\nmh.add(1.0);\nmh.add(2.0);\nmh.add(4.0); ";

        add(mh, 2,k);
        add(mh, 2,k);
        add(mh, 2,k);
        add(mh, 2,k);


        assertEquals(k + "mh.variance() ", 0, variance(mh, k + "mh.variance();"), 0.01);
    }

    @Test
    @Points("30.5")
    public void varianceCalculatedRight2() throws Throwable {
        Object mh = newContainerHistory();
        String k = "check code\nmh = new ContainerHistory();\nmh.add(3.0);\nmh.add(1.0);\nmh.add(2.0); ";

        add(mh, 3,k);
        add(mh, 1,k);
        add(mh, 2,k);

        assertEquals(k + "mh.variance() ", 1, variance(mh, k + "mh.variance();"), 0.01);
    }

    @Test
    @Points("30.5")
    public void varianceCalculatedRight3() throws Throwable {
        Object mh = newContainerHistory();

        String k = "check code\nmh = new ContainerHistory();\nmh.add(7.0);\nmh.add(-2.0);\nmh.add(1.0);\nmh.add(2.0);\nmh.add(5.0);\nmh.add(11.0);";

        add(mh, 7,k);
        add(mh, -2,k);
        add(mh, 1,k);
        add(mh, 2,k);
        add(mh, 5,k);
        add(mh, 11,k);

        assertEquals(k + "mh.variance() ", 21.6, variance(mh,k + "mh.variance();"), 0.01);
    }

    /*
     *
     */

    private void add(Object o, double tilavuus, String v) throws Throwable {
        classRef.method(o, "add").returningVoid().taking(double.class).withNiceError(v).invoke(tilavuus);

    }

    private void reset(Object o, String v) throws Throwable {
        classRef.method(o, "reset").returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    private double minValue(Object o, String v) throws Throwable {
        return classRef.method(o, "minValue").returning(double.class).takingNoParams().withNiceError(v).invoke();
    }

    private double maxValue(Object o, String v) throws Throwable {
        return classRef.method(o, "maxValue").returning(double.class).takingNoParams().withNiceError(v).invoke();
    }

    private double average(Object o, String v) throws Throwable {
        return classRef.method(o, "average").returning(double.class).takingNoParams().withNiceError(v).invoke();
    }

    private double greatestFluctuation(Object o, String v) throws Throwable {
        return classRef.method(o, "greatestFluctuation").returning(double.class).takingNoParams().withNiceError(v).invoke();
    }

    private double variance(Object o, String v) throws Throwable {
        return classRef.method(o, "variance").returning(double.class).takingNoParams().withNiceError(v).invoke();
    }

    Random arpa = new Random();

    private ArrayList<Double> arvoLukuja() {
        ArrayList<Double> luvut = new ArrayList<Double>();

        int raja = 10 + arpa.nextInt(10);

        for (int i = 0; i < raja; i++) {
            luvut.add(20.0 - arpa.nextInt(30));
        }

        return luvut;
    }

    private double ka(ArrayList<Double> luvut) {
        double s = 0;
        for (Double luku : luvut) {
            s += luku;
        }
        return s / luvut.size();
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

    private Object newContainerHistory() throws Throwable {
        assertTrue("Create for class ContainerHistory a constructor ContainerHistory()", classRef.constructor().takingNoParams().isPublic());

        Reflex.MethodRef0<Object, Object> ctor = classRef.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }
}
