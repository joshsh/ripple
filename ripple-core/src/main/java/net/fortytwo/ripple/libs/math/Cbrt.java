package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.types.NumericType;

/**
 * A primitive which consumes a number and produces its real cube root.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Cbrt extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "cbrt",
            MathLibrary.NS_2008_08 + "cbrt",
            MathLibrary.NS_2007_08 + "cbrt"};

    private final StackMapping self;

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Cbrt()
            throws RippleException {
        super();

        this.self = this;
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  real cube root of x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Number a, result;

        a = mc.toNumber(stack.getFirst());
        stack = stack.getRest();

        result = Math.cbrt(a.doubleValue());

        solutions.accept(
                stack.push(result));
    }

    @Override
    public StackMapping getInverse() {
        return new Cube();
    }

    private class Cube implements StackMapping {
        public int arity() {
            return 1;
        }

        public StackMapping getInverse() throws RippleException {
            return self;
        }

        public boolean isTransparent() {
            return true;
        }

        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            RippleList stack = arg;

            Number a, result;

            a = mc.toNumber(stack.getFirst());
            stack = stack.getRest();

            result = NumericType.mul(a, NumericType.mul(a,a));

            solutions.accept(
                    stack.push(result));
        }
    }
}

