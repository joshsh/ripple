package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number representing an angle in radians and
 * produces its tangent.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Tan extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "tan",
            MathLibrary.NS_2008_08 + "tan",
            MathLibrary.NS_2007_08 + "tan"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Tan() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  tan(x)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;
        double a;

        a = mc.toNumber(stack.getFirst()).doubleValue();
        stack = stack.getRest();

// TODO: check for undefined values
        double d = Math.tan(a);

        solutions.accept(
                stack.push(d));
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return MathLibrary.getAtanValue();
    }
}

