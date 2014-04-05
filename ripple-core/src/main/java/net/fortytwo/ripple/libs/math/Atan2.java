package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number and produces its two-argument arc tangent.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Atan2 extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "atan2"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Atan2()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("y", null, true)};
    }

    public String getComment() {
        return "x y  =>  atan2(x, y)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        double x, y, result;

        x = mc.toNumericValue(stack.getFirst()).doubleValue();
        stack = stack.getRest();
        y = mc.toNumericValue(stack.getFirst()).doubleValue();
        stack = stack.getRest();

        if (x != 0 || y != 0) {
            result = Math.atan2(x, y);

            solutions.put(
                    stack.push(mc.valueOf(result)));
        }
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return MathLibrary.getTanValue();
    }
}

