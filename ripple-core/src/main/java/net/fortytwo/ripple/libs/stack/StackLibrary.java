/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
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
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/stack#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/stack#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/stack#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/stack#";

	// Special values.
	private static PrimitiveStackMapping
            optionApplyVal,
            plusApplyVal,
            rangeApplyVal,
            starApplyVal,
            timesApplyVal;

    public void load( final URIMap uf,
                      final LibraryLoader.Context context )
		throws RippleException
	{
		uf.put( NS_2008_08, getClass().getResource( "stack.ttl" ) + "#" );

		// Stack shuffling primitives
        registerPrimitives( context,
                Dup.class,
                Dupd.class,
                Id.class,
                Pop.class,
                Popd.class,
                Rolldown.class,
                Rolldownd.class,
                Rollup.class,
                Rollupd.class,
                Rotate.class,
                Rotated.class,
                Swap.class,
                Swapd.class );

		// Application primitives
		registerPrimitives( context,
                Apply.class,
                Ary.class,
                Dip.class,
                Dipd.class );
		optionApplyVal = registerPrimitive( OptionApply.class, context );
		plusApplyVal = registerPrimitive( PlusApply.class, context );
		starApplyVal = registerPrimitive( StarApply.class, context );
		rangeApplyVal = registerPrimitive( RangeApply.class, context );
		timesApplyVal = registerPrimitive( TimesApply.class, context );

		// List primitives.
		registerPrimitives( context,
                At.class,
                Cat.class,
                Cons.class,
                Drop.class,
                Empty.class,
                Fold.class,
                Has.class,
                In.class,
                Map.class,
                Max.class,
                Min.class,
                Of.class,
                Size.class,
                Swons.class,
                Take.class,
                Uncons.class,
                Unswons.class );
	}

	////////////////////////////////////////////////////////////////////////////

	public static PrimitiveStackMapping getOptionApplyValue()
	{
		return optionApplyVal;
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

