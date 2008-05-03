/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.neo4j;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

// kate: tab-width 4
/**
 * A collection of hooks into selected web services.
 */
public class Neo4jLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/neo4j#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put( NS, getClass().getResource( "neo4j.ttl" ) + "#" );

        registerPrimitive( GetProperty.class, NS + "getProperty", context );
	}
}
