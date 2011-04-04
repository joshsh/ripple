/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiffContextFilter.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.rdf.RDFSink;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class RDFDiffContextFilter<E extends Exception> implements RDFDiffSink<E>
{
	private final Map<Resource, RDFDiffCollector<E>> contextToCollectorMap;
    private final RDFSink<E> addSink;
    private final RDFSink<E> subSink;
    private final DiffSink<Statement, E> stSink;
    private final DiffSink<Namespace, E> nsSink;
    private final DiffSink<String, E> cmtSink;

    public RDFDiffContextFilter()
	{
		contextToCollectorMap = new HashMap<Resource, RDFDiffCollector<E>>();

        final Sink<Statement, E> stAddSink = new Sink<Statement, E>()
        {
            public void put( final Statement st ) throws E
            {
                Resource context = st.getContext();

                RDFDiffCollector<E> sink = contextToCollectorMap.get( context );
                if ( null == sink )
                {
                    sink = new RDFDiffCollector<E>();
                    contextToCollectorMap.put( context, sink );
                }

                sink.adderSink().statementSink().put( st );
            }
        };

        final Sink<Statement, E> stSubSink = new Sink<Statement, E>()
        {
            public void put( final Statement st ) throws E
            {
                Resource context = st.getContext();

                RDFDiffCollector<E> sink = contextToCollectorMap.get( context );
                if ( null == sink )
                {
                    sink = new RDFDiffCollector<E>();
                    contextToCollectorMap.put( context, sink );
                }

                sink.subtractorSink().statementSink().put( st );
            }
        };

        final Sink<Namespace, E> nsAddSink = new Sink<Namespace, E>()
        {
            public void put( final Namespace ns ) throws E
            {
                contextToCollectorMap.get( null ).adderSink().namespaceSink().put( ns );
            }
        };

        final Sink<Namespace, E> nsSubSink = new Sink<Namespace, E>()
        {
            public void put( final Namespace ns ) throws E
            {
                contextToCollectorMap.get( null ).subtractorSink().namespaceSink().put( ns );
            }
        };

        final Sink<String, E> cmtAddSink = new Sink<String, E>()
        {
            public void put( final String comment ) throws E
            {
                contextToCollectorMap.get( null ).adderSink().commentSink().put( comment );
            }
        };

        final Sink<String, E> cmtSubSink = new Sink<String, E>()
        {
            public void put( final String comment ) throws E
            {
                contextToCollectorMap.get( null ).subtractorSink().commentSink().put( comment );
            }
        };

        addSink = new RDFSink<E>()
        {
            public Sink<Statement, E> statementSink()
            {
                return stAddSink;
            }

            public Sink<Namespace, E> namespaceSink()
            {
                return nsAddSink;
            }

            public Sink<String, E> commentSink()
            {
                return cmtAddSink;
            }
        };

        subSink = new RDFSink<E>()
        {
            public Sink<Statement, E> statementSink()
            {
                return stSubSink;
            }

            public Sink<Namespace, E> namespaceSink()
            {
                return nsSubSink;
            }

            public Sink<String, E> commentSink()
            {
                return cmtSubSink;
            }
        };

        stSink = new DiffSink<Statement, E>()
        {
            public Sink<Statement, E> getPlus()
            {
                return stAddSink;
            }

            public Sink<Statement, E> getMinus()
            {
                return stSubSink;
            }
        };

        nsSink = new DiffSink<Namespace, E>()
        {
            public Sink<Namespace, E> getPlus()
            {
                return nsAddSink;
            }

            public Sink<Namespace, E> getMinus()
            {
                return nsSubSink;
            }
        };

        cmtSink = new DiffSink<String, E>()
        {
            public Sink<String, E> getPlus()
            {
                return cmtAddSink;
            }

            public Sink<String, E> getMinus()
            {
                return cmtSubSink;
            }
        };

        clear();
	}

	public Iterator<Resource> contextIterator()
	{
		return contextToCollectorMap.keySet().iterator();
	}

	public RDFDiffSource<E> sourceForContext( final Resource context )
	{
		return contextToCollectorMap.get( context );
	}

	public void clear()
	{
		contextToCollectorMap.clear();

		RDFDiffCollector<E> nullCollector = new RDFDiffCollector<E>();
		contextToCollectorMap.put( null, nullCollector );
	}

	public void clearContext( final Resource context )
	{
		contextToCollectorMap.remove( context );
	}

	public RDFSink<E> adderSink()
	{
		return addSink;
	}

	public RDFSink<E> subtractorSink()
	{
		return subSink;
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
}

