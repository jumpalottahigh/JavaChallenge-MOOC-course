
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.*;
import static org.junit.Assert.*;

public class PersonnelTest<_Education, _Person, _Employees> {

    Reflex.ClassRef<_Education> _EducationRef;
    Reflex.ClassRef<_Person> _PersonRef;
    Reflex.ClassRef<_Employees> _EmployeesRef;

    @Before
    public void atStart() {
        try {
            _EducationRef = Reflex.reflect("personnel.Education");
            _PersonRef = Reflex.reflect("personnel.Person");
            _EmployeesRef = Reflex.reflect("personnel.Employees");
        } catch (Throwable e) {
        }
    }

    @Test
    @Points("45.1")
    public void isEnumEducation() {
        String className = "personnel.Education";
        try {
            _EducationRef = Reflex.reflect(className);
        } catch (Throwable t) {
            fail("Create enum Education inside the package personnel");
        }
        assertTrue("Create enum Education inside the package personnel", _EducationRef.isPublic());
        Class c = _EducationRef.cls();
        assertTrue("Create enum Education inside the package personnel, now you probably made normal class", c.isEnum());
    }

    @Test
    @Points("45.1")
    public void enumRightValues() {
        String className = "personnel.Education";
        Class c = ReflectionUtils.findClass(className);
        Object[] constants = c.getEnumConstants();
        assertEquals("enum Education should have a correct number of values", 4, constants.length);

        String[] t = {"GRAD", "B", "M", "D"};

        for (String id : t) {
            assertTrue("enum Education should define value " + id, sis(id, constants));
        }
    }

    @Test
    @Points("45.2")
    public void isClassPerson() {
        String className = "personnel.Person";
        _PersonRef = Reflex.reflect(className);
        assertTrue("Create class Person inside the package personnel", _PersonRef.isPublic());
    }

    public _Person createPerson(String name, _Education k) throws Throwable {
        return _PersonRef.constructor().taking(String.class, _EducationRef.cls()).invoke(name, k);
    }

    @Test
    @Points("45.2")
    public void personHasRightConstructor() throws Throwable {
        assertTrue("Define for class Person a constructor public Person(String name, Education education)",
                _PersonRef.constructor().taking(String.class, _EducationRef.cls()).isPublic());

        String className = "personnel.Person";
        Class c = ReflectionUtils.findClass(className);

        _Education k = education("D");

        assertEquals("Calss Person should have only one constructor, now there are ", 1, c.getConstructors().length);

        _PersonRef.constructor().taking(String.class, _EducationRef.cls()).withNiceError("\nError caused by code Person(\"Arto\", Education.D); ").invoke("arto", k);
    }

    @Test
    @Points("45.2")
    public void personCorrectInstanceVariables() {
        String className = "personnel.Person";

        Class c = ReflectionUtils.findClass(className);

        boolean k = false;
        boolean n = false;
        int fc = 0;
        for (Field f : c.getDeclaredFields()) {
            String name = f.toString();
            if (name.contains("String")) {
                n = true;
                fc++;
            } else if (name.contains("Education")) {
                k = true;
                fc++;
            } else {
                fail(k + " remove " + f.getName());
            }
            assertTrue("Instance variables of class Person should be private, change " + f.getName(), name.contains("private"));
        }
        assertTrue("Class Person should have instance variable of type String", n);
        assertTrue("Class Person should have instance variable of type Education", k);
        assertTrue("Class Person should have only 2 instance variables", fc == 2);
    }

    @Test
    @Points("45.2")
    public void personToString() {
        Object person = hlo("Arto", "D");
        assertFalse("Create method toString for class Person as specified in the assignment", person.toString().contains("@"));

        assertEquals("h = new Person(\"Arto\", Education.D); \nSystem.out.print(h);\n", "Arto, D", person.toString());
    }

    @Test
    @Points("45.2")
    public void personToString2() {
        String[][] tt = {{"Pekka", "GRAD"}, {"Mikke", "B"}, {"Thomas", "M"}, {"Esko", "D"}};

        for (String[] nameSchool : tt) {
            Object person = hlo(nameSchool[0], nameSchool[1]);

            assertEquals("h = new Person(\"" + nameSchool[0] + "\", Education." + nameSchool[1] + "); \nSystem.out.print(h);\n", nameSchool[0] + ", " + nameSchool[1], person.toString());
        }

    }

    @Test
    @Points("45.2")
    public void educationGetter() {
        String metodi = "getEducation";
        String error = "Create method public Education getEducation() for class Person";

        assertTrue(error, _PersonRef.method(metodi).returning(_EducationRef.cls()).takingNoParams().isPublic());
    }

    @Test
    @Points("45.2")
    public void educationGetterWorks() throws Throwable {
        _Person hlo = createPerson("Pekka", education("D"));
        _Education vast = _PersonRef.method("getEducation").returning(_EducationRef.cls()).
                takingNoParams().withNiceError("\nError caused by code\nPerson h = new Person(\"Arto\", Education.D); \nh.getEducation();\n").invokeOn(hlo);

        assertEquals("Person h = new Person(\"Arto\", Education.D); \nh.getEducation();\n", education("D"), vast);
    }

    @Test
    @Points("45.2")
    public void educationGetterWorks2() throws Exception {
        String[][] tt = {{"Pekka", "GRAD"}, {"Mikke", "B"}, {"Thomas", "M"}, {"Esko", "D"}};

        for (String[] nameSchool : tt) {
            Object person = hlo(nameSchool[0], nameSchool[1]);

            assertEquals("h = new Person(\"" + nameSchool[0] + "\", Education." + nameSchool[1] + "); \nh.getEducation();\n", nameSchool[1], getEducation(person).toString());
        }

    }

    @Test
    @Points("45.3")
    public void isClassEmployees() {
        String className = "personnel.Employees";
        _EmployeesRef = Reflex.reflect(className);
        assertTrue("Create class Employees inside the package personnel", _EmployeesRef.isPublic());
    }

    @Test
    @Points("45.3")
    public void employeesRightConstructor() throws Throwable {
        assertTrue("Define for class Employees a constructor public Employees()",
                _EmployeesRef.constructor().takingNoParams().isPublic());

        _EmployeesRef.constructor().takingNoParams().withNiceError("\nError caused by code new Employees(); ").invoke();
    }

    private _Employees createEmployees() throws Throwable {
        return _EmployeesRef.constructor().takingNoParams().invoke();
    }

    @Test
    @Points("45.3")
    public void hasMethodAddPerson() throws Throwable {
        String metodi = "add";
        String error = "Create method public void add(Person person) for class Employees";
        _Person person = createPerson("Arto", education("D"));

        _Employees tt = createEmployees();

        assertTrue(error, _EmployeesRef.method(tt, metodi).returningVoid().taking(_PersonRef.cls()).isPublic());
        String v = "Error caused by code\n"
                + "Employees tt = new Employees();\n"
                + "tt.add( new Person(\"Arto\", education.D));";
        _EmployeesRef.method(tt, metodi).returningVoid().taking(_PersonRef.cls()).withNiceError(v).invoke(person);
    }

    private void add(_Employees tt, _Person person, String v) throws Throwable {
        _EmployeesRef.method(tt, "add").returningVoid().taking(_PersonRef.cls()).withNiceError(v).invoke(person);
    }

    private void add(_Employees tt, List h, String v) throws Throwable {
        _EmployeesRef.method(tt, "add").returningVoid().taking(List.class).withNiceError(v).invoke(h);
    }

    private void print(_Employees tt, String v) throws Throwable {
        _EmployeesRef.method(tt, "print").returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    private void print(_Employees tt, _Education k, String v) throws Throwable {
        _EmployeesRef.method(tt, "print").returningVoid().taking(_EducationRef.cls()).withNiceError(v).invoke(k);
    }

    @Test
    @Points("45.3")
    public void hasMethodAddList() throws Throwable {
        String metodi = "add";
        String error = "Create method public void add(List<Person> persons) for class Employees";

        _Person person = createPerson("Arto", education("D"));
        _Person person2 = createPerson("Pekka", education("M"));

        _Employees tt = createEmployees();

        assertTrue(error, _EmployeesRef.method(tt, metodi).returningVoid().taking(List.class).isPublic());
        String v = "Error caused by code\n"
                + "List persons = new ArrayList<Person>();\n"
                + "persons.add(new Person(\"Arto\", education.FT));\n"
                + "persons.add(new Person(\"Pekka\", education.FM));\n"
                + "Employees tt = new Employees();\n"
                + "tt.add(persons);";

        List h = new ArrayList();
        h.add(person);
        h.add(person2);
        _EmployeesRef.method(tt, metodi).returningVoid().taking(List.class).withNiceError(v).invoke(h);
    }

    /*
     *
     */
    @Test
    @Points("45.3")
    public void hasMethodPrint() throws Throwable {
        String metodi = "print";
        String error = "Create method public void print() for class Employees";

        _Employees tt = createEmployees();
        assertTrue(error, _EmployeesRef.method(tt, metodi).returningVoid().takingNoParams().isPublic());
        String v = "Error caused by code\nEmployees t = new Employees();\nt.print();\n";

        _EmployeesRef.method(tt, metodi).returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("45.3")
    public void printingWorks1() throws Throwable {
        String v = "Check code \n"
                + "t = new Employees(); \n"
                + "h = new Person(\"Arto\", Education.D); \n"
                + "t.add(h); \n"
                + "t.print(), \n"
                + "print output should contain line \"Arto, D\"\n";


        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person = createPerson("Arto", education("D"));
        add(tt, person, v);
        print(tt, v);
        String out = io.getOutput();
        assertTrue(v + "printed\n" + out, out.contains(person.toString()));
    }

    @Test
    @Points("45.3")
    public void printingWorks2() throws Throwable {
        String v = "Check code t = new Employees(); \n"
                + "h = new Person(\"Arto\", Education.D); \n"
                + "t.add(h); \n"
                + "h2 = new Person(\"Pekka\", Education.GRAD); \n"
                + "t.add(h2); \n"
                + "t.print();\n"
                + "print output should contain line \"Arto, D\"\n";
        String v2 = "Check code \n"
                + "t = new Employees();\n"
                + "h = new Person(\"Pekka\",Education.GRAD); \n"
                + "t.add(h); \n"
                + "t.print()\n"
                + "print output should contain line \"Pekka, GRAD\"\n";


        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person = createPerson("Arto", education("D"));
        _Person person2 = createPerson("Pekka", education("GRAD"));
        add(tt, person, v);
        add(tt, person2, v);
        print(tt, v);
        String out = io.getOutput();
        assertTrue(v + "printed\n" + out, out.contains(person.toString()));
        assertTrue(v2 + "printed\n" + out, out.contains(person2.toString()));
    }

    @Test
    @Points("45.3")
    public void printingWorks3() throws Throwable {
        String v = "Check code \nt = new Employees(); \n"
                + "ArrayList<Person> list = new ...; \n"
                + "list.add((\"Arto\", Education.D); \n"
                + "list.add(\"Pekka\", Education.GRAD); \n"
                + "t.add(list); \n"
                + "t.print(); \n"
                + "print output should contain line \"Arto, D\"\n"
                + "";
        String v2 = "Check code \nt = new Employees(); \n"
                + "ArrayList<Person> list = new ...; \n"
                + "list.add((\"Arto\", Education.D); \n"
                + "list.add(\"Pekka\", Education.GRAD); \n"
                + "t.add(list); \n"
                + "t.print();\n "
                + "print output should contain line \"Pekka, GRAD\"\n";


        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person = createPerson("Arto", education("D"));
        _Person person2 = createPerson("Pekka", education("GRAD"));
        ArrayList l = new ArrayList();
        l.add(person);
        l.add(person2);
        add(tt, l, v);
        print(tt, v);
        String out = io.getOutput();
        assertTrue(v + "printed\n" + out, out.contains(person.toString()));
        assertTrue(v2 + "printed\n" + out, out.contains(person2.toString()));

    }

    @Test
    @Points("45.3")
    public void noRedundantVariables() {
        Object tt = employees();
        String v = "Class Employees needs only one instance variable"
                + ", which is the list of Person-objects. Remove others";
        assertEquals(v, 1, tt.getClass().getDeclaredFields().length);

    }

    @Test
    @Points("45.3")
    public void printingUsesIterator() {
        iteratorInUse();
    }

    /*
     *
     */
    @Test
    @Points("45.3")
    public void hasMethodPrintEducation() throws Throwable {
        String metodi = "print";
        String error = "Create method public void print(Education education) for class Employees";

        _Employees tt = createEmployees();
        assertTrue(error, _EmployeesRef.method(tt, metodi).returningVoid().taking(_EducationRef.cls()).isPublic());
        String v = "Error caused by code\nEmployees t = new Employees();\nt.print(Education.GRAD);\n";

        _EmployeesRef.method(tt, metodi).returningVoid().taking(_EducationRef.cls()).withNiceError(v).invoke(education("GRAD"));
    }

    @Test
    @Points("45.3")
    public void filteredPrintingWorks1() throws Throwable {
        String v = "Check code \n"
                + "t = new Employees(); \n"
                + "h = new Person(\"Arto\", Education.D); \n"
                + "t.add(h); t.print(Education.D); \n"
                + "print output should contain line \"Arto, D\"\n";


        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person = createPerson("Arto", education("D"));
        add(tt, person, v);
        print(tt, education("D"), v);
        String out = io.getOutput();
        assertTrue(v + "printed\n" + out, out.contains(person.toString()));
    }

    @Test
    @Points("45.3")
    public void filteredPrintingWorks2() throws Throwable {
        String v = "Check code \n"
                + "t = new Employees(); \n"
                + "h = new Person(\"Arto\", Education.B); \n"
                + "t.add(h); \n"
                + "t.print(Education.D)\n "
                + "shouldn't print anything\n";


        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person = createPerson("Arto", education("D"));
        add(tt, person, v);
        print(tt, education("B"), v);
        String out = io.getOutput();
        assertFalse(v + "printed\n"+out, out.contains(person.toString()));
    }

    @Test
    @Points("45.3")
    public void filteredPrintingUsesIterator() {
        iteratorInUse2();
    }

    /*
     *
     */
    @Test
    @Points("45.4")
    public void hasMethodFire() throws Throwable {
        String metodi = "fire";
        String error = "Create method public void fire(Education education) for class Employees";

        _Employees tt = createEmployees();
        assertTrue(error, _EmployeesRef.method(tt, metodi).returningVoid().taking(_EducationRef.cls()).isPublic());
        String v = "Error caused by code\nEmployees t = new Employees();\n"
                + "t.fire(Education.D);\n";

        _EmployeesRef.method(tt, metodi).returningVoid().taking(_EducationRef.cls()).withNiceError(v).invoke(education("D"));
    }

    private void fire(_Employees tt, _Education k, String v) throws Throwable {
        _EmployeesRef.method(tt, "fire").returningVoid().taking(_EducationRef.cls()).withNiceError(v).invoke(k);

    }

    @Test
    @Points("45.4")
    public void firingWorks() throws Throwable {
        String v = "Check code \n"
                + "t = new Employees(); \n"
                + "h = new Person(\"Arto\", Education.D); \n"
                + "t.add(h); \n"
                + "t.fire(Education.D); \n"
                + "t.print()\n"
                + " shouldn't print anything\n";

        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person = createPerson("Arto", education("D"));
        add(tt, person, v);

        fire(tt, education("D"), v);

        print(tt, v);
        String out = io.getOutput();
        assertFalse(v + "printed\n"+out, out.contains(person.toString()));
    }

    @Test
    @Points("45.4")
    public void fireUsesIterator() {
        iteratorInUse3();
    }

    @Test
    @Points("45.4")
    public void firingWorks2() throws Throwable {
        String v = "Check code \nt = new Employees(); \n"
                + "h = new Person(\"Arto\", Education.D); t.add(h); \n"
                + "h = new Person(\"Pekka\", Education.B); t.add(h); \n"
                + "h = new Person(\"Matti\", Education.D); t.add(h); \n"
                + "t.fire(Education.D);\n t.print();\n. Only Pekka should be printed\n";

        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person1 = createPerson("Arto", education("D"));
        add(tt, person1, v);
        _Person person2 = createPerson("Pekka", education("B"));
        add(tt, person2, v);
        _Person person3 = createPerson("Matti", education("D"));
        add(tt, person3, v);

        fire(tt, education("D"), v);


        print(tt, v);
        String out = io.getOutput();
        assertFalse(v+ "printed\n"+out, out.contains(person1.toString()));
        assertTrue(v+ "printed\n"+out, out.contains(person2.toString()));
        assertFalse(v+ "printed\n"+out, out.contains(person3.toString()));
    }

    @Test
    @Points("45.4")
    public void firingWorks3() throws Throwable {
        String v = "Check code \n"
                + "t = new Employees(); \n"
                + "h = new Person(\"Arto\", Education.D); t.add(h); \n"
                + "h = new Person(\"Pekka\", Education.B); t.add(h); \n"
                + "h = new Person(\"Matti\", Education.D); t.add(h); \n"
                + "t.fire(Education.M);\n t.print();\n Everyone should be printed\n";

        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person1 = createPerson("Arto", education("D"));
        add(tt, person1, v);
        _Person person2 = createPerson("Pekka", education("B"));
        add(tt, person2, v);
        _Person person3 = createPerson("Matti", education("D"));
        add(tt, person3, v);

        fire(tt, education("M"), v);

        print(tt, v);
        String out = io.getOutput();
        assertTrue(v+ "printed\n"+out, out.contains(person1.toString()));
        assertTrue(v+ "printed\n"+out, out.contains(person2.toString()));
        assertTrue(v+ "printed\n"+out, out.contains(person3.toString()));
    }

    @Test
    @Points("45.4")
    public void firingWorks4() throws Throwable {
        String v = "Check code \n"
                + "t = new Employees(); \n"
                + "h = new Person(\"Arto\", Education.D); t.add(h); \n"
                + "h = new Person(\"Pekka\", Education.B); t.add(h); \n"
                + "h = new Person(\"Matti\", Education.D); t.add(h); \n"
                + "t.fire(Education.B);\n "
                + "t.print(); \n"
                + "Arto and Matti should be printed\n";

        MockInOut io = new MockInOut("");
        _Employees tt = createEmployees();
        _Person person1 = createPerson("Arto", education("D"));
        add(tt, person1, v);
        _Person person2 = createPerson("Pekka", education("B"));
        add(tt, person2, v);
        _Person person3 = createPerson("Matti", education("D"));
        add(tt, person3, v);

        fire(tt, education("B"), v);


        print(tt, v);
        String out = io.getOutput();
        assertTrue(v+ "printed\n"+out, out.contains(person1.toString()));
        assertFalse(v+ "printed\n"+out, out.contains(person2.toString()));
        assertTrue(v+ "printed\n"+out, out.contains(person3.toString()));
    }
    /*
     *
     */

    private Object getEducation(Object olio) throws Exception {
        String metodi = "getEducation";
        String error = "Create method public Education getEducation() for class Person";

        Object k = education("D");
        Class c = ReflectionUtils.findClass("personnel.Person");
        Method m = ReflectionUtils.requireMethod(c, k.getClass(), metodi);

        return m.invoke(olio);
    }

    private Object employees() {
        Class c = ReflectionUtils.findClass("personnel.Employees");

        Constructor ctor = ReflectionUtils.requireConstructor(c);
        try {
            return ctor.newInstance();
        } catch (Throwable e) {
            fail("new Employees(); led to an error, additional information: " + e);
        }
        return null;
    }

    private Object hlo(String n, String ktus) {
        Class c = ReflectionUtils.findClass("personnel.Person");
        String name = n;
        Object k = education(ktus);
        Constructor ctor = c.getConstructors()[0];
        try {
            return ctor.newInstance(n, k);
        } catch (Throwable e) {
            fail("new Person(\"Arto\", Education.D); led to an error, additional information: " + e);
        }
        return null;
    }

    private _Education education(String e) {
        String className = "personnel.Education";
        Class c = ReflectionUtils.findClass(className);
        Object[] constants = c.getEnumConstants();

        for (Object constant : constants) {
            if (constant.toString().equals(e)) {
                return (_Education) constant;
            }
        }

        return null;
    }

    private boolean sis(String id, Object[] constants) {
        for (Object constant : constants) {
            if (constant.toString().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void iteratorInUse() {
        try {
            Scanner reader = new Scanner(new File("src/personnel/Employees.java"));
            int metodissa = 0;

            int iterator = 0;
            while (reader.hasNext()) {

                String line = reader.nextLine();

                if (line.indexOf("//") > -1) {
                    line = line.substring(0, line.indexOf("//"));
                }

                if (line.contains("void") && line.contains("print")) {
                    metodissa++;

                } else if (metodissa > 0) {
                    if (line.contains("{") && !line.contains("}")) {
                        metodissa++;
                    }

                    if (line.contains("}") && !line.contains("{")) {
                        metodissa--;
                    }

                    if (line.contains("Iterator") && line.contains("<Person>")) {
                        iterator++;
                    }
                    if (line.contains(".hasNext(")) {
                        iterator++;
                    }
                    if (line.contains(".next(")) {
                        iterator++;
                    }

                }

            }
            assertTrue("Method print() of class Employees should go through the list with the help of an iterator"
                    + ", check out the material for instructions!", iterator > 2);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void iteratorInUse2() {
        try {
            Scanner reader = new Scanner(new File("src/personnel/Employees.java"));
            int metodissa = 0;

            int iterator = 0;
            while (reader.hasNext()) {

                String line = reader.nextLine();

                if (line.indexOf("//") > -1) {
                    line = line.substring(0, line.indexOf("//"));
                }

                if (line.contains("void") && line.contains("print") && line.contains("Education")) {
                    metodissa++;
                } else if (metodissa > 0) {
                    if (line.contains("{") && !line.contains("}")) {
                        metodissa++;
                    }

                    if (line.contains("}") && !line.contains("{")) {
                        metodissa--;
                    }

                    if (line.contains("Iterator") && line.contains("<Person>")) {
                        iterator++;
                    }
                    if (line.contains(".hasNext(")) {
                        iterator++;
                    }
                    if (line.contains(".next(")) {
                        iterator++;
                    }

                }

            }
            assertTrue("Method print(Education education) of class Employees should go through the list with the help of an iterator"
                    + ", check out the material for instructions!", iterator > 2);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void iteratorInUse3() {
        try {
            Scanner reader = new Scanner(new File("src/personnel/Employees.java"));
            int metodissa = 0;

            int iterator = 0;
            while (reader.hasNext()) {

                String line = reader.nextLine();

                if (line.indexOf("//") > -1) {
                    line = line.substring(0, line.indexOf("//"));
                }

                if (line.contains("void") && line.contains("fire")) {
                    metodissa++;
                } else if (metodissa > 0) {
                    if (line.contains("{") && !line.contains("}")) {
                        metodissa++;
                    }

                    if (line.contains("}") && !line.contains("{")) {
                        metodissa--;
                    }

                    if (line.contains("Iterator") && line.contains("<Person>")) {
                        iterator++;
                    }

                    if (line.contains(".hasNext(")) {
                        iterator++;
                    }

                    if (line.contains(".next(")) {
                        iterator++;
                    }

                    if (line.contains("get(")) {
                        fail("Method fire() of class Employees should go through the list with the help"
                                + " of an iterator. When you use iterator, there won't be any problems if"
                                + " you need to remove someone from the list while going through it."
                                + "\nCheck out the material for help.");
                    }

                }

            }

            assertTrue("Method fire() of class Employees should go through the list with the help of an iterator."
                    + " When you use iterator, there won't be any problems if you need to remove someone"
                    + " from the list while going through it.\nCheck out the material for help.", iterator > 2);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
