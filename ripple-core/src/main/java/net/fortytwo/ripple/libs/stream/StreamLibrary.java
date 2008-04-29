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
import net.fortytwo.ripple.URIMap;

/**
 * A collection of data flow primitives.
 */
public class StreamLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/stream#";

	public void load( final URIMap uf, final ModelConnection mc )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "stream.ttl" ) + "#" );

        registerPrimitive( Both.class, NS + "both", mc );
		registerPrimitive( Each.class, NS + "each", mc );
		registerPrimitive( Intersect.class, NS + "intersect", mc );
		registerPrimitive( Limit.class, NS + "limit", mc );
        registerPrimitive( Require.class, NS + "require", mc );
        registerPrimitive( Scrap.class, NS + "scrap", mc );
		registerPrimitive( Unique.class, NS + "unique", mc );
	}
}

// kate: tab-width 4
