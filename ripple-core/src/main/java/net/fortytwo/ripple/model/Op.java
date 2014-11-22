package net.fortytwo.ripple.model;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Op {

    public boolean equals(final Object other) {
        return other instanceof Op;
    }

    public int hashCode() {
        // Arbitrary.
        return 1056205736;
    }

    public String toString() {
        return "op";
    }
}

