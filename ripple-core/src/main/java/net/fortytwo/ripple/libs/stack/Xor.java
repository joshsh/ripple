/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes two Boolean values and produces the result of
 * their exclusive logical disjunction.
 */
public class Xor extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

	public Xor()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		RippleList stack = arg.getStack();
		RippleValue x, y;

		x = stack.getFirst();
		stack = stack.getRest();
		y = stack.getFirst();
		stack = stack.getRest();

		RippleValue trueValue = StackLibrary.getTrueValue();

		// Note: everything apart from joy:true is considered false.
		boolean xb = ( 0 == x.compareTo( trueValue ) );
		boolean yb = ( 0 == y.compareTo( trueValue ) );
		RippleValue result = ( ( xb && !yb ) || ( !xb && yb ) )
			? trueValue
			: StackLibrary.getFalseValue();

		sink.put( arg.with(
				stack.push( result ) ) );
	}
}

// kate: tab-width 4
