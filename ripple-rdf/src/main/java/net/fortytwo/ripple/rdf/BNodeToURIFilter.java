/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
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
public class BNodeToURIFilter implements RDFSink
{
	private Sink<Statement, RippleException> stSink;
	private Sink<Namespace, RippleException> nsSink;
	private Sink<String, RippleException> cmtSink;

	private ValueFactory valueFactory;

	public BNodeToURIFilter( final RDFSink sink, final ValueFactory vf )
	{
		valueFactory = vf;

		final Sink<Statement, RippleException> destStSink = sink.statementSink();

		stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
				boolean s = st.getSubject() instanceof BNode;
				boolean o = st.getObject() instanceof BNode;
				boolean c = ( null == st.getContext() ) ? false : st.getContext() instanceof BNode;

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
		return valueFactory.createURI( Ripple.URN_BNODE_PREFIX + bnode.getID() );
	}
	
	public Sink<Statement, RippleException> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace, RippleException> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String, RippleException> commentSink()
	{
		return cmtSink;
	}
}
