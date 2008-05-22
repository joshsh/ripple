/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.services;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of hooks into selected web services.
 */
public class ServicesLibrary extends Library
{
    public static final String
            NS_2008_06 = "http://fortytwo.net/2008/06/ripple/services#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/services#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put( NS_2008_06, getClass().getResource( "services.ttl" ) + "#" );

		registerPrimitive( PingTheSemanticWeb.class, context );
		registerPrimitive( SwoogleIt.class, context );
		registerPrimitive( Uriqr.class, context );
	}
}

