package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SailConnectionRDFDiffSink implements RDFDiffSink {
    private final RDFSink addSink;
    private final RDFSink subtractSink;
    private final DiffSink<Statement> stSink;
    private final DiffSink<Namespace> nsSink;
    private final DiffSink<String> cmtSink;

    public SailConnectionRDFDiffSink(final SailConnection sailConnection) {
        final Sink<Statement> addStatementSink = statement -> {
            try {
                sailConnection.addStatement(
                        statement.getSubject(),
                        statement.getPredicate(),
                        statement.getObject(),
                        statement.getContext());
            } catch (SailException e) {
                throw new RippleException(e);
            }
        };

        final Sink<Statement> subtractStatementSink = statement -> {
            try {
                sailConnection.removeStatements(
                        statement.getSubject(),
                        statement.getPredicate(),
                        statement.getObject(),
                        statement.getContext());
            } catch (SailException e) {
                throw new RippleException(e);

            }
        };

        final Sink<Namespace> addNamespaceSink = namespace -> {
            try {
                sailConnection.setNamespace(namespace.getPrefix(), namespace.getName());
            } catch (SailException e) {
                throw new RippleException(e);
            }
        };

        final Sink<Namespace> subtractNamespaceSink = namespace -> {
            String name = null;
            try {
                name = sailConnection.getNamespace(namespace.getPrefix());

                if (null != name && name.equals(namespace.getName())) {
                    sailConnection.removeNamespace(namespace.getPrefix());
                }
            } catch (SailException e) {
                throw new RippleException(e);
            }
        };

        final Sink<String> addCommentSink = new NullSink<>();

        final Sink<String> subtractCommentSink = new NullSink<>();

        addSink = new RDFSink() {
            public Sink<Statement> statementSink() {
                return addStatementSink;
            }

            public Sink<Namespace> namespaceSink() {
                return addNamespaceSink;
            }

            public Sink<String> commentSink() {
                return addCommentSink;
            }
        };

        subtractSink = new RDFSink() {
            public Sink<Statement> statementSink() {
                return subtractStatementSink;
            }

            public Sink<Namespace> namespaceSink() {
                return subtractNamespaceSink;
            }

            public Sink<String> commentSink() {
                return subtractCommentSink;
            }
        };

        stSink = new DiffSink<Statement>() {
            public Sink<Statement> getPlus() {
                return addStatementSink;
            }

            public Sink<Statement> getMinus() {
                return subtractStatementSink;
            }
        };

        nsSink = new DiffSink<Namespace>() {
            public Sink<Namespace> getPlus() {
                return addNamespaceSink;
            }

            public Sink<Namespace> getMinus() {
                return subtractNamespaceSink;
            }
        };

        cmtSink = new DiffSink<String>() {
            public Sink<String> getPlus() {
                return addCommentSink;
            }

            public Sink<String> getMinus() {
                return subtractCommentSink;
            }
        };
    }

    public RDFSink adderSink() {
        return addSink;
    }

    public RDFSink subtractorSink() {
        return subtractSink;
    }


    public DiffSink<Statement> statementSink() {
        return stSink;
    }

    public DiffSink<Namespace> namespaceSink() {
        return nsSink;
    }

    public DiffSink<String> commentSink() {
        return cmtSink;
    }
}
