package net.fortytwo.ripple.logo;

import java.awt.geom.Point2D;

/**
 * User: josh
 * Date: 5/15/11
 * Time: 5:18 PM
 */
public class Rippler {
    private final Point2D center;
    private final double intensity;
    private final double wavelength;
    private final double dissipation;

    public Rippler(Point2D center,
                   double intensity,
                   double wavelength,
                   double dissipation) {
        this.center = center;
        this.intensity = intensity;
        this.wavelength = wavelength;
        this.dissipation = dissipation;
    }

    public void applyTo(final Field field) {
        for (int i = 0; i < field.height; i++) {
            for (int j = 0; j < field.width; j++) {
                Point2D p = new Point2D.Double(
                        field.virtualY(i),
                        field.virtualX(j));

                double dist = p.distance(center);

                field.values[i][j] += intensity * waveFunc(dist);
            }
        }
    }

    double waveFunc(double distance) {
        return dampen(distance, dissipation)
                * Math.sin(2 * Math.PI * distance / wavelength);
    }

    public static double dampen(double distance,
                                double dissipation) {
        // TODO: this is a bit of a hack
        double d = 1.0 + (distance * dissipation);
        return 1.0 / (d * d);
    }
}
