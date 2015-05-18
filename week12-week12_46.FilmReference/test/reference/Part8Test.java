package reference;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef3;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Part8Test  {

    String referenceLuokka = "Reference";
    Class referenceClass;
    String comparatorPakkaus = "reference.comparator";
    String elokuvaComparatorLuokka = "FilmComparator";
    Class elokuvaComparatorClass;
    String henkiloComparatorLuokka = "PersonComparator";
    Class henkiloComparatorClass;
    String pakkaus = "reference";
    String arvioRekisteriLuokka = "RatingRegister";
    Class arvioRekisteriClass;
    String domainPakkaus = "reference.domain";
    String henkiloLuokka = "Person";
    Class henkiloClass;
    String elokuvaLuokka = "Film";
    Class elokuvaClass;
    String arvioLuokka = "Rating";
    Class arvioClass;

    @Before
    public void setUp() {
        try {
            henkiloClass = ReflectionUtils.findClass(domainPakkaus + "." + henkiloLuokka);
        } catch (Throwable t) {
        }

        try {
            elokuvaClass = ReflectionUtils.findClass(domainPakkaus + "." + elokuvaLuokka);
        } catch (Throwable t) {
        }

        try {
            arvioClass = ReflectionUtils.findClass(domainPakkaus + "." + arvioLuokka);
        } catch (Throwable t) {
        }
        try {
            arvioRekisteriClass = ReflectionUtils.findClass(pakkaus + "." + arvioRekisteriLuokka);
        } catch (Throwable t) {
        }
        try {
            elokuvaComparatorClass = ReflectionUtils.findClass(comparatorPakkaus + "." + elokuvaComparatorLuokka);
        } catch (Throwable t) {
        }

        try {
            henkiloComparatorClass = ReflectionUtils.findClass(comparatorPakkaus + "." + henkiloComparatorLuokka);
        } catch (Throwable t) {
        }
        try {
            referenceClass = ReflectionUtils.findClass(pakkaus + "." + referenceLuokka);
        } catch (Throwable t) {
        }
    }

    @Test

    public void isReference() {
    }

    @Test
    @Points(Exercise.ID + ".8")
    public void Part8_recommendsMostSuitableFilm() throws Throwable {
        isReference();

        Object rekisteri = Reflex.reflect(arvioRekisteriClass).constructor().takingNoParams().invoke();

        Object leffa1 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("eka");
        Object leffa2 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("toka");
        Object leffa3 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("kolmas");
        Object leffa4 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("neljas");

        Object pekka = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Pekka");
        Object mikke = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Mikke");
        Object antti = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Antti");

        Object arvioOk = Enum.valueOf(arvioClass, "FINE");
        Object arvioHuono = Enum.valueOf(arvioClass, "BAD");

        MethodRef3 ref = Reflex.reflect(arvioRekisteriClass).method("addRating").returningVoid().taking(henkiloClass, elokuvaClass, arvioClass);
        ref.invokeOn(rekisteri, pekka, leffa1, arvioHuono);
        ref.invokeOn(rekisteri, pekka, leffa2, arvioOk);
        ref.invokeOn(rekisteri, pekka, leffa3, arvioHuono);
        ref.invokeOn(rekisteri, pekka, leffa4, arvioHuono);

        ref.invokeOn(rekisteri, antti, leffa1, arvioHuono);
        ref.invokeOn(rekisteri, antti, leffa3, arvioHuono);
        ref.invokeOn(rekisteri, antti, leffa4, arvioHuono);

        ref.invokeOn(rekisteri, mikke, leffa1, arvioOk);
        ref.invokeOn(rekisteri, mikke, leffa3, arvioOk);
        ref.invokeOn(rekisteri, mikke, leffa4, arvioOk);

        Object reference = Reflex.reflect(referenceClass).constructor().taking(arvioRekisteriClass).invoke(rekisteri);
        Object suositus = Reflex.reflect(referenceClass).method("recommendFilm").returning(elokuvaClass).taking(henkiloClass).invokeOn(reference, antti);
        if (suositus == null) {
            fail("Recommendation shouldn't be null if there is something to recommend.");
        }


        Object leffanNimi = Reflex.reflect(elokuvaClass).method("getName").returning(String.class).takingNoParams().invokeOn(suositus);
        assertEquals("Reference should return to a person the most suitable film which he hasn't watched yet, now it didn't happen.", "toka", leffanNimi);
    }

    @Test
    @Points(Exercise.ID + ".8")
    public void Part8_recommendsMostSuitableFilmReturnsNullIfNoOptions() throws Throwable {
        isReference();

        Object rekisteri = Reflex.reflect(arvioRekisteriClass).constructor().takingNoParams().invoke();

        Object leffa1 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("eka");
        Object leffa2 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("toka");
        Object leffa3 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("kolmas");
        Object leffa4 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("neljas");

        Object pekka = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Pekka");
        Object mikke = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Mikke");
        Object antti = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Antti");

        Object arvioOk = Enum.valueOf(arvioClass, "FINE");
        Object arvioHuono = Enum.valueOf(arvioClass, "BAD");

        MethodRef3 ref = Reflex.reflect(arvioRekisteriClass).method("addRating").returningVoid().taking(henkiloClass, elokuvaClass, arvioClass);
        ref.invokeOn(rekisteri, pekka, leffa1, arvioHuono);
        ref.invokeOn(rekisteri, pekka, leffa2, arvioOk);
        ref.invokeOn(rekisteri, pekka, leffa3, arvioHuono);
        ref.invokeOn(rekisteri, pekka, leffa4, arvioHuono);

        ref.invokeOn(rekisteri, antti, leffa1, arvioHuono);
        ref.invokeOn(rekisteri, antti, leffa3, arvioHuono);
        ref.invokeOn(rekisteri, antti, leffa4, arvioHuono);

        ref.invokeOn(rekisteri, mikke, leffa1, arvioOk);
        ref.invokeOn(rekisteri, mikke, leffa2, arvioOk);
        ref.invokeOn(rekisteri, mikke, leffa3, arvioOk);
        ref.invokeOn(rekisteri, mikke, leffa4, arvioOk);

        Object reference = Reflex.reflect(referenceClass).constructor().taking(arvioRekisteriClass).invoke(rekisteri);
        Object suositus = Reflex.reflect(referenceClass).method("recommendFilm").returning(elokuvaClass).taking(henkiloClass).invokeOn(reference, mikke);
        assertNull("If person has watched all films, reference should retur null.", suositus);
    }

    @Test
    @Points(Exercise.ID + ".8")
    public void Part8_recommendsMostSuitableFilmTwo() throws Throwable {
        isReference();

        Object rekisteri = Reflex.reflect(arvioRekisteriClass).constructor().takingNoParams().invoke();

        Object leffa1 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("eka1");
        Object leffa2 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("toka1");
        Object leffa3 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("kolmas1");
        Object leffa4 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("neljas1");

        Object pekka = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Pekka");
        Object mikke = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Mikke");
        Object antti = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Antti");

        Object arvioOk = Enum.valueOf(arvioClass, "FINE");
        Object arvioHyva = Enum.valueOf(arvioClass, "GOOD");
        Object arvioHuono = Enum.valueOf(arvioClass, "BAD");

        MethodRef3 ref = Reflex.reflect(arvioRekisteriClass).method("addRating").returningVoid().taking(henkiloClass, elokuvaClass, arvioClass);
        ref.invokeOn(rekisteri, pekka, leffa1, arvioHuono);
        ref.invokeOn(rekisteri, pekka, leffa2, arvioOk);
        ref.invokeOn(rekisteri, pekka, leffa4, arvioHuono);

        ref.invokeOn(rekisteri, antti, leffa3, arvioHyva);
        ref.invokeOn(rekisteri, antti, leffa4, arvioHuono);

        ref.invokeOn(rekisteri, mikke, leffa1, arvioHuono);
        ref.invokeOn(rekisteri, mikke, leffa2, arvioOk);
        ref.invokeOn(rekisteri, mikke, leffa3, arvioHyva);
        ref.invokeOn(rekisteri, mikke, leffa4, arvioHyva);

        Object reference = Reflex.reflect(referenceClass).constructor().taking(arvioRekisteriClass).invoke(rekisteri);
        Object suositus = Reflex.reflect(referenceClass).method("recommendFilm").returning(elokuvaClass).taking(henkiloClass).invokeOn(reference, antti);

        if (suositus == null) {
            fail("Recommendation shouldn't be null if there is something to recommend.");
        }

        Object leffanNimi = Reflex.reflect(elokuvaClass).method("getName").returning(String.class).takingNoParams().invokeOn(suositus);
        assertEquals("Reference should return to a person the most suitable film which he hasn't watched yet, now it didn't happen.", "toka1", leffanNimi);
    }

    @Test
    @Points(Exercise.ID + ".8")
    public void Part8_recommendsMostSuitableFilmThree() throws Throwable {
        isReference();

        Object rekisteri = Reflex.reflect(arvioRekisteriClass).constructor().takingNoParams().invoke();

        Object leffa1 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("eka1");
        Object leffa2 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("toka1");
        Object leffa3 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("kolmas1");
        Object leffa4 = Reflex.reflect(elokuvaClass).constructor().taking(String.class).invoke("neljas1");

        Object pekka = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Pekka");
        Object mikke = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Mikke");
        Object antti = Reflex.reflect(henkiloClass).constructor().taking(String.class).invoke("Antti");

        Object arvioOk = Enum.valueOf(arvioClass, "FINE");
        Object arvioHyva = Enum.valueOf(arvioClass, "GOOD");
        Object arvioHuono = Enum.valueOf(arvioClass, "BAD");

        MethodRef3 ref = Reflex.reflect(arvioRekisteriClass).method("addRating").returningVoid().taking(henkiloClass, elokuvaClass, arvioClass);
        ref.invokeOn(rekisteri, pekka, leffa1, arvioHuono);
        ref.invokeOn(rekisteri, pekka, leffa2, arvioOk);
        ref.invokeOn(rekisteri, pekka, leffa3, arvioOk);
        ref.invokeOn(rekisteri, pekka, leffa4, arvioHuono);

        ref.invokeOn(rekisteri, antti, leffa3, arvioHyva);
        ref.invokeOn(rekisteri, antti, leffa4, arvioHuono);

        ref.invokeOn(rekisteri, mikke, leffa1, arvioHuono);
        ref.invokeOn(rekisteri, mikke, leffa2, arvioOk);

        Object reference = Reflex.reflect(referenceClass).constructor().taking(arvioRekisteriClass).invoke(rekisteri);
        Object suositus = Reflex.reflect(referenceClass).method("recommendFilm").returning(elokuvaClass).taking(henkiloClass).invokeOn(reference, mikke);


        if (suositus == null) {
            fail("Recommendation shouldn't be null if there is something to recommend.");
        }

        Object leffanNimi = Reflex.reflect(elokuvaClass).method("getName").returning(String.class).takingNoParams().invokeOn(suositus);
        assertEquals("Reference should return to a person the most suitable film which he hasn't watched yet, now it didn't happen.", "kolmas1", leffanNimi);
    }

}
