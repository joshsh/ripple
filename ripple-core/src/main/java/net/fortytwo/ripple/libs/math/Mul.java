package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.types.NumericType;

/**
 * A primitive which consumes two numbers and produces their product.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Mul extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "mul",
            MathLibrary.NS_2008_08 + "mul",
            MathLibrary.NS_2007_08 + "mul",
            MathLibrary.NS_2007_05 + "mul"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Mul()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("y", null, true)};
    }

    public String getComment() {
        return "x y  =>  x * y";
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

        result = NumericType.mul(a, b);

        solutions.accept(
                stack.push(result));
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return MathLibrary.getDivValue();
    }
}

