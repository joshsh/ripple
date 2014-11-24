package net.fortytwo.ripple.cli;

import net.fortytwo.flow.Buffer;
import net.fortytwo.flow.HistorySink;
import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Switch;
import net.fortytwo.flow.SynchronizedSink;
import net.fortytwo.flow.Tee;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.control.TaskSet;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StatementPatternQuery;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.commands.RippleQueryCmd;
import org.openrdf.model.vocabulary.RDF;

/**
 * A command for evaluating a Ripple query at the command line.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class VisibleQueryCommand extends Command {

    private final ListAST query;
    private final HistorySink<RippleList> resultHistory;

    private TaskSet taskSet;

    private Switch<RippleList> results;

    public VisibleQueryCommand(final ListAST query,
                               final HistorySink<RippleList> history) {
        this.query = query;
        resultHistory = history;
    }

    public void execute(final QueryEngine qe, final ModelConnection mc)
            throws RippleException {
        resultHistory.advance();

        boolean doBuffer = Ripple.getConfiguration().getBoolean(
                Ripple.BUFFER_QUERY_RESULTS);

        qe.getPrintStream().println("");

        // Results are first dereferenced, then placed into a buffer which
        // will be flushed into the view after the lexicon is updated.
        TurtleView view = new TurtleView(
                qe.getPrintStream(), mc);

        Buffer<RippleList> buffer = doBuffer ? new Buffer<RippleList>(view) : null;

        Sink<RippleList> med = new SynchronizedSink<RippleList>(
                (doBuffer
                        ? buffer
                        : view));

        results = new Switch<RippleList>(
                new Tee<RippleList>(med, resultHistory),
                new NullSink<RippleList>());

        Sink<RippleList> derefSink = new Sink<RippleList>() {
            public void put(final RippleList list) throws RippleException {
                dereference(list.getFirst(), mc);
                results.put(list);
            }
        };

        Command cmd = new RippleQueryCmd(query, derefSink);

        // Execute the inner command and wait until it is finished.
        cmd.setQueryEngine(qe);
        taskSet = new TaskSet();
        taskSet.add(cmd);
        taskSet.waitUntilEmpty();

        // Flush results to the view.
        if (doBuffer) {
            buffer.flush();
        }

        if (view.size() > 0) {
            qe.getPrintStream().println("");
        }
    }

    public String getName() {
        // Note: never used
        return "visible-query";
    }

    protected void abort() {
        // Late arrivals should not show up in the view.
        results.flip();

        taskSet.stopWaiting();
    }

    private static void dereference(final Object v, final ModelConnection mc)
            throws RippleException {
        try {
            if (null != mc.toRDF(v)) {
                StatementPatternQuery query = new StatementPatternQuery(v, RDF.FIRST, null);
                mc.query(query, new NullSink<>(), false);
            }
        } catch (RippleException e) {
            // (soft fail... don't even log the error)
        }
    }
}
