/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes two items and produces the second item.
 */
public class False extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

	public False()
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
		RippleValue y;
		RippleList stack = arg.getStack();

		stack = stack.getRest();
		y = stack.getFirst();
		stack = stack.getRest();

		sink.put( arg.with(
				stack.push( y ) ) );
	}
}

// kate: tab-width 4
