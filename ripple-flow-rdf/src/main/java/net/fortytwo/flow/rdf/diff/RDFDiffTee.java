/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiffTee.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.RDFTee;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.Sink;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public class RDFDiffTee implements RDFDiffSink {
	private final RDFTee adderTee, subtractorTee;
    private final DiffSink<Statement> stSink;
    private final DiffSink<Namespace> nsSink;
    private final DiffSink<String> cmtSink;

    public RDFDiffTee( final RDFDiffSink sinkA, final RDFDiffSink sinkB )
	{
		adderTee = new RDFTee( sinkA.adderSink(), sinkB.adderSink() );
		subtractorTee = new RDFTee( sinkA.subtractorSink(), sinkB.subtractorSink() );

        stSink = new DiffSink<Statement>()
        {
            public Sink<Statement> getPlus()
            {
                return adderTee.statementSink();
            }

            public Sink<Statement> getMinus()
            {
                return subtractorTee.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace>()
        {
            public Sink<Namespace> getPlus()
            {
                return adderTee.namespaceSink();
            }

            public Sink<Namespace> getMinus()
            {
                return subtractorTee.namespaceSink();
            }
        };

        cmtSink = new DiffSink<String>()
        {
            public Sink<String> getPlus()
            {
                return adderTee.commentSink();
            }

            public Sink<String> getMinus()
            {
                return subtractorTee.commentSink();
            }
        };
    }

	public RDFSink adderSink()
	{
		return adderTee;
	}

	public RDFSink subtractorSink()
	{
		return subtractorTee;
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
}

