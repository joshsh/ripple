package net.fortytwo.ripple.logo;

import java.awt.geom.Point2D;

/**
* User: josh
* Date: 5/15/11
* Time: 12:41 PM
*/
class Slosher {
    public Point2D location;
    public double magnitude;
    private RippleEffect rippleEffect;

    public Slosher(RippleEffect rippleEffect) {
        this.rippleEffect = rippleEffect;
        double minMagnitude = 0.5;

        double x = rippleEffect.random.nextDouble();
        double y = rippleEffect.random.nextDouble();
        location = new Point2D.Double(x, y);

        magnitude = minMagnitude * (rippleEffect.random.nextDouble() * (1 - minMagnitude));
        if (rippleEffect.random.nextBoolean())
            magnitude = -magnitude;

        System.out.println("new slosher: " + magnitude + " at " + location);
    }

    public double waveFunc(Point2D p) {
        double distance = p.distance(location);
        double d = Math.cos((Math.PI / 2.0) - (rippleEffect.sloshConstA / ((rippleEffect.sloshPointiness * distance) + rippleEffect.sloshConstB)));

        return magnitude * d;
    }
}
