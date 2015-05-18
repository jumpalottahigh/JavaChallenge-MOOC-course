package movingfigure;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MovingFigureTest {

    Class figureClass;
    Method figureSiirraMethod;
    Method figurePiirraMethod;
    Constructor figureConstructor;
    Method figureGetXMethod;
    Method figureGetYMethod;
    Class ympyraClass;
    Constructor ympyraConstructor;
    Class piirtoalustaClass;
    Constructor piirtoalustaConstructor;
    Method piirtoalustaPaintComponentMethod;
    Field piirtoalustaFigureField;
    Constructor kayttoliittymaFigureConstructor;
    Method kayttoliittymaLuoKomponentitMethod;
    Method kayttoliittymaLisaaKuuntelijatMethod;
    Class nappaimistonKuuntelijaClass;
    Constructor nappaimistonKuuntelijaConstructor;
    Class nelioClass;
    Constructor nelioConstructor;
    Class laatikkoClass;
    Constructor laatikkoConstructor;
    Class koostefigureClass;
    Constructor koostefigureConstructor;
    Method koostefigureLiitaMethod;

    @Before
    public void setUp() {
        try {
            figureClass = ReflectionUtils.findClass("movingfigure.Figure");
            figureSiirraMethod = ReflectionUtils.requireMethod(figureClass, "move", int.class, int.class);
            figureConstructor = ReflectionUtils.requireConstructor(figureClass, int.class, int.class);
            figureGetXMethod = ReflectionUtils.requireMethod(figureClass, "getX");
            figureGetYMethod = ReflectionUtils.requireMethod(figureClass, "getY");
            figurePiirraMethod = ReflectionUtils.requireMethod(figureClass, "draw", Graphics.class);
        } catch (Throwable t) {
        }

        try {
            ympyraClass = ReflectionUtils.findClass("movingfigure.Circle");
            ympyraConstructor = ReflectionUtils.requireConstructor(ympyraClass, int.class, int.class, int.class);
        } catch (Throwable t) {
        }


        try {
            kayttoliittymaFigureConstructor = ReflectionUtils.requireConstructor(UserInterface.class, figureClass);

            for (Method m : UserInterface.class.getDeclaredMethods()) {
                if (m.getName().equals("createComponents")) {
                    kayttoliittymaLuoKomponentitMethod = m;
                    break;
                }
            }

            kayttoliittymaLuoKomponentitMethod.setAccessible(true);

            for (Method m : UserInterface.class.getDeclaredMethods()) {
                if (m.getName().equals("addListeners")) {
                    kayttoliittymaLisaaKuuntelijatMethod = m;
                    break;
                }
            }
            kayttoliittymaLisaaKuuntelijatMethod.setAccessible(true);
        } catch (Throwable t) {
        }


        try {
            piirtoalustaClass = ReflectionUtils.findClass("movingfigure.DrawingBoard");
            piirtoalustaConstructor = ReflectionUtils.requireConstructor(piirtoalustaClass, figureClass);

            for (Method m : piirtoalustaClass.getDeclaredMethods()) {
                if (m.getName().equals("paintComponent")) {
                    piirtoalustaPaintComponentMethod = m;
                    break;
                }
            }

            piirtoalustaPaintComponentMethod.setAccessible(true);

            for (Field f : piirtoalustaClass.getDeclaredFields()) {
                if (f.getType().equals(figureClass)) {
                    piirtoalustaFigureField = f;
                    break;
                }
            }
            piirtoalustaFigureField.setAccessible(true);

        } catch (Throwable t) {
        }

        try {
            nappaimistonKuuntelijaClass = ReflectionUtils.findClass("movingfigure.KeyboardListener");
            nappaimistonKuuntelijaConstructor = ReflectionUtils.requireConstructor(nappaimistonKuuntelijaClass, Component.class, figureClass);
        } catch (Throwable t) {
        }


        try {
            nelioClass = ReflectionUtils.findClass("movingfigure.Square");
            nelioConstructor = ReflectionUtils.requireConstructor(nelioClass, int.class, int.class, int.class);
        } catch (Throwable t) {
        }



        try {
            laatikkoClass = ReflectionUtils.findClass("movingfigure.Box");
            laatikkoConstructor = ReflectionUtils.requireConstructor(laatikkoClass, int.class, int.class, int.class, int.class);
        } catch (Throwable t) {
        }

        try {
            koostefigureClass = ReflectionUtils.findClass("movingfigure.CompoundFigure");
            koostefigureConstructor = ReflectionUtils.requireConstructor(koostefigureClass);
            koostefigureLiitaMethod = ReflectionUtils.requireMethod(koostefigureClass, "add", figureClass);
        } catch (Throwable t) {
        }

    }

    @Test
    @Points("42.1")
    public void figure() {
        Reflex.ClassRef<Object> ref;
        String luokanNimi = "movingfigure.Figure";
        ref = Reflex.reflect(luokanNimi);
        assertTrue("Create inside the package movingfigure a public abstract class Figure:\n"
                + "public abstract class Figure {", ref.isPublic());

        if (figureClass == null || !Modifier.isAbstract(figureClass.getModifiers())) {
            fail("Have you created abstract class Figure inside the package movingfigure and is the class Figure public?");
        }

        if (figureClass.getDeclaredFields().length != 2) {
            fail("Does the class Figure have only the instance variables private int x and private int y? You do not need any other instance variables.");
        }

        if (figureSiirraMethod == null || Modifier.isAbstract(figureSiirraMethod.getModifiers())) {
            fail("Does the class Figure have non-abstract method public void move(int dx, int dy), which moves the position of the figure?");
        }

        if (figureConstructor == null) {
            fail("Does the class Figure have constructor public Figure(int x, int y)?");
        }

        if (figureGetXMethod == null || !figureGetXMethod.getReturnType().equals(int.class)) {
            fail("Does the class Figure have method public int getX()?");
        }

        if (figureGetYMethod == null || !figureGetYMethod.getReturnType().equals(int.class)) {
            fail("Does the class Figure have method public int getY()?");
        }

        if (figurePiirraMethod == null || !Modifier.isAbstract(figurePiirraMethod.getModifiers())) {
            fail("Does the class Figure have method public abstract void draw(Graphics graphics)?");
        }
    }

    @Test
    @Points("42.2")
    public void circle() {
        if (ympyraClass == null || Modifier.isAbstract(ympyraClass.getModifiers())) {
            fail("Have you created class Circle inside the package movingfigure and is the class Circle public?");
        }

        if (ympyraClass.getDeclaredFields().length != 1) {
            fail("Does the class Circle have only the instance variable private int diameter? You do not need any other instance variables.");
        }

        if (ympyraConstructor == null) {
            fail("Does the class Circle have constructor public Circle(int x, int y, int diameter)?");
        }

        if (!figureClass.isAssignableFrom(ympyraClass)) {
            fail("Does the class Circle inherit class Figure?");
        }

        Object ympyra = luoCircle(5, 50, 500);
    }

    @Test
    @Points("42.3")
    public void drawingBoard() {
        if (piirtoalustaClass == null || Modifier.isAbstract(piirtoalustaClass.getModifiers())) {
            fail("Have you created class DrawingBoard inside the package movingfigure and is the class DrawingBoard public?");
        }

        if (piirtoalustaClass.getDeclaredFields().length != 1) {
            fail("Does the class DrawingBoard have only the instance variable private Figure figure? You do not need any other instance variables.");
        }

        if (piirtoalustaConstructor == null) {
            fail("Does the class DrawingBoard have constructor public DrawingBoard(Figure figure)?");
        }

        if (!JPanel.class.isAssignableFrom(piirtoalustaClass)) {
            fail("Does the class DrawingBoard inherit class JPanel?");
        }

        if (piirtoalustaPaintComponentMethod == null) {
            fail("Does the class DrawingBoard override JPanel's method protected void paintComponent(Graphics graphics)?");
        }

        Object piirtoAlustaCirclella = luoDrawingBoard(luoCircle(10, 20, 3));
        MovingFigureTest.MockGraphics mc = new MovingFigureTest.MockGraphics();
        try {
            ReflectionUtils.invokeMethod(void.class, piirtoalustaPaintComponentMethod, piirtoAlustaCirclella, mc);
        } catch (Throwable ex) {
            fail("Calling DrawingBoard's method paintComponent failed: " + ex.getMessage());
        }

        assertTrue("When calling DrawingBoard's method paintComponent, there you should call superclass' paintComponent-method. \n"
                + "In the beginning of DrawingBoard's method protected void paintComponent(Graphics graphics), check that you call super.paintComponent(graphics);", mc.getKutsut().size() > 1);
        assertTrue("When calling DrawingBoard's method paintComponent, you should call "
                + "draw-method of the figure that was given in DrawingBoard's constructor. Now it wasn't called.", mc.getKutsut().contains("fillOval(10, 20, 3, 3)"));

        if (piirtoalustaFigureField == null) {
            fail("Does the class DrawingBoard have instance variable private Figure figure?");
        }
    }

    @Test
    @Points("42.3")
    public void drawingBoardInUserInterface() throws IllegalArgumentException, IllegalAccessException {
        if (kayttoliittymaFigureConstructor == null) {
            fail("Did you modify class UserInterface so that it has a constructor where it is given a Figure-object as a parameter?");
        }

        if (kayttoliittymaLuoKomponentitMethod == null
                || kayttoliittymaLuoKomponentitMethod.getParameterTypes().length != 1
                || kayttoliittymaLuoKomponentitMethod.getParameterTypes()[0].equals(Component.class)) {
            fail("Does the class UserInterface have method private void createComponents(Container container), where components are added to user interface?");
        }

        Object ympyra = luoCircle(10, 10, 50);
        UserInterface kali = null;
        try {
            kali = (UserInterface) ReflectionUtils.invokeConstructor(kayttoliittymaFigureConstructor, ympyra);
        } catch (Throwable ex) {
            fail("Does the user interface have constructor public UserInterface(Figure figure) and is the class UserInterface public itself?");
        }

        for( Field f: UserInterface.class.getDeclaredFields() ){
            String mj = f.toString();
            if ( mj.contains("JFrame")) {
                f.setAccessible(true);
                f.set(kali, new MovingFigureTest.JFrameMock());
            }
        }

        MovingFigureTest.MockContainer container = new MovingFigureTest.MockContainer();
        try {
            ReflectionUtils.invokeMethod(void.class, kayttoliittymaLuoKomponentitMethod, kali, container);
        } catch (Throwable ex) {
            fail("Calling method createComponents failed\n"
                    + "additional information" + ex.getMessage());
        }

        assertNotNull("Do you add drawing board to user interface in UserInterface's method createComponents?", container.lisatty);
        Object piirtoalusta = container.lisatty;
        Object ympyraDrawingBoardsta = null;

        try {
            ympyraDrawingBoardsta = piirtoalustaFigureField.get(piirtoalusta);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MovingFigureTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MovingFigureTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertEquals("Do you set the figure given in user interface to drawing board?", ympyra, ympyraDrawingBoardsta);
    }

    @Test
    @Points("42.4")
    public void keyboardListener() {
        if (nappaimistonKuuntelijaClass == null) {
            fail("Have you created class KeyboardListener inside the package movingfigure and is the class KeyboardListener public?");
        }

        if (nappaimistonKuuntelijaConstructor == null) {
            fail("Does the class KeyboardListener have constructor public KeyboardListener(Component component, Figure figure)?");
        }

        if (!KeyListener.class.isAssignableFrom(nappaimistonKuuntelijaClass)) {
            fail("Does the class KeyboardListener implement interface KeyListener?");
        }

        boolean loytyi = false;
        for (Method m : nappaimistonKuuntelijaClass.getDeclaredMethods()) {
            if (m.getName().equals("keyPressed")) {
                loytyi = true;
                break;
            }
        }

        assertTrue("Have you implemented method public void keyPressed(KeyEvent e) in class KeyboardListener?", loytyi);

        MovingFigureTest.MockComponent mockComponent = new MovingFigureTest.MockComponent();
        Object ympyra = luoCircle(40, 20, 10);

        KeyListener kuuntelija = null;
        try {
            kuuntelija = (KeyListener) ReflectionUtils.invokeConstructor(nappaimistonKuuntelijaConstructor, mockComponent, ympyra);
        } catch (Throwable ex) {
            fail("Creating KeyboardListener failed, error: " + ex.getMessage() + ". Does the class KeyboardListener have constructor public KeyboardListener(Component component, Figure figure)?");
        }

        kuuntelija.keyPressed(new MovingFigureTest.MockKeyEvent(KeyEvent.VK_LEFT));
        assertTrue("Do you call drawing board's repaint-method after every key press?", mockComponent.repaintKutsuttu);


        int palautettuX = -1, palautettuY = -1;
        try {
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, ympyra);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, ympyra);
        } catch (Throwable t) {
            fail("Do methods getX and getY, which are inherited from Figure, work in the class Circle? Error: " + t.getMessage());
        }

        assertTrue("When user presses left, figure's x-coordinate should decrease. Y-coordinate shouldn't change.", palautettuX < 40 && palautettuY == 20);


        kuuntelija.keyPressed(new MovingFigureTest.MockKeyEvent(KeyEvent.VK_RIGHT));
        try {
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, ympyra);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, ympyra);
        } catch (Throwable t) {
            fail("Do methods getX and getY, which are inherited from Figure, work in the class Circle? Error: " + t.getMessage());
        }

        assertTrue("When user presses right, figure's x-coordinate should increase. Y-coordinate shouldn't change. Notice that the change should be the same for both directions\n"
                + " if x-coordinate is decreased by 1 when moving left, x-coordinate should be increased by 1 when moving right.", palautettuX == 40 && palautettuY == 20);

        kuuntelija.keyPressed(new MovingFigureTest.MockKeyEvent(KeyEvent.VK_UP));
        try {
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, ympyra);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, ympyra);
        } catch (Throwable t) {
            fail("Do methods getX and getY, which are inherited from Figure, work in the class Circle? Error: " + t.getMessage());
        }

        assertTrue("When user presses up, figure's y-coordinate should decrease. \n"
                + "This is because when drawing, y-coordinate increases downwards. Figure's x-coordinate shouldn't change.", palautettuX == 40 && palautettuY < 20);

        kuuntelija.keyPressed(new MovingFigureTest.MockKeyEvent(KeyEvent.VK_DOWN));
        try {
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, ympyra);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, ympyra);
        } catch (Throwable t) {
            fail("Do methods getX and getY, which are inherited from Figure, work in the class Circle? Error: " + t.getMessage());
        }

        assertTrue("When user presses down, figure's y-coordinate should increase. \n"
                + "This is because when drawing, y-coordinate increases downwards. \n"
                + "Figure's x-coordinate shouldn't change. Check also that y-coordinate is decreased and increased by 1 when going up or down.", palautettuX == 40 && palautettuY == 20);
    }

    @Test
    @Points("42.4")
    public void keyboardListenerInUserInterface() throws IllegalArgumentException, IllegalAccessException {
        if (kayttoliittymaFigureConstructor == null) {
            fail("Did you modify class UserInterface so that it has a constructor where it is given a Figure-object as a parameter?");
        }

        if (kayttoliittymaLisaaKuuntelijatMethod == null
                || kayttoliittymaLisaaKuuntelijatMethod.getParameterTypes().length != 0) {
            fail("Does the class UserInterface have method private void addListeners(), where event listeners are added to user interface?");
        }

        Object ympyra = luoCircle(40, 20, 10);
        UserInterface kali = null;
        try {
            kali = (UserInterface) ReflectionUtils.invokeConstructor(kayttoliittymaFigureConstructor, ympyra);
        } catch (Throwable ex) {
            fail("Does the user interface have constructor public UserInterface(Figure figure) and is the class UserInterface public itself?");
        }

        for (Field f : UserInterface.class.getDeclaredFields()) {
            if (f.toString().contains("JFrame")) {
                f.setAccessible(true);
                f.set(kali, new JFrame());
            }
        }

        try {
            ReflectionUtils.invokeMethod(void.class, kayttoliittymaLuoKomponentitMethod, kali, new Container());
        } catch (Throwable ex) {
            fail("Calling method createComponents failed: " + ex.getMessage());
        }

        try {
            ReflectionUtils.invokeMethod(void.class, kayttoliittymaLisaaKuuntelijatMethod, kali);
        } catch (Throwable ex) {
            fail("Calling method addListeners failed: " + ex.getMessage());
        }
        int nappaintenKuuntelijoita = kali.getFrame().getKeyListeners().length;

        assertTrue("Do you add keyboard listener to user interface in method addListeners()?",  nappaintenKuuntelijoita>0);



    }

    @Test
    @Points("42.5")
    public void square() {
        if (nelioClass == null || Modifier.isAbstract(nelioClass.getModifiers())) {
            fail("Have you created class Square inside the package movingfigure and is the class Square public?");
        }

        if (nelioClass.getDeclaredFields().length != 1) {
            fail("Does the class Square have only the instance variable private int sideLength? You do not need any other instance variables.");
        }

        if (nelioConstructor == null) {
            fail("Does the class Square have constructor public Square(int x, int y, int sideLength)?");
        }

        if (!figureClass.isAssignableFrom(nelioClass)) {
            fail("Does the class Square inherit class Figure?");
        }

        Object nelio = luoSquare(50, 75, 100);
    }

    @Test
    @Points("42.5")
    public void box() {
        if (laatikkoClass == null || Modifier.isAbstract(laatikkoClass.getModifiers())) {
            fail("Have you created class Box inside the package movingfigure and is the class Box public?");
        }

        if (laatikkoClass.getDeclaredFields().length != 2) {
            fail("Does the class Box have only the instance variables private int width ja private int height? You do not need any other instance variables.");
        }

        if (laatikkoConstructor == null) {
            fail("Does the class Box have constructor public Box(int x, int y, int width, int height)?");
        }

        if (!figureClass.isAssignableFrom(laatikkoClass)) {
            fail("Does the class Box inherit class Figure?");
        }

        Object laatikko = luoBox(50, 75, 100, 125);
    }

    @Test
    @Points("42.6")
    public void compoundFigure() {
        if (koostefigureClass == null || Modifier.isAbstract(koostefigureClass.getModifiers())) {
            fail("Have you created class CompoundFigure inside the package movingfigure and is the class CompoundFigure public?");
        }

        if (koostefigureClass.getDeclaredFields().length != 1) {
            fail("Does the class CompoundFigure have only one instance variable which is a list of figures that make up the compound figure?");
        }

        if (koostefigureConstructor == null) {
            fail("Does the class CompoundFigure have constructor public CompoundFigure() and is the class public?");
        }

        if (!figureClass.isAssignableFrom(koostefigureClass)) {
            fail("Does the class CompoundFigure inherit class Figure?");
        }


        Object laatikko = luoBox(50, 75, 100, 125);
        Object ympyra = luoCircle(10, 20, 30);

        metodiLisaa(ympyra);
        Object koostefigure = luoCompoundFigure(laatikko, ympyra);
        MovingFigureTest.MockGraphics mc = new MovingFigureTest.MockGraphics();

        try {
            ReflectionUtils.invokeMethod(void.class, figurePiirraMethod, koostefigure, mc);
        } catch (Throwable ex) {
            fail("Calling method " + figurePiirraMethod.getName() + " failed: " + ex.getMessage());
        }

        String kutsut = "";
        for (String k : mc.kutsut) {
            kutsut += " "+k;
        }

        assertTrue("CompoundFigure kk = new CompoundFigure();\n"
                + "kk.add(new Box(50, 75, 100, 125));\n"
                + "kk.add(new Circle(10, 20, 30));\n"
                + "kk.draw();\n"
                + "When drawing CompoundFigure, you have to draw all the figures it consists of. \n"
                + "You can call for each figure their draw-method. \n"
                + "you called: \n"+kutsut, mc.kutsut.contains("fillRect(50, 75, 100, 125)"));
        assertTrue("CompoundFigure kk = new CompoundFigure();\n"
                + "kk.add(new Box(50, 75, 100, 125));\n"
                + "kk.add(new Circle(10, 20, 30));\n"
                + "kk.draw();\n"
                + "When drawing CompoundFigure, you have to draw all the figures it consists of. \n"
                + "You can call for each figure their draw-method. \n"
                + "you called: \n"+kutsut, mc.kutsut.contains("fillOval(10, 20, 30, 30)"));

        int laatikkoX = -1, ympyraX = -1;
        try {
            ReflectionUtils.invokeMethod(void.class, figureSiirraMethod, koostefigure, 1, 0);
            laatikkoX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, laatikko);
            ympyraX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, ympyra);
        } catch (Throwable t) {
            fail("Error with code "+"CompoundFigure kk = new CompoundFigure();\n"
                + "kk.add(new Box(50, 75, 100, 125));\n"
                + "kk.add(new Circle(10, 20, 30));\n"
                + "kk.move(1,0)\n"+
                    "Additional information: " + t.getMessage());
        }

        assertEquals("When moving CompoundFigure you have to move all the figures it consists of. \n"
                + "So you have to overwrite the inherited move-method\n"+
                "After executing code\n"
                + "CompoundFigure kk = new CompoundFigure();\n"
                + "kk.add(new Box(50, 75, 100, 125));\n"
                + "kk.add(new Circle(10, 20, 30));\n"
                + "kk.move(1,0)\n"
                + "the x-coordinate of the box: ", 51, laatikkoX );
        assertEquals("When moving CompoundFigure you have to move all the figures it consists of. \n"
                + "So you have to overwrite the inherited move-method\n"+
                "After executing code\n"
                + "CompoundFigure kk = new CompoundFigure();\n"
                + "kk.add(new Box(50, 75, 100, 125));\n"
                + "kk.add(new Circle(10, 20, 30));\n"
                + "kk.move(1,0)\n"
                + "the x-coordinate of the circle: ", 11, ympyraX );


    }

    private Object luoDrawingBoard(Object figure) {
        try {
            return ReflectionUtils.invokeConstructor(piirtoalustaConstructor, figure);
        } catch (Throwable ex) {
            Logger.getLogger(MovingFigureTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private Object luoCircle(int x, int y, int diameter) {
        Object ympyra = null;

        try {
            ympyra = ReflectionUtils.invokeConstructor(ympyraConstructor, x, y, diameter);
        } catch (Throwable ex) {
            fail("Is the class Circle public and is its constructor public?");
        }

        tarkistaSijainti("Circle", ympyra, x, y);
        tarkistaSiirtyminen("Circle", ympyra, x, y);
        tarkistaFigurenPiirtaminen("new Circle(" + x + "," + y + "," + diameter + ")", "Circle", ympyra, "fillOval(" + x + ", " + y + ", " + diameter + ", " + diameter + ")");

        return ympyra;
    }

    private Object luoSquare(int x, int y, int sideLength) {
        Object nelio = null;

        try {
            nelio = ReflectionUtils.invokeConstructor(nelioConstructor, x, y, sideLength);
        } catch (Throwable ex) {
            fail("Is the class Square public and is its constructor public?");
        }

        tarkistaSijainti("Square", nelio, x, y);
        tarkistaSiirtyminen("Square", nelio, x, y);
        tarkistaFigurenPiirtaminen("new Square(", +x + ", " + y + ", " + sideLength + ")", nelio, "fillRect(" + x + ", " + y + ", " + sideLength + ", " + sideLength + ")");

        return nelio;
    }

    private Object luoBox(int x, int y, int leveys, int korkeus) {
        Object laatikko = null;

        try {
            laatikko = ReflectionUtils.invokeConstructor(laatikkoConstructor, x, y, leveys, korkeus);
        } catch (Throwable ex) {
            fail("Is the class Box public and is its constructor public?");
        }

        tarkistaSijainti("Box", laatikko, x, y);
        tarkistaSiirtyminen("Box", laatikko, x, y);
        tarkistaFigurenPiirtaminen("new Box(" + x + ", " + y + ", " + leveys + ", " + korkeus + ")", "Box", laatikko, "fillRect(" + x + ", " + y + ", " + leveys + ", " + korkeus + ")");

        return laatikko;
    }

    private void metodiLisaa(Object figure) {
        Object koostefigure = null;

        try {
            koostefigure = ReflectionUtils.invokeConstructor(koostefigureConstructor);
        } catch (Throwable ex) {
            fail("Is the class CompoundFigure public and is its constructor public?");
        }

        try {
            ReflectionUtils.invokeMethod(koostefigureClass, koostefigureLiitaMethod, koostefigure, figure);
        } catch (Throwable ex) {
            fail("Does the class CompoundFigure have method public void add(Figure figure)?\n"
                    + "\n"
                    + "if yes, check code\n"
                    + "CompoundFigure kk = new CompoundFigure();\n"
                    + "kk.add( new Circle(10, 20, 30) );\n\nAdditional information: "+ex);
        }

    }

    private Object luoCompoundFigure(Object... figuret) {
        Object koostefigure = null;

        try {
            koostefigure = ReflectionUtils.invokeConstructor(koostefigureConstructor);
        } catch (Throwable ex) {
            fail("Is the class CompoundFigure public and is its constructor public?");
        }

        for (Object figure : figuret) {
            try {
                ReflectionUtils.invokeMethod(koostefigureClass, koostefigureLiitaMethod, koostefigure, figure);
            } catch (Throwable ex) {
                fail("Adding figure to compound figure failed. ");
            }
        }

        return koostefigure;
    }

    private void tarkistaSijainti(String luokka, Object figure, int x, int y) {

        int palautettuX = -1, palautettuY = -1;
        try {
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, figure);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, figure);
        } catch (Throwable t) {
            fail("Do methods getX and getY, which are inherited from Figure, work in class " + luokka + "?");
        }

        assertEquals("Class Figure's constructor or method getX isn't correctly implemented!\n"
                + "When class " + luokka + " was given value " + x + " for the x-coordinate in the constructor, created object's method getX() returned " + palautettuX, x, palautettuX);
        assertEquals("Class Figure's constructor or method getY aren't correctly implemented!\n"
                + "When class " + luokka + " was given value " + y + ", for the y-coordinate in the constructor, created object's method getY() returned " + palautettuY, y, palautettuY);

    }

    private void tarkistaSiirtyminen(String luokka, Object figure, int x, int y) {
        int palautettuX = -1, palautettuY = -1;
        try {
            ReflectionUtils.invokeMethod(void.class, figureSiirraMethod, figure, 0, 1);
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, figure);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, figure);
        } catch (Throwable t) {
        }

        assertEquals("Class Figure's method move isn't correctly implemented!\n"
                + "When " + luokka + "-object's method move is called with values (0, 1), x-coordinate shouldn't change.", x, palautettuX);
        assertEquals("Class Figure's method move isn't correctly implemented!\n"
                + "When " + luokka + "-object's method move is called with values (0, 1), y-coordinate should increase by 1.", y + 1, palautettuY);

        try {
            ReflectionUtils.invokeMethod(void.class, figureSiirraMethod, figure, 0, -1);
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, figure);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, figure);
        } catch (Throwable t) {
        }

        assertEquals("When " + luokka + "-object's method move is called with values (0, -1), x-coordinate shouldn't change.", x, palautettuX);
        assertEquals("When " + luokka + "-object's method move is called with values (0, -1), y-coodinate should decrease by 1.", y, palautettuY);

        try {
            ReflectionUtils.invokeMethod(void.class, figureSiirraMethod, figure, 1, 0);
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, figure);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, figure);
        } catch (Throwable t) {
        }


        assertEquals("When " + luokka + "-object's method move is called with values (1, 0), x-coordinate should increase by 1.", x + 1, palautettuX);
        assertEquals("When " + luokka + "-object's method move is called with values (1, 0), y-coordinate shouldn't change.", y, palautettuY);

        try {
            ReflectionUtils.invokeMethod(void.class, figureSiirraMethod, figure, -1, 0);
            palautettuX = ReflectionUtils.invokeMethod(int.class, figureGetXMethod, figure);
            palautettuY = ReflectionUtils.invokeMethod(int.class, figureGetYMethod, figure);
        } catch (Throwable t) {
        }


        assertEquals("When " + luokka + "-object's method move is called with values (-1, 0), x-coordinate should decrease by 1.", x, palautettuX);
        assertEquals("When " + luokka + "-object's method move is called with values (-1, 0), y-coordinate shouldn't change.", y, palautettuY);
    }

    private void tarkistaFigurenPiirtaminen(String luokka, Object figure, String... kutsut) {
        MovingFigureTest.MockGraphics mc = new MovingFigureTest.MockGraphics();
        try {
            ReflectionUtils.invokeMethod(void.class, figurePiirraMethod, figure, mc);
        } catch (Throwable ex) {
            fail("Calling method " + figurePiirraMethod.getName() + " failed: " + ex.getMessage());
        }

        for (String kutsu : kutsut) {
            assertTrue("When drawing an instance of class " + luokka + ", there should have been a call " + kutsu + " with given values, now it didn't happen. \n"
                    + "Check that you use the right method and that you give correct parameters.", mc.getKutsut().contains(kutsu));
        }
    }

    private void tarkistaFigurenPiirtaminen(String ilm, String luokka, Object figure, String... kutsut) {
        MovingFigureTest.MockGraphics mc = new MovingFigureTest.MockGraphics();
        try {
            ReflectionUtils.invokeMethod(void.class, figurePiirraMethod, figure, mc);
        } catch (Throwable ex) {
            fail("Calling method " + figurePiirraMethod.getName() + " failed: " + ex.getMessage());
        }

        String k = "";
        for (String string : mc.getKutsut()) {
            k += " " + string;
        }

        for (String kutsu : kutsut) {
            assertTrue("When drawing an instance of class " + ilm + ", there should have been a call " + kutsu + " with given values, now it didn't happen. \n"
                    + "You called:\n"
                    + k, mc.getKutsut().contains(kutsu));
        }
    }

    private void superi(Class clas) {
        String file = clas.getName().replaceAll("\\.", "/");

        boolean ok = false;
        try {
            Scanner lukija = new Scanner(new File(file));

            while (lukija.hasNextLine()) {

                String rivi = lukija.nextLine();

                if (rivi.contains("super.paintComponent")) {
                    ok = true;
                }

            }

        } catch (Exception e) {
        }

        String f = clas.getName();
        f = f.substring(f.lastIndexOf('.') + 1);

        Assert.assertTrue("Add in the beginning of method paintComponent a method call super.paintComponent(graphics); in class " + f, ok);

    }

    private class MockGraphics extends Graphics {

        private java.util.List<String> kutsut = new ArrayList();

        @Override
        public Graphics create() {
            kutsut.add("create()");
            return this;
        }

        @Override
        public void translate(int i, int i1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Color getColor() {
            return Color.BLACK;
        }

        @Override
        public void setColor(Color color) {
        }

        @Override
        public void setPaintMode() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setXORMode(Color color) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Font getFont() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setFont(Font font) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public FontMetrics getFontMetrics(Font font) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Rectangle getClipBounds() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clipRect(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setClip(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Shape getClip() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setClip(Shape shape) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void copyArea(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawLine(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillRect(int i, int i1, int i2, int i3) {
            kutsut.add("fillRect(" + i + ", " + i1 + ", " + i2 + ", " + i3 + ")");
        }

        @Override
        public void clearRect(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawOval(int i, int i1, int i2, int i3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillOval(int i, int i1, int i2, int i3) {
            kutsut.add("fillOval(" + i + ", " + i1 + ", " + i2 + ", " + i3 + ")");
        }

        @Override
        public void drawArc(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillArc(int i, int i1, int i2, int i3, int i4, int i5) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawPolyline(int[] ints, int[] ints1, int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawPolygon(int[] ints, int[] ints1, int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fillPolygon(int[] ints, int[] ints1, int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawString(String string, int i, int i1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void drawString(AttributedCharacterIterator aci, int i, int i1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, int i2, int i3, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, Color color, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, int i2, int i3, Color color, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver io) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void dispose() {
        }

        public java.util.List<String> getKutsut() {
            return kutsut;
        }
    }

    private class MockContainer extends Container {

        private Component lisatty;

        public MockContainer() {
        }

        @Override
        public Component add(Component comp) {
            this.lisatty = comp;
            return this.lisatty;
        }

        @Override
        public void add(Component comp, Object constraints) {
            this.lisatty = comp;
        }

        @Override
        public Component add(Component comp, int index) {
            this.lisatty = comp;
            return this.lisatty;
        }

        @Override
        public Component add(String name, Component comp) {
            this.lisatty = comp;
            return this.lisatty;
        }

        @Override
        public void add(Component comp, Object constraints, int index) {
            this.lisatty = comp;
        }

        public Component getLisatty() {
            return lisatty;
        }
    }

    private class MockComponent extends Component {

        boolean repaintKutsuttu;

        @Override
        public void repaint() {
            repaintKutsuttu = true;
        }
    }

    private class MockKeyEvent extends KeyEvent {

        public MockKeyEvent(int keyCode) {
            super(new MovingFigureTest.MockComponent(), keyCode, 1, 1, keyCode);
        }
    }

    public class JFrameMock extends JFrame{

        @Override
        public synchronized void addKeyListener(KeyListener kl) {

        }

    }
}
