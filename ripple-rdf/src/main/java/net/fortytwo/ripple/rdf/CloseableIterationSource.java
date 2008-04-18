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
import net.fortytwo.ripple.flow.Source;
import info.aduna.iteration.CloseableIteration;

public class CloseableIterationSource<T, E extends Exception> implements Source<T, RippleException>
{
	private CloseableIteration<T, E> it;
	
	public CloseableIterationSource( final CloseableIteration<T, E> iter )
	{
		it = iter;
	}
	
	public void writeTo( final Sink<T, RippleException> sink ) throws RippleException
	{
		if ( null == it )
		{
			return;
		}

        try
        {
            while ( it.hasNext() )
            {
                sink.put( it.next() );
            }
        }

        // FIXME: sloppy...
        catch (Exception e)
        {
            if ( e instanceof RippleException )
            {
                throw (RippleException) e;
            }

            else
            {
                throw new RippleException( e );
            }
        }


        close();
	}
	
	private void close() throws RippleException
	{
		if ( null != it )
        {
            try {
                it.close();
            } catch (Exception e) {
                throw new RippleException(e);
            }
        }
		
        it = null;
	}
}
