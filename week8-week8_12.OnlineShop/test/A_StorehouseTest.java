
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class A_StorehouseTest {

    String klassName = "Storehouse";
    Class c;
    Reflex.ClassRef<Object> klass;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
        try {
            c = ReflectionUtils.findClass(klassName);
        } catch (Throwable e) {
        }
    }

    @Test
    @Points("12.1")
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    @Points("12.1")
    public void constructor() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "()", ctor.isPublic());
        String v = "error caused by code new Storehouse();";
        ctor.withNiceError(v).invoke();
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    @Points("12.1")
    @Test
    public void hasMap() {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        int map = 0;
        for (Field field : kentat) {
            assertFalse("Save the prices of the products in an instance variable Map<String, Integer> prices; in class " + klassName +" \n"
                    + "so change " + kentta(field.toString()) + " to the correct type ", field.toString().matches(".* java\\.util\\.[A-Z][a-z]+Map .*"));
            assertFalse("You don't need for class " + klassName + " an instance variable of type List! Remove " + kentta(field.toString()), field.toString().matches(".* java\\.util\\.([A-Z][a-z]+)*List .*"));
            if (field.toString().matches(".* java\\.util\\.([A-Z][a-z]+)*Map .*")) {
                map++;
            }
        }
        assertTrue("Save the prices of the products in an instance variable Map<String, Integer> prices; in class " + klassName, map > 0 && map < 3);

    }

    @Test
    @Points("12.1")
    public void hasMethodAddProduct() throws Throwable {
        String metodi = "addProduct";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(String product, int price, int stock) for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(String.class, int.class, int.class).isPublic());

        String v = "\nError caused by code Storehouse v = new Storehouse(); "
                + "v.addProduct(\"coffee\",2, 25);";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class, int.class, int.class).withNiceError(v).invoke("coffee", 2, 25);
    }

    private void addProduct(Object olio, String product, int price, int lkm) throws Throwable {
        klass.method(olio, "addProduct")
                .returningVoid().taking(String.class, int.class, int.class).invoke(product, price, lkm);
    }

    @Test
    @Points("12.1")
    public void hasMethodPrice() throws Throwable {
        String metodi = "price";
        Object olio = luo();

        assertTrue("Create method public int " + metodi + "(String product) for class " + klassName,
                klass.method(olio, metodi)
                .returning(int.class).taking(String.class).isPublic());

        String v = "\nError caused by code Storehouse v = new Storehouse(); \n"
                + "v.addProduct(\"coffee\",2, 25);\n"
                + "v.addProduct(\"milk\",3, 10);\n"
                + "v.price(\"coffee\");";

        addProduct(olio, "coffee", 2, 25);
        addProduct(olio, "milk", 3, 10);

        klass.method(olio, metodi)
                .returning(int.class).taking(String.class).withNiceError(v).invoke("coffee");

    }

    @Points("12.1")
    @Test
    public void successfulPriceQuery() throws Throwable {
        String koodi = "v = new Storehouse(); \n"
                + "v.addProduct(\"milk\", 3, 10); \n"
                + "v.addProduct(\"coffee\", 5, 7);\n"
                + "v.price(\"milk\"); ";

        Object v = newStorehouse();
        lisaa(v, "milk", 3, 10);
        lisaa(v, "coffee", 5, 7);

        int t = price(v, "milk");
        assertEquals(koodi, 3, t);

        koodi += "v.price(\"coffee\"); ";
        t = price(v, "coffee");
        assertEquals(koodi, 5, t);
    }

    @Points("12.1")
    @Test
    public void noExceptionsFailingPriceQuery() throws Throwable {
        String metodi = "price";
        Object olio = luo();

        assertTrue("Create method public int " + metodi + "(String product) for class " + klassName,
                klass.method(olio, metodi)
                .returning(int.class).taking(String.class).isPublic());

        String v = "\nRemember to handle the situation where user asks a price for the product which doesn't exist in the storehouse!"
                + "\nError caused by code Storehouse v = new Storehouse(); \n"
                + "v.addProduct(\"coffee\",2, 25);\n"
                + "v.addProduct(\"milk\",3, 10);\n"
                + "v.price(\"cheese\");";

        addProduct(olio, "coffee", 2, 25);
        addProduct(olio, "milk", 3, 10);

        klass.method(olio, metodi)
                .returning(int.class).taking(String.class).withNiceError(v).invoke("cheese");
    }

    @Points("12.1")
    @Test
    public void failingPriceQuery() throws Throwable {
        String koodi = "v = new Storehouse(); \n"
                + "v.addProduct(\"milk\", 3, 10); \n"
                + "v.addProduct(\"coffee\", 5, 7); \n"
                + "v.price(\"bread\"); ";

        Object v = newStorehouse();
        lisaa(v, "milk", 3, 10);
        lisaa(v, "coffee", 5, 7);

        int t = price(v, "bread");
        assertEquals("if product doesn't exist in the storehouse, price should be returned as -99, " + koodi, -99, t);
    }

    /*
     *
     */
    @Points("12.2")
    @Test
    public void hasMaps() {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        int map = 0;
        for (Field field : kentat) {
            assertFalse("Save stocks of the products in an instance variable Map<String, Integer> stocks; in class " + klassName + " \n"
                    + "so change " + kentta(field.toString()) + " to the correct type ", field.toString().contains("HashMap"));
            assertTrue("Instance variable " + kentta(field.toString()) + " should be private", field.toString().startsWith("private"));
            assertFalse("You don't need for class " + klassName + " an instance variable of type List! Remove " + kentta(field.toString()), field.toString().matches(".* java\\.util\\.([A-Z][a-z]+)?List .*"));
            if (field.toString().contains("Map")) {
                map++;
            }
        }
        assertTrue("Save stocks of the products in an instance variable Map<String, Integer> stocks; in class " + klassName + " \n"
                + "So you need two Maps for your class, no less, no more!", map > 1 && map < 3);

    }

    @Test
    @Points("12.2")
    public void hasMethodSaldo() throws Throwable {
        String metodi = "stock";
        Object olio = luo();

        assertTrue("Create method public int " + metodi + "(String product) for class " + klassName,
                klass.method(olio, metodi)
                .returning(int.class).taking(String.class).isPublic());

        String v = "\nError caused by code Storehouse v = new Storehouse(); \n"
                + "v.addProduct(\"coffee\",2, 25);\n"
                + "v.addProduct(\"milk\",3, 10);\n"
                + "v.stock(\"coffee\");";

        addProduct(olio, "coffee", 2, 25);
        addProduct(olio, "milk", 3, 10);

        klass.method(olio, metodi)
                .returning(int.class).taking(String.class).withNiceError(v).invoke("coffee");
    }

    @Points("12.2")
    @Test
    public void successfulStockQuery() throws Throwable {
        String koodi = "v = new Storehouse(); v.addProduct(\"milk\", 3, 10); v.addProduct(\"coffee\", 5, 7); "
                + "v.stock(\"milk\"); ";

        Object v = newStorehouse();
        lisaa(v, "milk", 3, 10);
        lisaa(v, "coffee", 5, 7);

        int t = stock(v, "milk");
        assertEquals(koodi, 10, t);

        koodi += "v.stock(\"coffee\"); ";
        t = stock(v, "coffee");
        assertEquals(koodi, 7, t);
    }

    @Test
    @Points("12.2")
    public void nonexistentStock() throws Throwable {
        String metodi = "stock";
        Object olio = luo();

        assertTrue("Create method public int " + metodi + "(String product) for class " + klassName,
                klass.method(olio, metodi)
                .returning(int.class).taking(String.class).isPublic());

        String v = "Remember to handle the situation where user asks stock for a product which doesn't exist in the storehouse!\n"
                + "Storehouse v = new Storehouse(); \n"
                + "v.addProduct(\"coffee\",2, 25);\n"
                + "v.addProduct(\"milk\",3, 10);\n"
                + "v.stock(\"cheese\");";

        addProduct(olio, "coffee", 2, 25);
        addProduct(olio, "milk", 3, 10);

        assertEquals(v, 0, (int) klass.method(olio, metodi)
                .returning(int.class).taking(String.class).withNiceError("Error caused by code \n" + v).invoke("cheese"));
    }

    @Test
    @Points("12.2")
    public void hasMethodTake() throws Throwable {
        String metodi = "take";
        Object olio = luo();

        assertTrue("Create method public boolean " + metodi + "(String product) for class " + klassName,
                klass.method(olio, metodi)
                .returning(boolean.class).taking(String.class).isPublic());

        String v = "\nError caused by code Storehouse v = new Storehouse(); \n"
                + "v.addProduct(\"coffee\",2, 25);\n"
                + "v.addProduct(\"milk\",3, 10);\n"
                + "v.take(\"coffee\");";

        addProduct(olio, "coffee", 2, 25);
        addProduct(olio, "milk", 3, 10);

        klass.method(olio, metodi)
                .returning(boolean.class).taking(String.class).withNiceError(v).invoke("coffee");

        v = "\nError caused by code Storehouse v = new Storehouse(); \n"
                + "v.addProduct(\"coffee\",2, 25);\n"
                + "v.addProduct(\"milk\",3, 10);\n"
                + "v.take(\"cheese\");";

        klass.method(olio, metodi)
                .returning(boolean.class).taking(String.class).withNiceError(v).invoke("cheese");
    }

    @Points("12.2")
    @Test
    public void takeDecreases() throws Throwable {
        String koodi = "v = new Storehouse(); \n"
                + "v.addProduct(\"milk\", 3, 10); \n"
                + "v.addProduct(\"coffee\", 5, 7); \n"
                + "v.take(\"coffee\");\n";

        Object v = newStorehouse();
        lisaa(v, "milk", 3, 10);
        lisaa(v, "coffee", 5, 7);

        boolean b = ota(v, "coffee");

        assertEquals(koodi, true, b);

        koodi += "v.stock(\"coffee\"); ";

        int t = stock(v, "coffee");
        assertEquals(koodi, 6, t);
    }

    @Points("12.2")
    @Test
    public void takeDoesNothingWhenStockIsZero() throws Throwable {
        String koodi = "v = new Storehouse(); \n"
                + "v.addProduct(\"milk\", 3, 10); \n"
                + "v.addProduct(\"coffee\", 5, 1); \n"
                + "v.take(\"coffee\");\n"
                + "v.take(\"coffee\");\n";

        Object v = newStorehouse();
        lisaa(v, "milk", 3, 10);
        lisaa(v, "coffee", 5, 1);

        ota(v, "coffee");
        boolean b = ota(v, "coffee");

        assertEquals(koodi, false, b);

        koodi += "v.stock(\"coffee\"); ";

        int t = stock(v, "coffee");
        assertEquals(koodi, 0, t);
    }

    @Points("12.2")
    @Test
    public void takeReturnsFalseForNonExistentProduct() throws Throwable {
        String koodi = "v = new Storehouse(); \n"
                + "v.addProduct(\"milk\", 3, 10); \n"
                + "v.addProduct(\"coffee\", 5, 7);\n"
                + "v.take(\"bread\");";

        Object v = newStorehouse();
        lisaa(v, "milk", 3, 10);
        lisaa(v, "coffee", 5, 7);

        boolean b = ota(v, "bread");
        assertFalse(koodi, b);
    }

    /*
     *
     */
    @Test
    @Points("12.3")
    public void hasMethodProducts() throws Throwable {
        String metodi = "products";
        Object olio = luo();

        assertTrue("Create method public Set<String> " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returning(Set.class).takingNoParams().isPublic());

        String v = "\nError caused by code Storehouse v = new Storehouse(); \n"
                + "v.addProduct(\"coffee\",2, 25);\n"
                + "v.addProduct(\"milk\",3, 10);\n"
                + "v.products();";

        addProduct(olio, "coffee", 2, 25);
        addProduct(olio, "milk", 3, 10);

        klass.method(olio, metodi)
                .returning(Set.class).takingNoParams().withNiceError(v).invoke();
    }

    @Points("12.3")
    @Test
    public void productsWorks() throws Throwable {
        String koodi = "v = new Storehouse(); \n"
                + "v.addProduct(\"milk\", 3, 10); \n"
                + "v.addProduct(\"coffee\", 5, 7); \n"
                + "v.addProduct(\"sugar\", 2, 25);\n"
                + "v.products();";

        Object v = newStorehouse();
        lisaa(v, "milk", 3, 10);
        lisaa(v, "coffee", 5, 7);
        lisaa(v, "sugar", 2, 25);
        Set<String> t = products(v);

        assertFalse(koodi + " returned a set which was null", t == null);

        assertEquals(koodi + " returned set +" + t + " size of the set ", 3, t.size());
        assertEquals(koodi + " returned set +" + t + " \"milk\" is included ", true, t.contains("milk"));
        assertEquals(koodi + " returned set +" + t + " \"coffee\" is included ", true, t.contains("coffee"));
        assertEquals(koodi + " returned set +" + t + " \"sugar\" is included ", true, t.contains("sugar"));
    }

    @Test
    @Points("12.1 12.2 12.3")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 2, "instance variables for saving stocks and the prices of the products");
    }

    /*
     *
     */
    private int price(Object olio, String product) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "price", String.class);
            return ReflectionUtils.invokeMethod(int.class, metodi, olio, product);
        } catch (Throwable t) {
            throw t;
        }
    }

    private int stock(Object olio, String product) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "stock", String.class);
            return ReflectionUtils.invokeMethod(int.class, metodi, olio, product);
        } catch (Throwable t) {
            throw t;
        }
    }

    private boolean ota(Object olio, String product) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "take", String.class);
            return ReflectionUtils.invokeMethod(boolean.class, metodi, olio, product);
        } catch (Throwable t) {
            throw t;
        }
    }

    private Set<String> products(Object olio) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "products");
            Set<String> vast = (Set<String>) metodi.invoke(olio);
            return vast;
        } catch (Throwable t) {
            throw t;
        }
    }

    private void lisaa(Object olio, String product, int price, int stock) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "addProduct", String.class, int.class, int.class);
            List<String> l = null;

            ReflectionUtils.invokeMethod(void.class, metodi, olio, product, price, stock);
        } catch (Throwable t) {
            throw t;
        }
    }

    private Object newStorehouse() throws Throwable {
        try {
            c = ReflectionUtils.findClass(klassName);
            return ReflectionUtils.invokeConstructor(c.getConstructor());
        } catch (Throwable t) {
            fail("Create for class " + klassName + " a public constructor which takes no parameters");
        }
        return null;
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

    private String kentta(String toString) {
        return toString.replace(klassName + ".", "");
    }
}
