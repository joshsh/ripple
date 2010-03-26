/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.RDFTee;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.Sink;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public class RDFDiffTee<E extends Exception> implements RDFDiffSink<E>
{
	private final RDFTee<E> adderTee, subtractorTee;
    private final DiffSink<Statement, E> stSink;
    private final DiffSink<Namespace, E> nsSink;
    private final DiffSink<String, E> cmtSink;

    public RDFDiffTee( final RDFDiffSink<E> sinkA, final RDFDiffSink<E> sinkB )
	{
		adderTee = new RDFTee<E>( sinkA.adderSink(), sinkB.adderSink() );
		subtractorTee = new RDFTee<E>( sinkA.subtractorSink(), sinkB.subtractorSink() );

        stSink = new DiffSink<Statement, E>()
        {
            public Sink<Statement, E> getPlus()
            {
                return adderTee.statementSink();
            }

            public Sink<Statement, E> getMinus()
            {
                return subtractorTee.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace, E>()
        {
            public Sink<Namespace, E> getPlus()
            {
                return adderTee.namespaceSink();
            }

            public Sink<Namespace, E> getMinus()
            {
                return subtractorTee.namespaceSink();
            }
        };

        cmtSink = new DiffSink<String, E>()
        {
            public Sink<String, E> getPlus()
            {
                return adderTee.commentSink();
            }

            public Sink<String, E> getMinus()
            {
                return subtractorTee.commentSink();
            }
        };
    }

	public RDFSink<E> adderSink()
	{
		return adderTee;
	}

	public RDFSink<E> subtractorSink()
	{
		return subtractorTee;
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

