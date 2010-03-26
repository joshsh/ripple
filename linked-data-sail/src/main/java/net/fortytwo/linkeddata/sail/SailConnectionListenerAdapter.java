/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.rdf.diff.RDFDiffSink;

import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.sail.SailConnectionListener;

public class SailConnectionListenerAdapter implements SailConnectionListener
{
	private static final Logger LOGGER
		= Logger.getLogger( SailConnectionListenerAdapter.class );
	
	private final Sink<Statement, RippleException> addSink, subSink;
	
	public SailConnectionListenerAdapter( final RDFDiffSink diffSink )
	{
		addSink = diffSink.adderSink().statementSink();
		subSink = diffSink.subtractorSink().statementSink();
	}
	
	public void statementAdded( final Statement st )
	{
		try
		{
			addSink.put( st );
		}
		
		catch ( RippleException e )
		{
			LOGGER.warn( "Unhandled exception", e );
		}
	}

	public void statementRemoved( final Statement st )
	{
		try
		{
			subSink.put( st );
		}
		
		catch ( RippleException e )
		{
			LOGGER.warn( "Unhandled exception", e );
		}
	}

}
