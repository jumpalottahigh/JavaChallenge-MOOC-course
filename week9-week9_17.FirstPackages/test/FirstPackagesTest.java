
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class FirstPackagesTest {

    @Test
    @Points("17.1")
    public void hasUserInterface() {
        Class clazz = null;
        try {
            clazz = ReflectionUtils.findClass("mooc.ui.UserInterface");
        } catch (Throwable e) {
            fail("Create package mooc.ui and inside it, create interface UserInterface:\n\n"
                    + "public interface UserInterface{\n  \\\\...\n}");
        }

        if (clazz == null) {
            fail("Have you created public interface UserInterface inside the package mooc.ui?");
        }

        if (!clazz.isInterface()) {
            fail("Are you sure that UserInterface is an interface?");
        }


        boolean loytyi = false;
        for (Method m : clazz.getMethods()) {
            if (!m.getReturnType().equals(void.class)) {
                continue;
            }

            if (!m.getName().equals("update")) {
                continue;
            }

            loytyi = true;
        }

        if (!loytyi) {
            fail("Does the interface UserInterface have method void update()?");
        }
    }

    /*
     *
     */
    @Test
    @Points("17.2")
    public void hasTextUserInterface() {
        Class clazz = ReflectionUtils.findClass("mooc.ui.TextUserInterface");

        if (clazz == null) {
            fail("Have you created public class TextUserInterface inside the package mooc.ui?");
        }

        Class ui = ReflectionUtils.findClass("mooc.ui.UserInterface");
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(ui)) {
                return;
            }
        }

        fail("Does the class TextUserInterface implement interface UserInterface?");
    }

    @Test
    @Points("17.2")
    public void textUserInterfacePrints() {
        Class clazz = ReflectionUtils.findClass("mooc.ui.TextUserInterface");
        Object ui = luoTextUserInterface();

        MockInOut io = new MockInOut("");

        Method m = ReflectionUtils.requireMethod(clazz, "update");
        try {
            m.invoke(ui);
        } catch (Throwable t) {
            fail("There was an error when executing code. Check what happens when you run code:\n"
                    + "UserInterface ui = new TextUserInterface();\n"
                    + "ui.update()\n");
        }

        Assert.assertTrue("Does the method update() of class TextUserInterface print something? \n"
                + "Now it printed:\n" + io.getOutput(), io.getOutput() != null && io.getOutput().length() > 1);
        Assert.assertTrue("TextUserInterface's method update should print linebreak \n"
                + "Now it printed:\n" + io.getOutput(), io.getOutput() != null && io.getOutput().contains("\n"));
        Assert.assertTrue("TextUserInterface's method update should print only one line. \n"
                + "Now it printed:\n" + io.getOutput(), io.getOutput() != null && io.getOutput().split("\n").length == 1);
    }

    /*
     *
     */
    @Test
    @Points("17.3")
    public void hasApplicationLogic() {
        Class sovellusLogiikkaClass = ReflectionUtils.findClass("mooc.logic.ApplicationLogic");

        if (sovellusLogiikkaClass == null) {
            fail("Have you created public class ApplicationLogic inside the package mooc.logic?");
        }
        Class uiClass = ReflectionUtils.findClass("mooc.ui.UserInterface");

        Constructor constructor = ReflectionUtils.requireConstructor(sovellusLogiikkaClass, uiClass);
        if (constructor == null) {
            fail("Does the class ApplicationLogic have constructor public ApplicationLogic(UserInterface ui)?");
        }

        Method m = null;

        try {
            m = ReflectionUtils.requireMethod(sovellusLogiikkaClass, "execute", int.class);
        } catch (Throwable t) {
            fail("Create method public void execute(int howManyTimes) for class ApplicationLogic");
        }

        assertTrue("Create method public void execute(int howManyTimes) for class ApplicationLogic", m.toString().contains("public"));
    }

    @Test
    @Points("17.3")
    public void noRedundantVariables() {
        saniteettitarkastus("mooc.logic.ApplicationLogic", 1, "an instance variable of type UserInterface");
    }

    @Test
    @Points("17.3")
    public void createApplicationLogicObject() throws IllegalArgumentException, IllegalAccessException {
        Class sovellusLogiikkaClass = ReflectionUtils.findClass("mooc.logic.ApplicationLogic");
        Object sovelluslogic = luoSovellusLogiikkaOlio();
        if (sovelluslogic == null) {
            fail("Does the class ApplicationLogic have constructor public ApplicationLogic(UserInterface ui)?");
        }

        Method executeMethod = ReflectionUtils.requireMethod(sovellusLogiikkaClass, "execute", int.class);

        Class clazz = ReflectionUtils.findClass("mooc.ui.TextUserInterface");
        Object ui = luoTextUserInterface();

        MockInOut io = new MockInOut("");

        Method m = ReflectionUtils.requireMethod(clazz, "update");
        try {
            m.invoke(ui);
        } catch (Throwable t) {
            fail("There was an error when executing the code. Check what happens when you run code:\n"
                    + "UserInterface ui = new TextUserInterface();\n"
                    + "ui.update()\n");
        }

        Assert.assertTrue("Does the method update() of class TextUserInterface print something?", io.getOutput() != null && io.getOutput().length() > 5);
        String output = io.getOutput();

        Field[] kentat = ReflectionUtils.findClass("mooc.logic.ApplicationLogic").getDeclaredFields();
        assertTrue("Does the class ApplicationLogic have an instance variable of type UserInterface?", kentat.length == 1);
        assertTrue("Does the class ApplicationLogic have an instance variable of type UserInterface?", kentat[0].toString().contains("UserInterface"));
        kentat[0].setAccessible(true);
        assertFalse("ApplicationLogic's instance variable " + kentat[0].toString().replace("mooc.logic.ApplicationLogic.", "") + " "
                + "null. \nSet the UserInterface-object given as a parameter in the constructor as this instance variable's value!", kentat[0].get(sovelluslogic) == null);

        try {
            executeMethod.invoke(sovelluslogic, 3);
        } catch (Throwable t) {
            fail("There was an error when executing ApplicationLogic. Check what happens when you run code:\n"
                    + "UserInterface ui = new TextUserInterface();\n"
                    + "ApplicationLogic app = new ApplicationLogic(ui);\n"
                    + "app.execute(3);");
        }

        String executeOutput = io.getOutput().substring(output.length());

        assertTrue("With code\n"
                + "UserInterface ui = new TextUserInterface();\n"
                + "ApplicationLogic app = new ApplicationLogic(ui);\n"
                + "app.execute(3);\n"
                + "Program should print 6 lines\nPrint output was\n" + executeOutput, executeOutput.split("\n").length > 5 && executeOutput.split("\n").length < 8);

        if (executeOutput.length() < output.length() * 3) {
            fail("Is the method update() of interface UserInterface called as many times as the parameter howManyTimes tells to in the method execute of class ApplicationLogic?");

        }
    }

    @Test
    @Points("17.3")
    public void anotherApplication() throws IllegalArgumentException, IllegalAccessException {
        Class sovellusLogiikkaClass = ReflectionUtils.findClass("mooc.logic.ApplicationLogic");
        Object sovelluslogic = luoSovellusLogiikkaOlio();
        if (sovelluslogic == null) {
            fail("Does the class ApplicationLogic have constructor public ApplicationLogic(UserInterface ui)?");
        }

        Method executeMethod = ReflectionUtils.requireMethod(sovellusLogiikkaClass, "execute", int.class);

        Class clazz = ReflectionUtils.findClass("mooc.ui.TextUserInterface");
        Object ui = luoTextUserInterface();

        MockInOut io = new MockInOut("");

        Method m = ReflectionUtils.requireMethod(clazz, "update");
        try {
            m.invoke(ui);
        } catch (Throwable t) {
            fail("There was an error when executing the code. Check what happens when you run code:\n"
                    + "UserInterface ui = new TextUserInterface();\n"
                    + "ui.update()\n");
        }

        Assert.assertTrue("Does the method update() of class TextUserInterface print something?", io.getOutput() != null && io.getOutput().length() > 5);
        String output = io.getOutput();

        Field[] kentat = ReflectionUtils.findClass("mooc.logic.ApplicationLogic").getDeclaredFields();
        assertTrue("Does the class ApplicationLogic have an instance variable of type UserInterface?", kentat.length == 1);
        assertTrue("Does the class ApplicationLogic have an instance variable of type UserInterface?", kentat[0].toString().contains("UserInterface"));
        kentat[0].setAccessible(true);
        assertFalse("ApplicationLogic's instance variable " + kentat[0].toString().replace("mooc.logic.ApplicationLogic.", "") + " "
                + "null. \nSet the UserInterface-object given as a parameter in the constructor as this instance variable's value!", kentat[0].get(sovelluslogic) == null);

        try {
            executeMethod.invoke(sovelluslogic, 5);
        } catch (Throwable t) {
            fail("There was an error when executing ApplicationLogic. Check what happens when you run code:\n"
                    + "UserInterface ui = new TextUserInterface();\n"
                    + "ApplicationLogic app = new ApplicationLogic(ui);\n"
                    + "app.execute(3);");
        }

        String executeOutput = io.getOutput().substring(output.length());

        assertTrue("Code\n"
                + "UserInterface ui = new TextUserInterface();\n"
                + "ApplicationLogic app = new ApplicationLogic(ui);\n"
                + "app.execute(5);\n"
                + "Should print 10 lines\nPrint output was\n" + executeOutput, executeOutput.split("\n").length > 9 && executeOutput.split("\n").length < 12);
    }

    @Test
    @Points("17.3")
    public void applicationLogicCallsUserInterface() {
        Field[] kentat = ReflectionUtils.findClass("mooc.logic.ApplicationLogic").getDeclaredFields();
        String muuttuja = kentat[0].toString();
        muuttuja = muuttuja.substring(muuttuja.lastIndexOf(".") + 1);
        assertTrue("ApplicationLogic's method execute must call userinterface's method update!", sisaltaaKutsun(muuttuja + ".update()"));
    }

    private boolean sisaltaaKutsun(String kutsu) {

        try {
            Scanner lukija = new Scanner(new File("src/mooc/logic/ApplicationLogic.java"));
            int metodissa = 0;

            while (lukija.hasNextLine()) {

                String rivi = lukija.nextLine();

                if (rivi.indexOf("//") > -1) {
                    continue;
                }

                if (metodissa > 0 && rivi.contains(kutsu)) {
                    return true;
                }

                if (rivi.contains("void") && rivi.contains("execute")) {
                    metodissa++;
                } else if (metodissa > 0) {
                    if (rivi.contains("{") && !rivi.contains("}")) {
                        metodissa++;
                    }

                    if (rivi.contains("}") && !rivi.contains("{")) {
                        metodissa--;
                    }
                }

            }

        } catch (Exception e) {
            fail(e.getMessage());
        }

        return false;
    }

    private Object luoTextUserInterface() {
        Class clazz = ReflectionUtils.findClass("mooc.ui.TextUserInterface");
        Object ui = null;
        try {
            ui = ReflectionUtils.invokeConstructor(ReflectionUtils.requireConstructor(clazz));
        } catch (Throwable ex) {
        }

        if (ui == null) {
            fail("Does the class TextUserInterface have a constructor which takes no parameters?");
        }

        return ui;
    }

    private Object luoSovellusLogiikkaOlio() {
        Class sovellusLogiikkaClass = ReflectionUtils.findClass("mooc.logic.ApplicationLogic");

        if (sovellusLogiikkaClass == null) {
            fail("Have you created public class ApplicationLogic inside the package mooc.logic?");
        }
        Class uiClass = ReflectionUtils.findClass("mooc.ui.UserInterface");

        Constructor constructor = ReflectionUtils.requireConstructor(sovellusLogiikkaClass, uiClass);

        try {
            return ReflectionUtils.invokeConstructor(constructor, luoTextUserInterface());
        } catch (Throwable ex) {
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
}
