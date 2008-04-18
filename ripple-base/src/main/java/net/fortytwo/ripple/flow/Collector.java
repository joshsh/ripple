/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.flow;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * Note: while this class is not actually thread-safe, put() may safely be
 * called while writeTo() is in progress.
 */
public class Collector<T, E extends Exception> implements Sink<T, E>, Source<T, E>
{
	private Node first, last;
	private int count;

	public Collector()
	{
		clear();
	}

	public void put( final T t ) throws E
	{
		Node n = new Node( t, null );

		if ( null == first )
		{
			first = n;
		}

		else
		{
			last.next = n;
		}

		last = n;
		count++;
	}

    public void writeTo( final Sink<T, E> sink ) throws E
	{
		Node cur = first;
		while ( null != cur )
		{
            // Any new items which are put() as a result of this call will
            // eventually be passed into the sink as well (even if clear()
            // is called).
            sink.put( cur.value );

            cur = cur.next;
		}
	}

    public int size()
	{
		return count;
	}

	public Iterator<T> iterator()
	{
		return new NodeIterator( first );
	}

    public List<T> asList()
    {
        List<T> list = new LinkedList<T>();
        Node cur = first;
        while ( null != cur )
        {
            list.add( cur.value );
            cur = cur.next;
        }

        return list;
    }

    public void clear()
	{
		first = null;
		count = 0;
	}

	////////////////////////////////////////////////////////////////////////////

	private class Node
	{
		public T value;
		public Node next;

		public Node( final T value, final Node next )
		{
			this.value = value;
			this.next = next;
		}
	}

	private class NodeIterator implements Iterator<T>
	{
		private Node cur;

		public NodeIterator( final Node first )
		{
			cur = first;
		}

		public boolean hasNext()
		{
			return null != cur;
		}

		public T next()
		{
			T value = cur.value;
			cur = cur.next;
			return value;
		}

		public void remove()
		{
		}
	}
}
