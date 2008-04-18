/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple;

// TODO: balancing operations

/**
 * A space-efficient map using where the key values are linked lists.
 */
public class ListMemoizer<T extends Comparable<T>, M>
{
	private T first;
	private M memo;

	private ListMemoizer<T, M> left, right;
	private ListMemoizer<T, M> rest;

	public ListMemoizer( final ListNode<T> list, final M memo )
	{
		if ( null == list )
		{
			throw new IllegalArgumentException( "the empty list cannot be memoized" );
		}

		first = list.getFirst();
		left = null;
		right = null;

		ListNode<T> r = list.getRest();
		if ( null == r )
		{
			rest = null;
			this.memo = memo;
		}

		else
		{
			rest = new ListMemoizer<T, M>( r, memo );
			this.memo = null;
		}
	}

	public boolean put( final ListNode<T> list, final M memo )
	{
		if ( null == list )
		{
			throw new IllegalArgumentException( "the empty list cannot be memoized" );
		}

		int cmp = this.first.compareTo( list.getFirst() );

		if ( 0 == cmp )
		{
			ListNode<T> r = list.getRest();

			if ( null == r )
			{
				if ( null == this.memo )
				{
					this.memo = memo;
					return true;
				}

				else
				{
					return false;
				}
			}

			else
			{
				if ( null == this.rest )
				{
					this.rest = new ListMemoizer<T, M>( r, memo );
					return true;
				}

				else
				{
					return this.rest.put( r, memo );
				}
			}
		}

		else if ( cmp < 0 )
		{
			if ( null == this.left )
			{
				this.left = new ListMemoizer<T, M>( list, memo );
				return true;
			}

			else
			{
				return this.left.put( list, memo );
			}
		}

		else
		{
			if ( null == this.right )
			{
				this.right = new ListMemoizer<T, M>( list, memo );
				return true;
			}

			else
			{
				return this.right.put( list, memo );
			}
		}
	}

	public M get( final ListNode<T> list )
	{
		if ( null == list )
		{
			return null;
		}

		int cmp = this.first.compareTo( list.getFirst() );

		if ( 0 == cmp )
		{
			ListNode<T> r = list.getRest();

			if ( null == r )
			{
				return this.memo;
			}

			else
			{
				return ( null == this.rest )
						? null
						: this.rest.get( r );
			}
		}

		else if ( cmp < 0 )
		{
			return ( null == this.left )
					? null
					: this.left.get( list );
		}

		else
		{
			return ( null == this.right )
					? null
					: this.right.get( list );
		}
	}

	////////////////////////////////////////////////////////////////////////////

	private int compare( final ListMemoizer<T, M> first,
						final ListMemoizer<T, M> second )
	{
		if ( null == first )
		{
			return ( null == second )
				? 0 : -1;
		}

		else if ( null == second )
		{
			return 1;
		}

		else
		{
			return first.compareTo( second );
		}
	}

	private int compareTo( final ListMemoizer<T, M> other )
	{
		int cmp = this.first.compareTo( other.first );

		if ( 0 != cmp )
		{
			return cmp;
		}

		cmp = compare( this.left, other.left );
		if ( 0 != cmp )
		{
			return cmp;
		}

		cmp = compare( this.rest, other.rest );
		if ( 0 != cmp )
		{
			return cmp;
		}

		cmp = compare( this.right, other.right );
		return cmp;
	}
}

// kate: tab-width 4
