/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

import java.util.LinkedList;

/**
 * A <code>Sink</code> which maintains a history of items received during any given interval between calls to
 * its <code>advance</code> method.
 * It has a limited capacity, remembering only the latest <code>k</code> intervals.
 *
 * @param <T> the class of pipeline items
 * @param <E> the class of exceptions thrown
 */
public class HistorySink<T> implements Sink<T>
{
    private final int capacity;
    private final LinkedList<Collector<T>> history;

    private int len;
	private Collector<T> current;

	public HistorySink( final int capacity )
	{
		if ( capacity < 1 )
		{
			throw new IllegalArgumentException();
		}

        this.capacity = capacity;
        history = new LinkedList<Collector<T>>();

		len = 0;
	}

	public void advance()
	{
		current = new Collector<T>();
		history.addFirst( current );

		len++;
		if ( len > capacity)
		{
			history.removeLast();
		}
	}

	public void put( final T t ) throws RippleException
	{
		current.put( t );
	}

	public Source<T> get(final int index)
	{
        if (index < 0) {
            throw new IllegalArgumentException();
        } else if (index >= history.size()) {
            return null;
        } else {
            return history.get(index);
        }
	}
}
