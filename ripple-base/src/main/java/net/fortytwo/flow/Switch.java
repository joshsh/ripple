/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.flow;

public class Switch<T, E extends Exception> implements Sink<T, E>
{
	private final Sink<T, E> left, right;
	private boolean state;

	public Switch( final Sink<T, E> left, final Sink<T, E> right )
	{
		this.left = left;
		this.right = right;
		state = true;
	}

	public void put( final T t ) throws E
	{
		( ( state ) ? left : right ).put( t );
	}

	public void flip()
	{
		state = !state;
	}
}
