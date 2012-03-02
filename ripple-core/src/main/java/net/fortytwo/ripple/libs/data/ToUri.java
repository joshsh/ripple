/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.graph.GraphLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a literal value and produces the resource
 * identified by the corresponding URI (if any).
 */
public class ToUri extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_2011_08 + "to-uri",
                GraphLibrary.NS_2008_08 + "toUri",
                GraphLibrary.NS_2007_08 + "toUri"};
    }

    public ToUri()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true)};
    }

    public String getComment() {
        return "s  =>  uri -- where uri is the URI whose string representation is s";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        String s;

        s = mc.toString(stack.getFirst());
        stack = stack.getRest();

        RDFValue uri = mc.uriValue(s);

        solutions.put(
                stack.push(uri));
    }
}

