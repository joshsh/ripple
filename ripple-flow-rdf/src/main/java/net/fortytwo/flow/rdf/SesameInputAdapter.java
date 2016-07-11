package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.NamespaceImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import java.io.IOException;
import java.io.InputStream;

/**
 * An RDFHandler which passes its input into an RdfSink.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SesameInputAdapter implements RDFHandler {
    private final Sink<Statement> stSink;
    private final Sink<Namespace> nsSink;
    private final Sink<String> cmtSink;

    public SesameInputAdapter(final RDFSink sink) {
        stSink = sink.statementSink();
        nsSink = sink.namespaceSink();
        cmtSink = sink.commentSink();
    }

    /**
     * Handles a comment.
     */
    public void handleComment(final String comment) {
        try {
            cmtSink.accept(comment);
        } catch (Exception e) {
            // Log the error, but continue.
            logError(e);
        }
    }

    /**
     * Handles a namespace declaration/definition.
     */
    public void handleNamespace(final String prefix, final String uri) {
        try {
            nsSink.accept(new NamespaceImpl(prefix, uri));
        } catch (Exception e) {
            // Log the error, but continue.
            logError(e);
        }
    }

    /**
     * Handles a statement.
     */
    public void handleStatement(final Statement st) {
        try {
            stSink.accept(st);
        } catch (Exception e) {
            // Log the error, but continue.
            logError(e);
        }
    }

    /**
     * Signals the start of the RDF data.
     */
    public void startRDF() {
    }

    /**
     * Signals the end of the RDF data.
     */
    public void endRDF() {
    }

    private void logError(Exception e) {
        // FIXME
        System.err.println("Error: " + e);
    }

    public static void parse(final InputStream is,
                             final RDFSink sink,
                             final String baseUri,
                             final RDFFormat format) throws RippleException {
        RDFParser parser = Rio.createParser(format);
        parser.setRDFHandler(new SesameInputAdapter(sink));

        try {
            parser.parse(is, baseUri);
        } catch (IOException | RDFParseException | RDFHandlerException e) {
            throw new RippleException(e);
        }
    }
}

