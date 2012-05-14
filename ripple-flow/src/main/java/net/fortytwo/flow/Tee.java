/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * A "tee" pipeline which passes data to both of two downstream sinks.
 * @param <T> the type of data being passed
 */
public class Tee<T> implements Sink<T>
{
	private final Sink<T> left, right;

    /**
     * Constructs a new tee using the given sinks
     * @param left one of the two downstream sinks
     * @param right the other of the two downstream sinks
     */
	public Tee( final Sink<T> left, final Sink<T> right)
	{
		this.left = left;
		this.right = right;
	}

    /**
     * Receives the next data item
     * @param t the data item being passed
     * @throws RippleException if a data handling error occurs
     */
	public void put( final  T t ) throws RippleException
	{
		left.put(t);
		right.put(t);
	}
}
