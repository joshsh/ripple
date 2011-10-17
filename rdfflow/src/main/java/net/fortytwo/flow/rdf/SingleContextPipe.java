/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/SingleContextPipe.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.Resource;
import org.openrdf.model.ValueFactory;

import net.fortytwo.flow.Sink;

public class SingleContextPipe implements RDFSink {
	private final Sink<Statement> stSink;
	private final Sink<Namespace> nsSink;
	private final Sink<String> cmtSink;

	public SingleContextPipe( final RDFSink sink,
                              final Resource context,
                              final ValueFactory valueFactory )
	{
		final Sink<Statement> otherStSink = sink.statementSink();

		stSink = new Sink<Statement>()
		{
			public void put( final Statement st ) throws RippleException
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

	public Sink<Statement> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String> commentSink()
	{
		return cmtSink;
	}
}

