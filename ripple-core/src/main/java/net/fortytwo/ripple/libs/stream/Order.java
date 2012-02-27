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
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
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
            StreamLibrary.NS_2011_08 + "order"};

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

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        // FIXME: cheat to temporarily disable asynchronous query answering
        boolean a = Ripple.asynchronousQueries();
        Ripple.enableAsynchronousQueries(false);
        try {
            Collector<RippleList> s = new Collector<RippleList>();
            StackEvaluator e = new LazyStackEvaluator();
            e.apply(arg, s, mc);

            List<RippleList> all = new LinkedList<RippleList>();
            for (RippleList c : s) {
                all.add(c);
            }
            Collections.sort(all, mc.getComparator());

            for (RippleList l : all) {
                solutions.put(l);
            }
        } finally {
            Ripple.enableAsynchronousQueries(a);
        }
    }
}
