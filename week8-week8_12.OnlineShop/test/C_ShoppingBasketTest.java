
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;
import org.junit.*;
import static org.junit.Assert.*;

public class C_ShoppingBasketTest {

    String klassName = "ShoppingBasket";
    Reflex.ClassRef<Object> klass;
    Class c;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
        try {
            c = ReflectionUtils.findClass(klassName);
        } catch (Throwable e) {
        }
    }

    @Test
    @Points("12.5")
    public void hasClassShoppingBasket() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Points("12.5")
    @Test
    public void hasMapOrList() {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        String k = "Save purchases of class " + klassName + " either to an instance variable Map<String, Purchase> purchases;\n"
                + "or to an instance variable List<Purchase> purchases; ";

        int map = 0;
        for (Field field : kentat) {
            assertFalse(k
                    + "so change " + kentta(field.toString()) + " to the correct type", field.toString().matches(".* java\\.util\\.[A-Z][a-z]+(Map|List) .*"));

            assertFalse(k + " you don't need any other instance variables, remove " + kentta(field.toString()), !field.toString().matches(".* java\\.util\\.([A-Z][a-z]+)?(Map|List) .*"));

            if (field.toString().matches(".* java\\.util\\.([A-Z][a-z]+)?(Map|List) .*")) {
                map++;
            }
        }
        assertTrue(k, map > 0 && map < 3);

    }

    @Test
    @Points("12.5")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "an instance variable for storing Purchase-objects");
    }

    @Test
    @Points("12.5")
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

    @Test
    @Points("12.5")
    public void hasMethodPrice() throws Throwable {
        String metodi = "price";

        Object olio = luo();

        assertTrue("Create method public int " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "ShoppingBasket basket = new ShoppingBasket(); basket.price()";

        klass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError("error caused by code \n" + v).invoke();
    }

    @Test
    @Points("12.5")
    public void emptyShoppingBasketPriceZero() throws Throwable {
        String k = "ShoppingBasket basket = new ShoppingBasket(); basket.price()";
        Object basket = newShoppingBasket();
        int price = price(basket);
        assertEquals(k, 0, price);
    }

    @Test
    @Points("12.5")
    public void hasMethodAdd() throws Throwable {
        String metodi = "add";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "(String product, int price) for class" + klassName,
                klass.method(olio, metodi)
                .returningVoid().taking(String.class, int.class).isPublic());

        String v = "ShoppingBasket basket = new ShoppingBasket(); basket.add(\"milk\",3)";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class, int.class).withNiceError("error caused by code \n" + v).invoke("milk", 3);
    }

    @Test
    @Points("12.5")
    public void addingProductIncreasesThePriceOfTheBasket() throws Throwable {
        String k = "ShoppingBasket basket = new ShoppingBasket(); basket.add(\"milk\",3); basket.price()";

        Object basket = newShoppingBasket();
        add(basket, "milk", 3);
        int price = price(basket);
        assertEquals(k, 3, price);
    }

    @Test
    @Points("12.5")
    public void addingTwoDifferentProductsIncreasesThePriceOfTheBasket() throws Throwable {
        String k = "ShoppingBasket basket = new ShoppingBasket(); basket.add(\"milk\",3); basket.add(\"butter\",5); basket.price()";

        Object basket = newShoppingBasket();
        add(basket, "milk", 3);
        add(basket, "butter", 5);
        int price = price(basket);
        assertEquals(k, 8, price);
    }

    @Test
    @Points("12.5")
    public void addingThreeDifferentProductsIncreasesThePriceOfTheBasket() throws Throwable {
        String k = "ShoppingBasket basket = new ShoppingBasket(); basket.add(\"milk\",3); basket.add(\"butter\",5);basket.add(\"bread\",4); basket.price()";

        Object basket = newShoppingBasket();
        add(basket, "milk", 3);
        add(basket, "butter", 5);
        add(basket, "bread", 4);
        int price = price(basket);
        assertEquals(k, 12, price);
    }

    /*
     *
     */
    @Test
    @Points("12.6")
    public void hasMethodPrint() throws Throwable {
        String metodi = "print";

        Object olio = luo();

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "ShoppingBasket basket = new ShoppingBasket(); basket.print()";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError("error caused by code \n" + v).invoke();

    }

    @Test
    @Points("12.6")
    public void printingWorks() throws Throwable {
        MockInOut io = new MockInOut("");

        String k = "ShoppingBasket basket = new ShoppingBasket(); \n"
                + "basket.add(\"milk\",3); \n"
                + "basket.add(\"butter\",5);\n"
                + "basket.add(\"bread\",4); \n"
                + "basket.print()\n";

        Object basket = newShoppingBasket();
        add(basket, "milk", 3);
        add(basket, "butter", 5);
        add(basket, "bread", 4);
        print(basket);

        String[] t = io.getOutput().split("\n");
        assertEquals("Check that ShoppingBasket's method print works as specified in the assignment, \n"
                + "" + k + " amount of printed lines", 3, t.length);
        String etsittava = "milk: 1";
        assertTrue("Check that ShoppingBasket's method print works as specified in the assignment,  \n"
                + k + " line " + etsittava + " should be printed. Output was:\n"+io.getOutput(), sisaltaa(t, etsittava));
        etsittava = "butter: 1";
        assertTrue("Check that ShoppingBasket's method print works as specified in the assignment,  \n"
                + k + " line " + etsittava + " should be printed. Output was:\n"+io.getOutput(), sisaltaa(t, etsittava));
        etsittava = "bread: 1";
        assertTrue("Check that ShoppingBasket's method print works as specified in the assignment,  \n"
                + k + " line " + etsittava + " should be printed. Output was:\n"+io.getOutput(), sisaltaa(t, etsittava));
    }

    @Test
    @Points("12.7")
    public void addingTwoSameProductsIncreasesThePriceOfTheBasket() throws Throwable {
        String k = "ShoppingBasket basket = new ShoppingBasket(); \n"
                + "basket.add(\"milk\",3); \n"
                + "basket.add(\"milk\",3); \n"
                + "basket.price()";

        Object basket = newShoppingBasket();
        add(basket, "milk", 3);
        add(basket, "milk", 3);
        int price = price(basket);
        assertEquals(k, 6, price);
    }

    @Test
    @Points("12.7")
    public void addingTwoSameProductsDoesntPrintTwoPurchases() throws Throwable {
        MockInOut io = new MockInOut("");

        String k = "ShoppingBasket basket = new ShoppingBasket(); \n"
                + "basket.add(\"milk\",3); \n"
                + "basket.add(\"milk\",3); \n"
                + "basket.print()";

        Object basket = newShoppingBasket();
        add(basket, "milk", 3);
        add(basket, "milk", 3);
        print(basket);

        String[] t = io.getOutput().split("\n");
        assertEquals("Check that ShoppingBasket's method print works as specified in the assignment, "
                + "when two same products are added to the basket \n"
                + k + " amount of printed lines", 1, t.length);
        assertTrue("Check that ShoppingBasket's method print works as specified in the assignment, "
                + "when two same products are added to the basket \n"
                + k + "\n"
                + "the only printed line should be milk: 2, but output was \n" + t[0] + "\n", t[0].contains("milk: 2"));
    }

    @Test
    @Points("12.7")
    public void manySameAndDifferentProducts() throws Throwable {
        MockInOut io = new MockInOut("");
        String k = "ShoppingBasket basket = new ShoppingBasket(); \n"
                + "basket.add(\"milk\",3); \n"
                + "basket.add(\"sausage\",7); \n"
                + "basket.add(\"milk\",3); \n"
                + "basket.add(\"milk\",3); \n"
                + "basket.add(\"sausage\",7); \n"
                + "basket.add(\"cream\", 2);\n"
                + "basket.price()";

        Object basket = newShoppingBasket();
        add(basket, "milk", 3);
        add(basket, "sausage", 7);
        add(basket, "milk", 3);
        add(basket, "milk", 3);
        add(basket, "sausage", 7);
        add(basket, "cream", 2);
        int price = price(basket);
        assertEquals(k, 25, price);

        print(basket);

        String[] t = io.getOutput().split("\n");

        assertEquals("Check that ShoppingBasket's method print works as specified in the assignment, "
                + "when many same products are added to the basket \n"
                + k + " amount of printed lines", 3, t.length);
        String etsittava = "milk: 3";
        assertTrue("Check that ShoppingBasket's method print works as specified in the assignment,  \n"
                + k + " line " + etsittava + " should be printed, ", sisaltaa(t, etsittava));
        etsittava = "sausage: 2";
        assertTrue("Check that ShoppingBasket's method print works as specified in the assignment,  \n"
                + k + " line " + etsittava + " should be printed, ", sisaltaa(t, etsittava));
        etsittava = "cream: 1";
        assertTrue("Check that ShoppingBasket's method print works as specified in the assignment,  \n"
                + k + " line " + etsittava + " should be printed, ", sisaltaa(t, etsittava));
    }

    /*
     *
     */
    @Test
    @Points("12.8")
    public void hasClassShop() {
        try {
            ReflectionUtils.findClass("Shop");
        } catch (Throwable e) {
            fail("Create class Shop and copy the code base given in the assignment");
        }
    }

    @Test
    @Points("12.8")
    public void managingWorks() throws Throwable {
        int stock = 0;
        String rivit[] = null;
        Object v = null;

        try {
            MockInOut io = new MockInOut("");
            Scanner sk = new Scanner("coffee\nbread\nwater\n\n");
            v = newStorehouse();

            addToStorehouse(v, "coffee", 5, 10);
            addToStorehouse(v, "milk", 3, 20);
            addToStorehouse(v, "cream", 2, 55);
            addToStorehouse(v, "bread", 7, 8);
            Object kauppa = newShop(v, sk);
            manage(kauppa, "pekka");
            stock = stock(v, "coffee");
            rivit = io.getOutput().split("\n");


        } catch (Throwable t) {
            fail("Shop has been created and customer\'s input is coffee<enter>bread<enter>water<enter><enter>\n this caused an exception " + t + "\n"
                    + "did you copy the code given in the assignment in to the class Shop?");
        }
        assertEquals("Shop has been created and customer\'s input is coffee<enter>bread<enter>water<enter><enter>\n coffee\'s stock should decrease by one", 9, stock);
        stock = stock(v, "bread");
        assertEquals("Shop has been created and customer\'s input is coffee<enter>bread<enter>water<enter><enter>\n bread\'s stock should decrease by one", 7, stock);
        assertTrue("Shop has been created and customer\'s input is coffee<enter>bread<enter>water<enter><enter>\n the price of the shopping basket should be 12, now price was " + rivit[rivit.length - 1], rivit[rivit.length - 1].contains("12"));
    }

    private void manage(Object olio, String nimi) throws Throwable {
        try {
            Class clzz = ReflectionUtils.findClass("Shop");
            Method metodi = ReflectionUtils.requireMethod(clzz, "manage", String.class);
            ReflectionUtils.invokeMethod(void.class, metodi, olio, nimi);
        } catch (Throwable t) {
            throw t;
        }
    }

    private int stock(Object olio, String product) throws Throwable {
        try {
            Class clzz = ReflectionUtils.findClass("Storehouse");
            Method metodi = ReflectionUtils.requireMethod(clzz, "stock", String.class);
            return ReflectionUtils.invokeMethod(int.class, metodi, olio, product);
        } catch (Throwable t) {
            throw t;
        }
    }

    private void addToStorehouse(Object olio, String product, int price, int stock) throws Throwable {
        try {
            Class clzz = ReflectionUtils.findClass("Storehouse");
            Method metodi = ReflectionUtils.requireMethod(clzz, "addProduct", String.class, int.class, int.class);
            List<String> l = null;

            ReflectionUtils.invokeMethod(void.class, metodi, olio, product, price, stock);
        } catch (Throwable t) {
            throw t;
        }
    }

    private Object newStorehouse() throws Throwable {
        String luokanNimi = "Storehouse";
        try {
            Class clzz = ReflectionUtils.findClass(luokanNimi);
            return ReflectionUtils.invokeConstructor(clzz.getConstructor());
        } catch (Throwable t) {
            fail("Create for class " + luokanNimi + " a public constructor which takes no parameters");
        }
        return null;
    }

    private Object newShop(Object varasto, Scanner lukija) throws Throwable {
        String luokanNimi = "Shop";
        try {
            Class clzz = ReflectionUtils.findClass(luokanNimi);
            return clzz.getConstructors()[0].newInstance(varasto, lukija);
            //return ReflectionUtils.invokeConstructor(clzz.getConstructor(), varasto, lukija);
        } catch (Throwable t) {
            fail("Create for class " + luokanNimi + " a public constructor which takes no parameters");
        }
        return null;
    }

    private void add(Object olio, String product, int price) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "add", String.class, int.class);
            ReflectionUtils.invokeMethod(void.class, metodi, olio, product, price);
        } catch (Throwable t) {
            throw t;
        }
    }

    private int price(Object olio) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "price");

            return ReflectionUtils.invokeMethod(int.class, metodi, olio);
        } catch (Throwable t) {
            throw t;
        }
    }

    private void print(Object olio) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "print");

            ReflectionUtils.invokeMethod(void.class, metodi, olio);
        } catch (Throwable t) {
            throw t;
        }
    }

    private Object newShoppingBasket() throws Throwable {
        try {
            c = ReflectionUtils.findClass(klassName);
            return ReflectionUtils.invokeConstructor(c.getConstructor());
        } catch (Throwable t) {
            fail("Create for class " + klassName + " a public constructor which takes no parameters");
        }
        return null;
    }

    private String kentta(String toString) {
        return toString.replace(klassName + ".", "");
    }

    private boolean sisaltaa(String[] t, String mj) {
        for (String rivi : t) {
            if (rivi.contains(mj)) {
                return true;
            }
        }
        return false;
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
