/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import info.aduna.iteration.CloseableIteration;

import java.util.HashSet;
import java.util.Set;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Buffer;
import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

public class BNodeClosureFilter implements Sink<Resource, RippleException>
{
	private Set<Resource> visited;
	private Sink<Statement, RippleException> sink;
	private SailConnection sailConnection;
	private Buffer<Resource, RippleException> buffer;

	public BNodeClosureFilter( final Sink<Statement, RippleException> sink, final SailConnection sc )
	{
		this.sink = sink;
		sailConnection = sc;
		buffer = new Buffer<Resource, RippleException>( this );
		visited = new HashSet<Resource>();
	}

	public void put( final Resource r ) throws RippleException
	{
		if ( visited.contains( r ) )
		{
			return;
		}

		else
		{
			visited.add( r );
		}

		try
		{
			CloseableIteration<? extends Statement, SailException> stmtIter
				= sailConnection.getStatements(
					r, null, null, Ripple.useInference() );

			while ( stmtIter.hasNext() )
			{
				Statement st = stmtIter.next();

				sink.put( st );

				// Traverse to any neighboring blank nodes (but not to URIs).
				Value obj = st.getObject();
				if ( isBlankNode( obj ) )
				{
					buffer.put( (Resource) obj );
				}
			}

			stmtIter.close();
		}

		catch ( RippleException e )
		{
			throw e;
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}

		// Note: the buffer may be written to before it has been completely read.
		buffer.flush();
	}
	
	private static boolean isBlankNode( final Value v )
	{
		if ( v instanceof Resource )
		{
			if ( v instanceof URI )
			{
				return ( (URI) v ).getNamespace().startsWith( Ripple.URN_BNODE_PREFIX );
			}
			
			else
			{
				// Note: assuming that all non-URI resources are blank nodes
				return true;
			}
		}
		
		else
		{
			return false;
		}
	}
}

// kate: tab-width 4
