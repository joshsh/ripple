/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of data flow primitives.
 */
public class StreamLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/stream#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "stream.ttl" ) + "#" );

        registerPrimitive( Both.class, NS + "both", context );
		registerPrimitive( Each.class, NS + "each", context );
		registerPrimitive( Intersect.class, NS + "intersect", context );
		registerPrimitive( Limit.class, NS + "limit", context );
        registerPrimitive( Require.class, NS + "require", context );
        registerPrimitive( Scrap.class, NS + "scrap", context );
		registerPrimitive( Unique.class, NS + "unique", context );
	}
}

// kate: tab-width 4
