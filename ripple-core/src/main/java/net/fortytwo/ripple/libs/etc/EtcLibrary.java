/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.etc;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of miscellaneous primitives.
 */
public class EtcLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/etc#";

    private static PrimitiveStackMapping invertVal;

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "etc.ttl" ) + "#" );

        registerPrimitive( DateTimeToMillis.class, NS + "dateTimeToMillis", context );
        registerPrimitive( Get.class, NS + "get", context );
        invertVal = registerPrimitive( Invert.class, NS + "invert", context );
		registerPrimitive( Time.class, NS + "time", context );
	}

    public static PrimitiveStackMapping getInvertValue()
    {
        return invertVal;
    }
}

// kate: tab-width 4
