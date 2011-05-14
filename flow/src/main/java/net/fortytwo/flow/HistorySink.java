/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow;

import java.util.LinkedList;

/**
 * A <code>Sink</code> which maintains a history of items received during any given interval between calls to
 * its <code>advance</code> method.
 * It has a limited capacity, remembering only the latest <code>k</code> intervals.
 *
 * @param <T> the class of pipeline items
 * @param <E> the class of exceptions thrown
 */
public class HistorySink<T, E extends Exception> implements Sink<T, E>
{
    private final int capacity;
    private final LinkedList<Collector<T, E>> history;

    private int len;
	private Collector<T, E> current;

	public HistorySink( final int capacity )
	{
		if ( capacity < 1 )
		{
			throw new IllegalArgumentException();
		}

        this.capacity = capacity;
        history = new LinkedList<Collector<T, E>>();

		len = 0;
	}

	public void advance()
	{
		current = new Collector<T, E>();
		history.addFirst( current );

		len++;
		if ( len > capacity)
		{
			history.removeLast();
		}
	}

	public void put( final T t ) throws E
	{
		current.put( t );
	}

	public Source<T, E> get(final int index) throws E
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
