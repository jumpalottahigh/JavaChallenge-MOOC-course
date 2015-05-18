
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.*;
import static org.junit.Assert.*;

public class ValidationTest {

    Class personLuokka;
    Constructor personConstructor;
    Class calculatorLuokka;
    Constructor calculatorConstructor;
    Method multiplicationMethod;
    Method binomialCoefficientMethod;

    @Before
    public void setup() {
        try {
            personLuokka = ReflectionUtils.findClass("validation.Person");
            personConstructor = ReflectionUtils.requireConstructor(personLuokka, String.class, int.class);
            calculatorLuokka = ReflectionUtils.findClass("validation.Calculator");
            calculatorConstructor = ReflectionUtils.requireConstructor(calculatorLuokka);
            multiplicationMethod = ReflectionUtils.requireMethod(calculatorLuokka, "multiplication", int.class);
            binomialCoefficientMethod = ReflectionUtils.requireMethod(calculatorLuokka, "binomialCoefficient", int.class, int.class);
        } catch (Throwable t) {
        }
    }

    @Test
    @Points("19.1")
    public void personClass() {
        if (personLuokka == null) {
            fail("Have you created class public Person inside the package validation?");
        }

        if (personConstructor == null) {
            fail("Does the class Person have constructor public Person(String name, int age)?");
        }

        for (int i = 0; i <= 120; i++) {
            try {
                luoPerson("mikael", i);
            } catch (IllegalArgumentException e) {
                fail("Creating person with age " + i + " and name \"mikael\" failed. Ages between 0 and 120 should be ok.");
            }
        }

        try {
            luoPerson("mikael", -5);
            fail("Person with negative age was created successfully. Constructor of class Person should throw exception IllegalArgumentException if age is not between 0-120.");
        } catch (IllegalArgumentException e) {
        }

        try {
            luoPerson("mikael", -1);
            fail("Person with negative age was created successfully. Constructor of class Person should throw exception IllegalArgumentException if age is not between 0-120.");
        } catch (IllegalArgumentException e) {
        }

        try {
            luoPerson("mikael", 121);
            fail("121-year-old person was created successfully. Constructor of class Person should throw exception IllegalArgumentException if age is not between 0-120.");
        } catch (IllegalArgumentException e) {
        }

        try {
            luoPerson("mikael", 130);
            fail("130-year-old person was created successfully. Constructor of class Person should throw exception IllegalArgumentException if age is not between 0-120.");
        } catch (IllegalArgumentException e) {
        }

        try {
            luoPerson("", 30);
            fail("Person with empty name was created successfully. Constructor of class Person should throw exception IllegalArgumentException if name is null or empty or longer than 40 characters.");
        } catch (IllegalArgumentException e) {
        }

        try {
            luoPerson(null, 30);
            fail("Person with null name was created successfully. Constructor of class Person should throw exception IllegalArgumentException if name is null or empty or longer than 40 characters.");
        } catch (IllegalArgumentException e) {
        } catch (NullPointerException e) {
            fail("Check that the constructor of class Person doesn't throw NullPointerException if name is null.");
        }

        try {
            luoPerson("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 30);
            fail("Person with over 40 characters long name was created successfully. Constructor of class Person should throw exception IllegalArgumentException if name is null or empty or longer than 40 characters.");
        } catch (IllegalArgumentException e) {
        }

        try {
            luoPerson("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 30);
        } catch (IllegalArgumentException e) {
            fail("Creating person with 40 characters long name failed. Constructor of class Person should throw exception IllegalArgumentException if name is null or empty or longer than 40 characters.");
        }

        try {
            luoPerson("a", 30);
        } catch (IllegalArgumentException e) {
            fail("Creating person with one character long name failed. Constructor of class Person should throw exception IllegalArgumentException if name is null or empty or longer than 40 characters.");
        }
    }

    @Test
    @Points("19.2")
    public void calculatorClass() {
        if (calculatorLuokka == null) {
            fail("Have you created public class Calculator inside the package validation?");
        }

        if (calculatorConstructor == null) {
            fail("Does the class Calculator have constructor public Calculator()?");
        }

        if (multiplicationMethod == null) {
            fail("Does the class Calculator have method public int multiplication(int fromInteger)?");
        }

        Object calculator = luoCalculator();

        try {
            kutsuKertoma(calculator, -1);
            fail("Calling Calculator's method multiplication was successful with a negative integer. Method should work only with non-negative integers.");
        } catch (IllegalArgumentException e) {
        }

        try {
            kutsuKertoma(calculator, -42);
            fail("Calling Calculator's method multiplication was successful with a negative integer. Method should work only with non-negative integers.");
        } catch (IllegalArgumentException e) {
        }

        for (int i = 0; i < 5; i++) {
            try {
                kutsuKertoma(calculator, i);
            } catch (IllegalArgumentException e) {
                fail("Calling Calculator's method multiplication failed with integer " + i + ". Integer " + i + " is non-negative, so calculator's multiplication-method should work correctly.");
            }
        }

        if (binomialCoefficientMethod == null) {
            fail("Does the class Calculator have method public int binomialCoefficient(int setSize, int subsetSize)?");
        }


        try {
            kutsuBinomikerroin(calculator, -1, 3);
            fail("Calling Calculator's method binomialCoefficient was successful when size of the set was negative. Method should work only with non-negative integers.");
        } catch (IllegalArgumentException e) {
        }

        try {
            kutsuBinomikerroin(calculator, 3, -1);
            fail("Calling Calculator's method binomialCoefficient was successful when size of the subset was negative. Method should work only with non-negative integers.");
        } catch (IllegalArgumentException e) {
        }

        try {
            kutsuBinomikerroin(calculator, 3, 4);
            fail("Calling Calculator's method binomialCoefficient was successful when size of the subset was bigger than the size of the set. Method should only work when size of the set is bigger or equal with the size of the subset.");
        } catch (IllegalArgumentException e) {
        }


    }

    private Object luoCalculator() {
        try {
            return ReflectionUtils.invokeConstructor(calculatorConstructor);
        } catch (Throwable t) {
            fail("Constructor of class Calculator caused an exception: " + t.getMessage() + ".");
        }

        return null;
    }

    private int kutsuKertoma(Object calculator, int luku) {
        try {
            return ReflectionUtils.invokeMethod(int.class, multiplicationMethod, calculator, luku);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Throwable t) {
            fail("Calling Calculator's method multiplication caused an exception: " + t.getMessage() + ".");
        }

        return -1;
    }

    private int kutsuBinomikerroin(Object calculator, int setSize, int osasetSize) {
        try {
            return ReflectionUtils.invokeMethod(int.class, binomialCoefficientMethod, calculator, setSize, osasetSize);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Throwable t) {
            fail("Calling Calculator's method binomialCoefficient caused an exception: " + t.getMessage() + ".");
        }

        return -1;
    }

    private Object luoPerson(String name, int age) {
        try {
            return ReflectionUtils.invokeConstructor(personConstructor, name, age);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable t) {
            fail("Creating Person with name: " + name + " and age: " + age + " caused an exception: " + t.getMessage() + ". Validation errors should create exception IllegalArgumentException.");
        }

        return null;
    }
}
