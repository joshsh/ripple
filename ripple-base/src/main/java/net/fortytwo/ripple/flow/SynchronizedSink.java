/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.flow;

public class SynchronizedSink<T, E extends Exception> implements Sink<T, E>
{
	private Object synch;
	private Sink<T, E> sink;

	public SynchronizedSink( final Sink<T, E> other, final Object synch )
	{
		sink = other;
		this.synch = synch;
	}
	
	public SynchronizedSink( final Sink<T, E> other )
	{
		this( other, other );
	}

	public void put( final T t ) throws E
	{
		synchronized ( synch )
		{
			sink.put( t );
		}
	}
}
