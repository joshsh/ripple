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
import net.fortytwo.ripple.URIMap;

/**
 * A collection of miscellaneous primitives.
 */
public class EtcLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/etc#";

    private static PrimitiveStackMapping invertVal;

    public void load( final URIMap uf, final ModelConnection mc )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "etc.ttl" ) + "#" );

        registerPrimitive( DateTimeToMillis.class, NS + "dateTimeToMillis", mc );
        registerPrimitive( Get.class, NS + "get", mc );
        invertVal = registerPrimitive( Invert.class, NS + "invert", mc );
		registerPrimitive( Time.class, NS + "time", mc );
	}

    public static PrimitiveStackMapping getInvertValue()
    {
        return invertVal;
    }
}

// kate: tab-width 4
