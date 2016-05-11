package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SingleContextPipe implements RDFSink {
    private final Sink<Statement> stSink;
    private final Sink<Namespace> nsSink;
    private final Sink<String> cmtSink;

    public SingleContextPipe(final RDFSink sink,
                             final Resource context,
                             final ValueFactory valueFactory) {
        final Sink<Statement> otherStSink = sink.statementSink();

        stSink = new Sink<Statement>() {
            public void accept(final Statement st) throws RippleException {
                Statement newSt;

                newSt = valueFactory.createStatement(
                        st.getSubject(), st.getPredicate(), st.getObject(), context);

                otherStSink.accept(newSt);
            }
        };

        nsSink = sink.namespaceSink();
        cmtSink = sink.commentSink();
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

