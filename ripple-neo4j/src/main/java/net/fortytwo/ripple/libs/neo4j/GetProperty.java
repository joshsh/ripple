/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
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
import net.fortytwo.ripple.libs.etc.EtcLibrary;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 1:15:23 PM
 */
public class GetProperty extends PrimitiveStackMapping {
    public int arity() {
        return 2;
    }

    private static final String[] IDENTIFIERS = {
            Neo4jLibrary.NS_2008_06 + "getProperty"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public void applyTo(final StackContext arg,
                        final Sink<StackContext, RippleException> sink) throws RippleException {
        ModelConnection mc = arg.getModelConnection();

        RippleList stack = arg.getStack();
        String label = mc.toString(stack.getFirst());
        RippleValue node = stack.getFirst();
        stack = stack.getRest();
        stack = stack.getRest();

        if (node instanceof Neo4jNode) {
            Object val = ((Neo4jNode) node).getNode().getProperty(label);

            // FIXME: this is a hack
            String result = val.toString();

            sink.put(arg.with(stack.push(mc.value(result))));
        } else {
            throw new RippleException("argument " + node + " should be a Neo4jNode");
        }
    }
}
