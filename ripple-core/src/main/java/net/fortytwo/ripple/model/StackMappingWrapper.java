package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.util.ModelConnectionHelper;
import org.openrdf.model.Value;

/**
 * A wrapper for a stack mapping with a settable RDF equivalent
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StackMappingWrapper implements StackMapping {
    private final Value rdfEquivalent;

    private final StackMapping innerMapping;

    public StackMappingWrapper(final StackMapping wrapped, final ModelConnection mc) throws RippleException {
        this.innerMapping = wrapped;

        // Uses a random identifier... not for actual use
        rdfEquivalent = new ModelConnectionHelper(mc).createRandomURI();
    }

    public int arity() {
        return innerMapping.arity();
    }

    public boolean isTransparent() {
        return innerMapping.isTransparent();
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        innerMapping.apply(arg, solutions, mc);
    }

    public StackMapping getInverse() throws RippleException {
        return innerMapping.getInverse();
    }

    public StackMapping getInnerMapping() {
        return innerMapping;
    }

    public Value getRDFEquivalent() {
        return rdfEquivalent;
    }
}
