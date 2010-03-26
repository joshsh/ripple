/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.RippleException;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.Resource;
import org.openrdf.model.ValueFactory;

import net.fortytwo.flow.Sink;

public class SingleContextPipe implements RDFSink<RippleException>
{
	private final Sink<Statement, RippleException> stSink;
	private final Sink<Namespace, RippleException> nsSink;
	private final Sink<String, RippleException> cmtSink;

	public SingleContextPipe( final RDFSink<RippleException> sink,
                              final Resource context,
                              final ValueFactory valueFactory )
	{
		final Sink<Statement, RippleException> otherStSink = sink.statementSink();

		stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
				Statement newSt;
				
				try
				{
					newSt = valueFactory.createStatement(
							st.getSubject(), st.getPredicate(), st.getObject(),	context );
				}
				
				catch ( Throwable t )
				{
					throw new RippleException( t );
				}
				
				otherStSink.put( newSt );
			}
		};

		nsSink = sink.namespaceSink();
		cmtSink = sink.commentSink();
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

