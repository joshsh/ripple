/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A filter which consumes two items and produces each item in its own stack.
 */
public class Both extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2011_08 + "both",
            StreamLibrary.NS_2008_08 + "both",
            StreamLibrary.NS_2007_08 + "union",
            StreamLibrary.NS_2007_05 + "union"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Both()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("y", null, true)};
    }

    public String getComment() {
        return "x y -> x, y";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;
        RippleValue x, y;

        x = stack.getFirst();
        stack = stack.getRest();
        y = stack.getFirst();
        stack = stack.getRest();

        try {
            solutions.put(stack.push(x));
            solutions.put(stack.push(y));
        } catch (RippleException e) {
            // Soft fail
            e.logError();
        }
    }
}

