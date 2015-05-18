
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.ClassRef;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef0;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef1;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import farmsimulator.Alive;
import farmsimulator.Milkable;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class FarmSimulatorTest<S, L, R, N, M> {

    private ClassRef<S> bulkTankClassRef;
    private MethodRef0<S, Double> bulkTankGetTilavuusMethodRef;
    private MethodRef0<S, Double> bulkTankGetSaldoMethodRef;
    private MethodRef0<S, Double> bulkTankPaljonkoTilaaJaljellaMethodRef;
    private MethodRef1<S, Void, Double> bulkTankLisaaSailioonMethodRef;
    private MethodRef1<S, Double, Double> bulkTankOtaSailiostaMethodRef;
    private ClassRef<L> cowClassRef;
    private MethodRef0<L, String> cowGetNimiMethodRef;
    private MethodRef0<L, Double> cowGetTilavuusMethodRef;
    private MethodRef0<L, Double> cowGetMaaraMethodRef;
    private MethodRef0<L, Double> cowLypsaMethodRef;
    private MethodRef0<L, Void> cowEleleTuntiMethodRef;
    private ClassRef<R> lypsyrobottiClassRef;
    private MethodRef1<R, Void, Milkable> lypsyrobottiLypsaMethodRef;
    private MethodRef1<R, Void, S> lypsyrobottiSetBulkTankMethodRef;
    private MethodRef0<R, S> lypsyrobottiGetBulkTankMethodRef;
    private ClassRef<N> cowhouseClassRef;
    private MethodRef0<N, S> cowhouseGetBulkTankMethodRef;
    private MethodRef1<N, Void, R> cowhouseAsennaMilkingRobotMethodRef;
    private MethodRef1<N, Void, L> cowhouseHoidaCowMethodRef;
    private MethodRef1<N, Void, Collection> cowhouseHoidaCowtMethodRef;
    private ClassRef<M> farmClassRef;
    private MethodRef0<M, String> farmGetOmistajaMethodRef;
    private MethodRef1<M, Void, R> farmAsennaCowHouseanMilkingRobotMethodRef;
    private MethodRef1<M, Void, L> farmLisaaCowMethodRef;
    private MethodRef0<M, Void> farmHoidaCowtMethodRef;
    private MethodRef0<M, Void> farmEleleTuntiMethodRef;

    public static void testaaOnkoKokonaisLuku(Class<?> luokka,
            String metodinNimi, double luku, String paluuarvo) {
        if (!Double.valueOf(luku).equals(Math.ceil(luku))) {
            Assert.fail("Class " + luokka.getName() + "'s method "
                    + metodinNimi + " returns a value " + luku
                    + " which needs to be rounded with class Math's ceil() method. "
                    + "Method's returned value was: " + paluuarvo);
        }
    }

    @Test
    @Points("31.1")
    public void testBulkTank() throws Throwable {
        reflektoiBulkTank();

        S tank = luoBulkTank(bulkTankClassRef, null);

        String v = ""
                + "BulkTank m = new BulkTank();";

        testaaTilavuus(tank, 2000.0, v);
        testaaSaldo(tank, 0.0, v);
        testaaPaljonkoTilaaJaljella(tank, v);

        v = ""
                + "BulkTank m = new BulkTank();\n"
                + "m.addToTank(20.234);";

        testaaLisaaSailioon(tank, 20.234, v);

        v = ""
                + "BulkTank m = new BulkTank();\n"
                + "m.addToTank(20.234);\n"
                + "m.addToTank(2032.0);";

        testaaLisaaSailioon(tank, 2032.0, v);

        v = ""
                + "BulkTank m = new BulkTank();\n"
                + "m.addToTank(20.234);\n"
                + "m.addToTank(2032.0);\n"
                + "m.addToTank(5000.0);";

        testaaLisaaSailioon(tank, 5000.0, v);

        tank = luoBulkTank(bulkTankClassRef, 5782.4);

        v = ""
                + "BulkTank m = new BulkTank(5782.4);";

        testaaTilavuus(tank, 5782.4, v);
        testaaSaldo(tank, 0.0, v);
        testaaPaljonkoTilaaJaljella(tank, v);

        v = ""
                + "BulkTank m = new BulkTank(5782.4);\n"
                + "m.addToTank(3232.13);";

        testaaLisaaSailioon(tank, 3232.13, v);

        v = ""
                + "BulkTank m = new BulkTank(5782.4);\n"
                + "m.addToTank(3232.13);\n"
                + "m.addToTank(50000.99);\n";

        testaaLisaaSailioon(tank, 50000.99, v);

        v = ""
                + "BulkTank m = new BulkTank(5782.4);\n"
                + "m.addToTank(3232.13);\n"
                + "m.addToTank(50000.99);\n"
                + "m.addToTank(5000.0);";

        testaaLisaaSailioon(tank, 5000.0, v);

        v = ""
                + "BulkTank m = new BulkTank(5782.4);\n"
                + "m.addToTank(3232.13);\n"
                + "m.addToTank(50000.99);\n"
                + "m.addToTank(5000.0);\n"
                + "m.getFromTank(1.5);";

        testaaOtaSailiosta(tank, 1.5, v);

        v = ""
                + "BulkTank m = new BulkTank(5782.4);\n"
                + "m.addToTank(3232.13);\n"
                + "m.addToTank(50000.99);\n"
                + "m.addToTank(5000.0);\n"
                + "m.getFromTank(1.5);\n"
                + "m.getFromTank(1432.1232);";

        testaaOtaSailiosta(tank, 1432.1232, v);

        v = ""
                + "BulkTank m = new BulkTank(5782.4);\n"
                + "m.addToTank(3232.13);\n"
                + "m.addToTank(50000.99);\n"
                + "m.addToTank(5000.0);\n"
                + "m.getFromTank(1.5);\n"
                + "m.getFromTank(1432.1232);\n"
                + "m.getFromTank(50000.0)";

        testaaOtaSailiosta(tank, 50000.0, v);

        v = ""
                + "BulkTank m = new BulkTank(5782.4);\n"
                + "m.addToTank(3232.13);\n"
                + "m.addToTank(50000.99);\n"
                + "m.addToTank(5000.0);\n"
                + "m.getFromTank(1.5);\n"
                + "m.getFromTank(1432.1232);\n"
                + "m.getFromTank(50000.0)\n"
                + "m.getFromTank(50000.0)";

        testaaOtaSailiosta(tank, 50000.0, v);
    }

    @Test
    @Points("31.2")
    public void testCow() throws Throwable {
        reflektoiCow();

        L cow = luoCow(cowClassRef, null);

        String v = "Cow cow = new Cow();";

        testaaCownElelemista(cow, v);
        testaaCownLypsamista(cow, v);

        testaaCownElelemista(cow, v);
        testaaCownElelemista(cow, v);

        testaaCownLypsamista(cow, v);
        testaaCownLypsamista(cow, v);

        for (int i = 0; i < 300; i++) {
            testaaCownElelemista(cow, v);
        }

        testaaCownLypsamista(cow, v);
    }

    @Test
    @Points("31.3")
    public void testMilkingRobot() throws Throwable {
        reflektoiBulkTank();
        reflektoiCow();
        reflektoiMilkingRobot();

        R robo = luoMilkingRobot(lypsyrobottiClassRef);
        L cow = luoCow(cowClassRef, null);

        testaaCownElelemista(cow);

        S tank = luoBulkTank(bulkTankClassRef, 100.0);

        String v = "MilkingRobot r = new MilkingRobot();\n"
                + "BulkTank m = new BulkTank(100.0);\n"
                + "r.setBulkTank(m);";

        lypsyrobottiSetBulkTankMethodRef.withNiceError(v).invokeOn(robo, tank);

        testaaCownElelemista(cow);
        testaaCownElelemista(cow);

        testaaLypsyrobotinLypsamista(robo, cow);
    }

    @Test
    @Points("31.3")
    public void testMilkingRobotWithoutTank() throws Throwable {
        reflektoiBulkTank();
        reflektoiCow();
        reflektoiMilkingRobot();

        R robo = luoMilkingRobot(lypsyrobottiClassRef);
        L cow = luoCow(cowClassRef, null);

        testaaLypsyrobotinLypsamistaIlmanSailota(robo, cow);
    }

    /*
     *
     */
    @Test
    @Points("31.4")
    public void testCowHouse() throws Throwable {
        reflektoiBulkTank();
        reflektoiCow();
        reflektoiMilkingRobot();
        reflektoiCowHouse();

        L cow1 = luoCow(cowClassRef, "Lehmä1");
        L cow2 = luoCow(cowClassRef, "Lehmä2");
        L cow3 = luoCow(cowClassRef, "Lehmä3");
        L cow4 = luoCow(cowClassRef, "Lehmä4");

        testaaCownElelemista(cow1);
        testaaCownElelemista(cow1);
        testaaCownElelemista(cow1);
        testaaCownElelemista(cow1);
        testaaCownElelemista(cow1);
        testaaCownElelemista(cow2);
        testaaCownElelemista(cow2);
        testaaCownElelemista(cow2);
        testaaCownElelemista(cow2);
        testaaCownElelemista(cow3);
        testaaCownElelemista(cow3);

        List<L> cows = new ArrayList<L>();
        cows.add(cow1);
        cows.add(cow2);
        cows.add(cow3);
        cows.add(cow4);

        S tank = luoBulkTank(bulkTankClassRef, 8374.0);

        N cowhouse = luoCowHouse(cowhouseClassRef, tank);

        R robo = luoMilkingRobot(lypsyrobottiClassRef);

        try {
            cowhouseAsennaMilkingRobotMethodRef.invokeOn(cowhouse, robo);
        } catch (Throwable t) {
            Assert.fail("Class " + cowhouse.getClass().getName() + "'s method "
                    + cowhouseAsennaMilkingRobotMethodRef.getMethod().getName()
                    + " threw an exception: " + t.toString());
        }

        testaaNavetanLehmienHoitamista(cowhouse, cows);
    }

    @Test
    @Points("31.5")
    public void testFarm() throws Throwable {
        reflektoiBulkTank();
        reflektoiCow();
        reflektoiMilkingRobot();
        reflektoiCowHouse();
        reflektoiFarm();

        L cow1 = luoCow(cowClassRef, "Lehmä1");
        L cow2 = luoCow(cowClassRef, null);
        L cow3 = luoCow(cowClassRef, "Lehmä3");
        L cow4 = luoCow(cowClassRef, null);
        L cow5 = luoCow(cowClassRef, "Lehmä5");

        List<L> cows = new ArrayList<L>();
        cows.add(cow1);
        cows.add(cow2);
        cows.add(cow3);
        cows.add(cow4);
        cows.add(cow5);

        S tank = luoBulkTank(bulkTankClassRef, 121384.0);
        N cowhouse = luoCowHouse(cowhouseClassRef, tank);
        M farm = luoFarm(farmClassRef, "Öyvind", cowhouse);

        String v = "\nBulkTank tank = new BulkTank();\n"
                + "Farm tila = new Farm(\"pekka\", "
                + "new CowHouse( tank ));\n";

        for (L cow : cows) {
            v += "tila.addCow( new Cow());\n";
            farmLisaaCowMethodRef.withNiceError(v).invokeOn(farm, cow);
        }

        v += "tila.installMilkingRobot( new MilkingRobot() );\n";

        R robo = luoMilkingRobot(lypsyrobottiClassRef);

        farmAsennaCowHouseanMilkingRobotMethodRef.withNiceError(v).invokeOn(farm, robo);

        testaaFarmnLehmienHoitamista(farm, tank, cows, 1);

        testaaFarmnLehmienHoitamista(farm, tank, cows, 5);

        testaaFarmnLehmienHoitamista(farm, tank, cows, 300);
    }

    private void reflektoiBulkTank() {
        String luokanNimi = "farmsimulator.BulkTank";

        bulkTankClassRef = (ClassRef<S>) Reflex.reflect(luokanNimi);

        saniteettitarkastus("farmsimulator.BulkTank", 2, "instance variables for capacity and for the amount of milk");

        bulkTankGetTilavuusMethodRef = bulkTankClassRef.method("getCapacity").
                returning(double.class).takingNoParams();
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + bulkTankGetTilavuusMethodRef.signature(),
                bulkTankGetTilavuusMethodRef.isPublic());

        bulkTankGetSaldoMethodRef = bulkTankClassRef.method("getVolume").
                returning(double.class).takingNoParams();
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + bulkTankGetSaldoMethodRef.signature(),
                bulkTankGetSaldoMethodRef.isPublic());

        bulkTankPaljonkoTilaaJaljellaMethodRef = bulkTankClassRef.method("howMuchFreeSpace").
                returning(double.class).takingNoParams();
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + bulkTankPaljonkoTilaaJaljellaMethodRef.signature(),
                bulkTankPaljonkoTilaaJaljellaMethodRef.isPublic());

        bulkTankLisaaSailioonMethodRef = bulkTankClassRef.method("addToTank").
                returningVoid().taking(double.class);
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + bulkTankLisaaSailioonMethodRef.signature(),
                bulkTankLisaaSailioonMethodRef.exists());

        bulkTankOtaSailiostaMethodRef = bulkTankClassRef.method("getFromTank").
                returning(double.class).taking(double.class);
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + bulkTankOtaSailiostaMethodRef.signature(),
                bulkTankOtaSailiostaMethodRef.exists());
    }

    private <T> T luoBulkTank(ClassRef<T> luokka, Double capacity) throws Throwable {
        if (capacity == null) {
            assertTrue("Class " + s(luokka.getReferencedClass().getName())
                    + " doesn't have public constructor which takes no parameters.",
                    luokka.constructor().takingNoParams().isPublic());

            String v = "\nerror caused by code\n"
                    + "new BulkTank()";

            return luokka.constructor().takingNoParams().withNiceError(v).invoke();
        }

        assertTrue("Class " + s(luokka.getReferencedClass().getName())
                + " doesn't have constructor public BulkTank(double capacity)",
                luokka.constructor().taking(double.class).isPublic());

        String v = "\nerror caused by code\n"
                + "new BulkTank(" + capacity + ")";

        return luokka.constructor().taking(double.class).withNiceError(v).invoke(capacity);

    }

    private void testaaTilavuus(S tank, Double oletettu, String v) throws Throwable {
        Double paluuarvo = bulkTankGetTilavuusMethodRef.withNiceError(v).invokeOn(tank);
        Assert.assertTrue(v + "\nm.getCapacity();\n expected result " + oletettu
                + "\nReturned value was: " + paluuarvo,
                paluuarvo >= oletettu - 0.1 && paluuarvo <= oletettu + 0.1);
    }

    private void testaaSaldo(S tank, Double oletettu, String v) throws Throwable {
        Double paluuarvo = bulkTankGetSaldoMethodRef.withNiceError(v).invokeOn(tank);
        Assert.assertTrue(v + "\nm.getVolume();\n expected result " + oletettu
                + "\nReturned value was: " + paluuarvo,
                paluuarvo >= oletettu - 0.1 && paluuarvo <= oletettu + 0.1);
    }

    private String toString(Object o, String v) throws Throwable {

        Reflex.ClassRef<Object> classRef = Reflex.reflect(o.getClass().getName());
        return classRef.method(o, "toString").returning(String.class).takingNoParams().withNiceError(v).invoke();
    }

    private void testaaPaljonkoTilaaJaljella(S tank, String v) throws Throwable {
        Double capacity, volume, paluuarvo;

        paluuarvo = bulkTankPaljonkoTilaaJaljellaMethodRef.withNiceError(v).invokeOn(tank);

        try {
            capacity = bulkTankGetTilavuusMethodRef.invokeOn(tank);
            volume = bulkTankGetSaldoMethodRef.invokeOn(tank);

        } catch (Throwable t) {
            Assert.fail("Class " + s(tank.getClass().getName()) + "'s method "
                    + bulkTankPaljonkoTilaaJaljellaMethodRef.getMethod().getName()
                    + " threw an exception: " + t.toString());
            return;
        }

        Double oletettu = (capacity - volume);

        Assert.assertTrue(v + "\nm.howMuchFreeSpace();\n expected result " + oletettu
                + "\nReturned value was: " + paluuarvo,
                paluuarvo >= oletettu - 0.1 && paluuarvo <= oletettu + 0.1);

        String tila = toString(tank, v + "\nm.toString();");

        assertFalse("Crete for class " + s(tank.getClass().getName()) + " method toString as defined in the assignment", tila.contains("@"));

        String[] tilanOsat = tila.split("/");

        Assert.assertTrue("Class " + s(tank.getClass().getName())
                + "'s method toString() should return the state of the bulk tank "
                + "in the following way volume/capacity, \n"
                + "but returned string was: " + tila,
                tilanOsat.length == 2);

        double merkkijonoSaldo;
        double merkkijonoTilavuus;

        try {
            merkkijonoSaldo = Double.parseDouble(tilanOsat[0].trim());
            merkkijonoTilavuus = Double.parseDouble(tilanOsat[1].trim());

        } catch (Exception e) {
            Assert.fail("Class " + s(tank.getClass().getName())
                    + "'s method toSring() should return tank's state "
                    + "in the following way: volume/capacity, \nbut "
                    + "returned string didn't contain any numbers: " + tila);
            return;
        }

        testaaOnkoKokonaisLuku(tank.getClass(), "toString()",
                merkkijonoSaldo, tila);
        testaaOnkoKokonaisLuku(tank.getClass(), "toString()",
                merkkijonoTilavuus, tila);

        Assert.assertEquals("Class " + s(tank.getClass().getName())
                + "'s method toSring() should return tank's state "
                + "in the following way: volume/capacity, but returned volume was wrong: "
                + merkkijonoSaldo + " -- it should have been: " + Math.ceil(volume),
                Math.ceil(volume), merkkijonoSaldo, 0.1);
        Assert.assertEquals("Class " + s(tank.getClass().getName())
                + "'s method toSring() should return tank's state "
                + "in the following way: volume/capacity, but returned capacity was wrong: "
                + merkkijonoTilavuus + " -- it should have been: " + Math.ceil(capacity),
                Math.ceil(capacity), merkkijonoTilavuus, 0.1);
    }

    private void testaaLisaaSailioon(S tank, Double maara, String v) throws Throwable {
        Double volumeEnnen, capacity, volumeJalkeen, odotettuSaldo;

        capacity = bulkTankGetTilavuusMethodRef.invokeOn(tank);
        volumeEnnen = bulkTankGetSaldoMethodRef.invokeOn(tank);
        bulkTankLisaaSailioonMethodRef.withNiceError(v).invokeOn(tank, maara);
        volumeJalkeen = bulkTankGetSaldoMethodRef.invokeOn(tank);


        if ((volumeEnnen + maara) >= capacity) {
            odotettuSaldo = capacity;
        } else {
            odotettuSaldo = volumeEnnen + maara;
        }

        Assert.assertTrue(v + "\nm.getVolume();\n expected result " + odotettuSaldo
                + "\nReturned value was: " + volumeJalkeen,
                volumeJalkeen >= odotettuSaldo - 0.1 && volumeJalkeen <= odotettuSaldo + 0.1);

        testaaPaljonkoTilaaJaljella(tank, v);
    }

    private void testaaOtaSailiosta(S tank, Double maara, String v) throws Throwable {
        Double volumeEnnen, capacity, otettu, volumeJalkeen, odotettuSaldo;

        capacity = bulkTankGetTilavuusMethodRef.invokeOn(tank);
        volumeEnnen = bulkTankGetSaldoMethodRef.invokeOn(tank);
        otettu = bulkTankOtaSailiostaMethodRef.withNiceError(v).invokeOn(tank, maara);
        volumeJalkeen = bulkTankGetSaldoMethodRef.invokeOn(tank);

        if ((volumeEnnen - maara) < 0) {
            odotettuSaldo = 0.0;
        } else {
            odotettuSaldo = volumeEnnen - maara;
        }

        Assert.assertTrue(v + "\nm.getVolume();\n expected result " + odotettuSaldo
                + "\nReturned value was: " + volumeJalkeen,
                volumeJalkeen >= odotettuSaldo - 0.1 && volumeJalkeen <= odotettuSaldo + 0.1);

        testaaPaljonkoTilaaJaljella(tank, v);
    }

    private void reflektoiCow() {
        String luokanNimi = "farmsimulator.Cow";

        saniteettitarkastus(luokanNimi, 5, "instance variables for name, capacity and amount of milk");

        cowClassRef = (ClassRef<L>) Reflex.reflect(luokanNimi);

        if (!Alive.class
                .isAssignableFrom(cowClassRef.getReferencedClass())) {
            Assert.fail(
                    "Class " + s(luokanNimi) + " should "
                    + "implement interface " + s(Alive.class.getName()));
        }

        if (!Milkable.class
                .isAssignableFrom(cowClassRef.getReferencedClass())) {
            Assert.fail(
                    "Class " + s(luokanNimi) + " should "
                    + "implement interface " + s(Milkable.class.getName()));
        }

        cowGetTilavuusMethodRef = cowClassRef.method("getCapacity").
                returning(double.class).takingNoParams();

        Assert.assertTrue(
                "Class " + s(luokanNimi) + " doesn't have method public " + cowGetTilavuusMethodRef.signature(),
                cowGetTilavuusMethodRef.isPublic());

        cowGetMaaraMethodRef = cowClassRef.method("getAmount").
                returning(double.class).takingNoParams();

        Assert.assertTrue(
                "Class " + s(luokanNimi) + " doesn't have method public " + cowGetMaaraMethodRef.signature(),
                cowGetMaaraMethodRef.isPublic());

        cowGetNimiMethodRef = cowClassRef.method("getName").
                returning(String.class).takingNoParams();

        Assert.assertTrue(
                "Class " + s(luokanNimi) + " doesn't have method public " + cowGetNimiMethodRef.signature(),
                cowGetNimiMethodRef.isPublic());

        cowLypsaMethodRef = cowClassRef.method("milk").
                returning(double.class).takingNoParams();

        Assert.assertTrue(
                "Class " + s(luokanNimi) + " doesn't have method public " + cowLypsaMethodRef.signature(),
                cowLypsaMethodRef.isPublic());

        cowEleleTuntiMethodRef = cowClassRef.method("liveHour").
                returningVoid().takingNoParams();

        Assert.assertTrue(
                "Class " + s(luokanNimi) + " doesn't have method public " + cowEleleTuntiMethodRef.signature(),
                cowEleleTuntiMethodRef.isPublic());
    }

    private L luoCow(ClassRef<L> luokka, String name) throws Throwable {
        L instanssi;
        String v = "";

        assertTrue("Create for class Cow a constructor public Cow(String name)", luokka.constructor().taking(String.class).isPublic());
        if (name == null) {
            assertTrue("Class Cow should have a public constructor which takes no parameters", luokka.constructor().takingNoParams().isPublic());
            instanssi = luokka.constructor().takingNoParams().invoke();
            v = "Cow l = new Cow();";
        } else {
            instanssi = luokka.constructor().taking(String.class).withNiceError().invoke(name);
            v = "Cow l = new Cow(\"" + name + "\");";
        }

        String palautettuNimi = cowGetNimiMethodRef.withNiceError(v + "\nl.getName();").invokeOn(instanssi);

        Double maara = cowGetMaaraMethodRef.withNiceError(v + "\nl.getAmount();").invokeOn(instanssi);

        Double capacity = cowGetTilavuusMethodRef.withNiceError(v + "\nl.getCapacity();").invokeOn(instanssi);

        if (name == null) {
            Assert.assertTrue(v + "\nl.getName();\n returned: " + palautettuNimi,
                    (palautettuNimi != null) && palautettuNimi.trim().length() > 0);
        } else {
            Assert.assertTrue(v + "\nl.getName();\n returned: " + palautettuNimi,
                    name.equals(palautettuNimi));
        }

        Assert.assertEquals(v + "\nl.getAmount();\n",
                0.0, maara, 0.1);

        Assert.assertTrue("Cow's capacity should be between 15-40 litres\n"
                + "" + v + "\nl.getCapacity();\nreturned: " + capacity, capacity >= 15 && capacity <= 40);

        String tila = toString(instanssi, v + "l.toString();");//instanssi.toString();

        assertFalse("Create for class Cow method toString() as defined in the assignment", tila.contains("@"));

        String[] tilanOsat = tila.split("[ /]");

        Assert.assertTrue("Class " + instanssi.getClass().getName()
                + "'s method toSring() should return cow's state "
                + "in the following way: name amount/capacity, \n"
                + "but returned string was: " + tila,
                tilanOsat.length == 3);

        String merkkijonoNimi = tilanOsat[0].trim();

        Assert.assertTrue("Class " + instanssi.getClass().getName()
                + "'s method toString() should return information of the cows "
                + "in the following way name volume/capacity, but in the returned string "
                + "cow's name wasn't the given name " + palautettuNimi + ", it was: " + merkkijonoNimi,
                palautettuNimi.equals(merkkijonoNimi));

        return instanssi;
    }

    private void testaaCownElelemista(L cow, String v) throws Throwable {
        Double maaraEnnen, capacity, maaraJalkeen;

        capacity = cowGetTilavuusMethodRef.invokeOn(cow);
        maaraEnnen = cowGetMaaraMethodRef.invokeOn(cow);
        cowEleleTuntiMethodRef.withNiceError(v + "\ncow.liveHour();").invokeOn(cow);
        maaraJalkeen = cowGetMaaraMethodRef.invokeOn(cow);


        Assert.assertTrue("Class " + s(cow.getClass().getName()) + "'s method "
                + cowEleleTuntiMethodRef.getMethod().getName()
                + " was called and cow's capacity was "
                + capacity + ", \n"
                + "but amount of the milk exceeded the capacity: " + maaraJalkeen,
                maaraJalkeen <= capacity);

        Assert.assertTrue("Class " + s(cow.getClass().getName()) + "'s method "
                + cowEleleTuntiMethodRef.getMethod().getName()
                + " was called and tank's capacity was "
                + capacity + ", \nbut amount of milk " + maaraJalkeen
                + " didn't increase in the excepted range: 0.7-2.0 litres per hour, \n"
                + "increase was "
                + (maaraJalkeen - maaraEnnen),
                maaraJalkeen >= capacity - 0.1
                || (maaraJalkeen >= maaraEnnen + 0.7
                && maaraJalkeen <= maaraEnnen + 2.0));

        String tila = cow.toString();

        String[] tilanOsat = tila.split("[ /]");

        Assert.assertTrue("Class " + cow.getClass().getName()
                + "'s method toString() should return the state of the tank "
                + "in the following way name amount/capacity, but returned string was: " + tila,
                tilanOsat.length == 3);

        double merkkijonoMaara;
        double merkkijonoTilavuus;

        try {
            merkkijonoMaara = Double.parseDouble(tilanOsat[1].trim());
            merkkijonoTilavuus = Double.parseDouble(tilanOsat[2].trim());

        } catch (Exception e) {
            Assert.fail("Class " + cow.getClass().getName()
                    + "'s method toString() should return cow's information "
                    + "in the following way name volume/capacity, but "
                    + "returned string didn't contain any numbers: " + tila);
            return;
        }

        testaaOnkoKokonaisLuku(cow.getClass(), "toString()",
                merkkijonoMaara, tila);
        testaaOnkoKokonaisLuku(cow.getClass(), "toString()",
                merkkijonoTilavuus, tila);

        Assert.assertEquals("Class " + cow.getClass().getName()
                + "'s method toSring() should return cow's information "
                + "in the following way: volume/capacity, but returned amount of milk was wrong: "
                + merkkijonoMaara + " -- it should have been: " + Math.ceil(maaraJalkeen),
                Math.ceil(maaraJalkeen), merkkijonoMaara, 0.1);
        Assert.assertEquals("Class " + cow.getClass().getName()
                + "'s method toSring() should return cow's information "
                + "in the following way: volume/capacity, but returned capacity was wrong: "
                + merkkijonoTilavuus + " -- it should have been: " + Math.ceil(capacity),
                Math.ceil(capacity), merkkijonoTilavuus, 0.1);
    }

    private void testaaCownElelemista(L cow) {
        Double maaraEnnen, capacity, maaraJalkeen;
        try {
            capacity = cowGetTilavuusMethodRef.invokeOn(cow);
            maaraEnnen = cowGetMaaraMethodRef.invokeOn(cow);
            cowEleleTuntiMethodRef.invokeOn(cow);
            maaraJalkeen = cowGetMaaraMethodRef.invokeOn(cow);
        } catch (Throwable t) {
            Assert.fail("Class " + cow.getClass().getName() + "'s method "
                    + cowEleleTuntiMethodRef.getMethod().getName() + " threw an exception: " + t.toString());
            return;
        }

        Assert.assertTrue("Class " + cow.getClass().getName() + "'s method "
                + cowEleleTuntiMethodRef.getMethod().getName()
                + " was called and cow's capacity was "
                + capacity + ", but amount of milk exceeded the capacity: " + maaraJalkeen,
                maaraJalkeen <= capacity);

        Assert.assertTrue("Class " + cow.getClass().getName() + "'s method "
                + cowEleleTuntiMethodRef.getMethod().getName()
                + " was called and tank's capacity was "
                + capacity + ", but amount of milk " + maaraJalkeen
                + " didn't increase in the expected range: 0.7-2.0 litres per hour, increase was "
                + (maaraJalkeen - maaraEnnen),
                maaraJalkeen >= capacity - 0.1
                || (maaraJalkeen >= maaraEnnen + 0.7
                && maaraJalkeen <= maaraEnnen + 2.0));

        String tila = cow.toString();

        String[] tilanOsat = tila.split("[ /]");

        Assert.assertTrue("Class " + cow.getClass().getName()
                + "'s method toSring() should return state of the tank "
                + "in the following way: name amount/capacity, but returned string was: " + tila,
                tilanOsat.length == 3);

        double merkkijonoMaara;
        double merkkijonoTilavuus;

        try {
            merkkijonoMaara = Double.parseDouble(tilanOsat[1].trim());
            merkkijonoTilavuus = Double.parseDouble(tilanOsat[2].trim());

        } catch (Exception e) {
            Assert.fail("Class " + cow.getClass().getName()
                    + "'s method toSring() should return cow's information "
                    + "in the following way: name volume/capacity, but "
                    + "returned string didn't contain any numbers: " + tila);
            return;
        }

        testaaOnkoKokonaisLuku(cow.getClass(), "toString()",
                merkkijonoMaara, tila);
        testaaOnkoKokonaisLuku(cow.getClass(), "toString()",
                merkkijonoTilavuus, tila);

        Assert.assertEquals("Class " + cow.getClass().getName()
                + "'s method toSring() should return cow's information "
                + "in the following way: volume/capacity, but returned amount of milk was wrong: "
                + merkkijonoMaara + " -- it should have been: " + Math.ceil(maaraJalkeen),
                Math.ceil(maaraJalkeen), merkkijonoMaara, 0.1);
        Assert.assertEquals("Class " + cow.getClass().getName()
                + "'s method toSring() should return cow's information "
                + "in the following way: volume/capacity, but returned capacity was wrong: "
                + merkkijonoTilavuus + " -- it should have been: " + Math.ceil(capacity),
                Math.ceil(capacity), merkkijonoTilavuus, 0.1);
    }

    private void testaaCownLypsamista(L cow, String v) throws Throwable {
        Double maaraEnnen, lypsettyMaara, maaraJalkeen;

        maaraEnnen = cowGetMaaraMethodRef.invokeOn(cow);
        lypsettyMaara = cowLypsaMethodRef.withNiceError(v + "\ncow.milk()").invokeOn(cow);
        maaraJalkeen = cowGetMaaraMethodRef.invokeOn(cow);


        Assert.assertTrue("Class " + s(cow.getClass().getName()) + "'s method "
                + cowLypsaMethodRef.getMethod().getName()
                + " was called, but milk was left: " + maaraJalkeen,
                maaraJalkeen == 0);

        Assert.assertTrue("Class " + s(cow.getClass().getName()) + "'s method "
                + cowLypsaMethodRef.getMethod().getName()
                + " was called but amount of milked milk " + maaraJalkeen
                + " wasn't the same as the cow's amount of milk: " + maaraEnnen,
                (lypsettyMaara >= maaraEnnen - 0.1
                && lypsettyMaara <= maaraEnnen + 0.1));
    }

    private void reflektoiMilkingRobot() {
        String luokanNimi = "farmsimulator.MilkingRobot";

        lypsyrobottiClassRef = (ClassRef<R>) Reflex.reflect(luokanNimi);

        saniteettitarkastus(luokanNimi, 1, "instance variable for bulk tank");

        lypsyrobottiLypsaMethodRef = lypsyrobottiClassRef.method("milk").
                returningVoid().taking(Milkable.class);
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + lypsyrobottiLypsaMethodRef.signature(),
                lypsyrobottiLypsaMethodRef.isPublic());

        lypsyrobottiGetBulkTankMethodRef = lypsyrobottiClassRef.method("getBulkTank").
                returning(bulkTankClassRef.getReferencedClass()).takingNoParams();
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + lypsyrobottiGetBulkTankMethodRef.signature(),
                lypsyrobottiGetBulkTankMethodRef.isPublic());

        lypsyrobottiSetBulkTankMethodRef = lypsyrobottiClassRef.method("setBulkTank").
                returningVoid().taking(bulkTankClassRef.getReferencedClass());
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + lypsyrobottiSetBulkTankMethodRef.signature(),
                lypsyrobottiSetBulkTankMethodRef.isPublic());
    }

    private R luoMilkingRobot(ClassRef<R> luokka) throws Throwable {
        assertTrue("Class MilkingRobot should have a constructor which takes no parameters", luokka.constructor().takingNoParams().isPublic());

        R instanssi = luokka.constructor().takingNoParams().invoke();

        String v = "MilkingRobot r = new MilkingRobot();\n"
                + "r.getBulkTank();";

        S tank = lypsyrobottiGetBulkTankMethodRef.withNiceError(v).invokeOn(instanssi);


        Assert.assertTrue("Class " + instanssi.getClass().getName()
                + "'s method " + lypsyrobottiGetBulkTankMethodRef.getMethod().getName()
                + " should return null-reference when bulk tank hasn't been installed: ",
                tank == null);

        return instanssi;
    }

    private void testaaLypsyrobotinLypsamistaIlmanSailota(R robo, L cow) throws Throwable {
        String v = "MilkingRobot r = new MilkingRobot();\n"
                + "Cow cow = new Cow();\n"
                + "r.milk(cow);";

        try {
            lypsyrobottiLypsaMethodRef.withNiceError(v).invokeOn(robo, (Milkable) cow);

            Assert.fail("Class " + s(robo.getClass().getName())
                    + "'s method " + lypsyrobottiLypsaMethodRef.getMethod().getName()
                    + " didn't throw an exception. Check that method throws IllegalStateException, "
                    + "if bulk tank hasn't been installed.");

        } catch (Throwable t) {

            if (!t.toString().contains("IllegalState")) {
                Assert.fail("Class " + s(robo.getClass().getName())
                        + "'s method " + lypsyrobottiLypsaMethodRef.getMethod().getName()
                        + " didn't throw an exception. Check that method throws IllegalStateException, "
                        + "if bulk tank hasn't been installed.\n"
                        + "For example, it should throw an exception with code:\n"
                        + "Cow cow = new Cow(\"Arto\");\n"
                        + "MilkingRobot r = new MilkingRobot();\n"
                        + "r.milk(cow);");
            }
        }
    }

    private void testaaLypsyrobotinLypsamista(R robo, L cow) throws Throwable {
        String v = "MilkingRobot r = new MilkingRobot();\n"
                + "BulkTank m = new BulkTank(100.0);\n"
                + "r.setBulkTank(m);";

        S tank = lypsyrobottiGetBulkTankMethodRef.withNiceError(v).invokeOn(robo);

        assertFalse(v + "\nr.getBulkTank(); \nreturned null", tank == null);

        Double tanknTilavuus = null, volumeEnnen = null,
                jaljellaEnnen = null, cowssaEnnen = null;

        tanknTilavuus = bulkTankGetTilavuusMethodRef.withNiceError().invokeOn(tank);
        volumeEnnen = bulkTankGetSaldoMethodRef.withNiceError().invokeOn(tank);
        jaljellaEnnen = bulkTankPaljonkoTilaaJaljellaMethodRef.withNiceError().invokeOn(tank);
        cowssaEnnen = cowGetMaaraMethodRef.withNiceError().invokeOn(cow);

        lypsyrobottiLypsaMethodRef.withNiceError(v).invokeOn(robo, (Milkable) cow);

        Double cowssaJalkeen = cowGetMaaraMethodRef.withNiceError().invokeOn(cow);

        Double volumeJalkeen = bulkTankGetSaldoMethodRef.withNiceError().invokeOn(tank);

        v = "MilkingRobot r = new MilkingRobot();\n"
                + "r.setBulkTank( new BulkTank(100.0) );\n";
        v += "Cow cow = new Cow();\n"
                + "cow.liveHour();\n"
                + "r.milk(cow)\n"
                + "cow.getVolume();\n";

        Assert.assertEquals("After MilkingRobot has milked cow, cow shouldn't have any milk left! Test code\n"
                + v,
                0.0, cowssaJalkeen, 0.01);

        v = "MilkingRobot r = new MilkingRobot();\n"
                + "BulkTank tank = new BulkTank(100.0);\n"
                + "r.setBulkTank( tank );\n";
        v += "Cow cow = new Cow();\n"
                + "cow.liveHour();\n"
                + "r.milk(cow)\n"
                + "tank.getVolume();\n";

        Assert.assertTrue("Before milking cow had " + cowssaEnnen + " and tank had " + volumeEnnen + " "
                + ", after milking robot is done milking, every milk should be in the bulk tank\n"
                + "Now tank only has " + volumeJalkeen
                + "\nCheck code:\n" + v,
                volumeJalkeen >= tanknTilavuus - 0.1
                || (volumeJalkeen >= volumeEnnen + cowssaEnnen - 0.1
                && volumeJalkeen <= volumeEnnen + cowssaEnnen + 0.1));

    }

    private void reflektoiCowHouse() {
        String luokanNimi = "farmsimulator.CowHouse";

        cowhouseClassRef = (ClassRef<N>) Reflex.reflect(luokanNimi);

        saniteettitarkastus(luokanNimi, 2, "instance variables for bulk tank and milking robot");

        cowhouseGetBulkTankMethodRef = cowhouseClassRef.method("getBulkTank").
                returning(bulkTankClassRef.getReferencedClass()).takingNoParams();
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + cowhouseGetBulkTankMethodRef.signature(),
                cowhouseGetBulkTankMethodRef.isPublic());

        cowhouseHoidaCowMethodRef = cowhouseClassRef.method("takeCareOf").
                returningVoid().taking(cowClassRef.getReferencedClass());
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + cowhouseHoidaCowMethodRef.signature(),
                cowhouseHoidaCowMethodRef.isPublic());

        cowhouseHoidaCowtMethodRef = cowhouseClassRef.method("takeCareOf").
                returningVoid().taking(Collection.class);
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public void takeCareOf(Collection<Cow> cows)",
                cowhouseHoidaCowtMethodRef.isPublic());

        cowhouseAsennaMilkingRobotMethodRef = cowhouseClassRef.method("installMilkingRobot").
                returningVoid().taking(lypsyrobottiClassRef.getReferencedClass());
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + cowhouseAsennaMilkingRobotMethodRef.signature(),
                cowhouseAsennaMilkingRobotMethodRef.isPublic());
    }

    private N luoCowHouse(ClassRef<N> luokka, S tank) throws Throwable {

        assertTrue("Create for class CowHouse a constructor public CowHouse(BulkTank tank)", luokka.constructor().taking(bulkTankClassRef.getReferencedClass()).isPublic());

        N instanssi = luokka.constructor().taking(bulkTankClassRef.getReferencedClass()).invoke(tank);

        String v = "BulkTank m = new BulkTank();\n"
                + "CowHouse n = new CowHouse(m);\n"
                + "n.getBulkTank();\n";

        S palautettuSailio = cowhouseGetBulkTankMethodRef.withNiceError(v).invokeOn(instanssi);

        Assert.assertTrue("Class " + s(instanssi.getClass().getName())
                + "'s method " + cowhouseGetBulkTankMethodRef.getMethod().getName()
                + " should return the bulk tank given in the constructor\ntest code\n"
                + v,
                tank.equals(palautettuSailio));

        L cow = luoCow(cowClassRef, null);

        try {
            cowhouseHoidaCowMethodRef.invokeOn(instanssi, cow);

        } catch (Throwable t) {

            if (!t.toString().contains("IllegalState")) {
                Assert.fail("Class " + s(instanssi.getClass().getName())
                        + "'s method takeCareOf should throw IllegalStateException, "
                        + "if milking robot hasn't been installed. \n"
                        + "check code:\n"
                        + "CowHouse house = new CowHouse( new BulkTank());\n"
                        + "Cow cow = new Cow(\"Pekka\");\n"
                        + "house.takeCareOf(cow);\n");
            }
        }

        return instanssi;
    }

    private double laskeMaidonMaaraLehmissa(Collection<L> cows) {
        double yhteensa = 0;
        for (L cow : cows) {
            try {
                yhteensa += cowGetMaaraMethodRef.invokeOn(cow);
            } catch (Throwable t) {
                Assert.fail("Class " + cow.getClass().getName()
                        + "'s method " + cowGetMaaraMethodRef.getMethod().getName()
                        + " threw an exception: " + t.toString());
            }
        }
        return yhteensa;
    }

    private double annaBulkTanknSaldo(S tank) {
        try {
            return bulkTankGetSaldoMethodRef.invokeOn(tank);
        } catch (Throwable t) {
            Assert.fail("Class " + tank.getClass().getName()
                    + "'s method " + bulkTankGetSaldoMethodRef.getMethod().getName()
                    + " threw an exception: " + t.toString());
            return -1;
        }
    }

    private double annaBulkTanknTilavuus(S tank) {
        try {
            return bulkTankGetTilavuusMethodRef.invokeOn(tank);
        } catch (Throwable t) {
            Assert.fail("Class " + tank.getClass().getName()
                    + "'s method " + bulkTankGetTilavuusMethodRef.getMethod().getName()
                    + " threw an exception: " + t.toString());
            return -1;
        }
    }

    private void testaaNavetanLehmienHoitamista(N cowhouse, List<L> cows) throws Throwable {
        double maaraLehmissaYhteensa = laskeMaidonMaaraLehmissa(cows);
        double maaraEnsimmaisessaCowssa = laskeMaidonMaaraLehmissa(
                Collections.singletonList(cows.get(0)));

        String v = "\n"
                + "Cow cow1 = new Cow(\"Cow 1\");\n"
                + "cow1.liveHour();"
                + "BulkTank m = new BulkTank();\n"
                + "CowHouse nav = new CowHouse(m);\n"
                + "nav.setMilkingRobot( new MilkingRobot() );\n"
                + "nav.takeCareOf(cow1);";

        cowhouseHoidaCowMethodRef.withNiceError(v).invokeOn(cowhouse, cows.get(0));

        S tank = cowhouseGetBulkTankMethodRef.withNiceError(v + "\nnav.getBulkTank()").invokeOn(cowhouse);

        double tanknSaldo = annaBulkTanknSaldo(tank);

        Assert.assertEquals("Executed code" + v + "\nSo one cow was taken care of in the cowhouse which had "
                + maaraEnsimmaisessaCowssa + " litres of milk, but in the tank went "
                + "different amount of milk: " + tanknSaldo,
                maaraEnsimmaisessaCowssa, tanknSaldo, 0.1);

        /*
         *
         */

        v = "\n"
                + "Cow cow1 = new Cow(\"Cow 1\");\n"
                + "cow1.liveHour();"
                + "BulkTank m = new BulkTank();\n"
                + "CowHouse nav = new CowHouse(m);\n"
                + "nav.setMilkingRobot( new MilkingRobot() );\n"
                + "nav.takeCareOf(cow1);\n"
                + "List<Cow> cows = new ArrayList<Cow>();\n"
                + "cows.add( new Cow() );\n"
                + "cows.add( new Cow() );\n"
                + "// cows in the list are living\n"
                + "nav.takeCareOf( cows );\n";

        cowhouseHoidaCowtMethodRef.withNiceError(v).invokeOn(cowhouse, cows);

        tanknSaldo = annaBulkTanknSaldo(tank);

        Assert.assertEquals("Executed code\n"+v
                + "So multiple cows were taken care of in the cowhouse and their total amount of milk was "
                + maaraLehmissaYhteensa + " litres but in the tank went "
                + "different amount of milk: " + tanknSaldo,
                maaraLehmissaYhteensa, tanknSaldo, 0.1);

        String tila = cowhouse.toString();

        assertFalse("Create for class CowHouse method toString() as defined in the assignment",
                tila.contains("@"));

        String[] tilanOsat = tila.split("/");

        Assert.assertTrue("Class " + cowhouse.getClass().getName()
                + "'s method toSring() should return the state of the cowhouse's bulk tank "
                + "in the following way: volume/capacity, \n"
                + "but returned string was: " + tila,
                tilanOsat.length == 2);

        double tanknTilavuus = annaBulkTanknTilavuus(tank);

        double merkkijonoSaldo;
        double merkkijonoTilavuus;

        try {
            String eka = tilanOsat[0].trim();
            int n = 0;
            while( !Character.isDigit(eka.charAt(n))){
                n++;
            }
            eka = eka.substring(n);

            merkkijonoSaldo = Double.parseDouble(eka);
            merkkijonoTilavuus = Double.parseDouble(tilanOsat[1].trim());

        } catch (Exception e) {
            Assert.fail("Class " + cowhouse.getClass().getName()
                    + "'s method toSring() should return the state of the bulk tank "
                    + "in the following way: volume/capacity, "
                    + "returned string didn't contain any numbers: " + tila);
            return;
        }

        testaaOnkoKokonaisLuku(cowhouse.getClass(), "toString()",
                merkkijonoSaldo, tila);
        testaaOnkoKokonaisLuku(cowhouse.getClass(), "toString()",
                merkkijonoTilavuus, tila);

        Assert.assertEquals("Class " + cowhouse.getClass().getName()
                + "'s method toSring() should return the state of the cowhouse's bulk tank "
                + "in the following way: volume/capacity, but returned volume was wrong: "
                + merkkijonoSaldo + " -- it should have been: " + Math.ceil(tanknSaldo),
                Math.ceil(tanknSaldo), merkkijonoSaldo, 0.1);
        Assert.assertEquals("Class " + cowhouse.getClass().getName()
                + "'s method toSring() should return the state of the cowhouse's bulk tank "
                + "in the following way: volume/capacity, but returned capacity was wrong: "
                + merkkijonoTilavuus + " -- it should have been: " + Math.ceil(tanknTilavuus),
                Math.ceil(tanknTilavuus), merkkijonoTilavuus, 0.1);
    }

    private void reflektoiFarm() {
        String luokanNimi = "farmsimulator.Farm";

        farmClassRef = (ClassRef<M>) Reflex.reflect(luokanNimi);

        saniteettitarkastus(luokanNimi, 3, "instance variables for owner, cowhouse and cows. ");

        farmGetOmistajaMethodRef = farmClassRef.method("getOwner").
                returning(String.class).takingNoParams();
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + farmGetOmistajaMethodRef.signature(),
                farmGetOmistajaMethodRef.isPublic());

        farmEleleTuntiMethodRef = farmClassRef.method("liveHour").
                returningVoid().takingNoParams();
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + farmEleleTuntiMethodRef.signature(),
                farmEleleTuntiMethodRef.isPublic());

        farmLisaaCowMethodRef = farmClassRef.method("addCow").
                returningVoid().taking(cowClassRef.getReferencedClass());
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + farmLisaaCowMethodRef.signature(),
                farmLisaaCowMethodRef.isPublic());

        farmHoidaCowtMethodRef = farmClassRef.method("manageCows").
                returningVoid().takingNoParams();
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + farmHoidaCowtMethodRef.signature(),
                farmHoidaCowtMethodRef.isPublic());

        farmAsennaCowHouseanMilkingRobotMethodRef = farmClassRef.method("installMilkingRobot").
                returningVoid().taking(lypsyrobottiClassRef.getReferencedClass());
        Assert.assertTrue("Class " + s(luokanNimi) + " doesn't have method public " + farmAsennaCowHouseanMilkingRobotMethodRef.signature(),
                farmAsennaCowHouseanMilkingRobotMethodRef.isPublic());
    }

    private M luoFarm(ClassRef<M> luokka, String owner, N cowhouse) throws Throwable {

        String v = "Farm tila = new Farm(\"" + owner + "\", new CowHouse( new BulkTank() ));\n";

        assertTrue("Create for class " + s(luokka.getReferencedClass().getName())
                + " a constructor public Farm(String owner, CowHouse cowhouse)", luokka.constructor().taking(String.class,
                cowhouseClassRef.getReferencedClass()).isPublic());

        M instanssi = luokka.constructor().taking(String.class,
                cowhouseClassRef.getReferencedClass()).withNiceError(v).invoke(owner, cowhouse);

        v += "tila.getOwner();\n";

        String palautettuOmistaja = farmGetOmistajaMethodRef.withNiceError(v).invokeOn(instanssi);

        Assert.assertTrue(v + "returned " + palautettuOmistaja,
                owner.equals(palautettuOmistaja));

        return instanssi;
    }

    private void testaaFarmnLehmienHoitamista(M farm, S tank,
            Collection<L> cows, int eleltavatTunnit) throws Throwable {

        String v = "BulkTank tank = new BulkTank();\n"
                + "Farm tila = new Farm(\"pekka\", new CowHouse( tank ));\n"
                + "tila.installMilkingRobot( new MilkingRobot() );\n";
        int x = 1;
        for (L l : cows) {
            v += "tila.addCow( new Cow(\"Lehmä " + x + "\") )\n";
            x++;
        }

        double tanknSaldoAlussa = annaBulkTanknSaldo(tank);

        for (int i = 0; i < eleltavatTunnit; i++) {
            v += "tila.liveHour()\n";
            farmEleleTuntiMethodRef.withNiceError(v).invokeOn(farm);
        }

        double maaraLehmissaYhteensa = 0;
        for (L cow : cows) {
            double maara = laskeMaidonMaaraLehmissa(
                    Collections.singletonList(cow));
            if (maara < 0.6) {
                Assert.fail("After executing code " + v
                        + " all cows should have milk at least 0.7 litres, "
                        + "but this isn't the case with cow: " + cow);
            }

            maaraLehmissaYhteensa += maara;
        }

        v += "tila.manageCows();\n";

        farmHoidaCowtMethodRef.withNiceError(v).invokeOn(farm);

        double tanknSaldo = annaBulkTanknSaldo(tank);

        v += "tank.getVolume()";

        String a = "Took care of cows, which had "
                + maaraLehmissaYhteensa + " litres of milk, but in the tank went "
                + "different amount of milk. Check code\n";

        Assert.assertEquals(a + "\n" + v + "\n",
                maaraLehmissaYhteensa, tanknSaldo - tanknSaldoAlussa, 0.1);

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
