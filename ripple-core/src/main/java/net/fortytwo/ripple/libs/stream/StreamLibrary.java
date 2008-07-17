/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of data flow primitives.
 */
public class StreamLibrary extends Library
{
    public static final String
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/stream#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/stream#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/stream#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put( NS_2008_08, getClass().getResource( "stream.ttl" ) + "#" );

        registerPrimitive( Both.class, context );
		registerPrimitive( Each.class, context );
		registerPrimitive( Intersect.class, context );
		registerPrimitive( Limit.class, context );
        registerPrimitive( Require.class, context );
        registerPrimitive( Scrap.class, context );
		registerPrimitive( Unique.class, context );
	}
}

