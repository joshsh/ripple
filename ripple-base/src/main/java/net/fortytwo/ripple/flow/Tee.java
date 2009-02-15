/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.flow;

public class Tee<T, E extends Exception> implements Sink<T, E>
{
	private final Sink<T, E> sinkA, sinkB;

	public Tee( final Sink<T, E> sinkA, final Sink<T, E> sinkB )
	{
		this.sinkA = sinkA;
		this.sinkB = sinkB;
	}

	public void put( final  T t ) throws E
	{
		sinkA.put( t );
		sinkB.put( t );
	}
}
