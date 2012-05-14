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
 * A sink which simply throws away all data passed into it.
 * @param <T>
 */
public class NullSink<T> implements Sink<T>
{
	public void put( final T t ) throws RippleException
	{
        // Do nothing.
	}
}
