/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.system;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.math.MathLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.flow.Sink;

/**
 * A primitive which produces a random number between 0 and 1.
 */
public class Random extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                SystemLibrary.NS_2011_08 + "random",
                MathLibrary.NS_2008_08 + "random",
                MathLibrary.NS_2007_08 + "random"};
    }

    public Random()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{};
    }

    public String getComment() {
        return "=> x, where x is a pseudorandom number in the interval [0.0, 1.0)";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        NumericValue result;

        result = mc.numericValue(Math.random());

        solutions.put(arg.with(
                stack.push(result)));
    }
}

