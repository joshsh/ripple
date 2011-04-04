/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiff.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.NullDiffSink;
import net.fortytwo.flow.rdf.RDFCollector;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.RDFSource;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public class RDFDiff<E extends Exception> implements RDFDiffSink<E>, RDFDiffSource<E>
{
	private final RDFCollector<E> added, subtracted;

    private final DiffSink<Statement, E> stSink;
    private final DiffSink<Namespace, E> nsSink;
    private final DiffSink<String, E> cmtSink;

    public RDFDiff()
	{
		added = new RDFCollector<E>();
		subtracted = new RDFCollector<E>();

        stSink = new DiffSink<Statement, E>()
        {
            public Sink<Statement, E> getPlus()
            {
                return added.statementSink();
            }

            public Sink<Statement, E> getMinus()
            {
                return subtracted.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace, E>()
        {
            public Sink<Namespace, E> getPlus()
            {
                return added.namespaceSink();
            }

            public Sink<Namespace, E> getMinus()
            {
                return subtracted.namespaceSink();
            }
        };

        cmtSink = new NullDiffSink<String, E>();
    }

    public DiffSink<Statement, E> statementSink() {
        return stSink;
    }

    public DiffSink<Namespace, E> namespaceSink() {
        return nsSink;
    }

    public DiffSink<String, E> commentSink() {
        return cmtSink;
    }

    public RDFSink<E> adderSink()
	{
		return added;
	}

	public RDFSink<E> subtractorSink()
	{
		return subtracted;
	}

    public RDFSource<E> adderSource()
	{
		return added;
	}

	public RDFSource<E> subtractorSource()
	{
		return subtracted;
	}

	public void writeTo( final RDFDiffSink<E> sink ) throws E
	{
        try {
            added.writeTo( sink.adderSink() );
            subtracted.writeTo( sink.subtractorSink() );
        } catch (Exception e) {
            throw (E) e;
        }
    }
}

