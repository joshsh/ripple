package net.fortytwo.ripple.model.enums;

import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public enum ExpressionOrder {
    DIAGRAMMATIC("diagrammatic"),
    ANTIDIAGRAMMATIC("antidiagrammatic");

    private String name;

    private ExpressionOrder(final String n) {
        name = n;
    }

    public static ExpressionOrder find(final String name)
            throws RippleException {
        for (ExpressionOrder x : ExpressionOrder.values()) {
            if (x.name.equals(name)) {
                return x;
            }
        }

        throw new RippleException("unknown ExpressionOrder: '" + name + "'");
    }
}
