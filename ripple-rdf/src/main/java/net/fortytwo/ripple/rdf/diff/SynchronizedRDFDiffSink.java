/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.SynchronizedRDFSink;
import net.fortytwo.ripple.flow.DiffSink;
import net.fortytwo.ripple.flow.Sink;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public class SynchronizedRDFDiffSink<E extends Exception> implements RDFDiffSink<E>
{
	private final SynchronizedRDFSink<E> addSink, subSink;
    private final DiffSink<Statement, E> stSink;
    private final DiffSink<Namespace, E> nsSink;
    private final DiffSink<String, E> cmtSink;

    public SynchronizedRDFDiffSink( final RDFDiffSink<E> sink, final Object mutex )
	{
		addSink = new SynchronizedRDFSink<E>( sink.adderSink(), mutex );
		subSink = new SynchronizedRDFSink<E>( sink.subtractorSink(), mutex );

        stSink = new DiffSink<Statement, E>()
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

        nsSink = new DiffSink<Namespace, E>()
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

        cmtSink = new DiffSink<String, E>()
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

    public Object getMutex()
    {
        return addSink.getMutex();
    }
}

