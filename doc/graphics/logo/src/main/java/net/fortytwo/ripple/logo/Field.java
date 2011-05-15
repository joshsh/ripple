package net.fortytwo.ripple.logo;

/**
 * User: josh
 * Date: 5/15/11
 * Time: 1:27 PM
 */
public class Field {
    public final int height;
    public final int width;
    public final double virtualHeight;
    public final double virtualWidth;

    public final double[][] values;

    public Field(final int height,
                 final int width,
                 double virtualHeight,
                 double virtualWidth) {
        this.height = height;
        this.width = width;
        this.virtualHeight = virtualHeight;
        this.virtualWidth = virtualWidth;

        values = new double[height][width];
    }

    public double virtualY(final int i) {
        return virtualHeight * ((i / ((double) height)) - 0.5);
    }

    public double virtualX(final int j) {
        return virtualWidth * ((j / ((double) width)) - 0.5);
    }

    public double virtualXMin() {
        return virtualX(0);
    }

    public double virtualYMin() {
        return virtualY(0);
    }

    public void clear() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                values[i][j] = 0;
            }
        }
    }

    public void normalize() {
        double max = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double d = values[i][j];
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
                values[i][j] /= max;
            }
        }
    }

    public void posNorm() {
        double max = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double d = values[i][j];
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
                values[i][j] = ((values[i][j] / max) + 1) / 2;
            }
        }
    }

    public void highPass(final double threshold) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (values[i][j] < threshold) {
                    values[i][j] = threshold;
                }
            }
        }
    }

    public void add(Field other) {
        if (other.height != height || other.width != width) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                values[i][j] += other.values[i][j];
            }
        }
    }
}
