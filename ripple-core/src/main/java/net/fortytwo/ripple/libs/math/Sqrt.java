/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number and produces all real square roots of the
 * number.
 */
public class Sqrt extends PrimitiveStackMapping {
    private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2011_08 + "sqrt",
            MathLibrary.NS_2008_08 + "sqrt",
            MathLibrary.NS_2007_08 + "sqrt"};

    private final StackMapping self;

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Sqrt()
            throws RippleException {
        super();

        this.self = this;
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  real square root(s) of x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        double a;

        a = mc.toNumericValue(stack.getFirst()).doubleValue();
        stack = stack.getRest();

        // Apply the function only if it is defined for the given argument.
        if (a >= 0) {
            double d = Math.sqrt(a);

            // Yield both square roots.
            try {
                solutions.put(
                        stack.push(mc.numericValue(d)));
                if (d > 0) {
                    solutions.put(
                            stack.push(mc.numericValue(0.0 - d)));
                }
            } catch (RippleException e) {
                // Soft fail
                e.logError();
            }
        }
    }

    @Override
    public StackMapping getInverse() {
        return new Square();
    }

    private class Square implements StackMapping {
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

            NumericValue a, result;

            a = mc.toNumericValue(stack.getFirst());
            stack = stack.getRest();

            result = a.mul(a);

            solutions.put(
                    stack.push(result));
        }
    }
}

