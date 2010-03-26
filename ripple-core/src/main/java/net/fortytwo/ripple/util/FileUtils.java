/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Collection;
import java.util.LinkedList;

import net.fortytwo.ripple.RippleException;

public final class FileUtils
{
	public static Collection<String> getLines( final InputStream is )
		throws RippleException
	{
		LinkedList<String> lines = new LinkedList<String>();

		try
		{
			BufferedReader reader = new BufferedReader(
				new InputStreamReader( is ) );
	
			// Break out when end of stream is reached.
			while ( true )
			{
				String line = reader.readLine();
	
				if ( null == line )
				{
					break;
				}

				line = line.trim();
	
				if ( !line.startsWith( "#" ) && !line.equals( "" ) )
				{
					lines.add( line );
				}
			}
		}

		catch ( java.io.IOException e )
		{
			throw new RippleException( e );
		}

		return lines;
	}

	private FileUtils()
	{
	}
}

