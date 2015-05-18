
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import static org.junit.Assert.*;
import application.Sensor;

public class SensorsAndTemperatureMeasurementTest<_Sensor> {

    @Test
    @Points("20.1")
    public void classConstantSensor() {
        String klassName = "application.ConstantSensor";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("20.1")
    public void noRedundantVariablesConstantSensor() {
        String klassName = "application.ConstantSensor";
        saniteettitarkastus(klassName, 1, "an instance variable for constant value");
    }

    @Test
    @Points("20.1")
    public void testConstantSensorConstructor() throws Throwable {
        String klassName = "application.ConstantSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Reflex.MethodRef1<Object, Object, Integer> ctor = classRef.constructor().taking(int.class).withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "(int value)", ctor.isPublic());
        String v = "error caused by code new ConstantSensor(10);\n";
        ctor.withNiceError(v).invoke(10);
    }

    public Sensor newConstantSensor(int ti) throws Throwable {
        String klassName = "application.ConstantSensor";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);
        Reflex.MethodRef1<Object, Object, Integer> ctor = classRef.constructor().taking(int.class).withNiceError();
        return (Sensor) ctor.invoke(ti);
    }

    @Test
    @Points("20.1")
    public void constantSensorIsSensor() {
        String klassName = "application.ConstantSensor";
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = Sensor.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class ConstantSensor implement interface Sensor?");
        }
    }

    @Test
    @Points("20.1")
    public void testConstantSensor() throws Throwable {
        String klassName = "application.ConstantSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Sensor vs10 = newConstantSensor(10);
        Sensor vs55 = newConstantSensor(55);

        String k1 = ""
                + "ConstantSensor v = new ConstantSensor(10);\n"
                + "v.measure();\n";

        String k2 = ""
                + "ConstantSensor v = new ConstantSensor(55);\n"
                + "v.measure();\n";

        assertEquals(k1, 10, (int) classRef.method(vs10, "measure").returning(int.class).takingNoParams().withNiceError(k1).invoke());
        assertEquals(k2, 55, (int) classRef.method(vs55, "measure").returning(int.class).takingNoParams().withNiceError(k2).invoke());

        k1 = ""
                + "ConstantSensor v = new ConstantSensor(10);\n"
                + "v.isOn();\n";

        k2 = ""
                + "ConstantSensor v = new ConstantSensor(55);\n"
                + "v.isOn();\n";


        assertEquals(k1, true, (boolean) classRef.method(vs10, "isOn").returning(boolean.class).takingNoParams().withNiceError(k1).invoke());
        assertEquals(k2, true, (boolean) classRef.method(vs55, "isOn").returning(boolean.class).takingNoParams().withNiceError(k1).invoke());

        k1 = ""
                + "ConstantSensor v = new ConstantSensor(10);\n"
                + "v.off();\n";

        classRef.method(vs10, "off").returningVoid().takingNoParams().withNiceError(k1).invoke();

        k1 = ""
                + "ConstantSensor v = new ConstantSensor(10);\n"
                + "v.off();\n"
                + "v.isOn();\n";

        assertEquals(k1, true, (boolean) classRef.method(vs10, "isOn").returning(boolean.class).takingNoParams().withNiceError(k1).invoke());

        k1 = ""
                + "ConstantSensor v = new ConstantSensor(10);\n"
                + "v.on();\n";

        classRef.method(vs10, "on").returningVoid().takingNoParams().withNiceError(k1).invoke();

        k1 = ""
                + "ConstantSensor v = new ConstantSensor(10);\n"
                + "v.on();\n"
                + "v.isOn();\n";

        assertEquals(k1, true, (boolean) classRef.method(vs10, "isOn").returning(boolean.class).takingNoParams().withNiceError(k1).invoke());

    }

    /*
     *
     */
    @Test
    @Points("20.2")
    public void classThermometer() {
        String klassName = "application.Thermometer";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("20.2")
    public void noRedundantVariablesThermoMeter() {
        String klassName = "application.Thermometer";
        saniteettitarkastus(klassName, 2, "an instance variable of type Random (which is not necessarily required) and instance variables for remembering the state (on/off)");
    }

    @Test
    @Points("20.2")
    public void testThermometerConstructor() throws Throwable {
        String klassName = "application.Thermometer";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Reflex.MethodRef0<Object, Object> ctor = classRef.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "()", ctor.isPublic());
        String v = "error caused by code new Thermometer();\n";
        ctor.withNiceError(v).invoke();
    }

    @Test
    @Points("20.2")
    public void thermoMeterIsSensor() {
        String klassName = "application.Thermometer";
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = Sensor.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class Thermometer implement interface Sensor?");
        }
    }

    public Sensor newThermometer() throws Throwable {
        String klassName = "application.Thermometer";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);
        Reflex.MethodRef0<Object, Object> ctor = classRef.constructor().takingNoParams().withNiceError();
        return (Sensor) ctor.invoke();
    }

    @Test
    @Points("20.2")
    public void testThermometer() throws Throwable {
        String klassName = "application.Thermometer";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Sensor meter1 = newThermometer();

        // alussa ei päällä

        String k1 = ""
                + "Thermometer v = new Thermometer();\n"
                + "v.isOn();\n";

        assertEquals(k1, false, (boolean) classRef.method(meter1, "isOn").returning(boolean.class).takingNoParams().withNiceError(k1).invoke());

        // päälle

        k1 = ""
                + "Thermometer v = new Thermometer();\n"
                + "v.on();\n";

        classRef.method(meter1, "on").returningVoid().takingNoParams().withNiceError(k1).invoke();

        k1 = ""
                + "Thermometer v = new Thermometer();\n"
                + "v.on();\n"
                + "v.isOn();\n";

        assertEquals(k1, true, (boolean) classRef.method(meter1, "isOn").returning(boolean.class).takingNoParams().withNiceError(k1).invoke());

        // readings

        k1 = ""
                + "Thermometer v = new Thermometer();\n"
                + "v.measure();\n";

        Set tulokset = new TreeSet();
        for (int i = 0; i < 1000; i++) {
            int tulos = (int) classRef.method(meter1, "measure").returning(int.class).takingNoParams().withNiceError(k1).invoke();
            assertTrue("Temperature should have been between -30...30, but:\n" + k1 + " \n" + tulos, tulos > -31 && tulos < 31);
            tulokset.add(tulos);
        }
        assertTrue("Created Thermometer v = new Thermometer(); and called v.measure() a thousand times.\n"
                + "temperatures should have been between -30...30. However, temperatures were\n"
                + tulokset.toString(), tulokset.size() > 50);

        // pois päältä

        k1 = ""
                + "Thermometer v = new Thermometer();\n"
                + "v.off();\n";

        classRef.method(meter1, "off").returningVoid().takingNoParams().withNiceError(k1).invoke();

        k1 = ""
                + "Thermometer v = new Thermometer();\n"
                + "v.off();\n"
                + "v.isOn();\n";

        assertEquals(k1, false, (boolean) classRef.method(meter1, "isOn").returning(boolean.class).takingNoParams().withNiceError(k1).invoke());

        k1 = ""
                + "Thermometer v = new Thermometer();\n"
                + "v.off();\n"
                + "v.measure();\n";
        try {
            classRef.method(meter1, "measure").returning(int.class).takingNoParams().withNiceError(k1).invoke();
            fail("Should have thrown exception IllegalStateException() when it executed\n"
                    + k1);
        } catch (Throwable e) {
        }
    }

    /*
     *
     */
    @Test
    @Points("20.2")
    public void classAverageSensor() {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("20.2")
    public void noRedundantVariablesAverageSensor() {
        String klassName = "application.AverageSensor";
        saniteettitarkastus(klassName, 2, "instance variables for list of sensors and readings\n"
                + "You shouldn't maintain the state of the sensor (on/off) directly in the average sensor."
                + "You should ask what is the current state from the sensors which average sensor controls");
    }

    @Test
    @Points("20.3")
    public void testAverageSensorConstructor() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Reflex.MethodRef0<Object, Object> ctor = classRef.constructor().takingNoParams().withNiceError();
        assertTrue("Define for class " + s(klassName) + " a public constructor: \n"
                + "public " + s(klassName) + "()", ctor.isPublic());
        String v = "error caused by code new Thermometer();\n";
        ctor.withNiceError(v).invoke();
    }

    @Test
    @Points("20.3")
    public void averageSensorIsSensor() {
        String klassName = "application.AverageSensor";
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = Sensor.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Does the class AverageSensor implement interface Sensor?");
        }
    }

    public Sensor newAverageSensor() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);
        Reflex.MethodRef0<Object, Object> ctor = classRef.constructor().takingNoParams().withNiceError();
        return (Sensor) ctor.invoke();
    }

    @Test
    @Points("20.3")
    public void averageSensorHasMethodForAddingOthetSensors() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Sensor meter1 = newThermometer();


        String k1 = "Error caused by code\n"
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( new Thermometer() );\n";

        Sensor ka = newAverageSensor();

        assertTrue("Create method public void addSensor(Sensor sensor) for class AverageSensor", classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).isPublic());

        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).withNiceError(k1).invoke(meter1);
    }

    @Test
    @Points("20.3")
    public void measuringAverageWorks() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Sensor meter1 = newConstantSensor(4);

        String koodi = "Error caused by code\n"
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( new ConstantSensor(4) );\n"
                + "ka.measure();\n";

        Sensor ka = newAverageSensor();

        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(meter1);

        classRef.method(ka, "measure").returning(int.class).takingNoParams().withNiceError(koodi).invoke();

        assertEquals(koodi, 4, ka.measure());

        koodi = "Error caused by code\n"
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( new ConstantSensor(4) );\n"
                + "ka.addSensor( new ConstantSensor(5) );\n"
                + "ka.addSensor( new ConstantSensor(9) );\n"
                + "ka.measure();\n";

        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).withNiceError(koodi).invoke(newConstantSensor(5));
        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).withNiceError(koodi).invoke(newConstantSensor(9));

        classRef.method(ka, "measure").returning(int.class).takingNoParams().withNiceError(koodi).invoke();
        assertEquals(koodi, 6, ka.measure());

    }

    @Test
    @Points("20.3")
    public void averageSensorOnAndOff() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Sensor meter1 = newThermometer();

        String koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( new Thermometer() );\n"
                + "ka.isOn();\n";

        Sensor ka = newAverageSensor();

        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(meter1);

        assertEquals("Because thermometer is at first set off, average sensor should also be set off\n"
                + "" + koodi, false, classRef.method(ka, "isOn").returning(boolean.class).takingNoParams().withNiceError(koodi).invoke());

        koodi = "Thermometer meter = new Thermometer();\n"
                + "meter.on();\n"
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( meter);\n"
                + "ka.isOn();\n";

        meter1 = newThermometer();
        meter1.on();
        ka = newAverageSensor();
        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(meter1);

        assertEquals("If average sensor has only one sensor it controls and it's on,"
                + " average sensor should also be on\n"
                + "" + koodi, true, classRef.method(ka, "isOn").returning(boolean.class).takingNoParams().withNiceError(koodi).invoke());

        koodi = "Thermometer meter = new Thermometer();\n"
                + "meter.on();\n"
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( meter);\n"
                + "ka.off();\n"
                + "ka.on();\n";

        meter1 = newThermometer();
        meter1.on();
        ka = newAverageSensor();
        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(meter1);
        classRef.method(ka, "off").returningVoid().takingNoParams().withNiceError(koodi).invoke();

        assertEquals(koodi, false, classRef.method(ka, "isOn").returning(boolean.class).takingNoParams().withNiceError(koodi).invoke());

        koodi =
                "Thermometer meter = new Thermometer();\n"
                + "meter.on();\n"
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( meter );\n"
                + "ka.off();\n"
                + "meter.on();\n";

        assertEquals(koodi, false, meter1.isOn());
    }

    @Test
    @Points("20.3")
    public void averageSensorOnAndOff2() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Sensor meter1 = newThermometer();
        Sensor meter2 = newThermometer();


        String koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "Thermometer meter1 = new Thermometer();\n"
                + "Thermometer meter2 = new Thermometer();\n"
                + "ka.addSensor(meter1);\n"
                + "ka.addSensor(meter2);\n"
                + "ka.on();\n";

        Sensor ka = newAverageSensor();

        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(meter1);
        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(meter2);

        assertEquals(koodi, false, classRef.method(ka, "isOn").returning(boolean.class).takingNoParams().withNiceError(koodi).invoke());

        ka.on();

        koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "Thermometer meter1 = new Thermometer();\n"
                + "Thermometer meter2 = new Thermometer();\n"
                + "ka.addSensor(meter1);\n"
                + "ka.addSensor(meter2);\n"
                + "ka.on();\n"
                + "ka.on();\n";

        assertEquals(koodi, true, ka.isOn());

        koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "Thermometer meter1 = new Thermometer();\n"
                + "Thermometer meter2 = new Thermometer();\n"
                + "ka.addSensor(meter1);\n"
                + "ka.addSensor(meter2);\n"
                + "ka.on();\n"
                + "meter1.on();\n";

        assertEquals(koodi, true, meter1.isOn());

        koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "Thermometer meter1 = new Thermometer();\n"
                + "Thermometer meter2 = new Thermometer();\n"
                + "ka.addSensor(meter1);\n"
                + "ka.addSensor(meter2);\n"
                + "ka.on();\n"
                + "meter2.on();\n";

        assertEquals(koodi, true, meter2.isOn());
    }

    @Test
    @Points("20.3")
    public void exceptionIfAverageSensorMeasuresAndItsOff() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        Sensor meter1 = newThermometer();
        Sensor meter2 = newThermometer();

        String koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "Thermometer meter1 = new Thermometer();\n"
                + "Thermometer meter2 = new Thermometer();\n"
                + "ka.addSensor( meter1);\n"
                + "ka.addSensor( meter2);\n"
                + "ka.measure();\n";

        Sensor ka = newAverageSensor();

        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(meter1);
        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(meter2);

        try {
            classRef.method(ka, "measure").returning(int.class).takingNoParams().withNiceError(koodi).invoke();
            fail("Should have thrown exception IllegalStateException() when executing\n"
                    + koodi);
        } catch (Throwable e) {
        }
    }

    /*
     *
     */
    @Test
    @Points("20.4")
    public void averageSensorMethodMeasurements() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        String k1 = "Error caused by code\n"
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.readings();\n";

        Sensor ka = newAverageSensor();

        assertTrue("Create method public List<Integer> readings() for class AverageSensor\n",
                classRef.method(ka, "readings").returning(List.class).takingNoParams().isPublic());

        classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(k1).invoke();
    }

    @Test
    @Points("20.4")
    public void averageSensorMethodMeasurementsWorks() throws Throwable {
        String klassName = "application.AverageSensor";
        Reflex.ClassRef<Object> classRef = Reflex.reflect(klassName);

        String koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( new ConstantSensor(3) );\n"
                + "ka.addSensor( new ConstantSensor(7) );\n"
                + "ka.readings();\n";

        Sensor ka = newAverageSensor();

        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(newConstantSensor(3));
        classRef.method(ka, "addSensor").returningVoid().taking(Sensor.class).invoke(newConstantSensor(7));

        assertTrue("If there are no readings, return an empty list.\n"
                + "Now it returned null with code\n" + koodi, classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(koodi).invoke() != null);
        assertTrue("Returned list should be empty with code\n" + koodi, classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(koodi).invoke().isEmpty());

        classRef.method(ka, "measure").returning(int.class).takingNoParams().withNiceError(koodi).invoke();

        koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( new ConstantSensor(3) );\n"
                + "ka.addSensor( new ConstantSensor(7) );\n"
                + "ka.measure();\n"
                + "ka.readings();\n";
        assertTrue("Returned null with code\n" + koodi, classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(koodi).invoke() != null);
        List l = classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(koodi).invoke();
        assertTrue("Returned list's size should have been 1 with code\n" + koodi+
                "\nyou returned: "+l,l.size() == 1);

        assertTrue("Returned list should have contained only the number 5 with code" + koodi+""
                + "\nyou returned: "+l, classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(koodi).invoke().get(0).equals(Integer.valueOf(5)));


        koodi = ""
                + "AverageSensor ka = new AverageSensor();\n"
                + "ka.addSensor( new ConstantSensor(3) );\n"
                + "ka.addSensor( new ConstantSensor(7) );\n"
                + "ka.measure();\n"
                + "ka.measure();\n"
                + "ka.measure();\n"
                + "ka.readings();\n";

        classRef.method(ka, "measure").returning(int.class).takingNoParams().withNiceError(koodi).invoke();
        classRef.method(ka, "measure").returning(int.class).takingNoParams().withNiceError(koodi).invoke();

        assertTrue("Returned null with code\n" + koodi, classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(koodi).invoke() != null);
        assertTrue("Returned list's size should have been 3 with code\n" + koodi+""
                + "\nyou returned: "+l, classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(koodi).invoke().size() == 3);
        l = classRef.method(ka, "readings").returning(List.class).takingNoParams().withNiceError(koodi).invoke();
        assertTrue("Returned list should have contained only number 5 three times with code" + koodi+""
                + "\nyou returned: "+l, l.get(0).equals(Integer.valueOf(5)) && l.get(1).equals(Integer.valueOf(5)) && l.get(2).equals(Integer.valueOf(5)));
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
