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
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of primitives for boolean logic and conditionals.
 */
public class LogicLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/logic#";

	// Special values.
    // TODO: forcing these to be static values prohibits concurrent Ripple models, and makes sequential Ripple models (as in the unit tests) problematic
    private static RippleValue branchVal, trueVal, falseVal;

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put( NS, getClass().getResource( "logic.ttl" ) + "#" );

		registerPrimitive( And.class, NS + "and", context );
		branchVal = registerPrimitive( Branch.class, NS + "branch", context );
		registerPrimitive( Choice.class, NS + "choice", context );
		falseVal = registerPrimitive( False.class, NS + "false", context );
		registerPrimitive( Ifte.class, NS + "ifte", context );
		registerPrimitive( Not.class, NS + "not", context );
		registerPrimitive( Or.class, NS + "or", context );
		trueVal = registerPrimitive( True.class, NS + "true", context );
		registerPrimitive( Xor.class, NS + "xor", context );
	}

	////////////////////////////////////////////////////////////////////////////

	public static RippleValue getBranchValue()
	{
		return branchVal;
	}

	public static RippleValue getTrueValue()
	{
		return trueVal;
	}

	public static RippleValue getFalseValue()
	{
		return falseVal;
	}
}