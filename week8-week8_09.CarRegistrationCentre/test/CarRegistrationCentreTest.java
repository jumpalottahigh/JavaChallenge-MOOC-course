
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CarRegistrationCentreTest {

    String klassName = "VehicleRegister";
    Reflex.ClassRef<Object> klass;

    @Test
    @Points("9.1")
    public void registerPlateNoRedundantVariables() {
        saniteettitarkastus("RegistrationPlate", 2, "instance variables for country and register code");
    }

    @Points("9.1")
    @Test
    public void registerPlateEquals() {
        testaaEquals("FI", "ABC-123", "FI", "ABC-123", true);
        testaaEquals("FI", "ABC-123", "FI", "ABC-122", false);
        testaaEquals("FI", "ABC-123", "F", "ABC-123", false);
        testaaEquals("D", "B IFK-333", "D", "B IFK-333", true);
        testaaEquals("D", "B IFK-33", "D", "B IFK-333", false);
        testaaEquals("D", "B IFK-33", "G", "B IFK-333", false);
    }

    @Points("9.1")
    @Test
    public void registerPlateHashCode() {
        testaaHash("FI", "ABC-123", "FI", "ABC-123");
        testaaHash("D", "B IFK-333", "D", "B IFK-333");
        testaaHash("FI", "TUX-100", "FI", "TUX-100");
        testaaHash("FI", "UKK-999", "FI", "UKK-999");

        RegistrationPlate r1 = new RegistrationPlate("FI", "AAA-111");
        RegistrationPlate r2 = new RegistrationPlate("B", "ZZ-22 A");
        RegistrationPlate r3 = new RegistrationPlate("QQ", "joopajoo");
        assertFalse("method hashCode seems to return the same value for all register plates: " + r1.hashCode(),
                r1.hashCode() == r2.hashCode() && r2.hashCode() == r3.hashCode());
    }

    @Points("9.2")
    @Test
    public void classIsPublic() {
        klass = Reflex.reflect(klassName);
        assertTrue("Class " + klassName + " should be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    @Points("9.2")
    public void noRedundantVariables() {
        saniteettitarkastus(klassName, 1, "an instance variable of type HashMap<RegistrationPlate, String> for storing car info");
    }

    @Test
    @Points("9.2")
    public void hasHashMap() {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();
        assertTrue("Add to class " + klassName + " an instance variable of type HashMap<RegistrationPlate, String>", kentat.length == 1);
        assertTrue("Class " + klassName + " should have an instance variable of type Map<RegistrationPlate, String>", kentat[0].toString().contains("Map"));
    }

    @Test
    @Points("9.2")
    public void emptyConstructor() throws Throwable {
        klass = Reflex.reflect(klassName);
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + klassName + " a public constructor: public " + klassName + "()", ctor.isPublic());
        String v = "error caused by code new VehicleRegister();";
        ctor.withNiceError(v).invoke();
    }

    @Test
    @Points("9.2")
    public void addMethod() throws Throwable {
        String metodi = "add";

        Object olio = luo();

        assertTrue("Create method public boolean " + metodi + "(RegistrationPlate plate, String owner) for class " + klassName,
                klass.method(olio, metodi)
                .returning(boolean.class).taking(RegistrationPlate.class, String.class).isPublic());

        String v = "\nError caused by code\n VehicleRegister ar = new VehicleRegister(); "
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");";

        assertEquals("VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");",
                true, klass.method(olio, metodi)
                .returning(boolean.class).taking(RegistrationPlate.class, String.class).
                withNiceError(v).invoke(new RegistrationPlate("FI", "AAA-111"), "Arto"));

        assertEquals("Adding a plate which is already in the register should return false\n"
                + "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Pekka\");",
                false, klass.method(olio, metodi)
                .returning(boolean.class).taking(RegistrationPlate.class, String.class).invoke(new RegistrationPlate("FI", "AAA-111"), "Arto"));
    }

    @Test
    @Points("9.2")
    public void getMethod() throws Throwable {
        String metodi = "get";

        Object olio = luo();

        assertTrue("Create method public String " + metodi + "(RegistrationPlate plate) for class " + klassName,
                klass.method(olio, metodi)
                .returning(String.class).taking(RegistrationPlate.class).isPublic());

        String v = "\nError caused by code"
                + "\n VehicleRegister ar = new VehicleRegister(); "
                + "ar.get( new VehicleRegister(\"FI\", \"AAA-111\"));";

        assertEquals("Getting a plate which doesn't exist in the register should return null\n"
                + "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.get( new VehicleRegister(\"FI\", \"AAA-111\"));",
                null, klass.method(olio, metodi)
                .returning(String.class).
                taking(RegistrationPlate.class).withNiceError(v).
                invoke(new RegistrationPlate("FI", "AAA-111")));

        add(olio, "FI", "AAA-111", "Arto");

        assertEquals("Getting a plate which is already in the register should return its owner\n"
                + "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.get( new VehicleRegister(\"FI\", \"AAA-111\"));",
                "Arto", klass.method(olio, metodi)
                .returning(String.class).taking(RegistrationPlate.class).invoke(new RegistrationPlate("FI", "AAA-111")));

        add(olio, "FI", "XX-999", "Arto");
        assertEquals("Getting a plate which is already in the register should return its owner\n"
                + "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"XX-999\"), \"Arto\");\n"
                + "ar.get( new VehicleRegister(\"FI\", \"AAA-111\"));",
                "Arto", klass.method(olio, metodi)
                .returning(String.class).taking(RegistrationPlate.class).invoke(new RegistrationPlate("FI", "AAA-111")));

        assertEquals("Getting a plate which is already in the register should return its owner\n"
                + "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"XX-999\"), \"Arto\");\n"
                + "ar.get( new VehicleRegister(\"FI\", \"XX-999\"));",
                "Arto", klass.method(olio, metodi)
                .returning(String.class).taking(RegistrationPlate.class).invoke(new RegistrationPlate("FI", "XX-999")));
    }

    @Test
    @Points("9.2")
    public void deleteMethod() throws Throwable {
        String metodi = "delete";

        Object olio = luo();

        assertTrue("Create method public boolean " + metodi + "(RegistrationPlate plate) for class " + klassName,
                klass.method(olio, metodi)
                .returning(boolean.class).taking(RegistrationPlate.class).isPublic());

        String v = "\nError caused by code\n VehicleRegister ar = new VehicleRegister(); "
                + "ar.delete( new VehicleRegister(\"FI\", \"AAA-111\"));";

        assertEquals("If the plate doesn't exist in the register, delete-method should return false\n"
                + "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.delete( new VehicleRegister(\"FI\", \"AAA-111\"));",
                false, klass.method(olio, metodi)
                .returning(boolean.class).taking(RegistrationPlate.class).withNiceError(v).invoke(new RegistrationPlate("FI", "AAA-111")));

        add(olio, "FI", "AAA-111", "Arto");

        assertEquals("If the plate is in the register and it is deleted, delete-method should return true\n"
                + "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.delete( new VehicleRegister(\"FI\", \"AAA-111\"));",
                true, klass.method(olio, metodi)
                .returning(boolean.class).taking(RegistrationPlate.class).invoke(new RegistrationPlate("FI", "AAA-111")));
    }

    @Test
    @Points("9.2")
    public void addGetDelete() throws Throwable {
        Object olio = luo();

        add(olio, "FI", "AAA-111", "Arto");
        add(olio, "FI", "BBB-222", "Pekka");

        String o = get(olio, "FI", "AAA-111");

        String v = "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"BBB-222\"), \"Pekka\");\n";

        assertEquals(v + "ar.get(new VehicleRegister(\"FI\", \"AAA-111\"));", "Arto", o);

        o = get(olio, "FI", "BBB-222");

        assertEquals(v + "ar.get(new VehicleRegister(\"FI\", \"BBB-222\"));\n", "Pekka", o);

        delete(olio, "FI", "AAA-111");
        o = get(olio, "FI", "AAA-111");

        assertEquals(v + "ar.delete(new VehicleRegister(\"FI\", \"AAA-111\"));\n"
                + "ar.get(new VehicleRegister(\"FI\", \"AAA-111\"));\n", null, o);
    }

    @Test
    @Points("9.3")
    public void printRegistrationPlatesMethod() throws Throwable {
        String metodi = "printRegistrationPlates";
        MockInOut io = new MockInOut("");
        Object olio = luo();

        add(olio, "FI", "AAA-111", "Arto");
        add(olio, "FI", "BBB-222", "Pekka");
        add(olio, "FI", "CCC-333", "Jukka");

        String v = "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"BBB-222\"), \"Pekka\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"CCC-333\"), \"Jukka\");\n"
                + "ar.printRegistrationPlates()";

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();

        String out = io.getOutput();
        assertTrue("There should have been three printed lines with code:\n"+v+"\nyou printed\n"+out,out.split("\n").length>2);

        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, out.contains("AAA-111"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, out.contains("BBB-222"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, out.contains("CCC-333"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("Arto"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("Pekka"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("Jukka"));
    }

    @Test
    @Points("9.3")
    public void printRegistrationPlatesMethod2() throws Throwable {
        String metodi = "printRegistrationPlates";
        MockInOut io = new MockInOut("");
        Object olio = luo();

        add(olio, "FI", "AAA-111", "Arto");
        add(olio, "FI", "BBB-222", "Pekka");
        add(olio, "FI", "CCC-333", "Arto");

        String v = "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"BBB-222\"), \"Pekka\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"CCC-333\"), \"Arto\");\n"
                + "ar.printRegistrationPlates()";

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();

        String out = io.getOutput();

        assertTrue("There should have been three printed lines with code:\n"+v+"\nyou printed\n"+out,out.split("\n").length>2);

        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, out.contains("AAA-111"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, out.contains("BBB-222"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, out.contains("CCC-333"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("Arto"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("Pekka"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("Arto"));
    }

    @Test
    @Points("9.3")
    public void printOwners() throws Throwable {
        String metodi = "printOwners";
        MockInOut io = new MockInOut("");
        Object olio = luo();

        add(olio, "FI", "AAA-111", "Arto");
        add(olio, "FI", "BBB-222", "Pekka");
        add(olio, "FI", "CCC-333", "Arto");

        String v = "VehicleRegister ar = new VehicleRegister(); \n"
                + "ar.add( new RegistrationPlate(\"FI\", \"AAA-111\"), \"Arto\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"BBB-222\"), \"Pekka\");\n"
                + "ar.add( new RegistrationPlate(\"FI\", \"CCC-333\"), \"Arto\");\n"
                + "ar.printOwners()";

        assertTrue("Create method public void " + metodi + "() for class " + klassName,
                klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();

        String out = io.getOutput();

        assertTrue("There should have been two printed lines with code:\n"+v+"\nyou printed\n"+out,out.split("\n").length>1);

        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("AAA-111"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("BBB-222"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, !out.contains("CCC-333"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, out.contains("Arto"));
        assertTrue("Printing is not correct with code: \n"+v+"\nyou printed:\n"+out, out.contains("Pekka"));
        String jaa = out.substring( out.indexOf("Arto")+1 );
        assertFalse("Same name isn't supposed to be printed twice, printing does not work with code: "
                + "\n"+v+"\nyou printed:\n"+out, jaa.contains("Arto"));
    }

    /*
     *
     */
    public Object luo() throws Throwable {
        klass = Reflex.reflect(klassName);
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    private void testaaEquals(String m1, String r1, String m2, String r2, boolean vast) {
        RegistrationPlate rr1 = new RegistrationPlate(m1, r1);
        RegistrationPlate rr2 = new RegistrationPlate(m2, r2);

        String v = "RegistrationPlate r1 = new RegistrationPlate(\"" + m1 + "\", \"" + r1 + "\");\n"
                + "RegistrationPlate r2 = new RegistrationPlate(\"" + m2 + "\", \"" + r2 + "\");\n"
                + "r1.equals(r2)";
        assertEquals(v, vast, rr1.equals(rr2));
    }

    private void testaaHash(String m1, String r1, String m2, String r2) {
        RegistrationPlate rr1 = new RegistrationPlate(m1, r1);
        RegistrationPlate rr2 = new RegistrationPlate(m2, r2);

        String v = "RegistrationPlate r1 = new RegistrationPlate(\"" + m1 + "\", \"" + r1 + "\");\n"
                + "RegistrationPlate r2 = new RegistrationPlate(\"" + m2 + "\", \"" + r2 + "\");\n"
                + "r2.hashCode() == r2.HashCode()";
        assertEquals(v, true, rr1.hashCode() == rr2.hashCode());
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

    private void add(Object olio, String maa, String rek, String om) throws Throwable {
        klass.method(olio, "add")
                .returning(boolean.class).taking(RegistrationPlate.class, String.class).invoke(new RegistrationPlate(maa, rek), om);
    }

    private void delete(Object olio, String maa, String rek) throws Throwable {
        klass.method(olio, "delete")
                .returning(boolean.class).taking(RegistrationPlate.class).invoke(new RegistrationPlate(maa, rek));
    }

    private String get(Object olio, String f, String r) throws Throwable {
        return klass.method(olio, "get")
                .returning(String.class).
                taking(RegistrationPlate.class).
                invoke(new RegistrationPlate(f, r));
    }
}
