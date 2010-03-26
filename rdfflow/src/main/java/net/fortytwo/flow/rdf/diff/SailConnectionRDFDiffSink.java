package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.diff.DiffSink;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 2:32:16 PM
 */
public class SailConnectionRDFDiffSink implements RDFDiffSink<SailException>
{
    private final RDFSink<SailException> addSink;
    private final RDFSink<SailException> subtractSink;
    private final DiffSink<Statement, SailException> stSink;
    private final DiffSink<Namespace, SailException> nsSink;
    private final DiffSink<String, SailException> cmtSink;

    public SailConnectionRDFDiffSink(final SailConnection sailConnection)
    {
        final Sink<Statement, SailException> addStatementSink = new Sink<Statement, SailException>() {
            public void put(final Statement statement) throws SailException {
//System.out.println("    adding statement: " + statement);
                sailConnection.addStatement(statement.getSubject(), statement.getPredicate(), statement.getObject(), statement.getContext());
            }
        };

        final Sink<Statement, SailException> subtractStatementSink = new Sink<Statement, SailException>() {
            public void put(final Statement statement) throws SailException {
                sailConnection.removeStatements(statement.getSubject(), statement.getPredicate(), statement.getObject(), statement.getContext());
            }
        };

        final Sink<Namespace, SailException> addNamespaceSink = new Sink<Namespace, SailException>() {
            public void put(final Namespace namespace) throws SailException {
                sailConnection.setNamespace(namespace.getPrefix(), namespace.getName());
            }
        };

        final Sink<Namespace, SailException> subtractNamespaceSink = new Sink<Namespace, SailException>() {
            public void put(final Namespace namespace) throws SailException {
                String name = sailConnection.getNamespace(namespace.getPrefix());
                if (null != name && name.equals(namespace.getName())) {
                    sailConnection.removeNamespace(namespace.getPrefix());
                }
            }
        };

        final Sink<String, SailException> addCommentSink = new NullSink<String, SailException>();

        final Sink<String, SailException> subtractCommentSink = new NullSink<String, SailException>();

        addSink = new RDFSink<SailException>()
        {
            public Sink<Statement, SailException> statementSink()
            {
                return addStatementSink;
            }

            public Sink<Namespace, SailException> namespaceSink()
            {
                return addNamespaceSink;
            }

            public Sink<String, SailException> commentSink()
            {
                return addCommentSink;
            }
        };

        subtractSink = new RDFSink<SailException>()
        {
            public Sink<Statement, SailException> statementSink()
            {
                return subtractStatementSink;
            }

            public Sink<Namespace, SailException> namespaceSink()
            {
                return subtractNamespaceSink;
            }

            public Sink<String, SailException> commentSink()
            {
                return subtractCommentSink;
            }
        };

        stSink = new DiffSink<Statement, SailException>()
        {
            public Sink<Statement, SailException> getPlus()
            {
                return addStatementSink;
            }

            public Sink<Statement, SailException> getMinus()
            {
                return subtractStatementSink;
            }
        };

        nsSink = new DiffSink<Namespace, SailException>()
        {
            public Sink<Namespace, SailException> getPlus()
            {
                return addNamespaceSink;
            }

            public Sink<Namespace, SailException> getMinus()
            {
                return subtractNamespaceSink;
            }
        };

        cmtSink = new DiffSink<String, SailException>()
        {
            public Sink<String, SailException> getPlus()
            {
                return addCommentSink;
            }

            public Sink<String, SailException> getMinus()
            {
                return subtractCommentSink;
            }
        };
    }

    public RDFSink<SailException> adderSink() {
        return addSink;
    }

    public RDFSink<SailException> subtractorSink() {
        return subtractSink;
    }


    public DiffSink<Statement, SailException> statementSink()
    {
        return stSink;
    }

    public DiffSink<Namespace, SailException> namespaceSink()
    {
        return nsSink;
    }

    public DiffSink<String, SailException> commentSink()
    {
        return cmtSink;
    }
}
