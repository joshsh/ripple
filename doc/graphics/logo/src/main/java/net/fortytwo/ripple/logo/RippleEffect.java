/*
* $URL: http://svn.fortytwo.net/projects/ripple/trunk/ripple-logo/RippleEffect.java $
* $Revision: 1054 $
* $Author: josh $
*
* Copyright (C) 2007-2011 Joshua Shinavier
*/

package net.fortytwo.ripple.logo;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.MemoryImageSource;
import java.util.Random;

public class RippleEffect {
    Color[][] matrix;
    int width, height;

    Point2D trefoilMidpoint;
    Point2D[] trefoilCenters;
    double trefoilRadius;
    double trefoilRotatedBy;

    double rippleWavelength;
    // Higher dissipation --> ripples die off more quickly when further from the source
    double rippleDissipation = 1.5;

    double sloshWeight;
    int nSloshes;
    double sloshConstA, sloshConstB;
    double sloshPointiness;

    Color backgroundColor;

    Image image;
    // Changing the seed value will change the result
    Random random = new Random(8212);

    double dampen(double distance) {
        // TODO: this is a bit of a hack
        double d = 1.0 + (distance * rippleDissipation);
        return 1.0 / (d * d);
    }

    double dampen(Point2D p) {
        double min = 100;
        for (int i = 0; i < 3; i++) {
            double d = p.distance(trefoilCenters[i]);
            if (d < min)
                min = d;
        }

        return dampen(min);
    }

    double waveFunc(double distance) {
        return dampen(distance)
                * Math.sin(2 * Math.PI * distance / rippleWavelength);
    }

    void addRippler(double[][] field, Point2D center, double intensity) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Point2D p = new Point2D.Double(
                        (double) i / (double) height,
                        1.0 - ((double) j / (double) width));

                double dist = p.distance(center);

                field[i][j] += intensity * waveFunc(dist);
            }
        }
    }

    void addSlosher(double[][] field) {
        Slosher s = new Slosher(this);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Point2D p = new Point2D.Double(j / ((double) width), i / ((double) height));
                field[i][j] += s.waveFunc(p);
            }
        }
    }

    void clearField(double[][] field) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = 0;
            }
        }
    }

    double[][] createEmptyField(int height, int width) {
        double[][] f = new double[height][width];
        clearField(f);
        return f;
    }

    void normalizeField(double[][] field) {
        double max = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double d = field[i][j];
                if (d > 0) {
                    if (d > max)
                        max = d;
                } else if (d < 0) {
                    if (-d > max)
                        max = -d;
                }
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = field[i][j] / max;
            }
        }
    }

    double intensify(double val, int n) {
        for (int i = 0; i < n; i++)
            val = Math.sin(val * Math.PI / 2.0);

        return val;
    }

    void createPoints() {
        trefoilMidpoint = new Point2D.Double(0.5, 0.5);
        trefoilCenters = new Point2D.Double[3];

        for (int i = 0; i < 3; i++) {
            double angle = (trefoilRotatedBy + i / 3.0) * 2 * Math.PI;
            double x = trefoilMidpoint.getX() + trefoilRadius * Math.cos(angle);
            double y = trefoilMidpoint.getY() + trefoilRadius * Math.sin(angle);
            trefoilCenters[i] = new Point2D.Double(x, y);
        }
    }

    void createField()
            throws Exception {
        matrix = new Color[height][width];

        double[][] ripples = createEmptyField(height, width);
        addRippler(ripples, trefoilCenters[0], 1.0);
        addRippler(ripples, trefoilCenters[1], 1.0);
        addRippler(ripples, trefoilCenters[2], 1.0);
        normalizeField(ripples);

        sloshConstB = 2 * sloshConstA / Math.PI;
        double[][] sloshes = createEmptyField(height, width);
        for (int i = 0; i < nSloshes; i++) {
            addSlosher(sloshes);
        }
        normalizeField(sloshes);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Point2D p = new Point2D.Double(
                        (double) i / (double) height,
                        1.0 - ((double) j / (double) width));

                double ripple = ripples[i][j];
                double slosh = sloshes[i][j];

                double intensity = ripple + (slosh * sloshWeight);
                intensity = intensity / 6.0;

                // Fade out at edges of trefoil.
                intensity = intensity * dampen(p);

                if (intensity < 0) {
                    intensity = 0;
                }

                intensity = intensify(intensity, 4);
//*
                int v = (int) (255 * intensity);
                Color result = new Color(255, 255, 255, v);
//*/

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

    void init()
            throws Exception {
        trefoilRadius = 0.06;
        trefoilRotatedBy = 0.55;

        backgroundColor = Color.BLUE;

        width = 1000;
        height = 1000;

        rippleWavelength = 0.1;

        nSloshes = 40;
        sloshWeight = 2.0;
        sloshConstA = 1.0;
        sloshPointiness = 20.0;

        createPoints();
        createField();
        createImage();
    }

    public void show()
            throws Exception {
        ImagePanel panel = new ImagePanel(image);
        JFrame f = new JFrame();
        f.setBackground(backgroundColor);
        f.getContentPane().add(panel);

        int width = panel.img.getWidth(null);
        int height = panel.img.getHeight(null);

        //show frame
        f.setBounds(0, 0, width, height);
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
