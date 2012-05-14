/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * A pipeline which enforces one data item at a time, regardless of the number of threads writing into the sink.
 * It is thread-safe but protects downstream components which may not be thread-safe.
 * @param <T> the type of data being passed
 */
public class SynchronizedSink<T> implements Sink<T>
{
	private final Object mutex;
	private final Sink<T> sink;

    /**
     * Constructs a new synchronized sink using a specified mutex
     * @param other the downstream sink which is to be protected
     * @param mutex a mutex to use for synchronization
     */
	public SynchronizedSink( final Sink<T> other, final Object mutex )
	{
		sink = other;
		this.mutex = mutex;
	}

    /**
     * Constructs a new synchronized sink using an internal mutex
     * @param other the downstream sink which is to be protected
     */
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

    /**
     * @return the mutex which this pipeline uses for synchronization
     */
    public Object getMutex()
    {
        return mutex;
    }
}
