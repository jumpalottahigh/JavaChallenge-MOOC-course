
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.*;
import static org.junit.Assert.*;
import containers.Container;

public class C_ProductContainerRecorderTest {

    String klassName = "containers.ProductContainerRecorder";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setup() {
        classRef = Reflex.reflect(klassName);
    }

    @Test
    @Points("30.6")
    public void classExists() {
        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("30.6")
    public void inheritsClassProductContainer() {
        Class c = ReflectionUtils.findClass(klassName);

        Class sc = c.getSuperclass();
        assertTrue("Class ProductContainerRecorder should extend class ProductContainer", sc.getName().equals("containers.ProductContainer"));
    }

    @Test
    @Points("30.6")
    public void hasThreeParameterConstructor() throws Throwable {
        newMustavaProductContainer("vilja", 10, 5);
    }

    @Test
    @Points("30.6")
    public void initialVolumeIsContainersInitialVolume() throws Throwable {
        containers.Container v = (containers.Container) newMustavaProductContainer("vilja", 10, 5);
        Assert.assertEquals("Are you setting the container's initial volume when you are calling the constructor of the ProductContainer?", 5, v.getVolume(), 0.01);
        Assert.assertEquals("Are you setting the container's capacity when you are calling the constructor of the ProductContainer?", 10, v.getOriginalCapacity(), 0.01);
    }

    @Test
    @Points("30.6")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "an instance variable of type ContainerHistory");
    }

    /*
     *
     */
    @Test
    @Points("30.6")
    public void hasMethodHistory() throws Throwable {
        String metodi = "history";
        String virhe = "Create method public String history() for class ProductContainerRecorder";

        Object o = newMustavaProductContainer("olut", 10, 2);

        assertTrue(virhe,
                classRef.method(o, metodi).returning(String.class).takingNoParams().isPublic());

        String v = "ProductContainerRecorder mtv = new ProductContainerRecorder(\"olut\", 10, 2);\n"
                + "mtv.history();\n";

        assertEquals(v, "[2.0]",
                classRef.method(o, metodi).returning(String.class).takingNoParams().withNiceError(v).invoke());

        o = newMustavaProductContainer("maito", 100, 99);

        v = "ProductContainerRecorder mtv = new ProductContainerRecorder(\"maito\", 100, 99);\n"
                + "mtv.history();\n";

        assertEquals(v, "[99.0]",
                classRef.method(o, metodi).returning(String.class).takingNoParams().withNiceError(v).invoke());

    }

    // ************
    @Test
    @Points("30.7")
    public void hasMethodAddToTheContainer() throws Throwable {
        String metodi = "addToTheContainer";
        String virhe = "Create method public void addToTheContainer(double amount) for class ProductContainerRecorder";

        Object o = newMustavaProductContainer("olut", 10, 2);

        assertTrue(virhe,
                classRef.method(o, metodi).returningVoid().taking(double.class).isPublic());

        String v = "ProductContainerRecorder mtv = new ProductContainerRecorder(\"olut\", 10, 2);\n"
                + "mtv.addToTheContainer(3);\n";

        classRef.method(o, metodi).returningVoid().taking(double.class).withNiceError(v).invoke(3.0);
    }

    @Test
    @Points("30.7")
    public void hasMethodTakeFromTheContainer() throws Throwable {
        String metodi = "takeFromTheContainer";
        String virhe = "Create method public double takeFromTheContainer(double amount) for class ProductContainerRecorder";

        Object o = newMustavaProductContainer("olut", 10, 2);

        assertTrue(virhe,
                classRef.method(o, metodi).returning(double.class).taking(double.class).isPublic());

        String v = "ProductContainerRecorder mtv = new ProductContainerRecorder(\"olut\", 10, 2);\n"
                + "mtv.takeFromTheContainer(1);\n";

        assertEquals(v, 1.0, (double) classRef.method(o, metodi).returning(double.class).taking(double.class).withNiceError(v).invoke(1.0), 0.01);
    }

    @Test
    @Points("30.7")
    public void addAndTakeWork() throws Throwable {
        Container mtv = (Container) (newMustavaProductContainer("kahvi", 10, 5));

        String koodi = "check code\nmtv = new ProductContainerRecorder(\"kahvi\",10,5);\nmtv.addToTheContainer(5);\nmtv.getSaldo() ";

        addToTheContainer(mtv, 5, koodi);

        assertEquals("Does the ProductContainerRecorder's method addToTheContainer call overwritten method? \ncheck code\n"
                + koodi, 10, mtv.getVolume(), 0.01);

        takeFromTheContainer(mtv, 7, koodi);
        koodi = "check code\nmtv = new ProductContainerRecorder(\"kahvi\",10,5);\nmtv.addToTheContainer(5);\nmtv.takeFromTheContainer(7);\nmtv.getSaldo() ";

        assertEquals("Does the ProductContainerRecorder's method takeFromTheContainer call overwritten method?\ncheck code\n"
                + koodi, 3, mtv.getVolume(), 0.01);
    }

    @Test
    @Points("30.7")
    public void addAndTakeAffectHistory1() throws Throwable {
        Container mtv = (Container) (newMustavaProductContainer("kahvi", 10, 5));

        String koodi = "check code\n"
                + "mtv = new ProductContainerRecorder(\"kahvi\",10,5);\nmtv.addToTheContainer(5);\nmtv.history() ";

        addToTheContainer(mtv, 5, koodi);

        assertEquals("remember to keep containerhistory up-to-date when calling method addToTheContainer! \ncheck code\n"
                + koodi, "[5.0, 10.0]", history(mtv, koodi));

        takeFromTheContainer(mtv, 7, koodi);


        koodi = "check code\n"
                + "mtv = new ProductContainerRecorder(\"kahvi\",10,5);\nmtv.addToTheContainer(5);\nmtv.takeFromTheContainer(3);\nmtv.history() ";

        assertEquals("remember to keep containerhistory up-to-date when calling addToTheContainer and"
                + "takeFromTheContainer! \ncheck code\n"
                + koodi, "[5.0, 10.0, 3.0]", history(mtv, koodi));
    }

    @Test
    @Points("30.7")
    public void addAndTakeAffectHistory2() throws Throwable {
        Container mtv = (Container) (newMustavaProductContainer("kahvi", 10, 5));

        String koodi = "check code\n"
                + "mtv = new ProductContainerRecorder(\"kahvi\",10,5);\nmtv.addToTheContainer(1);\nmtv.addToTheContainer(1);\nmtv.addToTheContainer(1);\nmtv.addToTheContainer(1);\nmtv.history() ";

        addToTheContainer(mtv, 1, koodi);
        addToTheContainer(mtv, 1, koodi);
        addToTheContainer(mtv, 1, koodi);
        addToTheContainer(mtv, 1, koodi);

        assertEquals("remember to maintain containerhistory  metodien addToTheContainer ja"
                + "takeFromTheContainer kutsujen yhteydessä! \ncheck code\n"
                + koodi, "[5.0, 6.0, 7.0, 8.0, 9.0]", history(mtv, koodi));
        takeFromTheContainer(mtv, 3, koodi);
        takeFromTheContainer(mtv, 3, koodi);
        takeFromTheContainer(mtv, 3, koodi);

        koodi = "check code\n"
                + "mtv = new ProductContainerRecorder(\"kahvi\",10,5);\nmtv.addToTheContainer(1);\nmtv.addToTheContainer(1);\nmtv.addToTheContainer(1);\nmtv.addToTheContainer(1);\nmtv.takeFromTheContainer(3);\nmtv.takeFromTheContainer(3);\nmtv.takeFromTheContainer(3);  mtv.history() ";

        assertEquals("remember to keep containerhistory up-to-date when calling addToTheContainer and"
                + "takeFromTheContainer! \ncheck code\n"
                + koodi, "[5.0, 6.0, 7.0, 8.0, 9.0, 6.0, 3.0, 0.0]", history(mtv, koodi));
    }

    @Test
    @Points("30.7")
    public void takeFromTheContainer1() throws Throwable {
        Container mtv = (Container) (newMustavaProductContainer("kahvi", 10, 5));

        String koodi = "Are you sure you're not returning from the container more than it has? Check code\n"
                + "mtv = new ProductContainerRecorder(\"kahvi\",10,5);\nmtv.takeFromTheContainer(7); ";

        double saatiin = takeFromTheContainer(mtv, 7, koodi);
        assertEquals(koodi, 5, saatiin, 0.1);
    }

    //*********
    @Test
    @Points("30.8")
    public void hasMethodPrintAnalysis() throws Throwable {
        String metodi = "printAnalysis";
        String virhe = "Create method public void printAnalysis() for class ProductContainerRecorder";

        Object o = newMustavaProductContainer("olut", 10, 2);

        assertTrue(virhe,
                classRef.method(o, metodi).returningVoid().takingNoParams().isPublic());

        String v = "ProductContainerRecorder mtv = new ProductContainerRecorder(\"olut\", 10, 2);\n"
                + "mtv.addToTheContainer(5);\n";

        addToTheContainer(o, 5, v);

        classRef.method(o, metodi).returningVoid().takingNoParams().withNiceError(v).invoke();

    }

    /*
     *
     */
    @Test
    @Points("30.8")
    public void printAnalysisContainsWantedLines() throws Throwable {
        MockInOut io = new MockInOut("");
        Object v = newMustavaProductContainer("olut", 10, 5);

        String k = "ProductContainerRecorder mtv = new ProductContainerRecorder(\"olut\", 10, 2);\n"
                + "mtv.printAnalysis();\n";

        printAnalysis(v, k);
        String[] rivit = io.getOutput().split("\n");
        String haettava = "Product:";
        String rivi = hae(rivit, haettava);
        assertTrue("Method printAnalysis should print line which has string \"" + haettava + "\"", rivi != null);
        haettava = "History:";
        rivi = hae(rivit, haettava);
        assertTrue("Method printAnalysis should print line which has string \"" + haettava + "\"", rivi != null);
        haettava = "Greatest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue("Method printAnalysis should print line which has string \"" + haettava + "\"", rivi != null);
        haettava = "Smallest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue("Method printAnalysis should print line which has string \"" + haettava + "\"", rivi != null);
        haettava = "Average:";
        rivi = hae(rivit, haettava);
        assertTrue("Method printAnalysis should print line which has string \"" + haettava + "\"", rivi != null);
    }

    @Test
    @Points("30.8")
    public void printAnalysisWorks1() throws Throwable {
        MockInOut io = new MockInOut("");
        Object v = newMustavaProductContainer("kahvi", 10, 2);

        String k = "Does printAnalysis work correctly? With code \n"
                + "tv = new ProductContainerRecorder(\"kahvi\", 10, 2);\n"
                + "mtv.addToTheContainer(4);\nmtv.takeFromTheContainer(2);\n"
                + "mtv.printAnalysis()\n it prints line \"";

        addToTheContainer(v, 4, k);
        takeFromTheContainer(v, 2, k);

        printAnalysis(v, k);
        String[] rivit = io.getOutput().split("\n");

        String haettava = "History:";
        String rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("[2.0, 6.0, 4.0]"));

        haettava = "Product:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("kahvi"));

        haettava = "Greatest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("6"));

        haettava = "Smallest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("2"));

        haettava = "Average:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("4"));

    }

    @Test
    @Points("30.8")
    public void printAnalysisWorks2() throws Throwable {
        MockInOut io = new MockInOut("");
        Object v = newMustavaProductContainer("kahvi", 10, 2);
        String k = "Does printAnalysis work correctly? With code \n"
                + "mtv = new ProductContainerRecorder(\"kahvi\", 10, 2);\n"
                + "mtv.addToTheContainer(4);\nmtv.takeFromTheContainer(2);\nmtv.printAnalysis()\n"
                + "it prints line \"";

        addToTheContainer(v, 4, k);
        takeFromTheContainer(v, 2, k);
        addToTheContainer(v, 6, k);
        takeFromTheContainer(v, 2, k);

        printAnalysis(v, k);
        String[] rivit = io.getOutput().split("\n");

        String haettava = "History:";
        String rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("[2.0, 6.0, 4.0, 10.0, 8.0]"));

        haettava = "Product:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("kahvi"));

        haettava = "Greatest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("10"));

        haettava = "Smallest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("2"));

        haettava = "Average:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("6"));
    }

    //**
    @Test
    @Points("30.9")
    public void printAnalysisContainsVarianceAndGreatestFluctuation() throws Throwable {
        MockInOut io = new MockInOut("");
        Object v = newMustavaProductContainer("olut", 10, 5);

        String k = "ProductContainerRecorder mtv = new ProductContainerRecorder(\"olut\", 10, 5);\n"
                + "mtv.printAnalysis();\n";

        printAnalysis(v, k);
        String[] rivit = io.getOutput().split("\n");
        String haettava = "Variance:";
        String rivi = hae(rivit, haettava);
        assertTrue("Method printAnalysis should print line which has string \"" + haettava + "\"", rivi != null);
        haettava = "Greatest change:";
        rivi = hae(rivit, haettava);
        assertTrue("Method printAnalysis should print line which has string \"" + haettava + "\"", rivi != null);

    }

    @Test
    @Points("30.9")
    public void extendedPrintAnalysisWorks1() throws Throwable {

        MockInOut io = new MockInOut("");
        Object v = newMustavaProductContainer("kahvi", 10, 1);

        String k = "Does printAnalysis work correctly? With code\n"
                + "mtv = new ProductContainerRecorder(\"kahvi\", 10, 1);\n"
                + "mtv.addToTheContainer(9);\nmtv.takeFromTheContainer(6);\n"
                + "mtv.printAnalysis();\n"
                + "it prints line \"";

        addToTheContainer(v, 9, k);
        takeFromTheContainer(v, 6, k);

        printAnalysis(v, k);
        String[] rivit = io.getOutput().split("\n");

        String haettava = "History:";
        String rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("[1.0, 10.0, 4.0]"));

        haettava = "Product:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("kahvi"));

        haettava = "Greatest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("10"));

        haettava = "Smallest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("1"));

        haettava = "Average:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("5"));

        haettava = "Variance:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"\n"
                + "variance should be 21.0", rivi.contains("21"));

        haettava = "Greatest change:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"\n"
                + "greatest fluctuation should be 9.0", rivi.contains("9"));

    }

    @Test
    @Points("30.9")
    public void extendedPrintAnalysisWorks2() throws Throwable {
        MockInOut io = new MockInOut("");
        Object v = newMustavaProductContainer("kahvi", 10, 2);
        String k = "Does printAnalysis work correctly? With code \n"
                + "mtv = new ProductContainerRecorder(\"kahvi\", 10, 2);\n"
                + "mtv.addToTheContainer(4);\nmtv.takeFromTheContainer(2);\n"
                + "mtv.printAnalysis();\n"
                + "it prints line \"";

        addToTheContainer(v, 4, k);
        takeFromTheContainer(v, 2, k);
        addToTheContainer(v, 6, k);
        takeFromTheContainer(v, 2, k);

        printAnalysis(v, k);
        String[] rivit = io.getOutput().split("\n");

        String haettava = "History:";
        String rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("[2.0, 6.0, 4.0, 10.0, 8.0]"));

        haettava = "Product:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("kahvi"));

        haettava = "Greatest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("10"));

        haettava = "Smallest product amount:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("2"));

        haettava = "Average:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"", rivi.contains("6"));

        haettava = "Variance:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"\n"
                + "variance should be 10.0", rivi.contains("10"));

        haettava = "Greatest change:";
        rivi = hae(rivit, haettava);
        assertTrue(k + rivi + "\"\n"
                + "greatest fluctuation should be 6.0", rivi.contains("6"));
    }

    /*
     *
     */
    private Object newMustavaProductContainer(String name, double capacity, double initialVolume) throws Throwable {
        assertTrue("Create for class ProductContainerRecorder constructor ProductContainerRecorder(String name, double capacity, double initialVolume)", classRef.constructor().taking(String.class, double.class, double.class).isPublic());

        String v = "\nError caused by code\n new ProductContainerRecorder(\"" + name + "\"," + capacity + "," + initialVolume + ");";

        Reflex.MethodRef3<Object, Object, String, Double, Double> ctor = classRef.constructor().taking(String.class, double.class, double.class).withNiceError();
        return ctor.withNiceError(v).invoke(name, capacity, initialVolume);

    }

    private String history(Object o, String v) throws Throwable {
        return classRef.method(o, "history").returning(String.class).takingNoParams().withNiceError(v).invoke();
    }

    private double takeFromTheContainer(Object o, double amount, String v) throws Throwable {
        return classRef.method(o, "takeFromTheContainer").returning(double.class)
                .taking(double.class).withNiceError(v).invoke(amount);
    }

    private void printAnalysis(Object o, String v) throws Throwable {
        classRef.method(o, "printAnalysis").returningVoid()
                .takingNoParams().withNiceError(v).invoke();
    }

    private void addToTheContainer(Object o, double amount, String v) throws Throwable {
        classRef.method(o, "addToTheContainer").returningVoid()
                .taking(double.class).withNiceError(v).invoke(amount);
    }

    private String hae(String[] rivit, String haettava) {
        for (String rivi : rivit) {
            if (rivi.contains(haettava)) {
                return rivi;
            }
        }

        return null;
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
