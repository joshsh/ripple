package net.fortytwo.ripple.model.enums;

import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public enum ExpressionAssociativity {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    ExpressionAssociativity(final String n) {
        name = n;
    }

    public static ExpressionAssociativity find(final String name)
            throws RippleException {
        for (ExpressionAssociativity x : ExpressionAssociativity.values()) {
            if (x.name.equals(name)) {
                return x;
            }
        }

        String msg = "unknown ExpressionAssociativity: '" + name + "'";
        throw new RippleException(msg);
    }
}
