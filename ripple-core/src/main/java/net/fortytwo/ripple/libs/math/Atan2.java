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
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number and produces its two-argument arc tangent.
 */
public class Atan2 extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2011_08 + "atan2"};

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

    public void apply(final StackContext arg,
                      final Sink<StackContext> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        double x, y, result;

        x = mc.toNumericValue(stack.getFirst()).doubleValue();
        stack = stack.getRest();
        y = mc.toNumericValue(stack.getFirst()).doubleValue();
        stack = stack.getRest();

        if (x != 0 || y != 0) {
            result = Math.atan2(x, y);

            solutions.put(arg.with(
                    stack.push(mc.numericValue(result))));
        }
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return MathLibrary.getTanValue();
    }
}

