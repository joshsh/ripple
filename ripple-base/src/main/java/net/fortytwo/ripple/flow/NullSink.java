/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.flow;

public class NullSink<T, E extends Exception> implements Sink<T, E>
{
	public void put( final T t ) throws E
	{
	}
}
