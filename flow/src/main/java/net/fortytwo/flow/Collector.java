/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

import java.util.Iterator;

/**
 * Note: while this class is not actually thread-safe, put() may safely be
 * called while writeTo() is in progress.
 */
public class Collector<T> extends SimpleReadOnlyCollection<T> implements Sink<T>, Source<T>
{
	private Node first, last;
	private int count;

	public Collector()
	{
		clear();
	}

	public void put( final T t ) throws RippleException
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

    public void writeTo( final Sink<T> sink ) throws RippleException
	{
		Node cur = first;
		while ( null != cur )
		{
            // Any new items which are put() as a result of this call will
            // eventually be passed into the sink as well (even if clear()
            // is called).
            try {
                sink.put( cur.value );
            } catch (RippleException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            cur = cur.next;
		}
	}

    // java.util.Collection methods ////////////////////////////////////////////

    public int size()
	{
		return count;
	}

	public Iterator<T> iterator()
	{
		return new NodeIterator( first );
	}

    @Override
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
