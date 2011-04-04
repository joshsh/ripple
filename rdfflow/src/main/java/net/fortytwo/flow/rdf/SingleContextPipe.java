/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/SingleContextPipe.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.Resource;
import org.openrdf.model.ValueFactory;

import net.fortytwo.flow.Sink;

public class SingleContextPipe<E extends Exception> implements RDFSink<E>
{
	private final Sink<Statement, E> stSink;
	private final Sink<Namespace, E> nsSink;
	private final Sink<String, E> cmtSink;

	public SingleContextPipe( final RDFSink<E> sink,
                              final Resource context,
                              final ValueFactory valueFactory )
	{
		final Sink<Statement, E> otherStSink = sink.statementSink();

		stSink = new Sink<Statement, E>()
		{
			public void put( final Statement st ) throws E
			{
				Statement newSt;

				newSt = valueFactory.createStatement(
						st.getSubject(), st.getPredicate(), st.getObject(),	context );
				
				otherStSink.put( newSt );
			}
		};

		nsSink = sink.namespaceSink();
		cmtSink = sink.commentSink();
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

