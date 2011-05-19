/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/libs/stream/Both.java $
 * $Revision: 106 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Op;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.query.LazyStackEvaluator;
import net.fortytwo.ripple.query.StackEvaluator;

/**
 *
 */
// FIXME: total hack
public class Count extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2011_04 + "count",
            StreamLibrary.NS_2008_08 + "count"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Count()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("mapping",
                        "a mapping to apply before counting",
                        false)
        };
    }

    public String getComment() {
        return "m -> m op, for which solutions are found and counted, then the result replaces m on the stack";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions)
            throws RippleException {
        // FIXME: cheat to temporarily disable asynchronous query answering
        boolean a = Ripple.asynchronousQueries();
        Ripple.enableAsynchronousQueries(false);

        ModelConnection mc = arg.getModelConnection();

        RippleList stack = arg.getStack();

        Collector<StackContext, RippleException> s = new Collector<StackContext, RippleException>();
        StackEvaluator e = new LazyStackEvaluator();
        e.apply(arg.with(stack.push(Operator.OP)), s);
        int count = s.size();

        solutions.put(arg.with(
                arg.getStack().getRest().push(
                        mc.numericValue(count))));

        Ripple.enableAsynchronousQueries(a);
    }
}
