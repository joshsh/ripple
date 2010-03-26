/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow;

public class Buffer<T, E extends Exception> extends Collector<T, E>
{
	private final Sink<T, E> sink;

	public Buffer( final Sink<T, E> sink )
	{
		super();

		this.sink = sink;
	}

	public void flush() throws E
	{
		writeTo( sink );

		clear();
	}
}
