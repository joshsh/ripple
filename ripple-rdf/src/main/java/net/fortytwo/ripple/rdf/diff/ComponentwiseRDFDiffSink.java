package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.flow.DiffSink;
import net.fortytwo.ripple.flow.Sink;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 9:00:56 PM
 */
public class ComponentwiseRDFDiffSink<E extends Exception> implements RDFDiffSink<E>
{
    private final RDFSink<E> addSink;
    private final RDFSink<E> subSink;
    private final DiffSink<Statement, E> stSink;
    private final DiffSink<Namespace, E> nsSink;
    private final DiffSink<String, E> cmtSink;

    public ComponentwiseRDFDiffSink( final RDFSink<E> addSink,
                                     final RDFSink<E> subSink )
    {
        this.addSink = addSink;
        this.subSink = subSink;

        this.stSink = new DiffSink<Statement, E>()
        {
            public Sink<Statement, E> getPlus()
            {
                return addSink.statementSink();
            }

            public Sink<Statement, E> getMinus()
            {
                return subSink.statementSink();
            }
        };

        this.nsSink = new DiffSink<Namespace, E>()
        {
            public Sink<Namespace, E> getPlus()
            {
                return addSink.namespaceSink();
            }

            public Sink<Namespace, E> getMinus()
            {
                return subSink.namespaceSink();
            }
        };

        this.cmtSink = new DiffSink<String, E>()
        {
            public Sink<String, E> getPlus()
            {
                return addSink.commentSink();
            }

            public Sink<String, E> getMinus()
            {
                return subSink.commentSink();
            }
        };
    }

    // TODO: construct from statement, namespace, and comment DiffSinks

    public RDFSink<E> adderSink()
    {
        return addSink;
    }

    public RDFSink<E> subtractorSink()
    {
        return subSink;
    }

    public DiffSink<Statement, E> statementSink()
    {
        return stSink;
    }

    public DiffSink<Namespace, E> namespaceSink()
    {
        return nsSink;
    }

    public DiffSink<String, E> commentSink()
    {
        return cmtSink;
    }
}
