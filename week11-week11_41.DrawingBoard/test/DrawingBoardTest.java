
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.io.File;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JPanel;
import org.junit.Assert;
import org.junit.Test;

@Points("41")
public class DrawingBoardTest {

    public static final String LUOKAN_NIMI =
            "drawing.ui.DrawingBoard";

    @Test
    public void hasDrawingBoard() {
        luoDrawingBoard();
    }

    @Test
    public void sizeOK() {
        Assert.assertTrue("you cannot change the content of the UserInterface!",
                sisaltaa("newDimension(400,400)"));

    }

    @Test
    public void drawingBoardUsingCorrectMethods() {
        JPanel piirtoalusta = luoDrawingBoard();
        Method paintCompoMethod = null;
        for (Method m : piirtoalusta.getClass().getDeclaredMethods()) {
            if (!m.getName().equals("paintComponent")) {
                continue;
            }

            try {
                m.setAccessible(true);
            } catch (Exception e) {
                Assert.fail("Method " + m.getName() + " mustn't be set to public.");
            }

            paintCompoMethod = m;
        }

        try {
            paintCompoMethod.invoke(piirtoalusta, new DrawingBoardTest.MockGraphics());
        } catch (UnsupportedOperationException e) {
            Assert.fail("Do you use in " + LUOKAN_NIMI + "'s method paintComponent only call super.paintComponent(graphics) and graphics-object's method fillRect?");
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void usingFillRectMethod() {
        JPanel piirtoalusta = luoDrawingBoard();
        Method paintCompoMethod = null;
        for (Method m : piirtoalusta.getClass().getDeclaredMethods()) {
            if (!m.getName().equals("paintComponent")) {
                continue;
            }

            try {
                m.setAccessible(true);
            } catch (Exception e) {
                Assert.fail("Method " + m.getName() + " mustn't be set to public.");
            }

            paintCompoMethod = m;
        }

        DrawingBoardTest.MockGraphics graphics = new DrawingBoardTest.MockGraphics();
        try {
            paintCompoMethod.invoke(piirtoalusta, graphics);
        } catch (UnsupportedOperationException e) {
            Assert.fail("Do you use in " + LUOKAN_NIMI + "'s method paintComponent only call super.paintComponent(graphics) and graphics-object's method fillRect?");
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        Assert.assertTrue("Do you use in " + LUOKAN_NIMI + "'s method paintComponent the method graphics.fillRect()?", graphics.rects.size() > 1);
    }

    @Test
    public void drawingSmiley() {

        int koko = 32;

        BufferedImage piirretty = luoKuva();

        Raster piirrettySkaalattu = mustavalkoRaster(skaalaaKuva(piirretty, koko, koko));
        int piirrettyMinimi = minimiArvo(piirrettySkaalattu);
        int piirrettyMaksimi = maksimiArvo(piirrettySkaalattu);

        Assert.assertFalse("Are you drawing something?", piirrettyMinimi == piirrettyMaksimi);

        int smileyVaaleita = 0;
        int samojaVaaleita = 0;

        int smileyTummia = 0;
        int samojaTummia = 0;

        for (int i = 0; i < koko * koko; i++) {
            int x = i % koko;
            int y = i / koko;

            int piirrettyValue = piirrettySkaalattu.getSample(x, y, 0);

            if (DrawingBoardTest.Kuva.data[x][y] == 1) {
                smileyTummia++;
            }

            if (DrawingBoardTest.Kuva.data[x][y] == 1 && piirrettyMinimi == piirrettyValue) {
                samojaTummia++;
                continue;
            }

            if (DrawingBoardTest.Kuva.data[x][y] == 0) {
                smileyVaaleita++;
            }

            if (DrawingBoardTest.Kuva.data[x][y] == 0 && piirrettyMaksimi == piirrettyValue) {
                samojaVaaleita++;
                continue;
            }
        }

        double vaaleistaOikein = 1.0 * samojaVaaleita / smileyVaaleita;
        double tummistaOikein = 1.0 * samojaTummia / smileyTummia;


        Assert.assertTrue("Light spots of the image should be at least 80% the same as in the example image. Example image can be found from the project directory with name smiley.gif. Now your image's light spots were about " + (vaaleistaOikein * 100) + "% correct. ", vaaleistaOikein >= 0.8);
        Assert.assertTrue("Dark spots of the image should be at least 80% the same as in the example image. Example image can be found from the project directory with name smiley.gif. Now your image's dark spots were about " + (tummistaOikein * 100) + "% correct. ", tummistaOikein >= 0.8);

    }

    @Test
    public void smileyDrawnWithFiveFillRectCalls() {
        JPanel piirtoalusta = luoDrawingBoard();
        Method paintCompoMethod = null;
        for (Method m : piirtoalusta.getClass().getDeclaredMethods()) {
            if (!m.getName().equals("paintComponent")) {
                continue;
            }

            try {
                m.setAccessible(true);
            } catch (Exception e) {
                Assert.fail("Method " + m.getName() + " mustn't be set to public.");
            }

            paintCompoMethod = m;
        }

        DrawingBoardTest.MockGraphics graphics = new DrawingBoardTest.MockGraphics();
        try {
            paintCompoMethod.invoke(piirtoalusta, graphics);
        } catch (UnsupportedOperationException e) {
            Assert.fail("Do you use in " + LUOKAN_NIMI + "'s method paintComponent only call super.paintComponent(graphics) and graphics-object's method fillRect?");
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        Assert.assertTrue("Do you use 5 fillRect-calls for drawing the smiley?", graphics.rects.size() > 4 && graphics.rects.size() <= 6);
    }

    private int minimiArvo(Raster raster) {
        int min = Integer.MAX_VALUE;

        for (int x = 0; x < raster.getWidth(); x++) {
            for (int y = 0; y < raster.getHeight(); y++) {
                min = Math.min(min, raster.getSample(x, y, 0));
            }
        }
        return min;
    }

    private int maksimiArvo(Raster raster) {
        int max = Integer.MIN_VALUE;

        for (int x = 0; x < raster.getWidth(); x++) {
            for (int y = 0; y < raster.getHeight(); y++) {
                max = Math.max(max, raster.getSample(x, y, 0));
            }
        }
        return max;
    }

    public Raster mustavalkoRaster(BufferedImage img) {
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(img, img);
        return img.getRaster();
    }

    private BufferedImage skaalaaKuva(BufferedImage kuva, int leveys, int korkeus) {
        BufferedImage skaalattu = new BufferedImage(leveys, korkeus, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = skaalattu.createGraphics();

        g.drawImage(kuva, 0, 0, leveys, korkeus, null);
        g.dispose();

        return skaalattu;
    }

    private BufferedImage luoKuva() {

        JPanel piirtoalusta = luoDrawingBoard();
        Method paintCompoMethod = null;
        for (Method m : piirtoalusta.getClass().getDeclaredMethods()) {
            if (!m.getName().equals("paintComponent")) {
                continue;
            }

            try {
                m.setAccessible(true);
            } catch (Exception e) {
                Assert.fail("Method " + m.getName() + " mustn't be set to public.");
            }

            paintCompoMethod = m;
        }

        DrawingBoardTest.MockGraphics graphics = new DrawingBoardTest.MockGraphics();
        try {
            paintCompoMethod.invoke(piirtoalusta, graphics);
        } catch (UnsupportedOperationException e) {
            Assert.fail("Do you use in " + LUOKAN_NIMI + "'s method paintComponent only call super.paintComponent(graphics) and graphics-object's method fillRect?");
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        BufferedImage bi = new BufferedImage(390, 370, BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 390, 370);

        g.setColor(Color.BLACK);
        for (Rectangle rect : graphics.rects) {
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        return bi;
    }

    private JPanel luoDrawingBoard() {
        Reflex.ClassRef<?> luokka;
        try {
            luokka = Reflex.reflect(LUOKAN_NIMI);
        } catch (Throwable t) {
            Assert.fail("Class " + LUOKAN_NIMI + " doesn't exist. In this assignment you have to create that class.");
            return null;
        }

        if (!JPanel.class.isAssignableFrom(
                luokka.getReferencedClass())) {
            Assert.fail(
                    "Class " + LUOKAN_NIMI + " should "
                    + "inherit class " + JPanel.class.getName());
        }

        Object instanssi;

        try {
            instanssi = luokka.constructor().takingNoParams().invoke();
        } catch (Throwable t) {
            Assert.fail("Class " + LUOKAN_NIMI + " isn't public or it doesn't have a public constructor.");
            return null;
        }
        return (JPanel) instanssi;
    }

    private class MockGraphics extends Graphics {

        private int fillRectKutsuja = 0;
        private java.util.List<Rectangle> rects = new ArrayList<Rectangle>();
        private Color color;

        @Override
        public Graphics create() {
            return this;
        }

        @Override
        public void translate(int i, int i1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Color getColor() {
            return this.color;
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
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
            fillRectKutsuja++;
            Rectangle rect = new Rectangle(i, i1, i2, i3);
            rects.add(rect);
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
            throw new UnsupportedOperationException("Not supported yet.");
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

        public int getFillRectKutsuja() {
            return fillRectKutsuja;
        }

        public java.util.List<Rectangle> getRects() {
            return rects;
        }
    }

    // kuvan lukemiselle palvelimella workaround
    static class Kuva {

        static int[][] data;

        static {
            data = new int[32][32];
            data[0][0] = 0;
            data[0][1] = 0;
            data[0][2] = 0;
            data[0][3] = 0;
            data[0][4] = 0;
            data[0][5] = 0;
            data[0][6] = 0;
            data[0][7] = 0;
            data[0][8] = 0;
            data[0][9] = 0;
            data[0][10] = 0;
            data[0][11] = 0;
            data[0][12] = 0;
            data[0][13] = 0;
            data[0][14] = 0;
            data[0][15] = 0;
            data[0][16] = 0;
            data[0][17] = 0;
            data[0][18] = 0;
            data[0][19] = 0;
            data[0][20] = 0;
            data[0][21] = 0;
            data[0][22] = 0;
            data[0][23] = 0;
            data[0][24] = 0;
            data[0][25] = 0;
            data[0][26] = 0;
            data[0][27] = 0;
            data[0][28] = 0;
            data[0][29] = 0;
            data[0][30] = 0;
            data[0][31] = 0;
            data[1][0] = 0;
            data[1][1] = 0;
            data[1][2] = 0;
            data[1][3] = 0;
            data[1][4] = 0;
            data[1][5] = 0;
            data[1][6] = 0;
            data[1][7] = 0;
            data[1][8] = 0;
            data[1][9] = 0;
            data[1][10] = 0;
            data[1][11] = 0;
            data[1][12] = 0;
            data[1][13] = 0;
            data[1][14] = 0;
            data[1][15] = 0;
            data[1][16] = 0;
            data[1][17] = 0;
            data[1][18] = 0;
            data[1][19] = 0;
            data[1][20] = 0;
            data[1][21] = 0;
            data[1][22] = 0;
            data[1][23] = 0;
            data[1][24] = 0;
            data[1][25] = 0;
            data[1][26] = 0;
            data[1][27] = 0;
            data[1][28] = 0;
            data[1][29] = 0;
            data[1][30] = 0;
            data[1][31] = 0;
            data[2][0] = 0;
            data[2][1] = 0;
            data[2][2] = 0;
            data[2][3] = 0;
            data[2][4] = 0;
            data[2][5] = 0;
            data[2][6] = 0;
            data[2][7] = 0;
            data[2][8] = 0;
            data[2][9] = 0;
            data[2][10] = 0;
            data[2][11] = 0;
            data[2][12] = 0;
            data[2][13] = 0;
            data[2][14] = 0;
            data[2][15] = 0;
            data[2][16] = 0;
            data[2][17] = 0;
            data[2][18] = 0;
            data[2][19] = 0;
            data[2][20] = 0;
            data[2][21] = 0;
            data[2][22] = 0;
            data[2][23] = 0;
            data[2][24] = 0;
            data[2][25] = 0;
            data[2][26] = 0;
            data[2][27] = 0;
            data[2][28] = 0;
            data[2][29] = 0;
            data[2][30] = 0;
            data[2][31] = 0;
            data[3][0] = 0;
            data[3][1] = 0;
            data[3][2] = 0;
            data[3][3] = 0;
            data[3][4] = 0;
            data[3][5] = 0;
            data[3][6] = 0;
            data[3][7] = 0;
            data[3][8] = 0;
            data[3][9] = 0;
            data[3][10] = 0;
            data[3][11] = 0;
            data[3][12] = 0;
            data[3][13] = 0;
            data[3][14] = 0;
            data[3][15] = 0;
            data[3][16] = 0;
            data[3][17] = 0;
            data[3][18] = 0;
            data[3][19] = 0;
            data[3][20] = 0;
            data[3][21] = 0;
            data[3][22] = 0;
            data[3][23] = 0;
            data[3][24] = 0;
            data[3][25] = 0;
            data[3][26] = 0;
            data[3][27] = 0;
            data[3][28] = 0;
            data[3][29] = 0;
            data[3][30] = 0;
            data[3][31] = 0;
            data[4][0] = 0;
            data[4][1] = 0;
            data[4][2] = 0;
            data[4][3] = 0;
            data[4][4] = 0;
            data[4][5] = 0;
            data[4][6] = 0;
            data[4][7] = 0;
            data[4][8] = 0;
            data[4][9] = 0;
            data[4][10] = 0;
            data[4][11] = 0;
            data[4][12] = 0;
            data[4][13] = 0;
            data[4][14] = 0;
            data[4][15] = 0;
            data[4][16] = 0;
            data[4][17] = 1;
            data[4][18] = 1;
            data[4][19] = 1;
            data[4][20] = 1;
            data[4][21] = 1;
            data[4][22] = 0;
            data[4][23] = 0;
            data[4][24] = 0;
            data[4][25] = 0;
            data[4][26] = 0;
            data[4][27] = 0;
            data[4][28] = 0;
            data[4][29] = 0;
            data[4][30] = 0;
            data[4][31] = 0;
            data[5][0] = 0;
            data[5][1] = 0;
            data[5][2] = 0;
            data[5][3] = 0;
            data[5][4] = 0;
            data[5][5] = 0;
            data[5][6] = 0;
            data[5][7] = 0;
            data[5][8] = 0;
            data[5][9] = 0;
            data[5][10] = 0;
            data[5][11] = 0;
            data[5][12] = 0;
            data[5][13] = 0;
            data[5][14] = 0;
            data[5][15] = 0;
            data[5][16] = 0;
            data[5][17] = 1;
            data[5][18] = 1;
            data[5][19] = 1;
            data[5][20] = 1;
            data[5][21] = 1;
            data[5][22] = 0;
            data[5][23] = 0;
            data[5][24] = 0;
            data[5][25] = 0;
            data[5][26] = 0;
            data[5][27] = 0;
            data[5][28] = 0;
            data[5][29] = 0;
            data[5][30] = 0;
            data[5][31] = 0;
            data[6][0] = 0;
            data[6][1] = 0;
            data[6][2] = 0;
            data[6][3] = 0;
            data[6][4] = 0;
            data[6][5] = 0;
            data[6][6] = 0;
            data[6][7] = 0;
            data[6][8] = 0;
            data[6][9] = 0;
            data[6][10] = 0;
            data[6][11] = 0;
            data[6][12] = 0;
            data[6][13] = 0;
            data[6][14] = 0;
            data[6][15] = 0;
            data[6][16] = 0;
            data[6][17] = 1;
            data[6][18] = 1;
            data[6][19] = 1;
            data[6][20] = 1;
            data[6][21] = 1;
            data[6][22] = 0;
            data[6][23] = 0;
            data[6][24] = 0;
            data[6][25] = 0;
            data[6][26] = 0;
            data[6][27] = 0;
            data[6][28] = 0;
            data[6][29] = 0;
            data[6][30] = 0;
            data[6][31] = 0;
            data[7][0] = 0;
            data[7][1] = 0;
            data[7][2] = 0;
            data[7][3] = 0;
            data[7][4] = 0;
            data[7][5] = 0;
            data[7][6] = 0;
            data[7][7] = 0;
            data[7][8] = 0;
            data[7][9] = 0;
            data[7][10] = 0;
            data[7][11] = 0;
            data[7][12] = 0;
            data[7][13] = 0;
            data[7][14] = 0;
            data[7][15] = 0;
            data[7][16] = 0;
            data[7][17] = 1;
            data[7][18] = 1;
            data[7][19] = 1;
            data[7][20] = 1;
            data[7][21] = 1;
            data[7][22] = 0;
            data[7][23] = 0;
            data[7][24] = 0;
            data[7][25] = 0;
            data[7][26] = 0;
            data[7][27] = 0;
            data[7][28] = 0;
            data[7][29] = 0;
            data[7][30] = 0;
            data[7][31] = 0;
            data[8][0] = 0;
            data[8][1] = 0;
            data[8][2] = 0;
            data[8][3] = 0;
            data[8][4] = 1;
            data[8][5] = 1;
            data[8][6] = 1;
            data[8][7] = 1;
            data[8][8] = 1;
            data[8][9] = 0;
            data[8][10] = 0;
            data[8][11] = 0;
            data[8][12] = 0;
            data[8][13] = 0;
            data[8][14] = 0;
            data[8][15] = 0;
            data[8][16] = 0;
            data[8][17] = 0;
            data[8][18] = 0;
            data[8][19] = 0;
            data[8][20] = 0;
            data[8][21] = 0;
            data[8][22] = 1;
            data[8][23] = 1;
            data[8][24] = 1;
            data[8][25] = 1;
            data[8][26] = 0;
            data[8][27] = 0;
            data[8][28] = 0;
            data[8][29] = 0;
            data[8][30] = 0;
            data[8][31] = 0;
            data[9][0] = 0;
            data[9][1] = 0;
            data[9][2] = 0;
            data[9][3] = 0;
            data[9][4] = 1;
            data[9][5] = 1;
            data[9][6] = 1;
            data[9][7] = 1;
            data[9][8] = 1;
            data[9][9] = 0;
            data[9][10] = 0;
            data[9][11] = 0;
            data[9][12] = 0;
            data[9][13] = 0;
            data[9][14] = 0;
            data[9][15] = 0;
            data[9][16] = 0;
            data[9][17] = 0;
            data[9][18] = 0;
            data[9][19] = 0;
            data[9][20] = 0;
            data[9][21] = 0;
            data[9][22] = 1;
            data[9][23] = 1;
            data[9][24] = 1;
            data[9][25] = 1;
            data[9][26] = 0;
            data[9][27] = 0;
            data[9][28] = 0;
            data[9][29] = 0;
            data[9][30] = 0;
            data[9][31] = 0;
            data[10][0] = 0;
            data[10][1] = 0;
            data[10][2] = 0;
            data[10][3] = 0;
            data[10][4] = 1;
            data[10][5] = 1;
            data[10][6] = 1;
            data[10][7] = 1;
            data[10][8] = 1;
            data[10][9] = 0;
            data[10][10] = 0;
            data[10][11] = 0;
            data[10][12] = 0;
            data[10][13] = 0;
            data[10][14] = 0;
            data[10][15] = 0;
            data[10][16] = 0;
            data[10][17] = 0;
            data[10][18] = 0;
            data[10][19] = 0;
            data[10][20] = 0;
            data[10][21] = 0;
            data[10][22] = 1;
            data[10][23] = 1;
            data[10][24] = 1;
            data[10][25] = 1;
            data[10][26] = 0;
            data[10][27] = 0;
            data[10][28] = 0;
            data[10][29] = 0;
            data[10][30] = 0;
            data[10][31] = 0;
            data[11][0] = 0;
            data[11][1] = 0;
            data[11][2] = 0;
            data[11][3] = 0;
            data[11][4] = 1;
            data[11][5] = 1;
            data[11][6] = 1;
            data[11][7] = 1;
            data[11][8] = 1;
            data[11][9] = 0;
            data[11][10] = 0;
            data[11][11] = 0;
            data[11][12] = 0;
            data[11][13] = 0;
            data[11][14] = 0;
            data[11][15] = 0;
            data[11][16] = 0;
            data[11][17] = 0;
            data[11][18] = 0;
            data[11][19] = 0;
            data[11][20] = 0;
            data[11][21] = 0;
            data[11][22] = 1;
            data[11][23] = 1;
            data[11][24] = 1;
            data[11][25] = 1;
            data[11][26] = 0;
            data[11][27] = 0;
            data[11][28] = 0;
            data[11][29] = 0;
            data[11][30] = 0;
            data[11][31] = 0;
            data[12][0] = 0;
            data[12][1] = 0;
            data[12][2] = 0;
            data[12][3] = 0;
            data[12][4] = 0;
            data[12][5] = 0;
            data[12][6] = 0;
            data[12][7] = 0;
            data[12][8] = 0;
            data[12][9] = 0;
            data[12][10] = 0;
            data[12][11] = 0;
            data[12][12] = 0;
            data[12][13] = 0;
            data[12][14] = 0;
            data[12][15] = 0;
            data[12][16] = 0;
            data[12][17] = 0;
            data[12][18] = 0;
            data[12][19] = 0;
            data[12][20] = 0;
            data[12][21] = 0;
            data[12][22] = 1;
            data[12][23] = 1;
            data[12][24] = 1;
            data[12][25] = 1;
            data[12][26] = 0;
            data[12][27] = 0;
            data[12][28] = 0;
            data[12][29] = 0;
            data[12][30] = 0;
            data[12][31] = 0;
            data[13][0] = 0;
            data[13][1] = 0;
            data[13][2] = 0;
            data[13][3] = 0;
            data[13][4] = 0;
            data[13][5] = 0;
            data[13][6] = 0;
            data[13][7] = 0;
            data[13][8] = 0;
            data[13][9] = 0;
            data[13][10] = 0;
            data[13][11] = 0;
            data[13][12] = 0;
            data[13][13] = 0;
            data[13][14] = 0;
            data[13][15] = 0;
            data[13][16] = 0;
            data[13][17] = 0;
            data[13][18] = 0;
            data[13][19] = 0;
            data[13][20] = 0;
            data[13][21] = 0;
            data[13][22] = 1;
            data[13][23] = 1;
            data[13][24] = 1;
            data[13][25] = 1;
            data[13][26] = 0;
            data[13][27] = 0;
            data[13][28] = 0;
            data[13][29] = 0;
            data[13][30] = 0;
            data[13][31] = 0;
            data[14][0] = 0;
            data[14][1] = 0;
            data[14][2] = 0;
            data[14][3] = 0;
            data[14][4] = 0;
            data[14][5] = 0;
            data[14][6] = 0;
            data[14][7] = 0;
            data[14][8] = 0;
            data[14][9] = 0;
            data[14][10] = 0;
            data[14][11] = 0;
            data[14][12] = 0;
            data[14][13] = 0;
            data[14][14] = 0;
            data[14][15] = 0;
            data[14][16] = 0;
            data[14][17] = 0;
            data[14][18] = 0;
            data[14][19] = 0;
            data[14][20] = 0;
            data[14][21] = 0;
            data[14][22] = 1;
            data[14][23] = 1;
            data[14][24] = 1;
            data[14][25] = 1;
            data[14][26] = 0;
            data[14][27] = 0;
            data[14][28] = 0;
            data[14][29] = 0;
            data[14][30] = 0;
            data[14][31] = 0;
            data[15][0] = 0;
            data[15][1] = 0;
            data[15][2] = 0;
            data[15][3] = 0;
            data[15][4] = 0;
            data[15][5] = 0;
            data[15][6] = 0;
            data[15][7] = 0;
            data[15][8] = 0;
            data[15][9] = 0;
            data[15][10] = 0;
            data[15][11] = 0;
            data[15][12] = 0;
            data[15][13] = 0;
            data[15][14] = 0;
            data[15][15] = 0;
            data[15][16] = 0;
            data[15][17] = 0;
            data[15][18] = 0;
            data[15][19] = 0;
            data[15][20] = 0;
            data[15][21] = 0;
            data[15][22] = 1;
            data[15][23] = 1;
            data[15][24] = 1;
            data[15][25] = 1;
            data[15][26] = 0;
            data[15][27] = 0;
            data[15][28] = 0;
            data[15][29] = 0;
            data[15][30] = 0;
            data[15][31] = 0;
            data[16][0] = 0;
            data[16][1] = 0;
            data[16][2] = 0;
            data[16][3] = 0;
            data[16][4] = 0;
            data[16][5] = 0;
            data[16][6] = 0;
            data[16][7] = 0;
            data[16][8] = 0;
            data[16][9] = 0;
            data[16][10] = 0;
            data[16][11] = 0;
            data[16][12] = 0;
            data[16][13] = 0;
            data[16][14] = 0;
            data[16][15] = 0;
            data[16][16] = 0;
            data[16][17] = 0;
            data[16][18] = 0;
            data[16][19] = 0;
            data[16][20] = 0;
            data[16][21] = 0;
            data[16][22] = 1;
            data[16][23] = 1;
            data[16][24] = 1;
            data[16][25] = 1;
            data[16][26] = 0;
            data[16][27] = 0;
            data[16][28] = 0;
            data[16][29] = 0;
            data[16][30] = 0;
            data[16][31] = 0;
            data[17][0] = 0;
            data[17][1] = 0;
            data[17][2] = 0;
            data[17][3] = 0;
            data[17][4] = 0;
            data[17][5] = 0;
            data[17][6] = 0;
            data[17][7] = 0;
            data[17][8] = 0;
            data[17][9] = 0;
            data[17][10] = 0;
            data[17][11] = 0;
            data[17][12] = 0;
            data[17][13] = 0;
            data[17][14] = 0;
            data[17][15] = 0;
            data[17][16] = 0;
            data[17][17] = 0;
            data[17][18] = 0;
            data[17][19] = 0;
            data[17][20] = 0;
            data[17][21] = 0;
            data[17][22] = 1;
            data[17][23] = 1;
            data[17][24] = 1;
            data[17][25] = 1;
            data[17][26] = 0;
            data[17][27] = 0;
            data[17][28] = 0;
            data[17][29] = 0;
            data[17][30] = 0;
            data[17][31] = 0;
            data[18][0] = 0;
            data[18][1] = 0;
            data[18][2] = 0;
            data[18][3] = 0;
            data[18][4] = 0;
            data[18][5] = 0;
            data[18][6] = 0;
            data[18][7] = 0;
            data[18][8] = 0;
            data[18][9] = 0;
            data[18][10] = 0;
            data[18][11] = 0;
            data[18][12] = 0;
            data[18][13] = 0;
            data[18][14] = 0;
            data[18][15] = 0;
            data[18][16] = 0;
            data[18][17] = 0;
            data[18][18] = 0;
            data[18][19] = 0;
            data[18][20] = 0;
            data[18][21] = 0;
            data[18][22] = 1;
            data[18][23] = 1;
            data[18][24] = 1;
            data[18][25] = 1;
            data[18][26] = 0;
            data[18][27] = 0;
            data[18][28] = 0;
            data[18][29] = 0;
            data[18][30] = 0;
            data[18][31] = 0;
            data[19][0] = 0;
            data[19][1] = 0;
            data[19][2] = 0;
            data[19][3] = 0;
            data[19][4] = 0;
            data[19][5] = 0;
            data[19][6] = 0;
            data[19][7] = 0;
            data[19][8] = 0;
            data[19][9] = 0;
            data[19][10] = 0;
            data[19][11] = 0;
            data[19][12] = 0;
            data[19][13] = 0;
            data[19][14] = 0;
            data[19][15] = 0;
            data[19][16] = 0;
            data[19][17] = 0;
            data[19][18] = 0;
            data[19][19] = 0;
            data[19][20] = 0;
            data[19][21] = 0;
            data[19][22] = 1;
            data[19][23] = 1;
            data[19][24] = 1;
            data[19][25] = 1;
            data[19][26] = 0;
            data[19][27] = 0;
            data[19][28] = 0;
            data[19][29] = 0;
            data[19][30] = 0;
            data[19][31] = 0;
            data[20][0] = 0;
            data[20][1] = 0;
            data[20][2] = 0;
            data[20][3] = 0;
            data[20][4] = 0;
            data[20][5] = 0;
            data[20][6] = 0;
            data[20][7] = 0;
            data[20][8] = 0;
            data[20][9] = 0;
            data[20][10] = 0;
            data[20][11] = 0;
            data[20][12] = 0;
            data[20][13] = 0;
            data[20][14] = 0;
            data[20][15] = 0;
            data[20][16] = 0;
            data[20][17] = 0;
            data[20][18] = 0;
            data[20][19] = 0;
            data[20][20] = 0;
            data[20][21] = 0;
            data[20][22] = 1;
            data[20][23] = 1;
            data[20][24] = 1;
            data[20][25] = 1;
            data[20][26] = 0;
            data[20][27] = 0;
            data[20][28] = 0;
            data[20][29] = 0;
            data[20][30] = 0;
            data[20][31] = 0;
            data[21][0] = 0;
            data[21][1] = 0;
            data[21][2] = 0;
            data[21][3] = 0;
            data[21][4] = 1;
            data[21][5] = 1;
            data[21][6] = 1;
            data[21][7] = 1;
            data[21][8] = 1;
            data[21][9] = 0;
            data[21][10] = 0;
            data[21][11] = 0;
            data[21][12] = 0;
            data[21][13] = 0;
            data[21][14] = 0;
            data[21][15] = 0;
            data[21][16] = 0;
            data[21][17] = 0;
            data[21][18] = 0;
            data[21][19] = 0;
            data[21][20] = 0;
            data[21][21] = 0;
            data[21][22] = 1;
            data[21][23] = 1;
            data[21][24] = 1;
            data[21][25] = 1;
            data[21][26] = 0;
            data[21][27] = 0;
            data[21][28] = 0;
            data[21][29] = 0;
            data[21][30] = 0;
            data[21][31] = 0;
            data[22][0] = 0;
            data[22][1] = 0;
            data[22][2] = 0;
            data[22][3] = 0;
            data[22][4] = 1;
            data[22][5] = 1;
            data[22][6] = 1;
            data[22][7] = 1;
            data[22][8] = 1;
            data[22][9] = 0;
            data[22][10] = 0;
            data[22][11] = 0;
            data[22][12] = 0;
            data[22][13] = 0;
            data[22][14] = 0;
            data[22][15] = 0;
            data[22][16] = 0;
            data[22][17] = 0;
            data[22][18] = 0;
            data[22][19] = 0;
            data[22][20] = 0;
            data[22][21] = 0;
            data[22][22] = 1;
            data[22][23] = 1;
            data[22][24] = 1;
            data[22][25] = 1;
            data[22][26] = 0;
            data[22][27] = 0;
            data[22][28] = 0;
            data[22][29] = 0;
            data[22][30] = 0;
            data[22][31] = 0;
            data[23][0] = 0;
            data[23][1] = 0;
            data[23][2] = 0;
            data[23][3] = 0;
            data[23][4] = 1;
            data[23][5] = 1;
            data[23][6] = 1;
            data[23][7] = 1;
            data[23][8] = 1;
            data[23][9] = 0;
            data[23][10] = 0;
            data[23][11] = 0;
            data[23][12] = 0;
            data[23][13] = 0;
            data[23][14] = 0;
            data[23][15] = 0;
            data[23][16] = 0;
            data[23][17] = 0;
            data[23][18] = 0;
            data[23][19] = 0;
            data[23][20] = 0;
            data[23][21] = 0;
            data[23][22] = 1;
            data[23][23] = 1;
            data[23][24] = 1;
            data[23][25] = 1;
            data[23][26] = 0;
            data[23][27] = 0;
            data[23][28] = 0;
            data[23][29] = 0;
            data[23][30] = 0;
            data[23][31] = 0;
            data[24][0] = 0;
            data[24][1] = 0;
            data[24][2] = 0;
            data[24][3] = 0;
            data[24][4] = 1;
            data[24][5] = 1;
            data[24][6] = 1;
            data[24][7] = 1;
            data[24][8] = 1;
            data[24][9] = 0;
            data[24][10] = 0;
            data[24][11] = 0;
            data[24][12] = 0;
            data[24][13] = 0;
            data[24][14] = 0;
            data[24][15] = 0;
            data[24][16] = 0;
            data[24][17] = 0;
            data[24][18] = 0;
            data[24][19] = 0;
            data[24][20] = 0;
            data[24][21] = 0;
            data[24][22] = 1;
            data[24][23] = 1;
            data[24][24] = 1;
            data[24][25] = 1;
            data[24][26] = 0;
            data[24][27] = 0;
            data[24][28] = 0;
            data[24][29] = 0;
            data[24][30] = 0;
            data[24][31] = 0;
            data[25][0] = 0;
            data[25][1] = 0;
            data[25][2] = 0;
            data[25][3] = 0;
            data[25][4] = 0;
            data[25][5] = 0;
            data[25][6] = 0;
            data[25][7] = 0;
            data[25][8] = 0;
            data[25][9] = 0;
            data[25][10] = 0;
            data[25][11] = 0;
            data[25][12] = 0;
            data[25][13] = 0;
            data[25][14] = 0;
            data[25][15] = 0;
            data[25][16] = 0;
            data[25][17] = 1;
            data[25][18] = 1;
            data[25][19] = 1;
            data[25][20] = 1;
            data[25][21] = 1;
            data[25][22] = 0;
            data[25][23] = 0;
            data[25][24] = 0;
            data[25][25] = 0;
            data[25][26] = 0;
            data[25][27] = 0;
            data[25][28] = 0;
            data[25][29] = 0;
            data[25][30] = 0;
            data[25][31] = 0;
            data[26][0] = 0;
            data[26][1] = 0;
            data[26][2] = 0;
            data[26][3] = 0;
            data[26][4] = 0;
            data[26][5] = 0;
            data[26][6] = 0;
            data[26][7] = 0;
            data[26][8] = 0;
            data[26][9] = 0;
            data[26][10] = 0;
            data[26][11] = 0;
            data[26][12] = 0;
            data[26][13] = 0;
            data[26][14] = 0;
            data[26][15] = 0;
            data[26][16] = 0;
            data[26][17] = 1;
            data[26][18] = 1;
            data[26][19] = 1;
            data[26][20] = 1;
            data[26][21] = 1;
            data[26][22] = 0;
            data[26][23] = 0;
            data[26][24] = 0;
            data[26][25] = 0;
            data[26][26] = 0;
            data[26][27] = 0;
            data[26][28] = 0;
            data[26][29] = 0;
            data[26][30] = 0;
            data[26][31] = 0;
            data[27][0] = 0;
            data[27][1] = 0;
            data[27][2] = 0;
            data[27][3] = 0;
            data[27][4] = 0;
            data[27][5] = 0;
            data[27][6] = 0;
            data[27][7] = 0;
            data[27][8] = 0;
            data[27][9] = 0;
            data[27][10] = 0;
            data[27][11] = 0;
            data[27][12] = 0;
            data[27][13] = 0;
            data[27][14] = 0;
            data[27][15] = 0;
            data[27][16] = 0;
            data[27][17] = 1;
            data[27][18] = 1;
            data[27][19] = 1;
            data[27][20] = 1;
            data[27][21] = 1;
            data[27][22] = 0;
            data[27][23] = 0;
            data[27][24] = 0;
            data[27][25] = 0;
            data[27][26] = 0;
            data[27][27] = 0;
            data[27][28] = 0;
            data[27][29] = 0;
            data[27][30] = 0;
            data[27][31] = 0;
            data[28][0] = 0;
            data[28][1] = 0;
            data[28][2] = 0;
            data[28][3] = 0;
            data[28][4] = 0;
            data[28][5] = 0;
            data[28][6] = 0;
            data[28][7] = 0;
            data[28][8] = 0;
            data[28][9] = 0;
            data[28][10] = 0;
            data[28][11] = 0;
            data[28][12] = 0;
            data[28][13] = 0;
            data[28][14] = 0;
            data[28][15] = 0;
            data[28][16] = 0;
            data[28][17] = 1;
            data[28][18] = 1;
            data[28][19] = 1;
            data[28][20] = 1;
            data[28][21] = 1;
            data[28][22] = 0;
            data[28][23] = 0;
            data[28][24] = 0;
            data[28][25] = 0;
            data[28][26] = 0;
            data[28][27] = 0;
            data[28][28] = 0;
            data[28][29] = 0;
            data[28][30] = 0;
            data[28][31] = 0;
            data[29][0] = 0;
            data[29][1] = 0;
            data[29][2] = 0;
            data[29][3] = 0;
            data[29][4] = 0;
            data[29][5] = 0;
            data[29][6] = 0;
            data[29][7] = 0;
            data[29][8] = 0;
            data[29][9] = 0;
            data[29][10] = 0;
            data[29][11] = 0;
            data[29][12] = 0;
            data[29][13] = 0;
            data[29][14] = 0;
            data[29][15] = 0;
            data[29][16] = 0;
            data[29][17] = 0;
            data[29][18] = 0;
            data[29][19] = 0;
            data[29][20] = 0;
            data[29][21] = 0;
            data[29][22] = 0;
            data[29][23] = 0;
            data[29][24] = 0;
            data[29][25] = 0;
            data[29][26] = 0;
            data[29][27] = 0;
            data[29][28] = 0;
            data[29][29] = 0;
            data[29][30] = 0;
            data[29][31] = 0;
            data[30][0] = 0;
            data[30][1] = 0;
            data[30][2] = 0;
            data[30][3] = 0;
            data[30][4] = 0;
            data[30][5] = 0;
            data[30][6] = 0;
            data[30][7] = 0;
            data[30][8] = 0;
            data[30][9] = 0;
            data[30][10] = 0;
            data[30][11] = 0;
            data[30][12] = 0;
            data[30][13] = 0;
            data[30][14] = 0;
            data[30][15] = 0;
            data[30][16] = 0;
            data[30][17] = 0;
            data[30][18] = 0;
            data[30][19] = 0;
            data[30][20] = 0;
            data[30][21] = 0;
            data[30][22] = 0;
            data[30][23] = 0;
            data[30][24] = 0;
            data[30][25] = 0;
            data[30][26] = 0;
            data[30][27] = 0;
            data[30][28] = 0;
            data[30][29] = 0;
            data[30][30] = 0;
            data[30][31] = 0;
            data[31][0] = 0;
            data[31][1] = 0;
            data[31][2] = 0;
            data[31][3] = 0;
            data[31][4] = 0;
            data[31][5] = 0;
            data[31][6] = 0;
            data[31][7] = 0;
            data[31][8] = 0;
            data[31][9] = 0;
            data[31][10] = 0;
            data[31][11] = 0;
            data[31][12] = 0;
            data[31][13] = 0;
            data[31][14] = 0;
            data[31][15] = 0;
            data[31][16] = 0;
            data[31][17] = 0;
            data[31][18] = 0;
            data[31][19] = 0;
            data[31][20] = 0;
            data[31][21] = 0;
            data[31][22] = 0;
            data[31][23] = 0;
            data[31][24] = 0;
            data[31][25] = 0;
            data[31][26] = 0;
            data[31][27] = 0;
            data[31][28] = 0;
            data[31][29] = 0;
            data[31][30] = 0;
            data[31][31] = 0;
        }
    }

    private boolean sisaltaa(String kutsu) {

        try {
            Scanner lukija = new Scanner(new File("src/drawing/ui/UserInterface.java"));

            while (lukija.hasNextLine()) {

                String rivi = lukija.nextLine();

                if ( rivi.replaceAll(" ","").contains(kutsu)) {
                    return true;
                }

            }

        } catch (Exception e) {
        }

        return false;
    }
}
