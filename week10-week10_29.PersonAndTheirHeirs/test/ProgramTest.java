
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class ProgramTest {

    String henkiloLuokanNimi = "people.Person";
    String opiskelijaLuokanNimi = "people.Student";
    String opettajaLuokanNimi = "people.Teacher";

    @Test
    @Points("29.1")
    public void classPerson() {
        String klassName = henkiloLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("29.1")
    public void personNoRedundantVariables() {
        String klassName = henkiloLuokanNimi;
        saniteettitarkastus(klassName, 2, "instance variables for name and address");
    }

    @Test
    @Points("29.1")
    public void personTwoParameterConstructor() throws Throwable {
        newPerson("Pekka", "Mannerheimintie");
    }

    @Test
    @Points("29.1")
    public void personToString() throws Throwable {
        Object h = newPerson("Pekka", "Mannerheimintie");

        assertFalse("Create method toString to class Person", h.toString().contains("@"));
    }

    /*
     *
     */
    @Test
    @Points("29.1")
    public void personToStringOK() throws Throwable {
        Object h = newPerson("Pekka", "Mannerheimintie");

        String[] aa = h.toString().split("\n");

        assertEquals("Check that Person's toString returns same string as specified in the assignment\n"
                + "does your toString-method's produced string have a line break, i.e. \\n-chracter? lines: ", 2, h.toString().split("\n").length);

        assertTrue("Check that Person's toString returns same string as specified in the assignment\n"
                + "second line should start with two whitespace characters!", h.toString().split("\n")[1].startsWith("  "));
        assertFalse("Check that Person's toString returns same string as specified in the assignment\n"
                + "second line should start with two whitespace characters!", h.toString().split("\n")[1].startsWith("   "));

        assertTrue("Check that Person's toString returns same string as specified in the assignment\n"
                + "do not put any extra characters in the end of the line", h.toString().split("\n")[0].endsWith("a"));
        assertTrue("Check that Person's toString returns same string as specified in the assignment\n"
                + "do not put any extra characters in the end of the line", h.toString().split("\n")[1].endsWith("e"));


        String[] hlot = {"Pekka Mikkola;Mannerheimintie", "Arto Vihavainen;Herttoniemenranta;",
            "Esko Ukkonen;Westend"};

        for (String hlo : hlot) {
            String[] nameOs = hlo.split(";");
            Object hh = newPerson(nameOs[0], nameOs[1]);
            assertTrue("Check that Person's toString returns same string as specified in the assignment", hh.toString().contains(nameOs[0]));
            assertTrue("Check that Person's toString returns same string as specified in the assignment", hh.toString().contains(nameOs[1]));
        }

    }

    //********************
    @Test
    @Points("29.2")
    public void classStudent() {
        String klassName = opiskelijaLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("29.2")
    public void studentNoRedundantVariables() {
        String klassName = opiskelijaLuokanNimi;
        saniteettitarkastus(klassName, 1, "instance variables for credits\n"
                + "Name and address have been inherited from superclass. They can be used the same way as "
                + "in the example where Motor extends Component!");
    }

    @Test
    @Points("29.2")
    public void studentExtendsPerson() {
        Class c = ReflectionUtils.findClass(opiskelijaLuokanNimi);

        Class sc = c.getSuperclass();
        assertTrue("Class Student should extend class Person", sc.getName().equals(henkiloLuokanNimi));
    }

    @Test
    @Points("29.2")
    public void studentTwoParameterConstructor() throws Throwable {
        newStudent("Olli", "Ida Albergintie 1");
    }

    @Test
    @Points("29.2")
    public void studentHasMethodCredits() throws Throwable {
        String metodi = "credits";
        String virhe = "Create method public int credits() for class Student";

        String klassName = opiskelijaLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        Object o = newStudent("Pekka", "Korso");

        assertTrue(virhe,
                classRef.method(o, metodi).returning(int.class).takingNoParams().isPublic());

        String v = "Student o = new Student(\"Pekka\",\"Korso\");\n"
                + "o.credits();\n";

        assertEquals(0, (int) classRef.method(o, metodi)
                .returning(int.class)
                .takingNoParams().withNiceError("error caused by code\n" + v).invoke());
    }

    @Test
    @Points("29.2")
    public void studentHasMethodStudy() throws Throwable {
        String metodi = "study";
        String virhe = "Create method public void study() for class Student";

        String klassName = opiskelijaLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        Object o = newStudent("Pekka", "Korso");

        assertTrue(virhe,
                classRef.method(o, metodi).returningVoid().takingNoParams().isPublic());

        String v = "Student o = new Student(\"Pekka\",\"Korso\");\n"
                + "o.study();\n";
        classRef.method(o, metodi).returningVoid().takingNoParams().withNiceError("error caused by code\n" + v).invoke();
    }

    @Test
    @Points("29.2")
    public void creditsIncrease() throws Throwable {
        String k = "Student op = new Student(\"Olli\", \"Ida Algergintie 1\"); op.credits(); ";
        Object op = newStudent("Olli", "Ida Albergintie 1");
        assertEquals("Test code " + k, 0, credits(op));

        k = "Student op = new Student(\"Olli\", \"Ida Algergintie 1\"); op.study(); op.credits(); ";
        study(op);
        assertEquals("Test code " + k, 1, credits(op));

        k = "Student op = new Student(\"Olli\", \"Ida Algergintie 1\"); op.study(); op.study(); op.study(); op.credits(); ";
        study(op);
        study(op);
        assertEquals("Test code " + k, 3, credits(op));
    }

    // **************
    @Test
    @Points("29.3")
    public void studentToStringOK() throws Throwable {
        Object h = newStudent("Olli", "Ida Albergintie 1");
        assertEquals("Check that Student's method toString produces a string as specified in the assignment\n"
                + "does your toString-method's returned string have 2 line breaks, i.e. 2 \\n-characters? lines: ", 3, h.toString().split("\n").length);

        assertTrue("Check that Student's method toString produces a string as specified in the assignment\n"
                + "second line should start with two whitespace characters!", h.toString().split("\n")[1].startsWith("  "));
        assertFalse("Check that Student's method toString produces a string as specified in the assignment\n"
                + "second line should start with two whitespace characters!", h.toString().split("\n")[1].startsWith("   "));
        assertTrue("Check that Student's method toString produces a string as specified in the assignment\n"
                + "third line should start with two whitespace characters!", h.toString().split("\n")[2].startsWith("  "));
        assertFalse("Check that Student's method toString produces a string as specified in the assignment\n"
                + "third line should start with two whitespace characters!", h.toString().split("\n")[2].startsWith("   "));

        String k = "Student op = new Student(\"Olli\",\"Ida Albergintie 1\"); System.out.print(op)";

        String aa = h.toString();

        assertTrue("Check that Student's method toString produces a string as specified in the assignment\n"
                + "check code " + k, h.toString().contains("Olli"));
        assertTrue("Check that Student's method toString produces a string as specified in the assignment\n"
                + "check code " + k, h.toString().contains("Ida Albergintie 1"));
        assertTrue("Check that Student's method toString produces a string as specified in the assignment\n"
                + "check code " + k, h.toString().contains("credits 0"));

        k = "Student op = new Student(\"Olli\",\"Ida Albergintie 1\"); op.study(); op.study(); System.out.print(op)";

        study(h);
        study(h);

        assertTrue("Check that Student's method toString produces a string as specified in the assignment\n"
                + "check code " + k, h.toString().contains("credits 2"));
    }

    // **************
    @Test
    @Points("29.4")
    public void classTeacher() {
        String klassName = opettajaLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Class " + s(klassName) + " should be public, so it must be defined as\n"
                + "public class " + klassName + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("29.4")
    public void teacherNoRedundantVariables() {
        String klassName = opettajaLuokanNimi;
        saniteettitarkastus(klassName, 1, "an instance variable for salary\n"
                + "Name and address have been inherited from superclass.");
    }

    @Test
    @Points("29.4")
    public void teacherExtendsPerson() {
        Class c = ReflectionUtils.findClass(opettajaLuokanNimi);

        Class sc = c.getSuperclass();
        assertTrue("Class Teacher should extend class Person", sc.getName().equals(henkiloLuokanNimi));
    }

    @Test
    @Points("29.4")
    public void teacherThreeParameterConstructor() throws Throwable {
        newTeacher("Joel Kaasinen", "Haagantie 123", 980);
    }

    @Test
    @Points("29.4")
    public void teacherToStringOK() throws Throwable {
        Object h = newTeacher("Joel Kaasinen", "Haagantie 123", 980);
        assertEquals("Check that Teacher's toString returns same string as specified in the assignment\n"
                + "does your toString-method's returned string have 2 line breaks, i.e. 2 \\n-characters? \\n-merkkiä? lines: ", 3, h.toString().split("\n").length);

        assertTrue("Check that Teacher's toString returns same string as specified in the assignment\n"
                + "second line should start with two whitespace characters!", h.toString().split("\n")[1].startsWith("  "));
        assertFalse("Check that Teacher's toString returns same string as specified in the assignment\n"
                + "second line should start with two whitespace characters!", h.toString().split("\n")[1].startsWith("   "));
        assertTrue("Check that Teacher's toString returns same string as specified in the assignment\n"
                + "third line should start with two whitespace characters!", h.toString().split("\n")[2].startsWith("  "));
        assertFalse("Check that Teacher's toString returns same string as specified in the assignment\n"
                + "third line should start with two whitespace characters!", h.toString().split("\n")[2].startsWith("   "));

        String k = "Teacher op = new Teacher(\"Joel Kaasinen\",\"Haagantie 123\",980); System.out.print(op)";

        String aa = h.toString();

        assertTrue("Check that Teacher's toString returns same string as specified in the assignment\n"
                + "check code " + k, h.toString().contains("Joel Kaasinen"));
        assertTrue("Check that Teacher's toString returns same string as specified in the assignment\n"
                + "check code " + k, h.toString().contains("Haagantie 123"));
        assertTrue("Check that Teacher's toString returns same string as specified in the assignment\n"
                + "with code " + k + " should be printed line which reads   salary 980 euros/month", h.toString().contains("salary 980 euros/month"));
    }
    // **************

    @Test
    @Points("29.5")
    public void printDepartmentMethodMain() throws Throwable {
        String klassName = "Main";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);
        String metodi = "printDepartment";

        String virhe = "Create method public static void printDepartment(List<Person> people) for class Main";

        assertTrue(virhe,
                classRef.staticMethod(metodi).returningVoid().taking(List.class).isPublic());

        String v = "List<Person> lista = new ArrayList<Person>();\n"
                + "lista.add( new Student(\"Pekka\",\"Korso\") );\n"
                + "Main.printDepartment(lista)\n";

        List lista = new ArrayList();
        lista.add(newStudent("Pekka", "Korso"));

        classRef.staticMethod(metodi).returningVoid().taking(List.class).withNiceError(v).invoke(lista);
    }

    public void tulosta(List lista) throws Throwable {
        String klassName = "Main";
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);
        String metodi = "printDepartment";

        classRef.staticMethod(metodi).returningVoid().taking(List.class).withNiceError().invoke(lista);
    }

    @Test
    @Points("29.5")
    public void printDepartmentWorks1() throws Exception, Throwable {
        MockInOut io = new MockInOut("");

        List a = new ArrayList();
        a.add(newStudent("Olli", "Ida Albergintie 1"));
        a.add(newTeacher("Mikael Nousiainen", "Leppavaara", 3850));
        tulosta(a);
        String output = io.getOutput();
        String v = "Does the method printDepartment work correctly? \n"
                + "Added to list new Student((\"Olli\", \"Ida Albergintie 1\") ja new Teacher(\"Mikael Nousiainen\", \"Leppavaara\", 3850) \n"
                + "and called method, it printed: " + output;

        assertTrue(v, output.contains("Olli"));
        assertTrue(v, output.contains("Ida Albergintie 1"));
        assertTrue(v, output.contains("Leppavaara"));
        assertTrue(v, output.contains("Mikael Nousiainen"));
        assertFalse(v, output.contains("Oskari"));
        assertFalse(v, output.contains("Domus Academica"));
        assertFalse(v, output.contains("Korso"));
        assertFalse(v, output.contains("Pekka Mikkola"));
    }

    @Test
    @Points("29.5")
    public void printDepartmentWorks2() throws Exception, Throwable {
        MockInOut io = new MockInOut("");
        List a = new ArrayList();
        a.add(newStudent("Oskari", "Domus Academica"));
        a.add(newTeacher("Pekka Mikkola", "Korso", 1105));
        tulosta(a);
        String output = io.getOutput();
        String v = "Does the method printDepartment work correctly?\n"
                + " Added to list new Student(\"Oskari\", \"Domus Academica\") ja "
                + "new Teacher(\"Pekka Mikkola\", \"Korso\", 1105) \nand called method, it printed: " + output;

        assertTrue(v, output.contains("Oskari"));
        assertTrue(v, output.contains("Domus Academica"));
        assertTrue(v, output.contains("Korso"));
        assertTrue(v, output.contains("Pekka Mikkola"));
        assertFalse(v, output.contains("Olli"));
        assertFalse(v, output.contains("Ida Albergintie 1"));
        assertFalse(v, output.contains("Leppavaara"));
        assertFalse(v, output.contains("Mikael Nousiainen"));
    }

    /*
     *
     */
    private Object newPerson(String name, String address) throws Throwable {
        String klassName = henkiloLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Define for class Person a constructor public Person(String name, String address)", classRef.constructor().taking(String.class, String.class).isPublic());

        Reflex.MethodRef2<Object, Object, String, String> ctor = classRef.constructor().taking(String.class, String.class).withNiceError();
        return ctor.invoke(name, address);
    }

    private Object newStudent(String name, String address) throws Throwable {
        String klassName = opiskelijaLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Define for class Student a constructor public Student(String name, String address)", classRef.constructor().taking(String.class, String.class).isPublic());

        Reflex.MethodRef2<Object, Object, String, String> ctor = classRef.constructor().taking(String.class, String.class).withNiceError();
        return ctor.invoke(name, address);

    }

    private Object newTeacher(String name, String address, int salary) throws Throwable {
        String klassName = opettajaLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        assertTrue("Define for class Teacher a constructor public Teacher(String name, String address, int salary)",
                classRef.constructor().taking(String.class, String.class, int.class).isPublic());

        Reflex.MethodRef3<Object, Object, String, String, Integer> ctor = classRef
                .constructor().taking(String.class, String.class, int.class).withNiceError();
        return ctor.invoke(name, address, salary);
    }

    private void study(Object olio) throws Throwable {
        String klassName = opiskelijaLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);
        classRef.method(olio, "study").returningVoid().takingNoParams().invoke();
    }

    private int credits(Object olio) throws Throwable {
        String klassName = opiskelijaLuokanNimi;
        Reflex.ClassRef<Object> classRef;
        classRef = Reflex.reflect(klassName);

        return classRef.method(olio, "credits").returning(int.class).takingNoParams().invoke();
    }

    private String toS(String[] nameOs) {
        return nameOs[0] + "\n  " + nameOs[1];
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
