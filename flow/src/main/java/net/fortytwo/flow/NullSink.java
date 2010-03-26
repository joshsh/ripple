/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow;

public class NullSink<T, E extends Exception> implements Sink<T, E>
{
	public void put( final T t ) throws E
	{
	}
}
