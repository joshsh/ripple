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
import java.util.LinkedHashMap;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.util.FileUtils;
import org.openrdf.model.Value;

public class LibraryLoader extends ClassLoader
{
    private static final String LIBRARIES_TXT = "libraries.txt";

    public LibraryLoader()
	{
		super( LibraryLoader.class.getClassLoader() );
	}

	public SpecialValueMap load( final URIMap uriMap, final ModelConnection mc )
		throws RippleException
	{
        Context specialValues = new Context( mc );

        for ( String className : getNames() )
		{
			Class c;
			Library library;

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
				library = (Library) c.newInstance();
			}

			catch ( InstantiationException e )
			{
				throw new RippleException( e );
			}

			catch ( IllegalAccessException e )
			{
				throw new RippleException( e );
			}

			library.load( uriMap, specialValues );
		}

        return specialValues.createSpecialValueMap();
    }

	private Collection<String> getNames() throws RippleException
	{
		try
		{
            Collection<String> names;
			InputStream is = net.fortytwo.ripple.Ripple.class.getResourceAsStream(
					LIBRARIES_TXT );

            try
            {
                names = FileUtils.getLines( is );
            }

            finally
            {
                is.close();
            }

            return names;
		}

		catch ( java.io.IOException e )
		{
			throw new RippleException( e );
		}
	}

    public class Context
    {
        // Note: LinkedHashMap is used because the order of added values is
        // significant.
        private final LinkedHashMap<Value, RippleValue>
                primaryMap,
                aliasMap;

        private final ModelConnection modelConnection;

        public Context( final ModelConnection mc )
        {
            this.modelConnection = mc;
            primaryMap = new LinkedHashMap<Value, RippleValue>();
            aliasMap = new LinkedHashMap<Value, RippleValue>();
        }

        public ModelConnection getModelConnection()
        {
            return modelConnection;
        }

        public void addPrimaryValue( final Value v, final RippleValue rv )
        {
            primaryMap.put(v, rv);
        }

        public void addAlias( final Value v, final RippleValue rv )
        {
            aliasMap.put( v, rv );
        }

        public SpecialValueMap createSpecialValueMap()
        {
            SpecialValueMap map = new SpecialValueMap();

            // Primary values are added first, so that they have priority
            // over aliases.
            for ( Value key : primaryMap.keySet() )
            {
                map.put( key, primaryMap.get( key ) );
            }

            // Aliases are added second.
            for ( Value key : aliasMap.keySet() )
            {
                map.put( key, aliasMap.get( key ) );
            }

            return map;
        }
    }
}
