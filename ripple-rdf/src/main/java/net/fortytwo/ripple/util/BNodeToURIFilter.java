/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/BNodeToURIFilter.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.util;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.ripple.Ripple;
import org.openrdf.model.BNode;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;

/**
 * Author: josh
 * Date: Jan 18, 2008
 * Time: 12:52:56 PM
 */
public class BNodeToURIFilter<E extends Exception> implements RDFSink<E>
{
	private final Sink<Statement, E> stSink;
	private final Sink<Namespace, E> nsSink;
	private final Sink<String, E> cmtSink;

	private ValueFactory valueFactory;

	public BNodeToURIFilter( final RDFSink<E> sink, final ValueFactory vf )
	{
		valueFactory = vf;

		final Sink<Statement, E> destStSink = sink.statementSink();

		stSink = new Sink<Statement, E>()
		{
			public void put( final Statement st ) throws E
			{
				boolean s = st.getSubject() instanceof BNode;
				boolean o = st.getObject() instanceof BNode;
				boolean c = ( null != st.getContext() )
                        && st.getContext() instanceof BNode;

				if ( s || o || c )
				{
					Resource subj = s ? bnodeToUri( (BNode) st.getSubject() ) : st.getSubject();
					URI pred = st.getPredicate();
					Value obj = o ? bnodeToUri( (BNode) st.getObject() ) : st.getObject();
					Resource con = c ? bnodeToUri( (BNode) st.getContext() ) : st.getContext();

					Statement newSt = ( null == con )
							? valueFactory.createStatement( subj, pred, obj )
							: valueFactory.createStatement( subj, pred, obj, con );

					destStSink.put( newSt );
				}

				else
				{
					destStSink.put( st );
				}
			}
		};

		nsSink = sink.namespaceSink();
		cmtSink = sink.commentSink();
	}

	private URI bnodeToUri( final BNode bnode )
	{
		return valueFactory.createURI( Ripple.RANDOM_URN_PREFIX + bnode.getID() );
	}
	
	public Sink<Statement, E> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace, E> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String, E> commentSink()
	{
		return cmtSink;
	}
}
