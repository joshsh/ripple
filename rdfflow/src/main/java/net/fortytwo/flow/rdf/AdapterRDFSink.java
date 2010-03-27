package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.AdapterSink;
import net.fortytwo.flow.rdf.RDFSink;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * User: josh
 * Date: Mar 26, 2010
 * Time: 6:48:49 PM
 */
public class AdapterRDFSink<E extends Exception, F extends Exception> implements RDFSink<F> {
    private final Sink<Statement, F> stSink;
    private final Sink<Namespace, F> nsSink;
    private final Sink<String, F> cmtSink;

    public AdapterRDFSink(final RDFSink<E> baseSink,
                         final AdapterSink.ExceptionAdapter<F> exAdapter) {
        final Sink<Statement, E> baseStSink = baseSink.statementSink();
        final Sink<Namespace, E> baseNsSink = baseSink.namespaceSink();
        final Sink<String, E> baseCmtSink = baseSink.commentSink();

        stSink = new AdapterSink<Statement, E, F>(baseStSink, exAdapter);
        nsSink = new AdapterSink<Namespace, E, F>(baseNsSink, exAdapter);
        cmtSink = new AdapterSink<String, E, F>(baseCmtSink, exAdapter);
    }

    public Sink<Statement, F> statementSink() {
        return stSink;
    }

    public Sink<Namespace, F> namespaceSink() {
        return nsSink;
    }

    public Sink<String, F> commentSink() {
        return cmtSink;
    }
}
