/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.jline;

import java.util.Collection;

public class DirectiveCompletor extends RippleCompletor
{
	private static final char [] DELIMITERS = { ' ', '\t', '\n', '\r' };

	public DirectiveCompletor( final Collection<String> alternatives )
	{
		super( alternatives );
	}

	private boolean isDelimiter( final char c )
	{
		for ( int i = 0; i < DELIMITERS.length; i++ )
		{
			if ( DELIMITERS[i] == c )
			{
				return true;
			}
		}

		return false;
	}

	protected int findStartIndex( final String s )
	{
		int index = 0;

		boolean ok = true;
		for ( int i = 0; i < s.length(); i++ )
		{
			char c = s.charAt( i );

			if ( isDelimiter( c ) )
			{
				if ( ok )
				{
					index = i + 1;
				}
			}

			else if ( '.' == c )
			{
				ok = true;
				index = i + 1;
			}

			else
			{
				ok = false;
			}
		}

		return index;
	}
}

// kate: tab-width 4
