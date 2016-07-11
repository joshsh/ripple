package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Statement;
import org.openrdf.sail.SailConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SailConnectionListenerAdapter implements SailConnectionListener {
    private static final Logger logger
            = LoggerFactory.getLogger(SailConnectionListenerAdapter.class.getName());

    private final Sink<Statement> addSink, subSink;

    public SailConnectionListenerAdapter(final RDFDiffSink diffSink) {
        addSink = diffSink.adderSink().statementSink();
        subSink = diffSink.subtractorSink().statementSink();
    }

    public void statementAdded(final Statement st) {
        try {
            addSink.accept(st);
        } catch (RippleException e) {
            logger.warn("Unhandled exception" + e.getMessage());
        }
    }

    public void statementRemoved(final Statement st) {
        try {
            subSink.accept(st);
        } catch (RippleException e) {
            logger.warn("Unhandled exception" + e.getMessage());
        }
    }

}
