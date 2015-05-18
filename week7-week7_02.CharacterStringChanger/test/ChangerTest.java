import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;


public class ChangerTest<_Changer, _Change>  {
    @Test
    @Points("2.1")
    public void testWhetherChangeExists() throws Throwable {
        createChange('a', 'b');
    }

    @Test
    @Points("2.1")
    public void testChange() throws Throwable {
        testChange('a', 'o', "halapakka");
        testChange('!', '#', "#!/bin/bash");
        testChange('U', 'u', "UU");
    }

    @Test
    @Points("2.2")
    public void testWhetherChangerExists() throws Throwable {
        createChanger();
    }

    @Test
    @Points("2.2")
    public void testChanger() throws Throwable {
        Map<Character, Character> muunnokset =
                new HashMap<Character, Character>();
        muunnokset.put('A', 'I');
        muunnokset.put('i', 'u');
        muunnokset.put('e', 'a');
        muunnokset.put('!', '?');
        testChange(muunnokset, "Architect!");
    }


    private void testChange(char merkki1, char merkki2, String merkkijono) throws Throwable {
        String muunnettu = changeWithChange(merkkijono, merkki1, merkki2);
        String vastaus = merkkijono.replace(merkki1, merkki2);

        if (!muunnettu.equals(vastaus)) {
            fail("Method change(\"" + merkkijono +
                    "\") was called to object Change('" + merkki1 + "', '" + merkki2 +
                    "') and it returned \"" + muunnettu +
                    "\", although it should have returned \"" + vastaus + "\".");
        }
    }

    private String changeWithChange(String merkkijono, char m1, char m2) throws Throwable {
        String klassName = "Change";
        Reflex.ClassRef<Object> klass = Reflex.reflect(klassName);
        String metodi = "change";

        Object olio = klass.constructor().taking(char.class, char.class).invoke(m1, m2);

        assertTrue("Create method public String " + metodi + "(String characterString) to class " + klassName, klass.method(olio, metodi)
                .returning(String.class).taking(String.class).isPublic());

        String e = "code m = Change('"+m1+"','"+m2+"'); m.change(\""+merkkijono+"\"); caused an exception";


        return klass.method(olio, metodi)
                .returning(String.class).taking(String.class).withNiceError(e).invoke(merkkijono);
    }

    private Object createChange(char merkki1, char merkki2) throws Throwable {
        String klassName = "Change";
        Reflex.ClassRef<Object> klass = Reflex.reflect(klassName);
        assertTrue("Class " + klassName + " has to be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());

        Reflex.MethodRef2<Object, Object, Character, Character> ctor = klass.constructor().taking(char.class, char.class).withNiceError();
        assertTrue("Define class " + klassName + " a public constructor: public " + klassName + "(char fromCharacter, char toCharacter)", ctor.isPublic());
        return ctor.invoke(merkki1, merkki2);
    }

    private void testChange(Map<Character, Character> muunnokset, String merkkijono) throws Throwable {
        _Changer muuntaja = createChanger();

        Iterator<Map.Entry<Character, Character>> it = muunnokset.entrySet().iterator();
        String vastaus = merkkijono;
        String muunnoksetTekstina = "";
        while (it.hasNext()) {
            Map.Entry<Character, Character> entry = it.next();
            char merkki1 = entry.getKey();
            char merkki2 = entry.getValue();

            addChange(muuntaja, merkki1, merkki2);

            vastaus = vastaus.replace(merkki1, merkki2);

            muunnoksetTekstina += "('" + merkki1 + "' -> '" + merkki2 + "'), ";
        }

        String muunnettu = changeWithChanger(muuntaja, merkkijono);

        if (!muunnettu.equals(vastaus)) {
            fail("Class Changer was given changes " + muunnoksetTekstina +
                    "and was called method change(\"" + merkkijono +
                    "\") which returned \"" + muunnettu +
                    "\", although it should have returned \"" + vastaus + "\".");
        }
    }

    private _Changer createChanger() throws Throwable {
        String klassName = "Changer";
        Reflex.ClassRef<_Changer> klass = Reflex.reflect(klassName);
        assertTrue("Class " + klassName + " has to be public, so it must be defined as\npublic class " + klassName + " {...\n}", klass.isPublic());

        Reflex.MethodRef0<_Changer, _Changer> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Define class " + klassName + " a public constructor: public " + klassName + "()", ctor.isPublic());
        return ctor.invoke();
    }

    private void addChange(_Changer mtaja, char m1, char m2) throws Throwable {
        Reflex.ClassRef<_Changer> _ChangerRef = Reflex.reflect("Changer");

        Reflex.ClassRef<_Change> _ChangeRef = Reflex.reflect("Change");

        _Change mnos = _ChangeRef.constructor().taking(char.class, char.class).invoke(m1, m2);

        Class<_Change> c = _ChangeRef.cls();

        String v = "\nChanger m = new Changer(); Change mnt = new Change('"+m1+"','"+m2+"'); m.addChange(mnt);";

        _ChangerRef.method(mtaja, "addChange").returningVoid().taking(c).withNiceError(v).invoke(mnos);


    }

    private String changeWithChanger(Object muuntaja, String merkkijono) {
        Method metodi;
        try {
            metodi = muuntaja.getClass().getMethod("change", String.class);
        } catch (Exception e) {
            fail("Class Changer doesn't have method: public String change(String characterString).");
            return null;
        }

        if (!metodi.getReturnType().equals(String.class)) {
            fail("Method change(String characterString) of the class Changer has to return a string.");
            return null;
        }

        try {
            return (String) metodi.invoke(muuntaja, merkkijono);
        } catch (Exception e) {
            fail("There was an exception in the method change of class Changer: " + e.toString());
            return null;
        }
    }
}