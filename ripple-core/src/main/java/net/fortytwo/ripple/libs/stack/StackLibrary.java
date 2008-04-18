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

	public void load( final URIMap uf, final ModelConnection mc )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "stack.ttl" ) + "#" );

		// Stack shuffling primitives
		registerPrimitive( Dup.class, NS + "dup", mc );
		registerPrimitive( Dupd.class, NS + "dupd", mc );
		registerPrimitive( Dupdd.class, NS + "dupdd", mc );
		registerPrimitive( Id.class, NS + "id", mc );
		registerPrimitive( Pop.class, NS + "pop", mc );
		registerPrimitive( Popd.class, NS + "popd", mc );
		registerPrimitive( Popdd.class, NS + "popdd", mc );
		registerPrimitive( Rolldown.class, NS + "rolldown", mc );
		registerPrimitive( Rolldownd.class, NS + "rolldownd", mc );
		registerPrimitive( Rollup.class, NS + "rollup", mc );
		registerPrimitive( Rollupd.class, NS + "rollupd", mc );
		registerPrimitive( Rotate.class, NS + "rotate", mc );
		registerPrimitive( Rotated.class, NS + "rotated", mc );
		registerPrimitive( Swap.class, NS + "swap", mc );
		registerPrimitive( Swapd.class, NS + "swapd", mc );
		registerPrimitive( Swapdd.class, NS + "swapdd", mc );

		// Boolean logic and conditionals
		registerPrimitive( And.class, NS + "and", mc );
		branchVal = registerPrimitive( Branch.class, NS + "branch", mc );
		registerPrimitive( Choice.class, NS + "choice", mc );
		falseVal = registerPrimitive( False.class, NS + "false", mc );
		registerPrimitive( Ifte.class, NS + "ifte", mc );
		registerPrimitive( Not.class, NS + "not", mc );
		registerPrimitive( Or.class, NS + "or", mc );
		trueVal = registerPrimitive( True.class, NS + "true", mc );
		registerPrimitive( Xor.class, NS + "xor", mc );
		
		// Application primitives
		registerPrimitive( Apply.class, NS + "apply", mc );
		registerPrimitive( Ary.class, NS + "ary", mc );
		registerPrimitive( Dip.class, NS + "dip", mc );
		registerPrimitive( Dipd.class, NS + "dipd", mc );
		optApplyVal = registerPrimitive( OptApply.class, NS + "optApply", mc );
		plusApplyVal = registerPrimitive( PlusApply.class, NS + "plusApply", mc );
		starApplyVal = registerPrimitive( StarApply.class, NS + "starApply", mc );
		rangeApplyVal = registerPrimitive( RangeApply.class, NS + "rangeApply", mc );
		timesApplyVal = registerPrimitive( TimesApply.class, NS + "timesApply", mc );

		// List primitives.
		registerPrimitive( At.class, NS + "at", mc );
		registerPrimitive( Cat.class, NS + "cat", mc );
		registerPrimitive( Cons.class, NS + "cons", mc );
		registerPrimitive( Fold.class, NS + "fold", mc );
		registerPrimitive( Has.class, NS + "has", mc );
		registerPrimitive( In.class, NS + "in", mc );
		registerPrimitive( Max.class, NS + "max", mc );
		registerPrimitive( Min.class, NS + "min", mc );
		registerPrimitive( Of.class, NS + "of", mc );
		registerPrimitive( Size.class, NS + "size", mc );
		registerPrimitive( Swons.class, NS + "swons", mc );
		registerPrimitive( Uncons.class, NS + "uncons", mc );
		registerPrimitive( Unswons.class, NS + "unswons", mc );
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

// kate: tab-width 4
