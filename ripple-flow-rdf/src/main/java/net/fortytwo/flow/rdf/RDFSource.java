package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Source;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class RDFSource {
    public abstract Source<Statement> statementSource();

    public abstract Source<Namespace> namespaceSource();

    public abstract Source<String> commentSource();

    public void writeTo(final RDFSink sink) throws RippleException {
        commentSource().writeTo(sink.commentSink());

        // Note: it's often important that namespaces are written before
        // statements.
        namespaceSource().writeTo(sink.namespaceSink());

        statementSource().writeTo(sink.statementSink());
    }
}
