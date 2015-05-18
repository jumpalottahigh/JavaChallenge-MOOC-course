
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.ClassRef;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef0;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class NationalServiceTest {

    @Points("10.1")
    @Test
    public void testCivilService() throws Throwable {
        testaaPalvelusvelvollista("CivilService", 362, false);
    }

    @Points("10.2")
    @Test
    public void testMilitaryService() throws Throwable {
        testaaPalvelusvelvollista("MilitaryService", 180, true);
        testaaPalvelusvelvollista("MilitaryService", 270, true);
        testaaPalvelusvelvollista("MilitaryService", 362, true);
    }

    private <T> void testaaPalvelusvelvollista(String luokka, int alkuTj, boolean daysLeftKonstruktoriin) throws Throwable {
        ClassRef<T> classRef;

        classRef = (ClassRef<T>) Reflex.reflect(luokka);
        assertTrue("Class " + luokka + " should be public, so it must be defined as\npublic class " + luokka + " {...\n}", classRef.isPublic());

        String v = "";

        T velvollinen;
        if (daysLeftKonstruktoriin) {
            v = "Create for class " + luokka + " a public constructor: public " + luokka + "(int daysLeft)";
            assertTrue(v, classRef.constructor().taking(int.class).isPublic());
            v = "Check code " + luokka + " v = new " + luokka + "(" + alkuTj + "); ";
            velvollinen = classRef.constructor().taking(int.class).withNiceError(v).invoke(alkuTj);
        } else {
            v = "Create for class " + luokka + " a public constructor: public " + luokka + "()";
            assertTrue(v, classRef.constructor().takingNoParams().isPublic());
            v = "Check code " + luokka + " v = new " + luokka + "(); ";
            velvollinen = classRef.constructor().takingNoParams().withNiceError(v).invoke();
        }

        if (!NationalService.class.isAssignableFrom(
                classRef.getReferencedClass())) {
            Assert.fail("Class " + luokka + " doesn't implement interface NationalService.");
            return;
        }

        MethodRef0<T, Integer> getDaysLeftMethod;
        getDaysLeftMethod = classRef.method(velvollinen, "getDaysLeft").
                returning(int.class).takingNoParams();

        assertTrue("Class " + luokka + " doesn't have method: public int getDaysLeft()",
                getDaysLeftMethod.isPublic());

        assertEquals(v + "v.getDaysLeft(); ", alkuTj, (int) getDaysLeftMethod.withNiceError(v).invoke());


        MethodRef0<T, Void> workMethod;
        workMethod = classRef.method(velvollinen, "work").
                returningVoid().takingNoParams();

        assertTrue("Class " + luokka + " doesn't have method: public void work()",
                workMethod.isPublic());

        v += "v.work(); ";

        workMethod.withNiceError(v).invoke() ;

        v += "v.getDaysLeft(); ";



        assertEquals(v, alkuTj-1, (int)getDaysLeftMethod.withNiceError(v).invoke() );

        Integer daysLeft;

        for (int i = 1; i < alkuTj; i++) {
            try {
                daysLeft = getDaysLeftMethod.invoke();
            } catch (Throwable t) {
                Assert.fail("Calling the method getDaysLeft() of class " + luokka
                        + " caused an exception: " + t.toString());
                return;
            }

            Assert.assertTrue(
                    luokka + "-class's daysLeft has to be " + (alkuTj - i) + " after serving " + i + " days, you returned: " + daysLeft,
                    daysLeft == (alkuTj - i));

            try {
                workMethod.invoke();
            } catch (Throwable t) {
                Assert.fail("Calling the method work() of class " + luokka
                        + " caused an exception: " + t.toString());
                return;
            }
        }


        try {
            daysLeft = getDaysLeftMethod.invoke();
        } catch (Throwable t) {
            Assert.fail("Calling the method getDaysLeft() of class " + luokka
                    + " caused an exception: " + t.toString());
            return;
        }

        Assert.assertTrue(
                luokka + "-class's daysLeft has to be zero after work is done, you returned: " + daysLeft,
                daysLeft == 0);

        try {
            workMethod.invoke();
        } catch (Throwable t) {
            Assert.fail("Calling the method work() of class " + luokka
                    + " caused an exception: " + t.toString());
            return;
        }

        try {
            daysLeft = getDaysLeftMethod.invoke();
        } catch (Throwable t) {
            Assert.fail("Calling the method getDaysLeft() of class " + luokka
                    + " caused an exception: " + t.toString());
            return;
        }

        Assert.assertTrue(
                luokka + "-class's daysLeft has to stay zero after service is done, even though work() method is called, you returned: " + daysLeft,
                daysLeft == 0);
    }
}
