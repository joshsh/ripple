/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;

/**
 * An RdfSink which passes its input into an RDFHandler.
 */
public class SesameOutputAdapter implements RDFSink
{
	private RDFHandler handler;

	private final Sink<Statement, RippleException> stSink;
	private final Sink<Namespace, RippleException> nsSink;
	private final Sink<String, RippleException> cmtSink;

	public SesameOutputAdapter( final RDFHandler handler )
	{
		this.handler = handler;

		stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
				try
				{
					handler.handleStatement( st );
				}
		
				catch ( Throwable t )
				{
					throw new RippleException( t );
				}
			}
		};

		nsSink = new Sink<Namespace, RippleException>()
		{
			public void put( final Namespace ns ) throws RippleException
			{
				try
				{
					handler.handleNamespace( ns.getPrefix(), ns.getName() );
				}
		
				catch ( Throwable t )
				{
					throw new RippleException( t );
				}
			}
		};

		cmtSink = new Sink<String, RippleException>()
		{
			public void put( final String comment ) throws RippleException
			{
				try
				{
					handler.handleComment( comment );
				}
		
				catch ( Throwable t )
				{
					throw new RippleException( t );
				}
			}
		};
	}

	public void startRDF() throws RippleException
	{
		try
		{
			handler.startRDF();
		}

		catch ( org.openrdf.rio.RDFHandlerException e )
		{
			throw new RippleException( e );
		}
	}

	public void endRDF() throws RippleException
	{
		try
		{
			handler.endRDF();
		}

		catch ( org.openrdf.rio.RDFHandlerException e )
		{
			throw new RippleException( e );
		}
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

