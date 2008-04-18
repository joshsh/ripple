/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.flow;

public class NullSource<T, E extends Exception> implements Source<T, E>
{
	public void writeTo( Sink<T, E> sink ) throws E
	{
	}
}
