/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.neo4j;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of hooks into selected web services.
 */
public class Neo4jLibrary extends Library
{
	public static final String
            NS_2008_06 = "http://fortytwo.net/2008/06/ripple/neo4j#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
        // FIXME: no such file
        uf.put( NS_2008_06, getClass().getResource( "neo4j.ttl" ) + "#" );

        registerPrimitive( GetProperty.class, context );
	}
}
