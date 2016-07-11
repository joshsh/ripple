package net.fortytwo.ripple.query.commands;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.StackEvaluator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
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
        final Collector<RippleList> expressions = new Collector<>();

        final Sink<RippleList> exprSink = l -> {
            // Note: the first element of the list will also be a list
            final RippleList stack = ((RippleList) l.getFirst()).invert();

            expressions.accept(stack);
        };

        query.evaluate(exprSink, qe, mc);

        evaluator = qe.getEvaluator();

        final Sink<RippleList> evaluatorSink = l -> evaluator.apply(l, sink, mc);

        expressions.writeTo(evaluatorSink);
    }

    public String getName() {
        return "ripple-query";
    }

    protected void abort() {
        if (null != evaluator) {
            evaluator.stop();
        }
    }
}

