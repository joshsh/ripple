package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.impl.sesame.types.NumericType;

/**
 * A primitive which consumes a number and produces its sign.  This has three
 * possible values: -1 if the number is less than 0, 0 if the number is equal to
 * 0, and 1 if the number is greater than 0.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Sign extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "sign",
            MathLibrary.NS_2008_08 + "sign",
            MathLibrary.NS_2007_08 + "signum",
            MathLibrary.NS_2007_05 + "sign"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Sign()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  sign of x (-1, 0, or +1)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Number a, result;

        a = mc.toNumber(stack.getFirst());
        stack = stack.getRest();

        result = NumericType.sign(a);

        solutions.put(
                stack.push(result));
    }
}

