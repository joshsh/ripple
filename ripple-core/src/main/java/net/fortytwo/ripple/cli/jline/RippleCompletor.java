package net.fortytwo.ripple.cli.jline;

import jline.console.completer.Completer;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class RippleCompletor implements Completer
{
	private final SortedSet<String> alts;

	public RippleCompletor( final Collection<String> alternatives )
	{
		alts = new TreeSet<String>();

        for (String alternative : alternatives) {
            alts.add(alternative);
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

        for (String can : matches) {
            if (!(can.startsWith(pre))) {
                break;
            } else {
                clist.add(can);
                foundMatches = true;
            }
        }

		return foundMatches ? startIndex : -1;
	}
}

