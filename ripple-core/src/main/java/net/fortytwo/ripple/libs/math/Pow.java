/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes two numbers x and y and produces the number x to
 * the power of y.
 */
public class Pow extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

	public Pow()
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
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		NumericValue p, x, result;

		p = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();
		x = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = x.pow( p );

		sink.put( arg.with(
				stack.push( result ) ) );
	}
}

// kate: tab-width 4
