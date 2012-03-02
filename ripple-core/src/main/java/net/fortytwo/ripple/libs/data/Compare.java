/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.graph.GraphLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes two resources and produces a comparison value
 * according to their data type.
 */
public class Compare extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_2011_08 + "compare",
                GraphLibrary.NS_2008_08 + "compare",
                GraphLibrary.NS_2007_08 + "compare",
                GraphLibrary.NS_2007_05 + "compare"};
    }

    public Compare()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x y  =>  i  -- where i is -1 if x < y, 0 if x = y, and 1 if x > y";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        RippleValue y, x;

        y = stack.getFirst();
        stack = stack.getRest();
        x = stack.getFirst();
        stack = stack.getRest();

        int result = mc.getComparator().compare(x, y);

        // Constrain the result to three possible values.
        result = (result < 0) ? -1 : (result > 0) ? 1 : 0;

        solutions.put(stack.push(mc.numericValue(result)));
    }
}

