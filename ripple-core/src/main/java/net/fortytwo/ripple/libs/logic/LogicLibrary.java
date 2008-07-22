/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of primitives for boolean logic and conditionals.
 */
public class LogicLibrary extends Library
{
    public static final String
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/logic#";

    public void load( final URIMap uf,
                      final LibraryLoader.Context context )
		throws RippleException
	{
		uf.put( NS_2008_08, getClass().getResource( "logic.ttl" ) + "#" );

		registerPrimitive( And.class, context );
		registerPrimitive( Branch.class, context );
		registerPrimitive( Choice.class, context );
		registerPrimitive( Ifte.class, context );
		registerPrimitive( Not.class, context );
        registerPrimitive( Or.class, context );
        registerPrimitive( While.class, context );
		registerPrimitive( Xor.class, context );
    }
}