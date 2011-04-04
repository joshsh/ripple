/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/libs/analysis/ToDouble.java $
 * $Revision: 106 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import org.apache.log4j.Logger;
import org.openrdf.model.Value;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;

/**
 * A primitive which consumes a SPARQL query and pushes, for each result, a variable number of values onto the stack, one for each binding.
 * FIXME: this is very much a hack.
 */
public class Sparql extends PrimitiveStackMapping {
    private static final Logger LOGGER
            = Logger.getLogger(Sparql.class);

    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "sparql"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Sparql()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("query", null, true)};
    }

    public String getComment() {
        return "evaluates a SPARQL eury";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        String query = mc.toString(stack.getFirst());
        stack = stack.getRest();

        CloseableIteration<? extends BindingSet, QueryEvaluationException> results
                = mc.evaluate(query);

        try {
            try {
                while (results.hasNext()) {
                    RippleList newStack = stack;

                    for (Binding b : results.next()) {
                        Value v = b.getValue();
                        newStack = newStack.push(new RDFValue(v));
                    }

                    try {
                        solutions.put(arg.with(newStack));
                    } catch (RippleException e) {
                        // Soft fail
                        e.logError();
                    }
                }
            } finally {
                results.close();
            }
        } catch (QueryEvaluationException e) {
            throw new RippleException(e);
        }
    }
}
