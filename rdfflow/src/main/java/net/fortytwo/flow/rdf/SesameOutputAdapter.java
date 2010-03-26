/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/SesameOutputAdapter.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;

/**
 * An RdfSink which passes its input into an RDFHandler.
 */
public class SesameOutputAdapter implements RDFSink<RDFHandlerException>
{
	private RDFHandler handler;

	private final Sink<Statement, RDFHandlerException> stSink;
	private final Sink<Namespace, RDFHandlerException> nsSink;
	private final Sink<String, RDFHandlerException> cmtSink;

	public SesameOutputAdapter( final RDFHandler handler )
	{
		this.handler = handler;

		stSink = new Sink<Statement, RDFHandlerException>()
		{
			public void put( final Statement st ) throws RDFHandlerException
			{
				handler.handleStatement( st );
			}
		};

		nsSink = new Sink<Namespace, RDFHandlerException>()
		{
			public void put( final Namespace ns ) throws RDFHandlerException
			{
				handler.handleNamespace( ns.getPrefix(), ns.getName() );
			}
		};

		cmtSink = new Sink<String, RDFHandlerException>()
		{
			public void put( final String comment ) throws RDFHandlerException
			{
				handler.handleComment( comment );
			}
		};
	}

	public void startRDF() throws RDFHandlerException
	{
		handler.startRDF();
	}

	public void endRDF() throws RDFHandlerException
	{
		handler.endRDF();
	}

	public Sink<Statement, RDFHandlerException> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace, RDFHandlerException> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String, RDFHandlerException> commentSink()
	{
		return cmtSink;
	}
}

