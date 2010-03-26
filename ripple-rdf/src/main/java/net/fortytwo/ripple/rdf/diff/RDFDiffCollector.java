/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.NullDiffSink;
import net.fortytwo.ripple.rdf.RDFCollector;
import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.RDFSource;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public class RDFDiffCollector<E extends Exception> implements RDFDiffSource<E>, RDFDiffSink<E>
{
	private final RDFCollector<E> adderCollector, subtractorCollector;

    private final DiffSink<Statement, E> stSink;
    private final DiffSink<Namespace, E> nsSink;
    private final DiffSink<String, E> cmtSink;

    public RDFDiffCollector()
	{
		adderCollector = new RDFCollector<E>();
		subtractorCollector = new RDFCollector<E>();

        stSink = new DiffSink<Statement, E>()
        {
            public Sink<Statement, E> getPlus()
            {
                return adderCollector.statementSink();
            }

            public Sink<Statement, E> getMinus()
            {
                return subtractorCollector.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace, E>()
        {
            public Sink<Namespace, E> getPlus()
            {
                return adderCollector.namespaceSink();
            }

            public Sink<Namespace, E> getMinus()
            {
                return subtractorCollector.namespaceSink();
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
		return adderCollector;
	}

	public RDFSink<E> subtractorSink()
	{
		return subtractorCollector;
	}

	public RDFSource<E> adderSource()
	{
		return adderCollector;
	}

	public RDFSource<E> subtractorSource()
	{
		return subtractorCollector;
	}

	public void writeTo( final RDFDiffSink<E> sink ) throws E
	{
        try {
            adderCollector.writeTo( sink.adderSink() );
            subtractorCollector.writeTo( sink.subtractorSink() );
        } catch (Exception e) {
            throw (E) e;
        }
	}

	public void clear()
	{
		adderCollector.clear();
		subtractorCollector.clear();
	}
}

