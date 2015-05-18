import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.*;
import static org.junit.Assert.*;

public class B_PurchaseTest {

    String klassName = "Purchase";
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
    @Points("12.4")
    public void classIsPublic() {
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    @Points("12.4")
    public void hasConstructor() throws Throwable {
        Reflex.MethodRef3<Object, Object, String, Integer, Integer> ctor = klass.constructor().taking(String.class, int.class, int.class).withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "(String product, int amount, int unitPrice)", ctor.isPublic());
        String v = "error caused by code new Purchase(\"milk\",2,4);";
        ctor.withNiceError(v).invoke("milk", 2, 4);
    }

    public Object luo(String product, int price, int lkm) throws Throwable {
        Reflex.MethodRef3<Object, Object, String, Integer, Integer> ctor = klass.constructor().taking(String.class, int.class, int.class).withNiceError();
        return ctor.invoke(product, price, lkm);
    }

    @Test
    @Points("12.4")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 3, "instance variables for product name, product price and for the amount of the product");
    }

    @Test
    @Points("12.4")
    public void methodPrice() throws Throwable {
        String metodi = "price";

        Object olio = luo("milk", 2, 3);

        assertTrue("Create method public int " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "Purchase purchase = new Purchase(\"milk\",2,3);\n"
                + "purchase.price();";

        assertEquals(v, 6, (int)klass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError("error caused by code \n"+v).invoke());
    }

    @Test
    @Points("12.4")
    public void priceIsCalculatedRight() throws Throwable {
        String k = "o = new Purchase(\"bread\", 1, 5); o.price()";

        Object purchase = newPurchase("bread", 1, 5);
        int price = price(purchase);
        assertEquals(k, 5, price);
    }

    /*
     *
     */

    @Test
    @Points("12.4")
    public void hasMethodIncreaseAmount() throws Throwable {
        String metodi = "increaseAmount";

        Object olio = luo("milk", 2, 3);

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "Purchase purchase = new Purchase(\"milk\",1,2);\n"
                + "purchase.increaseAmount();";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError("error caused by code \n"+v).invoke();

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v+"\npurchase.price();").invoke();
    }

    @Test
    @Points("12.4")
    public void amountIncreases() throws Throwable {
        String k = "o = new Purchase(\"bread\", 1, 5); o.increaseAmount(), o.price()";

        Object purchase = newPurchase("bread", 1, 5);
        increaseAmount(purchase);
        int price = price(purchase);
        assertEquals(k, 10, price);
    }

    @Test
    @Points("12.4")
    public void toStringDone() {
        Object purchase = newPurchase("bread", 1, 5);
        assertFalse("Create method public String toString() for class Purchase", purchase.toString().contains("@"));
    }

    @Test
    @Points("12.4")
    public void toStringCorrect() throws Throwable {
        String k = "o = new Purchase(\"milk\", 2, 4); System.out.println( o )";

        Object purchase = newPurchase("milk", 2, 4);
        assertEquals("Check that toString() returns a string of format \"product: amount\""
                + "\n" + k, "milk: 2", purchase.toString());
    }

    @Test
    @Points("12.4")
    public void toStringCorrect2() throws Throwable {
        String k = "o = new Purchase(\"cheese\", 17, 3); System.out.println( o )";

        Object purchase = newPurchase("cheese", 17, 3);
        assertEquals("Check that toString() returns a string of format \"product: amount\""
                + "\n" + k, "cheese: 17", purchase.toString());
    }

    /*
     *
     */
    private Object newPurchase(String product, int amount, int price) {
        try {
            c = ReflectionUtils.findClass(klassName);

            Constructor[] cc = c.getConstructors();
            return cc[0].newInstance(product, amount, price);
        } catch (Throwable t) {
            fail("Create for class " + klassName + " a constructor Purchase(String product, int amount, int unitPrice)\n"
                    + "this is the only constructor you need!");
        }
        return null;
    }

    private int price(Object olio) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "price");
            return ReflectionUtils.invokeMethod(int.class, metodi, olio);
        } catch (Throwable t) {
            throw t;
        }
    }

    private void increaseAmount(Object olio) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "increaseAmount");
            ReflectionUtils.invokeMethod(void.class, metodi, olio);
        } catch (Throwable t) {
            throw t;
        }
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
