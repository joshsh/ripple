/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

public class Buffer<T> extends Collector<T>
{
	private final Sink<T> sink;

	public Buffer( final Sink<T> sink )
	{
		super();

		this.sink = sink;
	}

	public void flush() throws RippleException
	{
		writeTo( sink );

		clear();
	}
}
