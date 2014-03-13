package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;

import org.openrdf.model.Statement;
import org.openrdf.sail.SailConnectionListener;

import java.util.logging.Logger;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SailConnectionListenerAdapter implements SailConnectionListener
{
	private static final Logger LOGGER
		= Logger.getLogger( SailConnectionListenerAdapter.class.getName() );
	
	private final Sink<Statement> addSink, subSink;
	
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
			LOGGER.warning( "Unhandled exception" + e.getMessage() );
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
			LOGGER.warning( "Unhandled exception" + e.getMessage() );
		}
	}

}
