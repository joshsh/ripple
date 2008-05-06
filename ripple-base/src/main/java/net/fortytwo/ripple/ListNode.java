/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple;

/**
 * Head of a linked list.
 */
public abstract class ListNode<T>
{
	public abstract T getFirst();
	public abstract ListNode<T> getRest();
	
	public boolean equals( final ListNode<T> other )
	{
		ListNode<T> thisCur = this;
		ListNode<T> otherCur = other;
	
		while ( null != thisCur )
		{
/*System.out.println("RippleList.NIL = " + RippleList.NIL);
System.out.println("RippleList.NIL.getFirst() = " + RippleList.NIL.getFirst());
System.out.println("thisCur = " + thisCur + " " + (thisCur instanceof SesameList));
System.out.println("thisCur.getFirst() = " + thisCur.getFirst());
System.out.println("otherCur = " + otherCur + " " + (otherCur instanceof SesameList));
System.out.println("otherCur.getFirst() = " + otherCur.getFirst());*/
			if ( null == otherCur )
			{
				return false;
			}

			if ( !thisCur.getFirst().equals( otherCur.getFirst() ) )
			{
				return false;
			}
	
			thisCur = thisCur.getRest();
			otherCur = otherCur.getRest();
		}
	
		if ( null != otherCur )
		{
			return false;
		}
	
		return true;
	}
}

