/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

public class SynchronizedSink<T> implements Sink<T>
{
	private final Object mutex;
	private final Sink<T> sink;

	public SynchronizedSink( final Sink<T> other, final Object mutex )
	{
		sink = other;
		this.mutex = mutex;
	}
	
	public SynchronizedSink( final Sink<T> other )
	{
		this( other, other );
	}

	public void put( final T t ) throws RippleException
	{
		synchronized (mutex)
		{
			sink.put( t );
		}
	}

    public Object getMutex()
    {
        return mutex;
    }
}
