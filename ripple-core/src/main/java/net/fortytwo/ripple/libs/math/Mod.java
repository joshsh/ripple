package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.types.NumericType;

/**
 * A primitive which consumes two numbers x and y and produces the number x
 * modulo y.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Mod extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "mod",
            MathLibrary.NS_2008_08 + "mod",
            MathLibrary.NS_2007_08 + "mod",
            MathLibrary.NS_2007_05 + "mod"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Mod()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("y", null, true)};
    }

    public String getComment() {
        return "x y  =>  x % y";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Number a, b, result;

        b = mc.toNumber(stack.getFirst());
        stack = stack.getRest();
        a = mc.toNumber(stack.getFirst());
        stack = stack.getRest();

        // Note: mod by zero simply does not yield a result.
        if (!NumericType.isZero(b)) {
            result = NumericType.mod(a, b);

            solutions.put(
                    stack.push(result));
        }
    }
}

