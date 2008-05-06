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
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of stack manipulation primitives.  Compare with Joy and other
 * functional stack languages.
 */
public class StackLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/stack#";

	// Some special values.
	private static RippleValue branchVal, trueVal, falseVal;
	private static PrimitiveStackMapping optApplyVal, starApplyVal, plusApplyVal, timesApplyVal, rangeApplyVal;

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "stack.ttl" ) + "#" );

		// Stack shuffling primitives
		registerPrimitive( Dup.class, NS + "dup", context );
		registerPrimitive( Dupd.class, NS + "dupd", context );
		registerPrimitive( Dupdd.class, NS + "dupdd", context );
		registerPrimitive( Id.class, NS + "id", context );
		registerPrimitive( Pop.class, NS + "pop", context );
		registerPrimitive( Popd.class, NS + "popd", context );
		registerPrimitive( Popdd.class, NS + "popdd", context );
		registerPrimitive( Rolldown.class, NS + "rolldown", context );
		registerPrimitive( Rolldownd.class, NS + "rolldownd", context );
		registerPrimitive( Rollup.class, NS + "rollup", context );
		registerPrimitive( Rollupd.class, NS + "rollupd", context );
		registerPrimitive( Rotate.class, NS + "rotate", context );
		registerPrimitive( Rotated.class, NS + "rotated", context );
		registerPrimitive( Swap.class, NS + "swap", context );
		registerPrimitive( Swapd.class, NS + "swapd", context );
		registerPrimitive( Swapdd.class, NS + "swapdd", context );

		// Boolean logic and conditionals
		registerPrimitive( And.class, NS + "and", context );
		branchVal = registerPrimitive( Branch.class, NS + "branch", context );
		registerPrimitive( Choice.class, NS + "choice", context );
		falseVal = registerPrimitive( False.class, NS + "false", context );
		registerPrimitive( Ifte.class, NS + "ifte", context );
		registerPrimitive( Not.class, NS + "not", context );
		registerPrimitive( Or.class, NS + "or", context );
		trueVal = registerPrimitive( True.class, NS + "true", context );
		registerPrimitive( Xor.class, NS + "xor", context );
		
		// Application primitives
		registerPrimitive( Apply.class, NS + "apply", context );
		registerPrimitive( Ary.class, NS + "ary", context );
		registerPrimitive( Dip.class, NS + "dip", context );
		registerPrimitive( Dipd.class, NS + "dipd", context );
		optApplyVal = registerPrimitive( OptApply.class, NS + "optApply", context );
		plusApplyVal = registerPrimitive( PlusApply.class, NS + "plusApply", context );
		starApplyVal = registerPrimitive( StarApply.class, NS + "starApply", context );
		rangeApplyVal = registerPrimitive( RangeApply.class, NS + "rangeApply", context );
		timesApplyVal = registerPrimitive( TimesApply.class, NS + "timesApply", context );

		// List primitives.
		registerPrimitive( At.class, NS + "at", context );
		registerPrimitive( Cat.class, NS + "cat", context );
		registerPrimitive( Cons.class, NS + "cons", context );
		registerPrimitive( Fold.class, NS + "fold", context );
		registerPrimitive( Has.class, NS + "has", context );
		registerPrimitive( In.class, NS + "in", context );
		registerPrimitive( Max.class, NS + "max", context );
		registerPrimitive( Min.class, NS + "min", context );
		registerPrimitive( Of.class, NS + "of", context );
		registerPrimitive( Size.class, NS + "size", context );
		registerPrimitive( Swons.class, NS + "swons", context );
		registerPrimitive( Uncons.class, NS + "uncons", context );
		registerPrimitive( Unswons.class, NS + "unswons", context );
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

