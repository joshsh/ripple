/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of stack manipulation primitives.  Compare with Joy and other
 * functional stack languages.
 */
public class StackLibrary extends Library
{
    public static final String
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/stack#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/stack#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/stack#";

	// Special values.
	private static PrimitiveStackMapping optApplyVal, starApplyVal, plusApplyVal, timesApplyVal, rangeApplyVal;

    public void load( final URIMap uf,
                      final LibraryLoader.Context context )
		throws RippleException
	{
		uf.put( NS_2008_08, getClass().getResource( "stack.ttl" ) + "#" );

		// Stack shuffling primitives
		registerPrimitive( Dup.class, context );
		registerPrimitive( Dupd.class, context );
		registerPrimitive( Dupdd.class, context );
		registerPrimitive( Id.class, context );
		registerPrimitive( Pop.class, context );
		registerPrimitive( Popd.class, context );
		registerPrimitive( Popdd.class, context );
		registerPrimitive( Rolldown.class, context );
		registerPrimitive( Rolldownd.class, context );
		registerPrimitive( Rollup.class, context );
		registerPrimitive( Rollupd.class, context );
		registerPrimitive( Rotate.class, context );
		registerPrimitive( Rotated.class, context );
		registerPrimitive( Swap.class, context );
		registerPrimitive( Swapd.class, context );
		registerPrimitive( Swapdd.class, context );

		// Application primitives
		registerPrimitive( Apply.class, context );
		registerPrimitive( Ary.class, context );
		registerPrimitive( Dip.class, context );
		registerPrimitive( Dipd.class, context );
		optApplyVal = registerPrimitive( OptApply.class, context );
		plusApplyVal = registerPrimitive( PlusApply.class, context );
		starApplyVal = registerPrimitive( StarApply.class, context );
		rangeApplyVal = registerPrimitive( RangeApply.class, context );
		timesApplyVal = registerPrimitive( TimesApply.class, context );

		// List primitives.
		registerPrimitive( At.class, context );
		registerPrimitive( Cat.class, context );
        registerPrimitive( Cons.class, context );
        registerPrimitive( Empty.class, context );
		registerPrimitive( Fold.class, context );
		registerPrimitive( Has.class, context );
		registerPrimitive( In.class, context );
        registerPrimitive( Map.class, context );
        registerPrimitive( Max.class, context );
		registerPrimitive( Min.class, context );
		registerPrimitive( Of.class, context );
		registerPrimitive( Size.class, context );
		registerPrimitive( Swons.class, context );
		registerPrimitive( Uncons.class, context );
		registerPrimitive( Unswons.class, context );
	}

	////////////////////////////////////////////////////////////////////////////

	public static PrimitiveStackMapping getOptApplyValue()
	{
		return optApplyVal;
	}

	public static PrimitiveStackMapping getStarApplyValue()
	{
		return starApplyVal;
	}

	public static PrimitiveStackMapping getPlusApplyValue()
	{
		return plusApplyVal;
	}

	public static PrimitiveStackMapping getTimesApplyValue()
	{
		return timesApplyVal;
	}

	public static PrimitiveStackMapping getRangeApplyValue()
	{
		return rangeApplyVal;
	}
}

