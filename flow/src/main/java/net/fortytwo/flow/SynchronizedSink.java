/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow;

public class SynchronizedSink<T, E extends Exception> implements Sink<T, E>
{
	private final Object mutex;
	private final Sink<T, E> sink;

	public SynchronizedSink( final Sink<T, E> other, final Object mutex )
	{
		sink = other;
		this.mutex = mutex;
	}
	
	public SynchronizedSink( final Sink<T, E> other )
	{
		this( other, other );
	}

	public void put( final T t ) throws E
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
