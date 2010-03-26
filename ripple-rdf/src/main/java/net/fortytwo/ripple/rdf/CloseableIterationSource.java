/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import info.aduna.iteration.CloseableIteration;

public class CloseableIterationSource<T, E extends Exception> implements Source<T, RippleException>
{
	private CloseableIteration<T, E> iteration;
	
	public CloseableIterationSource( final CloseableIteration<T, E> iter )
	{
		iteration = iter;
	}
	
	public void writeTo( final Sink<T, RippleException> sink ) throws RippleException
	{
		if ( null == iteration)
		{
			throw new RippleException( "CloseableIterationSource may only be written once" );
		}

        try
        {
            while ( iteration.hasNext() )
            {
                sink.put( iteration.next() );
            }
        }

        catch ( Exception e )
        {
            throw ( e instanceof RippleException )
                    ? (RippleException) e
                    : new RippleException( e );
        }

        finally
        {
            close();
        }
    }
	
	private void close() throws RippleException
	{
		if ( null != iteration)
        {
            try
            {
                iteration.close();
            }

            catch ( Exception e )
            {
                throw new RippleException( e );
            }

            finally
            {
                iteration = null;
            }
        }
	}
}
