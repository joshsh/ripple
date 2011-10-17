/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

public class Tee<T> implements Sink<T>
{
	private final Sink<T> sinkA, sinkB;

	public Tee( final Sink<T> sinkA, final Sink<T> sinkB )
	{
		this.sinkA = sinkA;
		this.sinkB = sinkB;
	}

	public void put( final  T t ) throws RippleException
	{
		sinkA.put( t );
		sinkB.put( t );
	}
}
