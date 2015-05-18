package reference;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Part1_7Test<_Person, _Film, _Rating, _Rekisteri, _HComp, _EComp, _Reference> {

    Reflex.ClassRef<_Rating> _RatingRef;
    Reflex.ClassRef<_Person> _PersonRef;
    Reflex.ClassRef<_Film> _FilmRef;
    Reflex.ClassRef<_Rekisteri> _RekisteriRef;
    Reflex.ClassRef<_Reference> _ReferenceRef;
    String personLuokka = "reference.domain.Person";
    String filmLuokka = "reference.domain.Film";
    String ratingLuokka = "reference.domain.Rating";
    String rekisteriLuokka = "reference.RatingRegister";
    String referenceLuokka = "reference.Reference";
    String filmComparatorLuokka = "reference.comparator.FilmComparator";
    String personComparatorLuokka = "reference.comparator.PersonComparator";

    @Before
    public void setUp() {
        try {
            _PersonRef = Reflex.reflect(personLuokka);
            _FilmRef = Reflex.reflect(filmLuokka);
            _RatingRef = Reflex.reflect(ratingLuokka);
            _RekisteriRef = Reflex.reflect(rekisteriLuokka);
            _ReferenceRef = Reflex.reflect(referenceLuokka);
        } catch (Throwable e) {
        }
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void isClassPerson() {
        String luokanNimi = personLuokka;
        _PersonRef = Reflex.reflect(luokanNimi);
        assertTrue("Create class Person inside the package reference.domain", _PersonRef.isPublic());
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void personCorrectConstructor() throws Throwable {
        assertTrue("Create constructor public Person(String name) for class Person",
                _PersonRef.constructor().taking(String.class).isPublic());

        String luokanNimi = personLuokka;
        Class c = ReflectionUtils.findClass(luokanNimi);


        assertEquals("Class Person should have only one constructor, now there are ", 1, c.getConstructors().length);

        _PersonRef.constructor().taking(String.class).withNiceError("\nError caused by code new Person(\"Arto\"); ").invoke("arto");
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void noRedundantVariablesClassPerson() {
        saniteettitarkastus(personLuokka, 1, "an instance variable for name of the person");
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void getNamePerson() throws Throwable {
        String metodi = "getName";
        String virhe = "Create method public String getName() for class Person";

        assertTrue(virhe, _PersonRef.method(metodi).returning(String.class).takingNoParams().isPublic());

        String v = ""
                + "Person h = new Person(\"Pekka\");\n"
                + "h.getName()\n";
        _Person h = newPerson("Pekka");
        assertEquals(v, "Pekka", _PersonRef.method(metodi).returning(String.class).takingNoParams().withNiceError(v).invokeOn(h));

        v = ""
                + "Person h = new Person(\"Mikko\");}n"
                + "h.getName()\n";
        h = newPerson("Mikko");
        assertEquals(v, "Mikko", _PersonRef.method(metodi).returning(String.class).takingNoParams().withNiceError(v).invokeOn(h));
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void personEqualsAndHashCode() throws Throwable {
        _Person h1 = newPerson("Pekka");
        _Person h2 = newPerson("Jukka");
        _Person h3 = newPerson("Pekka");

        String v = "Overwrite Person's method equals(Object);\n"
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Jukka\");\n"
                + "Person h3 = new Person(\"Pekka\");\n"
                + "h1.equals(h2);\n";

        assertEquals(v, false, _PersonRef.method(h1, "equals").returning(boolean.class).taking(Object.class).withNiceError(v).invoke(h2));

        v = "Overwrite Person's method equals(Object);\n"
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Jukka\");\n"
                + "Person h3 = new Person(\"Pekka\");\n"
                + "h1.equals(h3);\n";

        assertEquals(v, true, _PersonRef.method(h1, "equals").returning(boolean.class).taking(Object.class).withNiceError(v).invoke(h3));

        v = ""
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Jukka\");\n"
                + "Person h3 = new Person(\"Pekka\");\n"
                + "h1.hashCode() == h3.hashCode();\n";
        assertEquals(v, true, h1.hashCode() == h3.hashCode());

        v = ""
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Jukka\");\n"
                + "Person h3 = new Person(\"Pekka\");\n"
                + "h1.hashCode() == h2.hashCode();\n";
        assertEquals(v, false, h1.hashCode() == h2.hashCode());
    }

    /*
     *
     */
    @Test
    @Points(Exercise.ID + ".1")
    public void isClassFilm() {
        String luokanNimi = filmLuokka;
        _FilmRef = Reflex.reflect(luokanNimi);
        assertTrue("Create class Film inside the package reference.domain", _FilmRef.isPublic());
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void filmCorrectConstructor() throws Throwable {
        assertTrue("Create constructor public Film(String name) for class Film",
                _FilmRef.constructor().taking(String.class).isPublic());

        String luokanNimi = filmLuokka;
        Class c = ReflectionUtils.findClass(luokanNimi);


        assertEquals("Class Film should have only one constructor, now there are ", 1, c.getConstructors().length);

        _FilmRef.constructor().taking(String.class).withNiceError("\nError caused by code new Film(\"Rambo\"); ").invoke("Rambo");
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void noRedundantVariablesClassFilm() {
        saniteettitarkastus(filmLuokka, 1, "an isntance variable for name of the film");
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void getNameFilm() throws Throwable {
        String metodi = "getName";
        String virhe = "Create method public String getName() for class Film";

        assertTrue(virhe, _FilmRef.method(metodi).returning(String.class).takingNoParams().isPublic());

        String v = ""
                + "Film e = new Film(\"Rambo\");\n"
                + "e.getName()\n";
        _Film h = newFilm("Rambo");
        assertEquals(v, "Rambo", _FilmRef.method(metodi).returning(String.class).takingNoParams().withNiceError(v).invokeOn(h));

        v = ""
                + "Film e = new Film(\"Blue Velvet\");}n"
                + "e.getName()\n";
        h = newFilm("Blue Velvet");
        assertEquals(v, "Blue Velvet", _FilmRef.method(metodi).returning(String.class).takingNoParams().withNiceError(v).invokeOn(h));
    }

    @Test
    @Points(Exercise.ID + ".1")
    public void filmEqualsAndHashCode() throws Throwable {
        _Film h1 = newFilm("Rambo");
        _Film h2 = newFilm("Commando");
        _Film h3 = newFilm("Rambo");

        String v = "Overwrite Film's method equals(Object o)\n"
                + "Film e1 = new Film(\"Rambo\");\n"
                + "Film e1  = new Film(\"Commando\");\n"
                + "Film e1  = new Film(\"Rambo\");\n"
                + "e1.equals(e2);\n";

        assertEquals(v, false, _FilmRef.method(h1, "equals").returning(boolean.class).taking(Object.class).withNiceError(v).invoke(h2));

        v = "Overwrite Film's method equals(Object o)\n"
                + "Film e1 = new Film(\"Rambo\");\n"
                + "Film e1  = new Film(\"Commando\");\n"
                + "Film e1  = new Film(\"Rambo\");\n"
                + "e1.equals(e3);\n";
        assertEquals(v, true, _FilmRef.method(h1, "equals").returning(boolean.class).taking(Object.class).withNiceError(v).invoke(h3));

        v = ""
                + "Film e1 = new Film(\"Rambo\");\n"
                + "Film e1  = new Film(\"Commando\");\n"
                + "Film e1  = new Film(\"Rambo\");\n"
                + "e1.hashCode() == e3.hashCode();\n";
        assertEquals(v, true, h1.hashCode() == h3.hashCode());

        v = ""
                + "Film e1 = new Film(\"Rambo\");\n"
                + "Film e1  = new Film(\"Commando\");\n"
                + "Film e1  = new Film(\"Rambo\");\n"
                + "e1.hashCode() == e2.hashCode();\n";
        assertEquals(v, false, h1.hashCode() == h2.hashCode());
    }

    /*
     *
     */
    @Points(Exercise.ID + ".2")
    @Test
    public void isEnumRating() {
        String luokanNimi = ratingLuokka;
        try {
            _RatingRef = Reflex.reflect(luokanNimi);
        } catch (Throwable t) {
            fail("Create enum Rating inside the package reference.domain");
        }
        assertTrue("Create enum Rating inside the package reference.domain", _RatingRef.isPublic());
        Class c = _RatingRef.cls();
        assertTrue("Create enum Rating inside the package personnel, now you probably made a normal class", c.isEnum());
    }

    @Points(Exercise.ID + ".2")
    @Test
    public void enumCorrectValues() {
        String luokanNimi = ratingLuokka;
        Class c = ReflectionUtils.findClass(luokanNimi);
        Object[] vakiot = c.getEnumConstants();
        assertEquals("enum Rating should define correct number of values", 6, vakiot.length);

        String[] t = {"BAD", "MEDIOCRE", "NOT_WATCHED", "NEUTRAL", "FINE", "GOOD"};

        for (String tunnus : t) {
            assertTrue("enumin Rating should define value " + tunnus, sis(tunnus, vakiot));
        }
    }

    @Points(Exercise.ID + ".2")
    @Test
    public void enumMethodGetValue() {
        String metodi = "getValue";
        String virhe = "Create method public int getValue() for enum Rating";

        assertTrue(virhe, _RatingRef.method(metodi).returning(int.class).takingNoParams().isPublic());
    }

    @Points(Exercise.ID + ".2")
    @Test
    public void enumCorrectValues2() throws Throwable {
        String luokanNimi = ratingLuokka;
        Class c = ReflectionUtils.findClass(luokanNimi);
        Object[] vakiot = c.getEnumConstants();

        for (Object object : vakiot) {
            int arvo = _RatingRef.method("getValue").returning(int.class).takingNoParams().invokeOn((_Rating) object);

            String tunnus = object.toString();

            if (tunnus.equals("BAD")) {
                assertEquals("Rating rating = Rating.BAD;\n rating.getValue();\n", -5, arvo);
            } else if (tunnus.equals("MEDIOCRE")) {
                assertEquals("Rating rating = Rating.MEDIOCRE;\n rating.getValue();\n", -3, arvo);
            } else if (tunnus.equals("NOT_WATCHED")) {
                assertEquals("Rating rating = Rating.NOT_WATCHED;\n rating.getValue();\n", 0, arvo);
            } else if (tunnus.equals("NEUTRAL")) {
                assertEquals("Rating rating = Rating.NEUTRAL;\n rating.getValue();\n", 1, arvo);
            } else if (tunnus.equals("FINE")) {
                assertEquals("Rating rating = Rating.FINE;\n rating.getValue();\n", 3, arvo);
            } else if (tunnus.equals("GOOD")) {
                assertEquals("Rating rating = Rating.GOOD;\n rating.getValue();\n", 5, arvo);
            }
        }

    }

    /*
     *
     */
    @Test
    @Points(Exercise.ID + ".3")
    public void isClassRatingRegister() {
        _RatingRef = Reflex.reflect(rekisteriLuokka);
        assertTrue("Create class RatingRegister inside the package reference", _RekisteriRef.isPublic());
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void ratingRegisterCorrectConstructor() throws Throwable {
        assertTrue("Create constructor public RatingRegister() for class RatingRegister",
                _RekisteriRef.constructor().takingNoParams().isPublic());

        String luokanNimi = rekisteriLuokka;
        Class c = ReflectionUtils.findClass(luokanNimi);


        assertEquals("Class RatingRegister should only have one constructor, now there are ", 1, c.getConstructors().length);

        _RekisteriRef.constructor().takingNoParams().withNiceError("\n"
                + "Error caused by code new RatingRegister();\n").invoke();
    }

    @Points(Exercise.ID + ".3")
    public void noRedundantVariablesClassRatingRegister() {
        saniteettitarkastus(rekisteriLuokka, 4, "instance variables for personal ratings and film ratings which are stored in Maps");
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void addRatingRatingRegister() throws Throwable {
        String metodi = "addRating";
        String virhe = "Create method public void addRating(Film film, Rating rating) for class RatingRegister";

        assertTrue(virhe, _RekisteriRef.method(metodi).returningVoid().taking(_FilmRef.cls(), _RatingRef.cls()).isPublic());

        String v = "Error with code:\n"
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating( new Film(\"Rambo\"), Rating.BAD);\n";
        _Film e = newFilm("Rambo");
        _Rekisteri r = newRekisteri();
        _RekisteriRef.method(r, "addRating").returningVoid().taking(_FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(e, Rating("BAD"));
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void getRatingsRatingRegister() throws Throwable {
        String metodi = "getRatings";
        String virhe = "Create method public List<Rating> getRatings(Film film) for class RatingRegister";

        assertTrue(virhe, _RekisteriRef.method(metodi).returning(List.class).taking(_FilmRef.cls()).isPublic());

        String v = "Error with code:\n"
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating( new Film(\"Rambo\"), Rating.BAD);\n"
                + "rek.getRatings(new Film(\"Rambo\"));\n";
        _Film e = newFilm("Rambo");
        _Rekisteri rek = newRekisteri();
        _RekisteriRef.method(rek, "addRating").returningVoid().taking(_FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(e, Rating("BAD"));

        _RekisteriRef.method(rek, metodi).returning(List.class).taking(_FilmRef.cls()).withNiceError(v).invoke(e);
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void ratingsAreRegisteredToFilm() throws Throwable {
        _Rekisteri rek = newRekisteri();

        String v = ""
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating(new Film(\"Eraserhead\"), Rating.FINE);\n"
                + "rek.addRating(new Film(\"Blue Velvet\"), Rating.GOOD);\n"
                + "rek.addRating(new Film(\"Eraserhead\"), Rating.NEUTRAL);\n"
                + "rek.addRating(new Film(\"Eraserhead\"), Rating.BAD);\n"
                + "rek.addRating(new Film(\"Eraserhead\"), Rating.NOT_WATCHED);\n"
                + "rek.addRating(new Film(\"Blue Velvet\"), Rating.GOOD);\n"
                + "rek.addRating(new Film(\"Rambo\"), Rating.MEDIOCRE);\n";
        addRating(rek, newFilm("Eraserhead"), Rating("FINE"), v);
        addRating(rek, newFilm("Blue Velvet"), Rating("GOOD"), v);
        addRating(rek, newFilm("Eraserhead"), Rating("NEUTRAL"), v);
        addRating(rek, newFilm("Eraserhead"), Rating("BAD"), v);
        addRating(rek, newFilm("Eraserhead"), Rating("NOT_WATCHED"), v);
        addRating(rek, newFilm("Blue Velvet"), Rating("GOOD"), v);
        addRating(rek, newFilm("Rambo"), Rating("MEDIOCRE"), v);

        String w = v + ""
                + "rek.getRatings(new Film(\"Eraserhead\"));\n";

        List er = getRatings(rek, newFilm("Eraserhead"), v);
        assertTrue("Returned list cannot be null with code\n" + w, er != null);
        List exp = new ArrayList();
        exp.add(Rating("FINE"));
        exp.add(Rating("NEUTRAL"));
        exp.add(Rating("BAD"));
        exp.add(Rating("NOT_WATCHED"));
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);

        w = v + ""
                + "rek.getRatings(new Film(\"Blue Velvet\"));\n";

        er = getRatings(rek, newFilm("Blue Velvet"), v);
        assertTrue("Returned list cannot be null with code\n" + w, er != null);
        exp = new ArrayList();
        exp.add(Rating("GOOD"));
        exp.add(Rating("GOOD"));
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);

        w = v + ""
                + "rek.getRatings(new Film(\"Rambo\"));\n";

        er = getRatings(rek, newFilm("Rambo"), v);
        assertTrue("Returned list cannot be null with code\n" + w, er != null);
        exp = new ArrayList();
        exp.add(Rating("MEDIOCRE"));
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void filmRatingsRatingRegister() throws Throwable {
        String metodi = "filmRatings";
        String virhe = "Create method "
                + "public Map<Film, List<Rating>> filmRatings() for class RatingRegister";

        assertTrue(virhe, _RekisteriRef.method(metodi).returning(Map.class).takingNoParams().isPublic());

        _Rekisteri rek = newRekisteri();

        String v = "Error with code:\n"
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating( new Film(\"Rambo\"), Rating.BAD);\n"
                + "rek.filmRatings();\n";
        _Film e = newFilm("Rambo");
        _RekisteriRef.method(rek, "addRating").returningVoid().taking(_FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(e, Rating("BAD"));

        _RekisteriRef.method(rek, metodi).returning(Map.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points(Exercise.ID + ".3")
    public void filmRatingsReturned() throws Throwable {
        _Rekisteri rek = newRekisteri();

        String v = ""
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating(new Film(\"Eraserhead\"), Rating.FINE);\n"
                + "rek.addRating(new Film(\"Blue Velvet\"), Rating.GOOD);\n"
                + "rek.addRating(new Film(\"Eraserhead\"), Rating.NEUTRAL);\n"
                + "rek.addRating(new Film(\"Eraserhead\"), Rating.BAD);\n"
                + "rek.addRating(new Film(\"Eraserhead\"), Rating.NOT_WATCHED);\n"
                + "rek.addRating(new Film(\"Blue Velvet\"), Rating.GOOD);\n"
                + "rek.addRating(new Film(\"Rambo\"), Rating.MEDIOCRE);\n"
                + "Map<Film, List<Rating>> ratings = rek.filmRatings();\n";
        addRating(rek, newFilm("Eraserhead"), Rating("FINE"), v);
        addRating(rek, newFilm("Blue Velvet"), Rating("GOOD"), v);
        addRating(rek, newFilm("Eraserhead"), Rating("NEUTRAL"), v);
        addRating(rek, newFilm("Eraserhead"), Rating("BAD"), v);
        addRating(rek, newFilm("Eraserhead"), Rating("NOT_WATCHED"), v);
        addRating(rek, newFilm("Blue Velvet"), Rating("GOOD"), v);
        addRating(rek, newFilm("Rambo"), Rating("MEDIOCRE"), v);

        Map<_Film, List<_Rating>> all = filmRatings(rek, v);
        assertTrue("Returned map cannot be null with code\n" + v, all != null);

        assertEquals("Returned map's number of keys wrong with code " + v + "you returned: " + all + "\n", 3, all.keySet().size());



        String w = v + ""
                + "ratings.get(new Film(\"Eraserhead\"));\n";

        List er = all.get(newFilm("Eraserhead"));
        assertTrue("Returned list cannot be null with code\n" + w + "", er != null);
        List exp = new ArrayList();
        exp.add(Rating("FINE"));
        exp.add(Rating("NEUTRAL"));
        exp.add(Rating("BAD"));
        exp.add(Rating("NOT_WATCHED"));
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);

        w = v + ""
                + "ratings.get(new Film(\"Blue Velvet\"));\n";

        er = getRatings(rek, newFilm("Blue Velvet"), v);
        assertTrue("Returned list cannot be null with code\n" + w, er != null);
        exp = new ArrayList();
        exp.add(Rating("GOOD"));
        exp.add(Rating("GOOD"));
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);

        w = v + ""
                + "ratings.get(new Film(\"Rambo\"));\n";

        er = getRatings(rek, newFilm("Rambo"), v);
        assertTrue("Returned list cannot be null with code\n" + w, er != null);
        exp = new ArrayList();
        exp.add(Rating("MEDIOCRE"));
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);
    }

    /*
     *
     */
    @Test
    @Points(Exercise.ID + ".4")
    public void addRatingPersonRatingRegister() throws Throwable {
        String metodi = "addRating";
        String virhe = "Create method public void addRating(Person person, Film film, Rating rating) for class RatingRegister";

        assertTrue(virhe, _RekisteriRef.method(metodi).returningVoid().taking(_PersonRef.cls(), _FilmRef.cls(), _RatingRef.cls()).isPublic());

        String v = "Error with code:\n"
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating( new Person(\"Arto\"), new Film(\"Rambo\"), Rating.BAD);\n";
        _Film e = newFilm("Rambo");
        _Person h = newPerson("Arto");
        _Rekisteri r = newRekisteri();
        _RekisteriRef.method(r, "addRating").returningVoid().taking(_PersonRef.cls(), _FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(h, e, Rating("BAD"));
    }

    @Test
    @Points(Exercise.ID + ".4")
    public void getRatingRatingRegister() throws Throwable {
        String metodi = "getRating";
        String virhe = "Create method public Rating getRating(Person person, Film film) for class RatingRegister";

        assertTrue(virhe, _RekisteriRef.method(metodi).returning(_RatingRef.cls()).taking(_PersonRef.cls(), _FilmRef.cls()).isPublic());

        String v = "Error with code:\n"
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating( new Person(\"Arto\"), new Film(\"Rambo\"), Rating.BAD);\n"
                + "rek.getRating( new Person(\"Arto\"), new Film(\"Rambo\"));\n";
        _Film e = newFilm("Rambo");
        _Person h = newPerson("Arto");

        _Rekisteri r = newRekisteri();
        _RekisteriRef.method(r, "addRating").returningVoid().taking(_PersonRef.cls(), _FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(h, e, Rating("BAD"));

        _RekisteriRef.method(r, metodi).returning(_RatingRef.cls()).taking(_PersonRef.cls(), _FilmRef.cls()).withNiceError(v).invoke(h, e);
    }

    @Test
    @Points(Exercise.ID + ".4")
    public void personRatingsRegister() throws Throwable {
        _Rekisteri rek = newRekisteri();
        _Person h1 = newPerson("Pekka");
        _Person h2 = newPerson("Arto");

        String v = ""
                + "RatingRegister rek = new RatingRegister();\n"
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Arto\");\n"
                + "rek.addRating(h1, new Film(\"Pulp fiction\"), Rating.FINE);\n"
                + "rek.addRating(h1, new Film(\"Eraserhead\"), Rating.GOOD);\n"
                + "rek.addRating(h2, new Film(\"Eraserhead\"), Rating.BAD);\n"
                + "rek.addRating(h2, new Film(\"Pulp fiction\"), Rating.NEUTRAL);\n"
                + "rek.addRating(h2, new Film(\"Rambo\"), Rating.GOOD);\n";

        addRating(rek, h1, newFilm("Pulp fiction"), Rating("FINE"), ratingLuokka);
        addRating(rek, h1, newFilm("Eraserhead"), Rating("GOOD"), ratingLuokka);
        addRating(rek, h2, newFilm("Eraserhead"), Rating("BAD"), ratingLuokka);
        addRating(rek, h2, newFilm("Pulp fiction"), Rating("NEUTRAL"), ratingLuokka);
        addRating(rek, h2, newFilm("Rambo"), Rating("GOOD"), ratingLuokka);

        String w = v + "rek.getRating(h1, new Film(\"Pulp fiction\") );\n";
        assertEquals(w, Rating("FINE"), getRating(rek, h1, newFilm("Pulp fiction"), w));
        w = v + "rek.getRating(h1, new Film(\"Pulp fiction\") );\n";
        assertEquals(w, Rating("GOOD"), getRating(rek, h1, newFilm("Eraserhead"), w));
        w = v + "rek.getRating(h2, new Film(\"Eraserhead\") );\n";
        assertEquals(w, Rating("BAD"), getRating(rek, h2, newFilm("Eraserhead"), w));
        w = v + "rek.getRating(h2, new Film(\"Pulp fiction\") );\n";
        assertEquals(w, Rating("NEUTRAL"), getRating(rek, h2, newFilm("Pulp fiction"), w));
        w = v + "rek.getRating(h2, new Film(\"Rambo\") );\n";
        assertEquals(w, Rating("GOOD"), getRating(rek, h2, newFilm("Rambo"), w));
        w = v + "rek.getRating(h1, new Film(\"Rambo\") );\n";
        assertEquals(w, Rating("NOT_WATCHED"), getRating(rek, h1, newFilm("Rambo"), w));
    }

    @Test
    @Points(Exercise.ID + ".4")
    public void getPersonalRatingsRatingRegister() throws Throwable {
        String metodi = "getPersonalRatings";
        String virhe = "Create method public Map<Film, Rating> getPersonalRatings(Person person) for class RatingRegister";

        assertTrue(virhe, _RekisteriRef.method(metodi).returning(Map.class).taking(_PersonRef.cls()).isPublic());

        String v = "Error with code:\n"
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating( new Person(\"Arto\"), new Film(\"Rambo\"), Rating.BAD);\n"
                + "rek.getPersonalRatings( new Person(\"Arto\"));\n";
        _Film e = newFilm("Rambo");
        _Person h = newPerson("Arto");
        _Rekisteri rek = newRekisteri();
        _RekisteriRef.method(rek, "addRating").returningVoid().taking(_PersonRef.cls(), _FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(h, e, Rating("BAD"));

        _RekisteriRef.method(rek, metodi).returning(Map.class).taking(_PersonRef.cls()).withNiceError(v).invoke(h);
    }

    @Test
    @Points(Exercise.ID + ".4")
    public void personRatingsFromRegister() throws Throwable {
        _Rekisteri rek = newRekisteri();
        _Person h1 = newPerson("Pekka");
        _Person h2 = newPerson("Arto");

        String v = ""
                + "RatingRegister rek = new RatingRegister();\n"
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Arto\");\n"
                + "rek.addRating(h1, new Film(\"Pulp fiction\"), Rating.FINE);\n"
                + "rek.addRating(h1, new Film(\"Eraserhead\"), Rating.GOOD);\n"
                + "rek.addRating(h2, new Film(\"Eraserhead\"), Rating.BAD);\n"
                + "rek.addRating(h2, new Film(\"Pulp fiction\"), Rating.NEUTRAL);\n"
                + "rek.addRating(h2, new Film(\"Rambo\"), Rating.GOOD);\n";

        addRating(rek, h1, newFilm("Pulp fiction"), Rating("FINE"), ratingLuokka);
        addRating(rek, h1, newFilm("Eraserhead"), Rating("GOOD"), ratingLuokka);
        addRating(rek, h2, newFilm("Eraserhead"), Rating("BAD"), ratingLuokka);
        addRating(rek, h2, newFilm("Pulp fiction"), Rating("NEUTRAL"), ratingLuokka);
        addRating(rek, h2, newFilm("Rambo"), Rating("GOOD"), ratingLuokka);

        String w = v + ""
                + "Map<Film,Rating> ratings = rek.getPersonalRatings(new Person(\"Pekka\"));\n";

        Map<_Film, _Rating> er = getPersonalRatings(rek, newPerson("Pekka"), v);
        assertTrue("Returned map cannot be null with code\n" + w, er != null);
        assertEquals("Returned map's number of keys wrong with code " + w + " you returned: " + er + "\n", 2, er.keySet().size());
        String w1 = w + "ratings.get(new Film(\"Pulp fiction\"));\n";
        assertEquals(w1, Rating("FINE"), er.get(newFilm("Pulp fiction")));
        w1 = w + "ratings.get(new Film(\"Eraserhead\"));\n";
        assertEquals(w1, Rating("GOOD"), er.get(newFilm("Eraserhead")));

        v = ""
                + "RatingRegister rek = new RatingRegister();\n"
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Arto\");\n"
                + "rek.addRating(h1, new Film(\"Pulp fiction\"), Rating.FINE);\n"
                + "rek.addRating(h1, new Film(\"Eraserhead\"), Rating.GOOD);\n"
                + "rek.addRating(h2, new Film(\"Eraserhead\"), Rating.BAD);\n"
                + "rek.addRating(h2, new Film(\"Pulp fiction\"), Rating.NEUTRAL);\n"
                + "rek.addRating(h2, new Film(\"Rambo\"), Rating.GOOD);\n";

        v += ""
                + "Map<Film,Rating> ratings = rek.getPersonalRatings(new Person(\"Arto\"));\n";
        er = getPersonalRatings(rek, newPerson("Arto"), v);
        assertTrue("Returned map cannot be null with code\n" + w, er != null);
        assertEquals("Returned map's number of keys wrong with code " + w + " you returned: " + er, 3, er.keySet().size());
        String w2 = v + "ratings.get(new Film(\"Pulp fiction\"));\n";
        assertEquals(w2, Rating("NEUTRAL"), er.get(newFilm("Pulp fiction")));
        w2 = v + "ratings.get(new Film(\"Eraserhead\"));\n";
        assertEquals(w2, Rating("BAD"), er.get(newFilm("Eraserhead")));
        w2 = v + "ratings.get(new Film(\"Rambo\"));\n";
        assertEquals(w2, Rating("GOOD"), er.get(newFilm("Rambo")));

        v = ""
                + "RatingRegister rek = new RatingRegister();\n"
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Arto\");\n"
                + "rek.addRating(h1, new Film(\"Pulp fiction\"), Rating.FINE);\n"
                + "rek.addRating(h1, new Film(\"Eraserhead\"), Rating.GOOD);\n"
                + "rek.addRating(h2, new Film(\"Eraserhead\"), Rating.BAD);\n"
                + "rek.addRating(h2, new Film(\"Pulp fiction\"), Rating.NEUTRAL);\n"
                + "rek.addRating(h2, new Film(\"Rambo\"), Rating.GOOD);\n";

 v += ""
                + "Map<Film,Rating> ratings = rek.getPersonalRatings(new Person(\"Jukka\"));\n";
        er = getPersonalRatings(rek, newPerson("Jukka"), v);
        assertTrue("If person doesn't have any ratings, returned map cannot be null\n" + v, er != null);
        assertEquals("If person doesn't have any ratings, returned map should be empty " + v + " you returned: " + er, 0, er.keySet().size());
    }

    @Test
    @Points(Exercise.ID + ".4")
    public void personalRatingsAffectFilmRatingsInRatingRegister() throws Throwable {
        _Rekisteri rek = newRekisteri();
        _Person h1 = newPerson("Pekka");
        _Person h2 = newPerson("Arto");
        _Person h3 = newPerson("Jukka");

        String v = ""
                + "RatingRegister rek = new RatingRegister();\n"
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Arto\");\n"
                + "Person h3 = new Person(\"Jukka\");\n"
                + "rek.addRating(h1, new Film(\"Pulp fiction\"), Rating.FINE);\n"
                + "rek.addRating(h1, new Film(\"Eraserhead\"), Rating.GOOD);\n"
                + "rek.addRating(h2, new Film(\"Eraserhead\"), Rating.BAD);\n"
                + "rek.addRating(h2, new Film(\"Pulp fiction\"), Rating.NEUTRAL);\n"
                + "rek.addRating(h2, new Film(\"Rambo\"), Rating.GOOD);\n"
                + "rek.addRating(h3, new Film(\"Eraserhead\"), Rating.NEUTRAL);\n";

        addRating(rek, h1, newFilm("Pulp fiction"), Rating("FINE"), ratingLuokka);
        addRating(rek, h1, newFilm("Eraserhead"), Rating("GOOD"), ratingLuokka);
        addRating(rek, h2, newFilm("Eraserhead"), Rating("BAD"), ratingLuokka);
        addRating(rek, h2, newFilm("Pulp fiction"), Rating("NEUTRAL"), ratingLuokka);
        addRating(rek, h2, newFilm("Rambo"), Rating("GOOD"), ratingLuokka);
        addRating(rek, h3, newFilm("Eraserhead"), Rating("NEUTRAL"), ratingLuokka);

        String w = v + ""
                + "rek.getRatings(new Film(\"Eraserhead\"));\n";

        List er = getRatings(rek, newFilm("Eraserhead"), v);
        assertTrue("Returned list cannot be null with code\n" + w, er != null);
        List exp = new ArrayList();
        exp.add(Rating("GOOD"));
        exp.add(Rating("NEUTRAL"));
        exp.add(Rating("BAD"));
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);

        w = v + ""
                + "rek.getRatings(new Film(\"Pulp fiction\"));\n";

        er = getRatings(rek, newFilm("Pulp fiction"), v);
        assertTrue("Returned list cannot be null with code\n" + w, er != null);
        exp = new ArrayList();
        exp.add(Rating("NEUTRAL"));
        exp.add(Rating("FINE"));
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);

        w = v + ""
                + "rek.getRatings(new Film(\"Rambo\"));\n";

        er = getRatings(rek, newFilm("Rambo"), v);
        assertTrue("Returned list cannot be null with code\n" + w, er != null);
        exp = new ArrayList();
        exp.add(Rating("GOOD"));
        Collections.sort(exp);
        Collections.sort(er);
        assertEquals("Returned list's length wrong with code\n" + w + "you returned: " + er, exp.size(), er.size());
        assertEquals("Returned list's content wrong with code\n" + w + "you returned: " + er, exp, er);
    }

    @Test
    @Points(Exercise.ID + ".4")
    public void reviewers() throws Throwable {
        String metodi = "reviewers";
        String virhe = "Create method public List<Person> reviewers() for class RatingRegister";

        assertTrue(virhe, _RekisteriRef.method(metodi).returning(List.class).takingNoParams().isPublic());

        String v = "Error with code:\n"
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating( new Person(\"Arto\"), new Film(\"Rambo\"), Rating.BAD);\n"
                + "rek.reviewers();\n";
        _Film e = newFilm("Rambo");
        _Person h = newPerson("Arto");
        _Rekisteri r = newRekisteri();
        _RekisteriRef.method(r, "addRating").returningVoid().taking(_PersonRef.cls(), _FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(h, e, Rating("BAD"));

        _RekisteriRef.method(r, metodi).returning(List.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points(Exercise.ID + ".4")
    public void reviewersWorks() throws Throwable {
        _Rekisteri rek = newRekisteri();
        _Person h1 = newPerson("Pekka");
        _Person h2 = newPerson("Arto");

        String v = ""
                + "RatingRegister rek = new RatingRegister();\n"
                + "Person h1 = new Person(\"Pekka\");\n"
                + "Person h2 = new Person(\"Arto\");\n"
                + "rek.addRating(h1, new Film(\"Pulp fiction\"), Rating.FINE);\n"
                + "rek.addRating(h1, new Film(\"Eraserhead\"), Rating.GOOD);\n"
                + "rek.addRating(h2, new Film(\"Eraserhead\"), Rating.BAD);\n"
                + "rek.addRating(h2, new Film(\"Pulp fiction\"), Rating.NEUTRAL);\n"
                + "rek.addRating(h2, new Film(\"Rambo\"), Rating.GOOD);\n"
                + "rek.reviewers();\n";

        addRating(rek, h1, newFilm("Pulp fiction"), Rating("FINE"), ratingLuokka);
        addRating(rek, h1, newFilm("Eraserhead"), Rating("GOOD"), ratingLuokka);
        addRating(rek, h2, newFilm("Eraserhead"), Rating("BAD"), ratingLuokka);
        addRating(rek, h2, newFilm("Pulp fiction"), Rating("NEUTRAL"), ratingLuokka);
        addRating(rek, h2, newFilm("Rambo"), Rating("GOOD"), ratingLuokka);
        List<_Person> at = reviewers(rek, v);
        assertTrue("Returned list cannot be null with code " + v, at != null);
        assertEquals("Size of the returned list wrong with code " + v + "you returned: " + at + "\n", 2, at.size());
        List<_Person> l = new ArrayList<_Person>();
        l.add(newPerson("Pekka"));
        l.add(newPerson("Arto"));

        assertTrue("Returned list wrong with code " + v + "you returned: " + at, samat(l, at));
    }

    private boolean samat(List<_Person> a, List<_Person> b) {
        for (_Person _person : b) {
            if (!a.contains(_person)) {
                return false;
            }
        }

        return true;
    }
    /*
     *
     */

    @Test
    @Points(Exercise.ID + ".5")
    public void personComparator() throws Throwable {
        Class personComparatorClass = ReflectionUtils.findClass(personComparatorLuokka);

        assertNotNull("Have you created class PersonComparator inside the package reference.comparator?", personComparatorClass);
        assertTrue("Does the class " + personComparatorLuokka + " implement interface Comparator<Person>?", Comparator.class.isAssignableFrom(personComparatorClass));

        try {
            Reflex.reflect(personComparatorClass).constructor().taking(Map.class).isPublic();
        } catch (AssertionError ae) {
            fail("Does the class " + s(personComparatorLuokka) + " have constructor public " + s(personComparatorLuokka) + "(Map<Person, Integer> personIdentities)?");
        }

        _Person pekka = newPerson("Pekka");
        _Person arto = newPerson("Arto");
        _Person mikael = newPerson("Mikael");

        Map ratings = new HashMap();
        ratings.put(pekka, 42);
        ratings.put(arto, 1);
        ratings.put(mikael, 33);

        String v = "Map<Person, Integer> map = new HashMap<Person, Integer>();\n"
                + "Person pekka = new Person(\"Pekka\");\n"
                + "Person arto = new Person(\"Arto\");\n"
                + "Person mikael = new Person(\"Mikael\");\n"
                + "map.put(pekka, 42);\n"
                + "map.put(arto, 1);\n"
                + "map.put(mikael, 33)\n"
                + "PersonComparator comp = new PersonComparator(map);\n";

        Comparator personComparator = (Comparator) Reflex.reflect(personComparatorClass).constructor().taking(Map.class).invoke(ratings);
        _HComp hc = (_HComp) personComparator;

        String w = v + "comp.compare(arto, pekka);\n";
        int ero = compareH(hc, arto, pekka, w);
        assertTrue(w + "expected positive value, you returned: " + ero, ero > 0);

        w = v + "comp.compare(pekka, arto);\n";
        ero = compareH(hc, pekka, arto, w);
        assertTrue(w + "expected negative value, you returned: " + ero, ero < 0);

        w = v + "comp.compare(arto, mikael);\n";
        ero = compareH(hc, arto, mikael, w);
        assertTrue(w + "expected positive value, you returned: " + ero, ero > 0);

        w = v + "comp.compare(mikale, arto);\n";
        ero = compareH(hc, mikael, arto, w);
        assertTrue(w + "expected negative value, you returned: " + ero, ero < 0);

        w = v + "comp.compare(pekka, mikael);\n";
        ero = compareH(hc, pekka, mikael, w);
        assertTrue(w + "expected negative value, you returned: " + ero, ero < 0);

        w = v + "comp.compare(mikale, pekka);\n";
        ero = compareH(hc, mikael, pekka, w);
        assertTrue(w + "expected positive value, you returned: " + ero, ero > 0);
    }

    @Test
    @Points(Exercise.ID + ".6")
    public void filmComparator() throws Throwable {
        Class filmComparatorClass = ReflectionUtils.findClass(filmComparatorLuokka);

        assertNotNull("Have you created class FilmComparator inside the package reference.comparator?", filmComparatorClass);
        assertTrue("Does the class " + filmComparatorLuokka + " implement interface Comparator<Film>?", Comparator.class.isAssignableFrom(filmComparatorClass));

        try {
            Reflex.reflect(filmComparatorClass).constructor().taking(Map.class).requirePublic();
        } catch (AssertionError ae) {
            fail("Does the class " + s(filmComparatorLuokka) + " have constructor public " + s(filmComparatorLuokka) + "(Map<Film, List<Rating>> ratings)?");
        }

        String v = "RatingRegister rek = new RatingRegister();\n"
                + "Film saksiKasi = new Film(\"Saksikäsi\");\n"
                + "Film eraserhead = new Film(\"Eraserhead\");\n"
                + "Film haifisch = new Film(\"Haifisch\");\n"
                + "Person pekka = new Person(\"Pekka\");\n"
                + "rek.addRating(pekka, eraserhead, Rating.FINE);\n"
                + "rek.addRating(pekka, saksiKasi, Rating.BAD);\n"
                + "rek.addRating(saksiKasi, Rating.FINE);\n"
                + "rek.addRating(saksiKasi, Rating.FINE);\n"
                + "rek.addRating(haifisch, Rating.BAD);\n"
                + "rek.addRating(haifisch, Rating.BAD);\n";

        _Rekisteri rek = newRekisteri();
        _Film saksiKasi = newFilm("Saksikäsi");
        _Film eraserhead = newFilm("Eraserhead");
        _Film haifisch = newFilm("Haifisch");
        _Person pekka = newPerson("Pekka");

        addRating(rek, pekka, saksiKasi, Rating("BAD"), v);
        addRating(rek, pekka, eraserhead, Rating("FINE"), v);
        addRating(rek, haifisch, Rating("BAD"), v);
        addRating(rek, haifisch, Rating("BAD"), v);
        addRating(rek, saksiKasi, Rating("FINE"), v);
        addRating(rek, saksiKasi, Rating("FINE"), v);

        Map kaikkiRatingt = filmRatings(rek, v);

        v += "FilmComparator comp = new FilmComparator( rek.filmRatings() );\n";

        Comparator filmComparator = (Comparator) Reflex.reflect(filmComparatorClass).constructor().taking(Map.class).invoke(kaikkiRatingt);
        _EComp hc = (_EComp) filmComparator;

        String w = v + "comp.compare(eraserhead, saksikasi);\n";
        int ero = compareE(hc, eraserhead, saksiKasi, w);
        assertTrue(w + "expected negative value, you returned: " + ero, ero < 0);

        w = v + "comp.compare(saksikasi, eraserhead);\n";
        ero = compareE(hc, saksiKasi, eraserhead, w);
        assertTrue(w + "expected positive value you returned: " + ero, ero > 0);

        w = v + "comp.compare(saksikasi, haifisch);\n";
        ero = compareE(hc, saksiKasi, haifisch, w);
        assertTrue(w + "expected negative value you returned: " + ero, ero < 0);

        w = v + "comp.compare(haifisch, saksikasi);\n";
        ero = compareE(hc, haifisch, saksiKasi, w);
        assertTrue(w + "expected positive value you returned: " + ero, ero > 0);

        w = v + "comp.compare(eraserhead, haifisch);\n";
        ero = compareE(hc, eraserhead, haifisch, w);
        assertTrue(w + "expected negative value you returned: " + ero, ero < 0);

        w = v + "comp.compare( haifisch, eraserhead);\n";
        ero = compareE(hc, haifisch, eraserhead, w);
        assertTrue(w + "expected positive value you returned: " + ero, ero > 0);
    }

    /*
     *
     */
    @Test
    @Points(Exercise.ID + ".7")
    public void isClassReference() {
        String luokanNimi = referenceLuokka;
        _ReferenceRef = Reflex.reflect(luokanNimi);
        assertTrue("Create class Reference inside the package reference", _ReferenceRef.isPublic());
    }

    @Test
    @Points(Exercise.ID + ".7")
    public void noRedundantVariables() {
        saniteettitarkastus(referenceLuokka, 3, "instance variable for RatingRegister");
    }

    @Test
    @Points(Exercise.ID + ".7")
    public void referenceCorrectConstructor() throws Throwable {
        assertTrue("Create constructor public Reference(RatingRegister ratingRegister) for class Reference",
                _ReferenceRef.constructor().taking(_RekisteriRef.cls()).isPublic());

        String luokanNimi = referenceLuokka;
        Class c = ReflectionUtils.findClass(luokanNimi);

        assertEquals("Class Reference should only have one constructor, now there are ", 1, c.getConstructors().length);

        _ReferenceRef.constructor().taking(_RekisteriRef.cls()).withNiceError("\nError caused by code new Reference(\"new RatingRegister()\"); ").invoke(newRekisteri());
    }

    @Test
    @Points(Exercise.ID + ".7")
    public void referenceHasMethodRecommendFilm() throws Throwable {
        String metodi = "recommendFilm";
        String virhe = "Create method public Film recommendFilm(Person person) for class Reference";

        assertTrue(virhe, _ReferenceRef.method(metodi).returning(_FilmRef.cls()).taking(_PersonRef.cls()).isPublic());

        String v = "Error with code:\n"
                + "RatingRegister rek = new RatingRegister();\n"
                + "rek.addRating( new Film(\"Rambo\"), Rating.FINE);\n"
                + "Reference netflix = new Reference(rek);\n"
                + "netflix.recommendFilm(new Person(\"Arto\"));\n";
        _Film e = newFilm("Rambo");
        _Rekisteri r = newRekisteri();
        _RekisteriRef.method(r, "addRating").returningVoid().taking(_FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(e, Rating("FINE"));

        _Reference s = newReference(r);
        assertFalse("Returned recommended film was null with code" + v, suosittele(s, newPerson("arto"), v) == null);
        assertEquals(v, e, suosittele(s, newPerson("arto"), v));
    }

    @Test
    @Points(Exercise.ID + ".7")
    public void recommendedCorrectlyIfPersonHasntRatedAnything() throws Throwable {
        String v = "RatingRegister rek = new RatingRegister();\n"
                + "Film saksiKasi = new Film(\"Saksikäsi\");\n"
                + "Film eraserhead = new Film(\"Eraserhead\");\n"
                + "Film haifisch = new Film(\"Haifisch\");\n"
                + "Person pekka = new Person(\"Pekka\");\n"
                + "rek.addRating(pekka, eraserhead, Rating.FINE);\n"
                + "rek.addRating(pekka, saksiKasi, Rating.BAD);\n"
                + "rek.addRating(saksiKasi, Rating.FINE);\n"
                + "rek.addRating(saksiKasi, Rating.FINE);\n"
                + "rek.addRating(haifisch, Rating.BAD);\n"
                + "rek.addRating(haifisch, Rating.BAD);\n"
                + "Reference reference = new Reference(rek);\n";

        _Rekisteri rek = newRekisteri();
        _Film saksiKasi = newFilm("Saksikäsi");
        _Film eraserhead = newFilm("Eraserhead");
        _Film haifisch = newFilm("Haifisch");
        _Person pekka = newPerson("Pekka");

        addRating(rek, pekka, saksiKasi, Rating("BAD"), v);
        addRating(rek, pekka, eraserhead, Rating("FINE"), v);
        addRating(rek, haifisch, Rating("BAD"), v);
        addRating(rek, haifisch, Rating("BAD"), v);
        addRating(rek, saksiKasi, Rating("FINE"), v);
        addRating(rek, saksiKasi, Rating("FINE"), v);

        _Reference s = newReference(rek);

        v += "reference.recommendFilm(new Person(\"Arto\"));\n";
        assertEquals(v, newFilm("Eraserhead"), suosittele(s, newPerson("Arto"), v));
    }

    @Test
    @Points(Exercise.ID + ".7")
    public void recommendedCorrectlyIfPersonHasntRatedAnything2() throws Throwable {
        String v = "RatingRegister rek = new RatingRegister();\n"
                + "Film saksiKasi = new Film(\"Saksikäsi\");\n"
                + "Film eraserhead = new Film(\"Eraserhead\");\n"
                + "Film haifisch = new Film(\"Haifisch\");\n"
                + "Person pekka = new Person(\"Pekka\");\n"
                + "rek.addRating(pekka, eraserhead, Rating.FINE);\n"
                + "rek.addRating(pekka, saksiKasi, Rating.FINE);\n"
                + "rek.addRating(saksiKasi, Rating.FINE);\n"
                + "rek.addRating(saksiKasi, Rating.GOOD);\n"
                + "rek.addRating(haifisch, Rating.BAD);\n"
                + "rek.addRating(haifisch, Rating.BAD);\n"
                + "Reference reference = new Reference(rek);\n";

        _Rekisteri rek = newRekisteri();
        _Film saksiKasi = newFilm("Saksikäsi");
        _Film eraserhead = newFilm("Eraserhead");
        _Film haifisch = newFilm("Haifisch");
        _Person pekka = newPerson("Pekka");

        addRating(rek, pekka, saksiKasi, Rating("FINE"), v);
        addRating(rek, pekka, eraserhead, Rating("FINE"), v);
        addRating(rek, haifisch, Rating("BAD"), v);
        addRating(rek, haifisch, Rating("BAD"), v);
        addRating(rek, saksiKasi, Rating("FINE"), v);
        addRating(rek, saksiKasi, Rating("GOOD"), v);

        _Reference s = newReference(rek);

        v += "reference.recommendFilm(new Person(\"Arto\"));\n";
        assertEquals(v, newFilm("Saksikäsi"), suosittele(s, newPerson("Arto"), v));
    }

    /*
     *
     */
    //reference 2
    /*
     *
     */
    private int compareE(_EComp ec, _Film h1, _Film h2, String v) throws Throwable {
        Reflex.ClassRef<_EComp> _FilmCompRef = Reflex.reflect(filmComparatorLuokka);
        return _FilmCompRef.method(ec, "compare").returning(int.class).taking(_FilmRef.cls(), _FilmRef.cls()).withNiceError(v).invoke(h1, h2);
    }

    private int compareH(_HComp hc, _Person h1, _Person h2, String v) throws Throwable {
        Reflex.ClassRef<_HComp> _PersonCompRef = Reflex.reflect(personComparatorLuokka);
        return _PersonCompRef.method(hc, "compare").returning(int.class).taking(_PersonRef.cls(), _PersonRef.cls()).withNiceError(v).invoke(h1, h2);
    }

    private List<_Person> reviewers(_Rekisteri rek, String v) throws Throwable {
        return _RekisteriRef.method(rek, "reviewers").returning(List.class).takingNoParams().withNiceError(v).invoke();
    }

    private Map<_Film, _Rating> getPersonalRatings(_Rekisteri rek, _Person h, String v) throws Throwable {
        return _RekisteriRef.method(rek, "getPersonalRatings").returning(Map.class).taking(_PersonRef.cls()).withNiceError(v).invoke(h);
    }

    private void addRating(_Rekisteri rek, _Film e, _Rating a, String v) throws Throwable {
        _RekisteriRef.method(rek, "addRating").returningVoid().taking(_FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(e, a);
    }

    private void addRating(_Rekisteri rek, _Person h, _Film e, _Rating a, String v) throws Throwable {
        _RekisteriRef.method(rek, "addRating").returningVoid().taking(_PersonRef.cls(), _FilmRef.cls(), _RatingRef.cls()).withNiceError(v).invoke(h, e, a);
    }

    private _Rating getRating(_Rekisteri rek, _Person h, _Film e, String v) throws Throwable {
        return _RekisteriRef.method(rek, "getRating").returning(_RatingRef.cls()).taking(_PersonRef.cls(), _FilmRef.cls()).withNiceError(v).invoke(h, e);
    }

    private List<_Rating> getRatings(_Rekisteri rek, _Film e, String v) throws Throwable {
        return (List<_Rating>) _RekisteriRef.method(rek, "getRatings").returning(List.class).taking(_FilmRef.cls()).withNiceError(v).invoke(e);
    }

    private Map<_Film, List<_Rating>> filmRatings(_Rekisteri rek, String v) throws Throwable {

        return (Map<_Film, List<_Rating>>) _RekisteriRef.method(rek, "filmRatings")
                .returning(Map.class).takingNoParams().withNiceError(v).invoke();
    }

    private _Film suosittele(_Reference s, _Person h, String v) throws Throwable {
        return _ReferenceRef.method(s, "recommendFilm").returning(_FilmRef.cls()).taking(_PersonRef.cls()).withNiceError(v).invoke(h);
    }

    public _Rekisteri luoRatingRegister() throws Throwable {
        return _RekisteriRef.constructor().takingNoParams().invoke();
    }

    public _Person newPerson(String name) throws Throwable {
        return _PersonRef.constructor().taking(String.class).invoke(name);
    }

    public _Reference newReference(_Rekisteri rek) throws Throwable {
        return _ReferenceRef.constructor().taking(_RekisteriRef.cls()).invoke(rek);
    }

    public _Film newFilm(String name) throws Throwable {
        return _FilmRef.constructor().taking(String.class).invoke(name);
    }

    public _Rekisteri newRekisteri() throws Throwable {
        return _RekisteriRef.constructor().takingNoParams().invoke();
    }

    private _Rating Rating(String e) {
        String luokanNimi = ratingLuokka;
        Class c = ReflectionUtils.findClass(luokanNimi);
        Object[] vakiot = c.getEnumConstants();

        for (Object vakio : vakiot) {
            if (vakio.toString().equals(e)) {
                return (_Rating) vakio;
            }
        }

        return null;
    }

    private boolean sis(String tunnus, Object[] vakiot) {
        for (Object vakio : vakiot) {
            if (vakio.toString().equals(tunnus)) {
                return true;
            }
        }
        return false;
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("you do not need \"static variables\", remove from clas " + s(klassName) + " the following variable: " + kentta(field.toString(), s(klassName)), field.toString().contains("static") && !field.toString().contains("final"));
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
