
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.*;
import static org.junit.Assert.*;

public class A_ProductContainerTest {

    String klassName = "containers.ProductContainer";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setup() {
        classRef = Reflex.reflect(klassName);
    }

    @Test
    @Points("30.1")
    public void classExists() {
        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("30.1")
    public void extendsClassContainer() {
        Class c = ReflectionUtils.findClass(klassName);

        Class sc = c.getSuperclass();
        assertTrue("Class ProductContainer should extend class Container", sc.getName().equals("containers.Container"));
    }

    @Test
    @Points("30.1")
    public void hasTwoParameterConstructor() throws Throwable {
        newProductContainer("vilja", 10);
    }

    @Test
    @Points("30.1")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "instance variables for product name because ProductContainer inherits Container's volume and capacity");
    }

    /*
     *
     */
    @Test
    @Points("30.1")
    public void methodGetName() throws Throwable {
        String metodi = "getName";
        String virhe = "Create method public String getName() for class ProductContainer";

        Object o = newProductContainer("olut", 10);

        assertTrue(virhe,
                classRef.method(o, metodi).returning(String.class).takingNoParams().isPublic());

        String v = "ProductContainer t = new ProductContainer(\"olut\",\"10\");\n"
                + "t.getName();\n";

        assertEquals(v, "olut", classRef.method(o, metodi)
                .returning(String.class)
                .takingNoParams().withNiceError("error caused by code\n" + v).invoke());

        o = newProductContainer("mehu", 7);

        assertTrue(virhe,
                classRef.method(o, metodi).returning(String.class).takingNoParams().isPublic());

        v = "ProductContainer t = new ProductContainer(\"mehu\",\"7\");\n"
                + "t.getName();\n";

        assertEquals(v, "mehu", classRef.method(o, metodi)
                .returning(String.class)
                .takingNoParams().withNiceError("error caused by code\n" + v).invoke());

    }

    //**********

    @Test
    @Points("30.2")
    public void methodSetName() throws Throwable {
        String metodi = "setName";
        String virhe = "Create method public void setName(String name) for class ProductContainer";

        Object o = newProductContainer("olut", 10);

        assertTrue(virhe,
                classRef.method(o, metodi).returningVoid().taking(String.class).isPublic());

        String v = "ProductContainer t = new ProductContainer(\"olut\",\"10\");\n"
                + "t.setName(\"bier\");\n";

        classRef.method(o, metodi).returningVoid().taking(String.class).withNiceError(v).invoke("bier");

        v = "ProductContainer t = new ProductContainer(\"olut\",\"10\");\n"
                + "t.setName(\"bier\");\n"
                + "t.getName()";

        assertEquals(v, "bier", classRef.method(o, "getName")
                .returning(String.class)
                .takingNoParams().withNiceError("error caused by code\n" + v).invoke());


        Method m = null;
        try {
            Class c = ReflectionUtils.findClass(klassName);
            m = ReflectionUtils.requireMethod(c, void.class, metodi, String.class);
        } catch (Throwable t) {
            fail(virhe);
        }
        assertTrue(virhe, m.toString().contains("public"));
        assertFalse(virhe, m.toString().contains("static"));
    }

    @Test
    @Points("30.2")
    public void productContainerToString() throws Throwable {
        Object tv = newProductContainer("chocolate", 10.0);
        String mj = tv.toString();
        assertTrue("Overwrite method toString in class ProductContainer as specified in the assignment", mj.contains("chocolate"));
    }

    @Test
    @Points("30.2")
    public void productContainerToStringOK1() throws Throwable {
        Object tv = newProductContainer("chocolate", 10.0);
        ((containers.Container) tv).addToTheContainer(5);
        String mj = tv.toString();
        String v = "Does the ProductContainer's method toString work as specified in the assignment? ";
        String k = "try \ntv = new ProductContainer(\"chocolate\",10); \n"
                + "tv.addToTheContainer(5); \n"
                + "System.out.print(tv);\n";
        assertEquals(v+"\n"+k+"\n","chocolate: volume = 5.0, free space 5.0", mj);
    }

    @Test
    @Points("30.2")
    public void productContainerToStringOK2() throws Throwable {
        Object tv = newProductContainer("mustard", 15.0);
        ((containers.Container) tv).addToTheContainer(10);
        String mj = tv.toString();
        String v = "Does the ProductContainer's method toString work as specified in the assignment? ";
        String k = "try \ntv = new ProductContainer(\"mustard\",15); tv.addToTheContainer(10);\n"
                + "System.out.print(tv);\n";
        assertEquals(v+"\n"+k+"\n","mustard: volume = 10.0, free space 5.0", mj);
    }

    /*
     *
     */
    private Object newProductContainer(String name, double capacity) throws Throwable {
        assertTrue("Define for class ProductContainer a constructor public ProductContainer(String name, double capacity)", classRef.constructor().taking(String.class, double.class).isPublic());

        Reflex.MethodRef2<Object, Object, String, Double> ctor = classRef.constructor().taking(String.class, double.class).withNiceError();
        return ctor.invoke(name, capacity);
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
