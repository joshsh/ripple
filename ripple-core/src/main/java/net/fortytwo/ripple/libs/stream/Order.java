/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/libs/stream/Both.java $
 * $Revision: 106 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.query.LazyStackEvaluator;
import net.fortytwo.ripple.query.StackEvaluator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
// FIXME: total hack
public class Order extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2011_04 + "order"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Order()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{};
    }

    public String getComment() {
        return "orders solutions according to Ripple's total order (closed world operation)";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions)
            throws RippleException {
        // FIXME: cheat to temporarily disable asynchronous query answering
        boolean a = Ripple.asynchronousQueries();
        Ripple.enableAsynchronousQueries(false);
        try {
            ModelConnection mc = arg.getModelConnection();

            Collector<StackContext, RippleException> s = new Collector<StackContext, RippleException>();
            StackEvaluator e = new LazyStackEvaluator();
            e.apply(arg, s);

            List<RippleList> all = new LinkedList<RippleList>();
            for (StackContext c : s) {
                all.add(c.getStack());
            }
            Collections.sort(all, mc.getComparator());

            for (RippleList l : all) {
                solutions.put(arg.with(l));
            }
        } finally {
            Ripple.enableAsynchronousQueries(a);
        }
    }
}
