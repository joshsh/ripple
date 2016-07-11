package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.types.NumericType;

/**
 * A primitive which consumes a number and produces its additive inverse.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Neg extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "neg",
            MathLibrary.NS_2008_08 + "neg",
            MathLibrary.NS_2007_08 + "neg",
            MathLibrary.NS_2007_05 + "neg"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Neg() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  -x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Number a, result;

        a = mc.toNumber(stack.getFirst());
        stack = stack.getRest();

        result = NumericType.neg(a);

        solutions.accept(
                stack.push(result));
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        // neg is its own inverse
        return this;
    }
}

