/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/SynchronizedRDFDiffSink.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.SynchronizedRDFSink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.Sink;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public class SynchronizedRDFDiffSink implements RDFDiffSink {
	private final SynchronizedRDFSink addSink, subSink;
    private final DiffSink<Statement> stSink;
    private final DiffSink<Namespace> nsSink;
    private final DiffSink<String> cmtSink;

    public SynchronizedRDFDiffSink( final RDFDiffSink sink, final Object mutex )
	{
		addSink = new SynchronizedRDFSink( sink.adderSink(), mutex );
		subSink = new SynchronizedRDFSink( sink.subtractorSink(), mutex );

        stSink = new DiffSink<Statement>()
        {
            public Sink<Statement> getPlus()
            {
                return addSink.statementSink();
            }

            public Sink<Statement> getMinus()
            {
                return subSink.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace>()
        {
            public Sink<Namespace> getPlus()
            {
                return addSink.namespaceSink();
            }

            public Sink<Namespace> getMinus()
            {
                return subSink.namespaceSink();
            }
        };

        cmtSink = new DiffSink<String>()
        {
            public Sink<String> getPlus()
            {
                return addSink.commentSink();
            }

            public Sink<String> getMinus()
            {
                return subSink.commentSink();
            }
        };
	}

	public RDFSink adderSink()
	{
		return addSink;
	}

	public RDFSink subtractorSink()
	{
		return subSink;
	}

    public DiffSink<Statement> statementSink()
    {
        return stSink;
    }

    public DiffSink<Namespace> namespaceSink()
    {
        return nsSink;
    }

    public DiffSink<String> commentSink()
    {
        return cmtSink;
    }

    public Object getMutex()
    {
        return addSink.getMutex();
    }
}

