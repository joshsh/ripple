/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

import java.util.HashSet;
import java.util.Set;

public class DistinctFilter<T> implements Sink<T>
{
	private final Set<T> set = new HashSet<T>();
	private final Sink<T> sink;

	public DistinctFilter( final Sink<T> sink )
	{
		this.sink = sink;
	}

	public void put( final T t ) throws RippleException
	{
		if ( set.add( t ) )
		{
			sink.put( t );
		}
	}
}
