package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;

/**
 * An RdfSink which passes its input into an RDFHandler.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SesameOutputAdapter implements RDFSink {
    private final RDFHandler handler;

    private final Sink<Statement> stSink;
    private final Sink<Namespace> nsSink;
    private final Sink<String> cmtSink;

    public SesameOutputAdapter(final RDFHandler handler) {
        this.handler = handler;

        stSink = st -> {
            try {
                handler.handleStatement(st);
            } catch (RDFHandlerException e) {
                throw new RippleException(e);
            }
        };

        nsSink = ns -> {
            try {
                handler.handleNamespace(ns.getPrefix(), ns.getName());
            } catch (RDFHandlerException e) {
                throw new RippleException(e);
            }
        };

        cmtSink = comment -> {
            try {
                handler.handleComment(comment);
            } catch (RDFHandlerException e) {
                throw new RippleException(e);
            }
        };
    }

    public void startRDF() throws RDFHandlerException {
        handler.startRDF();
    }

    public void endRDF() throws RDFHandlerException {
        handler.endRDF();
    }

    public Sink<Statement> statementSink() {
        return stSink;
    }

    public Sink<Namespace> namespaceSink() {
        return nsSink;
    }

    public Sink<String> commentSink() {
        return cmtSink;
    }
}

