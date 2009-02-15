/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.diff.DiffSink;
import net.fortytwo.ripple.rdf.RDFSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

public final class RDFDiffContextFilter implements RDFDiffSink<RippleException>
{
	private final Map<Resource, RDFDiffCollector<RippleException>> contextToCollectorMap;
    private final RDFSink<RippleException> addSink;
    private final RDFSink<RippleException> subSink;
    private final DiffSink<Statement, RippleException> stSink;
    private final DiffSink<Namespace, RippleException> nsSink;
    private final DiffSink<String, RippleException> cmtSink;

    public RDFDiffContextFilter()
	{
		contextToCollectorMap = new HashMap<Resource, RDFDiffCollector<RippleException>>();

        final Sink<Statement, RippleException> stAddSink = new Sink<Statement, RippleException>()
        {
            public void put( final Statement st ) throws RippleException
            {
                Resource context = st.getContext();

                RDFDiffCollector<RippleException> sink = contextToCollectorMap.get( context );
                if ( null == sink )
                {
                    sink = new RDFDiffCollector<RippleException>();
                    contextToCollectorMap.put( context, sink );
                }

                sink.adderSink().statementSink().put( st );
            }
        };

        final Sink<Statement, RippleException> stSubSink = new Sink<Statement, RippleException>()
        {
            public void put( final Statement st ) throws RippleException
            {
                Resource context = st.getContext();

                RDFDiffCollector<RippleException> sink = contextToCollectorMap.get( context );
                if ( null == sink )
                {
                    sink = new RDFDiffCollector<RippleException>();
                    contextToCollectorMap.put( context, sink );
                }

                sink.subtractorSink().statementSink().put( st );
            }
        };

        final Sink<Namespace, RippleException> nsAddSink = new Sink<Namespace, RippleException>()
        {
            public void put( final Namespace ns ) throws RippleException
            {
                contextToCollectorMap.get( null ).adderSink().namespaceSink().put( ns );
            }
        };

        final Sink<Namespace, RippleException> nsSubSink = new Sink<Namespace, RippleException>()
        {
            public void put( final Namespace ns ) throws RippleException
            {
                contextToCollectorMap.get( null ).subtractorSink().namespaceSink().put( ns );
            }
        };

        final Sink<String, RippleException> cmtAddSink = new Sink<String, RippleException>()
        {
            public void put( final String comment ) throws RippleException
            {
                contextToCollectorMap.get( null ).adderSink().commentSink().put( comment );
            }
        };

        final Sink<String, RippleException> cmtSubSink = new Sink<String, RippleException>()
        {
            public void put( final String comment ) throws RippleException
            {
                contextToCollectorMap.get( null ).subtractorSink().commentSink().put( comment );
            }
        };

        addSink = new RDFSink<RippleException>()
        {
            public Sink<Statement, RippleException> statementSink()
            {
                return stAddSink;
            }

            public Sink<Namespace, RippleException> namespaceSink()
            {
                return nsAddSink;
            }

            public Sink<String, RippleException> commentSink()
            {
                return cmtAddSink;
            }
        };

        subSink = new RDFSink<RippleException>()
        {
            public Sink<Statement, RippleException> statementSink()
            {
                return stSubSink;
            }

            public Sink<Namespace, RippleException> namespaceSink()
            {
                return nsSubSink;
            }

            public Sink<String, RippleException> commentSink()
            {
                return cmtSubSink;
            }
        };

        stSink = new DiffSink<Statement, RippleException>()
        {
            public Sink<Statement, RippleException> getPlus()
            {
                return stAddSink;
            }

            public Sink<Statement, RippleException> getMinus()
            {
                return stSubSink;
            }
        };

        nsSink = new DiffSink<Namespace, RippleException>()
        {
            public Sink<Namespace, RippleException> getPlus()
            {
                return nsAddSink;
            }

            public Sink<Namespace, RippleException> getMinus()
            {
                return nsSubSink;
            }
        };

        cmtSink = new DiffSink<String, RippleException>()
        {
            public Sink<String, RippleException> getPlus()
            {
                return cmtAddSink;
            }

            public Sink<String, RippleException> getMinus()
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

	public RDFDiffSource<RippleException> sourceForContext( final Resource context )
	{
		return contextToCollectorMap.get( context );
	}

	public void clear()
	{
		contextToCollectorMap.clear();

		RDFDiffCollector<RippleException> nullCollector = new RDFDiffCollector<RippleException>();
		contextToCollectorMap.put( null, nullCollector );
	}

	public void clearContext( final Resource context )
	{
		contextToCollectorMap.remove( context );
	}

	public RDFSink<RippleException> adderSink()
	{
		return addSink;
	}

	public RDFSink<RippleException> subtractorSink()
	{
		return subSink;
	}

    public DiffSink<Statement, RippleException> statementSink() {
        return stSink;
    }

    public DiffSink<Namespace, RippleException> namespaceSink() {
        return nsSink;
    }

    public DiffSink<String, RippleException> commentSink() {
        return cmtSink;
    }
}

