/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/CloseableIterationSource.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import info.aduna.iteration.CloseableIteration;

public class CloseableIterationSource<T, E extends Exception> implements Source<T, E>
{
	private CloseableIteration<T, E> iteration;
	
	public CloseableIterationSource( final CloseableIteration<T, E> iter )
	{
		iteration = iter;
	}
	
	public void writeTo( final Sink<T, E> sink ) throws E
	{
		if ( null == iteration)
		{
			throw new IllegalStateException( "CloseableIterationSource may only be written once" );
		}

        try
        {
            while ( iteration.hasNext() )
            {
                sink.put( iteration.next() );
            }
        }

        finally
        {
            close();
        }
    }
	
	private void close() throws E
	{
		if ( null != iteration)
        {
            try
            {
                iteration.close();
            }

            finally
            {
                iteration = null;
            }
        }
	}
}
