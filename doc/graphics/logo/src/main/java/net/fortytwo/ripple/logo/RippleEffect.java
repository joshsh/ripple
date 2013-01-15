/*
* $URL: http://svn.fortytwo.net/projects/ripple/trunk/ripple-logo/RippleEffect.java $
* $Revision: 1054 $
* $Author: josh $
*
* @author Joshua Shinavier (http://fortytwo.net)
*/

package net.fortytwo.ripple.logo;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.MemoryImageSource;
import java.util.Random;

public class RippleEffect {
    // Changing the seed value will change the result
    Random random = new Random(4621);
//    Random random = new Random(293);
//Random random = new Random(7884);

    Color[][] matrix;
    int width, height;

    double virtualWidth, virtualHeight;

    Point2D trefoilMidpoint;
    Point2D[] trefoilCenters;
    double trefoilRadius;
    double trefoilRotatedBy;

    double rippleWavelength;
    // Higher dissipation --> ripples die off more quickly when further from the source
    double rippleDissipation = 1.5;

    double sloshWeight;
    int nSloshes;
    int nRandomRipplers;
    double randomRipplerIntensity;
    double sloshConstA, sloshConstB;
    double sloshPointiness;

    double highPassThreshold;

    Color backgroundColor;
    Color foregroundColor;

    Image image;

    double intensify(double val, int n) {
        for (int i = 0; i < n; i++) {
            val = Math.sin(val * Math.PI / 2.0);
        }

        return val;
    }

    void createPoints() {
        trefoilMidpoint = new Point2D.Double(0, -0.5);
        trefoilCenters = new Point2D.Double[3];

        for (int i = 0; i < 3; i++) {
            double angle = (trefoilRotatedBy + i / 3.0) * 2 * Math.PI;
            double x = trefoilMidpoint.getX() + trefoilRadius * Math.cos(angle);
            double y = trefoilMidpoint.getY() + trefoilRadius * Math.sin(angle);
            trefoilCenters[i] = new Point2D.Double(x, y);
        }
    }

    double dissipateFieldOfView(final double distance) {
        double d = 1 - (distance / 1.5);
        if (d < 0) {
            d = 0;
        }
        return d;
    }

    void createField()
            throws Exception {
        matrix = new Color[height][width];

        Field ripples = new Field(height, width, virtualHeight, virtualWidth);
        ripples.clear();
        for (Point2D p : trefoilCenters) {
            new Rippler(p, 1.0, rippleWavelength, rippleDissipation).applyTo(ripples);
        }
        for (int i = 0; i < nRandomRipplers; i++) {
            Point2D p = new Point2D.Double(random.nextDouble(), random.nextDouble());
            double intensity = random.nextDouble() * randomRipplerIntensity;
            new Rippler(p, intensity, rippleWavelength, rippleDissipation).applyTo(ripples);
        }
        ripples.normalize();

        sloshConstB = 2 * sloshConstA / Math.PI;
        Field sloshes = new Field(height, width, virtualHeight, virtualWidth);
        sloshes.clear();
        for (int i = 0; i < nSloshes; i++) {
            Slosher s = new Slosher(sloshConstA, sloshConstB, sloshPointiness, random);
            s.applyTo(sloshes);
        }
        sloshes.normalize();

        Field field = new Field(height, width, virtualHeight, virtualWidth);
        field.clear();
        field.add(ripples);
        field.add(sloshes);
        //field.posNorm();
        //field.highPass(highPassThreshold);
        field.normalize();
        //field.highPass(0);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Point2D p = new Point2D.Double(
                        field.virtualY(i),
                        field.virtualX(j));

                double intensity = field.values[i][j];

                // Fade out at edges of trefoil.
                //intensity = intensity * Rippler.dampen(p.distance(trefoilMidpoint), rippleDissipation * 3);
                intensity = intensity * dissipateFieldOfView(p.distance(trefoilMidpoint));

                intensity = intensify(intensity, 3);
                if (intensity < 0) {
                    intensity = 0;
                }

                int v = (int) (255 * intensity);

                Color result = new Color(0, 0, 255, v);
//                Color result = new Color(255, 255, 255, v);

                matrix[i][j] = result;
            }
        }
    }

    void createImage()
            throws Exception {
        int[] bytes = new int[width * height];
        int k = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = matrix[i][j];
                bytes[k++] = (c.getAlpha() << 24)
                        | (c.getRed() << 16)
                        | (c.getGreen() << 8)
                        | c.getBlue();
            }
        }

        int off = 0;
        int scan = width;

        image = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(width, height, bytes, off, scan));
    }

    enum Config {RainPuddle, HighEnergy}

    void init()
            throws Exception {
        trefoilRadius = 0.06;
        trefoilRotatedBy = 0.45;

        width = 1600;
        height = 800;

        virtualHeight = 1.0;
        virtualWidth = ((double) width) / ((double) height);

        Config config = RippleEffect.Config.HighEnergy;

//        backgroundColor = Color.WHITE;

        switch (config) {

            case RainPuddle:
                backgroundColor = Color.BLUE;
                foregroundColor = Color.WHITE;
                rippleWavelength = 0.1;
                nRandomRipplers = 10;
                randomRipplerIntensity = 0.75;
                nSloshes = 40;
                sloshWeight = 1.0;
                sloshConstA = 1.0;
                sloshPointiness = 20.0;
                highPassThreshold = 0;
                break;
            case HighEnergy:
                backgroundColor = Color.WHITE;
                foregroundColor = Color.BLUE;
                rippleWavelength = 0.05;
                nRandomRipplers = 0;
                randomRipplerIntensity = 0.5;
                nSloshes = 40;
                sloshWeight = 1.0;
                sloshConstA = 1.0;
                sloshPointiness = 20.0;
                highPassThreshold = 0.5;
                break;
        }

        createPoints();
        createField();
        createImage();
    }

    public void show()
            throws Exception {
        ImagePanel panel = new ImagePanel(image);
        JScrollPane scroll = new JScrollPane(panel);

        JFrame f = new JFrame();
        f.setBackground(backgroundColor);
        f.getContentPane().add(scroll);

        //f.setSize(1000, 1000);
        //int width = panel.img.getWidth(null);
        //int height = panel.img.getHeight(null);

        //show frame
        //f.setBounds(0, 0, width, height);
        f.setVisible(true);
    }

    //panel used to draw image on
    private class ImagePanel extends JPanel {
        Image img;

        public ImagePanel(final Image img) {
            this.img = img;
//setBackground( Color.WHITE );
        }

        //override paint method of panel
        public void paint(Graphics g) {
//setBackground( Color.WHITE );
            g.drawImage(img, 0, 0, this);
        }
    }

    public static void main(String[] args) {
        try {
            RippleEffect effect = new RippleEffect();
            effect.init();
            effect.show();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
}
