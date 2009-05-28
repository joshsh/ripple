/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.neo4j;

import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.impl.neo4j.Neo4jNode;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.RippleException;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 1:15:23 PM
 */
public class GetProperty extends PrimitiveStackMapping {

    private static final String[] IDENTIFIERS = {
            Neo4jLibrary.NS_2008_08 + "getProperty"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("node", null, true),
                new Parameter("label", null, true)
        };
    }

    public String getComment() {
        return "gets the value for a property";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions) throws RippleException {
        ModelConnection mc = arg.getModelConnection();

        RippleList stack = arg.getStack();
        String label = mc.toString(stack.getFirst());
        stack = stack.getRest();
        RippleValue node = stack.getFirst();
        stack = stack.getRest();

        if (node instanceof Neo4jNode) {
            Object val = ((Neo4jNode) node).getNode().getProperty(label);

            // FIXME: this is a hack
            String result = val.toString();

            solutions.put(arg.with(stack.push(mc.value(result))));
        } else {
            throw new RippleException("argument " + node + " should be a Neo4jNode");
        }
    }
}
