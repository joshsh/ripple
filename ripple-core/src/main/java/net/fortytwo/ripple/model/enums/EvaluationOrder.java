package net.fortytwo.ripple.model.enums;

import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public enum EvaluationOrder {
    EAGER("eager"),
    LAZY("lazy");

    private String name;

    private EvaluationOrder(final String name) {
        this.name = name;
    }

    public static EvaluationOrder find(final String name)
            throws RippleException {
        for (EvaluationOrder order : EvaluationOrder.values()) {
            if (order.name.equals(name)) {
                return order;
            }
        }

        throw new RippleException("unknown EvaluationOrder: " + name);
    }
}
