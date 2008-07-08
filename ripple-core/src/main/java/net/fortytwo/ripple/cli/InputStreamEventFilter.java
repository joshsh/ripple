/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli;

import java.io.InputStream;

public class InputStreamEventFilter extends InputStream
{
	private static final int ESC = 27;

	private final InputStream source;
	private final RecognizerAdapter recognizerAdapter;

	private int buffered;

	public InputStreamEventFilter( final InputStream is,
								   final RecognizerAdapter rc )
	{
		source = is;
		recognizerAdapter = rc;

		buffered = -1;
	}

	public int read() throws java.io.IOException
	{
		if ( -1 != buffered )
		{
			int tmp = buffered;
			buffered = -1;
			return tmp;
		}

		// Break out when an ordinary character or sequence is found
		while ( true )
		{
			int c = source.read();

			if ( ESC == c )
			{
				c = source.read();

				if ( ESC == c )
				{
					recognizerAdapter.putEvent( RecognizerEvent.ESCAPE );
				}

				else
				{
					buffered = c;
					return ESC;
				}
			}

			else
			{
				return c;
			}
		}
	}

	public int available() throws java.io.IOException
	{
		// Note: ESC characters may throw this number off in either direction.
		return source.available();
	}
}

