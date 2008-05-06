/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a resource and produces the resource again after
 * removing all statements in the context its description.  The next time a
 * description of the resource is needed, Ripple will request a new
 * representation.
 */
public class Forget extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

	public Forget()
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

		RippleValue v;

		v = stack.getFirst();

// FIXME: this should not subvert the ModelConnection
		mc.forget( v );
		
		// Note: the stack itself has not been altered.
		sink.put( arg );
	}
}

