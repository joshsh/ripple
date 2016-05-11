package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number representing an angle in radians and
 * produces its sine.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Sin extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "sin",
            MathLibrary.NS_2008_08 + "sin",
            MathLibrary.NS_2007_08 + "sin"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Sin()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  sin(x)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Number a, result;

        a = mc.toNumber(stack.getFirst());
        stack = stack.getRest();

        result = Math.sin(a.doubleValue());

        solutions.accept(
                stack.push(result));
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return MathLibrary.getAsinValue();
    }
}

