/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.ListNode;

public abstract class RippleList extends ListNode<RippleValue> implements RippleValue
{
	protected RippleValue first;
	protected RippleList rest;

	public RippleValue getFirst()
	{
		return first;
	}

	public RippleList getRest()
	{
		return rest;
	}

	public int length()
	{
		int l = 0;

		RippleList cur = this;
		while ( !cur.isNil() )
		{
			l++;
			cur = cur.getRest();
		}

		return l;
	}
	
	public RippleValue get( final int i )
		throws RippleException
	{
		if ( i < 0 )
		{
			throw new RippleException( "list index out of bounds: " + i );
		}

		RippleList cur = this;
		for ( int j = 0; j < i; j++ )
		{
			if ( cur.isNil() )
			{
				throw new RippleException( "list index out of bounds: " + i );
			}

			cur = cur.getRest();
		}

		return cur.getFirst();
	}
	
	public abstract RippleList push( RippleValue v ) throws RippleException;
    public abstract boolean isNil();
}
