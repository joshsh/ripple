package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes two numbers x and y and produces the number x to
 * the power of y.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Pow extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "pow",
            MathLibrary.NS_2008_08 + "pow",
            MathLibrary.NS_2007_08 + "pow",
            MathLibrary.NS_2007_05 + "pow"};

    private final StackMapping self = this;

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Pow()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("p", null, true)};
    }

    public String getComment() {
        return "x p  =>  x^p";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        NumericValue p, x, result;

        p = mc.toNumericValue(stack.getFirst());
        stack = stack.getRest();
        x = mc.toNumericValue(stack.getFirst());
        stack = stack.getRest();

        result = x.pow(p);

        solutions.put(
                stack.push(result));
    }

    @Override
    public StackMapping getInverse() {
        return new StackMapping() {
            public int arity() {
                return 2;
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

                NumericValue a, c, result;
                c = mc.toNumericValue(stack.getFirst());
                stack = stack.getRest();
                a = mc.toNumericValue(stack.getFirst());
                stack = stack.getRest();

                if (a.doubleValue() > 0 && c.doubleValue() > 0) {
                    result = mc.valueOf(Math.log(c.doubleValue()) / Math.log(a.doubleValue()));
                    solutions.put(stack.push(result));
                }
            }
        };
    }
}

