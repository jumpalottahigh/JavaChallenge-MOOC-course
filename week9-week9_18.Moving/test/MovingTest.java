
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class MovingTest<_Thing, _Item, _Laatikko, _Packer> {

    String thingString = "moving.domain.Thing";
    String itemString = "moving.domain.Item";
    String boxString = "moving.domain.Box";
    String packerString = "moving.logic.Packer";
    Reflex.ClassRef<_Item> _ItemRef;
    Reflex.ClassRef<_Laatikko> _LaatikkoRef;
    Reflex.ClassRef<_Thing> _ThingRef;
    Reflex.ClassRef<_Packer> _PackerRef;

    @Test
    @Points("18.1")
    public void hasThingInterface() {
        Class clazz = null;
        try {
            clazz = ReflectionUtils.findClass(thingString);
        } catch (Throwable t) {
            fail("Create package moving.domain and inside it, create interface Thing");
        }

        if (clazz == null) {
            fail("Have you created public interface Thing inside the package moving.domain?");
        }

        if (!clazz.isInterface()) {
            fail("Is the class Thing an interface?");
        }

        boolean loytyi = false;
        for (Method m : clazz.getMethods()) {
            if (!m.getReturnType().equals(int.class)) {
                continue;
            }

            if (!m.getName().equals("getVolume")) {
                continue;
            }

            loytyi = true;
        }

        if (!loytyi) {
            fail("Does the interface Thing have method int getVolume()?");
        }
    }

    @Test
    @Points("18.1")
    public void classItemPublic() {
        String klassName = "moving.domain.Item";
        _ItemRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", _ItemRef.isPublic());
    }

    @Test
    @Points("18.1")
    public void noRedundantVariables() {
        String klassName = "moving.domain.Item";
        _ItemRef = Reflex.reflect(klassName);

        saniteettitarkastus(klassName, 2, "instance variables for name and volume");
    }

    @Test
    @Points("18.1")
    public void testItemConstructor() throws Throwable {
        String klassName = "moving.domain.Item";

        _ItemRef = Reflex.reflect(klassName);

        Reflex.MethodRef2<_Item, _Item, String, Integer> ctor = _ItemRef.constructor().taking(String.class, int.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "(String name, int volume)", ctor.isPublic());
        String v = "error caused by code new Item(\"Olutkori\", 10);";
        ctor.withNiceError(v).invoke("Olutkori", 10);
    }

    public _Item newItem(String n, int ti) throws Throwable {
        String klassName = "moving.domain.Item";
        _ItemRef = Reflex.reflect(klassName);
        Reflex.MethodRef2<_Item, _Item, String, Integer> ctor = _ItemRef.constructor().taking(String.class, int.class).withNiceError();
        return ctor.invoke(n, ti);
    }

    @Test
    @Points("18.1")
    public void classItemImplementsInterfaceThing() {
        Class clazz = ReflectionUtils.findClass(itemString);


        boolean toteuttaaRajapinnan = false;
        Class kali = ReflectionUtils.findClass(thingString);
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class Item implement interface Thing?");
        }
    }

    @Test
    @Points("18.1")
    public void itemGetVolume() throws Throwable {
        String klassName = "moving.domain.Item";
        _ItemRef = Reflex.reflect(klassName);

        _Item item = newItem("Olutkori", 10);

        String v = "Item item = new Item(\"Olutkori\", 10); \n"
                + "item.getVolume();\n";

        assertTrue("Class " + s(klassName) + " should have method public int getVolume()",
                _ItemRef.method(item, "getVolume").returning(int.class).takingNoParams().
                withNiceError("Error caused by code: \n" + v).isPublic());

        assertEquals(v, 10, (int) _ItemRef.method(item, "getVolume").returning(int.class).takingNoParams().invoke());

        _Item item2 = newItem("Computer", 3);

        String v2 = "Item item = new Item(\"Computer\", 3); \n"
                + "item.getVolume();\n";

        assertEquals(v2, 3, (int) _ItemRef.method(item2, "getVolume").returning(int.class).takingNoParams().invoke());
    }

    @Test
    @Points("18.1")
    public void itemGetName() throws Throwable {
        String klassName = "moving.domain.Item";
        _ItemRef = Reflex.reflect(klassName);

        _Item item = newItem("Olutkori", 10);

        String v = "Item item = new Item(\"Olutkori\", 10); \n"
                + "item.getName();\n";

        assertTrue("Class " + s(klassName) + " should have method public String getName()",
                _ItemRef.method(item, "getName").returning(String.class).takingNoParams().
                withNiceError("Error caused by code: \n" + v).isPublic());

        assertEquals(v, "Olutkori", _ItemRef.method(item, "getName").returning(String.class).takingNoParams().invoke());

        _Item item2 = newItem("Computer", 3);

        String v2 = "Item item = new Item(\"Computer\", 3); \n"
                + "item.getName();\n";

        assertEquals(v2, "Computer", _ItemRef.method(item2, "getName").returning(String.class).takingNoParams().invoke());
    }

    @Test
    @Points("18.1")
    public void itemToString() throws Throwable {
        String klassName = "moving.domain.Item";
        _ItemRef = Reflex.reflect(klassName);

        _Item item = newItem("Olutkori", 10);
        assertFalse("Create method toString for class Item as specified in the assignment", item.toString().contains("@"));

        String v = "Item item = new Item(\"Olutkori\", 10); \n"
                + "System.out.println(item)\n";

        assertEquals(v, "Olutkori (10 dm^3)", item.toString());

        _Item item2 = newItem("Computer", 3);

        String v2 = "Item item = new Item(\"Computer\", 3); \n"
                + "System.out.println(item)\n";

        assertEquals(v2, "Computer (3 dm^3)", item2.toString());
    }

    /*
     *
     */
    @Test
    @Points("18.2")
    public void itemComparable() {

        Class clazz = ReflectionUtils.findClass(itemString);

        boolean toteuttaaRajapinnan = false;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(Comparable.class)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class Item implement interface Comparable<Item>?");
        }


        Method compareToMeto = null;
        try {
            compareToMeto = ReflectionUtils.requireMethod(clazz, "compareTo", clazz);
        } catch (Throwable t) {
        }

        if (compareToMeto == null) {
            try {
                compareToMeto = ReflectionUtils.requireMethod(clazz, "compareTo", ReflectionUtils.findClass(thingString));
            } catch (Throwable t) {
            }
        }
    }

    @Test
    @Points("18.2")
    public void itemCompareTo() throws Throwable {
        String klassName = "moving.domain.Item";
        _ItemRef = Reflex.reflect(klassName);

        _Item item = newItem("Olutkori", 10);
        _Item item2 = newItem("Computer", 3);

        String v = "Item item1 = new Item(\"Olutkori\", 10); \n"
                + "Item item2 = new Item(\"Computer\", 3);\n"
                + "item1.compareTo(item2);\n";

        assertTrue("Class " + klassName + " should have method public int compareTo(Item comparable)",
                _ItemRef.method(item, "compareTo").returning(int.class).taking(_ItemRef.cls()).
                withNiceError("Error caused by code: \n" + v).isPublic());

        int x = (int) _ItemRef.method(item, "compareTo").returning(int.class).taking(_ItemRef.cls()).invoke(item2);

        assertTrue("Result should have been a positive integer with code\n" + v + " you returned " + x, x > 0);

        v = "Item item1 = new Item(\"Olutkori\", 10); \n"
                + "Item item2 = new Item(\"Computer\", 3);\n"
                + "item2.compareTo(item1);\n";

        x = (int) _ItemRef.method(item2, "compareTo").returning(int.class).taking(_ItemRef.cls()).invoke(item);

        assertTrue("Result should have been a negative integer with code\n" + v + " "
                + "you returned " + x, x < 0);
    }

    @Test
    @Points("18.2")
    public void itemCompareToWorks() {

        Class clazz = ReflectionUtils.findClass(itemString);

        boolean toteuttaaRajapinnan = false;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(Comparable.class)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class Item implement interface Comparable?");
        }


        Method compareToMeto = null;
        try {
            compareToMeto = ReflectionUtils.requireMethod(clazz, "compareTo", clazz);
        } catch (Throwable t) {
        }

        if (compareToMeto == null) {
            try {
                compareToMeto = ReflectionUtils.requireMethod(clazz, "compareTo", ReflectionUtils.findClass(thingString));
            } catch (Throwable t) {
            }
        }


        Object item = luoItem("stone", 5);
        Integer ret = null;
        try {
            ret = ReflectionUtils.invokeMethod(int.class, compareToMeto, item, luoItem("record", 2));
        } catch (Throwable ex) {
            fail("Does calling method compareTo of class Item work?");
        }

        if (ret <= 0) {
            fail("Does method compareTo of class Item sort items into a descending order?\n"
                    + "try codea\n"
                    + "Item item1 = new Item(\"stone\", 5); \n"
                    + "Item item2 = new Item(\"record\", 2); \n"
                    + "item1.compareTo(item2)");
        }


        try {
            ret = ReflectionUtils.invokeMethod(int.class, compareToMeto, item, luoItem("record", 7));
        } catch (Throwable ex) {
            fail("Does calling method compareTo of class Item work?");
        }

        if (ret >= 0) {
            fail("Does method compareTo of class Item sort items into a descending order?\n"
                    + "try codea\n"
                    + "Item item1 = new Item(\"stone\", 5); \n"
                    + "Item item2 = new Item(\"record\", 7); \n"
                    + "item1.compareTo(item2)");
        }

        try {
            ret = ReflectionUtils.invokeMethod(int.class, compareToMeto, item, luoItem("record", 5));
        } catch (Throwable ex) {
            fail("Does calling method compareTo of class Item work?");
        }

        if (ret != 0) {
            fail("Does method compareTo of class Item sort items into a descending order so that items that have equal volume are in the same place?\n"
                    + "try codea\n"
                    + "Item item1 = new Item(\"stone\", 5); \n"
                    + "Item item2 = new Item(\"record\", 5); \n"
                    + "item1.compareTo(item2)");
        }
    }

    /*
     *
     */
    @Test
    @Points("18.3")
    public void classBoxPublic() {
        String klassName = "moving.domain.Box";
        _LaatikkoRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + s(klassName) + " {...\n}", _LaatikkoRef.isPublic());
    }

    @Test
    @Points("18.3")
    public void noRedundantVariablesInBox() {
        String klassName = "moving.domain.Box";
        _LaatikkoRef = Reflex.reflect(klassName);

        saniteettitarkastus(klassName, 2, "instance variables for maximum capacity and for list of things");
    }

    @Test
    @Points("18.3")
    public void testBoxConstructor() throws Throwable {
        String klassName = "moving.domain.Box";
        _LaatikkoRef = Reflex.reflect(klassName);

        Reflex.MethodRef1<_Laatikko, _Laatikko, Integer> ctor = _LaatikkoRef.constructor().taking(int.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "(int maximumCapacity)", ctor.isPublic());
        String v = "error caused by code new Box(1000);";
        ctor.withNiceError(v).invoke(1000);
    }

    public _Laatikko newBox(int ti) throws Throwable {
        String klassName = "moving.domain.Box";
        _LaatikkoRef = Reflex.reflect(klassName);
        Reflex.MethodRef1<_Laatikko, _Laatikko, Integer> ctor = _LaatikkoRef.constructor().taking(int.class).withNiceError();
        return ctor.invoke(ti);
    }

    @Test
    @Points("18.3")
    public void boxMethodAdd() throws Throwable {
        String klassName = "moving.domain.Box";

        _Thing item = (_Thing) newItem("Olutkori", 10);

        _LaatikkoRef = Reflex.reflect(klassName);

        _Laatikko box = newBox(1000);

        _ThingRef = Reflex.reflect("moving.domain.Thing");

        String v = "Box box = new Box(1000);\n"
                + "Item item = new Item(\"Olutkori\", 10);\n"
                + "box.addThing( item );\n";

        assertTrue("Class " + s(klassName) + " should have method "
                + "public boolean addThing(Thing t)",
                _LaatikkoRef.method(box, "addThing").returning(boolean.class).taking(_ThingRef.cls()).
                withNiceError("Error caused by code: \n" + v).isPublic());

        assertEquals(v, true, _LaatikkoRef.method(box, "addThing").
                returning(boolean.class).
                taking(_ThingRef.cls()).
                withNiceError("Error caused by code: \n" + v).
                invoke(item));

        box = newBox(1000);
        item = (_Thing) newItem("stone", 1001);

        v = "Box box = new Box(1000);\n"
                + "Item item = new Item(\"Stone\", 1001); \n"
                + "box.addThing( item );\n";
        assertEquals(v, false, _LaatikkoRef.method(box, "addThing").returning(boolean.class).taking(_ThingRef.cls()).invoke(item));

    }

    @Test
    @Points("18.3")
    public void boxIsThing() {
        Class boxClass = ReflectionUtils.findClass(boxString);

        if (boxClass == null) {
            fail("Have you created public class Box inside the package moving.domain?");
        }

        boolean toteuttaaRajapinnan = false;
        Class thingIface = ReflectionUtils.findClass(thingString);
        for (Class iface : boxClass.getInterfaces()) {
            if (iface.equals(thingIface)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class Box implement interface Thing?");
        }
    }

    @Test
    @Points("18.3")
    public void boxGetVolume() throws Throwable {
        String klassName = "moving.domain.Box";

        _Thing item = (_Thing) newItem("Olutkori", 10);

        _LaatikkoRef = Reflex.reflect(klassName);

        _Laatikko box = newBox(1000);

        String v = "Box box = new Box(1000);"
                + "box.getVolume(  );\n";

        assertTrue("Class " + klassName + " should have method public int getVolume()",
                _LaatikkoRef.method(box, "getVolume").returning(int.class).takingNoParams().
                withNiceError("Error caused by code: \n" + v).isPublic());

        assertEquals(v, 0, (int) _LaatikkoRef.method(box, "getVolume").returning(int.class).takingNoParams().invoke());

        _Thing item2 = (_Thing) newItem("Computer", 3);

        _ThingRef = Reflex.reflect("moving.domain.Thing");

        _LaatikkoRef.method(box, "addThing").returning(boolean.class).taking(_ThingRef.cls()).invoke(item);
        _LaatikkoRef.method(box, "addThing").returning(boolean.class).taking(_ThingRef.cls()).invoke(item2);

        v = "Box box = new Box(1000);\n"
                + "box.addThing( new Item(\"Olutkori\", 10); )\n"
                + "box.addThing( new Item(\"Stone\", 5); )\n"
                + "box.getVolume(  );\n";

        assertEquals(v, 13, (int) _LaatikkoRef.method(box, "getVolume").returning(int.class).takingNoParams().invoke());
    }

    @Test
    @Points("18.3")
    public void hasClassBox() {
        Class boxClass = ReflectionUtils.findClass(boxString);

        if (boxClass == null) {
            fail("Have you created public class Box inside the package moving.domain?");
        }

        boolean toteuttaaRajapinnan = false;
        Class thingIface = ReflectionUtils.findClass(thingString);
        for (Class iface : boxClass.getInterfaces()) {
            if (iface.equals(thingIface)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class Box implement interface Thing?");
        }

        try {
            ReflectionUtils.requireConstructor(boxClass, int.class);
        } catch (Throwable t) {
            fail("Does the class Box have constructor public Box(int maximumCapacity)?");
        }

        try {

            boolean onMetodi = false;
            for (Method m : boxClass.getMethods()) {
                if (!m.getReturnType().equals(boolean.class)) {
                    continue;
                }

                if (!m.getName().equals("addThing")) {
                    continue;
                }

                if (!m.getParameterTypes()[0].equals(thingIface)) {
                    continue;
                }

                onMetodi = true;

            }

            if (!onMetodi) {
                fail("fail");
            }
        } catch (Throwable t) {
            fail("Does the class Box have method public boolean addThing(Thing thing)?");
        }

        Field collectionField = null;
        for (Field f : boxClass.getDeclaredFields()) {
            if (!f.getType().equals(List.class) && !f.getType().equals(ArrayList.class)) {
                continue;
            }

            f.setAccessible(true);
            collectionField = f;
            break;
        }

        if (collectionField == null) {
            fail("Box should add things into a list.");
        }

        Object box = luoBox(10);
        Object stone = luoItem("stone", 5);
        lisaaBoxon(box, stone);
        List data = null;
        try {
            data = (List) collectionField.get(box);
        } catch (Throwable t) {
            fail("Box should add things into a list.");
        }

        boolean stoneLoytyi = false;
        for (Object obj : data) {
            if (obj == stone) {
                stoneLoytyi = true;
                break;
            }
        }

        if (!stoneLoytyi) {
            fail("Check that things are added into Box's internal list.");
        }

        if (!lisaaBoxon(luoBox(10), luoItem("stone", 10))) {
            fail("Can items be added all the way to maximum capacity? For example, if box's max capacity is 10, then an item of volume 10 should fit in there.");
        }

        if (lisaaBoxon(luoBox(10), luoItem("stone", 20))) {
            fail("Check that you cannot add too big items into the box. For example, if box's max capacity is 10, you can not add an item of volume 20.");
        }


        Object box2 = luoBox(10);
        if (!lisaaBoxon(box2, luoItem("stone", 5))) {
            fail("It should be possible to add items into an empty box.");
        }

        if (lisaaBoxon(box2, luoItem("stone", 8))) {
            fail("Check that you can't add items into a box so that the total volume of items exceeds max capacity.");
        }

        if (!lisaaBoxon(box2, luoItem("stone", 2))) {
            fail("You can add more items into a box even if it already has an item, as long as max capacity isn't exceeded.");
        }

        if (!lisaaBoxon(box2, luoItem("stone", 3))) {
            fail("You can add more items into a box even if it already has an item, as long as max capacity isn't exceeded.");
        }

        if (lisaaBoxon(box2, luoItem("stone", 3))) {
            fail("Check that you can't add items to the box if it's already full.");
        }
    }

    /*
     *
     */
    @Test
    @Points("18.4")
    public void classPackerPublic() {
        String klassName = "moving.logic.Packer";
        _PackerRef = Reflex.reflect(klassName);

        assertTrue("Class " + klassName + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", _PackerRef.isPublic());
    }

    @Test
    @Points("18.4")
    public void testPackerConstructor() throws Throwable {
        String klassName = "moving.logic.Packer";
        _PackerRef = Reflex.reflect(klassName);

        Reflex.MethodRef1<_Packer, _Packer, Integer> ctor = _PackerRef.constructor().taking(int.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "(int boxesVolume)", ctor.isPublic());
        String v = "error caused by code new Packer(1000);";
        Object o = ctor.withNiceError(v).invoke(1000);
        o.toString();
    }

    public _Packer newPacker(int ti) throws Throwable {
        String klassName = "moving.logic.Packer";
        _PackerRef = Reflex.reflect(klassName);
        Reflex.MethodRef1<_Packer, _Packer, Integer> ctor = _PackerRef.constructor().taking(int.class).withNiceError();
        return ctor.invoke(ti);
    }

    @Test
    @Points("18.4")
    public void packerMethodPackThings() throws Throwable {
        String klassName = "moving.logic.Packer";

        _Thing item1 = (_Thing) newItem("Olutkori", 10);
        _Thing item2 = (_Thing) newItem("Stone", 5);
        _Thing item3 = (_Thing) newItem("Taulu", 20);

        List lista = new ArrayList<_Thing>();
        lista.add(item1);
        lista.add(item2);
        lista.add(item3);

        _LaatikkoRef = Reflex.reflect(klassName);

        _Packer pakaaja = newPacker(1000);

        String v = "Packer packer = new Packer(1000);\n"
                + "List<Thing> things = new ArrayList<Thing>;\n"
                + "things.add( new Item(\"Olutkori\", 10) );\n"
                + "things.add( new Item(\"Stone\", 5) );\n"
                + "things.add( new Item(\"Taulu\", 20) );\n"
                + "packer.packThings( things );\n";

        assertTrue("Class " + s(klassName) + " should have method "
                + "public List<Box> packThings(List<Thing> things)",
                _PackerRef.method(pakaaja, "packThings").returning(List.class).taking(List.class).
                withNiceError("Error caused by code: \n" + v).isPublic());

        _PackerRef.method(pakaaja, "packThings").returning(List.class).taking(List.class).withNiceError("Error caused by code: \n" + v).invoke(lista);
    }

    @Test
    @Points("18.4")
    public void packerWorks() throws Throwable {
        Class boxClass = ReflectionUtils.findClass(boxString);
        Class packerClass = ReflectionUtils.findClass(packerString);

        if (packerClass == null) {
            fail("Have you created public class Packer inside the package moving.logic?");
        }

        Object packer = null;
        try {
            Constructor packerConst = ReflectionUtils.requireConstructor(packerClass, int.class);
            packer = ReflectionUtils.invokeConstructor(packerConst, 20);

            if (packer == null) {
                fail("fail");
            }
        } catch (Throwable t) {
            fail("Does the class Packer have constructor public Packer(int boxesVolume)?");
        }

        Method pakkaaMetodi = null;
        try {
            for (Method m : packerClass.getMethods()) {
                if (!m.getReturnType().equals(ArrayList.class) && !m.getReturnType().equals(List.class)) {
                    continue;
                }

                if (!m.getName().equals("packThings")) {
                    continue;
                }

                if (!m.getParameterTypes()[0].equals(ArrayList.class) && !m.getParameterTypes()[0].equals(List.class)) {
                    continue;
                }

                pakkaaMetodi = m;
            }

            if (pakkaaMetodi == null) {
                fail("fail");
            }
        } catch (Throwable t) {
            fail("Does the class Packer have method public List<Box> packThings(List<Thing> things)?");
        }

        List itemet = new ArrayList();

        String itemMj = "List<Thing> things = new ArrayList<Thing>();\n";

        for (int i = 0; i < 5; i++) {
            int til = (int) (1 + (Math.random() * 10));
            Object e = luoItem("stone", til);
            itemMj += " things.add( new Item(\"Stone\", " + til + ") );\n";
            itemet.add(e);
        }

        String e = itemet.toString();

        List itemetkopio = new ArrayList(itemet);

        List laatikot = null;
        try {
            laatikot = (List) pakkaaMetodi.invoke(packer, itemet);
        } catch (Throwable t) {
            fail("Does the packing work? Test code\n"
                    + itemMj
                    + "Packer packer = new Packer(20);\n"
                    + "packer.packThings(things);");
        }

        String koodi = ""
                + itemMj
                + "Packer packer = new Packer(20);\n"
                + "packer.packThings(things);";

        Field collectionField = null;
        for (Field f : boxClass.getDeclaredFields()) {
            if (!f.getType().equals(List.class) && !f.getType().equals(ArrayList.class)) {
                continue;
            }

            f.setAccessible(true);
            collectionField = f;
            break;
        }

        if (collectionField == null) {
            fail("Box should add things into a list. Error with code\n"
                    + koodi);
        }

        assertFalse("Packer's method packThings cannot return null", laatikot == null);

        for (Object box : laatikot) {
            List data = null;
            try {
                data = (List) collectionField.get(box);
            } catch (Throwable t) {
                fail("Box should add things into a list. Check code\n"
                        + koodi);
            }

            for (Object item : data) {
                itemetkopio.remove(item);
            }

            oikeanKokoinen(box, 20);
        }

        if (!itemetkopio.isEmpty()) {
            fail("Are you sure that all items were packed into boxes? Check code\n"
                    + koodi);
        }
    }

    private boolean lisaaBoxon(Object box, Object item) {
        Method addThingMeto = ReflectionUtils.requireMethod(ReflectionUtils.findClass(boxString), "addThing", ReflectionUtils.findClass(thingString));

        try {
            return ReflectionUtils.invokeMethod(boolean.class, addThingMeto, box, item);
        } catch (Throwable ex) {
            fail("Adding item into a box failed: " + item);
        }
        return false;
    }

    private Object luoItem(String name, int volume) {
        Class clazz = ReflectionUtils.findClass(itemString);
        Object item = null;
        try {
            item = ReflectionUtils.invokeConstructor(ReflectionUtils.requireConstructor(clazz, String.class, int.class), name, volume);
        } catch (Throwable ex) {
            fail("Creating an item failed when its name is " + name + " and volume is " + volume);
        }

        return item;
    }

    private Object luoBox(int maximumCapacity) {
        Class clazz = ReflectionUtils.findClass(boxString);
        Object box = null;
        try {
            box = ReflectionUtils.invokeConstructor(ReflectionUtils.requireConstructor(clazz, int.class), maximumCapacity);
        } catch (Throwable ex) {
            fail("Creating box failed when maximumCapacity is " + maximumCapacity);
        }

        return box;
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

    private void oikeanKokoinen(Object box, int koko) throws Throwable {
        _Laatikko lodju = (_Laatikko) box;
        int alussa = getVolume(lodju);
        for (int i = 1; i <= koko - alussa; i++) {
            Object kilo = newItem("stone", 1);
            lisaaBoxon(lodju, kilo);
            assertTrue("When creating new Packer(20); packer must use "
                    + "boxes of volume 20\nYou used boxes of volume "
                    + getVolume(lodju),
                    getVolume(lodju) == alussa + i);

        }
        int was = getVolume(lodju);
        while (true) {
            Object kilo = newItem("stone", 1);
            lisaaBoxon(lodju, kilo);
            int now = getVolume(lodju);
            if (now == was) {
                break;
            }
            was = now;
        }
        assertTrue("When creating new Packer(20); packer must use "
                + "boxes of volume 20\nYou used boxes of volume "
                + getVolume(lodju),
                getVolume(lodju) == koko);
    }

    private int getVolume(_Laatikko box) throws Throwable {
        String klassName = "moving.domain.Box";
        _LaatikkoRef = Reflex.reflect(klassName);
        String v = "Box box = new Box(1000);\n"
                + "box.addThing( new Item(\"stone\",1));\n"
                + "box.getVolume();\n";

        return _LaatikkoRef.method(box, "getVolume").returning(int.class).takingNoParams().
                withNiceError("Error caused by code: \n" + v).invoke();

    }
}
