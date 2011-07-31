/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.data;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.graph.GraphLibrary;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * A primitive which consumes a resource or literal value and produces its
 * string representation.  For literal values, this is the same literal
 * value but with a type of xsd:string.  For resources identified by URIs,
 * this is the URI as a string.  For blank nodes, this is the identifier of
 * the node.
 */
public class ToString extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_2011_08 + "to-string",
                GraphLibrary.NS_2008_08 + "toString",
                GraphLibrary.NS_2007_08 + "toString",
                GraphLibrary.NS_2007_05 + "toString"};
    }

    public ToString()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  string representation of x";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        RippleValue v;

        v = stack.getFirst();
        stack = stack.getRest();

        solutions.put(arg.with(
                stack.push(mc.typedValue(mc.toString(v), XMLSchema.STRING))));
    }
}

