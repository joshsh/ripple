/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.StackEvaluator;

public class RippleQueryCmd extends Command {
    private final ListAST query;
    private final Sink<RippleList> sink;

    private StackEvaluator evaluator;

    public RippleQueryCmd(final ListAST query,
                          final Sink<RippleList> sink) {
        this.query = query;
        this.sink = sink;
    }

    public void execute(final QueryEngine qe, final ModelConnection mc)
            throws RippleException {
        //System.out.println("executing query: " + query + " against " + ((SesameModelConnection) mc).getSailConnection());
        final Collector<RippleList> expressions = new Collector<RippleList>();

        final Sink<RippleList> exprSink = new Sink<RippleList>() {
            public void put(final RippleList l) throws RippleException {
                // Note: the first element of the list will also be a list
                final RippleList stack = ((RippleList) l.getFirst()).invert();

                expressions.put(stack);
            }
        };

        query.evaluate(exprSink, qe, mc);

        evaluator = qe.getEvaluator();

        final Sink<StackContext> resultSink = new Sink<StackContext>() {
            public void put(final StackContext arg) throws RippleException {
                sink.put(arg.getStack());
            }
        };

        final Sink<RippleList> evaluatorSink = new Sink<RippleList>() {
            // Note: v will always be a list.
            public void put(final RippleList l) throws RippleException {
                StackContext arg = new StackContext(l, mc);
                evaluator.apply(arg, resultSink);
            }
        };

//System.out.println( "evaluating: " + listAst );
        expressions.writeTo(evaluatorSink);
    }

    public String getName() {
        return "ripple-query";
    }

    protected void abort() {
//System.out.println( "aborting Ripple query command" );
        if (null != evaluator) {
            evaluator.stop();
        }
    }
}

