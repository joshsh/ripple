/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiffContextFilter.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class RDFDiffContextFilter implements RDFDiffSink {
	private final Map<Resource, RDFDiffCollector> contextToCollectorMap;
    private final RDFSink addSink;
    private final RDFSink subSink;
    private final DiffSink<Statement> stSink;
    private final DiffSink<Namespace> nsSink;
    private final DiffSink<String> cmtSink;

    public RDFDiffContextFilter()
	{
		contextToCollectorMap = new HashMap<Resource, RDFDiffCollector>();

        final Sink<Statement> stAddSink = new Sink<Statement>()
        {
            public void put( final Statement st ) throws RippleException
            {
                Resource context = st.getContext();

                RDFDiffCollector sink = contextToCollectorMap.get( context );
                if ( null == sink )
                {
                    sink = new RDFDiffCollector();
                    contextToCollectorMap.put( context, sink );
                }

                sink.adderSink().statementSink().put( st );
            }
        };

        final Sink<Statement> stSubSink = new Sink<Statement>()
        {
            public void put( final Statement st ) throws RippleException
            {
                Resource context = st.getContext();

                RDFDiffCollector sink = contextToCollectorMap.get( context );
                if ( null == sink )
                {
                    sink = new RDFDiffCollector();
                    contextToCollectorMap.put( context, sink );
                }

                sink.subtractorSink().statementSink().put( st );
            }
        };

        final Sink<Namespace> nsAddSink = new Sink<Namespace>()
        {
            public void put( final Namespace ns ) throws RippleException
            {
                contextToCollectorMap.get( null ).adderSink().namespaceSink().put( ns );
            }
        };

        final Sink<Namespace> nsSubSink = new Sink<Namespace>()
        {
            public void put( final Namespace ns ) throws RippleException
            {
                contextToCollectorMap.get( null ).subtractorSink().namespaceSink().put( ns );
            }
        };

        final Sink<String> cmtAddSink = new Sink<String>()
        {
            public void put( final String comment ) throws RippleException
            {
                contextToCollectorMap.get( null ).adderSink().commentSink().put( comment );
            }
        };

        final Sink<String> cmtSubSink = new Sink<String>()
        {
            public void put( final String comment ) throws RippleException
            {
                contextToCollectorMap.get( null ).subtractorSink().commentSink().put( comment );
            }
        };

        addSink = new RDFSink()
        {
            public Sink<Statement> statementSink()
            {
                return stAddSink;
            }

            public Sink<Namespace> namespaceSink()
            {
                return nsAddSink;
            }

            public Sink<String> commentSink()
            {
                return cmtAddSink;
            }
        };

        subSink = new RDFSink()
        {
            public Sink<Statement> statementSink()
            {
                return stSubSink;
            }

            public Sink<Namespace> namespaceSink()
            {
                return nsSubSink;
            }

            public Sink<String> commentSink()
            {
                return cmtSubSink;
            }
        };

        stSink = new DiffSink<Statement>()
        {
            public Sink<Statement> getPlus()
            {
                return stAddSink;
            }

            public Sink<Statement> getMinus()
            {
                return stSubSink;
            }
        };

        nsSink = new DiffSink<Namespace>()
        {
            public Sink<Namespace> getPlus()
            {
                return nsAddSink;
            }

            public Sink<Namespace> getMinus()
            {
                return nsSubSink;
            }
        };

        cmtSink = new DiffSink<String>()
        {
            public Sink<String> getPlus()
            {
                return cmtAddSink;
            }

            public Sink<String> getMinus()
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

	public RDFDiffSource sourceForContext( final Resource context )
	{
		return contextToCollectorMap.get( context );
	}

	public void clear()
	{
		contextToCollectorMap.clear();

		RDFDiffCollector nullCollector = new RDFDiffCollector();
		contextToCollectorMap.put( null, nullCollector );
	}

	public void clearContext( final Resource context )
	{
		contextToCollectorMap.remove( context );
	}

	public RDFSink adderSink()
	{
		return addSink;
	}

	public RDFSink subtractorSink()
	{
		return subSink;
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

