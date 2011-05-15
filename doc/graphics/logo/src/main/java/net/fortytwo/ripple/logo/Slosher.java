package net.fortytwo.ripple.logo;

import java.awt.geom.Point2D;
import java.util.Random;

/**
 * User: josh
 * Date: 5/15/11
 * Time: 12:41 PM
 */
class Slosher {
    public Point2D location;
    private final double constA, constB, pointiness;
    private double magnitude;

    public Slosher(double constA,
                   double constB,
                   double pointiness,
                   Random random) {
        this.constA = constA;
        this.constB = constB;
        this.pointiness = pointiness;
        double minMagnitude = 0.5;

        double x = random.nextDouble();
        double y = random.nextDouble();
        location = new Point2D.Double(x, y);

        magnitude = minMagnitude * (random.nextDouble() * (1 - minMagnitude));
        if (random.nextBoolean()) {
            magnitude = -magnitude;
        }

        System.out.println("new slosher: " + magnitude + " at " + location);
    }

    public double waveFunc(double distance) {
        double d = Math.cos((Math.PI / 2.0) - (constA / ((pointiness * distance) + constB)));

        return magnitude * d;
    }

    void applyTo(Field field) {
        Point2D virtualP = new Point2D.Double(
                field.virtualXMin() + location.getX() * field.virtualWidth,
                field.virtualYMin() + location.getY() * field.virtualHeight);
        for (int i = 0; i < field.height; i++) {
            for (int j = 0; j < field.width; j++) {
                Point2D p = new Point2D.Double(field.virtualX(j), field.virtualY(i));
                double distance = p.distance(virtualP);

                field.values[i][j] += waveFunc(distance);
            }
        }
    }
}
