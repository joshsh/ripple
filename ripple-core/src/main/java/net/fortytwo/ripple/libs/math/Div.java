package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a numerator and divisor and produces their
 * quotient.  If the divisor is 0, no value is produced.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Div extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "div",
            MathLibrary.NS_2008_08 + "div",
            MathLibrary.NS_2007_08 + "div",
            MathLibrary.NS_2007_05 + "div"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Div()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("y", null, true)};
    }

    public String getComment() {
        return "x y  =>  y / x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        NumericValue a, b, result;

        b = mc.toNumericValue(stack.getFirst());
        stack = stack.getRest();
        a = mc.toNumericValue(stack.getFirst());
        stack = stack.getRest();

        // Note: division by zero simply does not yield a result.
        if (!b.isZero()) {
            result = a.div(b);

            solutions.put(
                    stack.push(result));
        }
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return MathLibrary.getMulValue();
    }
}

