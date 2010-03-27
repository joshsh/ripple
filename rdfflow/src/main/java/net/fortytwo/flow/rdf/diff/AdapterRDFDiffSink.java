package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.diff.AdapterDiffSink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.AdapterRDFSink;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.flow.AdapterSink;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * User: josh
 * Date: Mar 26, 2010
 * Time: 6:47:47 PM
 */
public class AdapterRDFDiffSink<E extends Exception, F extends Exception> implements RDFDiffSink<F> {
    private final RDFSink<F> adderSink;
    private final RDFSink<F> subtractorSink;
    private final DiffSink<Statement, F> stSink;
    private final DiffSink<Namespace, F> nsSink;
    private final DiffSink<String, F> cmtSink;

    public AdapterRDFDiffSink(final RDFDiffSink<E> baseSink,
                             final AdapterSink.ExceptionAdapter<F> exAdapter) {
        adderSink = new AdapterRDFSink<E, F>(baseSink.adderSink(), exAdapter);
        subtractorSink = new AdapterRDFSink<E, F>(baseSink.subtractorSink(), exAdapter);
        stSink = new AdapterDiffSink<Statement, E, F>(baseSink.statementSink(), exAdapter);
        nsSink = new AdapterDiffSink<Namespace, E, F>(baseSink.namespaceSink(), exAdapter);
        cmtSink = new AdapterDiffSink<String, E, F>(baseSink.commentSink(), exAdapter);
    }

    public RDFSink<F> adderSink() {
        return adderSink;
    }

    public RDFSink<F> subtractorSink() {
        return subtractorSink;
    }

    public DiffSink<Statement, F> statementSink() {
        return stSink;
    }

    public DiffSink<Namespace, F> namespaceSink() {
        return nsSink;
    }

    public DiffSink<String, F> commentSink() {
        return cmtSink;
    }
}
