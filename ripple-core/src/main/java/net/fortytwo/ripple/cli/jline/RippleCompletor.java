/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.jline;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jline.Completor;

public abstract class RippleCompletor implements Completor
{
	private final SortedSet<String> alts;

	public RippleCompletor( final Collection<String> alternatives )
	{
		alts = new TreeSet<String>();

		for ( Iterator<String> i = alternatives.iterator(); i.hasNext(); )
		{
			alts.add( i.next() );
		}
	}

	protected abstract int findStartIndex( String s );

	public int complete( final String buffer,
						final int cursor,
						final List clist )
	{
		String start = ( null == buffer ) ? "" : buffer;

		// A cursor more than one character beyond the end of the buffer doesn't
		// make sense (does it?)
		if ( cursor > start.length() )
		{
			return -1;
		}

		// Allow replacement before the end of input only if the cursor is on a
		// (single-character) delimiter.
		if ( cursor < start.length() )
		{
			String tail = start.substring( cursor, cursor + 1 );
			if ( findStartIndex( tail ) <= 0 )
			{
				return -1;
			}

			else
			{
				start = ( cursor > 0 )
					? start.substring( 0, cursor )
					: "";
			}
		}

		int startIndex = findStartIndex( start );

		if ( startIndex < 0 )
		{
			return -1;
		}

		String pre = ( start.length() == startIndex )
			? "" : start.substring( startIndex );

		SortedSet<String> matches = alts.tailSet( pre );
		boolean foundMatches = false;

		for ( Iterator<String> i = matches.iterator(); i.hasNext(); )
		{
			String can = i.next();
	
			if ( !( can.startsWith( pre ) ) )
			{
				break;
			}

			else
			{
				clist.add( can );
				foundMatches = true;
			}
		}

		return foundMatches ? startIndex : -1;
	}
}

