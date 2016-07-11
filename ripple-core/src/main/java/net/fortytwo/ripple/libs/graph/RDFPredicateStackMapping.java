package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFPredicateMapping;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StatementPatternQuery;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class RDFPredicateStackMapping extends PrimitiveStackMapping {
    private final boolean inverted;
    protected StackMapping inverse;

    public RDFPredicateStackMapping(final boolean inverted) {
        super();

        this.inverted = inverted;
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return inverse;
    }

    protected RDFPredicateMapping getMapping(final Value predicate,
                                             final Value context) {
        StatementPatternQuery.Pattern type = inverted
                ? StatementPatternQuery.Pattern.PO_S
                : StatementPatternQuery.Pattern.SP_O;
        return new RDFPredicateMapping(type, predicate, context);
    }
}
