package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFPredicateMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StatementPatternQuery;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class RDFPredicateStackMapping extends PrimitiveStackMapping {
    private final boolean inverted;
    protected StackMapping inverse;

    public RDFPredicateStackMapping(final boolean inverted) throws RippleException {
        super();

        this.inverted = inverted;
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return inverse;
    }

    protected RDFPredicateMapping getMapping(final RDFValue predicate,
                                             final RDFValue context)
            throws RippleException {
        StatementPatternQuery.Pattern type = inverted
                ? StatementPatternQuery.Pattern.PO_S
                : StatementPatternQuery.Pattern.SP_O;
        return new RDFPredicateMapping(type, predicate, context);
    }
}
