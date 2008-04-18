/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import java.io.InputStream;

import java.util.Collection;
import java.util.Iterator;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.util.FileUtils;

public class LibraryLoader extends ClassLoader
{
	public LibraryLoader()
	{
		super( LibraryLoader.class.getClassLoader() );
	}

	public void load( final URIMap uf, final ModelConnection mc )
		throws RippleException
	{
		for ( Iterator<String> namesIter = getNames().iterator();
			namesIter.hasNext(); )
		{
			String className = namesIter.next();

			Class c;
			Library ext;

			try
			{
				c = loadClass( className );
			}

			catch ( ClassNotFoundException e )
			{
				throw new RippleException( e );
			}

			try
			{
				ext = (Library) c.newInstance();
			}

			catch ( InstantiationException e )
			{
				throw new RippleException( e );
			}

			catch ( IllegalAccessException e )
			{
				throw new RippleException( e );
			}

			ext.load( uf, mc );
		}
	}

	private Collection<String> getNames() throws RippleException
	{
		try
		{
			InputStream is
				= net.fortytwo.ripple.Ripple.class.getResourceAsStream(
					"libraries.txt" );
			Collection<String> names = FileUtils.getLines( is );
			is.close();

			return names;
		}

		catch ( java.io.IOException e )
		{
			throw new RippleException( e );
		}
	}
}

// kate: tab-width 4
